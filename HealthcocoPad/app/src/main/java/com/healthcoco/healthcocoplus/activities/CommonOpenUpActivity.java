package com.healthcoco.healthcocoplus.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.enums.ActionbarLeftRightActionTypeDrawables;
import com.healthcoco.healthcocoplus.enums.ActionbarType;
import com.healthcoco.healthcocoplus.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocoplus.fragments.AboutUsFragment;
import com.healthcoco.healthcocoplus.fragments.AddEditClinicImageFragment;
import com.healthcoco.healthcocoplus.fragments.AddNewPrescriptionFragment;
import com.healthcoco.healthcocoplus.fragments.CommonOpenUpPatientDetailFragment;
import com.healthcoco.healthcocoplus.fragments.DiseaseListFragment;
import com.healthcoco.healthcocoplus.fragments.FeedbackFragment;
import com.healthcoco.healthcocoplus.fragments.InitialSyncFragment;
import com.healthcoco.healthcocoplus.fragments.LoginSignupFragment;
import com.healthcoco.healthcocoplus.fragments.PatientRegistrationFragment;
import com.healthcoco.healthcocoplus.fragments.PrescriptionUIPermissionFragment;
import com.healthcoco.healthcocoplus.fragments.SettingUIPermissionsFragment;
import com.healthcoco.healthcocoplus.fragments.WebViewFragments;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;

public class CommonOpenUpActivity extends HealthCocoActivity {
    private Fragment loginSignupFragment;
    private Intent intent;
    private FragmentTransaction transaction;
    private Toolbar toolbar;
    private int fragmentOrdinal;
    private CommonOpenUpFragmentType fragmentType;
    private WebViewFragments webViewFragments;
    private LinearLayout patientProfileLayout;
    private int tabOrdinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_screen);
        init();
    }

    private void init() {
        intent = getIntent();
        fragmentOrdinal = intent.getIntExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, -1);
        tabOrdinal = intent.getIntExtra(HealthCocoConstants.TAG_TAB_TYPE, -1);
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
                mApp.addActivityToStack(this);
                openFragment(ActionbarType.HIDDEN, R.string.sign_up, new LoginSignupFragment());
                break;
            case CONTINUE_SIGN_UP:
                break;
            case TERMS_OF_SERVICE:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.terms_of_service, new WebViewFragments());
                break;
            case PRIVACY_POLICY:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.privacy_policy, new WebViewFragments());
                break;
            case INITIAL_SYNC:
                openFragment(ActionbarType.HIDDEN, R.string.sync, new InitialSyncFragment());
                break;
            case FEEDBACK:
                openFragment(ActionbarType.TITLE_SAVE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.help_us_to_improve, new FeedbackFragment());
                break;
            case ADD_EDIT_CLINIC_IMAGE:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_DONE, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.clinic_photos, new AddEditClinicImageFragment());
                break;
            case SETTINGS_PATIENT:
//                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, R.string.patient, new SettingUIPermissionsFragment());
                break;
            case SETTINGS_CLINICAL_NOTES:
//                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, R.string.clinical_notes, new SettingUIPermissionsFragment());
                break;
            case SETTINGS_HISTORY:
//                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, R.string.history_small, new SettingUIPermissionsFragment());
                break;
            case SETTINGS_PRESCRIPTION:
//                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, R.string.prescriptions, new SettingUIPermissionsFragment());
                break;
            case SETTINGS_TEMPLATE:
//                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, R.string.templates, new SettingUIPermissionsFragment());
                break;
            case SETTING_UI_PERMISSION:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.WITH_SYNC, R.string.ui_permission, new SettingUIPermissionsFragment());
                break;
            case SETTINGS_BILLING:
//                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, R.string.billing, new SettingUIPermissionsFragment());
                break;
            case SETTING_SMS:
//                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, R.string.sms, new SettingUIPermissionsFragment());
                break;
            case SETTING_ABOUT_US:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.about_us, new AboutUsFragment());
                break;
            case SETTINGS_UI_PERMISSION_PRESCRIPTION:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, R.string.prescription_ui_permission_details, new PrescriptionUIPermissionFragment());
                break;
            case SETTINGS_UI_PERMISSION_CLINICAL_NOTES:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, R.string.clinical_notes_ui_permission_details, new PrescriptionUIPermissionFragment());
                break;
            case SETTINGS_UI_PERMISSION_VISITS:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, R.string.visits_ui_permission_details, new PrescriptionUIPermissionFragment());
                break;
            case SETTINGS_UI_PERMISSION_PATIENT_TAB:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, R.string.patient_tab_ui_permission_details, new PrescriptionUIPermissionFragment());
                break;
            case ADD_NEW_PRESCRIPTION:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, R.string.new_prescription, new AddNewPrescriptionFragment());
                break;
            case PATIENT_REGISTRATION:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, fragmentType.getTitleId(), new PatientRegistrationFragment());
                break;
            case PATIENT_DETAIL:
                hideSoftKeyboardOnStartUp();
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.patient_profile, new CommonOpenUpPatientDetailFragment());
                break;
            case HISTORY_DISEASE_LIST:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, R.string.past_history, new DiseaseListFragment());
                break;
        }
    }

    private void hideSoftKeyboardOnStartUp() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void openFragment(HealthCocoFragment fragment) {
        hideActionBar();
        transaction.add(R.id.layout_fragment_common_open_up, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

    private void openFragment(ActionbarType actionbarType, HealthCocoFragment fragment) {
        openFragment(actionbarType, null, null, 0, fragment);
    }

    private void openFragment(ActionbarType actionbarType, int actionBarTitleId, HealthCocoFragment fragment) {
        openFragment(actionbarType, null, null, actionBarTitleId, fragment);
    }

    private void openFragment(ActionbarType actionbarType, ActionbarLeftRightActionTypeDrawables leftAction, ActionbarLeftRightActionTypeDrawables rightAction, int actionBarTitle, HealthCocoFragment fragment) {
        Bundle bundle = new Bundle();
        initActionBar(actionbarType, actionBarTitle, leftAction, rightAction);
        bundle.putInt(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        bundle.putInt(HealthCocoConstants.TAG_TAB_TYPE, tabOrdinal);
        fragment.setArguments(bundle);
        transaction.add(R.id.layout_fragment_common_open_up, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

    private void openFragment(ActionbarType actionbarType, CommonOpenUpFragmentType fragmentType, ActionbarLeftRightActionTypeDrawables leftAction, ActionbarLeftRightActionTypeDrawables rightAction, HealthCocoFragment fragment) {
        initActionBar(actionbarType, fragmentType.getTitleId(), leftAction, rightAction);
        transaction.add(R.id.layout_fragment_common_open_up, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

    public void initActionbarTitle(int titleId) {
        initActionbarTitle(getResources().getString(titleId));
    }

    public void initActionbarTitle(String titleId) {
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.tv_title);
        if (tvTitle != null) tvTitle.setText(titleId);
    }


    public void initActionBar(ActionbarType actionbarType, int title, ActionbarLeftRightActionTypeDrawables leftAction, ActionbarLeftRightActionTypeDrawables rightAction) {
        if (actionbarType == ActionbarType.HIDDEN) {
            hideActionBar();
        } else {
            View actionbar = getLayoutInflater().inflate(actionbarType.getActionBarLayoutId(), null);
            actionbar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            toolbar.setContentInsetsAbsolute(0, 0);
            toolbar.addView(actionbar);
            LinearLayout containerLeftAction = (LinearLayout) actionbar.findViewById(R.id.container_left_action);
            if (leftAction != null && leftAction != ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION) {
                containerLeftAction.setVisibility(View.VISIBLE);
                View leftView = getLayoutInflater().inflate(leftAction.getLayoutId(), null);
                if (leftView != null) {
                    if (leftAction.isDrawableBackground()) {
                        ImageButton imageButton = (ImageButton) leftView;
                        imageButton.setImageResource(leftAction.getDrawableTitleId());
                    } else {
                        TextView button = (TextView) leftView;
                        button.setText(leftAction.getDrawableTitleId());
                    }
                    containerLeftAction.addView(leftView);
                }
                containerLeftAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            } else
                containerLeftAction.setVisibility(View.GONE);

            LinearLayout containerRightAction = (LinearLayout) actionbar.findViewById(R.id.container_right_action);
            if (rightAction != null && rightAction != ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION) {
                containerRightAction.setVisibility(View.VISIBLE);
                View rightView = getLayoutInflater().inflate(rightAction.getLayoutId(), null);
                if (rightView != null) {
                    if (rightAction.isDrawableBackground()) {
                        ImageButton imageButton = (ImageButton) rightView;
                        imageButton.setImageResource(rightAction.getDrawableTitleId());
                    } else {
                        TextView button = (TextView) rightView;
                        button.setText(rightAction.getDrawableTitleId());
                    }
                    containerRightAction.addView(rightView);
                }
            } else {
                containerRightAction.setVisibility(View.GONE);
            }
            if (title > 0) {
                TextView tvTitle = (TextView) toolbar.findViewById(R.id.tv_title);
                tvTitle.setText(title);
            }
        }
        setupUI(toolbar);
    }

    public void setupUI(final View view) {
        try {
            //Set up touch listener for non-text box views to hide keyboard.
            if (!(view instanceof EditText)) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        Util.hideKeyboard(CommonOpenUpActivity.this, view);
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
                case ADD_NEW_PRESCRIPTION:
//                case ADD_CLINICAL_NOTE:
//                case ADD_DRUG_DETAIL:
//                case ADD_NEW_TEMPLATE:
//                case ADD_RECORD_DETAIL:
                case PATIENT_REGISTRATION:
//                case ADD_VISIT:
//                case BOOK_APPOINTMENT:
                    showFinishConfirmationAlert();
                    break;
                default:
                    finish();
                    break;
            }
        } else
            finish();
    }

    private void showFinishConfirmationAlert() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.alert);
        alertBuilder.setMessage(R.string.your_changes_will_not_be_saved);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.leave, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        alertBuilder.setNegativeButton(R.string.stay, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    public void initActionbarRightAction(View.OnClickListener listener) {
        LinearLayout rightAction = (LinearLayout) findViewById(R.id.container_right_action);
        rightAction.setOnClickListener(listener);
    }

    public void initSaveButton(View.OnClickListener listener) {
        Button btSave = (Button) findViewById(R.id.bt_save);
        if (btSave != null)
            btSave.setOnClickListener(listener);
    }

    public void initFloatingActionButton(View.OnClickListener listener) {
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.bt_add_patient);
        floatingActionButton.setOnClickListener(listener);
    }
}
