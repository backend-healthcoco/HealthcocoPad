package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.AppointmentSlot;
import com.healthcoco.healthcocopad.bean.server.ConsultationFee;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.enums.AppointmentSlotsType;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.CurrencyType;
import com.healthcoco.healthcocopad.enums.DoctorFacility;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 22-02-2017.
 */
public class AddEditAppointmentDetailDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener,
        GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, AdapterView.OnItemClickListener {
    private EditText editAppointmentNumber;
    private EditText editConsultantFee;
    private EditText editSecondConsultantFee;
    private DoctorClinicProfile clinicProfile;
    private AppointmentSlot seletecdAppointmentSlot;
    private AutoCompleteTextView autoTvAppointmentSlot;
    private AutoCompleteTextViewAdapter adapter;
    private RadioGroup rgFacilityType;
    private RadioButton rbBOOK;
    private RadioButton rbIBS;
    private RadioButton rbCALL;
    private TextView tvClinicName;
    private String uniqueLocationId;
    private CommonOpenUpFragmentType fragmentType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_edit_appointment_details, container, false);
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
        getIntentData();
        initViews();
        initListeners();
        initDefaultData();
        initAutoTvAdapter();
        initData();
    }

    private void getIntentData() {
        Intent intent = mActivity.getIntent();
        uniqueLocationId = intent.getStringExtra(HealthCocoConstants.TAG_UNIQUE_ID);
        int fragmentOrdinal = getArguments().getInt(HealthCocoConstants.TAG_FRAGMENT_NAME);
        fragmentType = CommonOpenUpFragmentType.values()[fragmentOrdinal];
        clinicProfile = Parcels.unwrap(getArguments().getParcelable(HealthCocoConstants.TAG_CLINIC_PROFILE));
    }

    @Override
    public void initViews() {
        tvClinicName = (TextView) view.findViewById(R.id.tv_clinic_name);
        editAppointmentNumber = (EditText) view.findViewById(R.id.edit_appointment_number);
        editConsultantFee = (EditText) view.findViewById(R.id.edit_consultant_fee);
        editSecondConsultantFee = (EditText) view.findViewById(R.id.edit_second_consultant_fee);
        autoTvAppointmentSlot = (AutoCompleteTextView) view.findViewById(R.id.autotv_appointment_slot);
        rgFacilityType = (RadioGroup) view.findViewById(R.id.rg_facility_type);

        //initalising Facility views
        rbIBS = (RadioButton) view.findViewById(R.id.rb_ibs);
        rbBOOK = (RadioButton) view.findViewById(R.id.rb_book);
        rbCALL = (RadioButton) view.findViewById(R.id.rb_call);
        rbIBS.setTag(DoctorFacility.IBS);
        rbBOOK.setTag(DoctorFacility.BOOK);
        rbCALL.setTag(DoctorFacility.CALL);
        initSaveCancelButton(this);
        initActionbarTitle(getResources().getString(R.string.appointment_details));
    }

    @Override
    public void initListeners() {
        autoTvAppointmentSlot.setOnItemClickListener(this);
    }

    private void initDefaultData() {
    }

    private void initAutoTvAdapter() {
        try {
            ArrayList<Object> list = (ArrayList<Object>) (ArrayList<?>) getAppoinmentSlotsList();
            if (!Util.isNullOrEmptyList(list)) {
                adapter = new AutoCompleteTextViewAdapter(mActivity, R.layout.spinner_drop_down_item_grey_background,
                        list, AutoCompleteTextViewType.APPOINTMENT_SLOT);
                autoTvAppointmentSlot.setThreshold(0);
                autoTvAppointmentSlot.setAdapter(adapter);
                autoTvAppointmentSlot.setDropDownAnchor(R.id.autotv_appointment_slot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<AppointmentSlot> getAppoinmentSlotsList() {
        ArrayList<AppointmentSlot> list = new ArrayList<>();
        for (AppointmentSlotsType slotType :
                AppointmentSlotsType.values()) {
            AppointmentSlot appointmentSlot = new AppointmentSlot();
            appointmentSlot.setTime(slotType.getTime());
            appointmentSlot.setTimeUnit(slotType.getUnits());
            list.add(appointmentSlot);
        }
        return list;
    }

    @Override
    public void initData() {
        if (clinicProfile != null) {
            tvClinicName.setText(Util.getValidatedValue(clinicProfile.getLocationName()));
            seletecdAppointmentSlot = clinicProfile.getAppointmentSlot();

            String appointmentSlot = Util.getFormattedAppointmentSlot(clinicProfile.getAppointmentSlot());
            if (Util.isNullOrBlank(appointmentSlot))
                appointmentSlot = mActivity.getResources().getString(R.string.select_slot);
            autoTvAppointmentSlot.setText(appointmentSlot);
            ConsultationFee consultantFee = clinicProfile.getConsultationFee();
            if (consultantFee != null) {
                editConsultantFee.setText(Util.getValidatedValue(consultantFee.getAmount()));
            }
            ConsultationFee revisitConsultationFee = clinicProfile.getRevisitConsultationFee();
            if (revisitConsultationFee != null) {
                editSecondConsultantFee.setText(Util.getValidatedValue(revisitConsultationFee.getAmount()));
            }
            if (!Util.isNullOrEmptyList(clinicProfile.getAppointmentBookingNumber()))
                editAppointmentNumber.setText(clinicProfile.getAppointmentBookingNumber().get(0));

            if (clinicProfile.getFacility() != null) {
                View view = rgFacilityType.findViewWithTag(clinicProfile.getFacility());
                if (view != null && view instanceof RadioButton)
                    ((RadioButton) view).setChecked(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Util.checkNetworkStatus(mActivity);
        switch (v.getId()) {
            case R.id.bt_save:
                if (HealthCocoConstants.isNetworkOnline)
                    validateData();
                else
                    Util.showToast(mActivity, R.string.user_offline);
                break;
        }
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        String appointmentNumber = Util.getValidatedValueOrNull(editAppointmentNumber);
        if (Util.isNullOrBlank(appointmentNumber)) {
            errorViewList.add(editAppointmentNumber);
            msg = mActivity.getResources().getString(R.string.please_enter_appointment_booking_number);
        } else if (!Util.isValidMobileNoLandlineNumber(appointmentNumber)) {
            errorViewList.add(editAppointmentNumber);
            msg = mActivity.getResources().getString(R.string.please_enter_a_valid_appointment_booking_number);
        }
        if (Util.isNullOrBlank(msg)) {
            addAppointmentDetails();
        } else
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
    }

    private void addAppointmentDetails() {
        mActivity.showLoading(false);
        DoctorClinicProfile doctorClinicProfile = new DoctorClinicProfile();
        doctorClinicProfile.setConsultationFee(getConsultantFee());
        doctorClinicProfile.setRevisitConsultationFee(getRevisitConsultationFee());
        doctorClinicProfile.setAppointmentSlot(getAppointmentSlot());
        doctorClinicProfile.setAppointmentBookingNumber(getAppointmentBookingNumbers());
        doctorClinicProfile.setLocationId(clinicProfile.getLocationId());
        doctorClinicProfile.setDoctorId(clinicProfile.getDoctorId());

        //setting facility
        View checkedRadioButton = view.findViewById(rgFacilityType.getCheckedRadioButtonId());
        if (checkedRadioButton != null) {
            Object tag = checkedRadioButton.getTag();
            if (tag != null && tag instanceof DoctorFacility) {
                doctorClinicProfile.setFacility((DoctorFacility) tag);
            }
        }
        WebDataServiceImpl.getInstance(mApp).addUpdateGeneralInfo(DoctorClinicProfile.class, doctorClinicProfile, this, this);
    }

    private ConsultationFee getRevisitConsultationFee() {
        Integer revisitConsulatntFee = Util.getValidatedIntegerValue(editSecondConsultantFee);
        if (revisitConsulatntFee != null) {
            ConsultationFee consultationFee = new ConsultationFee();
            consultationFee.setCurrency(CurrencyType.INR);
            consultationFee.setAmount(revisitConsulatntFee);
            return consultationFee;
        }
        return null;
    }

    private ConsultationFee getConsultantFee() {
        Integer consulatntFee = Util.getValidatedIntegerValue(editConsultantFee);
        if (consulatntFee != null) {
            ConsultationFee consultationFee = new ConsultationFee();
            consultationFee.setCurrency(CurrencyType.INR);
            consultationFee.setAmount(consulatntFee);
            return consultationFee;
        }
        return null;
    }

    private AppointmentSlot getAppointmentSlot() {
        if (seletecdAppointmentSlot != null) {
            AppointmentSlot appointmentSlot = new AppointmentSlot();
            appointmentSlot.setTime(seletecdAppointmentSlot.getTime());
            appointmentSlot.setTimeUnit(seletecdAppointmentSlot.getTimeUnit());
            return appointmentSlot;
        }
        return null;
    }

    private List<String> getAppointmentBookingNumbers() {
        ArrayList<String> list = new ArrayList<>();
        list.add(Util.getValidatedValueOrNull(editAppointmentNumber));
        return list;
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
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case ADD_UPDATE_GENERAL_INFO:
                    if (response.getData() != null && response.getData() instanceof DoctorClinicProfile) {
                        DoctorClinicProfile doctorClinicProfileResponse = (DoctorClinicProfile) response.getData();
                        clinicProfile.setAppointmentSlot(doctorClinicProfileResponse.getAppointmentSlot());
                        clinicProfile.setFacility(doctorClinicProfileResponse.getFacility());
                        clinicProfile.setAppointmentBookingNumber(doctorClinicProfileResponse.getAppointmentBookingNumber());
                        clinicProfile.setConsultationFee(doctorClinicProfileResponse.getConsultationFee());
                        clinicProfile.setRevisitConsultationFee(doctorClinicProfileResponse.getRevisitConsultationFee());
                        LocalDataServiceImpl.getInstance(mApp).addDoctorClinicProfile(doctorClinicProfileResponse.getDoctorId(), clinicProfile);
                    }
                    getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_ADD_EDIT_APPOINTMENT_DETAILS, new Intent().putExtra(HealthCocoConstants.TAG_CLINIC_PROFILE, Parcels.wrap(clinicProfile)));
                    getDialog().dismiss();
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Object object = adapter.getItem(position);
            if (object instanceof AppointmentSlot) {
                seletecdAppointmentSlot = (AppointmentSlot) object;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
