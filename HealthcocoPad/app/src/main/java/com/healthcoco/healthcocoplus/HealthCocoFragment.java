package com.healthcoco.healthcocoplus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.dialogFragment.CommonListDialogFragment;
import com.healthcoco.healthcocoplus.dialogFragment.CommonOptionsDialogFragment;
import com.healthcoco.healthcocoplus.dialogFragment.CommonOptionsDialogListFragment;
import com.healthcoco.healthcocoplus.enums.CommonListDialogType;
import com.healthcoco.healthcocoplus.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocoplus.enums.DialogType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocoplus.listeners.CommonOptionsDialogItemClickListener;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Shreshtha on 18-01-2017.
 */

public abstract class HealthCocoFragment extends Fragment implements GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {
    protected static String SHOW_LOADING = "showLoading";
    protected View view;
    protected HealthCocoActivity mActivity;
    protected FragmentManager mFragmentManager;
    protected String TAG;
    protected HealthCocoApplication mApp;
    private Tracker mTracker;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TAG = getClass().getSimpleName();
        mActivity = (HealthCocoActivity) getActivity();
        mApp = (HealthCocoApplication) mActivity.getApplication();
        mFragmentManager = mActivity.getSupportFragmentManager();
//        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        view.setFocusable(true);
//        view.setFocusableInTouchMode(true);
        if (savedInstanceState != null) {
            LogUtils.LOGD(TAG, "onCreateView");
            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = savedInstanceState.getString(HealthCocoConstants.TAG_SELECTED_USER_ID);
            LogUtils.LOGD(TAG, "onCreateView " + HealthCocoConstants.SELECTED_PATIENTS_USER_ID + mActivity);
        }
        if (mTracker != null) {
            LogUtils.LOGD(TAG, "Setting screen name: " + TAG);
            mTracker.setScreenName("Screen Name " + TAG);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTracker = ((HealthCocoApplication) getActivity().getApplication()).getDefaultTracker();
    }

    public abstract void init();

    public abstract void initViews();

    public abstract void initListeners();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            LogUtils.LOGD(TAG, "onSaveInstanceState");
            outState.putString(HealthCocoConstants.TAG_SELECTED_USER_ID, HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        }
    }

    protected void initEditSearchView(int hintId, TextWatcher textWatcher, boolean setPaddingTop) {
        LinearLayout layoutEditSearch = (LinearLayout) view.findViewById(R.id.parent_edit_search);
        if (setPaddingTop)
            layoutEditSearch.setPadding(layoutEditSearch.getPaddingLeft(), mActivity.getResources().getDimensionPixelOffset(R.dimen.layout_edit_search_padding), layoutEditSearch.getPaddingRight(), layoutEditSearch.getPaddingBottom());
        final EditText editSearch = (EditText) view.findViewById(R.id.edit_search);
        editSearch.setHint(hintId);
        if (textWatcher != null)
            editSearch.addTextChangedListener(textWatcher);
        ImageButton btClear = (ImageButton) view.findViewById(R.id.bt_clear);
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSearch.setText("");
            }
        });
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        try {
            String errorMsg = errorMessage;
            if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
                errorMsg = volleyResponseBean.getErrMsg();
            }
            mActivity.hideLoading();
            showLoadingOverlay(false);
            Util.showToast(mActivity, errorMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {

    }

    protected void showLoadingOverlay(boolean showLoading) {
        LinearLayout loadingOverlay = (LinearLayout) view.findViewById(R.id.loading_overlay);
        if (loadingOverlay != null) {
            if (showLoading)
                loadingOverlay.setVisibility(View.VISIBLE);
            else
                loadingOverlay.setVisibility(View.GONE);
        }
    }

    protected void moveCamerToLatLong(GoogleMap googleMap, Double latitude, Double longitude) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(latitude, longitude)).zoom(12).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    protected void addBullet(LinearLayout containerBullets, int layoutId) {
        LinearLayout layout = (LinearLayout) mActivity.getLayoutInflater().inflate(layoutId, null);
        layout.setSelected(false);
        containerBullets.addView(layout);
    }

    protected void setBulletSelected(int position, LinearLayout containerBullets) {
        for (int i = 0; i < containerBullets.getChildCount(); i++) {
            View view = containerBullets.getChildAt(i);
            if (i == position)
                view.setSelected(true);
            else view.setSelected(false);
        }
    }

    protected void openDialogFragment(DialogType dialogTypeTitle, CommonOptionsDialogItemClickListener listener) {
        CommonOptionsDialogFragment mDialogFragment = new CommonOptionsDialogFragment(dialogTypeTitle, listener);
        mDialogFragment.show(this.mFragmentManager, CommonOptionsDialogItemClickListener.class.getSimpleName());
    }

    protected void openCommonOpenUpActivity(CommonOpenUpFragmentType fragmentType, Object intentData, int requestCode) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        if (intentData != null) {
            intent.putExtra(HealthCocoConstants.TAG_COMMON_OPENUP_INTENT_DATA, (Serializable) intentData);
        }
        if (requestCode == 0)
            startActivity(intent);
        else
            startActivityForResult(intent, requestCode);
    }

    protected String getSearchEditTextValue() {
        EditText editSearch = (EditText) view.findViewById(R.id.edit_search);
        if (editSearch != null)
            return Util.getValidatedValue(String.valueOf(editSearch.getText()));
        return "";
    }

    protected void clearSearchEditText() {
        try {
            EditText editSearch = (EditText) view.findViewById(R.id.edit_search);
            if (editSearch != null)
                editSearch.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
