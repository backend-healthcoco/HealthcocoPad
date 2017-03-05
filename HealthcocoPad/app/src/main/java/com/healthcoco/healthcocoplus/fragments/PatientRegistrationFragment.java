package com.healthcoco.healthcocoplus.fragments;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocoplus.adapter.NotesListViewAdapter;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.bean.request.RegisterNewPatientRequest;
import com.healthcoco.healthcocoplus.bean.server.Address;
import com.healthcoco.healthcocoplus.bean.server.AlreadyRegisteredPatientsResponse;
import com.healthcoco.healthcocoplus.bean.server.BloodGroup;
import com.healthcoco.healthcocoplus.bean.server.CityResponse;
import com.healthcoco.healthcocoplus.bean.server.FileDetails;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.Profession;
import com.healthcoco.healthcocoplus.bean.server.Reference;
import com.healthcoco.healthcocoplus.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.bean.server.UserGroups;
import com.healthcoco.healthcocoplus.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocoplus.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocoplus.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocoplus.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocoplus.enums.BooleanTypeValues;
import com.healthcoco.healthcocoplus.enums.CommonListDialogType;
import com.healthcoco.healthcocoplus.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocoplus.enums.DialogType;
import com.healthcoco.healthcocoplus.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocoplus.enums.OptionsType;
import com.healthcoco.healthcocoplus.enums.PatientProfileScreenType;
import com.healthcoco.healthcocoplus.enums.RecordType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocoplus.listeners.CommonOptionsDialogItemClickListener;
import com.healthcoco.healthcocoplus.listeners.DownloadFileFromUrlListener;
import com.healthcoco.healthcocoplus.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocoplus.listeners.NotesItemClickListener;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocoplus.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocoplus.utilities.BitmapUtil;
import com.healthcoco.healthcocoplus.utilities.DateTimeUtil;
import com.healthcoco.healthcocoplus.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.ImageUtil;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.views.CustomAutoCompleteTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Shreshtha on 03-03-2017.
 */
public class PatientRegistrationFragment extends HealthCocoFragment implements View.OnClickListener, CommonListDialogItemClickListener,
        GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised,
        CommonOptionsDialogItemClickListener, DownloadFileFromUrlListener, NotesItemClickListener {
    private ArrayList<Object> BLOOD_GROUPS = new ArrayList<Object>() {{
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
    private TextView tvGroup;
    private CustomAutoCompleteTextView autotvBloodGroup;
    private EditText editLocality;
    private EditText editSecondaryMobile;
    private EditText editStreetAddress;
    private AutoCompleteTextView autotvCountry;
    private EditText editPincode;
    private TextView tvMobileNumber;
    private User user;
    private String mobileNumber;
    private ArrayList<String> groupIdsList;
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
    private ArrayList<String> notesListLastAdded;
    private TextView tvNotes;
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
    private ArrayList<String> notesList = new ArrayList<String>();
    private NotesListViewAdapter notesListViewAdapter;


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
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
    }

    @Override
    public void init() {
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            initViews();
            initListeners();
            initAutoTvAdapter(autotvBloodGroup, AutoCompleteTextViewType.BLOOD_GROUP, BLOOD_GROUPS);
            initAutoTvAdapter(autotvCountry, AutoCompleteTextViewType.COUNTRY, (ArrayList<Object>) (ArrayList<?>) new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.array_countries))));
            initNoteListAdapter();
            initDefaultData();
            initData();
        }
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
        gvGroups = (GridView) view.findViewById(R.id.gv_groups);
    }

    @Override
    public void initListeners() {
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
        List<UserGroups> groupsList = null;
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
        ArrayList<String> notesList = new ArrayList<>();
        if (patientDetails instanceof RegisteredPatientDetailsUpdated) {
            RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated = (RegisteredPatientDetailsUpdated) patientDetails;
            imageUrl = Util.getValidatedValue(registeredPatientDetailsUpdated.getImageUrl());
            name = Util.getValidatedValue(registeredPatientDetailsUpdated.getLocalPatientName());
            mobileNumber = Util.getValidatedValue(registeredPatientDetailsUpdated.getMobileNumber());
            gender = Util.getValidatedValue(registeredPatientDetailsUpdated.getGender());
            birthday = Util.getDOB(registeredPatientDetailsUpdated.getDob());
            groupsList = registeredPatientDetailsUpdated.getGroups();
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
                notesList = registeredPatientDetailsUpdated.getPatient().getNotes();
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

//        tvGroup.setText(getGroupNames(groupsList));
//        tvNotes.setText(getNotesName(notesList));
        editAadharId.setText(aadharId);
        editDrivingLicence.setText(drivingLicense);
        editPanCardNumber.setText(panNumber);
    }

    private String getGroupNames(List<UserGroups> groupsList) {
        String groupNamesText = "";
        if (groupIdsList == null)
            groupIdsList = new ArrayList<>();
        if (!Util.isNullOrEmptyList(groupsList)) {
            for (UserGroups group :
                    groupsList) {
                int index = groupsList.indexOf(group);
                if (index == groupsList.size() - 1)
                    groupNamesText = groupNamesText + group.getName();
                else
                    groupNamesText = groupNamesText + group.getName() + SEPARATOR_GROUP_NOTES;
                groupIdsList.add(group.getUniqueId());
            }
        } else
            groupIdsList.clear();
        return groupNamesText;
    }

    private String getNotesNames(List<String> notesList) {
        String note = "";
        if (groupIdsList == null)
            groupIdsList = new ArrayList<>();
        for (String string :
                notesList) {
            int index = notesList.indexOf(string);
            if (index == notesList.size() - 1)
                note = string;
            else
                note = string + SEPARATOR_GROUP_NOTES;
        }
        return note;
    }

    private String getNotesName(ArrayList<String> notesList) {
        String notesText = "";
        if (notesListLastAdded == null)
            notesListLastAdded = new ArrayList<>();
        notesListLastAdded.clear();
        if (!Util.isNullOrEmptyList(notesList)) {
            for (String note :
                    notesList) {
                int index = notesList.indexOf(note);
                if (index == notesList.size() - 1)
                    notesText = notesText + note;
                else
                    notesText = notesText + note + SEPARATOR_GROUP_NOTES;
                notesListLastAdded.add(note);
            }
        }
        return notesText;
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
//            case R.id.tv_city:
//                if (!Util.isNullOrEmptyList(citiesList))
//                    commonListDialog = openCommonListDialogFragment(this, CommonListDialogType.CITY, citiesList);
//                else
//                    getCitiesList(true);
////                    Util.showToast(mActivity, R.string.no_cities_found);
//                break;
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
                openAddUpdateNameDialogFragment(WebServiceType.LOCAL_STRING_SAVE, AddUpdateNameDialogType.LOCAL_STRING_SAVE, this, null, "", HealthCocoConstants.REQUEST_CODE_STRINGS_LIST);
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

    private void clearPreviousAlerts() {
        editName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    private void validateData() {
        clearPreviousAlerts();
        String msg = null;
        EditText selectedEditText = null;
        String name = Util.getValidatedValueOrNull(editName);
        String email = Util.getValidatedValueOrNull(editEmail);
        String aadharId = Util.getValidatedValueOrNull(editAadharId);
        String secondaryMobileNumber = Util.getValidatedValueOrNull(editSecondaryMobile);
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
        }
        if (Util.isNullOrBlank(msg)) {
            registerPatient(name);
        } else {
            Util.showErrorOnEditText(selectedEditText);
            Util.showAlert(mActivity, msg);
        }
    }

    private void registerPatient(String name) {
//        mActivity.showLoading(false);
        RegisterNewPatientRequest patientDetails = new RegisterNewPatientRequest();
        patientDetails.setLocalPatientName(name);
        patientDetails.setMobileNumber(String.valueOf(tvMobileNumber.getText()));
        patientDetails.setDoctorId(user.getUniqueId());
        patientDetails.setLocationId(user.getForeignLocationId());
        patientDetails.setHospitalId(user.getForeignHospitalId());
        patientDetails.setDob(DateTimeUtil.getDob(String.valueOf(tvBirthDay.getText())));
        patientDetails.setEmailAddress(Util.getValidatedValueOrNull(String.valueOf(editEmail.getText())));
        patientDetails.setNotes(notesListLastAdded);
        patientDetails.setGroups(groupIdsList);
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
//            patientDetails.setUserId(selectedPatient.getUserId());
//            WebDataServiceImpl.getInstance(mApp).updatePatient(RegisteredPatientDetailsUpdated.class, patientDetails, this, this);

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
//            switch (volleyResponseBean.getWebServiceType()) {
//                case GET_REFERENCE:
//                    isReferenceLoaded = true;
//                    break;
//                case GET_PROFESSION:
//                    isProfessionLoaded = true;
//                    break;
//                case GET_CITIES:
//                    isCityLoaded = true;
//                    break;
//            }
//            if (isBloodGroupLoaded && isCityLoaded && isProfessionLoaded && isReferenceLoaded)
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
                    refreshContactsData(patientDetails);
                    mActivity.hideLoading();
                    //will directly refresh PatientDetailSCreen on its onActivityResult
                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_REGISTRATION);
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
//                    if (!response.isFromLocalAfterApiSuccess()) {
//                        mActivity.syncReferenceList(user, this, this);
//                        return;
//                    }
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

//                LogUtils.LOGD(TAG, "Success onResponse citiesList Size " + citiesList.size() + " isDataFromLocal " + response.isDataFromLocal());
//                if (openCitiesListScreen)
//                    onClick(tvCity);
                break;
            case GET_PROFESSION:
                if (!Util.isNullOrEmptyList(response.getDataList())) {
                    initAutoTvAdapter(autotvProfession, AutoCompleteTextViewType.PROFESSION, response.getDataList());
                }
//                if (response.isDataFromLocal()) {
//                    professionsList = (ArrayList<Profession>) (ArrayList<?>) response
//                            .getDataList();
//                    if (!Util.isNullOrEmptyList(professionsList)) {
//                        LogUtils.LOGD(TAG, "Success onResponse professionsList Size " + professionsList.size() + " isDataFromLocal " + response.isDataFromLocal());
//                        initAutoTvAdapter(tvProfession, AutoCompleteTextViewType.PROFESSION, (ArrayList<Object>) (ArrayList<?>) professionsList);
//                    }
//                } else if (!Util.isNullOrEmptyList(response.getDataList())) {
//                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PROFESSION, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
//                    response.setIsFromLocalAfterApiSuccess(true);
//                    LogUtils.LOGD(TAG, "Success onResponse professionsList Size Total" + professionsList.size() + " isDataFromLocal " + response.isDataFromLocal());
//                    return;
//                }
//                if (openProfessionListScreen)
//                    onClick(tvProfession);
                break;
        }

//        if (isCityLoaded && isProfessionLoaded && isReferenceLoaded)
        mActivity.hideLoading();
    }

    private void refreshContactsData(RegisteredPatientDetailsUpdated patientDetails) {
        LocalDataServiceImpl.getInstance(mApp).addPatient(patientDetails);
        mActivity.syncContacts(user);
        Util.sendBroadcast(mApp, ContactsListFragment.INTENT_GET_CONTACT_LIST_LOCAL);
    }

    private void openPatientDetailScreen(RegisteredPatientDetailsUpdated selecetdPatient) {
        if (selecetdPatient.getPatient() != null && !Util.isNullOrBlank(selecetdPatient.getPatient().getPatientId())) {
            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = selecetdPatient.getUserId();
//            Util.sendBroadcast(mApp, ContactsListFragment.INTENT_REGISTER_NEW_CONTACT);
            openCommonOpenUpActivity(CommonOpenUpFragmentType.PATIENT_DETAIL, null,
                    HealthCocoConstants.REQUEST_CODE_CONTACTS_DETAIL);
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
        } else if (requestCode == HealthCocoConstants.REQUEST_CODE_REGISTRATION) {
            LogUtils.LOGD(TAG, "Contacts List onActivityResult ");
            if (resultCode == HealthCocoConstants.RESULT_CODE_GROUPS_LIST && data != null) {
                String text = "";
                groupIdsList = data.getStringArrayListExtra(HealthCocoConstants.TAG_GROUP_IDS_LIST);
                if (!Util.isNullOrEmptyList(groupIdsList)) {
                    ArrayList<UserGroups> groupsList = new ArrayList<UserGroups>();
                    for (String groupId :
                            groupIdsList) {
                        UserGroups group = LocalDataServiceImpl.getInstance(mApp).getUserGroup(groupId);
                        if (group != null) {
                            groupsList.add(group);
                            int index = groupIdsList.indexOf(groupId);
                            if (index == groupIdsList.size() - 1)
                                text = text + group.getName();
                            else
                                text = text + group.getName() + SEPARATOR_GROUP_NOTES;
                        }
                    }
                }
                tvGroup.setText(text);
            }
        } else if (requestCode == HealthCocoConstants.REQUEST_CODE_STRINGS_LIST && data != null) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_STRING && data != null) {
                String note = (String) data.getSerializableExtra(HealthCocoConstants.TAG_INTENT_DATA);
                if (!Util.isNullOrBlank(note)) {
                    if (notesList == null)
                        notesList = new ArrayList<>();
                    notesList.add(note);
                    notifyAdapter(notesList);
                }
                mActivity.hideLoading();
            }
        }
    }

    private void notifyAdapter(List<String> list) {
        if (!Util.isNullOrEmptyList(list)) {
            lvNotes.setVisibility(View.VISIBLE);
        }
        notesListViewAdapter.setListData(list);
        notesListViewAdapter.notifyDataSetChanged();
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

//                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getReferenceListAll(WebServiceType.GET_REFERENCE, user.getUniqueId(), BooleanTypeValues.FALSE, null, null);
                break;
            case ADD_PROFESSION:
                LocalDataServiceImpl.getInstance(mApp).addProfessionsList((ArrayList<Profession>) (ArrayList<?>) response.getDataList());
            case GET_PROFESSION_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getProfessionList(WebServiceType.GET_PROFESSION, null, null);
                break;
        }
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
                if (notesList.contains(notes)) {
                    notesList.remove(notes);
                    notifyAdapter(notesList);
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

}
