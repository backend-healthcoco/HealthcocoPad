package com.healthcoco.healthcocopad.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.PatientCard;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.CalendarStatus;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Prashant on 07/03/2018.
 */

public class QueueItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private HealthCocoActivity mActivity;
    private CalendarEvents calendarEvents;

    private TextView tvTime;
    private TextView tvPatientName;
    private TextView tvMobileNumber;
    private TextView tvDoctorName;
    private LinearLayout btCheckIn;
    private LinearLayout btCheckOut;
    private LinearLayout btEngage;

    public QueueItemViewHolder(HealthCocoActivity mActivity, View itemView) {
        super(itemView);
        this.mActivity = mActivity;
        initViews(itemView);
    }

    private void initViews(View itemView) {
        tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        tvPatientName = (TextView) itemView.findViewById(R.id.tv_patient_name);
        tvMobileNumber = (TextView) itemView.findViewById(R.id.tv_mobile_number);
        tvDoctorName = (TextView) itemView.findViewById(R.id.tv_doctor_name);
        btCheckIn = (LinearLayout) itemView.findViewById(R.id.bt_check_in);
        btCheckOut = (LinearLayout) itemView.findViewById(R.id.bt_check_out);
        btEngage = (LinearLayout) itemView.findViewById(R.id.bt_engage);

        btEngage.setVisibility(View.GONE);
        btCheckOut.setVisibility(View.GONE);
        btCheckIn.setVisibility(View.GONE);
    }

    public void setData(Object object) {
        this.calendarEvents = (CalendarEvents) object;
    }

    public void applyData() {
        if (calendarEvents != null) {
            String doctorName = "";
            String mobileNumber = "";
            String patientName = "";

            switch (calendarEvents.getType()) {
                case APPOINTMENT:
                    if (calendarEvents.getPatient() != null) {
                        PatientCard patient = calendarEvents.getPatient();
                        patientName = Util.getValidatedValueOrNull(patient.getLocalPatientName());
                        mobileNumber = Util.getValidatedValueOrNull(patient.getMobileNumber());
                        doctorName = Util.getValidatedValueOrNull(calendarEvents.getDoctorName());
                    }
                    break;
            }
            if (!Util.isNullOrBlank(patientName)) {
                tvPatientName.setText(patientName);
            }
            if (!Util.isNullOrBlank(mobileNumber)) {
                tvMobileNumber.setText(mobileNumber);
            }
            if (!Util.isNullOrBlank(doctorName)) {
                tvDoctorName.setText(doctorName);
            }

            CalendarStatus calendarEventsStatus = calendarEvents.getStatus();
            if (calendarEventsStatus != null) {
                switch (calendarEventsStatus) {
                    case SCHEDULED:
                        if (calendarEvents.getTime() != null && calendarEvents.getTime().getFromTime() != null) {
                            tvTime.setText(DateTimeUtil.getFormattedTime(0, Math.round(calendarEvents.getTime().getFromTime())));
                        }
                        btCheckIn.setVisibility(View.VISIBLE);
                        break;
                    case ENGAGED:
                        tvTime.setText(DateTimeUtil.getFormttedTime(calendarEvents.getEngagedAt()));
                        btCheckOut.setVisibility(View.VISIBLE);
                        break;
                    case WAITING:
                        tvTime.setText(DateTimeUtil.getFormttedTime(calendarEvents.getCheckedInAt()));
                        btEngage.setVisibility(View.VISIBLE);
                        break;
                    case CHECKED_OUT:
                        tvTime.setText(DateTimeUtil.getFormttedTime(calendarEvents.getCheckedOutAt()));
                        break;
                }
            }

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.iv_image:
//                mActivity.openEnlargedImageDialogFragment(fileRecords.getRecordsUrl());
//                break;
        }
    }
}