package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.AddVisitsActivity;
import com.healthcoco.healthcocopad.adapter.DiagramGridAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Diagram;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.DiagramsGridItemListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Locale;

public class DiagramsListFragment extends HealthCocoFragment implements OnItemClickListener,
        LocalDoInBackgroundListenerOptimised, DiagramsGridItemListener,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, TextWatcher {
    private static final int REQUEST_CODE_DIAGRAMS_LIST = 100;
    private GridView gvDiagrams;
    private ArrayList<Diagram> diagramsList;
    private DiagramGridAdapter adapter;
    private TextView tvNoDiagrams;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diagrams_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            initViews();
            initListeners();
            initAdapter();
            getListFromLocal();
        }
    }

    private void getListFromLocal() {
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DIAGRAMS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void getDiagramsList() {
        mActivity.showLoading(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DIAGRAMS);
        WebDataServiceImpl.getInstance(mApp).getDiagramsList(Diagram.class, latestUpdatedTime, user.getUniqueId(), this, this);
    }

    @Override
    public void initViews() {
        initEditSearchView(R.string.search_diagram, this);
        gvDiagrams = (GridView) view.findViewById(R.id.gv_diagrams);
        tvNoDiagrams = (TextView) view.findViewById(R.id.tv_no_diagrams);
    }

    @Override
    public void initListeners() {
        gvDiagrams.setOnItemClickListener(this);
    }

    private void initAdapter() {
        adapter = new DiagramGridAdapter(mActivity, this);
        gvDiagrams.setAdapter(adapter);
    }

    private void notifyAdapter(ArrayList<Diagram> list) {
        if (!Util.isNullOrEmptyList(list)) {
            gvDiagrams.setVisibility(View.VISIBLE);
            tvNoDiagrams.setVisibility(View.GONE);
        } else {
            gvDiagrams.setVisibility(View.GONE);
            tvNoDiagrams.setVisibility(View.VISIBLE);
        }
        adapter.setListData(list);
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Diagram selectedDiagram = adapter.getObject(position);
        if (selectedDiagram != null && !Util.isNullOrBlank(selectedDiagram.getDiagramUrl()))
            openDiagramDetailActivity(selectedDiagram);
    }

    private void openDiagramDetailActivity(Diagram diagram) {
        Intent intent = new Intent(mActivity, AddVisitsActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.SELECTED_DIAGRAM_DETAIL.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_DIAGRAM_TAG, diagram.getTags());
        intent.putExtra(HealthCocoConstants.TAG_SELECTED_DIAGRAM, Parcels.wrap(diagram));
        startActivityForResult(intent, REQUEST_CODE_DIAGRAMS_LIST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DIAGRAMS_LIST && resultCode == HealthCocoConstants.RESULT_CODE_DIAGRAM_DETAIL) {
            ((AddVisitsActivity) mActivity).setResult(HealthCocoConstants.RESULT_CODE_DIAGRAM_DETAIL, data);
            ((AddVisitsActivity) mActivity).finish();
        }
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        switch (response.getWebServiceType()) {
            case GET_DIAGRAMS_LIST:
                if (response.isDataFromLocal()) {
                    diagramsList = (ArrayList<Diagram>) (ArrayList<?>) response
                            .getDataList();
                    if (!Util.isNullOrEmptyList(diagramsList))
                        LogUtils.LOGD(TAG, "Success onResponse diagramsList Size " + diagramsList.size() + " isDataFromLocal " + response.isDataFromLocal());
                    notifyAdapter(diagramsList);
                    if (!response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                        getDiagramsList();
                        return;
                    }
                } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DIAGRAMS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    response.setIsFromLocalAfterApiSuccess(true);
                    LogUtils.LOGD(TAG, "Success onResponse diagramsList Size Total" + diagramsList.size() + " isDataFromLocal " + response.isDataFromLocal());
                    return;
                }
                break;

            default:
                break;
        }
        mActivity.hideLoading();
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_DIAGRAMS:
                LocalDataServiceImpl.getInstance(mApp).addDiagramsList(null, (ArrayList<Diagram>) (ArrayList<?>) response
                        .getDataList());
            case GET_DIAGRAMS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getGlobalDiagramsList(WebServiceType.GET_DIAGRAMS_LIST, null, null);
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String search = String.valueOf(s).toLowerCase(Locale.ENGLISH);
        ArrayList tempList = new ArrayList<>();
        if (!Util.isNullOrEmptyList(diagramsList)) {
            if (search.length() == 0) {
                tempList.addAll(diagramsList);
            } else {
                for (Diagram diagram : diagramsList) {
                    if (!Util.isNullOrBlank(diagram.getTags()) && diagram.getTags().toLowerCase(Locale.ENGLISH)
                            .contains(search)) {
                        tempList.add(diagram);
                    }
                }
            }
        }
        notifyAdapter(tempList);
    }

    @Override
    public void onErrorImageLoading(Diagram diagram) {
        if (diagramsList.contains(diagram)) {
            diagramsList.remove(diagram);
            notifyAdapter(diagramsList);
        }
    }
}
