package com.healthcoco.healthcocopad.viewholders;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.listeners.AutoCompleteTextViewListener;
import com.healthcoco.healthcocopad.listeners.ProfessipnalMembershipDetailItemListener;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 21-02-2017.
 */

public class ProfessionalMembershipListViewHolder extends LinearLayout implements View.OnClickListener, AdapterView.OnItemClickListener, AutoCompleteTextViewListener {
    private HealthCocoActivity mActivity;
    private HealthCocoApplication mApp;
    private ImageButton btDelete;
    private TextView tvProfessionalMembership;
    private String objData;
    private ProfessipnalMembershipDetailItemListener membershipDetailItemListener;

    public ProfessionalMembershipListViewHolder(Context context) {
        super(context);
        init(context);
    }

    public ProfessionalMembershipListViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProfessionalMembershipListViewHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mActivity = (HealthCocoActivity) context;
        this.mApp = (HealthCocoApplication) mActivity.getApplicationContext();
        inflate(context, R.layout.list_item_professional_membership_detail, this);
        EditTextTextViewErrorUtil.setUpUI(mActivity, this);
        initViews();
        initListeners();
    }


    private void initViews() {
        btDelete = (ImageButton) findViewById(R.id.bt_delete);
        tvProfessionalMembership = (TextView) findViewById(R.id.tv_professional_membership);
    }

    private void initListeners() {
        btDelete.setOnClickListener(this);
        tvProfessionalMembership.setOnClickListener(this);
    }

    public void setData(String professionalMemberships, ProfessipnalMembershipDetailItemListener itemClickListener, int position) {
        this.membershipDetailItemListener = itemClickListener;
        this.objData = professionalMemberships;
        if (objData != null) {
            tvProfessionalMembership.setText(Util.getValidatedValue(objData));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_delete:
                membershipDetailItemListener.onDeleteProfessionalMembershipDetailClicked(this, objData);
                break;
            case R.id.tv_professional_membership:
                membershipDetailItemListener.onProfessionalMembershipClicked(tvProfessionalMembership, objData);
                break;
        }
    }

    private void clearPreviousAlerts() {
        tvProfessionalMembership.setActivated(false);
    }

    public Object getErrorMessageOrTrueIfValidated(boolean isOnSaveClick) {
        clearPreviousAlerts();
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        String professionalMembership = Util.getValidatedValueOrNull(tvProfessionalMembership);
        if (isOnSaveClick && Util.isNullOrBlank(professionalMembership)) {
            return true;
        } else if (Util.isNullOrBlank(professionalMembership)) {
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
            errorViewList.add(tvProfessionalMembership);
        }
        if (Util.isNullOrBlank(msg)) {
            membershipDetailItemListener.addProfessionalMembershipDetailToList(professionalMembership);
            return true;
        }
        EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, errorViewList, msg);
        return msg;
    }

//    private ForeignProfessionalMemberships getProfessionalMembership() {
//        ForeignProfessionalMemberships foreignProfessionalMemberships = null;
//        String professionalMembership = Util.getValidatedValueOrNull(tvProfessionalMembership);
//        if (!Util.isNullOrBlank(professionalMembership)) {
//            foreignProfessionalMemberships = new ForeignProfessionalMemberships();
//            foreignProfessionalMemberships.setProfessionalMemberships(professionalMembership);
//        }
//        return foreignProfessionalMemberships;
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onEmptyListFound(AutoCompleteTextView autoCompleteTextView) {

    }

    @Override
    public void scrollToPosition(int position) {

    }
}