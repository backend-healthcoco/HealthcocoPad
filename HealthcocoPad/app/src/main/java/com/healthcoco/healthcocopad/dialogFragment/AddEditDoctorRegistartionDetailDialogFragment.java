package com.healthcoco.healthcocopad.dialogFragment;

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
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorRegistrationDetail;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.MedicalCouncil;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.RegistrationDetailItemListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.RegistrationDetailListViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 18-02-2017.
 */
public class AddEditDoctorRegistartionDetailDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, RegistrationDetailItemListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, CommonListDialogItemClickListener {
    private LinearLayout containerItemsRegistrationDetail;
    private List<DoctorRegistrationDetail> registrationDetailList = new ArrayList<>();
    private FloatingActionButton btAddMore;
    private CommonListSolarDialogFragment commonListDialog;
    private TextView seletectMedicalCouncil;
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
            registrationDetailList = LocalDataServiceImpl.getInstance(mApp).getRegistrationDetailsList(user.getUniqueId());
            initViews();
            initListeners();
            notifyAdapter();
        }
    }

    @Override
    public void initViews() {
        containerItemsRegistrationDetail = (LinearLayout) view.findViewById(R.id.container_items_education_detail);
        btAddMore = (FloatingActionButton) view.findViewById(R.id.bt_add_more);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        initActionbarTitle(getResources().getString(R.string.registration_details));
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
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
                    addEditRegistrationDetails();
                }

                break;
            case R.id.bt_add_more:
                Object object1 = validateData(false);
                if (object1 instanceof Boolean && (Boolean) object1) {
                    addRegistrationItem(null, true);
                }
                break;
        }
    }

    private void addEditRegistrationDetails() {
        LogUtils.LOGD(TAG, "Registration Details List Size " + registrationDetailList.size());
        mActivity.showLoading(false);
        DoctorProfile doctorProfile = new DoctorProfile();
        doctorProfile.setDoctorId(user.getUniqueId());
        doctorProfile.setRegistrationDetails(registrationDetailList);
        WebDataServiceImpl.getInstance(mApp).addUpdateRegistrationDetail(DoctorProfile.class, doctorProfile, this, this);
    }

    private Object validateData(boolean isOnSaveClick) {
        registrationDetailList = new ArrayList<>();
        Object object = true;
        if (containerItemsRegistrationDetail.getChildCount() > 0) {
            for (int i = 0; i < containerItemsRegistrationDetail.getChildCount(); i++) {
                RegistrationDetailListViewHolder viewHolder = (RegistrationDetailListViewHolder) containerItemsRegistrationDetail.getChildAt(i);
                object = viewHolder.getErrorMessageOrTrueIfValidated(isOnSaveClick);
                if (object instanceof String)
                    return object;
            }
        }
        return true;
    }

    private void notifyAdapter() {
        containerItemsRegistrationDetail.removeAllViews();
        if (Util.isNullOrEmptyList(registrationDetailList)) {
            registrationDetailList = new ArrayList<>();
            registrationDetailList.add(null);
        }
        for (DoctorRegistrationDetail registrationDetail : registrationDetailList) {
            addRegistrationItem(registrationDetail, false);
        }
    }

    private void addRegistrationItem(DoctorRegistrationDetail registrationDetail, boolean isAddMoreClicked) {
        RegistrationDetailListViewHolder viewHolder = new RegistrationDetailListViewHolder(mActivity);
        viewHolder.setData(registrationDetail, this, containerItemsRegistrationDetail.getChildCount());
        containerItemsRegistrationDetail.addView(viewHolder);
        if (isAddMoreClicked)
            scrollView.postDelayed(new Runnable() {
                public void run() {
                    scrollView.fullScroll(HorizontalScrollView.FOCUS_DOWN);
                }
            }, 100L);
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
            case ADD_UPDATE_REGISTRATION_DETAIL:
                if (response.getData() != null && response.getData() instanceof DoctorProfile) {
                    DoctorProfile doctorProfileResponse = (DoctorProfile) response.getData();
                    LocalDataServiceImpl.getInstance(mApp).addRegistrationDetailsList(user.getUniqueId(), doctorProfileResponse.getRegistrationDetails());
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
            case MEDICAL_COUNCIL:
                if (object instanceof MedicalCouncil) {
                    MedicalCouncil medicalCouncil = (MedicalCouncil) object;
                    LogUtils.LOGD(TAG, "Selected Medical Council " + medicalCouncil.getMedicalCouncil());
                    seletectMedicalCouncil.setText(medicalCouncil.getMedicalCouncil());
                }
                break;
        }
        if (commonListDialog != null)
            commonListDialog.dismiss();
    }

    @Override
    public void onMedicalCouncilClicked(TextView textView, DoctorRegistrationDetail registrationDetail) {
        seletectMedicalCouncil = textView;
        commonListDialog = openCommonListSolarDialogFragment(this, CommonListDialogType.MEDICAL_COUNCIL);
    }

    @Override
    public void onDeleteRegistrationDetailClicked(View view, DoctorRegistrationDetail registrationDetail) {
        showConfirmationAlert(view, registrationDetail);
    }

    @Override
    public void addRegistrationDetailToList(DoctorRegistrationDetail registrationDetail) {
        if (registrationDetailList == null)
            registrationDetailList = new ArrayList<>();
        registrationDetailList.add(registrationDetail);
    }

    private void showConfirmationAlert(final View viewToDelete, final DoctorRegistrationDetail registrationDetail) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.this_cannot_be_undone);
        alertBuilder.setMessage(getResources().getString(
                R.string.do_you_want_to_delete_this_item));
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    containerItemsRegistrationDetail.removeView(viewToDelete);
                    if (registrationDetailList.contains(registrationDetail))
                        registrationDetailList.remove(registrationDetail);
                    if (containerItemsRegistrationDetail.getChildCount() == 0)
                        addRegistrationItem(null, true);
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
}
