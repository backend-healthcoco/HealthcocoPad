package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.CityResponse;
import com.healthcoco.healthcocopad.bean.server.ClinicDetailResponse;
import com.healthcoco.healthcocopad.bean.server.Location;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.MyClinicFragment;
import com.healthcoco.healthcocopad.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Shreshtha on 24-02-2017.
 */
public class AddEditClinicAddressDialogFragment extends HealthCocoDialogFragment implements CommonListDialogItemClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised, View.OnClickListener {
    public static final String TAG_CLINIC_ADDRESS = "ClinicAddress";
    private EditText editClinicName;
    private EditText editAddress;
    private EditText editLocality;
    private EditText editState;
    private CityResponse selectedCity;
    private Location clinicDetail;
    private EditText editLandmark;
    private EditText editPincode;
    private AutoCompleteTextView autotvCountry;
    private AutoCompleteTextView autotvCity;
    private ClinicDetailResponse clinicDetailResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_edit_clinic_address, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setWidthHeight(0.50, 0.85);
    }

    @Override
    public void init() {
        clinicDetailResponse = Parcels.unwrap(getArguments().getParcelable(AddEditClinicAddressDialogFragment.TAG_CLINIC_ADDRESS));
        clinicDetail = clinicDetailResponse.getLocation();
        initViews();
        initListeners();
        initAutoTvAdapter(autotvCountry, AutoCompleteTextViewType.COUNTRY, (ArrayList<Object>) (ArrayList<?>) new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.array_countries))));
        initDefaultData();
    }

    @Override
    public void initViews() {
        editClinicName = (EditText) view.findViewById(R.id.edit_clinic_name);
        editAddress = (EditText) view.findViewById(R.id.edit_address);
        editLandmark = (EditText) view.findViewById(R.id.edit_landmark);
        autotvCity = (AutoCompleteTextView) view.findViewById(R.id.autotv_city);
        editLocality = (EditText) view.findViewById(R.id.edit_locality);
        autotvCountry = (AutoCompleteTextView) view.findViewById(R.id.autotv_country);
        editState = (EditText) view.findViewById(R.id.edit_state);
        editPincode = (EditText) view.findViewById(R.id.edit_pincode);
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
    }

    @Override
    public void initData() {

    }

    private void initDefaultData() {
        if (clinicDetail != null) {
            editClinicName.setText(Util.getValidatedValue(clinicDetail.getLocationName()));
            editAddress.setText(Util.getValidatedValue(clinicDetail.getStreetAddress()));
            editLandmark.setText(Util.getValidatedValue(clinicDetail.getLandmarkDetails()));
            autotvCity.setText(Util.getValidatedValue(clinicDetail.getCity()));
            editLocality.setText(Util.getValidatedValue(clinicDetail.getLocality()));
            editState.setText(Util.getValidatedValue(clinicDetail.getState()));
            editPincode.setText(Util.getValidatedValue(clinicDetail.getPostalCode()));
            autotvCountry.setText(Util.getValidatedValue(clinicDetail.getCountry()));
        }
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_CITIES, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void initAutoTvAdapter(AutoCompleteTextView autoCompleteTextView, final AutoCompleteTextViewType autoCompleteTextViewType, ArrayList<Object> list) {
        try {
            if (!Util.isNullOrEmptyList(list)) {
                final AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(mActivity, R.layout.spinner_drop_down_item_grey_background,
                        list, autoCompleteTextViewType);
                autoCompleteTextView.setThreshold(1);
                autoCompleteTextView.setAdapter(adapter);
                autoCompleteTextView.setDropDownAnchor(autoCompleteTextView.getId());
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (autoCompleteTextViewType) {
                            case CITY:
                                onDialogItemClicked(CommonListDialogType.CITY, adapter.getSelectedObject(position));
                                break;
                        }

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogItemClicked(CommonListDialogType commonListDialogType, Object object) {
        switch (commonListDialogType) {
            case CITY:
                if (object instanceof CityResponse) {
                    selectedCity = (CityResponse) object;
                    autotvCity.setText(selectedCity.getCity());
                }
                break;
        }
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
            case GET_CITIES:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    initAutoTvAdapter(autotvCity, AutoCompleteTextViewType.CITY, response.getDataList());
                break;
            case ADD_UPDATE_CLINIC_ADDRESS:
                if (response.getData() instanceof Location) {
                    Location resposneObject = (Location) response.getData();
                    clinicDetail.setLocationName(resposneObject.getLocationName());
                    clinicDetail.setStreetAddress(resposneObject.getStreetAddress());
                    clinicDetail.setLandmarkDetails(resposneObject.getLandmarkDetails());
                    clinicDetail.setLocality(resposneObject.getLocality());
                    clinicDetail.setLocality(resposneObject.getCity());
                    clinicDetail.setCountry(resposneObject.getCountry());
                    clinicDetail.setPostalCode(resposneObject.getPostalCode());
                    LocalDataServiceImpl.getInstance(mApp).addLocation(clinicDetail);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_ADD_EDIT_CLINIC_ADDRESS, new Intent().putExtra(MyClinicFragment.TAG_CLINIC_PROFILE, Parcels.wrap(clinicDetail)));
                    getDialog().dismiss();
                }
                break;
        }
        mActivity.hideLoading();
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_CITIES:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getCitiesList(WebServiceType.GET_CITIES, null, null);
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

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
        clearPreviousAlerts();
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        String clinicName = String.valueOf(editClinicName.getText()).trim();
        if (Util.isNullOrBlank(clinicName)) {
            msg = getResources().getString(R.string.please_enter_clinic_name);
            errorViewList.add(editClinicName);
        }
        if (Util.isNullOrBlank(msg))
            updatedAddress(clinicName);
        else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void updatedAddress(String clinicName) {
        mActivity.showLoading(false);
        Location location = new Location();
        if (clinicDetail != null) {
            location.setUniqueId(clinicDetail.getUniqueId());
            location.setClinicNumber(clinicDetail.getClinicNumber());
            location.setAlternateClinicNumbers(clinicDetail.getAlternateClinicNumbers());
        }
        location.setLocationName(clinicName);
        location.setStreetAddress(Util.getValidatedValueOrNull(editAddress));
        if (selectedCity != null && Util.getValidatedValue(selectedCity.getCity()) == Util.getValidatedValueOrNull(autotvCity)) {
            location.setCity(selectedCity.getCity());
            location.setLatitude(selectedCity.getLatitude());
            location.setLongitude(selectedCity.getLongitude());
        } else
            location.setCity(Util.getValidatedValueOrNull(autotvCity));
        location.setLocality(Util.getValidatedValueOrNull(editLocality));
        location.setState(Util.getValidatedValueOrNull(editState));
        location.setLandmarkDetails(Util.getValidatedValueOrNull(editLandmark));
        location.setCountry(Util.getValidatedValueOrNull(autotvCountry));
        location.setPostalCode(Util.getValidatedValueOrNull(editPincode));
        WebDataServiceImpl.getInstance(mApp).addUpdateCommonMethod(WebServiceType.ADD_UPDATE_CLINIC_ADDRESS, Location.class, location, this, this);
    }

    private void clearPreviousAlerts() {
        editClinicName.setActivated(false);
    }
}
