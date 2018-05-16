package com.healthcoco.healthcocopad.popupwindow;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AppointmentRequestToSend;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.PatientCard;
import com.healthcoco.healthcocopad.dialogFragment.BookAppointmentDialogFragment;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.BookAppointmentFromScreenType;
import com.healthcoco.healthcocopad.enums.CalendarStatus;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.CreatedByType;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.PatientAppointmentDetailFragment;
import com.healthcoco.healthcocopad.fragments.QueueFragment;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static com.healthcoco.healthcocopad.dialogFragment.BookAppointmentDialogFragment.TAG_APPOINTMENT_ID;
import static com.healthcoco.healthcocopad.enums.BookAppointmentFromScreenType.APPOINTMENTS_QUEUE_RESCHEDULE;

/**
 * Created by neha on 03/05/17.
 */

public class AppointmentDetailsPopupWindow extends PopupWindow implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    private final String TAG = AppointmentDetailsPopupWindow.class.getSimpleName();

    private final View anchorView;
    protected HealthCocoApplication mApp;
    private HealthCocoActivity mActivity;
    private int dropDownLayoutId;
    private CalendarEvents calendarEvents;
    private TextView tvPatientName;
    private TextView tvContactNumber;
    private TextView tvPatientId;
    private TextView tvGender;
    private TextView tvDoctorName;
    private TextView tvInitialAlphabet;
    private ImageView ivContactProfile;
    private TextView tvFromTime;
    private TextView tvToTime;
    private TextView tvCheckIn;
    private TextView tvWaitedFor;
    private TextView tvEngagedAt;
    private TextView tvEngagedFor;
    private TextView tvTotalTime;
    private TextView tvCheckOut;
    private TextView tvScheduledAt;
    private TextView tvAppointmentStatus;
    private LinearLayout layoutAppointmentStatus;
    private LinearLayout layoutPatientDetails;
    private LinearLayout layoutScheduledAt;
    private LinearLayout layoutCheckIn;
    private LinearLayout layoutWaitedFor;
    private LinearLayout layoutEngagedAt;
    private LinearLayout layoutEngagedFor;
    private LinearLayout layoutTotalTime;
    private LinearLayout layoutCheckOut;
    private LinearLayout btAddVisit;
    private LinearLayout btAddInvoice;
    private LinearLayout btAddReceipt;
    private ImageView btEdit;
    private ImageView btPrint;
    private ImageView btDiscard;
    //    private FloatingActionButton btPrint;
    private ImageView btDismiss;
    private BookAppointmentFromScreenType screenType = APPOINTMENTS_QUEUE_RESCHEDULE;


    public AppointmentDetailsPopupWindow(Context context, View view, Object object) {
        super(context);
        this.mActivity = (HealthCocoActivity) context;
        mApp = ((HealthCocoApplication) mActivity.getApplication());
        this.dropDownLayoutId = dropDownLayoutId;
        this.anchorView = view;
        calendarEvents = (CalendarEvents) object;
    }

    public View getPopupView() {
        FrameLayout linearLayout = (FrameLayout) mActivity.getLayoutInflater().inflate(R.layout.popup_window_patient_appointment_details, null);

        tvPatientName = (TextView) linearLayout.findViewById(R.id.tv_contact_name);
        tvContactNumber = (TextView) linearLayout.findViewById(R.id.tv_contact_number);
        tvPatientId = (TextView) linearLayout.findViewById(R.id.tv_patient_id);
        tvGender = (TextView) linearLayout.findViewById(R.id.tv_patient_gender);
        tvDoctorName = (TextView) linearLayout.findViewById(R.id.tv_doctor_name);
        tvInitialAlphabet = (TextView) linearLayout.findViewById(R.id.tv_initial_aplhabet);
        ivContactProfile = (ImageView) linearLayout.findViewById(R.id.iv_image);
        tvFromTime = (TextView) linearLayout.findViewById(R.id.tv_from_time);
        tvToTime = (TextView) linearLayout.findViewById(R.id.tv_to_time);

        tvCheckIn = (TextView) linearLayout.findViewById(R.id.tv_check_in);
        tvWaitedFor = (TextView) linearLayout.findViewById(R.id.tv_waited_for);
        tvEngagedAt = (TextView) linearLayout.findViewById(R.id.tv_engaged_at);
        tvEngagedFor = (TextView) linearLayout.findViewById(R.id.tv_engaged_for);
        tvTotalTime = (TextView) linearLayout.findViewById(R.id.tv_total_time);
        tvCheckOut = (TextView) linearLayout.findViewById(R.id.tv_check_out);
        tvScheduledAt = (TextView) linearLayout.findViewById(R.id.tv_scheduled_at);
        tvAppointmentStatus = (TextView) linearLayout.findViewById(R.id.tv_appointment_status);

        layoutPatientDetails = (LinearLayout) linearLayout.findViewById(R.id.layout_patient_details);
        layoutScheduledAt = (LinearLayout) linearLayout.findViewById(R.id.layout_scheduled_at);
        layoutCheckIn = (LinearLayout) linearLayout.findViewById(R.id.layout_check_in);
        layoutWaitedFor = (LinearLayout) linearLayout.findViewById(R.id.layout_waited_for);
        layoutEngagedAt = (LinearLayout) linearLayout.findViewById(R.id.layout_engaged_at);
        layoutEngagedFor = (LinearLayout) linearLayout.findViewById(R.id.layout_engaged_for);
        layoutTotalTime = (LinearLayout) linearLayout.findViewById(R.id.layout_total_time);
        layoutCheckOut = (LinearLayout) linearLayout.findViewById(R.id.layout_check_out);
        layoutAppointmentStatus = (LinearLayout) linearLayout.findViewById(R.id.layout_appointment_status);

        btAddVisit = (LinearLayout) linearLayout.findViewById(R.id.bt_add_visit);
        btAddInvoice = (LinearLayout) linearLayout.findViewById(R.id.bt_add_invoice);
        btAddReceipt = (LinearLayout) linearLayout.findViewById(R.id.bt_add_receipt);

        btEdit = (ImageView) linearLayout.findViewById(R.id.bt_edit_appointment);
        btDiscard = (ImageView) linearLayout.findViewById(R.id.bt_discard_appointment);
        btPrint = (ImageView) linearLayout.findViewById(R.id.bt_print_patient_card);
        btDismiss = (ImageView) linearLayout.findViewById(R.id.bt_dismiss);

        layoutScheduledAt.setVisibility(View.GONE);
        layoutCheckIn.setVisibility(View.GONE);
        layoutWaitedFor.setVisibility(View.GONE);
        layoutEngagedAt.setVisibility(View.GONE);
        layoutEngagedFor.setVisibility(View.GONE);
        layoutTotalTime.setVisibility(View.GONE);
        layoutCheckOut.setVisibility(View.GONE);

        initListener();

        applyData();

        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(false);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
//        setInputMethodMode(ListPopupWindow.INPUT_METHOD_NEEDED);
        if (anchorView != null)
            anchorView.setOnClickListener(this);

        return linearLayout;
    }

    private void initListener() {
        btAddVisit.setOnClickListener(this);
        btAddInvoice.setOnClickListener(this);
        btAddReceipt.setOnClickListener(this);

        btPrint.setOnClickListener(this);
        btDiscard.setOnClickListener(this);
        btEdit.setOnClickListener(this);
        btDismiss.setOnClickListener(this);
        layoutPatientDetails.setOnClickListener(this);
        tvAppointmentStatus.setOnClickListener(this);
    }

    public void showOptionsWindow(View v, int x, int y) {
//        showAsDropDown(v);
        int verticalOffset;
        showAtLocation(v, Gravity.LEFT | Gravity.TOP, 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            verticalOffset = y;
        } else
            verticalOffset = -(v.getHeight() * 86) / 100;

        update(v, x, verticalOffset, (int) mActivity.getResources().getDimension(R.dimen.queue_layout_width), LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void showOptionsWindowAtLeftCenter(View v) {
        showAtLocation(v, Gravity.LEFT, 0, 0);
        update(v, -(int) mActivity.getResources().getDimension(R.dimen.queue_layout_width), -v.getHeight() * (5 / 2), (int) mActivity.getResources().getDimension(R.dimen.queue_layout_width), LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public void onClick(View v) {
        if (anchorView != null && anchorView.getId() == v.getId()) {
            showOptionsWindowAtLeftCenter(v);
        }
        switch (v.getId()) {
            case R.id.bt_add_visit:
                onVisitClicked();
//                dismiss();
                break;

            case R.id.bt_add_invoice:
                onInvoiceClicked();
//                dismiss();
                break;

            case R.id.layout_patient_details:
                onPatientDatailsClicked();
//                dismiss();
                break;

            case R.id.bt_add_receipt:
                onReceiptClicked();
//                dismiss();
                break;

            case R.id.bt_edit_appointment:
                onEditClicked();
                dismiss();
                break;

            case R.id.bt_discard_appointment:
                onCancelClicked();
//                dismiss();
                break;

            case R.id.bt_print_patient_card:
                mActivity.openPatientCardFragment(calendarEvents);
                dismiss();
                break;

            case R.id.bt_dismiss:
                dismiss();
                break;
            case R.id.tv_appointment_status:
                mActivity.openChangeAppointmentStatusDialogFragment(calendarEvents.getAppointmentId());
                break;

        }
    }

    public void applyData() {

        if (calendarEvents.getPatient() != null) {
            PatientCard patient = calendarEvents.getPatient();
            LogUtils.LOGD(TAG, "Unique Id " + patient.getUniqueId());
            tvContactNumber.setText(Util.getValidatedValue(patient.getMobileNumber()));
            tvPatientId.setText(Util.getValidatedValue(patient.getPid()));
            String formattedGenderAge = Util.getFormattedGenderAge(patient);
            String patientName = Util.getValidatedValue(patient.getLocalPatientName());

            if (!Util.isNullOrBlank(patientName)) {
                tvPatientName.setText(patientName);
            } else {
                if (!Util.isNullOrBlank(calendarEvents.getPatient().getFirstName()))
                    tvPatientName.setText(calendarEvents.getPatient().getFirstName());
            }
            if (!Util.isNullOrBlank(formattedGenderAge)) {
                tvGender.setVisibility(View.VISIBLE);
                tvGender.setText(formattedGenderAge);
            } else {
                tvGender.setVisibility(View.GONE);
                tvGender.setText("");
            }
            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENTS_LIST, patient, null, ivContactProfile, tvInitialAlphabet);
        }

        String doctorName = Util.getValidatedValueOrNull(calendarEvents.getDoctorName());
        if (!Util.isNullOrBlank(doctorName)) {
            tvDoctorName.setText(doctorName);
        } else {
            if (!Util.isNullOrBlank(calendarEvents.getCreatedBy()))
                tvDoctorName.setText(calendarEvents.getCreatedBy());
        }

        if (calendarEvents.getTime() != null && calendarEvents.getTime().getFromTime() != null && calendarEvents.getTime().getToTime() != null) {
            WorkingHours workingHours = calendarEvents.getTime();
            tvFromTime.setText(DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getFromTime())));
            tvToTime.setText(DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getToTime())));
        }

        if (DateTimeUtil.getStartTimeOfDayMilli(DateTimeUtil.getCurrentDateLong()) > calendarEvents.getFromDate()) {
            btEdit.setVisibility(View.GONE);
            btDiscard.setVisibility(View.GONE);
        }

        CalendarStatus calendarEventsStatus = calendarEvents.getStatus();
        if (calendarEventsStatus != null) {
            tvAppointmentStatus.setText(calendarEventsStatus.getValue());
            switch (calendarEventsStatus) {
                case SCHEDULED:
                    if (calendarEvents.getTime() != null && calendarEvents.getTime().getFromTime() != null) {
                        tvScheduledAt.setText(DateTimeUtil.getFormattedTime(0, Math.round(calendarEvents.getTime().getFromTime())));
                    }
                    layoutScheduledAt.setVisibility(View.VISIBLE);
                    tvAppointmentStatus.setTextColor(mActivity.getResources().getColor(R.color.orange));
                    break;
                case WAITING:
                    tvCheckIn.setText(DateTimeUtil.getFormttedTime(calendarEvents.getCheckedInAt()));
                    tvWaitedFor.setText(calendarEvents.getWaitedForInMin());
                    layoutCheckIn.setVisibility(View.VISIBLE);
                    layoutWaitedFor.setVisibility(View.VISIBLE);
                    tvAppointmentStatus.setTextColor(mActivity.getResources().getColor(R.color.red_error));
                    break;
                case ENGAGED:
                    tvCheckIn.setText(DateTimeUtil.getFormttedTime(calendarEvents.getEngagedAt()));
                    tvWaitedFor.setText(calendarEvents.getWaitedForInMin());
                    tvEngagedAt.setText(DateTimeUtil.getFormttedTime(calendarEvents.getEngagedAt()));
                    tvEngagedFor.setText(calendarEvents.getEngagedForInMin());
                    layoutCheckIn.setVisibility(View.VISIBLE);
                    layoutWaitedFor.setVisibility(View.VISIBLE);
                    layoutEngagedAt.setVisibility(View.VISIBLE);
                    layoutEngagedFor.setVisibility(View.VISIBLE);
                    btEdit.setVisibility(View.GONE);
                    btDiscard.setVisibility(View.GONE);
                    tvAppointmentStatus.setTextColor(mActivity.getResources().getColor(R.color.green_logo));
                    break;
                case CHECKED_OUT:
                    tvTotalTime.setText(calendarEvents.getTotalTimeInMin());
                    tvCheckOut.setText(DateTimeUtil.getFormttedTime(calendarEvents.getCheckedOutAt()));
                    tvCheckIn.setText(DateTimeUtil.getFormttedTime(calendarEvents.getEngagedAt()));
                    tvWaitedFor.setText(calendarEvents.getWaitedForInMin());
                    tvEngagedFor.setText(calendarEvents.getEngagedForInMin());
                    layoutCheckIn.setVisibility(View.VISIBLE);
                    layoutWaitedFor.setVisibility(View.VISIBLE);
                    layoutEngagedFor.setVisibility(View.VISIBLE);
                    layoutTotalTime.setVisibility(View.VISIBLE);
                    layoutCheckOut.setVisibility(View.VISIBLE);
                    btAddVisit.setVisibility(View.INVISIBLE);
                    btEdit.setVisibility(View.GONE);
                    btDiscard.setVisibility(View.GONE);
                    tvAppointmentStatus.setTextColor(mActivity.getResources().getColor(R.color.blue_action_bar));
                    break;

            }
        }
    }

    public void onReceiptClicked() {
        HealthCocoConstants.SELECTED_PATIENTS_USER_ID = calendarEvents.getPatient().getUserId();
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_DETAIL.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_TAB_TYPE, PatientDetailTabType.PATIENT_DETAIL_RECEIPT.ordinal());
        startActivityForResult(mActivity, intent, 0, null);

    }

    public void onVisitClicked() {
        HealthCocoConstants.SELECTED_PATIENTS_USER_ID = calendarEvents.getPatient().getUserId();
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_DETAIL.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_TAB_TYPE, PatientDetailTabType.PATIENT_DETAIL_VISIT.ordinal());
        startActivityForResult(mActivity, intent, 0, null);

    }

    public void onInvoiceClicked() {
        HealthCocoConstants.SELECTED_PATIENTS_USER_ID = calendarEvents.getPatient().getUserId();
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_DETAIL.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_TAB_TYPE, PatientDetailTabType.PATIENT_DETAIL_INVOICE.ordinal());
        startActivityForResult(mActivity, intent, 0, null);

    }

    public void onPatientDatailsClicked() {
        HealthCocoConstants.SELECTED_PATIENTS_USER_ID = calendarEvents.getPatient().getUserId();
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_DETAIL.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_TAB_TYPE, PatientDetailTabType.PATIENT_DETAIL_PROFILE.ordinal());
        startActivityForResult(mActivity, intent, 0, null);

    }

    public void onEditClicked() {
        BookAppointmentDialogFragment dialogFragment = new BookAppointmentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(HealthCocoConstants.TAG_UNIQUE_ID, calendarEvents.getAppointmentId());
        bundle.putParcelable(BookAppointmentDialogFragment.TAG_FROM_SCREEN_TYPE, Parcels.wrap(screenType.ordinal()));
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(dialogFragment, PatientAppointmentDetailFragment.REQUEST_CODE_APPOINTMENTS_LIST);
        dialogFragment.show(mActivity.getSupportFragmentManager(), dialogFragment.getClass().getSimpleName());

    }

    public void onCancelClicked() {
        showConfirmationAlert(null, mActivity.getResources().getString(R.string.confirm_cancel_appointment));
    }


    public void cancelAppointment() {

        mActivity.showLoading(false);
        AppointmentRequestToSend appointmentRequestToSend = new AppointmentRequestToSend();
        appointmentRequestToSend.setDoctorId(calendarEvents.getDoctorId());
        appointmentRequestToSend.setHospitalId(calendarEvents.getHospitalId());
        appointmentRequestToSend.setLocationId(calendarEvents.getLocationId());
        appointmentRequestToSend.setPatientId(calendarEvents.getPatientId());
        appointmentRequestToSend.setState(AppointmentStatusType.CANCEL);
        appointmentRequestToSend.setCancelledBy(CreatedByType.DOCTOR);
        appointmentRequestToSend.setNotifyDoctorByEmail(true);
        appointmentRequestToSend.setNotifyDoctorBySms(true);
        appointmentRequestToSend.setNotifyPatientByEmail(true);
        appointmentRequestToSend.setNotifyPatientBySms(true);
        appointmentRequestToSend.setAppointmentId(calendarEvents.getAppointmentId());
        WebDataServiceImpl.getInstance(mApp).addAppointment(CalendarEvents.class, appointmentRequestToSend, this, this);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case ADD_APPOINTMENT:
                    if (response.isValidData(response) && response.getData() instanceof CalendarEvents) {
                        calendarEvents = (CalendarEvents) response.getData();
                        calendarEvents.setIsAddedOnSuccess(true);
                        LocalDataServiceImpl.getInstance(mApp).addAppointment(calendarEvents);
                        sendBroadcasts(calendarEvents.getAppointmentId());
                        dismiss();
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

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {

    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {

    }

    private void showConfirmationAlert(String title, String msg) {
        if (Util.isNullOrBlank(title))
            title = mActivity.getResources().getString(R.string.confirm);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(msg);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelAppointment();
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

    private void sendBroadcasts(String aptId) {
        Intent intent = new Intent();
        intent.setAction(QueueFragment.INTENT_GET_APPOINTMENT_LIST_LOCAL);
        intent.putExtra(TAG_APPOINTMENT_ID, aptId);
        LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
    }

}
