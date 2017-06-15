package com.healthcoco.healthcocopad.bean.request;

import com.orm.annotation.Unique;

import java.util.List;

/**
 * Created by Shreshtha on 15-06-2017.
 */

public class AddEditDoctorContactRequest {
    @Unique
    private String doctorId;
    private String mobileNumber;
    private List<String> additionalNumbers;
    private List<String> otherEmailAddresses;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public List<String> getAdditionalNumbers() {
        return additionalNumbers;
    }

    public void setAdditionalNumbers(List<String> additionalNumbers) {
        this.additionalNumbers = additionalNumbers;
    }

    public List<String> getOtherEmailAddresses() {
        return otherEmailAddresses;
    }

    public void setOtherEmailAddresses(List<String> otherEmailAddresses) {
        this.otherEmailAddresses = otherEmailAddresses;
    }
}
