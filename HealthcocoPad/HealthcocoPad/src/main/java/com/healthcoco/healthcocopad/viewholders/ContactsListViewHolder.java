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
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsNew;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.enums.ContactListItemOptionType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.listeners.ContactsItemOptionsListener;
import com.healthcoco.healthcocopad.listeners.ImageLoadedListener;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ScreenDimensions;
import com.healthcoco.healthcocopad.utilities.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ContactsListViewHolder extends HealthCocoViewHolder implements OnClickListener, ImageLoadedListener, PopupWindowListener {
    private final String TAG = ContactsListViewHolder.class.getSimpleName();
    private final ImageLoader imageLoader;
    private HealthCocoActivity mActivity;
    private View convertView;
    private ImageButton btAddToGroup;
    private int position;
    private ContactsItemOptionsListener optionsListener;
    private RegisteredPatientDetailsNew objData;
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
    private ImageButton btOption;


    public ContactsListViewHolder(HealthCocoActivity mActivity, ContactsItemOptionsListener optionsListener, int position) {
        this.mActivity = mActivity;
        this.optionsListener = optionsListener;
        this.position = position;
        imageLoader = ImageLoader.getInstance();
        mobileNumberOptional = optionsListener.isMobileNumberOptional();
    }

    @Override
    public void setData(Object data) {
        this.objData = (RegisteredPatientDetailsNew) data;
    }

    @Override
    public void applyData() {
        LogUtils.LOGD(TAG, "Unique Id " + objData.getUniqueId());
//        scrollViewContactsItems.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        tvContactName.setText(Util.getValidatedValue(objData.getLocalPatientName()));
        tvContactNumber.setText(Util.getValidatedValue(objData.getMobileNumber()));
        DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENTS_LIST, objData, null, ivContactProfile, tvInitialAlphabet);
//        checkIsDiscarded(objData.isPatientDiscarded());
        if (!objData.getDoctorId().equals(optionsListener.getUser().getUniqueId())) {
            btOption.setVisibility(View.GONE);
        } else btOption.setVisibility(View.VISIBLE);
    }

    @Override
    public View getContentView() {
        convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_contacts, null);
//        scrollViewContactsItems = (ContactsListItemHorizontalScrollView) covertView.findViewById(R.id.scrollview_contacts_item);
        containerTop = (LinearLayout) convertView.findViewById(R.id.container_top);
        containerTop.setLayoutParams(new LinearLayout.LayoutParams(ScreenDimensions.SCREEN_WIDTH, LinearLayout.LayoutParams.MATCH_PARENT));
        tvContactName = (TextView) convertView.findViewById(R.id.tv_contact_name);
        tvContactNumber = (TextView) convertView.findViewById(R.id.tv_contact_number);
        btOption = (ImageButton) convertView.findViewById(R.id.bt_option);
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
        mActivity.initPopupWindows(btOption, PopupWindowType.CONTACT_LIST_OPTION_TYPE,
                PopupWindowType.CONTACT_LIST_OPTION_TYPE.getList(),R.layout.layout_popup_dialog_item_with_icon, this);

//        if (mobileNumberOptional)
//            btDiscard.setVisibility(View.VISIBLE);

//        scrollViewContactsItems.setSwipeRefreshLayout(optionsListener.getSwipeRefreshLayout());
//        containerTop.setOnTouchListener(new ContactsListScollViewTouchListener(mActivity));

        return convertView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.bt_call) {
            optionsListener.onCallClicked(objData);

        } else if (id == R.id.bt_discard) {
            optionsListener.onDiscardClicked(objData);

        } else if (id == R.id.bt_group) {
            optionsListener.onAddToGroupClicked(objData);

        } else if (id == R.id.bt_prescription) {
            optionsListener.onAddPrescriptionClicked(objData);

        } else if (id == R.id.container_top) {
            optionsListener.onItemContactDetailClicked(objData);

// default: do nothing
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

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        switch (popupWindowType) {
            case CONTACT_LIST_OPTION_TYPE:
                if (object != null && object instanceof ContactListItemOptionType) {
                    ContactListItemOptionType listItemOptionType = (ContactListItemOptionType) object;
                    switch (listItemOptionType) {
                        case DELETE_PATIENT:
                            if (objData.getDoctorId().equals(optionsListener.getUser().getUniqueId())) {
                                if (!Util.isNullOrBlank(objData.getUserId()))
                                    optionsListener.onDeletePatientClicked(objData);
                                else
                                    Util.showToast(mActivity, R.string.no_mobile_number_found);
                            }
                            break;
                        case EDIT_MOBILE_NUMBER:
                            optionsListener.onEditPatientNumberClicked(objData);
                            break;
                    }
                }    break;
        }
    }

    @Override
    public void onEmptyListFound() {

    }
}