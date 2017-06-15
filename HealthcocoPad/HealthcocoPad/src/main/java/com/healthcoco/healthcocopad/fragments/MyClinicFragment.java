package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.WorkingHours;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorWorkingSchedule;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.dialogFragment.AddEditAppointmentDetailDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.AddEditClinicHoursDialogFragment;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.MapType;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 02-02-2017.
 */
public class MyClinicFragment extends HealthCocoFragment implements View.OnClickListener, OnMapReadyCallback {

    public static final String TIME_FORMAT_CLINIC_HOURS = "hh:mm  aaa";
    public static final int REQUEST_CODE_MY_CLINIC = 114;
    public static final String INTENT_GET_DOCTOR_CLINIC_PROFILE = "com.healthcoco.REFRESH_DOCTOR_CLINIC_PROFILE";
    private AutoCompleteTextView autotvClinicName;
    private TextView tvAddress;
    private LinearLayout containerFromToTimeMon;
    private TextView tvnofromToTimeMon;
    private LinearLayout containerFromToTimeTue;
    private TextView tvnofromToTimeTue;
    private LinearLayout containerFromToTimeWed;
    private TextView tvnofromToTimeWed;
    private LinearLayout containerFromToTimeThu;
    private TextView tvnofromToTimeThu;
    private LinearLayout containerFromToTimeFri;
    private TextView tvnofromToTimeFri;
    private LinearLayout containerFromToTimeSat;
    private TextView tvnofromToTimeSat;
    private LinearLayout containerFromToTimeSun;
    private TextView tvnofromToTimeSun;
    private TextView tvConsultantFees;
    private TextView tvSecondConsultantFees;
    private TextView tvAppointmentNumbers;
    private TextView tvAppointmentSlot;
    private TextViewFontAwesome btEditVisitingHours;
    private TextViewFontAwesome btEditAppointmentDetails;
    private User user;
    private DoctorClinicProfile selectedClinicProfile;
    private TextView tvBookingFacility;
    private LinearLayout containerClinicOpen247;
    private LinearLayout containerClinicHours;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private View btEnlargedMap;
    private boolean receiversRegistered;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_clinic, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            initViews();
            initListeners();
            initMapFragment();
        }
    }

    private void initMapFragment() {
        mapFragment = new SupportMapFragment();
        mFragmentManager.beginTransaction().add(R.id.container_map_fragment, mapFragment, mapFragment.getClass().getSimpleName()).commit();
    }

    @Override
    public void initViews() {
        autotvClinicName = (AutoCompleteTextView) view.findViewById(R.id.autotv_clinic_name);
        tvAddress = (TextView) view.findViewById(R.id.tv_address);
        containerClinicOpen247 = (LinearLayout) view.findViewById(R.id.container_clinc_open_24_7);
        containerClinicHours = (LinearLayout) view.findViewById(R.id.container_clinic_hours);
        //clinic Hours view
        containerFromToTimeMon = (LinearLayout) view.findViewById(R.id.container_from_to_time_mon);
        tvnofromToTimeMon = (TextView) view.findViewById(R.id.tv_no_from_to_time_found_mon);
        containerFromToTimeTue = (LinearLayout) view.findViewById(R.id.container_from_to_time_tue);
        tvnofromToTimeTue = (TextView) view.findViewById(R.id.tv_no_from_to_time_found_tue);
        containerFromToTimeWed = (LinearLayout) view.findViewById(R.id.container_from_to_time_wed);
        tvnofromToTimeWed = (TextView) view.findViewById(R.id.tv_no_from_to_time_found_wed);
        containerFromToTimeThu = (LinearLayout) view.findViewById(R.id.container_from_to_time_thu);
        tvnofromToTimeThu = (TextView) view.findViewById(R.id.tv_no_from_to_time_found_thu);
        containerFromToTimeFri = (LinearLayout) view.findViewById(R.id.container_from_to_time_fri);
        tvnofromToTimeFri = (TextView) view.findViewById(R.id.tv_no_from_to_time_found_fri);
        containerFromToTimeSat = (LinearLayout) view.findViewById(R.id.container_from_to_time_sat);
        tvnofromToTimeSat = (TextView) view.findViewById(R.id.tv_no_from_to_time_found_sat);
        containerFromToTimeSun = (LinearLayout) view.findViewById(R.id.container_from_to_time_sun);
        tvnofromToTimeSun = (TextView) view.findViewById(R.id.tv_no_from_to_time_found_sun);

        tvConsultantFees = (TextView) view.findViewById(R.id.tv_consultant_fees);
        tvSecondConsultantFees = (TextView) view.findViewById(R.id.tv_second_consultant_fees);
        tvBookingFacility = (TextView) view.findViewById(R.id.tv_appointment_booking_facility);
        tvAppointmentNumbers = (TextView) view.findViewById(R.id.tv_appointment_number);
        tvAppointmentSlot = (TextView) view.findViewById(R.id.tv_appointment_slot);

        btEditVisitingHours = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_visiting_hours);
        btEditAppointmentDetails = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_appointment_details);
        btEnlargedMap = (View) view.findViewById(R.id.bt_enlarged_map);
    }

    @Override
    public void initListeners() {
        btEditVisitingHours.setOnClickListener(this);
        btEditAppointmentDetails.setOnClickListener(this);
        btEnlargedMap.setOnClickListener(this);
    }

    public void initData(DoctorProfile doctorProfile) {
        if (doctorProfile != null) {
            clearDetails();
            if (!Util.isNullOrEmptyList(doctorProfile.getClinicProfile())) {
                initAutoTvAdapter(autotvClinicName, AutoCompleteTextViewType.DOCTOR_CLINIC, (ArrayList<Object>) (ArrayList<?>) doctorProfile.getClinicProfile());
                String selectedLocationId = user.getForeignLocationId();
                if (selectedClinicProfile != null && !Util.isNullOrBlank(selectedClinicProfile.getLocationId()))
                    selectedLocationId = selectedClinicProfile.getLocationId();
                DoctorClinicProfile doctorClinicProfile = getDoctorClinicProfileFromList(selectedLocationId, doctorProfile.getClinicProfile());
                refreshSelectedClinicProfileData(doctorClinicProfile);
            }
        }
    }

    private DoctorClinicProfile getDoctorClinicProfileFromList(String locationId, List<DoctorClinicProfile> list) {
        for (DoctorClinicProfile doctorClinicProfile :
                list) {
            if (doctorClinicProfile.getLocationId().equalsIgnoreCase(locationId))
                return doctorClinicProfile;
        }
        return list.get(0);
    }

    private void clearDetails() {
        clearFromToTime(containerFromToTimeMon, tvnofromToTimeMon);
        clearFromToTime(containerFromToTimeTue, tvnofromToTimeTue);
        clearFromToTime(containerFromToTimeWed, tvnofromToTimeWed);
        clearFromToTime(containerFromToTimeThu, tvnofromToTimeThu);
        clearFromToTime(containerFromToTimeFri, tvnofromToTimeFri);
        clearFromToTime(containerFromToTimeSat, tvnofromToTimeSat);
        clearFromToTime(containerFromToTimeSun, tvnofromToTimeSun);
    }

    private void clearFromToTime(LinearLayout container, TextView noFromToTime) {
        container.removeAllViews();
        container.setVisibility(View.GONE);
        noFromToTime.setVisibility(View.VISIBLE);
    }

    private void initAutoTvAdapter(AutoCompleteTextView autoCompleteTextView, final AutoCompleteTextViewType autoCompleteTextViewType, List<Object> list) {
        try {
            if (!Util.isNullOrEmptyList(list)) {
                final AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(mActivity, R.layout.drop_down_item_doctor_profile_my_clinic,
                        list, autoCompleteTextViewType);
                autoCompleteTextView.setThreshold(1);
                autoCompleteTextView.setAdapter(adapter);
                autoCompleteTextView.setDropDownAnchor(autoCompleteTextView.getId());
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (autoCompleteTextViewType) {
                            case DOCTOR_CLINIC:
                                Object object = adapter.getSelectedObject(position);
                                if (object != null && object instanceof DoctorClinicProfile)
                                    refreshSelectedClinicProfileData((DoctorClinicProfile) object);
                                break;
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshSelectedClinicProfileData(DoctorClinicProfile selectedClinicProfile) {
        this.selectedClinicProfile = selectedClinicProfile;
        autotvClinicName.setText(Util.getValidatedValue(selectedClinicProfile.getLocationName()));
        tvAddress.setText(selectedClinicProfile.getFormattedClinicAddress(mActivity));
        if (selectedClinicProfile.getTwentyFourSevenOpen() != null && selectedClinicProfile.getTwentyFourSevenOpen()) {
            containerClinicHours.setVisibility(View.GONE);
            containerClinicOpen247.setVisibility(View.VISIBLE);
        } else {
            containerClinicHours.setVisibility(View.VISIBLE);
            containerClinicOpen247.setVisibility(View.GONE);
            addWorkingSchedules(selectedClinicProfile.getWorkingSchedules());
        }
        if (selectedClinicProfile.getConsultationFee() != null)
            tvConsultantFees.setText(Util.getValidatedValueOrDash(mActivity, Util.getFormattedConsultantFee(selectedClinicProfile.getConsultationFee())));
        else
            tvConsultantFees.setText(R.string.no_text_dash);

        if (selectedClinicProfile.getRevisitConsultationFee() != null)
            tvSecondConsultantFees.setText(Util.getValidatedValueOrDash(mActivity, Util.getFormattedConsultantFee(selectedClinicProfile.getRevisitConsultationFee())));
        else
            tvSecondConsultantFees.setText(R.string.no_text_dash);

        if (selectedClinicProfile.getFacility() != null)
            tvBookingFacility.setText(Util.getValidatedValueOrDash(mActivity, selectedClinicProfile.getFacility().getType()));
        if (!Util.isNullOrEmptyList(selectedClinicProfile.getAppointmentBookingNumber()))
            tvAppointmentNumbers.setText(Util.getValidatedValueOrDash(mActivity, selectedClinicProfile.getAppointmentBookingNumber().get(0)));
        else
            tvAppointmentNumbers.setText(R.string.no_text_dash);
        tvAppointmentSlot.setText(Util.getValidatedValueOrDash(mActivity, Util.getFormattedAppointmentSlot(selectedClinicProfile.getAppointmentSlot())));
        refreshMapLocation();
        view.requestFocus();
    }

    public void refreshMapLocation() {
        if (googleMap == null && mapFragment != null)
            mapFragment.getMapAsync(this);
    }

    private void addWorkingSchedules(List<DoctorWorkingSchedule> list) {
        clearDetails();
        if (!Util.isNullOrEmptyList(list)) {
            for (DoctorWorkingSchedule schedule :
                    list) {
                switch (schedule.getWorkingDay()) {
                    case MONDAY:
                        addWorkingHoursFromToTime(containerFromToTimeMon, tvnofromToTimeMon, schedule.getWorkingHours());
                        break;
                    case TUESDAY:
                        addWorkingHoursFromToTime(containerFromToTimeTue, tvnofromToTimeTue, schedule.getWorkingHours());
                        break;
                    case WEDNESDAY:
                        addWorkingHoursFromToTime(containerFromToTimeWed, tvnofromToTimeWed, schedule.getWorkingHours());
                        break;
                    case THURSDAY:
                        addWorkingHoursFromToTime(containerFromToTimeThu, tvnofromToTimeThu, schedule.getWorkingHours());
                        break;
                    case FRIDAY:
                        addWorkingHoursFromToTime(containerFromToTimeFri, tvnofromToTimeFri, schedule.getWorkingHours());
                        break;
                    case SATURDAY:
                        addWorkingHoursFromToTime(containerFromToTimeSat, tvnofromToTimeSat, schedule.getWorkingHours());
                        break;
                    case SUNDAY:
                        addWorkingHoursFromToTime(containerFromToTimeSun, tvnofromToTimeSun, schedule.getWorkingHours());
                        break;
                }
            }
        }
    }

    private void addWorkingHoursFromToTime(LinearLayout container, TextView noFromToTime, List<WorkingHours> list) {
        container.removeAllViews();
        if (!Util.isNullOrEmptyList(list)) {
            container.setVisibility(View.VISIBLE);
            noFromToTime.setVisibility(View.GONE);
            for (WorkingHours hours :
                    list) {
                if (hours.getFromTime() != null && hours.getFromTime() != null) {
                    LinearLayout subItemFromToTime = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.sub_item_from_to_clinic_profile, null);
                    TextView tvTimefrom = (TextView) subItemFromToTime.findViewById(R.id.tv_time_from);
                    TextView tvTimeTo = (TextView) subItemFromToTime.findViewById(R.id.tv_time_to);
                    tvTimefrom.setText(DateTimeUtil.getFormattedTime(TIME_FORMAT_CLINIC_HOURS, 0, Math.round(hours.getFromTime())));
                    tvTimeTo.setText(DateTimeUtil.getFormattedTime(TIME_FORMAT_CLINIC_HOURS, 0, Math.round(hours.getToTime())));
                    container.addView(subItemFromToTime);
                }
            }
        } else {
            container.setVisibility(View.GONE);
            noFromToTime.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_edit_visiting_hours:
                openDialogFragment(new AddEditClinicHoursDialogFragment(), HealthCocoConstants.TAG_CLINIC_PROFILE, selectedClinicProfile, REQUEST_CODE_MY_CLINIC, CommonOpenUpFragmentType.ADD_EDIT_DOCTOR_PROFILE_CLINIC_HOURS);
                break;
            case R.id.bt_edit_appointment_details:
                openDialogFragment(new AddEditAppointmentDetailDialogFragment(), HealthCocoConstants.TAG_CLINIC_PROFILE, selectedClinicProfile, REQUEST_CODE_MY_CLINIC, CommonOpenUpFragmentType.ADD_EDIT_DOCTOR_APPOINTMENT_DETAIL);
                break;
            case R.id.bt_enlarged_map:
                if (selectedClinicProfile != null && !Util.isNullOrBlank(selectedClinicProfile.getUniqueId()))
                    openMapViewActivity(CommonOpenUpFragmentType.ENLARGED_MAP_VIEW_FRAGMENT, selectedClinicProfile.getUniqueId(), MapType.DOCTOR_PROFILE_CLINIC, 0);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MY_CLINIC
                && data != null && data.hasExtra(HealthCocoConstants.TAG_CLINIC_PROFILE)
                && selectedClinicProfile != null) {
            DoctorClinicProfile profile = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_CLINIC_PROFILE));
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_EDIT_CLINIC_HOURS) {
                selectedClinicProfile = profile;
                selectedClinicProfile.setWorkingSchedules(profile.getWorkingSchedules());
                addWorkingSchedules(selectedClinicProfile.getWorkingSchedules());
            } else if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_EDIT_APPOINTMENT_DETAILS) {
                selectedClinicProfile = profile;
                selectedClinicProfile.setConsultationFee(profile.getConsultationFee());
                selectedClinicProfile.setRevisitConsultationFee(profile.getRevisitConsultationFee());
                selectedClinicProfile.setAppointmentSlot(profile.getAppointmentSlot());
                selectedClinicProfile.setAppointmentBookingNumber(profile.getAppointmentBookingNumber());
                selectedClinicProfile.setLocationId(profile.getLocationId());
                selectedClinicProfile.setDoctorId(profile.getDoctorId());
                refreshSelectedClinicProfileData(selectedClinicProfile);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null && selectedClinicProfile != null && selectedClinicProfile.getLatitude() != null && selectedClinicProfile.getLongitude() != null) {
            MarkerOptions marker = new MarkerOptions().position(new LatLng(selectedClinicProfile.getLatitude(), selectedClinicProfile.getLongitude())).title(selectedClinicProfile.getLocationName());
            // adding marker
            googleMap.addMarker(marker);
            moveCamerToLatLong(googleMap, selectedClinicProfile.getLatitude(), selectedClinicProfile.getLongitude());
            googleMap.getUiSettings().setAllGesturesEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_GET_DOCTOR_CLINIC_PROFILE);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshDoctorClinicProfileReceiver, filter);
            receiversRegistered = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(refreshDoctorClinicProfileReceiver);
    }

    BroadcastReceiver refreshDoctorClinicProfileReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
            if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                user = doctor.getUser();
                DoctorClinicProfile doctorClinicProfile = LocalDataServiceImpl.getInstance(mApp).
                        getDoctorClinicProfile(user.getUniqueId(), user.getForeignLocationId());
                if (doctorClinicProfile != null)
                    refreshSelectedClinicProfileData(doctorClinicProfile);
            }
        }
    };
}
