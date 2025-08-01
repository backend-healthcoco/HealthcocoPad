package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorWorkingSchedule;
import com.healthcoco.healthcocopad.bean.server.Location;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
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
public class AboutClinicFragment extends HealthCocoFragment implements View.OnClickListener, OnMapReadyCallback {

    public static final String TIME_FORMAT_CLINIC_HOURS = "hh:mm  aaa";
    public static final String INTENT_GET_DOCTOR_CLINIC_PROFILE = "com.healthcoco.REFRESH_DOCTOR_CLINIC_PROFILE";
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

    private TextView tvClinicNumbers;
    private TextView tvWebsite;
    private TextView tvEmailAddress;

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
        view = inflater.inflate(R.layout.fragment_about_clinic, container, false);
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
        tvClinicNumbers = (TextView) view.findViewById(R.id.tv_clinic_numbers);
        tvWebsite = (TextView) view.findViewById(R.id.tv_website);
        tvEmailAddress = (TextView) view.findViewById(R.id.tv_clinic_email_address);


        btEnlargedMap = (View) view.findViewById(R.id.bt_enlarged_map);
    }

    @Override
    public void initListeners() {
        btEnlargedMap.setOnClickListener(this);
    }

    public void initData(DoctorProfile doctorProfile) {
        if (doctorProfile != null) {
            clearDetails();
            if (!Util.isNullOrEmptyList(doctorProfile.getClinicProfile())) {
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

    private void refreshSelectedClinicProfileData(DoctorClinicProfile selectedClinicProfile) {
        this.selectedClinicProfile = selectedClinicProfile;
        tvAddress.setText(selectedClinicProfile.getFormattedClinicAddress(mActivity));
        refreshWorkingSchedule(selectedClinicProfile);
        refreshMapLocation();
    }


    private void refreshWorkingSchedule(DoctorClinicProfile selectedClinicProfile) {
        if (selectedClinicProfile.getTwentyFourSevenOpen() != null && selectedClinicProfile.getTwentyFourSevenOpen()) {
            containerClinicHours.setVisibility(View.GONE);
            containerClinicOpen247.setVisibility(View.VISIBLE);
        } else {
            containerClinicHours.setVisibility(View.VISIBLE);
            containerClinicOpen247.setVisibility(View.GONE);
            addWorkingSchedules(selectedClinicProfile.getWorkingSchedules());
        }
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
        if (view.getId() == R.id.bt_enlarged_map) {
            if (selectedClinicProfile != null && !Util.isNullOrBlank(selectedClinicProfile.getUniqueId())) {
                openMapViewActivity(
                        CommonOpenUpFragmentType.ENLARGED_MAP_VIEW_FRAGMENT,
                        selectedClinicProfile.getUniqueId(),
                        MapType.DOCTOR_PROFILE_CLINIC,
                        0
                );
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null && selectedClinicProfile != null && selectedClinicProfile.getLatitude() != null && selectedClinicProfile.getLongitude() != null) {
            googleMap.clear();
            MarkerOptions marker = new MarkerOptions().position(new LatLng(selectedClinicProfile.getLatitude(), selectedClinicProfile.getLongitude())).title(selectedClinicProfile.getLocationName());
            // adding marker
            googleMap.addMarker(marker);
            moveCamerToLatLong(googleMap, selectedClinicProfile.getLatitude(), selectedClinicProfile.getLongitude());
            googleMap.getUiSettings().setAllGesturesEnabled(false);
        }
    }


    public void refreshClinicContacDetail(Location location) {
        tvClinicNumbers.setText(getClinicNumbers(location));
        tvWebsite.setText(Util.getValidatedValueOrDash(mActivity, location.getWebsiteUrl()));

        tvEmailAddress.setText(Util.getValidatedValueOrDash(mActivity, location.getLocationEmailAddress()));
    }

    private String getClinicNumbers(Location location) {
        String numbers = "";
        if (!Util.isNullOrBlank(location.getClinicNumber()))
            numbers = location.getClinicNumber();
        if (!Util.isNullOrEmptyList(location.getAlternateClinicNumbers())) {
            for (String alternateNumber :
                    location.getAlternateClinicNumbers()) {
                if (!Util.isNullOrBlank(alternateNumber)) {
                    numbers = addCharacterToText("\n", numbers);
                    numbers = numbers + alternateNumber;
                }
            }
        }
        if (!Util.isNullOrBlank(location.getLocationPhoneNumber())) {
            numbers = addCharacterToText("\n", numbers);
            numbers = numbers + location.getLocationPhoneNumber();
        }
        if (Util.isNullOrBlank(numbers))
            return getResources().getString(R.string.no_text_dash);
        return numbers;
    }

    private String addCharacterToText(String character, String numbers) {
        if (!Util.isNullOrBlank(numbers))
            numbers = numbers + character;
        return numbers;
    }


}
