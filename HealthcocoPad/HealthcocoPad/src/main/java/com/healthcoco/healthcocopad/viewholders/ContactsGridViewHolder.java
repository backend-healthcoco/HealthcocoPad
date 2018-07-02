package com.healthcoco.healthcocopad.viewholders;

import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.listeners.ContactsItemOptionsListener;
import com.healthcoco.healthcocopad.listeners.ImageLoadedListener;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ContactsGridViewHolder extends HealthCocoViewHolder implements OnClickListener, ImageLoadedListener {
    private final String TAG = ContactsGridViewHolder.class.getSimpleName();
    private final ImageLoader imageLoader;
    public TextView tvHeaderView;
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
    private TextView tvInitialAlphabet;
    private LinearLayout containerTop;
    private RelativeLayout containerBottom;
    private TextView tvCreatedTime;
    private TextView tvPatientId;
    private ImageButton btEdit;
    private ImageButton btDiscard;
    private ImageButton btQueue;
    private ImageButton btGroup;
    private ImageButton btPrescription;
    private TextView tvGenderDate;
    private TextView tvPatientAge;
    private boolean mobileNumberOptional;
    private Boolean pidHasDate;
    private LinearLayout layoutDiscarded;


    public ContactsGridViewHolder(HealthCocoActivity mActivity, ContactsItemOptionsListener optionsListener, int position) {
        this.mActivity = mActivity;
        this.optionsListener = optionsListener;
        this.position = position;
        imageLoader = ImageLoader.getInstance();
        mobileNumberOptional = optionsListener.isMobileNumberOptional();
        pidHasDate = optionsListener.isPidHasDate();
    }

    @Override
    public void setData(Object data) {
        this.objData = (RegisteredPatientDetailsUpdated) data;
    }

    @Override
    public void applyData() {
        LogUtils.LOGD(TAG, "Unique Id " + objData.getUniqueId());
        tvContactName.setText(Util.getValidatedValue(objData.getLocalPatientName()));
        tvContactNumber.setText(Util.getValidatedValue(objData.getMobileNumber()));

        if (!pidHasDate && (!Util.isNullOrBlank(objData.getPnum())))
            tvPatientId.setText(Util.getValidatedValue(objData.getPnum()));
        else
            tvPatientId.setText(Util.getValidatedValue(objData.getPid()));

        String formattedGenderAge = Util.getFormattedGenderAge(objData);
        if (!Util.isNullOrBlank(formattedGenderAge)) {
            tvGenderDate.setVisibility(View.VISIBLE);
            tvGenderDate.setText(formattedGenderAge);
        } else {
            tvGenderDate.setVisibility(View.GONE);
            tvGenderDate.setText("");
        }
        DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENTS_LIST, objData, null, ivContactProfile, tvInitialAlphabet);
//        checkIsDiscarded(objData.isPatientDiscarded());
    }


    @Override
    public View getContentView() {
        convertView = mActivity.getLayoutInflater().inflate(R.layout.grid_item_contacts, null);
        tvContactName = (TextView) convertView.findViewById(R.id.tv_contact_name);
        tvContactNumber = (TextView) convertView.findViewById(R.id.tv_contact_number);
        tvPatientId = (TextView) convertView.findViewById(R.id.tv_patient_id);
        tvGenderDate = (TextView) convertView.findViewById(R.id.tv_patient_gender);
        btEdit = (ImageButton) convertView.findViewById(R.id.bt_edit);
        btDiscard = (ImageButton) convertView.findViewById(R.id.bt_discard);
        btQueue = (ImageButton) convertView.findViewById(R.id.bt_queue);
        btCall = (ImageButton) convertView.findViewById(R.id.bt_call);
        btGroup = (ImageButton) convertView.findViewById(R.id.bt_group);
        btPrescription = (ImageButton) convertView.findViewById(R.id.bt_prescription);
        tvInitialAlphabet = (TextView) convertView.findViewById(R.id.tv_initial_aplhabet);
        ivContactProfile = (ImageView) convertView.findViewById(R.id.iv_image);
        containerTop = (LinearLayout) convertView.findViewById(R.id.container_top);
        containerBottom = (RelativeLayout) convertView.findViewById(R.id.container_call_rx);
        layoutDiscarded = (LinearLayout) convertView.findViewById(R.id.layout_cantact_discarded);

        btEdit.setOnClickListener(this);
        btDiscard.setOnClickListener(this);
        btQueue.setOnClickListener(this);
        btCall.setOnClickListener(this);
        btGroup.setOnClickListener(this);
        btPrescription.setOnClickListener(this);
        containerTop.setOnClickListener(this);

        if (!optionsListener.isInHomeActivity())
            containerBottom.setVisibility(View.GONE);
//        if (mobileNumberOptional)
//            btDiscard.setVisibility(View.VISIBLE);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_edit:
                optionsListener.onEditClicked(objData);
                break;
            case R.id.bt_discard:
                optionsListener.onDiscardClicked(objData);
                break;
            case R.id.bt_queue:
                optionsListener.onQueueClicked(objData);
                break;
            case R.id.bt_call:
                optionsListener.onCallClicked(objData);
                break;
            case R.id.bt_group:
                optionsListener.onAddToGroupClicked(objData);
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

    private void checkIsDiscarded(boolean isDiscarded) {
        if (isDiscarded)
            layoutDiscarded.setVisibility(View.VISIBLE);
        else layoutDiscarded.setVisibility(View.GONE);
    }


    @Override
    public void onImageLoaded(Bitmap bitmap) {
        if (objData != null)
            objData.setProfileImageBitmap(bitmap);
    }
}