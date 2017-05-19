package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.PersonalHistory;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.HistoryDetailsResponse;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.PatientProfileDetailFragment;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

/**
 * Created by Shreshtha on 14-03-2017.
 */
public class AddEditPersonalHistoryDetailDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised {
    public static final String TAG_PERSONAL_HISTORY = "personalHistory";
    private PersonalHistory personalHistory;
    private EditText editDiet;
    private EditText editAddiction;
    private EditText editBowelHabits;
    private EditText editBladderHabits;
    private HistoryDetailsResponse historyDetailsResponse;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;

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
        showLoadingOverlay(true);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        getDataFromIntent();
        initListeners();
        initData();
    }

    private void getDataFromIntent() {
        historyDetailsResponse = Parcels.unwrap(getArguments().getParcelable(TAG_PERSONAL_HISTORY));
        if (historyDetailsResponse != null && historyDetailsResponse.getPersonalHistory() != null) {
            personalHistory = historyDetailsResponse.getPersonalHistory();
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
        personalHistory.setDoctorId(user.getUniqueId());
        personalHistory.setLocationId(user.getForeignLocationId());
        personalHistory.setPatientId(selectedPatient.getUserId());
        personalHistory.setHospitalId(user.getForeignHospitalId());
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

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId()) && selectedPatient != null && !Util.isNullOrBlank(selectedPatient.getUserId())) {
                    user = doctor.getUser();
                }
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
}
