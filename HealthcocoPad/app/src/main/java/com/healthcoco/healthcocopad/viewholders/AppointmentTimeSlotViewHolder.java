package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.AvailableTimeSlots;
import com.healthcoco.healthcocopad.dialogFragment.BookAppointmentDialogFragment;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;


/**
 * Created by neha on 10/10/16.
 */
public class AppointmentTimeSlotViewHolder extends HealthCocoViewHolder {

    private AvailableTimeSlots availableTimeSlots;
    private TextView textViewTimeSlot;
    private View convertView;
    private TextView tvBullet;

    public AppointmentTimeSlotViewHolder(HealthCocoActivity activity) {
        super(activity);
    }

    @Override
    public void setData(Object object) {
        availableTimeSlots = (AvailableTimeSlots) object;
    }

    @Override
    public void applyData() {
        String text = DateTimeUtil.convertFormattedDate(BookAppointmentDialogFragment.TIME_FORMAT_RECEIVED_FROM_SERVER,
                BookAppointmentDialogFragment.TIME_SLOT_FORMAT_USED_IN_THIS_SCREEN, availableTimeSlots.getTime());
        textViewTimeSlot.setText(text);
    }

    private void refreshViewState() {
        if (availableTimeSlots.getIsAvailable() != null && availableTimeSlots.getIsAvailable()) {
            tvBullet.setVisibility(View.VISIBLE);
            textViewTimeSlot.setEnabled(true);
            convertView.setBackgroundResource(R.drawable.shape_rounded_corners_grey_border_solid_white);
        } else {
            tvBullet.setVisibility(View.GONE);
            textViewTimeSlot.setEnabled(false);
            convertView.setBackgroundResource(R.drawable.shape_rounded_corners_grey_border_solid_lighter_grey);
        }
    }

    @Override
    public View getContentView() {
        convertView = inflater.inflate(R.layout.grid_item_appointment_time_slot, null);
        tvBullet = (TextView) convertView.findViewById(R.id.tv_bullet);
        textViewTimeSlot = (TextView) convertView.findViewById(R.id.tv_text);
        return convertView;
    }

    public void setSelected(boolean b) {
        if (b) {
            tvBullet.setVisibility(View.GONE);
            textViewTimeSlot.setEnabled(true);
            convertView.setBackgroundResource(R.drawable.shape_rounded_rectangle_green_translucent);
        } else
            refreshViewState();
    }
}
