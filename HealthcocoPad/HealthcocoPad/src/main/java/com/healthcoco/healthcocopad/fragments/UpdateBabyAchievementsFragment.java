package com.healthcoco.healthcocopad.fragments;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.BabyAchievementsRequest;
import com.healthcoco.healthcocopad.bean.server.AchievementsDuration;
import com.healthcoco.healthcocopad.bean.server.BabyAchievementsResponse;
import com.healthcoco.healthcocopad.bean.server.DrugDurationUnit;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.PopupWindowListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;

public class UpdateBabyAchievementsFragment extends HealthCocoDialogFragment implements LocalDoInBackgroundListenerOptimised,
        View.OnClickListener, PopupWindowListener, com.healthcoco.healthcocopad.popupwindow.PopupWindowListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "dd/MM/yyyy";
    private BabyAchievementsResponse babyAchievementsResponse;
    private TextView tvAchievement;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private TextView tvNote;
    private TextView tvAchievementDate;
    private EditText editDuration;
    private TextView tvDuration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_achievements, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setWidthHeight(0.50, 0.80);
        Bundle bundle = getArguments();
        babyAchievementsResponse = Parcels.unwrap(bundle.getParcelable(BabyAchievementsListFragment.TAG_BABY_ACHIEVEMENTS_DATA));
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
    }

    @Override
    public void initViews() {
        tvAchievement = (TextView) view.findViewById(R.id.tv_achievement);
        tvNote = (TextView) view.findViewById(R.id.tv_note);
        tvAchievementDate = (TextView) view.findViewById(R.id.tv_achievement_date);
        editDuration = (EditText) view.findViewById(R.id.edit_duration);
        tvDuration = (TextView) view.findViewById(R.id.tv_duration);
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
        initActionbarTitle(getResources().getString(R.string.edit_baby_achievemsts));
        tvAchievementDate.setOnClickListener(this);
        initPopupWindows(tvDuration, PopupWindowType.ACHIEVEMENT_DURATION, PopupWindowType.ACHIEVEMENT_DURATION.getList());
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null)
                    user = doctor.getUser();
                selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
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
                validateData();
                break;
            case R.id.tv_achievement_date:
                openDatePickerDialog();
                break;
        }
    }

    private void openDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar = DateTimeUtil.setCalendarDefaultvalue(calendar, Util.getValidatedValueOrNull(tvAchievementDate));
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvAchievementDate.setText(DateTimeUtil.getBirthDateFormat(dayOfMonth, monthOfYear + 1, year));

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        String selectedDate = String.valueOf(tvAchievementDate.getText()).trim();
        String selectedDuration = String.valueOf(editDuration.getText()).trim();
        String selectedDurationUnit = String.valueOf(tvDuration.getText()).trim();
        if (Util.isNullOrBlank(selectedDate)) {
            msg = getResources().getString(R.string.please_select_date);
            errorViewList.add(tvAchievementDate);
        } else if (Util.isNullOrBlank(selectedDuration)) {
            msg = getResources().getString(R.string.please_select_duration);
            errorViewList.add(editDuration);
        } else if (!Util.isNullOrBlank(selectedDuration) && Util.isNullOrBlank(selectedDurationUnit)) {
            msg = getResources().getString(R.string.please_select_duration_unit);
            errorViewList.add(editDuration);
        }
        if (Util.isNullOrBlank(msg))
            addAchievements(selectedDate, selectedDuration, selectedDurationUnit);
        else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addAchievements(String selectedDate, String selectedDuration, String selectedDurationUnit) {
        BabyAchievementsRequest babyAchievementsReques = new BabyAchievementsRequest();
        babyAchievementsReques.setAchievement(babyAchievementsResponse.getAchievement());
        babyAchievementsReques.setNote(babyAchievementsResponse.getNote());
        babyAchievementsReques.setAchievementDate(DateTimeUtil.getLongFromFormattedFormatString(DATE_FORMAT_USED_IN_THIS_SCREEN, selectedDate));
        AchievementsDuration achievementsDuration = new AchievementsDuration();
        achievementsDuration.setValue(selectedDuration);
        DrugDurationUnit drugDurationUnit = new DrugDurationUnit();
        drugDurationUnit.setUnit(selectedDurationUnit);
        achievementsDuration.setDurationUnit(drugDurationUnit);
        babyAchievementsReques.setDuration(achievementsDuration);
        babyAchievementsReques.setPatientId(selectedPatient.getUserId());
        babyAchievementsReques.setDoctorId(user.getUniqueId());
        if (babyAchievementsResponse != null)
            babyAchievementsReques.setUniqueId(babyAchievementsResponse.getUniqueId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addAchievements(BabyAchievementsResponse.class, babyAchievementsReques, this, this);
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
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    initData();
                    break;
                case ADD_EDIT_BABY_ACHIEVEMENTS:
                    if (response.getData() != null) {
                        if (response.getData() instanceof BabyAchievementsResponse) {
                            BabyAchievementsResponse babyAchievementsResponse = (BabyAchievementsResponse) response.getData();
                            LocalDataServiceImpl.getInstance(mApp).addBabyAchievementsResponse(babyAchievementsResponse);
                            Util.sendBroadcast(mApp, BabyAchievementsListFragment.INTENT_REFRESH_REQUEST_LIST_FROM_SERVER);
                            getDialog().dismiss();
                        }
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }

    public void initData() {
        if (babyAchievementsResponse != null) {
            if (!Util.isNullOrBlank(babyAchievementsResponse.getAchievement()))
                tvAchievement.setText(Util.getValidatedValue(babyAchievementsResponse.getAchievement()));

            if (!Util.isNullOrBlank(babyAchievementsResponse.getNote()))
                tvNote.setText(Util.getValidatedValue(babyAchievementsResponse.getNote()));

            if (babyAchievementsResponse.getAchievementDate() != null && babyAchievementsResponse.getAchievementDate() > 0)
                tvAchievementDate.setText(DateTimeUtil.getFormatedDate(babyAchievementsResponse.getAchievementDate()));

            if (babyAchievementsResponse.getDuration() != null) {
                editDuration.setText(Util.getValidatedValue(babyAchievementsResponse.getDuration().getValue()));
                tvDuration.setText(Util.getValidatedValue(babyAchievementsResponse.getDuration().getDurationUnit().getUnit()));
            }
        }
    }

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        switch (popupWindowType) {
            case ACHIEVEMENT_DURATION:
                if (object != null && object instanceof String) {
                    String duration = (String) object;
                }
                break;
        }
    }

    @Override
    public void onEmptyListFound() {

    }
}
