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
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.Events;
import com.healthcoco.healthcocopad.bean.server.PatientCard;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
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
 * Created by Prashant on 30/05/18.
 */

public class EventDetailsPopupWindow extends PopupWindow implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    public static final String DATE_FORMAT_EVENT_POPUP = "EEEEEEEE, MMMM dd";
    private final String TAG = EventDetailsPopupWindow.class.getSimpleName();
    private final View anchorView;
    protected HealthCocoApplication mApp;
    private HealthCocoActivity mActivity;
    private int dropDownLayoutId;
    private Events events;
    private TextView tvPatientName;
    private TextView tvSubject;
    private TextView tvPatientId;
    private TextView tvGender;
    private TextView tvDoctorName;
    private TextView tvDate;
    private TextView tvFromTime;
    private TextView tvToTime;
    private TextView tvExplaination;
    private LinearLayout layoutExplaination;
    private LinearLayout layoutPatientDetails;
    private ImageView btEdit;
    private ImageView btPrint;
    private ImageView btDiscard;
    //    private FloatingActionButton btPrint;
    private ImageView btDismiss;
    private BookAppointmentFromScreenType screenType = APPOINTMENTS_QUEUE_RESCHEDULE;


    public EventDetailsPopupWindow(Context context, View view, Object object) {
        super(context);
        this.mActivity = (HealthCocoActivity) context;
        mApp = ((HealthCocoApplication) mActivity.getApplication());
        this.dropDownLayoutId = dropDownLayoutId;
        this.anchorView = view;
        events = (Events) object;
    }

    public View getPopupView() {
        FrameLayout linearLayout = (FrameLayout) mActivity.getLayoutInflater().inflate(R.layout.popup_window_event_details, null);

        tvPatientName = (TextView) linearLayout.findViewById(R.id.tv_patient_name);
        tvPatientId = (TextView) linearLayout.findViewById(R.id.tv_patient_id);
        tvGender = (TextView) linearLayout.findViewById(R.id.tv_patient_gender);
        tvDoctorName = (TextView) linearLayout.findViewById(R.id.tv_doctor_name);
        tvFromTime = (TextView) linearLayout.findViewById(R.id.tv_from_time);
        tvToTime = (TextView) linearLayout.findViewById(R.id.tv_to_time);
        tvSubject = (TextView) linearLayout.findViewById(R.id.tv_subject);
        tvDate = (TextView) linearLayout.findViewById(R.id.tv_date);
        tvExplaination = (TextView) linearLayout.findViewById(R.id.tv_explaination);
        layoutExplaination = (LinearLayout) linearLayout.findViewById(R.id.layout_explaination);
        layoutPatientDetails = (LinearLayout) linearLayout.findViewById(R.id.layout_patient_details);


        btEdit = (ImageView) linearLayout.findViewById(R.id.bt_edit_appointment);
        btDiscard = (ImageView) linearLayout.findViewById(R.id.bt_discard_appointment);
        btPrint = (ImageView) linearLayout.findViewById(R.id.bt_print_patient_card);
        btDismiss = (ImageView) linearLayout.findViewById(R.id.bt_dismiss);

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
        btPrint.setOnClickListener(this);
        btDiscard.setOnClickListener(this);
        btEdit.setOnClickListener(this);
        btDismiss.setOnClickListener(this);
        layoutPatientDetails.setOnClickListener(this);
    }

    public void showOptionsWindowAtLeftCenter(View v) {
        showAtLocation(v, Gravity.LEFT, 0, 0);
        update(v, -(int) mActivity.getResources().getDimension(R.dimen.queue_layout_width), -v.getHeight() * (5 / 2), (int) mActivity.getResources().getDimension(R.dimen.queue_layout_width), LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    public void showOptionsWindowAtRightCenter(View v) {
        int verticalOffset;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            verticalOffset = -v.getHeight() * (5 / 2);
        } else
            verticalOffset = -v.getHeight() * (5 / 2);

        showAtLocation(v, Gravity.RIGHT, 0, 0);
        update(v, (int) mActivity.getResources().getDimension(R.dimen.queue_layout_width), verticalOffset, (int) mActivity.getResources().getDimension(R.dimen.queue_layout_width), LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public void onClick(View v) {
        if (anchorView != null && anchorView.getId() == v.getId()) {
            showOptionsWindowAtLeftCenter(v);
        }
        switch (v.getId()) {
            case R.id.layout_patient_details:
                onPatientDatailsClicked();
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
//                mActivity.openPatientCardFragment(calendarEvents);
                dismiss();
                break;

            case R.id.bt_dismiss:
                dismiss();
                break;

        }
    }

    public void applyData() {

        if (events.getPatientCard() != null) {
            PatientCard patient = events.getPatientCard();
            LogUtils.LOGD(TAG, "Unique Id " + patient.getUniqueId());
            tvPatientId.setText(Util.getValidatedValue(patient.getPid()));
            String formattedGenderAge = Util.getFormattedGenderAge(patient);
            String patientName = Util.getValidatedValue(patient.getLocalPatientName());

            if (!Util.isNullOrBlank(patientName)) {
                tvPatientName.setText(patientName);
            } else {
                if (!Util.isNullOrBlank(events.getPatientCard().getFirstName()))
                    tvPatientName.setText(events.getPatientCard().getFirstName());
            }
            if (!Util.isNullOrBlank(formattedGenderAge)) {
                tvGender.setVisibility(View.VISIBLE);
                tvGender.setText(formattedGenderAge);
            } else {
                tvGender.setVisibility(View.GONE);
                tvGender.setText("");
            }
        } else layoutPatientDetails.setVisibility(View.GONE);

        String doctorName = Util.getValidatedValueOrNull(events.getDoctorName());
        if (!Util.isNullOrBlank(doctorName)) {
            tvDoctorName.setText(doctorName);
        } else {
            if (!Util.isNullOrBlank(events.getCreatedBy()))
                tvDoctorName.setText(events.getCreatedBy());
        }

        if (events.getTime() != null && events.getTime().getFromTime() != null && events.getTime().getToTime() != null) {
            WorkingHours workingHours = events.getTime();
            tvFromTime.setText(DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getFromTime())));
            tvToTime.setText(DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getToTime())));
        }

        if (DateTimeUtil.getStartTimeOfDayMilli(DateTimeUtil.getCurrentDateLong()) > events.getFromDate()) {
            btEdit.setVisibility(View.GONE);
            btDiscard.setVisibility(View.GONE);
        }
        if (!Util.isNullOrZeroNumber(events.getFromDate()))
            tvDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_EVENT_POPUP, events.getFromDate()));

        if (!Util.isNullOrBlank(events.getSubject()))
            tvSubject.setText(events.getSubject());

        if (!Util.isNullOrBlank(events.getExplanation()))
            tvExplaination.setText(events.getExplanation());
        else layoutExplaination.setVisibility(View.GONE);

    }

    public void onPatientDatailsClicked() {
        HealthCocoConstants.SELECTED_PATIENTS_USER_ID = events.getPatientCard().getUserId();
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_DETAIL.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_TAB_TYPE, PatientDetailTabType.PATIENT_DETAIL_PROFILE.ordinal());
        startActivityForResult(mActivity, intent, 0, null);

    }

    public void onEditClicked() {
       /* BookAppointmentDialogFragment dialogFragment = new BookAppointmentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(HealthCocoConstants.TAG_UNIQUE_ID, calendarEvents.getAppointmentId());
        bundle.putParcelable(BookAppointmentDialogFragment.TAG_FROM_SCREEN_TYPE, Parcels.wrap(screenType.ordinal()));
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(dialogFragment, PatientAppointmentDetailFragment.REQUEST_CODE_APPOINTMENTS_LIST);
        dialogFragment.show(mActivity.getSupportFragmentManager(), dialogFragment.getClass().getSimpleName());
*/
    }

    public void onCancelClicked() {
        showConfirmationAlert(null, mActivity.getResources().getString(R.string.confirm_cancel_appointment));
    }


    public void cancelAppointment() {

     /*   mActivity.showLoading(false);
        AppointmentRequestToSend appointmentRequestToSend = new AppointmentRequestToSend();
        appointmentRequestToSend.setDoctorId(events.getDoctorId());
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
  */
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case ADD_APPOINTMENT:
                    if (response.isValidData(response) && response.getData() instanceof CalendarEvents) {
                    /*     calendarEvents = (CalendarEvents) response.getData();
                       calendarEvents.setIsAddedOnSuccess(true);
                        LocalDataServiceImpl.getInstance(mApp).addAppointment(calendarEvents);
                        sendBroadcasts(calendarEvents.getAppointmentId());
                   */
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

   /* private void sendBroadcasts(String aptId) {
        Intent intent = new Intent();
        intent.setAction(QueueFragment.INTENT_GET_APPOINTMENT_LIST_LOCAL);
        intent.putExtra(TAG_APPOINTMENT_ID, aptId);
        LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
    }*/

}
