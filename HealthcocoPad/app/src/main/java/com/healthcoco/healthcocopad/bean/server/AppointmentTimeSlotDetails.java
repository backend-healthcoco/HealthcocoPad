package com.healthcoco.healthcocopad.bean.server;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by neha on 07/05/16.
 */
@Parcel
public class AppointmentTimeSlotDetails {
    private ArrayList<AvailableTimeSlots> slots;
    private AppointmentSlot appointmentSlot;

    public ArrayList<AvailableTimeSlots> getSlots() {
        return slots;
    }

    public void setSlots(ArrayList<AvailableTimeSlots> slots) {
        this.slots = slots;
    }

    public AppointmentSlot getAppointmentSlot() {
        return appointmentSlot;
    }

    public void setAppointmentSlot(AppointmentSlot appointmentSlot) {
        this.appointmentSlot = appointmentSlot;
    }
}
