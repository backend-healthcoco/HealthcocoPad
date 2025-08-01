package com.healthcoco.healthcocopad.viewholders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsNew;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.ContactsListFragment;
import com.healthcoco.healthcocopad.fragments.DeletedPatientsFragment;
import com.healthcoco.healthcocopad.listeners.ContactsItemOptionsForDeletedListener;
import com.healthcoco.healthcocopad.listeners.ImageLoadedListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.Util;

public class ContactsListViewForDeletedPatientHolder extends HealthCocoViewHolder implements
        View.OnClickListener, ImageLoadedListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    private View convertView;
    private int position;
    private ContactsItemOptionsForDeletedListener onItemClickListener;
    private RegisteredPatientDetailsNew objData;
    private TextView tvContactName;
    private TextView tvContactNumber;
    private ImageView ivContactProfile;
    private TextView tvInitialAlphabet;
    private TextView btRestore;

    public ContactsListViewForDeletedPatientHolder(HealthCocoActivity mActivity, ContactsItemOptionsForDeletedListener onItemClickListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.onItemClickListener = onItemClickListener;
    }

//    @Override
//    public void initViews(View convertView) {
//        tvContactName = (TextView) convertView.findViewById(R.id.tv_contact_name);
//        tvContactNumber = (TextView) convertView.findViewById(R.id.tv_contact_number);
//        btRestore = (TextView) convertView.findViewById(R.id.bt_restore);
//        tvInitialAlphabet = (TextView) convertView.findViewById(R.id.tv_initial_aplhabet);
//        ivContactProfile = (ImageView) convertView.findViewById(R.id.iv_image);
//        btRestore.setOnClickListener(this);
//    }
//
//    @Override
//    public void applyData(Object object) {
//        this.objData = (RegisteredPatientDetailsNew) object;
//        if (objData != null) {
//            tvContactName.setText(Util.getValidatedValue(objData.getLocalPatientName()));
//            tvContactNumber.setText(Util.getValidatedValue(objData.getMobileNumber()));
//            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENTS_LIST, objData, null, ivContactProfile, tvInitialAlphabet);
//            if (!objData.getDoctorId().equals(onItemClickListener.getUser().getUniqueId())) {
//
//            }
//        }
//    }


    private void showDeletePatientConfirmationAlert() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm);
        alertBuilder.setMessage(mActivity.getResources().getString(
                R.string.confirm_undo_delete_patient) + objData.getLocalPatientName());
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePatient();
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

    private void deletePatient() {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).deletePatient(Boolean.class, WebServiceType.DELETE_PATIENT,
                onItemClickListener.getUser().getUniqueId(), onItemClickListener.getUser().getForeignLocationId(),
                onItemClickListener.getUser().getForeignHospitalId(), objData.getUserId(), false, this, this);
    }

    @Override
    public void setData(Object object) {
        this.objData = (RegisteredPatientDetailsNew) object;
    }

    @Override
    public void applyData() {
        if (objData != null) {
            tvContactName.setText(Util.getValidatedValue(objData.getLocalPatientName()));
            tvContactNumber.setText(Util.getValidatedValue(objData.getMobileNumber()));
            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENTS_LIST, objData, null, ivContactProfile, tvInitialAlphabet);
            if (!objData.getDoctorId().equals(onItemClickListener.getUser().getUniqueId())) {

            }
        }
    }

    @Override
    public View getContentView() {
        LinearLayout view = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.grid_item_deleted_contacts, null);
        tvContactName = (TextView) view.findViewById(R.id.tv_contact_name);
        tvContactNumber = (TextView) view.findViewById(R.id.tv_contact_number);
        btRestore = (TextView) view.findViewById(R.id.bt_restore);
        tvInitialAlphabet = (TextView) view.findViewById(R.id.tv_initial_aplhabet);
        ivContactProfile = (ImageView) view.findViewById(R.id.iv_image);
        btRestore.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_restore) {
            if (objData.getDoctorId().equals(onItemClickListener.getUser().getUniqueId())) {
                if (!Util.isNullOrBlank(objData.getUserId()))
                    showDeletePatientConfirmationAlert();
                else
                    Util.showToast(mActivity, R.string.no_mobile_number_found);
            }
        }
// no else needed for default
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        if (objData != null)
            objData.setProfileImageBitmap(bitmap);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case DELETE_PATIENT:
                    if (response.getData() instanceof Boolean) {
                        boolean isDataSuccess = (boolean) response.getData();
                        if (isDataSuccess) {
                            objData.setPatientDiscarded(false);
                            LocalDataServiceImpl.getInstance(mApp).addPatientNew(objData);
                            Util.sendBroadcast(mApp, ContactsListFragment.INTENT_GET_CONTACT_LIST_LOCAL);
                            Util.sendBroadcast(mApp, DeletedPatientsFragment.INTENT_REFRESH_CONTACTS_LIST_FROM_LOCAL);
                        } else Util.showToast(mActivity, R.string.patient_not_updated);
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }
}