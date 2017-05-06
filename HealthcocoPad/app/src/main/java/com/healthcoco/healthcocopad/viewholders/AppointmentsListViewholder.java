package com.healthcoco.healthcocopad.viewholders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AppointmentRequest;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
import com.healthcoco.healthcocopad.dialogFragment.BookAppointmentDialogFragment;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.BookAppointmentFromScreenType;
import com.healthcoco.healthcocopad.enums.CreatedByType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.PatientAppointmentDetailFragment;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

/**
 * Created by neha on 05/05/16.
 */
public class AppointmentsListViewholder extends HealthCocoViewHolder implements
        View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {

    public static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    private HealthCocoActivity mActivity;
    private TextView tvAppointmentID;
    private CalendarEvents appointment;
    private TextView tvDate;
    private TextView tvDoctorName;
    private TextView tvMobileNumber;
    private TextView tvAppointmentStatus;
    private TextView tvFromTime;
    private TextView tvToTime;
    private LinearLayout layoutButtons;
    private LinearLayout btCancel;
    private LinearLayout btConfirm;
    private LinearLayout btReschedule;
    private TextView tvDiscardedCancelled;
    private LinearLayout layoutDiscarded;
    private LinearLayout btRemind;
    private LinearLayout containerBottomOptions;
    private TextView tvNote;

    public AppointmentsListViewholder(HealthCocoActivity mActivity) {
        super(mActivity);
        this.mActivity = mActivity;
    }

    @Override
    public void setData(Object object) {
        this.appointment = (CalendarEvents) object;
    }

    @Override
    public void applyData() {
        tvAppointmentID.setText(Util.getValidatedValue(appointment.getAppointmentId()));
        if (appointment.getState() != null) {
            switch (appointment.getState()) {
                case CANCEL:
                    layoutButtons.setVisibility(View.GONE);
                    tvAppointmentStatus.setText(appointment.getState().getText());
                    layoutDiscarded.setVisibility(View.VISIBLE);
                    tvDiscardedCancelled.setText(R.string.cancelled);
                    break;
                case CONFIRM:
                    layoutButtons.setVisibility(View.VISIBLE);
                    tvAppointmentStatus.setText(appointment.getState().getText());
                    layoutDiscarded.setVisibility(View.GONE);
                    btConfirm.setVisibility(View.GONE);
                    btRemind.setVisibility(View.VISIBLE);
                    break;
                default:
                    layoutButtons.setVisibility(View.VISIBLE);
                    tvAppointmentStatus.setText(appointment.getState().getText());
                    layoutDiscarded.setVisibility(View.GONE);
                    btConfirm.setVisibility(View.VISIBLE);
                    btRemind.setVisibility(View.GONE);
                    break;
            }
        }
        tvDate.setText(DateTimeUtil.getFormattedDateTime(PatientAppointmentDetailFragment.DATE_FORMAT_USED_IN_THIS_SCREEN, appointment.getFromDate()));
        if (appointment.getTime() != null && appointment.getTime().getFromTime() != null && appointment.getTime().getToTime() != null) {
            WorkingHours workingHours = appointment.getTime();
            tvFromTime.setText(DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getFromTime())));
            tvToTime.setText(DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getToTime())));
            if (appointment.getLongDateTime(appointment.getFromDate(), appointment.getTime()) < DateTimeUtil.getCurrentDateLong()) {
                containerBottomOptions.setVisibility(View.GONE);
            } else {
                containerBottomOptions.setVisibility(View.VISIBLE);
            }
        }
        tvDoctorName.setText(Util.getValidatedValue(appointment.getDoctorName()));
        if (!Util.isNullOrBlank(appointment.getClinicNumber())) {
            tvMobileNumber.setVisibility(View.VISIBLE);
            tvMobileNumber.setText(Util.getValidatedValue(appointment.getClinicNumber()));
        } else {
            tvMobileNumber.setText("");
            tvMobileNumber.setVisibility(View.GONE);
        }

        if (!Util.isNullOrBlank(appointment.getExplanation())) {
            tvNote.setVisibility(View.VISIBLE);
            tvNote.setText(Util.getValidatedValue(appointment.getExplanation()));
        } else
            tvNote.setVisibility(View.GONE);
    }

    @Override
    public View getContentView() {
        View view = (View) inflater.inflate(R.layout.list_item_appointment, null);
        tvAppointmentID = (TextView) view.findViewById(R.id.tv_aid);
        tvFromTime = (TextView) view.findViewById(R.id.tv_from_time);
        tvToTime = (TextView) view.findViewById(R.id.tv_to_time);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvDoctorName = (TextView) view.findViewById(R.id.tv_doctor_name);
        tvMobileNumber = (TextView) view.findViewById(R.id.tv_number);
        tvNote = (TextView) view.findViewById(R.id.tv_note);
        tvAppointmentStatus = (TextView) view.findViewById(R.id.tv_appointment_status);
        layoutButtons = (LinearLayout) view.findViewById(R.id.layout_buttons);
        btCancel = (LinearLayout) view.findViewById(R.id.bt_cancel);
        btRemind = (LinearLayout) view.findViewById(R.id.bt_remind);
        btConfirm = (LinearLayout) view.findViewById(R.id.bt_confirm);
        btReschedule = (LinearLayout) view.findViewById(R.id.bt_reschedule);
        containerBottomOptions = (LinearLayout) view.findViewById(R.id.container_bottom_options);

        tvDiscardedCancelled = (TextView) view.findViewById(R.id.tv_discarded);
        layoutDiscarded = (LinearLayout) view.findViewById(R.id.layout_discarded);
        btCancel.setOnClickListener(this);
        btConfirm.setOnClickListener(this);
        btReschedule.setOnClickListener(this);
        btRemind.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_cancel:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    showConfirmationAlert(v.getId(), null, mActivity.getResources().getString(R.string.confirm_cancel_appointment));
                } else
                    onNetworkUnavailable(null);
                break;
            case R.id.bt_remind:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    remindForAppointment();
                } else
                    onNetworkUnavailable(null);
                break;
            case R.id.bt_confirm:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    String fromTime = "";
                    String toTime = "";
                    String formattedMergedFromToTime = "";
                    WorkingHours workingHours = appointment.getTime();
                    if (workingHours != null) {
                        fromTime = DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getFromTime()));
                        toTime = DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getToTime()));
                    }
                    if (!Util.isNullOrBlank(fromTime) && !Util.isNullOrBlank(toTime)) {
                        formattedMergedFromToTime = mActivity.getResources().getString(R.string.from_with_spaces)
                                + fromTime
                                + mActivity.getResources().getString(R.string.to_with_spaces)
                                + toTime;
                    }
                    showConfirmationAlert(v.getId(), DateTimeUtil
                            .getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, appointment.getFromDate()), mActivity.getResources()
                            .getString(R.string.are_you_sure_confirm_appointment)
                            + formattedMergedFromToTime.trim() + "?");
                } else
                    onNetworkUnavailable(null);
                break;
            case R.id.bt_reschedule:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    openAddAppointmentScreen();
                } else
                    onNetworkUnavailable(null);
                break;
        }
    }

    private void remindForAppointment() {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).sendReminder(Boolean.class, appointment.getAppointmentId(), this, this);
    }

    private void openAddAppointmentScreen() {
        BookAppointmentDialogFragment dialogFragment = new BookAppointmentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(HealthCocoConstants.TAG_UNIQUE_ID, appointment.getAppointmentId());
        bundle.putParcelable(BookAppointmentDialogFragment.TAG_FROM_SCREEN_TYPE, Parcels.wrap(BookAppointmentFromScreenType.APPOINTMENTS_LIST_RESCHEDULE.ordinal()));
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(dialogFragment, PatientAppointmentDetailFragment.REQUEST_CODE_APPOINTMENTS_LIST);
        dialogFragment.show(mActivity.getSupportFragmentManager(), dialogFragment.getClass().getSimpleName());
    }

    private void cancelAppointment() {
        mActivity.showLoading(false);
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setDoctorId(appointment.getDoctorId());
        appointmentRequest.setHospitalId(appointment.getHospitalId());
        appointmentRequest.setLocationId(appointment.getLocationId());
        appointmentRequest.setPatientId(appointment.getPatientId());
        appointmentRequest.setState(AppointmentStatusType.CANCEL);
        appointmentRequest.setCancelledBy(CreatedByType.DOCTOR);
        appointmentRequest.setNotifyDoctorByEmail(true);
        appointmentRequest.setNotifyDoctorBySms(true);
        appointmentRequest.setNotifyPatientByEmail(true);
        appointmentRequest.setNotifyPatientBySms(true);
        appointmentRequest.setAppointmentId(appointment.getAppointmentId());
        WebDataServiceImpl.getInstance(mApp).addAppointment(CalendarEvents.class, appointmentRequest, this, this);
    }

    private void confirmAppointment() {
        mActivity.showLoading(false);
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setDoctorId(appointment.getDoctorId());
        appointmentRequest.setLocationId(appointment.getLocationId());
        appointmentRequest.setHospitalId(appointment.getHospitalId());
        appointmentRequest.setPatientId(appointment.getPatientId());
        appointmentRequest.setState(AppointmentStatusType.CONFIRM);
        appointmentRequest.setCreatedBy(CreatedByType.DOCTOR);
        appointmentRequest.setNotifyDoctorByEmail(true);
        appointmentRequest.setNotifyDoctorBySms(true);
        appointmentRequest.setNotifyPatientByEmail(true);
        appointmentRequest.setNotifyPatientBySms(true);
        appointmentRequest.setAppointmentId(appointment.getAppointmentId());
        WebDataServiceImpl.getInstance(mApp).addAppointment(CalendarEvents.class, appointmentRequest, this, this);

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
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case ADD_APPOINTMENT:
                    if (response.isValidData(response) && response.getData() instanceof CalendarEvents) {
                        appointment = (CalendarEvents) response.getData();
                        appointment.setIsAddedOnSuccess(true);
//                        LocalDataServiceImpl.getInstance(mApp).addAppointment(appointment);
                        applyData();
                    }
                    break;
                case SEND_REMINDER:
                    Util.showToast(mActivity, R.string.reminder_sent);
                    break;
                default:
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void showConfirmationAlert(final int viewId, String title, String msg) {
        if (Util.isNullOrBlank(title))
            title = mActivity.getResources().getString(R.string.confirm);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(msg);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (viewId) {
                    case R.id.bt_confirm:
                        confirmAppointment();
                        break;
                    case R.id.bt_cancel:
                        cancelAppointment();
                        break;
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
