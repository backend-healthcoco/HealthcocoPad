package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.BiometricDetails;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.InvestigationNote;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.InvestigationSubType;
import com.healthcoco.healthcocopad.enums.InvestigationType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.DoctorListPopupWindowListener;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.popupwindow.HealthcocoPopupWindow;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class InvestigationGraphFragment extends HealthCocoFragment
        implements View.OnClickListener, LocalDoInBackgroundListenerOptimised, DoctorListPopupWindowListener, PopupWindowListener {

    public ArrayList<RegisteredDoctorProfile> clinicDoctorProfileList;
    public List<RegisteredDoctorProfile> registeredDoctorProfileList;
    InvestigationSubType investigationSubType;
    private LineChart lineChart;
    private LinearLayout layoutLineChart;
    private TextView tvNoDataFound;
    private TextView tvTitle;
    private TextView tvInvestigation;
    private TextView tvDone;
    private ImageButton btnBack;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;
    private DoctorProfile doctorProfile;
    private TextView tvDoctorName;
    private LinkedHashMap<String, RegisteredDoctorProfile> clinicDoctorListHashMap = new LinkedHashMap<>();
    private HealthcocoPopupWindow doctorsListPopupWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_investigation_graphs, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();
        initListeners();

        Intent intent = mActivity.getIntent();

        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void init() {

    }

    @Override
    public void initViews() {


        tvTitle = (TextView) view.findViewById(R.id.tv_title_chart);
        tvDone = (TextView) view.findViewById(R.id.tv_done);
        btnBack = (ImageButton) view.findViewById(R.id.bt_back);
        tvDoctorName = (TextView) view.findViewById(R.id.tv_doctor_name);
        tvNoDataFound = (TextView) view.findViewById(R.id.tv_no_data_found);
        tvInvestigation = (TextView) view.findViewById(R.id.tv_investigation);
        lineChart = (LineChart) view.findViewById(R.id.line_chart);
        layoutLineChart = (LinearLayout) view.findViewById(R.id.layout_line_chart);
    }

    @Override
    public void initListeners() {
        btnBack.setOnClickListener(this);
        tvDone.setOnClickListener(this);

        List<Object> listType = (List<Object>) (List<?>) Arrays.asList(InvestigationSubType.values());

        mActivity.initPopupWindows(tvInvestigation, PopupWindowType.INVESTIGATION_LIST, listType, this);
    }

    private void initLineChart(float minLimit, float maxLimit) {

//        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
//        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setBackgroundColor(Color.WHITE);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextSize(13f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);/*
        xAxis.setAxisMaximum(10f);
        xAxis.setAxisMinimum(0f);*/

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(13f);
//        leftAxis.setAxisMaximum(maxLimit);
//        leftAxis.setAxisMinimum(minLimit);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularityEnabled(true);


        lineChart.getAxisRight().setEnabled(false);

//        lineChart.animateX(2000);
        lineChart.animateY(1600);

        //mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);

        // // dont forget to refresh the drawing
    }


    private void setLineChartData(String label, ArrayList<Entry> entries) {
        LineDataSet set1;

        // create a dataset and give it a type

        set1 = new LineDataSet(entries, label);
        // set the line to be drawn like this "- - - - - -"
//        set1.enableDashedLine(10f, 5f, 0f);
//        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.RED);
        set1.setCircleColor(Color.RED);
        set1.setLineWidth(1.5f);
        set1.setCircleRadius(5f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(13f);
        set1.setDrawFilled(true);
        set1.setFormLineWidth(1f);
//        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set1.setFormSize(15.f);

        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(mActivity, R.drawable.fade_red);
            set1.setFillDrawable(drawable);
        } else {
            set1.setFillColor(Color.BLACK);
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets
        // create a data object with the datasets
        LineData data = new LineData(dataSets);
        // set data
        lineChart.setData(data);

//        lineChart.invalidate();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_done:
                getListFromLocal(true);
                break;
            case R.id.bt_back:
                mActivity.finish();
                break;
            default:
                break;
        }
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
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    user = doctor.getUser();
                    if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId()) && selectedPatient != null && !Util.isNullOrBlank(selectedPatient.getUserId())) {
                        doctorProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorProfileObject(user.getUniqueId());
                    }
                    registeredDoctorProfileList = LocalDataServiceImpl.getInstance(mApp).getRegisterDoctorDetails(user.getForeignLocationId());
                }
                break;
            case GET_INVESTIGATION_NOTE:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getInvestigationList(WebServiceType.GET_INVESTIGATION_NOTE, clinicDoctorProfileList, user.getForeignLocationId(),
                        user.getForeignHospitalId(), selectedPatient.getUserId(), 0, 100, null, null);
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
    public void onResponse(VolleyResponseBean response) {
        super.onResponse(response);
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (!Util.isNullOrEmptyList(registeredDoctorProfileList))
                        fromHashMapAndRefresh(registeredDoctorProfileList);
                    if (selectedPatient != null) {
                        getListFromLocal(true);
                    }
                    break;
                case GET_INVESTIGATION_NOTE:
                    ArrayList<InvestigationNote> responseList = (ArrayList<InvestigationNote>) (ArrayList<?>) response
                            .getDataList();
                    formHashMapAndRefresh(responseList);
                    showLoadingOverlay(false);

                    break;
            }
        }
        mActivity.hideLoading();
    }


    public void getListFromLocal(boolean initialLoading) {
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_INVESTIGATION_NOTE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void formHashMapAndRefresh(ArrayList<InvestigationNote> responseList) {
        ArrayList<Entry> entries = new ArrayList<Entry>();
        String text = "0";

        if (investigationSubType != null) {
            if (!Util.isNullOrEmptyList(responseList)) {
                layoutLineChart.setVisibility(View.VISIBLE);
                tvNoDataFound.setVisibility(View.GONE);
                for (InvestigationNote investigationNote :
                        responseList) {
                    switch (investigationSubType) {
                        case AEC:
                            text = investigationNote.getAbsoluteEosinophilCount();
                            break;
                        case HAEMOGLOBIN:
                            text = investigationNote.getHaemoglobin();
                            break;
                        case TWC:
                            text = investigationNote.getTotalWcbCount();
                            break;
                        case HAEMATOCRIT:
                            text = investigationNote.getHaematocrit();
                            break;
                        case NEUTROPHILS:
                            text = investigationNote.getNeutrophils();
                            break;
                        case LYMPHOCYTES:
                            text = investigationNote.getLymphocytes();
                            break;
                        case EOSINOPHILS:
                            text = investigationNote.getEosinophils();
                            break;
                        case MONOCYTES:
                            text = investigationNote.getMonocytes();
                            break;
                        case BASOPHILS:
                            text = investigationNote.getBasophils();
                            break;
                        case RBC:
                            text = investigationNote.getRbcRedBloodCells();
                            break;
                        case ESR:
                            text = investigationNote.getErythrocyteSedimentationrate();
                            break;
                        case RBCS:
                            text = investigationNote.getRbcs();
                            break;
                        case WBCS:
                            text = investigationNote.getWbcs();
                            break;
                        case PLATELETS:
                            text = investigationNote.getPlatelets();
                            break;
                        case HAEMOPARASITES:
                            text = investigationNote.getHaemoparasites();
                            break;
                        case IMPRESSION:
                            text = investigationNote.getImpression();
                            break;
                        case MCV:
                            text = investigationNote.getMeanCorpuscularVolume();
                            break;
                        case MCH:
                            text = investigationNote.getMeanCorpuscularHaemoglobin();
                            break;
                        case MCHC:
                            text = investigationNote.getMeanCorpuscularHaemoglobinConcentration();
                            break;
                        case FSB:
                            text = investigationNote.getFastingBloodSugar();
                            break;
                        case FUS:
                            text = investigationNote.getFastingUrineSugar();
                            break;
                        case PPBS:
                            text = investigationNote.getPostPrandialBloodSugar();
                            break;
                        case PPUS:
                            text = investigationNote.getPostPrandialUrineSugar();
                            break;
                        case GH:
                            text = investigationNote.getGlycosylatedHaemoglobin();
                            break;
                        case MBG:
                            text = investigationNote.getMeanBloodGlucose();
                            break;
                        case RBS:
                            text = investigationNote.getRbs();
                            break;
                        case RUS:
                            text = investigationNote.getRandomUrineSugar();
                            break;
                        case KETONE:
                            text = investigationNote.getKetone();
                            break;
                        case PROTEIN:
                            text = investigationNote.getProtein();
                            break;
                        case BU:
                            text = investigationNote.getBloodUrea();
                            break;
                        case SC:
                            text = investigationNote.getSerumCreatinine();
                            break;
                        case SS:
                            text = investigationNote.getSerumSodium();
                            break;
                        case TC:
                            text = investigationNote.getTotalCholesterol();
                            break;
                        case SHC:
                            text = investigationNote.getSerumHdlCholesterol();
                            break;
                        case ST:
                            text = investigationNote.getSerumTriglycerides();
                            break;
                        case SLC:
                            text = investigationNote.getSerumLdlCholesterol();
                            break;
                        case SVC:
                            text = investigationNote.getSerumVdlCholesterol();
                            break;
                        case LDL:
                            text = investigationNote.getLdlCholesterol();
                            break;
                        case TRIGLYCERIDES:
                            text = investigationNote.getTrigkycerides();
                            break;
                        case NHC:
                            text = investigationNote.getNonHdlCholesterol();
                            break;
                        case SBT:
                            text = investigationNote.getSerumBilirubinTotal();
                            break;
                        case SBD:
                            text = investigationNote.getSerumBilirubinDirect();
                            break;
                        case BI:
                            text = investigationNote.getBilirubinIndirect();
                            break;
                    }
                    if (!Util.isNullOrBlank(text)) {
                        Float entry = Float.parseFloat(text);
                        entries.add(new Entry(responseList.indexOf(investigationNote), entry, getResources().getDrawable(R.drawable.star)));
                    }
                }
                if (!Util.isNullOrEmptyList(entries)) {
                    initLineChart(0f, 250f);
                    setLineChartData(investigationSubType.getValue(), entries);
                    tvTitle.setText(investigationSubType.getTextId());
                } else {
                    layoutLineChart.setVisibility(View.GONE);
                    tvNoDataFound.setVisibility(View.VISIBLE);
                }
            } else {
                layoutLineChart.setVisibility(View.GONE);
                tvNoDataFound.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDoctorSelected(ArrayList<RegisteredDoctorProfile> clinicDoctorProfileList) {
        this.clinicDoctorProfileList = clinicDoctorProfileList;
        String doctorName = "";
        if (!Util.isNullOrEmptyList(clinicDoctorProfileList)) {
            if (clinicDoctorProfileList.size() == clinicDoctorListHashMap.size())
                tvDoctorName.setText(R.string.all_doctor);
            else {
                for (RegisteredDoctorProfile clinicDoctorProfile : clinicDoctorProfileList) {
                    doctorName = doctorName + clinicDoctorProfile.getFirstNameWithTitle() + ", ";
                }
                doctorName = doctorName.substring(0, doctorName.length() - 2);
                tvDoctorName.setText(doctorName);
            }
            getListFromLocal(true);
        }
    }

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        switch (popupWindowType) {
            case INVESTIGATION_LIST:
                investigationSubType = (InvestigationSubType) object;
                tvInvestigation.setText(investigationSubType.getTextId());
                break;
        }
    }

    @Override
    public void onEmptyListFound() {

    }

    private void fromHashMapAndRefresh(List<RegisteredDoctorProfile> responseList) {
//        clinicDoctorProfileList = (ArrayList<RegisteredDoctorProfile>) responseList;
        if (responseList.size() > 1) {
            tvDoctorName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_drug, 0);
            tvDoctorName.setEnabled(true);
            tvDoctorName.setText(R.string.all_doctor);

            if (!Util.isNullOrEmptyList(responseList)) {
                for (RegisteredDoctorProfile registeredDoctorProfile :
                        responseList) {
                    clinicDoctorListHashMap.put(registeredDoctorProfile.getUserId(), registeredDoctorProfile);
                }
            }
//            notifyAdapter(new ArrayList<ClinicDoctorProfile>(clinicDoctorListHashMap.values()));
            if (doctorsListPopupWindow != null)
                doctorsListPopupWindow.notifyAdapter(new ArrayList<Object>(clinicDoctorListHashMap.values()));
            else {
                mActivity.initDoctorListPopupWindows(tvDoctorName, PopupWindowType.DOCTOR_LIST, new ArrayList<Object>(clinicDoctorListHashMap.values()), this);
            }
        } else {
            tvDoctorName.setText("Dr. " + user.getFirstName());
            tvDoctorName.setEnabled(false);
            clinicDoctorProfileList = (ArrayList<RegisteredDoctorProfile>) responseList;
        }
    }

}

