package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.Events;
import com.healthcoco.healthcocopad.bean.server.PatientCard;
import com.healthcoco.healthcocopad.enums.CalendarStatus;
import com.healthcoco.healthcocopad.fragments.QueueFragment;
import com.healthcoco.healthcocopad.listeners.QueueListitemlistener;
import com.healthcoco.healthcocopad.popupwindow.EventDetailsPopupWindow;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Prashant on 29/05/2018.
 */

public class EventItemViewHolder extends HealthcocoComonRecylcerViewHolder {

    public static final String DATE_FORMAT = "MMM dd";
    public static final String DATE_FORMAT_FOR_DAY = "EEE";

    private HealthCocoActivity mActivity;
    private Events events;


    private TextView tvDay;
    private TextView tvTime;
    private TextView tvDate;
    private TextView tvSubject;
    private TextView tvNote;
    private TextView tvDoctorName;
    private LinearLayout layoutListItem;
    private LinearLayout layoutEventDetails;

    public EventItemViewHolder(HealthCocoActivity mActivity, View itemView, HealthcocoRecyclerViewItemClickListener itemClickListener, Object listenerObject) {
        super(mActivity, itemView, itemClickListener);
        this.mActivity = mActivity;
    }

    @Override
    public void initViews(View itemView) {
        tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        tvDay = (TextView) itemView.findViewById(R.id.tv_day);
        tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        tvDoctorName = (TextView) itemView.findViewById(R.id.tv_doctor_name);
        tvSubject = (TextView) itemView.findViewById(R.id.tv_subject);
        tvNote = (TextView) itemView.findViewById(R.id.tv_Note);
        layoutListItem = (LinearLayout) itemView.findViewById(R.id.layout_item_event);
        layoutEventDetails = (LinearLayout) itemView.findViewById(R.id.layout_event_details);


        if (onItemClickListener != null)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(events);
                }
            });

     /*   layoutEventDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventDetailOnClick(layoutEventDetails);
            }
        });*/
    }

    @Override
    public void applyData(Object object) {

        events = (Events) object;
        if (events != null) {
            String doctorName = "";
            String subject = "";
            String fromTime = "";
            String toTime = "";
            String date = "";

            if (events.getForDoctor() != null) {
                doctorName = Util.getValidatedValueOrNull(events.getForDoctor());
                tvDoctorName.setText(doctorName);
            } else
                tvDoctorName.setText("");

            if (events.getSubject() != null) {
                subject = Util.getValidatedValueOrNull(events.getSubject());
                tvSubject.setText(subject);
            } else
                tvSubject.setText("");

            if (events.getExplanation() != null) {
                tvNote.setVisibility(View.VISIBLE);
                if ((!Util.isNullOrBlank(events.getExplanation())))
                    tvNote.setText(Util.getValidatedValueOrNull(events.getExplanation()));
                else
                    tvNote.setVisibility(View.GONE);
            } else
                tvNote.setVisibility(View.GONE);

            if (events.getTime() != null && events.getTime().getFromTime() != null) {
                fromTime = DateTimeUtil.getFormattedTime(0, Math.round(events.getTime().getFromTime()));
            }
            if (events.getTime() != null && events.getTime().getToTime() != null) {
                toTime = DateTimeUtil.getFormattedTime(0, Math.round(events.getTime().getToTime()));
            }
            tvTime.setText(fromTime + " - " + toTime);

            if (!Util.isNullOrZeroNumber(events.getFromDate())) {
                tvDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT, events.getFromDate()));
                tvDay.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_FOR_DAY, events.getFromDate()));
            }
        }

    }

    private void eventDetailOnClick(View v) {
        EventDetailsPopupWindow eventDetailsPopupWindow = new EventDetailsPopupWindow(mActivity, null, events);
        eventDetailsPopupWindow.setOutsideTouchable(true);
        eventDetailsPopupWindow.setContentView(eventDetailsPopupWindow.getPopupView());
//        y = x + y;
        eventDetailsPopupWindow.showOptionsWindowAtRightCenter(v);
    }

}