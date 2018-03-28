package com.healthcoco.healthcocopad.viewholders;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.PatientCard;
import com.healthcoco.healthcocopad.enums.CalendarStatus;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.listeners.AppointmentDetailsPopupListener;
import com.healthcoco.healthcocopad.listeners.DoctorListPopupWindowListener;
import com.healthcoco.healthcocopad.listeners.QueueListitemlistener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by Prashant on 07/03/2018.
 */

public class QueueItemViewHolder extends HealthcocoComonRecylcerViewHolder implements View.OnClickListener, AppointmentDetailsPopupListener {
    private HealthCocoActivity mActivity;
    private CalendarEvents calendarEvents;

    private TextView tvTime;
    private TextView tvPatientName;
    private TextView tvMobileNumber;
    private TextView tvDoctorName;
    private LinearLayout btCheckIn;
    private LinearLayout btCheckOut;
    private LinearLayout btEngage;
    private LinearLayout layoutListItem;
    private QueueListitemlistener queueListitemlistener;

    public QueueItemViewHolder(HealthCocoActivity mActivity, View itemView, HealthcocoRecyclerViewItemClickListener itemClickListener, Object listenerObject) {
        super(mActivity, itemView, itemClickListener);
        this.mActivity = mActivity;
        this.queueListitemlistener = (QueueListitemlistener) listenerObject;
    }

    @Override
    public void initViews(View itemView) {
        tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        tvPatientName = (TextView) itemView.findViewById(R.id.tv_patient_name);
        tvMobileNumber = (TextView) itemView.findViewById(R.id.tv_mobile_number);
        tvDoctorName = (TextView) itemView.findViewById(R.id.tv_doctor_name);
        btCheckIn = (LinearLayout) itemView.findViewById(R.id.bt_check_in);
        btCheckOut = (LinearLayout) itemView.findViewById(R.id.bt_check_out);
        btEngage = (LinearLayout) itemView.findViewById(R.id.bt_engage);
        layoutListItem = (LinearLayout) itemView.findViewById(R.id.layout_item_queue);

        btCheckIn.setOnClickListener(this);
        btEngage.setOnClickListener(this);
        btCheckOut.setOnClickListener(this);

        btEngage.setVisibility(View.GONE);
        btCheckOut.setVisibility(View.GONE);
        btCheckIn.setVisibility(View.GONE);

        if (onItemClickListener != null)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(calendarEvents);
                }
            });

    }


    @Override
    public void applyData(Object object) {
        mActivity.initDoctorListPopupWindows(layoutListItem, object, this);

        this.calendarEvents = (CalendarEvents) object;
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
        queueListitemlistener.onCheckInClicked(calendarEvents);
    }


    @Override
    public void onVisitClicked(Object object) {

        HealthCocoConstants.SELECTED_PATIENTS_USER_ID = calendarEvents.getPatient().getUserId();
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_DETAIL.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_TAB_TYPE, PatientDetailTabType.PATIENT_DETAIL_VISIT.ordinal());
        startActivityForResult(mActivity, intent, 0, null);

    }

    @Override
    public void onInvoiceClicked(Object object) {
        HealthCocoConstants.SELECTED_PATIENTS_USER_ID = calendarEvents.getPatient().getUserId();
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_DETAIL.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_TAB_TYPE, PatientDetailTabType.PATIENT_DETAIL_INVOICE.ordinal());
        startActivityForResult(mActivity, intent, 0, null);

    }

    @Override
    public void onReceiptClicked(Object object) {
        HealthCocoConstants.SELECTED_PATIENTS_USER_ID = calendarEvents.getPatient().getUserId();
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_DETAIL.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_TAB_TYPE, PatientDetailTabType.PATIENT_DETAIL_RECEIPT.ordinal());
        startActivityForResult(mActivity, intent, 0, null);

    }

    @Override
    public void onEditClicked(Object object) {

    }

    @Override
    public void onRescheduleClicked(Object object) {

    }

    @Override
    public void onCancelClicked(Object object) {

    }
}