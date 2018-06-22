package com.healthcoco.healthcocopad.viewholders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AppointmentRequestToSend;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.PatientCard;
import com.healthcoco.healthcocopad.dialogFragment.BookAppointmentDialogFragment;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.BookAppointmentFromScreenType;
import com.healthcoco.healthcocopad.enums.CalendarStatus;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.CreatedByType;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.PatientAppointmentDetailFragment;
import com.healthcoco.healthcocopad.listeners.AppointmentDetailsPopupListener;
import com.healthcoco.healthcocopad.listeners.DoctorListPopupWindowListener;
import com.healthcoco.healthcocopad.listeners.QueueListitemlistener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;

import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static com.healthcoco.healthcocopad.enums.BookAppointmentFromScreenType.APPOINTMENTS_LIST_RESCHEDULE;

/**
 * Created by Prashant on 07/03/2018.
 */

public class QueueItemViewHolder extends HealthcocoComonRecylcerViewHolder implements View.OnClickListener {
    private HealthCocoActivity mActivity;
    private CalendarEvents calendarEvents;

    private TextView tvTime;
    private TextView tvPatientName;
    private TextView tvPatientId;
    private TextView tvMobileNumber;
    private TextView tvDoctorName;
    private LinearLayout btCheckIn;
    private LinearLayout btCheckOut;
    private LinearLayout btEngage;
    private LinearLayout layoutListItem;
    private QueueListitemlistener queueListitemlistener;
    private boolean pidHasDate;


    public QueueItemViewHolder(HealthCocoActivity mActivity, View itemView, HealthcocoRecyclerViewItemClickListener itemClickListener, Object listenerObject) {
        super(mActivity, itemView, itemClickListener);
        this.mActivity = mActivity;
        this.queueListitemlistener = (QueueListitemlistener) listenerObject;
        pidHasDate = queueListitemlistener.isPidHasDate();

    }

    @Override
    public void initViews(View itemView) {
        tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        tvPatientName = (TextView) itemView.findViewById(R.id.tv_patient_name);
        tvPatientId = (TextView) itemView.findViewById(R.id.tv_patient_id);
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
        mActivity.initAppointmentDetailsPopupWindows(layoutListItem, object, queueListitemlistener);

        calendarEvents = (CalendarEvents) object;
        if (calendarEvents != null) {
            String doctorName = "";
            String mobileNumber = "";
            String patientName = "";
            String patientId = "";

            switch (calendarEvents.getType()) {
                case APPOINTMENT:
                    if (calendarEvents.getPatient() != null) {
                        PatientCard patient = calendarEvents.getPatient();
                        patientName = Util.getValidatedValueOrNull(patient.getLocalPatientName());
                        if (pidHasDate && (!Util.isNullOrBlank(calendarEvents.getPatient().getPnum())))
                            patientId = Util.getValidatedValue(calendarEvents.getPatient().getPnum());
                        else
                            patientId = Util.getValidatedValue(calendarEvents.getPatient().getPid());

                        mobileNumber = Util.getValidatedValueOrNull(patient.getMobileNumber());
                        doctorName = Util.getValidatedValueOrNull(calendarEvents.getDoctorName());


                        if (!Util.isNullOrBlank(patientName)) {
                            tvPatientName.setText(patientName);
                        } else {
                            if (!Util.isNullOrBlank(calendarEvents.getPatient().getFirstName()))
                                tvPatientName.setText(calendarEvents.getPatient().getFirstName());
                        }

                        if (!Util.isNullOrBlank(patientId)) {
                            tvPatientId.setText(patientId);
                        } else {
                            tvPatientId.setText("");
                        }
                        if (!Util.isNullOrBlank(mobileNumber)) {
                            tvMobileNumber.setText(mobileNumber);
                            tvMobileNumber.setVisibility(View.VISIBLE);
                        } else tvMobileNumber.setVisibility(View.GONE);
                        if (!Util.isNullOrBlank(doctorName)) {
                            tvDoctorName.setText(doctorName);
                        } else {
                            if (!Util.isNullOrBlank(calendarEvents.getCreatedBy()))
                                tvDoctorName.setText(calendarEvents.getCreatedBy());
                        }
                    }
                    break;
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
                        btEngage.setVisibility(View.INVISIBLE);
                        break;
                }
            }

        }
    }


    @Override
    public void onClick(View v) {
        queueListitemlistener.onCheckInClicked(calendarEvents);
    }

}