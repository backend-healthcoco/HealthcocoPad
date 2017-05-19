package com.healthcoco.healthcocopad.dialogFragment;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AddEditLocaleVisitDetailsRequest;
import com.healthcoco.healthcocopad.bean.server.ClinicDetailResponse;
import com.healthcoco.healthcocopad.bean.server.ClinicWorkingSchedule;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorWorkingSchedule;
import com.healthcoco.healthcocopad.bean.server.Location;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
import com.healthcoco.healthcocopad.bean.server.WorkingSchedule;
import com.healthcoco.healthcocopad.custom.CustomTimePickerDialog;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.enums.WeekDayNameType;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ReflectionUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Shreshtha on 22-02-2017.
 */
public class AddEditClinicHoursDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {
    public static final int DEFAULT_TIME_INTERVAL = 15;
    private static final String TIME_FORMAT = "hh:mm aaa";
    public static final String TAG_CLINIC_HOURS = "clinicHours";
    private LinearLayout containerWeekDays;
    private ToggleButton toggleButton247;
    private List<?> workingScheduleList;
    private CommonOpenUpFragmentType fragmentType;
    private LinearLayout containerToggle247;
    private Object object;
    private TextView tvClinicname;
    private String clinicName;
    private Boolean isTwentyFourSevenOpen;
    private ClinicDetailResponse clinicDetailResponse;
    private DoctorClinicProfile doctorClinicProfile;
    private String uniqueId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_edit_clinic_hours_detail, container, false);
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
        initViews();
        initListeners();
        initContainerWeekdays();
        getIntentData();
        initData();
    }

    private void getIntentData() {
        int fragmentOrdinal = getArguments().getInt(HealthCocoConstants.TAG_FRAGMENT_NAME);
        fragmentType = CommonOpenUpFragmentType.values()[fragmentOrdinal];
        clinicDetailResponse = Parcels.unwrap(getArguments().getParcelable(AddEditClinicHoursDialogFragment.TAG_CLINIC_HOURS));
        doctorClinicProfile = Parcels.unwrap(getArguments().getParcelable(HealthCocoConstants.TAG_CLINIC_PROFILE));
        if (clinicDetailResponse != null)
            uniqueId = clinicDetailResponse.getUniqueId();
        else uniqueId = doctorClinicProfile.getUniqueId();
        if (!Util.isNullOrBlank(uniqueId))
            switch (fragmentType) {
                case ADD_EDIT_CLINIC_HOURS:
                    containerToggle247.setVisibility(View.VISIBLE);
                    object = LocalDataServiceImpl.getInstance(mApp).getLocation(uniqueId);
                    System.out.println("Object " + object);
                    break;
                case ADD_EDIT_DOCTOR_PROFILE_CLINIC_HOURS:
                    containerToggle247.setVisibility(View.GONE);
                    object = Parcels.unwrap(getArguments().getParcelable(HealthCocoConstants.TAG_CLINIC_PROFILE));
                    break;
            }
    }

    @Override
    public void initData() {
        try {
            switch (fragmentType) {
                case ADD_EDIT_CLINIC_HOURS:
                    if (object instanceof Location) {
                        Location location = (Location) object;
                        clinicName = location.getLocationName();
                        workingScheduleList = location.getClinicWorkingSchedules();
                        containerToggle247.setVisibility(View.VISIBLE);
                        isTwentyFourSevenOpen = location.getTwentyFourSevenOpen();
                    }
                    break;
                case ADD_EDIT_DOCTOR_PROFILE_CLINIC_HOURS:
                    if (object instanceof DoctorClinicProfile) {
                        DoctorClinicProfile clinicProfile = (DoctorClinicProfile) object;
                        containerToggle247.setVisibility(View.GONE);
                        clinicName = clinicProfile.getLocationName();
                        workingScheduleList = clinicProfile.getWorkingSchedules();
                        containerToggle247.setVisibility(View.GONE);
                        isTwentyFourSevenOpen = false;
                    }
                    break;
            }
            tvClinicname.setText(Util.getValidatedValue(clinicName));
            if (containerToggle247.getVisibility() == View.VISIBLE) {
                if (isTwentyFourSevenOpen != null)
                    toggleButton247.setChecked(isTwentyFourSevenOpen);
            }
            if (!Util.isNullOrEmptyList(workingScheduleList)) {
                for (Object object :
                        workingScheduleList) {
                    WorkingSchedule workingSchedule = new WorkingSchedule();
                    ReflectionUtil.copy(workingSchedule, object);

                    WeekDayNameType weekdayType = workingSchedule.getWorkingDay();
                    if (!Util.isNullOrEmptyList(workingSchedule.getWorkingHours())) {
                        for (WorkingHours workingHours :
                                workingSchedule.getWorkingHours()) {
                            addSubItemFromTo(weekdayType, workingHours);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initViews() {
        tvClinicname = (TextView) view.findViewById(R.id.tv_clinic_name);
        containerWeekDays = (LinearLayout) view.findViewById(R.id.container_weekdays);
        toggleButton247 = (ToggleButton) view.findViewById(R.id.check_box_24_7);
        containerToggle247 = (LinearLayout) view.findViewById(R.id.container_toggle_24_7);
        initSaveCancelButton(this);
        initActionbarTitle(getResources().getString(R.string.clinic_hours));
    }

    @Override
    public void initListeners() {

    }

    /**
     * sets btAddSession id as weekdayTypes ordinal value
     * and containerFromTo's tag as WeekdayType
     */

    private void initContainerWeekdays() {
        for (WeekDayNameType weekDayNameType :
                WeekDayNameType.values()) {
            LinearLayout itemAddSession = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.item_weekdayname_add_session, null);
            TextView tvweekDayName = (TextView) itemAddSession.findViewById(R.id.tv_weekday_name);
            TextView btAddSession = (TextView) itemAddSession.findViewById(R.id.bt_add_session);
            LinearLayout containerFromToSession = (LinearLayout) itemAddSession.findViewById(R.id.container_from_to_session);
            containerFromToSession.setTag(weekDayNameType);

            btAddSession.setId(weekDayNameType.ordinal());
            tvweekDayName.setText(weekDayNameType.getWeekDayNameId());
            btAddSession.setOnClickListener(this);
            containerWeekDays.addView(itemAddSession);
        }
    }

    @Override
    public void onClick(View v) {
        Util.checkNetworkStatus(mActivity);
        WeekDayNameType weekdayType = null;
        try {
            weekdayType = WeekDayNameType.values()[v.getId()];
            if (isPreviousDataNotBlank(weekdayType))
                addSubItemFromTo(weekdayType, null);
            else
                Util.showToast(mActivity, R.string.please_fill_timing);
        } catch (Exception e) {
            e.printStackTrace();
            weekdayType = null;
        }
        if (weekdayType != null) {
            LogUtils.LOGD(TAG, "AddSession Clicked " + weekdayType);
        } else {
            switch (v.getId()) {
                case R.id.bt_save:
                    if (HealthCocoConstants.isNetworkOnline)
                        validateData();
                    else
                        Util.showToast(mActivity, R.string.user_offline);
                    break;
                case R.id.bt_delete:
                    showConfirmationAlert(v);
                    break;
                case R.id.tv_time_from:
                    openTimePickerDialog(null, (TextView) v);
                    break;
                case R.id.tv_time_to:
                    LinearLayout parent = (LinearLayout) v.getParent();
                    if (parent != null) {
                        TextView tvFromTime = (TextView) parent.findViewById(R.id.tv_time_from);
                        String selectedFromTime = Util.getValidatedValueOrNull(tvFromTime);
                        if (!Util.isNullOrBlank(selectedFromTime))
                            openTimePickerDialog(selectedFromTime, (TextView) v);
                        else {
                            Util.showToast(mActivity, R.string.please_select_from_time);
                        }
                    }
                    break;
            }
        }
    }

    private boolean isPreviousDataNotBlank(WeekDayNameType weekdayType) {
        LinearLayout containerSubItemFromTo = (LinearLayout) view.findViewWithTag(weekdayType);
        if (containerSubItemFromTo.getChildCount() > 0) {
            View lastChild = containerSubItemFromTo.getChildAt(containerSubItemFromTo.getChildCount() - 1);
            TextView tvTimeFrom = (TextView) lastChild.findViewById(R.id.tv_time_from);
            TextView tvTimeTo = (TextView) lastChild.findViewById(R.id.tv_time_to);

            String timeFrom = Util.getValidatedValueOrNull(tvTimeFrom);
            String timeTo = Util.getValidatedValueOrNull(tvTimeTo);

            if (Util.isNullOrBlank(timeFrom) || Util.isNullOrBlank(timeTo))
                return false;
        }
        return true;
    }

    private void validateData() {
        addUpdateClinicHours();
    }

    private void addUpdateClinicHours() {
        mActivity.showLoading(false);
        switch (fragmentType) {
            case ADD_EDIT_CLINIC_HOURS:
                if (object instanceof Location) {
                    Location location = new Location();
                    location.setClinicWorkingSchedules((ArrayList<ClinicWorkingSchedule>) (ArrayList<?>) getWorkingSchedules());
                    location.setTwentyFourSevenOpen(toggleButton247.isChecked());
                    location.setUniqueId(clinicDetailResponse.getUniqueId());
                    WebDataServiceImpl.getInstance(mApp).addUpdateCommonMethod(WebServiceType.ADD_UPDATE_CLINIC_HOURS, Location.class, location, this, this);
                }
                break;
            case ADD_EDIT_DOCTOR_PROFILE_CLINIC_HOURS:
                if (object instanceof DoctorClinicProfile) {
                    DoctorClinicProfile clinicProfile = (DoctorClinicProfile) object;
                    AddEditLocaleVisitDetailsRequest addEditLocaleVisitDetailsRequest = new AddEditLocaleVisitDetailsRequest();
                    addEditLocaleVisitDetailsRequest.setUniqueId(clinicProfile.getUniqueId());
                    addEditLocaleVisitDetailsRequest.setDoctorId(clinicProfile.getDoctorId());
                    addEditLocaleVisitDetailsRequest.setLocationId(clinicProfile.getLocationId());
                    addEditLocaleVisitDetailsRequest.setWorkingSchedules((ArrayList<DoctorWorkingSchedule>) (ArrayList<?>) getWorkingSchedules());
                    WebDataServiceImpl.getInstance(mApp).addUpdateCommonMethod(WebServiceType.ADD_UPDATE_DOCTOR_CLINIC_HOURS, AddEditLocaleVisitDetailsRequest.class, addEditLocaleVisitDetailsRequest, this, this);
                }
                break;
        }
    }

    private List<?> getWorkingSchedules() {
        ArrayList<WorkingSchedule> scheduleList = new ArrayList<>();
        for (WeekDayNameType weekdayType :
                WeekDayNameType.values()) {

            LinearLayout containerSubItemFromTo = (LinearLayout) view.findViewWithTag(weekdayType);
            if (containerSubItemFromTo != null && containerSubItemFromTo.getChildCount() > 0) {

                ArrayList<WorkingHours> hoursList = new ArrayList<>();
                for (int i = 0; i < containerSubItemFromTo.getChildCount(); i++) {
                    View subItemFromTo = containerSubItemFromTo.getChildAt(i);
                    TextView tvTimeFrom = (TextView) subItemFromTo.findViewById(R.id.tv_time_from);
                    TextView tvTimeTo = (TextView) subItemFromTo.findViewById(R.id.tv_time_to);

                    Float fromTime = DateTimeUtil.getMinutesFromFormattedTime(Util.getValidatedValueOrNull(tvTimeFrom));
                    Float toTime = DateTimeUtil.getMinutesFromFormattedTime(Util.getValidatedValueOrNull(tvTimeTo));
                    if (fromTime != null && toTime != null) {
                        WorkingHours hours = new WorkingHours();
                        hours.setFromTime(fromTime);
                        hours.setToTime(toTime);
                        hoursList.add(hours);
                    }
                }
                WorkingSchedule schedule = new WorkingSchedule();
                schedule.setWorkingDay(weekdayType);
                schedule.setWorkingHours(hoursList);
                scheduleList.add(schedule);
            }
        }
        return scheduleList;
    }

    private void addSubItemFromTo(WeekDayNameType weekdayType, WorkingHours workingHours) {
        LinearLayout containerSubItemFromTo = (LinearLayout) view.findViewWithTag(weekdayType);

        LinearLayout subItemFromTo = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.sub_item_from_to_session, null);
        ImageButton btDelete = (ImageButton) subItemFromTo.findViewById(R.id.bt_delete);
        TextView tvTimeFrom = (TextView) subItemFromTo.findViewById(R.id.tv_time_from);
        TextView tvTimeTo = (TextView) subItemFromTo.findViewById(R.id.tv_time_to);
        tvTimeFrom.setOnClickListener(this);
        tvTimeTo.setOnClickListener(this);
        btDelete.setTag(subItemFromTo);
        btDelete.setOnClickListener(this);
        if (workingHours != null && workingHours.getToTime() != null && workingHours.getFromTime() != null) {
            tvTimeFrom.setText(DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getFromTime())));
            tvTimeTo.setText(DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getToTime())));
        }
        if (containerSubItemFromTo != null)
            containerSubItemFromTo.addView(subItemFromTo);
    }

    private void openTimePickerDialog(final String selectedFromTime, final TextView tvToTime) {
        String defaultPickerTime = selectedFromTime;
        String textTime = Util.getValidatedValueOrNull(tvToTime);
        boolean isTextShown = false;
        if (!Util.isNullOrBlank(textTime)) {
            isTextShown = true;
            defaultPickerTime = textTime;
        }
        final Calendar calendar = DateTimeUtil.getCalendarInstanceFromFormattedTime(defaultPickerTime, isTextShown, DEFAULT_TIME_INTERVAL);

        final CustomTimePickerDialog datePickerDialog = new CustomTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String selectedDatePickerTime = DateTimeUtil.getFormattedTime(TIME_FORMAT, hourOfDay, minute);
                int msg = 0;
                if (selectedFromTime != null)
                    if (!DateTimeUtil.selectedTimeIsGreaterThanTime(selectedDatePickerTime, selectedFromTime)) {
                        msg = R.string.time_to_should_be_greater_than_from;
                    } else if (!DateTimeUtil.isDifferenceGreaterThan15Minutes(selectedDatePickerTime, selectedFromTime)) {
                        msg = R.string.time_difference_should_15_minutes;
                    }

                if (msg == 0) {
                    LogUtils.LOGD(TAG, "Time lesser");
                    tvToTime.setText(selectedDatePickerTime);
                } else {
                    openTimePickerDialog(selectedFromTime, tvToTime);
                    Util.showToast(mActivity, msg);
                    LogUtils.LOGD(TAG, "Time greater");
                }
            }

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        datePickerDialog.show();
    }

    private void showConfirmationAlert(final View v) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm_delete_this_session);
        alertBuilder.setMessage(getResources().getString(
                R.string.this_cannot_be_undone));
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                LinearLayout itemContainer = (LinearLayout) v.getParent().getParent();
                View parent = (View) v.getParent();
                if (parent != null && itemContainer != null) {
                    itemContainer.removeView(parent);
                }
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.create();
        alertBuilder.show();
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
        mActivity.hideLoading();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case ADD_UPDATE_CLINIC_HOURS:
                case ADD_UPDATE_DOCTOR_CLINIC_HOURS:
                    if (response.getData() != null) {
                        switch (fragmentType) {
                            case ADD_EDIT_CLINIC_HOURS:
                                if (response.getData() instanceof Location && object instanceof Location) {
                                    Location locationUpdated = (Location) response.getData();
                                    Location location = (Location) object;
                                    location.setTwentyFourSevenOpen(locationUpdated.getTwentyFourSevenOpen());
                                    location.setClinicWorkingSchedules(locationUpdated.getClinicWorkingSchedules());
                                    LocalDataServiceImpl.getInstance(mApp).addLocation(location);
                                }
                                break;
                            case ADD_EDIT_DOCTOR_PROFILE_CLINIC_HOURS:
                                if (response.getData() instanceof AddEditLocaleVisitDetailsRequest && object instanceof DoctorClinicProfile) {
                                    AddEditLocaleVisitDetailsRequest addEditLocaleVisitDetailsRequest = (AddEditLocaleVisitDetailsRequest) response.getData();
                                    DoctorClinicProfile doctorClinicProfile = (DoctorClinicProfile) object;
                                    doctorClinicProfile.setTwentyFourSevenOpen(addEditLocaleVisitDetailsRequest.getTwentyFourSevenOpen());
                                    doctorClinicProfile.setWorkingSchedules(addEditLocaleVisitDetailsRequest.getWorkingSchedules());
                                    LocalDataServiceImpl.getInstance(mApp).addDoctorClinicProfile(doctorClinicProfile.getForeignUniqueId(), doctorClinicProfile);
                                }
                                break;
                        }
                        getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_ADD_EDIT_CLINIC_HOURS, new Intent().putExtra(HealthCocoConstants.TAG_CLINIC_PROFILE, Parcels.wrap(object)));
                        getDialog().dismiss();
                    }
            }
        }
        mActivity.hideLoading();
    }
}