package com.healthcoco.healthcocoplus.dialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoDialogFragment;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.bean.server.CollegeUniversityInstitute;
import com.healthcoco.healthcocoplus.bean.server.DoctorProfile;
import com.healthcoco.healthcocoplus.bean.server.Education;
import com.healthcoco.healthcocoplus.bean.server.EducationQualification;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.enums.CommonListDialogType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.listeners.EducationDetailItemListner;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocoplus.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.viewholders.EducationDetailListViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 18-02-2017.
 */
public class AddEditDoctorEducationDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, EducationDetailItemListner {
    private LinearLayout containerItemsEducationDetail;
    private List<Education> educationList = new ArrayList<>();
    private FloatingActionButton btAddMore;
    private CommonListSolarDialogFragment commonListDialog;
    private TextView seletectQualificationView;
    private TextView selectedInstituteView;
    private User user;
    private ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_edit_education_detail, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setWidthHeight(0.50,0.75);
        init();
    }

    @Override
    public void init() {
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            educationList = LocalDataServiceImpl.getInstance(mApp).getEducationDetailsList(user.getUniqueId());
            initViews();
            initListeners();
            notifyAdapter();
        }
    }

    @Override
    public void initViews() {
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        containerItemsEducationDetail = (LinearLayout) view.findViewById(R.id.container_items_education_detail);
        btAddMore = (FloatingActionButton) view.findViewById(R.id.bt_add_more);
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
        initActionbarTitle(getResources().getString(R.string.education));
        btAddMore.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                Object object = validateData(true);
                if (object instanceof Boolean && (Boolean) object) {
                    addEditEducationDetails();
                }

                break;
            case R.id.bt_add_more:
                Object object1 = validateData(false);
                if (object1 instanceof Boolean && (Boolean) object1) {
                    addEducationItem(null, true);
                }
                break;
        }
    }

    private void addEditEducationDetails() {
        LogUtils.LOGD(TAG, "Educations List Size " + educationList.size());
        mActivity.showLoading(false);
        DoctorProfile doctorProfile = new DoctorProfile();
        doctorProfile.setDoctorId(user.getUniqueId());
        doctorProfile.setEducation(educationList);
        WebDataServiceImpl.getInstance(mApp).addUpdateEducation(DoctorProfile.class, doctorProfile, this, this);
    }

    private Object validateData(boolean isOnSaveClick) {
        educationList = new ArrayList<>();
        Object object = true;
        if (containerItemsEducationDetail.getChildCount() > 0) {
            for (int i = 0; i < containerItemsEducationDetail.getChildCount(); i++) {
                EducationDetailListViewHolder viewHolder = (EducationDetailListViewHolder) containerItemsEducationDetail.getChildAt(i);
                object = viewHolder.getErrorMessageOrTrueIfValidated(isOnSaveClick);
                if (object instanceof String)
                    return object;
            }
        }
        return true;
    }

    private void notifyAdapter() {
        containerItemsEducationDetail.removeAllViews();
        if (Util.isNullOrEmptyList(educationList)) {
            educationList = new ArrayList<>();
            educationList.add(null);
        }
        for (Education education : educationList) {
            addEducationItem(education, false);
        }
    }

    private void addEducationItem(Education education, boolean isAddMoreClicked) {
        EducationDetailListViewHolder viewHolder = new EducationDetailListViewHolder(mActivity);
        viewHolder.setData(education, this, containerItemsEducationDetail.getChildCount());
        containerItemsEducationDetail.addView(viewHolder);
        if (isAddMoreClicked)
            scrollView.postDelayed(new Runnable() {
                public void run() {
                    scrollView.fullScroll(HorizontalScrollView.FOCUS_DOWN);
                }
            }, 100L);
    }

    @Override
    public void onQualificationClicked(TextView textView, Education education) {
        seletectQualificationView = textView;
        commonListDialog = openCommonListSolarDialogFragment(this, CommonListDialogType.QUALIFICATION);
    }

    @Override
    public void onCollegeUniversityClicked(TextView textView, Education education) {
        selectedInstituteView = textView;
        commonListDialog = openCommonListSolarDialogFragment(this, CommonListDialogType.COLLEGE_UNIVERSITY_INSTITUTE);
    }

    @Override
    public void onDeleteEducationDetailClicked(View view, Education education) {
        showConfirmationAlert(view, education);
    }

    @Override
    public void addEducationDetailToList(Education education) {
        if (educationList == null)
            educationList = new ArrayList<>();
        educationList.add(education);
    }

    private void showConfirmationAlert(final View viewToDelete, final Education education) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.this_cannot_be_undone);
        alertBuilder.setMessage(getResources().getString(
                R.string.do_you_want_to_delete_this_item));
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    containerItemsEducationDetail.removeView(viewToDelete);
                    if (educationList.contains(education))
                        educationList.remove(education);
                    if (containerItemsEducationDetail.getChildCount() == 0)
                        addEducationItem(null, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        }
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        switch (response.getWebServiceType()) {
            case ADD_UPDATE_EDUCATION:
                if (response.getData() != null && response.getData() instanceof DoctorProfile) {
                    DoctorProfile doctorProfileResponse = (DoctorProfile) response.getData();
                    LocalDataServiceImpl.getInstance(mApp).addEducationsList(user.getUniqueId(), doctorProfileResponse.getEducation());
                }
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                getDialog().dismiss();
                break;
        }
        mActivity.hideLoading();
    }

    @Override
    public void onDialogItemClicked(CommonListDialogType commonListDialogType, Object object) {
        switch (commonListDialogType) {
            case QUALIFICATION:
                if (object instanceof EducationQualification) {
                    EducationQualification qualification = (EducationQualification) object;
                    LogUtils.LOGD(TAG, "Selected Qualification " + qualification.getName());
                    seletectQualificationView.setText(qualification.getName());
                }
                break;
            case COLLEGE_UNIVERSITY_INSTITUTE:
                if (object instanceof CollegeUniversityInstitute) {
                    CollegeUniversityInstitute institute = (CollegeUniversityInstitute) object;
                    LogUtils.LOGD(TAG, "Selected institute " + institute.getName());
                    selectedInstituteView.setText(institute.getName());
                }
                break;
            case YEAR:
                if (object instanceof String) {
                    String year = (String) object;
                    LogUtils.LOGD(TAG, "Selected year " + year);
                }
                break;
        }
        if (commonListDialog != null)
            commonListDialog.dismiss();
    }
}
