package com.healthcoco.healthcocopad.bean.server;


import com.healthcoco.healthcocopad.enums.NotificationContentType;
import com.healthcoco.healthcocopad.utilities.ObjectParcelConvertor;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

/**
 * Created by neha on 03/07/16.
 */
@Parcel
public class NotificationResponse {

    private String title;

    private String text;

    private String img;

    private NotificationContentType notificationType;

    //represents PrescriptionId
    private String xi;

    //represents ReportId
    private String ri;

    //represents PatientId
    private String pi;

    //represents DoctorId
    private String di;

    //represents AppointmentId
    private String ai;

    private long createdTime;
    @ParcelPropertyConverter(ObjectParcelConvertor.class)
    private Object data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public NotificationContentType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationContentType notificationType) {
        this.notificationType = notificationType;
    }

    public String getXi() {
        return xi;
    }

    public void setXi(String xI) {
        xi = xI;
    }

    public String getRi() {
        return ri;
    }

    public void setRi(String rI) {
        ri = rI;
    }

    public String getPi() {
        return pi;
    }

    public void setPi(String pI) {
        pi = pI;
    }

    public String getDi() {
        return di;
    }

    public void setDi(String dI) {
        di = dI;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getAi() {
        return ai;
    }

    public void setAi(String ai) {
        this.ai = ai;
    }
}
