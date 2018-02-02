package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.DoctorVideosListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DoctorVideos;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Shreshtha on 31-07-2017.
 */

public class DoctorVideoListFragment extends HealthCocoFragment implements View.OnClickListener,
        LocalDoInBackgroundListenerOptimised, TextWatcher, SwipeRefreshLayout.OnRefreshListener, LoadMorePageListener, AdapterView.OnItemClickListener {

    private ListViewLoadMore lvVideos;
    private User user;
    private ArrayList<DoctorVideos> doctorVideosList;
    private TextView tvNoVideos;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressLoading;
    private DoctorVideosListAdapter adapter;
    private boolean isEndOfListAchieved;
    private boolean isInitialLoading;
    private String lastTextSearched;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doctor_videos_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapters();
    }

    @Override
    public void initViews() {
        lvVideos = (ListViewLoadMore) view.findViewById(R.id.lv_videos);
        tvNoVideos = (TextView) view.findViewById(R.id.tv_no_videos);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue_action_bar);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        initEditSearchView(R.string.search_videos, this, this);
    }

    @Override
    public void initListeners() {
        lvVideos.setLoadMoreListener(this);
        lvVideos.setOnItemClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        lvVideos.setSwipeRefreshLayout(swipeRefreshLayout);
    }

    private void initAdapters() {
        adapter = new DoctorVideosListAdapter(mActivity);
        lvVideos.setAdapter(adapter);
    }

    private void getDataFromLocal() {
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_VIDEOS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    user = doctor.getUser();
                }
                break;
            case ADD_VIDEOS:
                LocalDataServiceImpl.getInstance(mApp).addVideos(user.getUniqueId(),
                        (ArrayList<DoctorVideos>) (ArrayList<?>) response.getDataList());
            case GET_VIDEOS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getVideosResponse(WebServiceType.GET_VIDEO, user.getUniqueId(), null, null);
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
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null && !Util.isNullOrBlank(user.getUniqueId()))
                        getDataFromLocal();
                    break;
                case GET_VIDEO:
                    if (response.isDataFromLocal()) {
                        doctorVideosList = (ArrayList<DoctorVideos>) (ArrayList<?>) response.getDataList();

                        if (!Util.isNullOrEmptyList(doctorVideosList)) {
                            LogUtils.LOGD(TAG, "Success onResponse list Size in page " + doctorVideosList.size());
                        }
                        if (!response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                            getVideoList(true);
                            return;
                        }
                        notifyAdapter(doctorVideosList);
                    } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_VIDEOS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    }
                    break;
            }
        }
        mActivity.hideLoading();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        Util.showToast(mActivity, errorMsg);
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        mActivity.hideLoading();
        Util.showToast(mActivity, R.string.user_offline);
    }

    private void getVideoList(boolean showLoading) {
        if (showLoading)
            mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).getVideos(DoctorVideos.class, WebServiceType.GET_VIDEO, user.getUniqueId(), this, this);
    }

    private void notifyAdapter(ArrayList<DoctorVideos> list) {
        if (!Util.isNullOrEmptyList(list)) {
            lvVideos.setVisibility(View.VISIBLE);
            tvNoVideos.setVisibility(View.GONE);
        } else {
            lvVideos.setVisibility(View.GONE);
            tvNoVideos.setVisibility(View.VISIBLE);
        }
        adapter.setListData(list);
        adapter.notifyDataSetChanged();
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
        if (!Util.isNullOrEmptyList(doctorVideosList)) {
            if (search.length() == 0) {
                tempList.addAll(doctorVideosList);
            } else {
                for (DoctorVideos doctorVideos : doctorVideosList) {
                    if (!Util.isNullOrBlank(doctorVideos.getName()) && doctorVideos.getName().toLowerCase(Locale.ENGLISH)
                            .contains(search)) {
                        tempList.add(doctorVideos);
                    }
                }
            }
        }
        notifyAdapter(tempList);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        getVideoList(false);
    }

    @Override
    public void loadMore() {
//        if (!isEndOfListAchieved && !isInitialLoading) {
//            PAGE_NUMBER++;
//            getListFromLocal(false);
//        }
    }

    @Override
    public boolean isEndOfListAchieved() {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Object object = parent.getAdapter().getItem(position);
            if (object instanceof DoctorVideos) {
                DoctorVideos doctorVideos = (DoctorVideos) object;
                openVideoViewActivity(CommonOpenUpFragmentType.PLAY_VIDEO, HealthCocoConstants.TAG_DOCTOR_VIDEO_DATA, doctorVideos,
                        HealthCocoConstants.REQUEST_CODE_PLAY_VIDEO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openVideoViewActivity(CommonOpenUpFragmentType fragmentType, String tag, Object intentData, int requestCode) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        if (!Util.isNullOrBlank(tag) && intentData != null)
            intent.putExtra(tag, Parcels.wrap(intentData));
        if (requestCode == 0)
            startActivity(intent);
        else
            startActivityForResult(intent, requestCode);
    }
}
