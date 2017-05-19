package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.ClinicDetailResponse;
import com.healthcoco.healthcocopad.bean.server.Location;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 24-02-2017.
 */
public class AddEditClinicContactDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {
    public static final String TAG_CONTACT = "clinicContact";
    private Location clinicDetail;
    private EditText editClinicNumber;
    private EditText editAlternateNumber;
    private EditText editWebsite;
    private EditText editEmailAddress;
    private TextView tvClinicName;
    private ClinicDetailResponse clinicDetailResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_edit_clinic_contact, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setWidthHeight(0.50, 0.80);
    }

    @Override
    public void init() {
        clinicDetailResponse = Parcels.unwrap(getArguments().getParcelable(AddEditClinicContactDialogFragment.TAG_CONTACT));
        clinicDetail = clinicDetailResponse.getLocation();
        initViews();
        initListeners();
        initData();
    }

    @Override
    public void initViews() {
        tvClinicName = (TextView) view.findViewById(R.id.tv_clinic_name);
        editClinicNumber = (EditText) view.findViewById(R.id.edit_clinic_number);
        editAlternateNumber = (EditText) view.findViewById(R.id.edit_alternate_number);
        editWebsite = (EditText) view.findViewById(R.id.edit_website);
        editEmailAddress = (EditText) view.findViewById(R.id.edit_email_address);
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
    }

    @Override
    public void initData() {
        if (clinicDetail != null) {
            tvClinicName.setText(Util.getValidatedValue(clinicDetail.getLocationName()));
            editClinicNumber.setText(Util.getValidatedValue(clinicDetail.getClinicNumber()));
            if (!Util.isNullOrEmptyList(clinicDetail.getAlternateClinicNumbers()))
                editAlternateNumber.setText(Util.getValidatedValue(clinicDetail.getAlternateClinicNumbers().get(0)));
            editWebsite.setText(Util.getValidatedValue(clinicDetail.getWebsiteUrl()));
            editEmailAddress.setText(Util.getValidatedValue(clinicDetail.getLocationEmailAddress()));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    validateData();
                else
                    onNetworkUnavailable(null);
                break;
        }
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        clearPreviousAlerts();
        String clinicNumber = Util.getValidatedValueOrNull(editClinicNumber);
        String alternateNumebr = Util.getValidatedValueOrNull(editAlternateNumber);
        String emailAddress = Util.getValidatedValueOrNull(editEmailAddress);
        String websiteUrl = Util.getValidatedValueOrNull(editWebsite);
        if (!Util.isNullOrBlank(clinicNumber) && !Util.isValidMobileNoLandlineNumber(clinicNumber)) {
            msg = getResources().getString(R.string.please_enter_valid_clinic_no);
            errorViewList.add(editClinicNumber);
        } else if (!Util.isNullOrBlank(alternateNumebr) && !Util.isValidMobileNoLandlineNumber(alternateNumebr)) {
            msg = getResources().getString(R.string.please_enter_valid_alternate_clinic_no);
            errorViewList.add(editAlternateNumber);
        } else if (!Util.isNullOrBlank(emailAddress) && !Util.isValidEmail(emailAddress)) {
            msg = getResources().getString(R.string.please_enter_valid_email_address);
            errorViewList.add(editEmailAddress);
        } else if (!Util.isNullOrBlank(websiteUrl) && !Util.isValidWebsite(websiteUrl)) {
            msg = getResources().getString(R.string.please_enter_valid_website_url);
            errorViewList.add(editWebsite);
        }
        if (Util.isNullOrBlank(msg))
            addUpdateContact(clinicNumber, alternateNumebr, emailAddress, websiteUrl);
        else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }

    }

    private void addUpdateContact(String clinicNumber, String alternateNumber, String emailAddress, String websiteUrl) {
        mActivity.showLoading(false);
        Location location = new Location();
        location.setClinicNumber(clinicNumber);
        location.setAlternateClinicNumbers(getAlternateNumbersList(alternateNumber));
        location.setLocationEmailAddress(emailAddress);
        location.setWebsiteUrl(websiteUrl);
        if (clinicDetail != null)
            location.setUniqueId(clinicDetail.getUniqueId());
        WebDataServiceImpl.getInstance(mApp).addUpdateCommonMethod(WebServiceType.ADD_UPDATE_CLINIC_CONTACT, Location.class, location, this, this);
    }

    private ArrayList<String> getAlternateNumbersList(String alternateNumber) {
        if (!Util.isNullOrBlank(alternateNumber)) {
            ArrayList<String> listString = new ArrayList<>();
            listString.add(alternateNumber);
            return listString;
        }
        return null;
    }

    private void clearPreviousAlerts() {
        editClinicNumber.setActivated(false);
        editAlternateNumber.setActivated(false);
        editWebsite.setActivated(false);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        }
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        switch (response.getWebServiceType()) {
            case ADD_UPDATE_CLINIC_CONTACT:
                if (response.getData() instanceof Location) {
                    Location resposneObject = (Location) response.getData();
                    clinicDetail.setClinicNumber(resposneObject.getClinicNumber());
                    clinicDetail.setAlternateClinicNumbers(resposneObject.getAlternateClinicNumbers());
                    clinicDetail.setWebsiteUrl(resposneObject.getWebsiteUrl());
                    clinicDetail.setLocationEmailAddress(resposneObject.getLocationEmailAddress());
                    LocalDataServiceImpl.getInstance(mApp).addLocation(clinicDetail);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_ADD_EDIT_CLINIC_CONTACT, new Intent().putExtra(HealthCocoConstants.TAG_CLINIC_PROFILE, Parcels.wrap(clinicDetail)));
                    getDialog().dismiss();
                }
                break;
        }
        mActivity.hideLoading();
    }
}
