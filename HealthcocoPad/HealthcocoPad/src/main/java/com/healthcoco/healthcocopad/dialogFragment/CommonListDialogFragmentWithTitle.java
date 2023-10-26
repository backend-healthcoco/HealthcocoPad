package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.AddVisitsActivity;
import com.healthcoco.healthcocopad.adapter.CommonListDialogAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.BloodGroup;
import com.healthcoco.healthcocopad.bean.server.CityResponse;
import com.healthcoco.healthcocopad.bean.server.CollegeUniversityInstitute;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugDosage;
import com.healthcoco.healthcocopad.bean.server.DrugDurationUnit;
import com.healthcoco.healthcocopad.bean.server.EducationQualification;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.MedicalCouncil;
import com.healthcoco.healthcocopad.bean.server.Profession;
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.Specialities;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.ActionbarLeftRightActionTypeDrawables;
import com.healthcoco.healthcocopad.enums.ActionbarType;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.GridViewLoadMore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by neha on 21/03/16.
 */
public class CommonListDialogFragmentWithTitle extends HealthCocoDialogFragment implements TextWatcher, TextView.OnEditorActionListener,
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LocalDoInBackgroundListenerOptimised,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {

    private List<?> list;
    private CommonListDialogType commonListDialogType;
    private CommonListDialogItemClickListener commonListDialogItemClickListener;
    private GridViewLoadMore listView;
    private CommonListDialogAdapter mAdapter;
    private TextView tvNoResultFound;
    private LinearLayout layoutActionbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private User user;
    private LinearLayout loadingOverlay;
    private Toolbar toolbar;

    public CommonListDialogFragmentWithTitle() {
    }

    public CommonListDialogFragmentWithTitle(CommonListDialogItemClickListener commonListDialogItemClickListener, CommonListDialogType commonListDialogType, List<?> list) {
        this.commonListDialogItemClickListener = commonListDialogItemClickListener;
        this.commonListDialogType = commonListDialogType;
        this.list = list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_common_list_with_title, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        boolean isMyScriptVisitToggleState = Util.getVisitToggleStateFromPreferences(mActivity);
       /* if (isMyScriptVisitToggleState)
            ((AddVisitsActivity) mActivity).setWidthHeight(getDialog(), 0.90, 0.90);
        else*/
        setWidthHeight(0.90, 0.90);
        init();
    }

    @Override
    public void init() {
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            initViews();
            initDialogTitle();
            initListeners();
            initAdapter();
            getListFromLocal();
        }
    }

    @Override
    public void initViews() {
        loadingOverlay = (LinearLayout) view.findViewById(R.id.loading_overlay);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        listView = (GridViewLoadMore) view.findViewById(R.id.lv_list);
        tvNoResultFound = (TextView) view.findViewById(R.id.tv_no_result_found);
        if (commonListDialogType.isAddCustomAllowed()) {
            getSearchEditText().setImeActionLabel(getResources().getString(R.string.done), EditorInfo.IME_ACTION_DONE);
            initEditSearchView(commonListDialogType.getHint(), this, null, this, false);
        } else
            initEditSearchView(commonListDialogType.getHint(), this, false);
    }

    private void initDialogTitle() {
        if (commonListDialogType.getActionBarType() != null) {
            switch (commonListDialogType) {
                case DIRECTION:
                case FREQUENCY:
                    initActionBar(ActionbarType.TITLE, 0, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_ADD);
                    break;
                case DURATION:
                    initActionBar(ActionbarType.TITLE, 0, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION);
                    break;
            }
        }
    }

    //    private void initActionbar(ActionbarLeftRightActionTypeDrawables actionbarLeftRightActionType) {
//        if (commonListDialogType.getActionBarType() != null) {
//            View actionbar = mActivity.getLayoutInflater().inflate(commonListDialogType.getActionBarType().getActionBarLayoutId(), null);
//            actionbar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            layoutActionbar.addView(actionbar);
//            //Back,Cross,Done
//            LinearLayout containerRightAction = (LinearLayout) actionbar.findViewById(R.id.container_right_action);
//            if (containerRightAction != null) {
//                if (actionbarLeftRightActionType != ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION) {
//                    containerRightAction.removeAllViews();
//                    if (actionbarLeftRightActionType != null) {
//                        containerRightAction.setVisibility(View.VISIBLE);
//                        View actionView = mActivity.getLayoutInflater().inflate(actionbarLeftRightActionType.getLayoutId(), null);
//                        actionView.setClickable(false);
//                        containerRightAction.addView(actionView);
//                    }
//                    containerRightAction.setOnClickListener(this);
//                } else
//                    containerRightAction.setVisibility(View.GONE);
//            }
//
//            TextView tvTitle = (TextView) layoutActionbar.findViewById(R.id.tv_title);
//            tvTitle.setText(commonListDialogType.getTitleId());
//        }
//    }
    public void initActionBar(ActionbarType actionbarType, int title, ActionbarLeftRightActionTypeDrawables leftAction, ActionbarLeftRightActionTypeDrawables rightAction) {
        if (actionbarType == ActionbarType.HIDDEN) {
            hideActionBar();
        } else {
            View actionbar = mActivity.getLayoutInflater().inflate(actionbarType.getActionBarLayoutId(), null);
            actionbar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            toolbar.setContentInsetsAbsolute(0, 0);
            toolbar.addView(actionbar);
            LinearLayout containerLeftAction = (LinearLayout) actionbar.findViewById(R.id.container_left_action);
            if (leftAction != null && leftAction != ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION) {
                containerLeftAction.setVisibility(View.VISIBLE);
                View leftView = mActivity.getLayoutInflater().inflate(leftAction.getLayoutId(), null);
                if (leftView != null) {
                    if (leftAction.isDrawableBackground()) {
                        ImageButton imageButton = (ImageButton) leftView;
                        imageButton.setImageResource(leftAction.getDrawableTitleId());
                    } else {
                        TextView button = (TextView) leftView;
                        button.setText(leftAction.getDrawableTitleId());
                    }
                    containerLeftAction.addView(leftView);
                }
                containerLeftAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDialog().dismiss();
                    }
                });
            } else
                containerLeftAction.setVisibility(View.GONE);

            LinearLayout containerRightAction = (LinearLayout) actionbar.findViewById(R.id.container_right_action);
            if (rightAction != null && rightAction != ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION) {
                containerRightAction.setVisibility(View.VISIBLE);
                View rightView = mActivity.getLayoutInflater().inflate(rightAction.getLayoutId(), null);
                if (rightView != null) {
                    if (rightAction.isDrawableBackground()) {
                        ImageButton imageButton = (ImageButton) rightView;
                        imageButton.setImageResource(rightAction.getDrawableTitleId());
                    } else {
                        TextView button = (TextView) rightView;
                        button.setText(rightAction.getDrawableTitleId());
                    }
                    containerRightAction.addView(rightView);
                }
                containerRightAction.setOnClickListener(this);
            } else {
                containerRightAction.setVisibility(View.GONE);
            }
            TextView tvTitle = (TextView) toolbar.findViewById(R.id.tv_title);
            tvTitle.setText(commonListDialogType.getTitleId());
        }
    }

    public void hideActionBar() {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
        listView.setSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void initData() {
    }

    private void initAdapter() {
        if (!Util.isNullOrEmptyList(list))
            sortList(list);
        mAdapter = new CommonListDialogAdapter(mActivity, commonListDialogItemClickListener);
        mAdapter.setListData(commonListDialogType, list);
        listView.setAdapter(mAdapter);
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
        if (!Util.isNullOrEmptyList(list)) {
            if (search.length() == 0) {
                tempList.addAll(list);
            } else {
                for (Object object : list) {
                    switch (commonListDialogType) {
                        case SPECIALITY:
                            Specialities speciality = (Specialities) object;
                            if (speciality.getSuperSpeciality().toLowerCase(Locale.ENGLISH)
                                    .contains(search)) {
                                tempList.add(object);
                            }
                            break;

                        case CITY:
                            CityResponse cityResponse = (CityResponse) object;
                            if (cityResponse.getCity().toLowerCase(Locale.ENGLISH)
                                    .contains(search)) {
                                tempList.add(object);
                            }
                            break;
                        case BLOOD_GROUP:
                            BloodGroup bloodGroup = (BloodGroup) object;
                            if (bloodGroup.getBloodGroup().toLowerCase(Locale.ENGLISH)
                                    .contains(search)) {
                                tempList.add(object);
                            }
                            break;

                        case PROFESSION:
                            Profession profession = (Profession) object;
                            if (profession.getProfession().toLowerCase(Locale.ENGLISH)
                                    .contains(search)) {
                                tempList.add(object);
                            }
                            break;
                        case REFERRED_BY:
                            Reference reference = (Reference) object;
                            if (reference.getReference().toLowerCase(Locale.ENGLISH)
                                    .contains(search)) {
                                tempList.add(object);
                            }
                            break;
                        case DURATION:
                            if (object instanceof DrugDurationUnit) {
                                DrugDurationUnit durationUnit = (DrugDurationUnit) object;
                                if (durationUnit.getUnit().toLowerCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case DIRECTION:
                            if (object instanceof DrugDirection) {
                                DrugDirection drugDirection = (DrugDirection) object;
                                if (drugDirection.getDirection().toLowerCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;
                        case FREQUENCY:
                            if (object instanceof DrugDosage) {
                                DrugDosage drugDosage = (DrugDosage) object;
                                if (drugDosage.getDosage().toLowerCase(Locale.ENGLISH)
                                        .contains(search)) {
                                    tempList.add(object);
                                }
                            }
                            break;

                    }
                }
            }
        }
        notifyAdapter(tempList);

    }

    private void notifyAdapter(List<?> tempList) {
        if (!Util.isNullOrEmptyList(tempList)) {
//            sortList(tempList);
            listView.setVisibility(View.VISIBLE);
            tvNoResultFound.setVisibility(View.GONE);
            mAdapter.setListData(commonListDialogType, tempList);
            mAdapter.notifyDataSetChanged();
        } else {
            listView.setVisibility(View.GONE);
            tvNoResultFound.setVisibility(View.VISIBLE);
        }
    }

    private void sortList(List<?> list) {
        switch (commonListDialogType) {
            case CITY:
                Collections.sort(list, ComparatorUtil.cityListComparator);
                break;
            case SPECIALITY:
                Collections.sort(list, ComparatorUtil.specialityListComparator);
                break;
            case REFERRED_BY:
                Collections.sort(list, ComparatorUtil.referrefByNameComparator);
                break;
            case FREQUENCY:
                Collections.sort(list, ComparatorUtil.dosageListComparator);
                break;
            case DURATION:
                Collections.sort(list, ComparatorUtil.durationUnitListComparator);
                break;
            case DIRECTION:
                Collections.sort(list, ComparatorUtil.directionListComparator);
                break;
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            Object object = null;
            switch (commonListDialogType) {
                case QUALIFICATION:
                    EducationQualification educationQualification = new EducationQualification();
                    educationQualification.setName(getSearchEditTextValue());
                    object = educationQualification;
                    break;
                case COLLEGE_UNIVERSITY_INSTITUTE:
                    CollegeUniversityInstitute collegeUniversityInstitute = new CollegeUniversityInstitute();
                    collegeUniversityInstitute.setName(getSearchEditTextValue());
                    object = collegeUniversityInstitute;
                    break;
                case MEDICAL_COUNCIL:
                    MedicalCouncil medicalCouncil = new MedicalCouncil();
                    medicalCouncil.setMedicalCouncil(getSearchEditTextValue());
                    object = medicalCouncil;
                    break;
                case REFERRED_BY:
                    Reference reference = new Reference();
                    reference.setReference(getSearchEditTextValue());
                    object = reference;
                    break;
            }
            if (object != null) {
                commonListDialogItemClickListener.onDialogItemClicked(commonListDialogType, object);
                dismiss();
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_right_action:
                performRightAction();
                break;
        }
    }

    private void performRightAction() {
        switch (commonListDialogType) {
            case DIRECTION:
                openAddUpdateNameDialogFragment(WebServiceType.ADD_DIRECTION, AddUpdateNameDialogType.DIRECTION, this, "", HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST);
                break;
            case FREQUENCY:
                openAddUpdateNameDialogFragment(WebServiceType.ADD_DOSAGE, AddUpdateNameDialogType.FREQUENCY, this, "", HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_REFERENCE_LIST) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_REFERENCE_LIST) {
                getListFromLocal();
            }
        }
    }

    private void getListFromLocal() {
        loadingOverlay.setVisibility(View.VISIBLE);
        switch (commonListDialogType) {
            case FREQUENCY:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FREQUENCY_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case DURATION:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DURATION_UNIT, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case DIRECTION:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DIRECTION_ACTIVATED_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            default:
                loadingOverlay.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onRefresh() {
        getDataFromServer(false);
//        getHistoryListFromLocal();
    }

    private void getDataFromServer(boolean showLoading) {
        if (showLoading)
            loadingOverlay.setVisibility(View.VISIBLE);
        else
            loadingOverlay.setVisibility(View.GONE);
        switch (commonListDialogType) {
            case FREQUENCY:
                //getting dosages/frequency
                Long latestUpdatedTimeDosage = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DRUG_DOSAGE);
                WebDataServiceImpl.getInstance(mApp)
                        .getDosageDirection(WebServiceType.GET_DRUG_DOSAGE, DrugDosage.class, true, this.user.getUniqueId(), latestUpdatedTimeDosage, this, this);
                break;
            case DURATION:
                //getting durationUnit
                Long latestUpdatedTimeDuration = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DRUG_DURATION_UNIT);
                WebDataServiceImpl.getInstance(mApp)
                        .getDosageDirection(WebServiceType.GET_DURATION_UNIT, DrugDurationUnit.class, true, this.user.getUniqueId(), latestUpdatedTimeDuration, this, this);
                break;
            case DIRECTION:
                //getting directions
                Long latestUpdatedTimeDirection = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DRUG_DIRECTION);
                WebDataServiceImpl.getInstance(mApp)
                        .getDosageDirection(WebServiceType.GET_DIRECTION, DrugDirection.class, true, this.user.getUniqueId(), latestUpdatedTimeDirection, this, this);
                break;
            default:
                mActivity.hideLoading();
                break;
        }
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
        loadingOverlay.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        swipeRefreshLayout.setRefreshing(false);
        loadingOverlay.setVisibility(View.GONE);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null) {
            switch (response.getWebServiceType()) {
                case GET_DURATION_UNIT:
                    if (response.isDataFromLocal()) {
                        list = (ArrayList<DrugDurationUnit>) (ArrayList<?>) response.getDataList();
                        if (!Util.isNullOrEmptyList(list))
                            LogUtils.LOGD(TAG, "Success onResponse receivedDurationList Size " + list.size() + " isDataFromLocal " + response.isDataFromLocal());
                        notifyAdapter(list);
                    } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DURATION_UNIT, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    }
                    break;
                case GET_DRUG_DOSAGE:
                    if (response.isDataFromLocal()) {
                        list = (ArrayList<DrugDosage>) (ArrayList<?>) response.getDataList();
                        if (!Util.isNullOrEmptyList(list))
                            LogUtils.LOGD(TAG, "Success onResponse receivedFrequencyDosageList Size " + list.size() + " isDataFromLocal " + response.isDataFromLocal());
                        notifyAdapter(list);
                    } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DRUG_DOSAGE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    }
                    break;
                case GET_DIRECTION:
                    if (response.isDataFromLocal()) {
                        list = (ArrayList<DrugDirection>) (ArrayList<?>) response.getDataList();
                        if (!Util.isNullOrEmptyList(list))
                            LogUtils.LOGD(TAG, "Success onResponse receivedDirectionsList Size " + list.size() + " isDataFromLocal " + response.isDataFromLocal());
                        notifyAdapter(list);
                    } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DIRECTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    }
                    break;
                default:
                    break;
            }
        }
        loadingOverlay.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_DIRECTIONS:
                LocalDataServiceImpl.getInstance(mApp).addDirectionsList((ArrayList<DrugDirection>) (ArrayList<?>) response.getDataList());
            case GET_DIRECTION_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getGlobalIncludedDosageDurationDirectionList(WebServiceType.GET_DIRECTION, user.getUniqueId(), false, 0, null, null);
                break;
            case ADD_DRUG_DOSAGE:
                LocalDataServiceImpl.getInstance(mApp).addDrugDosageList((ArrayList<DrugDosage>) (ArrayList<?>) response.getDataList());
            case GET_FREQUENCY_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getGlobalIncludedDosageDurationDirectionList(WebServiceType.GET_DRUG_DOSAGE, user.getUniqueId(), false, 0, null, null);
                break;
            case ADD_DURATION_UNIT:
                LocalDataServiceImpl.getInstance(mApp).addDurationUnitList((ArrayList<DrugDurationUnit>) (ArrayList<?>) response.getDataList());
            case GET_DURATION_UNIT:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getGlobalIncludedDosageDurationDirectionList(WebServiceType.GET_DURATION_UNIT, user.getUniqueId(), false, 0, null, null);
                break;
        }
        if (volleyResponseBean != null)
            volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

}
