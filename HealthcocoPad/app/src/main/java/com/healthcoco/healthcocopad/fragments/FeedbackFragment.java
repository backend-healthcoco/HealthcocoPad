package com.healthcoco.healthcocopad.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.Feedback;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.AppType;
import com.healthcoco.healthcocopad.enums.DeviceType;
import com.healthcoco.healthcocopad.enums.FeedbackType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 31-01-2017.
 */
public class FeedbackFragment extends HealthCocoFragment implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    private EditText editFeedback;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feedback, null);
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
        }
    }

    @Override
    public void initViews() {
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
        editFeedback = (EditText) view.findViewById(R.id.edit_feedback);
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_right_action:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    validateData();
                else
                    onNetworkUnavailable(null);
                break;
        }
    }

    private void validateData() {
        String msg = null;
        String feedback = Util.getValidatedValue(String.valueOf(editFeedback.getText()));
        if (Util.isNullOrBlank(feedback)) {
            msg = getResources().getString(R.string.please_enter_feedback);
        }
        if (Util.isNullOrBlank(msg)) {
            sendFeedback(feedback);
        } else
            Util.showToast(mActivity, msg);

    }

    private void sendFeedback(String feedbackText) {
        mActivity.showLoading(false);
        Feedback feedback = new Feedback();
        feedback.setDoctorId(user.getUniqueId());
        feedback.setLocationId(user.getForeignLocationId());
        feedback.setHospitalId(user.getForeignHospitalId());
        feedback.setExplanation(feedbackText);
        feedback.setType(FeedbackType.HELP_US);
        feedback.setDeviceType(DeviceType.IOS);
        feedback.setAppType(AppType.HEALTHCOCO_PLUS);
        feedback.setDeviceInfo(Util.getFormattedDeviceInfo(mActivity));
        WebDataServiceImpl.getInstance(mApp).sendFeedback(Feedback.class, feedback, this, this);
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

    @Override
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
            case ADD_FEEDBACK:
                Util.showToast(mActivity, R.string.feedback_sent_successfully);
                mActivity.finish();
                break;
        }
        mActivity.hideLoading();
    }
}
