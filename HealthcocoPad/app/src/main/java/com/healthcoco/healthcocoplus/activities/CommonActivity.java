package com.healthcoco.healthcocoplus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewFragment;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.enums.ActionbarLeftRightActionType;
import com.healthcoco.healthcocoplus.enums.ActionbarType;
import com.healthcoco.healthcocoplus.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocoplus.enums.DefaultSyncServiceType;
import com.healthcoco.healthcocoplus.fragments.InitialSyncFragment;
import com.healthcoco.healthcocoplus.fragments.LoginSignupFragment;
import com.healthcoco.healthcocoplus.fragments.WebViewFragments;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;

public class CommonActivity extends HealthCocoActivity {
    private Fragment loginSignupFragment;
    private Intent intent;
    private FragmentTransaction transaction;
    private Toolbar toolbar;
    private int fragmentOrdinal;
    private CommonOpenUpFragmentType fragmentType;
    private WebViewFragments webViewFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_screen);
        init();
    }

    private void init() {
        intent = getIntent();
        fragmentOrdinal = intent.getIntExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, -1);
        initMembers();
        if (fragmentOrdinal != -1)
            initFragment(fragmentOrdinal);
    }

    private void initMembers() {
        LogUtils.LOGD(TAG, "initMembers");
        transaction = getSupportFragmentManager().beginTransaction();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void initFragment(int ordinal) {
        fragmentType = CommonOpenUpFragmentType.values()[ordinal];
        switch (fragmentType) {
            case LOGIN_SIGN_UP:
//                mApp.addActivityToStack(this);
                openFragment(ActionbarType.HIDDEN, R.string.sign_up, new LoginSignupFragment());
                break;
            case CONTINUE_SIGN_UP:
                break;
            case TERMS_OF_SERVICE:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionType.WITH_CROSS, R.string.terms_of_service, new WebViewFragments());
                break;
            case PRIVACY_POLICY:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionType.WITH_CROSS, R.string.privacy_policy, new WebViewFragments());
                break;
            case INITIAL_SYNC:
                openFragment(ActionbarType.HIDDEN, R.string.sync, new InitialSyncFragment());
                break;
        }
    }

    private void openFragment(ActionbarType actionbarType, int actionBarTitleId, HealthCocoFragment fragment) {
        openFragment(actionbarType, null, actionBarTitleId, fragment);
    }

    private void openFragment(ActionbarType actionbarType, ActionbarLeftRightActionType actionbarLeftRightActionType, int actionBarTitleId, HealthCocoFragment fragment) {
        openFragment(actionbarType, actionbarLeftRightActionType, getResources().getString(actionBarTitleId), fragment);
    }

    public void initActionbarTitle(int titleId) {
        initActionbarTitle(getResources().getString(titleId));
    }

    public void initActionbarTitle(String titleId) {
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.tv_title);
        if (tvTitle != null) tvTitle.setText(titleId);
    }

    private void openFragment(ActionbarType actionbarType, ActionbarLeftRightActionType actionbarLeftRightActionType, String actionBarTitle, HealthCocoFragment fragment) {
        initActionBar(actionbarType, actionbarLeftRightActionType, actionBarTitle);
        transaction.add(R.id.layout_fragment_common_open_up, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

    public void initActionBar(ActionbarType actionbarType, ActionbarLeftRightActionType actionbarLeftRightActionType, int titleId) {
        initActionBar(actionbarType, actionbarLeftRightActionType, getResources().getString(titleId));
    }

    public void initActionBar(ActionbarType actionbarType, ActionbarLeftRightActionType actionbarLeftRightActionType, String title) {
//        containerActionBar.removeAllViews();
        if (actionbarType == ActionbarType.HIDDEN) {
            hideActionBar();
        } else {
            View actionbar = getLayoutInflater().inflate(actionbarType.getActionBarLayoutId(), null);
            actionbar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            containerActionBar.addView(actionbar);
            toolbar.setContentInsetsAbsolute(0, 0);
            toolbar.addView(actionbar);
//            setSupportActionBar(toolbar);

            //initialising left action button
            //Back,Cross,Done
            LinearLayout containerLeftAction = (LinearLayout) actionbar.findViewById(R.id.container_left_action);
            if (containerLeftAction != null) {
                containerLeftAction.removeAllViews();
                if (actionbarLeftRightActionType != null) {
                    View actionView = getLayoutInflater().inflate(actionbarLeftRightActionType.getLayoutId(), null);
                    actionView.setClickable(false);
                    containerLeftAction.addView(actionView);
                }
                containerLeftAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishThisActivity();
                    }
                });
            }

            TextView tvTitle = (TextView) toolbar.findViewById(R.id.tv_title);
            tvTitle.setText(title);
        }
        setupUI(toolbar);
    }

    public void setupUI(final View view) {
        try {
            //Set up touch listener for non-text box views to hide keyboard.
            if (!(view instanceof EditText)) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        Util.hideKeyboard(CommonActivity.this, view);
                        return false;
                    }
                });
            }
            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    View innerView = ((ViewGroup) view).getChildAt(i);
                    setupUI(innerView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideActionBar() {
        toolbar.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        if (fragmentType != null) {
            switch (fragmentType) {
                case CONTINUE_SIGN_UP:
                    return;
                default:
                    finishThisActivity();
                    break;
            }
        } else
            super.onBackPressed();
    }


    private void finishThisActivity() {
        if (fragmentType != null) {
            switch (fragmentType) {
//                case ADD_NEW_PRESCRIPTION:
//                case ADD_CLINICAL_NOTE:
//                case ADD_DRUG_DETAIL:
//                case ADD_NEW_TEMPLATE:
//                case ADD_RECORD_DETAIL:
//                case PATIENT_REGISTRATION:
//                case ADD_VISIT:
//                case BOOK_APPOINTMENT:
//                    showFinishConfirmationAlert();
//                    break;
                default:
                    finish();
                    break;
            }
        } else
            finish();
    }
}
