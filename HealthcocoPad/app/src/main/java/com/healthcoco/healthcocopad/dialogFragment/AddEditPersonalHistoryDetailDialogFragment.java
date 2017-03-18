package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.HistoryDetailsResponse;
import com.healthcoco.healthcocopad.bean.server.PersonalHistory;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.PatientProfileDetailFragment;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

/**
 * Created by Shreshtha on 14-03-2017.
 */
public class AddEditPersonalHistoryDetailDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {
    private PersonalHistory personalHistory;
    private HistoryDetailsResponse historyDetailsResponse;
    private EditText editDiet;
    private EditText editAddiction;
    private EditText editBowelHabits;
    private EditText editBladderHabits;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_edit_personal_history, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setWidthHeight(0.50, 0.75);
        init();
    }

    @Override
    public void init() {
        initViews();
        getDataFromIntent();
        initListeners();
        initData();
    }

    private void getDataFromIntent() {
        historyDetailsResponse = Parcels.unwrap(getArguments().getParcelable(HealthCocoConstants.TAG_PERSONAL_HISTORY));
        if (!Util.isNullOrEmptyList(historyDetailsResponse)) {
            personalHistory = historyDetailsResponse.getPersonalHistory();
            if (!Util.isNullOrEmptyList(personalHistory)) {
                String diet = personalHistory.getDiet();
                String addictions = personalHistory.getAddictions();
                String bowelHabit = personalHistory.getBowelHabit();
                String bladderHabit = personalHistory.getBladderHabit();
                editDiet.setText(Util.getValidatedValue(diet));
                editAddiction.setText(Util.getValidatedValue(addictions));
                editBowelHabits.setText(Util.getValidatedValue(bowelHabit));
                editBladderHabits.setText(Util.getValidatedValue(bladderHabit));
            }
        }
    }

    @Override
    public void initViews() {
        editDiet = (EditText) view.findViewById(R.id.edit_diet);
        editAddiction = (EditText) view.findViewById(R.id.edit_addiction);
        editBowelHabits = (EditText) view.findViewById(R.id.edit_bowel_habits);
        editBladderHabits = (EditText) view.findViewById(R.id.edit_bladder_habits);
    }


    @Override
    public void initListeners() {
        initSaveCancelButton(this);
        initActionbarTitle(getResources().getString(R.string.personal_history));
    }

    @Override
    public void initData() {

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
        String diet = Util.getValidatedValueOrNull(editDiet);
        String addiction = Util.getValidatedValueOrNull(editAddiction);
        String bowelHabit = Util.getValidatedValueOrNull(editBowelHabits);
        String bladderHabit = Util.getValidatedValueOrNull(editBladderHabits);

        addEditPerosnalHistoryDetails(diet, addiction, bowelHabit, bladderHabit);

    }

    private void addEditPerosnalHistoryDetails(String diet, String addiction, String bowelHabit, String bladderHabit) {
        mActivity.showLoading(false);
        personalHistory = new PersonalHistory();
        personalHistory.setDiet(diet);
        personalHistory.setAddictions(addiction);
        personalHistory.setBowelHabit(bowelHabit);
        personalHistory.setBladderHabit(bladderHabit);
        personalHistory.setDoctorId(historyDetailsResponse.getDoctorId());
        personalHistory.setLocationId(historyDetailsResponse.getLocationId());
        personalHistory.setPatientId(historyDetailsResponse.getPatientId());
        personalHistory.setHospitalId(historyDetailsResponse.getHospitalId());
        WebDataServiceImpl.getInstance(mApp).addUpdatePersonalHistory(HistoryDetailsResponse.class, personalHistory, this, this);
    }

    private void clearPreviousAlerts() {
        editDiet.setActivated(false);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        switch (response.getWebServiceType()) {
            case ADD_UPDATE_PERSONAL_HISTORY_DETAIL:
                if (response.getData() != null && response.getData() instanceof HistoryDetailsResponse) {
                    HistoryDetailsResponse historyDetailsResponse = (HistoryDetailsResponse) response.getData();
                    personalHistory = historyDetailsResponse.getPersonalHistory();
//                    LocalDataServiceImpl.getInstance(mApp).addDoctorProfile(doctorProfile);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), HealthCocoConstants.RESULT_CODE_DOCTOR_PERSONAL_HISTORY_DETAIL, new Intent().putExtra(PatientProfileDetailFragment.TAG_PERSONAL_HISTORY, Parcels.wrap(personalHistory)));
                    getDialog().dismiss();
                }
                break;
        }
        mActivity.hideLoading();
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
}
