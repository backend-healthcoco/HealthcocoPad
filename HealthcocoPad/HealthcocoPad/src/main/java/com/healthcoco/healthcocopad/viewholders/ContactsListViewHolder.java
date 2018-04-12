package com.healthcoco.healthcocopad.viewholders;

import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.healthcoco.healthcocopad.utilities.ScreenDimensions;
import com.healthcoco.healthcocopad.utilities.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ContactsListViewHolder extends HealthCocoViewHolder implements OnClickListener, ImageLoadedListener {
    private final String TAG = ContactsListViewHolder.class.getSimpleName();
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
    private ImageButton btPrescription;
    private ImageButton btDiscard;
    private ImageButton btCall;
    private TextView tvInitialAlphabet;
    private LinearLayout containerTop;
    private boolean mobileNumberOptional;
    private LinearLayout layoutDiscarded;


    public ContactsListViewHolder(HealthCocoActivity mActivity, ContactsItemOptionsListener optionsListener, int position) {
        this.mActivity = mActivity;
        this.optionsListener = optionsListener;
        this.position = position;
        imageLoader = ImageLoader.getInstance();
        mobileNumberOptional = optionsListener.isMobileNumberOptional();
    }

    @Override
    public void setData(Object data) {
        this.objData = (RegisteredPatientDetailsUpdated) data;
    }

    @Override
    public void applyData() {
        LogUtils.LOGD(TAG, "Unique Id " + objData.getUniqueId());
//        scrollViewContactsItems.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        tvContactName.setText(Util.getValidatedValue(objData.getLocalPatientName()));
        tvContactNumber.setText(Util.getValidatedValue(objData.getMobileNumber()));
        DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENTS_LIST, objData, null, ivContactProfile, tvInitialAlphabet);
//        checkIsDiscarded(objData.isPatientDiscarded());
    }

    @Override
    public View getContentView() {
        convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_contacts, null);
//        scrollViewContactsItems = (ContactsListItemHorizontalScrollView) covertView.findViewById(R.id.scrollview_contacts_item);
        containerTop = (LinearLayout) convertView.findViewById(R.id.container_top);
        containerTop.setLayoutParams(new LinearLayout.LayoutParams(ScreenDimensions.SCREEN_WIDTH, LinearLayout.LayoutParams.MATCH_PARENT));
        tvContactName = (TextView) convertView.findViewById(R.id.tv_contact_name);
        tvContactNumber = (TextView) convertView.findViewById(R.id.tv_contact_number);
        btCall = (ImageButton) convertView.findViewById(R.id.bt_call);
        btDiscard = (ImageButton) convertView.findViewById(R.id.bt_discard);
        btPrescription = (ImageButton) convertView.findViewById(R.id.bt_prescription);
        btAddToGroup = (ImageButton) convertView.findViewById(R.id.bt_group);
        tvInitialAlphabet = (TextView) convertView.findViewById(R.id.tv_initial_aplhabet);
        ivContactProfile = (ImageView) convertView.findViewById(R.id.iv_image);
        layoutDiscarded = (LinearLayout) convertView.findViewById(R.id.layout_cantact_discarded);

        btAddToGroup.setTag(position);
        btDiscard.setOnClickListener(this);
        btCall.setOnClickListener(this);
        btPrescription.setOnClickListener(this);
        btAddToGroup.setOnClickListener(this);
        containerTop.setOnClickListener(this);

//        if (mobileNumberOptional)
//            btDiscard.setVisibility(View.VISIBLE);

//        scrollViewContactsItems.setSwipeRefreshLayout(optionsListener.getSwipeRefreshLayout());
//        containerTop.setOnTouchListener(new ContactsListScollViewTouchListener(mActivity));

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_call:
                optionsListener.onCallClicked(objData);
                break;
            case R.id.bt_discard:
                optionsListener.onDiscardClicked(objData);
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