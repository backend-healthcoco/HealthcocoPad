package com.healthcoco.healthcocopad.popupwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.WorkingHours;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.PatientCard;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.listeners.DoctorListPopupWindowListener;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.ContactsGridViewHolder;

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


    public AppointmentDetailsPopupWindow(Context context, View view, Object object, DoctorListPopupWindowListener doctorListPopupWindowListener) {
        this(context, view, object);
    }

    public AppointmentDetailsPopupWindow(Context context, View view, Object object) {
        super(context);
        this.mActivity = (HealthCocoActivity) context;
        this.dropDownLayoutId = dropDownLayoutId;
        this.anchorView = view;
        calendarEvents = (CalendarEvents) object;
    }

    public View getPopupView() {
        LinearLayout linearLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.popup_window_patient_appointment_details, null);

        tvPatientName = (TextView) linearLayout.findViewById(R.id.tv_contact_name);
        tvContactNumber = (TextView) linearLayout.findViewById(R.id.tv_contact_number);
        tvPatientId = (TextView) linearLayout.findViewById(R.id.tv_patient_id);
        tvGender = (TextView) linearLayout.findViewById(R.id.tv_patient_gender);
        tvDoctorName = (TextView) linearLayout.findViewById(R.id.tv_doctor_name);
        tvInitialAlphabet = (TextView) linearLayout.findViewById(R.id.tv_initial_aplhabet);
        ivContactProfile = (ImageView) linearLayout.findViewById(R.id.iv_image);
        tvFromTime = (TextView) linearLayout.findViewById(R.id.tv_from_time);
        tvToTime = (TextView) linearLayout.findViewById(R.id.tv_to_time);

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

    public void showOptionsWindow(View v) {
        showAsDropDown(v);
        update(v, 0, 0, anchorView.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void showOptionsWindowAtLeftCenter(View v) {
        showAtLocation(v, Gravity.LEFT, 0, 0);
        update(v, -v.getWidth() * 5 / 4, -v.getHeight() * 2, (int) mActivity.getResources().getDimension(R.dimen.doctor_name_textview_width), LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public void onClick(View v) {
        if (anchorView != null && anchorView.getId() == v.getId()) {
            showOptionsWindowAtLeftCenter(v);
        }
        switch (v.getId()) {
            case R.id.layout_select_all:

                break;

            case R.id.tv_apply:

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
            tvPatientId.setText(Util.getValidatedValue(patient.getUserId()));
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
    }


}
