package com.healthcoco.healthcocopad.dialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 17-07-2017.
 */

public class ChangeAppointmentStatusDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, PopupWindowListener {

    private TextView tvAppointmentStatus;
    private TextView titleTextView;
    private String appointmentId;
    private String status;
    private Bundle bundle;
    private CalendarEvents calendarEvents;

    public ChangeAppointmentStatusDialogFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_change_appointment_status, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bundle = getArguments();
        if (bundle != null)
            appointmentId = bundle.getString(HealthCocoConstants.TAG_UNIQUE_ID);
        if (!Util.isNullOrBlank(appointmentId))
            calendarEvents = LocalDataServiceImpl.getInstance(mApp).getAppointment(appointmentId);

        init();
    }

    @Override
    public void init() {
        if (calendarEvents != null && calendarEvents.getStatus() != null) {
            status = calendarEvents.getStatus().getValue();
        }
        initViews();
        initListeners();

    }

    @Override
    public void initViews() {
        tvAppointmentStatus = (TextView) view.findViewById(R.id.tv_appointment_status);
        titleTextView = (TextView) view.findViewById(R.id.tv_title);

        titleTextView.setText(R.string.appointment_status);

        if (!Util.isNullOrBlank(status)) {
            tvAppointmentStatus.setText(status);
        }

        mActivity.initPopupWindows(tvAppointmentStatus, PopupWindowType.APPOINTMENT_STATUS_TYPE, PopupWindowType.APPOINTMENT_STATUS_TYPE.getList(), this);

    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                validateData();
                break;
        }
    }

    private void validateData() {
        String msg = null;
        String calendarStatus = String.valueOf(tvAppointmentStatus.getText());
        if (status.equals(calendarStatus))
            changeAppointmentStatus(calendarStatus);
        else
            changeAppointmentStatus(calendarStatus);
    }

    private void changeAppointmentStatus(String appointmentStatus) {
        if (calendarEvents != null) {
            mActivity.showLoading(false);
            WebDataServiceImpl.getInstance(mApp).changeAppointmentStatus(CalendarEvents.class, calendarEvents.getDoctorId(), calendarEvents.getLocationId(), calendarEvents.getHospitalId(), calendarEvents.getPatientId(), calendarEvents.getAppointmentId(), appointmentStatus, this, this);
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
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
            case CHANGE_APPOINTMENT_STATUS:
                mActivity.hideLoading();
                if (response.getData() != null) {
                    boolean data = (boolean) response.getData();
                    if (data)
                        dismiss();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        switch (popupWindowType) {
            case APPOINTMENT_STATUS_TYPE:
                tvAppointmentStatus.setTag(object);
                String calendarStatus = (String) object;
                tvAppointmentStatus.setText(calendarStatus);
                break;
        }
    }

    @Override
    public void onEmptyListFound() {

    }
}
