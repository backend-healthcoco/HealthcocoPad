package com.healthcoco.healthcocopad.popupwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.PatientCard;
import com.healthcoco.healthcocopad.enums.CalendarStatus;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.listeners.AppointmentDetailsPopupListener;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by neha on 03/05/17.
 */

public class AppointmentDetailsPopupWindow extends PopupWindow implements View.OnClickListener {
    private final String TAG = AppointmentDetailsPopupWindow.class.getSimpleName();

    private final View anchorView;
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
    private ImageView btDiscard;
    private FloatingActionButton btPrint;
    private ImageView btDismiss;
    private AppointmentDetailsPopupListener popupListener;


    public AppointmentDetailsPopupWindow(Context context, View view, Object object, AppointmentDetailsPopupListener popupListener) {
        super(context);
        this.mActivity = (HealthCocoActivity) context;
        this.dropDownLayoutId = dropDownLayoutId;
        this.anchorView = view;
        this.popupListener = popupListener;
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

        layoutScheduledAt = (LinearLayout) linearLayout.findViewById(R.id.layout_scheduled_at);
        layoutCheckIn = (LinearLayout) linearLayout.findViewById(R.id.layout_check_in);
        layoutWaitedFor = (LinearLayout) linearLayout.findViewById(R.id.layout_waited_for);
        layoutEngagedAt = (LinearLayout) linearLayout.findViewById(R.id.layout_engaged_at);
        layoutEngagedFor = (LinearLayout) linearLayout.findViewById(R.id.layout_engaged_for);
        layoutTotalTime = (LinearLayout) linearLayout.findViewById(R.id.layout_total_time);
        layoutCheckOut = (LinearLayout) linearLayout.findViewById(R.id.layout_check_out);
        btAddVisit = (LinearLayout) linearLayout.findViewById(R.id.bt_add_visit);
        btAddInvoice = (LinearLayout) linearLayout.findViewById(R.id.bt_add_invoice);
        btAddReceipt = (LinearLayout) linearLayout.findViewById(R.id.bt_add_receipt);

        btEdit = (ImageView) linearLayout.findViewById(R.id.bt_edit_appointment);
        btDiscard = (ImageView) linearLayout.findViewById(R.id.bt_discard_appointment);
        btPrint = (FloatingActionButton) linearLayout.findViewById(R.id.fl_bt_print);
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
    }

    public void showOptionsWindow(View v) {
        showAsDropDown(v);
        update(v, 0, 0, anchorView.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
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
                popupListener.onVisitClicked();
                dismiss();
                break;

            case R.id.bt_add_invoice:
                popupListener.onInvoiceClicked();
                dismiss();
                break;

            case R.id.bt_add_receipt:
                popupListener.onReceiptClicked();
                dismiss();
                break;

            case R.id.bt_edit_appointment:
                popupListener.onEditClicked();
                dismiss();
                break;

            case R.id.bt_discard_appointment:
                popupListener.onCancelClicked();
                dismiss();
                break;

            case R.id.fl_bt_print:
                mActivity.openPatientCardFragment(calendarEvents);
                dismiss();
                break;

            case R.id.bt_dismiss:
                dismiss();
                break;

        }
    }

    public void applyData() {

        if (calendarEvents.getPatient() != null) {
            PatientCard patient = calendarEvents.getPatient();
            LogUtils.LOGD(TAG, "Unique Id " + patient.getUniqueId());
            tvPatientName.setText(Util.getValidatedValue(patient.getLocalPatientName()));
            tvContactNumber.setText(Util.getValidatedValue(patient.getMobileNumber()));
            tvPatientId.setText(Util.getValidatedValue(patient.getPid()));
            String formattedGenderAge = Util.getFormattedGenderAge(patient);
            if (!Util.isNullOrBlank(formattedGenderAge)) {
                tvGender.setVisibility(View.VISIBLE);
                tvGender.setText(formattedGenderAge);
            } else {
                tvGender.setVisibility(View.GONE);
                tvGender.setText("");
            }
            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENTS_LIST, patient, null, ivContactProfile, tvInitialAlphabet);
        }

        tvDoctorName.setText(Util.getValidatedValue(calendarEvents.getDoctorName()));

        if (calendarEvents.getTime() != null && calendarEvents.getTime().getFromTime() != null && calendarEvents.getTime().getToTime() != null) {
            WorkingHours workingHours = calendarEvents.getTime();
            tvFromTime.setText(DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getFromTime())));
            tvToTime.setText(DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getToTime())));
        }

        CalendarStatus calendarEventsStatus = calendarEvents.getStatus();
        if (calendarEventsStatus != null) {
            switch (calendarEventsStatus) {
                case SCHEDULED:
                    if (calendarEvents.getTime() != null && calendarEvents.getTime().getFromTime() != null) {
                        tvScheduledAt.setText(DateTimeUtil.getFormattedTime(0, Math.round(calendarEvents.getTime().getFromTime())));
                    }
                    layoutScheduledAt.setVisibility(View.VISIBLE);
                    break;
                case WAITING:
                    tvCheckIn.setText(DateTimeUtil.getFormttedTime(calendarEvents.getCheckedInAt()));
                    tvWaitedFor.setText(calendarEvents.getWaitedForInMin());
                    layoutCheckIn.setVisibility(View.VISIBLE);
                    layoutWaitedFor.setVisibility(View.VISIBLE);
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
                    break;

            }
        }
    }


}
