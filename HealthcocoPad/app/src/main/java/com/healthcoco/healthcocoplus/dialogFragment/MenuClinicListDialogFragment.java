package com.healthcoco.healthcocoplus.dialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoDialogFragment;
import com.healthcoco.healthcocoplus.adapter.MenuClinicListAdapter;
import com.healthcoco.healthcocoplus.bean.server.DoctorClinicProfile;

import java.util.List;

/**
 * Created by Shreshtha on 01-02-2017.
 */
public class MenuClinicListDialogFragment extends HealthCocoDialogFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private List<DoctorClinicProfile> clinicProfile;
    private ListView lvClinics;
    private MenuClinicListAdapter menuClinicListAdapter;
    private ImageButton btCancel;

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
        lvClinics.setOnItemClickListener(this);
        btCancel.setOnClickListener(this);
    }

    @Override
    public void initData() {
        initClinicListAdapter();
    }

    private void initClinicListAdapter() {
        menuClinicListAdapter = new MenuClinicListAdapter(mActivity, clinicProfile);
        lvClinics.setAdapter(menuClinicListAdapter);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onClick(View view) {
        getDialog().cancel();
    }

}
