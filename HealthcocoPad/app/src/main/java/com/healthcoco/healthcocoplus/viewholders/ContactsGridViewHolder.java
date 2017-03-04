package com.healthcoco.healthcocoplus.viewholders;

import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocoplus.enums.PatientProfileScreenType;
import com.healthcoco.healthcocoplus.listeners.ContactsItemOptionsListener;
import com.healthcoco.healthcocoplus.listeners.ImageLoadedListener;
import com.healthcoco.healthcocoplus.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ContactsGridViewHolder implements OnClickListener, ImageLoadedListener {
    private final String TAG = ContactsGridViewHolder.class.getSimpleName();
    private final ImageLoader imageLoader;
    private HealthCocoActivity mActivity;
    private View convertView;
    private ImageButton btAddToGroup;
    private int position;
    private ContactsItemOptionsListener optionsListener;
    private RegisteredPatientDetailsUpdated objData;
    private TextView tvContactName;
    private TextView tvContactNumber;
    private ImageView ivContactProfile;
    private ImageButton btMail;
    private ImageButton btCall;
    public TextView tvHeaderView;
    private TextView tvInitialAlphabet;
    private LinearLayout containerTop;
    private TextView tvCreatedTime;
    private TextView tvPatientId;
    private ImageButton btEdit;
    private ImageButton btQueue;
    private ImageButton btGroup;
    private ImageButton btPrescription;

    public ContactsGridViewHolder(HealthCocoActivity mActivity, ContactsItemOptionsListener optionsListener, int position) {
        this.mActivity = mActivity;
        this.optionsListener = optionsListener;
        this.position = position;
        imageLoader = ImageLoader.getInstance();
    }

    public void setData(Object data) {
        this.objData = (RegisteredPatientDetailsUpdated) data;
    }

    public void applyData() {
        LogUtils.LOGD(TAG, "Unique Id " + objData.getUniqueId());
        tvContactName.setText(Util.getValidatedValue(objData.getLocalPatientName()));
        tvContactNumber.setText(Util.getValidatedValue(objData.getMobileNumber()));
        tvPatientId.setText(Util.getValidatedValue(objData.getPid()));
        DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENTS_LIST, objData, null, ivContactProfile, tvInitialAlphabet);
    }

    public View getConvertView() {
        convertView = mActivity.getLayoutInflater().inflate(R.layout.grid_item_contacts, null);
        tvContactName = (TextView) convertView.findViewById(R.id.tv_contact_name);
        tvContactNumber = (TextView) convertView.findViewById(R.id.tv_contact_number);
        tvPatientId = (TextView) convertView.findViewById(R.id.tv_patient_id);
        btEdit = (ImageButton) convertView.findViewById(R.id.bt_edit);
        btQueue = (ImageButton) convertView.findViewById(R.id.bt_queue);
        btCall = (ImageButton) convertView.findViewById(R.id.bt_call);
        btGroup = (ImageButton) convertView.findViewById(R.id.bt_call);
        btPrescription = (ImageButton) convertView.findViewById(R.id.bt_call);
        tvInitialAlphabet = (TextView) convertView.findViewById(R.id.tv_initial_aplhabet);
        ivContactProfile = (ImageView) convertView.findViewById(R.id.iv_image);
        containerTop = (LinearLayout) convertView.findViewById(R.id.container_top);

        btEdit.setOnClickListener(this);
        btQueue.setOnClickListener(this);
        btCall.setOnClickListener(this);
        btGroup.setOnClickListener(this);
        btPrescription.setOnClickListener(this);
        containerTop.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_edit:
                optionsListener.onEditClicked(objData);
                break;
            case R.id.bt_queue:
//                optionsListener.onCallClicked(objData);
                break;
            case R.id.bt_call:
                optionsListener.onCallClicked(objData);
                break;
            case R.id.bt_group:
//                optionsListener.onAddPrescriptionClicked(objData);
                break;
            case R.id.bt_prescription:
                optionsListener.onAddPrescriptionClicked(objData);
                break;
            case R.id.container_top:
                optionsListener.onItemContactDetailClicked(objData);
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
}