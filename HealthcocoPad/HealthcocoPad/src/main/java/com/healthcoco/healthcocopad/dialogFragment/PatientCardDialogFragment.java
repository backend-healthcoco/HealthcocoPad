package com.healthcoco.healthcocopad.dialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
import com.healthcoco.healthcocopad.bean.request.PrintPatientCardRequest;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.PatientCard;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

/**
 * Created by Shreshtha on 17-07-2017.
 */

public class PatientCardDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    public static final String TAG_CALENDAR_EVENTS_DETAIL = "calendarEventsDetail";
    public static final String DATE_FORMAT_FOR_THIS_SCREEN = "dd MMM yyyy";
    private TextView tvPatientName;
    private TextView tvContactNumber;
    private TextView tvPatientId;
    private TextView tvGender;
    private TextView tvAge;
    private TextView tvDoctorName;
    private TextView tvInitialAlphabet;
    private ImageView ivContactProfile;
    private TextView tvFromTime;
    private TextView tvToTime;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private EditText etGeneralNotes;
    private Button btPrint;
    private Bundle bundle;
    private CalendarEvents calendarEvents;
    private PatientCard patient;

    public PatientCardDialogFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_patient_card, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null && bundle.containsKey(TAG_CALENDAR_EVENTS_DETAIL)) {
            calendarEvents = Parcels.unwrap(bundle.getParcelable(TAG_CALENDAR_EVENTS_DETAIL));
        }
        init();
        setWidthHeight(0.65, 0.65);

    }

    @Override
    public void init() {
        initViews();
        initListeners();
        if (calendarEvents != null) {

            initData();
        }
    }

    @Override
    public void initViews() {

        tvPatientName = (TextView) view.findViewById(R.id.tv_contact_name);
        tvContactNumber = (TextView) view.findViewById(R.id.tv_contact_number);
        tvPatientId = (TextView) view.findViewById(R.id.tv_patient_id);
        tvGender = (TextView) view.findViewById(R.id.tv_patient_gender);
        tvAge = (TextView) view.findViewById(R.id.tv_patient_age);
        tvDoctorName = (TextView) view.findViewById(R.id.tv_doctor_name);
        tvInitialAlphabet = (TextView) view.findViewById(R.id.tv_initial_aplhabet);
        ivContactProfile = (ImageView) view.findViewById(R.id.iv_image);
        tvFromTime = (TextView) view.findViewById(R.id.tv_start_time);
        tvToTime = (TextView) view.findViewById(R.id.tv_end_time);
        tvStartDate = (TextView) view.findViewById(R.id.tv_start_date);
        tvEndDate = (TextView) view.findViewById(R.id.tv_end_date);
        etGeneralNotes = (EditText) view.findViewById(R.id.et_general_notes);
        btPrint = (Button) view.findViewById(R.id.bt_print_patient_card);
    }

    @Override
    public void initListeners() {
        initCrossButton();
        btPrint.setOnClickListener(this);
    }

    @Override
    public void initData() {

        if (calendarEvents.getPatient() != null) {
            patient = calendarEvents.getPatient();
            LogUtils.LOGD(TAG, "Unique Id " + patient.getUniqueId());
            tvContactNumber.setText(Util.getValidatedValue(patient.getMobileNumber()));
            tvPatientId.setText("(" + Util.getValidatedValue(patient.getPid()) + ")");
            String formattedGenderAge = Util.getFormattedGenderAge(patient);

            String patientName = Util.getValidatedValue(patient.getLocalPatientName());
            if (!Util.isNullOrBlank(patientName)) {
                tvPatientName.setText(patientName);
            } else {
                if (!Util.isNullOrBlank(calendarEvents.getPatient().getFirstName()))
                    tvPatientName.setText(calendarEvents.getPatient().getFirstName());
            }
            if (!Util.isNullOrBlank(formattedGenderAge)) {
                tvGender.setVisibility(View.VISIBLE);
                tvGender.setText(formattedGenderAge);
            } else {
                tvGender.setVisibility(View.GONE);
                tvGender.setText("");
            }
            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENTS_LIST, patient, null, ivContactProfile, tvInitialAlphabet);
        }

        String doctorName = Util.getValidatedValueOrNull(calendarEvents.getDoctorName());
        if (!Util.isNullOrBlank(doctorName)) {
            tvDoctorName.setText(doctorName);
        } else {
            if (!Util.isNullOrBlank(calendarEvents.getCreatedBy()))
                tvDoctorName.setText(calendarEvents.getCreatedBy());
        }

        if (calendarEvents.getTime() != null && calendarEvents.getTime().getFromTime() != null && calendarEvents.getTime().getToTime() != null) {
            WorkingHours workingHours = calendarEvents.getTime();
            tvFromTime.setText(DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getFromTime())));
            tvToTime.setText(DateTimeUtil.getFormattedTime(0, Math.round(workingHours.getToTime())));
        }

        tvStartDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_FOR_THIS_SCREEN, calendarEvents.getFromDate()));
        tvEndDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_FOR_THIS_SCREEN, calendarEvents.getToDate()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_print_patient_card:
                validateData();
                break;
        }
    }

    private void validateData() {
        String generalNotes = String.valueOf(etGeneralNotes.getText());

        ptintPatientCard(generalNotes);
    }

    private void ptintPatientCard(String generalNotes) {

        PrintPatientCardRequest printPatientCardRequest = new PrintPatientCardRequest();
        if (calendarEvents != null) {
            printPatientCardRequest.setDoctorId(calendarEvents.getDoctorId());
            printPatientCardRequest.setLocationId(calendarEvents.getLocationId());
            printPatientCardRequest.setHospitalId(calendarEvents.getHospitalId());
            printPatientCardRequest.setAppointmentId(calendarEvents.getAppointmentId());
            printPatientCardRequest.setFromDate(calendarEvents.getFromDate());
        }
        if (patient != null) {
            printPatientCardRequest.setPatientName(patient.getLocalPatientName());
            printPatientCardRequest.setPatientId(patient.getPid());
            printPatientCardRequest.setMobileNumber(patient.getMobileNumber());
            printPatientCardRequest.setGender(patient.getGender());
        }
        printPatientCardRequest.setGeneralNotes(generalNotes);

        Util.checkNetworkStatus(mActivity);
        if (HealthCocoConstants.isNetworkOnline) {
            mActivity.showLoading(false);
            WebDataServiceImpl.getInstance(mApp).getPatientCardPdf(String.class, printPatientCardRequest, this, this);
        } else onNetworkUnavailable(null);
    }


    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        Util.showToast(mActivity, errorMsg);
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
            case GET_PATIENT_CARD_PDF_URL:
                if (response.getData() != null && response.getData() instanceof String) {
                    mActivity.openEnlargedImageDialogFragment(true, (String) response.getData());
                }
                break;

            default:
                break;
        }
        mActivity.hideLoading();
    }


}
