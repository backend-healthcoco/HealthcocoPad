package com.healthcoco.healthcocopad.dialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AddEditDoctorEducationRequest;
import com.healthcoco.healthcocopad.bean.server.CollegeUniversityInstitute;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.Education;
import com.healthcoco.healthcocopad.bean.server.EducationQualification;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.MyProfileFragment;
import com.healthcoco.healthcocopad.listeners.EducationDetailItemListner;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.EducationDetailListViewHolder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 18-02-2017.
 */
public class AddEditDoctorEducationDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, EducationDetailItemListner, LocalDoInBackgroundListenerOptimised {
    public static final String TAG_DOCTOR_EDUCATION = "doctorsEducationDeatil";
    private LinearLayout containerItemsEducationDetail;
    private List<Education> educationList = new ArrayList<>();
    private FloatingActionButton btAddMore;
    private CommonListSolarDialogFragment commonListDialog;
    private TextView seletectQualificationView;
    private TextView selectedInstituteView;
    private User user;
    private ScrollView scrollView;
    private DoctorProfile doctorProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_edit_education_detail, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setWidthHeight(0.50, 0.80);
        init();
        showLoadingOverlay(true);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        educationList = Parcels.unwrap(getArguments().getParcelable(TAG_DOCTOR_EDUCATION));
        initViews();
        initListeners();
        notifyAdapter();
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
        int id = v.getId();

        if (id == R.id.bt_save) {
            Object object = validateData(true);
            if (object instanceof Boolean && (Boolean) object) {
                addEditEducationDetails();
            }
        } else if (id == R.id.bt_add_more) {
            Object object1 = validateData(false);
            if (object1 instanceof Boolean && (Boolean) object1) {
                addEducationItem(null, true);
            }
        }
    }

    private void addEditEducationDetails() {
        LogUtils.LOGD(TAG, "Educations List Size " + educationList.size());
        mActivity.showLoading(false);
        AddEditDoctorEducationRequest doctorEducationRequest = new AddEditDoctorEducationRequest();
        doctorEducationRequest.setDoctorId(user.getUniqueId());
        doctorEducationRequest.setEducation(educationList);
        WebDataServiceImpl.getInstance(mApp).addUpdateEducation(AddEditDoctorEducationRequest.class, doctorEducationRequest, this, this);
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
                if (response.getData() != null && response.getData() instanceof AddEditDoctorEducationRequest) {
                    doctorProfile = new DoctorProfile();
                    AddEditDoctorEducationRequest addEditDoctorEducationRequest = (AddEditDoctorEducationRequest) response.getData();
                    doctorProfile.setEducation(addEditDoctorEducationRequest.getEducation());
                    LocalDataServiceImpl.getInstance(mApp).addEducationsList(user.getUniqueId(), doctorProfile.getEducation());
                }
                Intent intent = new Intent();
                intent.putExtra(MyProfileFragment.TAG_DOCTOR_PROFILE, Parcels.wrap(doctorProfile));
                getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_EDUCATIONAL_DETAIL, intent);
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

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null)
                    user = doctor.getUser();
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }
}
