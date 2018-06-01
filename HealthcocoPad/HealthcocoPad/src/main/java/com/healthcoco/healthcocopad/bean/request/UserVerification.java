package com.healthcoco.healthcocopad.bean.request;

import com.healthcoco.healthcocopad.bean.DOB;
import com.healthcoco.healthcocopad.bean.server.AssignedUserUiPermissions;
import com.healthcoco.healthcocopad.bean.server.Role;
import com.healthcoco.healthcocopad.enums.RoleType;
import com.healthcoco.healthcocopad.enums.UserState;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 23-01-2017.
 */
public class UserVerification {

    protected String locationId;
    private char[] password;
    private String username;

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}
