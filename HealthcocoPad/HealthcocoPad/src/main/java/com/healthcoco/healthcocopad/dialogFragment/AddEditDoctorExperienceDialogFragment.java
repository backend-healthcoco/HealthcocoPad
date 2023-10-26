package com.healthcoco.healthcocopad.dialogFragment;

import android.app.Activity;
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

import com.android.volley.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AddEditDoctorExperinceRequest;
import com.healthcoco.healthcocopad.bean.server.DoctorExperienceDetail;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.MyProfileFragment;
import com.healthcoco.healthcocopad.listeners.ExperienceDetailItemListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.ExperienceDetailListViewHolder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 18-02-2017.
 */
public class AddEditDoctorExperienceDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, ExperienceDetailItemListener, LocalDoInBackgroundListenerOptimised {
    public static final String TAG_DOCTOR_EXPERIENCE_DETAIL = "doctorsExperienceDetail";
    private LinearLayout containerItemsExperienceDetail;
    private List<DoctorExperienceDetail> experienceList = new ArrayList<>();
    private FloatingActionButton btAddMore;
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
        init();
        setWidthHeight(0.50, 0.80);
        showLoadingOverlay(true);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        experienceList = Parcels.unwrap(getArguments().getParcelable(TAG_DOCTOR_EXPERIENCE_DETAIL));
        initViews();
        initListeners();
        initData();
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
        initActionbarTitle(getResources().getString(R.string.experience));
        btAddMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                Object object = validateData(true);
                if (object instanceof Boolean && (Boolean) object) {
                    addEditExperienceDetails();
                }

                break;
            case R.id.bt_add_more:
                Object object1 = validateData(false);
                if (object1 instanceof Boolean && (Boolean) object1) {
                    addEditExperienceItem(null, true);
                }
                break;
        }
    }

    private void addEditExperienceItem(DoctorExperienceDetail experienceDetail, boolean isAddMoreClicked) {
        ExperienceDetailListViewHolder viewHolder = new ExperienceDetailListViewHolder(mActivity);
        viewHolder.setData(experienceDetail, this, containerItemsExperienceDetail.getChildCount());
        containerItemsExperienceDetail.addView(viewHolder);
        if (isAddMoreClicked)
            scrollView.postDelayed(new Runnable() {
                public void run() {
                    scrollView.fullScroll(HorizontalScrollView.FOCUS_DOWN);
                }
            }, 100L);
    }

    private void addEditExperienceDetails() {
        LogUtils.LOGD(TAG, "Experience List Size " + experienceList.size());
        mActivity.showLoading(false);
        AddEditDoctorExperinceRequest doctorExperinceRequest = new AddEditDoctorExperinceRequest();
        doctorExperinceRequest.setDoctorId(user.getUniqueId());
        doctorExperinceRequest.setExperienceDetails(experienceList);
        WebDataServiceImpl.getInstance(mApp).addUpdateExperirnce(AddEditDoctorExperinceRequest.class, doctorExperinceRequest, this, this);
    }

    private Object validateData(boolean isOnSaveClick) {
        experienceList = new ArrayList<>();
        Object object = true;
        if (containerItemsExperienceDetail.getChildCount() > 0) {
            for (int i = 0; i < containerItemsExperienceDetail.getChildCount(); i++) {
                ExperienceDetailListViewHolder viewHolder = (ExperienceDetailListViewHolder) containerItemsExperienceDetail.getChildAt(i);
                object = viewHolder.getErrorMessageOrTrueIfValidated(isOnSaveClick);
                if (object instanceof String)
                    return object;
            }
        }
        return true;
    }

    private void notifyAdapter() {
        containerItemsExperienceDetail.removeAllViews();
        if (Util.isNullOrEmptyList(experienceList)) {
            experienceList = new ArrayList<>();
            experienceList.add(null);
        }
        for (DoctorExperienceDetail experienceDetail : experienceList) {
            addEditExperienceItem(experienceDetail, false);
        }
    }

    private void showConfirmationAlert(final View viewToDelete, final DoctorExperienceDetail experienceDetail) {
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
                    if (experienceList.contains(experienceDetail))
                        experienceList.remove(experienceDetail);
                    if (containerItemsExperienceDetail.getChildCount() == 0)
                        addEditExperienceItem(null, true);
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
            case ADD_UPDATE_EXPERIENCE_DETAIL:
                if (response.getData() != null && response.getData() instanceof AddEditDoctorExperinceRequest) {
                    doctorProfile = new DoctorProfile();
                    AddEditDoctorExperinceRequest addEditDoctorExperinceRequest = (AddEditDoctorExperinceRequest) response.getData();
                    doctorProfile.setExperienceDetails(addEditDoctorExperinceRequest.getExperienceDetails());
                    LocalDataServiceImpl.getInstance(mApp).addExperirnce(user.getUniqueId(), doctorProfile.getExperienceDetails());

                    Intent intent = new Intent();
                    intent.putExtra(MyProfileFragment.TAG_DOCTOR_PROFILE, Parcels.wrap(doctorProfile));
                    getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_EXPERIENCE_DETAIL, intent);
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
    public void initData() {

    }

    @Override
    public void onDeleteExperienceDetailClicked(View view, DoctorExperienceDetail experienceDetail) {
        showConfirmationAlert(view, experienceDetail);
    }

    @Override
    public void addExperienceDetailToList(DoctorExperienceDetail experienceDetail) {
        if (experienceDetail == null)
            experienceList = new ArrayList<>();
        experienceList.add(experienceDetail);
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
