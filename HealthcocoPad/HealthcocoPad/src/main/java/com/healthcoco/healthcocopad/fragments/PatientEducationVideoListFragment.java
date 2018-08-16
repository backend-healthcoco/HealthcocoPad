package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.PatientEducationVideosListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.PatientEducationVideo;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.UploadVideoDialogFragment;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.PatientVideoListItemClickListeners;
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

public class PatientEducationVideoListFragment extends HealthCocoFragment implements View.OnClickListener,
        LocalDoInBackgroundListenerOptimised, TextWatcher, SwipeRefreshLayout.OnRefreshListener, LoadMorePageListener, AdapterView.OnItemClickListener, PatientVideoListItemClickListeners {

    private ListViewLoadMore lvVideos;
    private GridView gvVideos;
    private User user;
    private ArrayList<PatientEducationVideo> educationVideoList;
    private TextView tvNoVideos;
    //    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressLoading;
    private PatientEducationVideosListAdapter adapter;
    private boolean isEndOfListAchieved;
    private boolean isInitialLoading;
    private String lastTextSearched;
    private FloatingActionButton floatingActionButton;
    private boolean isFromSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doctor_videos_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = mActivity.getIntent();
        isFromSettings = intent.getBooleanExtra(HealthCocoConstants.TAG_IS_FROM_SETTINGS, false);

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
        gvVideos = (GridView) view.findViewById(R.id.gv_videos);
        tvNoVideos = (TextView) view.findViewById(R.id.tv_no_videos);
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setColorSchemeResources(R.color.blue_action_bar);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fl_bt_add_video);

        initEditSearchView(R.string.search_videos, this, this);
        view.findViewById(R.id.bt_advance_search).setVisibility(View.GONE);

        if (isFromSettings)
            floatingActionButton.setVisibility(View.VISIBLE);
        else
            floatingActionButton.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
//        lvVideos.setLoadMoreListener(this);
        gvVideos.setOnItemClickListener(this);
        floatingActionButton.setOnClickListener(this);
//        swipeRefreshLayout.setOnRefreshListener(this);
//        lvVideos.setSwipeRefreshLayout(swipeRefreshLayout);
    }

    private void initAdapters() {
        adapter = new PatientEducationVideosListAdapter(mActivity, this);
        gvVideos.setAdapter(adapter);
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
//            case ADD_VIDEOS:
//                LocalDataServiceImpl.getInstance(mApp).addVideos(user.getUniqueId(),
//                        (ArrayList<DoctorVideos>) (ArrayList<?>) response.getDataList());
            case GET_VIDEOS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getEducationVideosResponse(WebServiceType.GET_VIDEO, user.getUniqueId(), isFromSetting(), null, null);
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
                        educationVideoList = (ArrayList<PatientEducationVideo>) (ArrayList<?>) response.getDataList();

                        if (!Util.isNullOrEmptyList(educationVideoList)) {
                            LogUtils.LOGD(TAG, "Success onResponse list Size in page " + educationVideoList.size());
                        }
//                        if (!response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                        getVideoList(true);
//                            return;
//                        }
                        notifyAdapter(educationVideoList);
                    } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_VIDEOS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    }
                    break;
            }
        }
        mActivity.hideLoading();
//        swipeRefreshLayout.setRefreshing(false);
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
      /*  if (showLoading)
            mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).getPatientEducationVideos(PatientEducationVideo.class, WebServiceType.GET_VIDEO, user.getUniqueId(), this, this);
*/
    }

    private void notifyAdapter(ArrayList<PatientEducationVideo> list) {
        if (!Util.isNullOrEmptyList(list)) {
            gvVideos.setVisibility(View.VISIBLE);
            tvNoVideos.setVisibility(View.GONE);
        } else {
            gvVideos.setVisibility(View.GONE);
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
        if (!Util.isNullOrEmptyList(educationVideoList)) {
            if (search.length() == 0) {
                tempList.addAll(educationVideoList);
            } else {
                for (PatientEducationVideo patientEducationVideo : educationVideoList) {
                    if (!Util.isNullOrBlank(patientEducationVideo.getName()) && patientEducationVideo.getName().toLowerCase(Locale.ENGLISH)
                            .contains(search)) {
                        tempList.add(patientEducationVideo);
                    }
                }
            }
        }
        notifyAdapter(tempList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_bt_add_video:
                openDialogFragment(new UploadVideoDialogFragment(), HealthCocoConstants.REQUEST_CODE_ADD_VIDEO);
                break;
        }
    }

    @Override
    public void onRefresh() {
//        getVideoList(false);
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
        if (!isFromSettings)
            try {
                Object object = parent.getAdapter().getItem(position);
                if (object instanceof PatientEducationVideo) {
                    PatientEducationVideo patientEducationVideo = (PatientEducationVideo) object;
                    openVideoViewActivity(CommonOpenUpFragmentType.PLAY_VIDEO, HealthCocoConstants.TAG_EDUCATION_VIDEO_DATA, patientEducationVideo,
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

    @Override
    public void onEditClicked(Object object) {
        if (object instanceof PatientEducationVideo) {
            PatientEducationVideo patientEducationVideo = (PatientEducationVideo) object;
            openDialogFragment(new UploadVideoDialogFragment(), HealthCocoConstants.TAG_UNIQUE_ID,
                    patientEducationVideo.getUniqueId(), HealthCocoConstants.REQUEST_CODE_ADD_VIDEO);
        }
    }

    @Override
    public void onDiscardClicked(Object object) {
        if (object instanceof PatientEducationVideo) {
            PatientEducationVideo patientEducationVideo = (PatientEducationVideo) object;
            boolean isDeleted = LocalDataServiceImpl.getInstance(mApp).deletePatientEducationVideo(patientEducationVideo.getUniqueId());
            if (isDeleted)
                getDataFromLocal();
        }
    }

    @Override
    public void onVideoClicked(Object object) {
        try {
            if (object instanceof PatientEducationVideo) {
                PatientEducationVideo patientEducationVideo = (PatientEducationVideo) object;
                openVideoViewActivity(CommonOpenUpFragmentType.PLAY_VIDEO, HealthCocoConstants.TAG_EDUCATION_VIDEO_DATA, patientEducationVideo,
                        HealthCocoConstants.REQUEST_CODE_PLAY_VIDEO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isFromSetting() {
        return isFromSettings;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_ADD_VIDEO) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_VIDEO) {
                getDataFromLocal();
            }
        }
    }
}
