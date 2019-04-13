package com.healthcoco.healthcocopad.viewholders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.ContactsListFragment;
import com.healthcoco.healthcocopad.fragments.DeletedPatientsFragment;
import com.healthcoco.healthcocopad.listeners.ContactsItemOptionsForDeletedListener;
import com.healthcoco.healthcocopad.listeners.ImageLoadedListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ContactsListViewForDeletedPatientHolder extends HealthcocoComonRecylcerViewHolder implements
        View.OnClickListener, ImageLoadedListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    private final ImageLoader imageLoader;
    private View convertView;
    private int position;
    private ContactsItemOptionsForDeletedListener optionsListener;
    private RegisteredPatientDetailsUpdated objData;
    private TextView tvContactName;
    private TextView tvContactNumber;
    private ImageView ivContactProfile;
    private TextView tvInitialAlphabet;
    private TextView btRestore;

    public ContactsListViewForDeletedPatientHolder(HealthCocoActivity mActivity, View convertView, HealthcocoRecyclerViewItemClickListener onItemClickListener, Object listenerObject, int position) {
        super(mActivity, convertView, onItemClickListener, listenerObject);
        this.optionsListener = (ContactsItemOptionsForDeletedListener) listenerObject;
        this.position = position;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public void initViews(View convertView) {
        tvContactName = (TextView) convertView.findViewById(R.id.tv_contact_name);
        tvContactNumber = (TextView) convertView.findViewById(R.id.tv_contact_number);
        btRestore = (TextView) convertView.findViewById(R.id.bt_restore);
        tvInitialAlphabet = (TextView) convertView.findViewById(R.id.tv_initial_aplhabet);
        ivContactProfile = (ImageView) convertView.findViewById(R.id.iv_image);
        btRestore.setOnClickListener(this);
    }

    @Override
    public void applyData(Object object) {
        this.objData = (RegisteredPatientDetailsUpdated) object;
        if (objData != null) {
            tvContactName.setText(Util.getValidatedValue(objData.getLocalPatientName()));
            tvContactNumber.setText(Util.getValidatedValue(objData.getMobileNumber()));
            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENTS_LIST, objData, null, ivContactProfile, tvInitialAlphabet);
            if (!objData.getDoctorId().equals(optionsListener.getUser().getUniqueId())) {

            }
        }
    }


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
                optionsListener.getUser().getUniqueId(), optionsListener.getUser().getForeignLocationId(),
                optionsListener.getUser().getForeignHospitalId(), objData.getUserId(), false, this, this);
    }

    @Override
    public View getContentView() {
        convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_contacts, null);
        tvContactName = (TextView) convertView.findViewById(R.id.tv_contact_name);
        tvContactNumber = (TextView) convertView.findViewById(R.id.tv_contact_number);
        tvInitialAlphabet = (TextView) convertView.findViewById(R.id.tv_initial_aplhabet);
        ivContactProfile = (ImageView) convertView.findViewById(R.id.iv_image);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_restore:
                if (objData.getDoctorId().equals(optionsListener.getUser().getUniqueId())) {
                    if (!Util.isNullOrBlank(objData.getUniqueId()))
                        showDeletePatientConfirmationAlert();
                    else
                        Util.showToast(mActivity, R.string.no_mobile_number_found);
                }
                break;
            default:
                break;
        }
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
                            LocalDataServiceImpl.getInstance(mApp).addPatient(objData);
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