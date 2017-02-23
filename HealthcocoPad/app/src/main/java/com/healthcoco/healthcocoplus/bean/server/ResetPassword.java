package com.healthcoco.healthcocoplus.bean.server;

/**
 * Created by Shreshtha on 23-01-2017.
 */
public class ResetPassword {
    private String mobileNumber;
    private String emailAddress;
    private String username;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
