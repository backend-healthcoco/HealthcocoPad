package com.healthcoco.healthcocopad.dialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.MenuClinicListAdapter;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.listeners.SelectedClinicListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.List;

import static com.healthcoco.healthcocopad.fragments.MenuDrawerFragment.SELECTED_LOCATION_ID;

/**
 * Created by Shreshtha on 01-02-2017.
 */
public class MenuClinicListDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, SelectedClinicListener {
    public static final String TAG_CLINIC_NAME = "clinicName";
    private List<DoctorClinicProfile> clinicProfile;
    private ListView lvClinics;
    private MenuClinicListAdapter menuClinicListAdapter;
    private ImageButton btCancel;

    public MenuClinicListDialogFragment() {

    }

    public MenuClinicListDialogFragment(List<DoctorClinicProfile> clinicProfile) {
        this.clinicProfile = clinicProfile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_clinic_profile_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
//        setWidthHeight(0.75,0.50);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initData();
    }

    @Override
    public void initViews() {
        lvClinics = (ListView) view.findViewById(R.id.lv_clinics);
        btCancel = (ImageButton) view.findViewById(R.id.bt_cancel);
    }

    @Override
    public void initListeners() {
        btCancel.setOnClickListener(this);
    }

    @Override
    public void initData() {
        initClinicListAdapter();
    }

    private void initClinicListAdapter() {
        menuClinicListAdapter = new MenuClinicListAdapter(mActivity, clinicProfile,this);
        lvClinics.setAdapter(menuClinicListAdapter);
    }

    private void refreshSelectedDoctorClinicProfileDetails(DoctorClinicProfile doctorClinicProfile) {
        SELECTED_LOCATION_ID = doctorClinicProfile.getLocationId();
        if (!Util.isNullOrBlank(SELECTED_LOCATION_ID)) {
            LocalDataServiceImpl.getInstance(mApp).updatedSelectedLocationDetails(doctorClinicProfile.getDoctorId(), doctorClinicProfile);
        }
    }

    @Override
    public void onClick(View view) {
        getDialog().cancel();
    }

    @Override
    public void onSelectedClinicCheckClicked(DoctorClinicProfile doctorClinicProfile) {
        refreshSelectedDoctorClinicProfileDetails(doctorClinicProfile);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,null);
        getDialog().dismiss();
    }
}
