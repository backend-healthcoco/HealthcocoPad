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
import com.healthcoco.healthcocopad.bean.request.ProfessionalMembershipRequest;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.ProfessionalMembership;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.MyProfileFragment;
import com.healthcoco.healthcocopad.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.ProfessipnalMembershipDetailItemListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.ProfessionalMembershipListViewHolder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 18-02-2017.
 */
public class AddEditDoctorMembershipDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, ProfessipnalMembershipDetailItemListener, CommonListDialogItemClickListener, LocalDoInBackgroundListenerOptimised {
    public static final String TAG_DOCTOR_MEMBERSHIP_DETAIL = "doctorsMembershipDetail";
    private LinearLayout containerItemsExperienceDetail;
    private List<String> professionalMembershipses = new ArrayList<>();
    private FloatingActionButton btAddMore;
    private User user;
    private ScrollView scrollView;
    private CommonListSolarDialogFragment commonListDialog;
    private TextView seletectProfesssionalMembership;
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
        init();
        setWidthHeight(0.50, 0.80);
        showLoadingOverlay(true);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        professionalMembershipses = Parcels.unwrap(getArguments().getParcelable(TAG_DOCTOR_MEMBERSHIP_DETAIL));
        initViews();
        initListeners();
        notifyAdapter();
    }

    @Override
    public void initViews() {
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        containerItemsExperienceDetail = (LinearLayout) view.findViewById(R.id.container_items_education_detail);
        btAddMore = (FloatingActionButton) view.findViewById(R.id.bt_add_more);
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
        initActionbarTitle(getResources().getString(R.string.professional_membership));
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
                    addEditProfessionalMembershipDetails();
                }

                break;
            case R.id.bt_add_more:
                Object object1 = validateData(false);
                if (object1 instanceof Boolean && (Boolean) object1) {
                    addEditProfessionalMembershipItem(null, true);
                }
                break;
        }
    }

    private void addEditProfessionalMembershipItem(String professionalMemberships, boolean isAddMoreClicked) {
        ProfessionalMembershipListViewHolder viewHolder = new ProfessionalMembershipListViewHolder(mActivity);
        viewHolder.setData(professionalMemberships, this, containerItemsExperienceDetail.getChildCount());
        containerItemsExperienceDetail.addView(viewHolder);
        if (isAddMoreClicked)
            scrollView.postDelayed(new Runnable() {
                public void run() {
                    scrollView.fullScroll(HorizontalScrollView.FOCUS_DOWN);
                }
            }, 100L);
    }

    private void addEditProfessionalMembershipDetails() {
        LogUtils.LOGD(TAG, "Professional Membership List Size " + professionalMembershipses.size());
        mActivity.showLoading(false);
        ProfessionalMembershipRequest professionalMembershipRequest = new ProfessionalMembershipRequest();
        professionalMembershipRequest.setDoctorId(user.getUniqueId());
        professionalMembershipRequest.setMembership(professionalMembershipses);
        WebDataServiceImpl.getInstance(mApp).addUpdateProfessionalMembership(ProfessionalMembershipRequest.class, professionalMembershipRequest, this, this);
    }

    private Object validateData(boolean isOnSaveClick) {
        professionalMembershipses = new ArrayList<>();
        Object object = true;
        if (containerItemsExperienceDetail.getChildCount() > 0) {
            for (int i = 0; i < containerItemsExperienceDetail.getChildCount(); i++) {
                ProfessionalMembershipListViewHolder viewHolder = (ProfessionalMembershipListViewHolder) containerItemsExperienceDetail.getChildAt(i);
                object = viewHolder.getErrorMessageOrTrueIfValidated(isOnSaveClick);
                if (object instanceof String)
                    return object;
            }
        }
        return true;
    }

    private void notifyAdapter() {
        containerItemsExperienceDetail.removeAllViews();
        if (Util.isNullOrEmptyList(professionalMembershipses)) {
            professionalMembershipses = new ArrayList<>();
            professionalMembershipses.add(null);
        }
        for (String professionalMemberships : professionalMembershipses) {
            addEditProfessionalMembershipItem(professionalMemberships, false);
        }
    }

    private void showConfirmationAlert(final View viewToDelete, final String professionalMemberships) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.this_cannot_be_undone);
        alertBuilder.setMessage(getResources().getString(
                R.string.do_you_want_to_delete_this_item));
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    containerItemsExperienceDetail.removeView(viewToDelete);
                    if (professionalMembershipses.contains(professionalMemberships))
                        professionalMembershipses.remove(professionalMemberships);
                    if (containerItemsExperienceDetail.getChildCount() == 0)
                        addEditProfessionalMembershipItem(null, true);
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
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        switch (response.getWebServiceType()) {
            case ADD_UPDATE_PROFESSIONAL_MEMBERSHIP_DETAIL:
                if (response.getData() != null && response.getData() instanceof ProfessionalMembershipRequest) {
                    ProfessionalMembershipRequest membershipRequest = (ProfessionalMembershipRequest) response.getData();
                    doctorProfile = new DoctorProfile();
                    doctorProfile.setProfessionalMemberships(membershipRequest.getMembership());
                    LocalDataServiceImpl.getInstance(mApp).addDoctorProfile(doctorProfile);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_DOCTOR_PROFESSIONAL_MEMBERSHIP_DETAIL, new Intent().putExtra(MyProfileFragment.TAG_DOCTOR_PROFILE, Parcels.wrap(doctorProfile)));
                    getDialog().dismiss();
                }
                break;
        }
        mActivity.hideLoading();
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
    public void onDeleteProfessionalMembershipDetailClicked(View view, String professionalMemberships) {
        showConfirmationAlert(view, professionalMemberships);
    }

    @Override
    public void addProfessionalMembershipDetailToList(String professionalMemberships) {
        if (professionalMemberships == null)
            professionalMembershipses = new ArrayList<>();
        professionalMembershipses.add(professionalMemberships);
    }

    @Override
    public void onProfessionalMembershipClicked(TextView textView, String professionalMemberships) {
        seletectProfesssionalMembership = textView;
        commonListDialog = openCommonListSolarDialogFragment(this, CommonListDialogType.PROFESSIONAL_MEMBERSHIP);
    }

    @Override
    public void onDialogItemClicked(CommonListDialogType commonListDialogType, Object object) {
        switch (commonListDialogType) {
            case PROFESSIONAL_MEMBERSHIP:
                if (object instanceof ProfessionalMembership) {
                    ProfessionalMembership professionalMemberships = (ProfessionalMembership) object;
                    LogUtils.LOGD(TAG, "Selected Professional Membership " + professionalMemberships.getMembership());
                    seletectProfesssionalMembership.setText(professionalMemberships.getMembership());
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
