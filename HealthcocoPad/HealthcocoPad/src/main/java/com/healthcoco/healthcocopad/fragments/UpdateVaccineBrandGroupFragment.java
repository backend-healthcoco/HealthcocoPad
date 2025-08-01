package com.healthcoco.healthcocopad.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.server.VaccineCustomResponse;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.VaccineRequest;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VaccineBrandResponse;
import com.healthcoco.healthcocopad.bean.server.VaccineResponse;
import com.healthcoco.healthcocopad.dialogFragment.AddEditVaccineNoteDialogFragment;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.VaccineStatus;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddEditVaccineNoteListener;
import com.healthcoco.healthcocopad.listeners.SelectVaccinationListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ReflectionUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.VaccinationListViewHolder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;

public class UpdateVaccineBrandGroupFragment extends HealthCocoFragment implements View.OnClickListener, SelectVaccinationListener, CompoundButton.OnCheckedChangeListener, AddEditVaccineNoteListener {

    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "dd MMM yyyy";
    public static final String TAG_VACCINE_NOTE = "note";
    public static final String TAG_VACCINE_BRAND_DATA = "vaccineBrandData";
    private RecyclerView rvVaccineList;
    private Button btUpdateVaccine;
    private HealthcocoRecyclerViewAdapter adapter;
    private TextView tvVaccineName;
    private TextView tvDueDate;
    private TextView tvGivenDate;
    private View tvAddNote;
    private VaccineCustomResponse vaccineCustomResponse;
    private LinkedHashMap<String, VaccineBrandResponse> brandLinkedHashMap;
    private LinkedHashMap<String, VaccineRequest> requestLinkedHashMapForValidate = new LinkedHashMap<>();
    private LinkedHashMap<String, VaccineRequest> requestNewLinkedHashMapForValidate = new LinkedHashMap<>();
    private ArrayList<VaccineResponse> vaccineResponse;
    private ArrayList<VaccineResponse> vaccineResponseNew = new ArrayList<>();
    private LinkedHashMap<String, String> vaccineBrandName = new LinkedHashMap<>();
    private TextView tvBrandName;
    private ArrayList<VaccineBrandResponse> responseList;
    private User user;
    private LinkedHashMap<String, VaccineBrandResponse> vaccineBrandHashmap = new LinkedHashMap<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_vaccine_brand_group, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
        }
        init();
    }

    @Override
    public void init() {
        Intent intent = mActivity.getIntent();
        vaccineCustomResponse = Parcels.unwrap(intent.getParcelableExtra(VaccinationListViewHolder.TAG_VACCINE_DATA));
        brandLinkedHashMap = Parcels.unwrap(intent.getParcelableExtra(TAG_VACCINE_BRAND_DATA));
        responseList = Parcels.unwrap(intent.getParcelableExtra(UpdateBrandsFragment.TAG_BRAND_DATA));
        vaccineBrandHashmap = Parcels.unwrap(intent.getParcelableExtra(UpdateBrandsFragment.TAG_SELECTED_BRAND_DATA));
        vaccineResponse = vaccineCustomResponse.getVaccineResponse();
        initViews();
        initListeners();
        initData();
        initAdapters();
    }

    private void initData() {
        if (vaccineCustomResponse.getDueDate() != null && vaccineCustomResponse.getDueDate() > 0)
            tvDueDate.setText(DateTimeUtil.getFormatedDateAndTime(DATE_FORMAT_USED_IN_THIS_SCREEN, vaccineCustomResponse.getDueDate()));
        else tvDueDate.setText("");
        tvGivenDate.setText(DateTimeUtil.getCurrentFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN));

        for (VaccineBrandResponse response :
                brandLinkedHashMap.values()) {
            tvBrandName.setText(response.getVaccineBrand().getName());
            tvBrandName.setTag(response.getVaccineBrandId());
        }

        for (VaccineBrandResponse vaccineBrandResponse :
                responseList) {
            if (vaccineBrandResponse.getVaccineBrand().getName().equals(tvBrandName.getText().toString())) {
                vaccineBrandName.put(vaccineBrandResponse.getVaccine().getUniqueId(), vaccineBrandResponse.getVaccine().getName());
            }
        }
        for (VaccineResponse response1 :
                vaccineResponse) {
            if (!vaccineBrandName.containsValue(response1.getName())) {
                VaccineBrandResponse brandResponse = vaccineBrandHashmap.get(response1.getVaccineId());
                if (brandResponse != null) {
                    response1.setGivenDate(DateTimeUtil.getCurrentDateLong());
                    response1.setSelected(true);
                }
                vaccineResponseNew.add(response1);
            }
            if (vaccineBrandName.containsValue(response1.getName())) {
                VaccineRequest vaccineRequest = new VaccineRequest();
                try {
                    ReflectionUtil.copy(vaccineRequest, response1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                vaccineRequest.setStatus(VaccineStatus.GIVEN);
                vaccineRequest.setDoctorId(user.getUniqueId());
                vaccineRequest.setLocationId(user.getForeignLocationId());
                vaccineRequest.setHospitalId(user.getForeignHospitalId());

                requestLinkedHashMapForValidate.put(response1.getUniqueId(), vaccineRequest);
            }
        }

        tvVaccineName.setText(Util.getStringFromListOfString(new ArrayList<>(vaccineBrandName.values())));
        for (VaccineRequest vaccineRequest :
                requestLinkedHashMapForValidate.values()) {
            vaccineRequest.setGivenDate(DateTimeUtil.getCurrentDateLong(DATE_FORMAT_USED_IN_THIS_SCREEN));
        }

    }

    private void initAdapters() {
        adapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.UPDATE_VACCINE, this);
        rvVaccineList.setAdapter(adapter);
        adapter.setListData((ArrayList<Object>) (Object) vaccineResponseNew);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initViews() {
        rvVaccineList = (RecyclerView) view.findViewById(R.id.rv_vaccine_list);
//        int spacingInPixelsVerticalBlogs = getResources().getDimensionPixelSize(R.dimen.item_spacing_vaccination_list_item);
        //initialsing adapter for Health Blogs
        rvVaccineList.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
//        rvVaccineList.addItemDecoration(new VerticalRecyclerViewItemDecoration(spacingInPixelsVerticalBlogs));
        btUpdateVaccine = (Button) view.findViewById(R.id.bt_update_vaccine);

        tvVaccineName = (TextView) view.findViewById(R.id.tv_vaccine_name);
        tvBrandName = (TextView) view.findViewById(R.id.tv_brand_name);
        tvDueDate = (TextView) view.findViewById(R.id.tv_due_date);
        tvGivenDate = (TextView) view.findViewById(R.id.tv_given_date);
        tvAddNote = view.findViewById(R.id.tv_add_note);
    }


    @Override
    public void initListeners() {
        btUpdateVaccine.setOnClickListener(this);
        tvAddNote.setOnClickListener(this);
        tvGivenDate.setOnClickListener(this);
        tvDueDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_update_vaccine) {
            validateData();

        } else if (v.getId() == R.id.tv_due_date || v.getId() == R.id.tv_given_date) {
            openDatePickerDialog((TextView) v);

        } else if (v.getId() == R.id.tv_add_note) {
            String note = null;
            for (VaccineRequest vaccineRequest : requestLinkedHashMapForValidate.values()) {
                note = vaccineRequest.getNote();
            }
            openAddEditVaccineNoteDialogFragment(note);
        }
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        UpdateBrandsFragment updateBrandsFragment = (UpdateBrandsFragment) getFragmentManager().findFragmentByTag(UpdateBrandsFragment.class.getSimpleName());
        if (updateBrandsFragment != null) {
            for (VaccineRequest vaccineRequest :
                    requestLinkedHashMapForValidate.values()) {
                if (!Util.isNullOrBlank(updateBrandsFragment.getVaccineBrandResponse(vaccineRequest.getVaccineId())))
                    vaccineRequest.setVaccineBrandId(updateBrandsFragment.getVaccineBrandResponse(vaccineRequest.getVaccineId()));

            }
        } else {
            for (VaccineRequest vaccineRequest :
                    requestLinkedHashMapForValidate.values()) {
                vaccineRequest.setVaccineBrandId((String) tvBrandName.getTag());
                vaccineRequest.setDoctorId(user.getUniqueId());
                vaccineRequest.setLocationId(user.getForeignLocationId());
                vaccineRequest.setHospitalId(user.getForeignHospitalId());
            }
        }

        for (VaccineRequest vaccineRequest :
                requestNewLinkedHashMapForValidate.values()) {
            VaccineBrandResponse brandResponse = vaccineBrandHashmap.get(vaccineRequest.getVaccineId());
            if (brandResponse != null) {
                vaccineRequest.setVaccineBrandId(brandResponse.getVaccineBrandId());

                vaccineRequest.setStatus(VaccineStatus.GIVEN);
                vaccineRequest.setDoctorId(user.getUniqueId());
                vaccineRequest.setLocationId(user.getForeignLocationId());
                vaccineRequest.setHospitalId(user.getForeignHospitalId());
            }
        }

        for (VaccineRequest vaccineRequest :
                requestLinkedHashMapForValidate.values()) {
            if (vaccineRequest.getGivenDate() == null)
                msg = getResources().getString(R.string.please_select_given_date);
        }
        for (VaccineRequest vaccineRequest :
                requestNewLinkedHashMapForValidate.values()) {
            if (vaccineRequest.getGivenDate() == null)
                msg = getResources().getString(R.string.please_select_given_date);
        }
        if (Util.isNullOrBlank(msg)) {
            sendUpdatedVaccineList();
        } else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }

    }

    private void sendUpdatedVaccineList() {
        mActivity.showLoading(false);
        requestLinkedHashMapForValidate.putAll(requestNewLinkedHashMapForValidate);
        WebDataServiceImpl.getInstance(mApp).addEditVaccinationList(Boolean.class, WebServiceType.ADD_EDIT_MULTIPLE_VACCINATION,
                new ArrayList<VaccineRequest>(requestLinkedHashMapForValidate.values()), this, this);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
        }

        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
        showLoadingOverlay(false);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        mActivity.hideLoading();
        Util.showToast(mActivity, R.string.user_offline);
        showLoadingOverlay(false);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
            case ADD_EDIT_MULTIPLE_VACCINATION:
                if (response.isValidData(response)) {
                    if (response.getData() != null && response.getData() instanceof Boolean) {
                        if (response.getData() instanceof Boolean) {
                            boolean isDataSuccess = (boolean) response.getData();
                            if (isDataSuccess) {
                                Util.sendBroadcast(mApp, VaccinationListFragment.INTENT_REFRESH_REQUEST_LIST_FROM_SERVER);
                            } else Util.showToast(mActivity, R.string.vaccines_not_updated);
                            ((CommonOpenUpActivity) mActivity).setResult(HealthCocoConstants.RESULT_CODE_UPDATA_BRAND_GROUP);
                            ((CommonOpenUpActivity) mActivity).finish();
                        } else {
                            Util.showToast(mActivity, response.getErrMsg());
                        }
                    }
                }
                break;
            default:
                break;
        }
        mActivity.hideLoading();
        showLoadingOverlay(false);
    }

    @Override
    public void isVaccinationClicked(boolean isChecked, VaccineResponse response) {
        try {
            VaccineRequest vaccineRequest = new VaccineRequest();
            ReflectionUtil.copy(vaccineRequest, response);
            vaccineRequest.setStatus(VaccineStatus.GIVEN);
            vaccineRequest.setDoctorId(user.getUniqueId());
            vaccineRequest.setLocationId(user.getForeignLocationId());
            vaccineRequest.setHospitalId(user.getForeignHospitalId());

            if (isChecked) {
                requestNewLinkedHashMapForValidate.put(response.getUniqueId(), vaccineRequest);
            } else {
                requestNewLinkedHashMapForValidate.remove(vaccineRequest.getUniqueId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isVaccinationSelected(String uniqueId) {
        return false;
    }

    @Override
    public void setSelectAll(boolean isSelected) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    private void openAddEditVaccineNoteDialogFragment(String note) {
        Bundle args = new Bundle();
        args.putString(TAG_VACCINE_NOTE, note);
        AddEditVaccineNoteDialogFragment dialogFragment = new AddEditVaccineNoteDialogFragment(this);
        dialogFragment.setArguments(args);
        dialogFragment.show(mActivity.getSupportFragmentManager(), dialogFragment.getClass().getSimpleName());
    }

    private void openDatePickerDialog(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        calendar = DateTimeUtil.setCalendarDefaultvalue(DATE_FORMAT_USED_IN_THIS_SCREEN, calendar, textView);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                textView.setText(DateTimeUtil.getFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN, year, monthOfYear, dayOfMonth, 0, 0, 0));
                if (textView.getId() == R.id.tv_due_date) {
                    for (VaccineRequest vaccineRequest :
                            requestLinkedHashMapForValidate.values()) {
                        vaccineRequest.setDueDate(DateTimeUtil.getLongFromFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, tvDueDate.getText().toString()));
                    }
                } else if (textView.getId() == R.id.tv_given_date) {
                    for (VaccineRequest vaccineRequest :
                            requestLinkedHashMapForValidate.values()) {
                        vaccineRequest.setGivenDate(DateTimeUtil.getLongFromFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, tvGivenDate.getText().toString()));
                    }
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void getVaccineNote(String note) {
        for (VaccineRequest vaccineRequest :
                requestLinkedHashMapForValidate.values()) {
            vaccineRequest.setNote(note);
        }
    }
}
