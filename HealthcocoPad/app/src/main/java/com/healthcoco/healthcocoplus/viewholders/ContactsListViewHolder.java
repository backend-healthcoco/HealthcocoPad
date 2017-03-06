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
import com.healthcoco.healthcocoplus.HealthCocoViewHolder;
import com.healthcoco.healthcocoplus.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocoplus.enums.PatientProfileScreenType;
import com.healthcoco.healthcocoplus.listeners.ContactsItemOptionsListener;
import com.healthcoco.healthcocoplus.listeners.ImageLoadedListener;
import com.healthcoco.healthcocoplus.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.ScreenDimensions;
import com.healthcoco.healthcocoplus.utilities.Util;
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
    private ImageButton btMail;
    private ImageButton btCall;
    private TextView tvInitialAlphabet;
    private LinearLayout containerTop;

    public ContactsListViewHolder(HealthCocoActivity mActivity, ContactsItemOptionsListener optionsListener, int position) {
        this.mActivity = mActivity;
        this.optionsListener = optionsListener;
        this.position = position;
        imageLoader = ImageLoader.getInstance();
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
    }

    @Override
    public View getContentView() {
        convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_contacts, null);
//        scrollViewContactsItems = (ContactsListItemHorizontalScrollView) convertView.findViewById(R.id.scrollview_contacts_item);
        containerTop = (LinearLayout) convertView.findViewById(R.id.container_top);
        containerTop.setLayoutParams(new LinearLayout.LayoutParams(ScreenDimensions.SCREEN_WIDTH, LinearLayout.LayoutParams.MATCH_PARENT));
        tvContactName = (TextView) convertView.findViewById(R.id.tv_contact_name);
        tvContactNumber = (TextView) convertView.findViewById(R.id.tv_contact_number);
        btCall = (ImageButton) convertView.findViewById(R.id.bt_call);
        btMail = (ImageButton) convertView.findViewById(R.id.bt_mail);
        btAddToGroup = (ImageButton) convertView.findViewById(R.id.bt_group);
        tvInitialAlphabet = (TextView) convertView.findViewById(R.id.tv_initial_aplhabet);
        ivContactProfile = (ImageView) convertView.findViewById(R.id.iv_image);

        btAddToGroup.setTag(position);
        btCall.setOnClickListener(this);
        btMail.setOnClickListener(this);
        btAddToGroup.setOnClickListener(this);
        containerTop.setOnClickListener(this);
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

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        if (objData != null)
            objData.setProfileImageBitmap(bitmap);
    }
}