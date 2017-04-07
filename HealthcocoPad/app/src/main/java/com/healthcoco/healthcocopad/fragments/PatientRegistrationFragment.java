package com.healthcoco.healthcocopad.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.GroupsGridViewAdapter;
import com.healthcoco.healthcocopad.adapter.NotesListViewAdapter;
import com.healthcoco.healthcocopad.bean.Address;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.RegisterNewPatientRequest;
import com.healthcoco.healthcocopad.bean.server.AlreadyRegisteredPatientsResponse;
import com.healthcoco.healthcocopad.bean.server.BloodGroup;
import com.healthcoco.healthcocopad.bean.server.CityResponse;
import com.healthcoco.healthcocopad.bean.server.FileDetails;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Profession;
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.BooleanTypeValues;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.DialogType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.OptionsType;
import com.healthcoco.healthcocopad.enums.RecordType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.CommonOptionsDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.DownloadFileFromUrlListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.BitmapUtil;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.CustomAutoCompleteTextView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Shreshtha on 03-03-2017.
 */
public class PatientRegistrationFragment extends HealthCocoFragment implements View.OnClickListener, CommonListDialogItemClickListener,
        GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised,
        CommonOptionsDialogItemClickListener, DownloadFileFromUrlListener, PatientRegistrationListener {
    private static final int REQUEST_CODE_REGISTER_PATIENT = 101;
    public static ArrayList<Object> BLOOD_GROUPS = new ArrayList<Object>() {{
        add("O-");
        add("O+");
        add("A-");
        add("A+");
        add("B-");
        add("B+");
        add("AB-");
        add("AB+");
        add("A1-");
        add("A1+");
        add("A1B-");
        add("A1B+");
        add("A2-");
        add("A2+");
        add("A2B-");
        add("A2B+");
        add("B1-");
        add("B1+");
    }};
    private static final String SEPARATOR_GROUP_NOTES = "; ";
    private static final int PIC_CROP = 2;
    private EditText editName;
    private RadioGroup radioGroupGender;
    private TextView tvBirthDay;
    private EditText editEmail;
    private TextView tvReferredBy;
    private CustomAutoCompleteTextView autotvBloodGroup;
    private EditText editLocality;
    private EditText editSecondaryMobile;
    private EditText editStreetAddress;
    private AutoCompleteTextView autotvCountry;
    private EditText editPincode;
    private TextView tvMobileNumber;
    private User user;
    private String mobileNumber;
    private DialogFragment commonListDialog;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private boolean isEditPatient;

    private List<Reference> referenceList;
    private Reference selectedReference;
    private CityResponse selectedCity;
    private ImageView ivImageView;
    private Uri profileImageUri;
    private int selectedRequestCode;
    private AlreadyRegisteredPatientsResponse alreadyRegisteredPatient;
    private ArrayList<String> notesListLastAdded = new ArrayList<>();
    private LinearLayout containerEditName;
    private EditText editAadharId;
    private EditText editDrivingLicence;
    private EditText editPanCardNumber;
    private ImageButton btContactProfile;
    private FileDetails patientProfileFileDetails;
    private boolean openReferenceListScreen;
    private AutoCompleteTextView autotvCity;
    private AutoCompleteTextView autotvProfession;
    private ImageButton btDeleteBloodGroup;
    private EditText editAge;
    //    private LinearLayout containerAge;
    private ImageButton btDeleteReferredBy;
    private TextView btAddNote;
    private ListView lvNotes;
    private GridView gvGroups;

    private NotesListViewAdapter notesListViewAdapter;
    private GroupsGridViewAdapter groupsListViewAdapter;
    private ArrayList<String> groupIdsToAssign = new ArrayList<String>();
    private TextView tvNoNotes;
    private TextView tvNoGroups;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_registration, container, false);
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
            initAdapters();
            getGroupListFromLocal();
            initData();
            initDefaultData();
        }
    }

    private void initAdapters() {
        initNoteListAdapter();
        initGroupAdapter();
    }

    private void getGroupListFromLocal() {
        LocalDataServiceImpl.getInstance(mApp).getUserGroups(WebServiceType.GET_GROUPS, null, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
    }

    private void initGroupAdapter() {
        groupsListViewAdapter = new GroupsGridViewAdapter(mActivity, this);
        gvGroups.setAdapter(groupsListViewAdapter);
    }

    @Override
    public void initViews() {
        tvMobileNumber = (TextView) view.findViewById(R.id.tv_mobile_number);
        editName = (EditText) view.findViewById(R.id.edit_name);
        containerEditName = (LinearLayout) view.findViewById(R.id.container_edit_name);
        radioGroupGender = (RadioGroup) view.findViewById(R.id.rg_gender_select);
        tvBirthDay = (TextView) view.findViewById(R.id.tv_birthday);
        editEmail = (EditText) view.findViewById(R.id.edit_email);
        tvReferredBy = (TextView) view.findViewById(R.id.tv_referred_by);
        autotvBloodGroup = (CustomAutoCompleteTextView) view.findViewById(R.id.tv_blood_group);
        editSecondaryMobile = (EditText) view.findViewById(R.id.edit_secondary_mobile_number);
        editStreetAddress = (EditText) view.findViewById(R.id.edit_street);
        editLocality = (EditText) view.findViewById(R.id.edit_locality);
        autotvCountry = (AutoCompleteTextView) view.findViewById(R.id.autotv_country);
        editPincode = (EditText) view.findViewById(R.id.edit_pincode);
        ivImageView = (ImageView) view.findViewById(R.id.iv_image_view);
        btContactProfile = (ImageButton) view.findViewById(R.id.bt_contact_profile);
        editAadharId = (EditText) view.findViewById(R.id.edit_aadhar_id);
        editDrivingLicence = (EditText) view.findViewById(R.id.edit_driving_license);
        editPanCardNumber = (EditText) view.findViewById(R.id.edit_pan_number);
        autotvCity = (AutoCompleteTextView) view.findViewById(R.id.autotv_city);
        autotvProfession = (AutoCompleteTextView) view.findViewById(R.id.autotv_profession);
        btDeleteBloodGroup = (ImageButton) view.findViewById(R.id.bt_delete_blood_group);
        btDeleteReferredBy = (ImageButton) view.findViewById(R.id.bt_delete_referred_by);
        editAge = (EditText) view.findViewById(R.id.edit_age);
//        containerAge = (LinearLayout) view.findViewById(R.id.container_age);
        btAddNote = (TextView) view.findViewById(R.id.bt_add_note);
        lvNotes = (ListView) view.findViewById(R.id.lv_notes);
        tvNoNotes = (TextView) view.findViewById(R.id.tv_no_notes);
        tvNoGroups = (TextView) view.findViewById(R.id.tv_no_groups);
        gvGroups = (GridView) view.findViewById(R.id.gv_groups);
    }

    @Override
    public void initListeners() {
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
        tvBirthDay.setOnClickListener(this);
        tvReferredBy.setOnClickListener(this);
        btContactProfile.setOnClickListener(this);
        btDeleteBloodGroup.setOnClickListener(this);
        btDeleteReferredBy.setOnClickListener(this);
        btAddNote.setOnClickListener(this);
    }

    private void initDefaultData() {
        mActivity.showLoading(false);
        getCitiesList(false);
        getReferenceList(false);
        getProfessionsList(false);
        initAutoTvAdapter(autotvBloodGroup, AutoCompleteTextViewType.BLOOD_GROUP, BLOOD_GROUPS);
        initAutoTvAdapter(autotvCountry, AutoCompleteTextViewType.COUNTRY, (ArrayList<Object>) (ArrayList<?>) new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.array_countries))));
    }

    private void initNoteListAdapter() {
        notesListViewAdapter = new NotesListViewAdapter(mActivity, this);
        lvNotes.setAdapter(notesListViewAdapter);
    }

    private void initAutoTvAdapter(AutoCompleteTextView autoCompleteTextView, final AutoCompleteTextViewType autoCompleteTextViewType, ArrayList<Object> list) {
        try {
            if (!Util.isNullOrEmptyList(list)) {
                final AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(mActivity, R.layout.spinner_drop_down_item_grey_background,
                        list, autoCompleteTextViewType);
                autoCompleteTextView.setThreshold(1);
                autoCompleteTextView.setAdapter(adapter);
                autoCompleteTextView.setDropDownAnchor(autoCompleteTextView.getId());
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (autoCompleteTextViewType) {
                            case BLOOD_GROUP:
                                btDeleteBloodGroup.setVisibility(View.VISIBLE);
                                break;
                            case CITY:
                                onDialogItemClicked(CommonListDialogType.CITY, adapter.getSelectedObject(position));
                                break;
                            case PROFESSION:
                                onDialogItemClicked(CommonListDialogType.PROFESSION, adapter.getSelectedObject(position));
                                break;
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        Intent intent = mActivity.getIntent();
        String patientUniqueId = intent.getStringExtra(HealthCocoConstants.TAG_UNIQUE_ID);
        mobileNumber = intent.getStringExtra(HealthCocoConstants.TAG_MOBILE_NUMBER);
        isEditPatient = intent.getBooleanExtra(HealthCocoConstants.TAG_IS_EDIT_PATIENT, false);
        if (!Util.isNullOrBlank(patientUniqueId)) {
            if (isEditPatient) {
                selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(patientUniqueId);
                initPatientDetails(selectedPatient);
            } else {
                alreadyRegisteredPatient = LocalDataServiceImpl.getInstance(mApp).getALreadyRegisteredPatient(patientUniqueId);
                if (alreadyRegisteredPatient != null) {
                    initPatientDetails(alreadyRegisteredPatient);
                }
            }
        }
        if (!Util.isNullOrBlank(mobileNumber))
            tvMobileNumber.setText(mobileNumber);
    }

    private void initPatientDetails(Object patientDetails) {
        String imageUrl = "";
        String mobileNumber = "";
        String name = "";
        String gender = "";
        String birthday = "";
        String emailAddress = "";
        String city = "";
        String locality = "";
        String pincode = "";
        String profession = "";
        String referredBy = "";
        String bloodGroup = "";
        String secondaryMobile = "";
        String country = "";
        String streetAddress = "";
        String aadharId = "";
        String panNumber = "";
        String drivingLicense = "";
        String colorCode = "";
        if (patientDetails instanceof RegisteredPatientDetailsUpdated) {
            RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated = (RegisteredPatientDetailsUpdated) patientDetails;
            imageUrl = Util.getValidatedValue(registeredPatientDetailsUpdated.getImageUrl());
            name = Util.getValidatedValue(registeredPatientDetailsUpdated.getLocalPatientName());
            mobileNumber = Util.getValidatedValue(registeredPatientDetailsUpdated.getMobileNumber());
            gender = Util.getValidatedValue(registeredPatientDetailsUpdated.getGender());
            birthday = Util.getDOB(registeredPatientDetailsUpdated.getDob());
            groupIdsToAssign = registeredPatientDetailsUpdated.getGroupIds();
            colorCode = registeredPatientDetailsUpdated.getColorCode();
            selectedReference = registeredPatientDetailsUpdated.getReferredBy();
            if (selectedReference != null)
                referredBy = Util.getValidatedValue(selectedReference.getReference());

            if (registeredPatientDetailsUpdated.getAddress() != null) {
                Address address = registeredPatientDetailsUpdated.getAddress();
                city = Util.getValidatedValue(address.getCity());
                locality = Util.getValidatedValue(address.getLocality());
                pincode = Util.getValidatedValue(address.getPostalCode());
                country = Util.getValidatedValue(address.getCountry());
                streetAddress = Util.getValidatedValue(address.getStreetAddress());
                selectedCity = new CityResponse();
                selectedCity.setCity(Util.getValidatedValue(city));
                selectedCity.setLatitude(address.getLatitude());
                selectedCity.setLongitude(address.getLongitude());
            }
            if (registeredPatientDetailsUpdated.getPatient() != null) {
                emailAddress = Util.getValidatedValue(registeredPatientDetailsUpdated.getPatient().getEmailAddress());
                profession = Util.getValidatedValue(registeredPatientDetailsUpdated.getPatient().getProfession());
                bloodGroup = Util.getValidatedValue(registeredPatientDetailsUpdated.getPatient().getBloodGroup());
                secondaryMobile = Util.getValidatedValue(registeredPatientDetailsUpdated.getPatient().getSecMobile());
                notesListLastAdded = registeredPatientDetailsUpdated.getPatient().getNotes();
                aadharId = registeredPatientDetailsUpdated.getPatient().getAdhaarId();
                panNumber = registeredPatientDetailsUpdated.getPatient().getPanCardNumber();
                drivingLicense = registeredPatientDetailsUpdated.getPatient().getDrivingLicenseId();
            }
        } else if (patientDetails instanceof AlreadyRegisteredPatientsResponse) {
            AlreadyRegisteredPatientsResponse alreadyRegisteredPatient = (AlreadyRegisteredPatientsResponse) patientDetails;
            mobileNumber = Util.getValidatedValue(alreadyRegisteredPatient.getMobileNumber());
            name = Util.getValidatedValue(alreadyRegisteredPatient.getFirstName());
            colorCode = alreadyRegisteredPatient.getColorCode();
        }
        if (!Util.isNullOrBlank(imageUrl))
            DownloadImageFromUrlUtil.loadImageUsingImageLoaderUsingDefaultImage(R.drawable.icon_profile_normal, ivImageView, imageUrl, null);
//        else {
//            DownloadImageFromUrlUtil.loadRingWithColorCode(mActivity, PatientProfileScreenType.IN_PATIENT_REGISTRATION, colorCode, ivImageView);
//        }
        editName.setEnabled(true);
        editName.setBackgroundResource(R.drawable.edit_text_bottom_line_grey_blue_solid_white_selector);
        containerEditName.setBackgroundResource(android.R.color.transparent);
        editName.setText(name);

        tvMobileNumber.setText(mobileNumber);
        autotvCity.setText(city);
        editLocality.setText(locality);
        editPincode.setText(pincode);
        autotvProfession.setText(profession);
        tvReferredBy.setText(referredBy);
        if (!Util.isNullOrBlank(bloodGroup)) {
            autotvBloodGroup.setText(bloodGroup);
            btDeleteBloodGroup.setVisibility(View.VISIBLE);
        } else btDeleteBloodGroup.setVisibility(View.GONE);
        editSecondaryMobile.setText(secondaryMobile);
        autotvCountry.setText(country);
        editStreetAddress.setText(streetAddress);
        RadioButton radioButton = (RadioButton) radioGroupGender.findViewWithTag(gender.toUpperCase());
        if (radioButton != null)
            radioButton.setChecked(true);

//        if (!Util.isNullOrBlank(birthday)) {
//            containerAge.setVisibility(View.GONE);
        tvBirthDay.setText(birthday);
//        } else containerAge.setVisibility(View.VISIBLE);
        editEmail.setText(emailAddress);

        notifyNoteListAdapter(notesListLastAdded);
        editAadharId.setText(aadharId);
        editDrivingLicence.setText(drivingLicense);
        editPanCardNumber.setText(panNumber);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_right_action:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    validateData();
                else
                    onNetworkUnavailable(null);
                break;
            case R.id.tv_birthday:
                openBirthDatePickerDialog();
                break;
            case R.id.bt_delete_blood_group:
                autotvBloodGroup.setText("");
                btDeleteBloodGroup.setVisibility(View.GONE);
                break;
            case R.id.tv_referred_by:
                if (!Util.isNullOrEmptyList(referenceList))
                    commonListDialog = openCommonListDialogFragment(this, CommonListDialogType.REFERRED_BY, referenceList);
                else
                    Util.showToast(mActivity, R.string.no_referred_by_found);
                break;
            case R.id.bt_contact_profile:
                openDialogFragment(DialogType.SELECT_IMAGE, this);
                break;
            case R.id.bt_delete_referred_by:
                tvReferredBy.setText("");
                break;
            case R.id.bt_add_note:
                mActivity.openAddUpdateNameDialogFragment(WebServiceType.LOCAL_STRING_SAVE, AddUpdateNameDialogType.LOCAL_STRING_SAVE, this, null, "", REQUEST_CODE_REGISTER_PATIENT);
                break;
        }
    }

    private void getCitiesList(boolean openCitiesListScreen) {
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_CITIES, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void getReferenceList(boolean openReferenceListScreen) {
        this.openReferenceListScreen = openReferenceListScreen;
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_REFERENCE_LIST_ALL, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void getProfessionsList(boolean openProfessionListScreen) {
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PROFESSION_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        EditText selectedEditText = null;
        String name = Util.getValidatedValueOrNull(editName);
        String email = Util.getValidatedValueOrNull(editEmail);
        String aadharId = Util.getValidatedValueOrNull(editAadharId);
        String secondaryMobileNumber = Util.getValidatedValueOrNull(editSecondaryMobile);
        String pincode = Util.getValidatedValueOrNull(editPincode);
        if (Util.isNullOrBlank(name)) {
            selectedEditText = editName;
            msg = getResources().getString(R.string.please_enter_full_name);
        } else if (!Util.isNullOrBlank(email) && !Util.isValidEmail(email)) {
            selectedEditText = editEmail;
            msg = getResources().getString(R.string.please_enter_valid_email_address);
        } else if (!Util.isNullOrBlank(secondaryMobileNumber) && !Util.isValidMobileNo(secondaryMobileNumber)) {
            selectedEditText = editSecondaryMobile;
            msg = getResources().getString(R.string.please_enter_valid_mobile_no);
        } else if (!Util.isNullOrBlank(aadharId) && !Util.isValidAadharId(mActivity, aadharId)) {
            selectedEditText = editAadharId;
            msg = getResources().getString(R.string.please_enter_valid_aadhar_id);
        } else if (!Util.isNullOrBlank(pincode) && pincode.length() < mActivity.getResources().getInteger(R.integer.max_pincode_length)) {
            selectedEditText = editPincode;
            msg = getResources().getString(R.string.please_enter_valid_pincode);
        }
        if (Util.isNullOrBlank(msg)) {
            registerPatient(name);
        } else {
            errorViewList.add(selectedEditText);
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void registerPatient(String name) {
        mActivity.showLoading(false);
        RegisterNewPatientRequest patientDetails = new RegisterNewPatientRequest();
        patientDetails.setLocalPatientName(name);
        patientDetails.setMobileNumber(String.valueOf(tvMobileNumber.getText()));
        patientDetails.setDoctorId(user.getUniqueId());
        patientDetails.setLocationId(user.getForeignLocationId());
        patientDetails.setHospitalId(user.getForeignHospitalId());
        patientDetails.setDob(DateTimeUtil.getDob(String.valueOf(tvBirthDay.getText())));
        patientDetails.setEmailAddress(Util.getValidatedValueOrNull(String.valueOf(editEmail.getText())));
        patientDetails.setNotes(notesListLastAdded);
        patientDetails.setGroups(groupIdsToAssign);
        patientDetails.setAdhaarId(Util.getValidatedValueOrNull(editAadharId));
        patientDetails.setDrivingLicenseId(Util.getValidatedValueOrNull(editDrivingLicence));
        patientDetails.setPanCardNumber(Util.getValidatedValueOrNull(editPanCardNumber));
        //setting gender
        View checkedRadioButton = view.findViewById(radioGroupGender.getCheckedRadioButtonId());
        if (checkedRadioButton != null)
            patientDetails.setGender(String.valueOf(checkedRadioButton.getTag()));

        patientDetails.setBloodGroup(Util.getValidatedValueOrNull(String.valueOf(autotvBloodGroup.getText())));
        patientDetails.setProfession(Util.getValidatedValueOrNull(String.valueOf(autotvProfession.getText())));
        patientDetails.setReferredBy(selectedReference);
        patientDetails.setSecMobile(Util.getValidatedValueOrNull(String.valueOf(editSecondaryMobile.getText())));
        patientDetails.setAddress(getAddress());
        patientDetails.setImage(patientProfileFileDetails);

        String age = Util.getValidatedValueOrNull(editAge);
        if (!Util.isNullOrBlank(age))
            patientDetails.setAge(Integer.parseInt(age));
        if (isEditPatient) {
            patientDetails.setUserId(selectedPatient.getUserId());
            WebDataServiceImpl.getInstance(mApp).updatePatient(RegisteredPatientDetailsUpdated.class, patientDetails, this, this);
        } else {
            if (alreadyRegisteredPatient != null)
                patientDetails.setUserId(alreadyRegisteredPatient.getUserId());
            WebDataServiceImpl.getInstance(mApp).registerNewPatient(RegisteredPatientDetailsUpdated.class, patientDetails, this, this);
        }
    }

    private Address getAddress() {
        Address address = new Address();
        address.setLocality(Util.getValidatedValueOrNull(String.valueOf(editLocality.getText())));
        address.setCountry(Util.getValidatedValueOrNull(String.valueOf(autotvCountry.getText())));
        address.setPostalCode(Util.getValidatedValueOrNull(String.valueOf(editPincode.getText())));
        address.setStreetAddress(Util.getValidatedValueOrNull(editStreetAddress));
        if (selectedCity != null && Util.getValidatedValue(selectedCity.getCity()) == Util.getValidatedValueOrNull(autotvCity)) {
            address.setCity(selectedCity.getCity());
            address.setLatitude(selectedCity.getLatitude());
            address.setLongitude(selectedCity.getLongitude());
        } else
            address.setCity(Util.getValidatedValueOrNull(autotvCity));
        return address;
    }


    private FileDetails getFileDetails(String fileName, Bitmap bitmap) {
        FileDetails fileDetails = new FileDetails();
        fileDetails.setFileEncoded(ImageUtil.encodeTobase64(bitmap));
        fileDetails.setFileExtension(ImageUtil.DEFAULT_IMAGE_EXTENSION);
        fileDetails.setFileName(fileName);
        fileDetails.setBitmap(bitmap);
        return fileDetails;
    }

    private void openBirthDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar = DateTimeUtil.setCalendarDefaultvalue(calendar, Util.getValidatedValueOrNull(tvBirthDay));
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvBirthDay.setText(DateTimeUtil.getBirthDateFormat(dayOfMonth, monthOfYear + 1, year));

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(DateTimeUtil.getMaxDate(new Date().getTime()));
        datePickerDialog.show();
    }

    @Override
    public void onDialogItemClicked(CommonListDialogType commonListDialogType, Object object) {
        switch (commonListDialogType) {
            case BLOOD_GROUP:
                if (object instanceof BloodGroup) {
                    BloodGroup bloodGroup = (BloodGroup) object;
                    autotvBloodGroup.setText(bloodGroup.getBloodGroup());
                }
                break;
            case PROFESSION:
                if (object instanceof Profession) {
                    Profession profession = (Profession) object;
                    autotvProfession.setText(profession.getProfession());
                    autotvProfession.setSelection(autotvProfession.getText().length());
                }
                break;
            case CITY:
                if (object instanceof CityResponse) {
                    selectedCity = (CityResponse) object;
                    autotvCity.setText(selectedCity.getCity());
                    autotvCity.setSelection(autotvCity.getText().length());
                }
                break;
            case REFERRED_BY:
                if (object instanceof Reference) {
                    selectedReference = (Reference) object;
                    tvReferredBy.setText(selectedReference.getReference());
                }
                break;
        }
        if (commonListDialog != null)
            commonListDialog.dismiss();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        }
        if (volleyResponseBean.getWebServiceType() != null) {
            mActivity.hideLoading();
            Util.showToast(mActivity, volleyResponseBean.getWebServiceType() + errorMsg);
            return;
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
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        switch (response.getWebServiceType()) {
            case UPDATE_PATIENT:
                if (response.isValidData(response) && response.getData() instanceof RegisteredPatientDetailsUpdated) {
                    RegisteredPatientDetailsUpdated patientDetails = (RegisteredPatientDetailsUpdated) response.getData();
                    LocalDataServiceImpl.getInstance(mApp).addPatient(patientDetails);
                    mActivity.hideLoading();
                    //will directly refresh PatientDetailSCreen on its onActivityResult
                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_REGISTRATION, new Intent().putExtra(HealthCocoConstants.TAG_PATIENT_PROFILE, Parcels.wrap(patientDetails)));
                    ((CommonOpenUpActivity) mActivity).finish();
                    return;
                }
                break;
            case REGISTER_PATIENT:
                if (response.isValidData(response) && response.getData() instanceof RegisteredPatientDetailsUpdated) {
                    RegisteredPatientDetailsUpdated patientDetails = (RegisteredPatientDetailsUpdated) response.getData();
                    LogUtils.LOGD(TAG, "REGISTER_PATIENT SYNC_COMPLETE" + patientDetails.getLocalPatientName());
                    LocalDataServiceImpl.getInstance(mApp).addPatient(patientDetails);
                    openPatientDetailScreen(patientDetails);
                    refreshContactsData(patientDetails);
                    mActivity.hideLoading();
                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_REGISTRATION);
                    ((CommonOpenUpActivity) mActivity).finish();
                    return;
                }
                break;
            case GET_REFERENCE:
                if (response.isDataFromLocal()) {
                    referenceList = (ArrayList<Reference>) (ArrayList<?>) response
                            .getDataList();
                    LogUtils.LOGD(TAG, "Success onResponse referenceList Size " + referenceList.size() + " isDataFromLocal " + response.isDataFromLocal());
                } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_REFERENCE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    response.setIsFromLocalAfterApiSuccess(true);
                    LogUtils.LOGD(TAG, "Success onResponse referenceList Size Total" + referenceList.size() + " isDataFromLocal " + response.isDataFromLocal());
                    return;
                }
                if (openReferenceListScreen)
                    onClick(tvReferredBy);
                break;
            case GET_CITIES:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    initAutoTvAdapter(autotvCity, AutoCompleteTextViewType.CITY, response.getDataList());
                break;
            case GET_PROFESSION:
                if (!Util.isNullOrEmptyList(response.getDataList())) {
                    initAutoTvAdapter(autotvProfession, AutoCompleteTextViewType.PROFESSION, response.getDataList());
                }
                break;
            case GET_GROUPS:
                if (response.isDataFromLocal()) {
                    ArrayList<UserGroups> groupsList = (ArrayList<UserGroups>) (ArrayList<?>) response
                            .getDataList();
                    if (!Util.isNullOrEmptyList(groupsList))
                        LogUtils.LOGD(TAG, "Success onResponse groupsList Size " + groupsList.size() + " isDataFromLocal " + response.isDataFromLocal());
                    notifyGroupListAdapter(groupsList);
                    if (!response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                        getGroupsList();
                        return;
                    }
                } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_GROUPS_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    response.setIsFromLocalAfterApiSuccess(true);
                    return;
                }
                mActivity.hideLoading();
                break;
        }
        mActivity.hideLoading();
    }

    private void getGroupsList() {
        mActivity.showLoading(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.USER_GROUP);
        WebDataServiceImpl.getInstance(mApp).getGroupsList(WebServiceType.GET_GROUPS, UserGroups.class, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), latestUpdatedTime, groupIdsToAssign, this, this);
    }

    private void notifyGroupListAdapter(List<UserGroups> list) {
        if (!Util.isNullOrEmptyList(list)) {
            Collections.sort(list, ComparatorUtil.groupDateComparator);
            gvGroups.setVisibility(View.VISIBLE);
            tvNoGroups.setVisibility(View.GONE);
        } else {
            gvGroups.setVisibility(View.GONE);
            tvNoGroups.setVisibility(View.VISIBLE);
        }
        groupsListViewAdapter.setListData(list);
        groupsListViewAdapter.notifyDataSetChanged();
    }

    private void notifyNoteListAdapter(ArrayList<String> notesList) {
        if (!Util.isNullOrEmptyList(notesList)) {
            lvNotes.setVisibility(View.VISIBLE);
            tvNoNotes.setVisibility(View.GONE);
        } else {
            lvNotes.setVisibility(View.GONE);
            tvNoNotes.setVisibility(View.VISIBLE);
        }
        notesListViewAdapter.setListData(notesList);
        notesListViewAdapter.notifyDataSetChanged();
    }

    private void refreshContactsData(RegisteredPatientDetailsUpdated patientDetails) {
        LocalDataServiceImpl.getInstance(mApp).addPatient(patientDetails);
        mActivity.syncContacts(user);
        Util.sendBroadcast(mApp, ContactsListFragment.INTENT_GET_CONTACT_LIST_LOCAL);
    }

    private void openPatientDetailScreen(RegisteredPatientDetailsUpdated selecetdPatient) {
        if (selecetdPatient.getPatient() != null && !Util.isNullOrBlank(selecetdPatient.getPatient().getPatientId())) {
            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = selecetdPatient.getUserId();
            openCommonOpenUpActivity(CommonOpenUpFragmentType.PATIENT_DETAIL, null,
                    REQUEST_CODE_REGISTER_PATIENT);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                selectedRequestCode = requestCode;
                if (requestCode == HealthCocoConstants.REQUEST_CODE_CAMERA && profileImageUri != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), profileImageUri);
                    if (bitmap != null) {
                        showImage(profileImageUri.getPath(), bitmap);
                    }

                } else if (requestCode == HealthCocoConstants.REQUEST_CODE_GALLERY && data.getData() != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), data.getData());
                    if (bitmap != null) {
                        profileImageUri = data.getData();
                        showImage(profileImageUri.getPath(), bitmap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PIC_CROP) {
            Bundle extras = data.getExtras();
            Bitmap thePic = extras.getParcelable("data");
            if (thePic != null)
                ivImageView.setImageBitmap(thePic);
        } else if (requestCode == REQUEST_CODE_REGISTER_PATIENT) {
            LogUtils.LOGD(TAG, "Contacts List onActivityResult ");
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_STRING && data != null) {
                String note = (String) data.getSerializableExtra(HealthCocoConstants.TAG_INTENT_DATA);
                if (!Util.isNullOrBlank(note)) {
                    if (notesListLastAdded == null)
                        notesListLastAdded = new ArrayList<>();
                    notesListLastAdded.add(note);
                    notifyNoteListAdapter(notesListLastAdded);
                }
                mActivity.hideLoading();
            }
        }
    }

    private void showImage(String filePath, Bitmap originalBitmap) {
        originalBitmap = ImageUtil.getRotatedBitmapIfRequiredFromPath(filePath, originalBitmap);
        //croping bitmap to show on image as per its dimensions
        Bitmap bitmap = BitmapUtil.scaleCenterCrop(originalBitmap, Util.getValidatedWidth(ivImageView.getWidth()), Util.getValidatedHeight(ivImageView.getHeight()));
        if (bitmap != null) ivImageView.setImageBitmap(bitmap);
        //passing original bitmap to servere
        patientProfileFileDetails = getFileDetails(ImageUtil.DEFAULT_PATIENT_IMAGE_NAME, originalBitmap);
    }

    private void performCrop(Uri profileUri) {
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(profileUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(mActivity, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * dont add break statement after add(since we need to call get statement also after add
     *
     * @param response
     * @return
     */
    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_CITIES:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getCitiesList(WebServiceType.GET_CITIES, null, null);
                break;
            case GET_BLOOD_GROUP:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getBloodGroup(WebServiceType.GET_BLOOD_GROUP, null, null);
                break;
            case ADD_REFERENCE:
                LocalDataServiceImpl.getInstance(mApp).addReferenceList((ArrayList<Reference>) (ArrayList<?>) response.getDataList());
            case GET_REFERENCE_LIST_ALL:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getReferenceList(WebServiceType.GET_REFERENCE, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), BooleanTypeValues.FALSE, RecordType.BOTH, null, null);
                break;
            case ADD_PROFESSION:
                LocalDataServiceImpl.getInstance(mApp).addProfessionsList((ArrayList<Profession>) (ArrayList<?>) response.getDataList());
            case GET_PROFESSION_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getProfessionList(WebServiceType.GET_PROFESSION, null, null);
                break;
            case ADD_GROUPS_LIST:
                LocalDataServiceImpl.getInstance(mApp).addUserGroupsList((ArrayList<UserGroups>) (ArrayList<?>) response.getDataList());
            case GET_GROUPS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getUserGroups(WebServiceType.GET_GROUPS, groupIdsToAssign, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), null, null);
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean volleyResponseBean) {

    }

    @Override
    public void onOptionsItemSelected(Object object) {
        OptionsType optionsType = (OptionsType) object;
        if (optionsType != null) {
            LogUtils.LOGD(TAG, getResources().getString(optionsType.getStringId()));
            switch (optionsType) {
                case CAMERA:
                    profileImageUri = mActivity.openCamera(this, "profileImage");
                    break;
                case GALLERY:
                    mActivity.openGallery(this);
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (selectedRequestCode == HealthCocoConstants.REQUEST_CODE_CAMERA && profileImageUri != null)
            ImageUtil.deleteFileFrom(profileImageUri);

    }

    @Override
    public void onPostExecute(String filePath) {
        if (!Util.isNullOrBlank(filePath)) {
            if (!Util.isNullOrBlank(filePath)) {
                int width = ivImageView.getLayoutParams().width;
                LogUtils.LOGD(TAG, "Image SIze " + width);
                Bitmap bitmap = ImageUtil.getDecodedBitmapFromPath(filePath, width, width);
                if (bitmap != null) {
                    ivImageView.setImageBitmap(bitmap);
                    ivImageView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onDeleteNotesClicked(String notes) {
        showConfirmDeleteAlert(notes);
    }

    private void showConfirmDeleteAlert(final String notes) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm);
        alertBuilder.setMessage(getResources().getString(
                R.string.confirm_delete_note));
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (notesListLastAdded.contains(notes)) {
                    notesListLastAdded.remove(notes);
                    notifyNoteListAdapter(notesListLastAdded);
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
    public void onAssignGroupCheckClicked(boolean isSelected, UserGroups group) {
        String groupId = group.getUniqueId();
        if (isSelected) {
            if (!groupIdsToAssign.contains(groupId)) {
                groupIdsToAssign.add(groupId);
            }
        } else if (groupIdsToAssign.contains(groupId)) {
            groupIdsToAssign.remove(groupId);
        }
    }

    @Override
    public boolean isGroupAssigned(String groupId) {
        return groupIdsToAssign.contains(groupId);
    }
}
