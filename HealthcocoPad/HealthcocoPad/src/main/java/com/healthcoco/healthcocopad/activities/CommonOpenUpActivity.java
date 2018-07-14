package com.healthcoco.healthcocopad.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.calendar.CalendarFragment;
import com.healthcoco.healthcocopad.calendar.pinlockview.ChangePinFragment;
import com.healthcoco.healthcocopad.enums.ActionbarLeftRightActionTypeDrawables;
import com.healthcoco.healthcocopad.enums.ActionbarType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.fragments.AboutDoctorFragment;
import com.healthcoco.healthcocopad.fragments.AboutUsFragment;
import com.healthcoco.healthcocopad.fragments.AddEditClinicImageFragment;
import com.healthcoco.healthcocopad.fragments.AddClinicalNotesVisitNormalFragment;
import com.healthcoco.healthcocopad.fragments.AddEditNormalVisitPrescriptionFragment;
import com.healthcoco.healthcocopad.fragments.AddEditNormalVisitsFragment;
import com.healthcoco.healthcocopad.fragments.AddInvoiceFragment;
import com.healthcoco.healthcocopad.fragments.AddNewTemplateFragment;
import com.healthcoco.healthcocopad.fragments.AddNewTreatmentFragment;
import com.healthcoco.healthcocopad.fragments.AddReceiptFragment;
import com.healthcoco.healthcocopad.fragments.AppointmentFeedbackFragment;
import com.healthcoco.healthcocopad.fragments.BlogDetailFragment;
import com.healthcoco.healthcocopad.fragments.CommonOpenUpPatientDetailFragment;
import com.healthcoco.healthcocopad.fragments.CommonUiPermissionsFragment;
import com.healthcoco.healthcocopad.fragments.ContactsListFragment;
import com.healthcoco.healthcocopad.fragments.DiagramsListFragment;
import com.healthcoco.healthcocopad.fragments.DiseaseListFragment;
import com.healthcoco.healthcocopad.fragments.DoctorDetailsFragment;
import com.healthcoco.healthcocopad.fragments.EnlargedMapViewFragment;
import com.healthcoco.healthcocopad.fragments.FeedbackFragment;
import com.healthcoco.healthcocopad.fragments.FeedsFragment;
import com.healthcoco.healthcocopad.fragments.InitialSyncFragment;
import com.healthcoco.healthcocopad.fragments.KioskFragment;
import com.healthcoco.healthcocopad.fragments.LoginSignupFragment;
import com.healthcoco.healthcocopad.fragments.NotificationResponseDataFragment;
import com.healthcoco.healthcocopad.fragments.PatientEducationVideoListFragment;
import com.healthcoco.healthcocopad.fragments.PatientRegistrationFragment;
import com.healthcoco.healthcocopad.fragments.PatientRegistrationTabsFragment;
import com.healthcoco.healthcocopad.fragments.QueueFragment;
import com.healthcoco.healthcocopad.fragments.SelectedDiagramDetailFragment;
import com.healthcoco.healthcocopad.fragments.SettingKioskFragment;
import com.healthcoco.healthcocopad.fragments.SettingPrintSetupFragment;
import com.healthcoco.healthcocopad.fragments.SettingUIPermissionsFragment;
import com.healthcoco.healthcocopad.fragments.SettingsNameHideActivateFragment;
import com.healthcoco.healthcocopad.fragments.SyncFragment;
import com.healthcoco.healthcocopad.fragments.SettingsNameHideActivateTabFragment;
import com.healthcoco.healthcocopad.fragments.TemplateListFragment;
import com.healthcoco.healthcocopad.fragments.VideoFragment;
import com.healthcoco.healthcocopad.fragments.WebViewFragments;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

public class CommonOpenUpActivity extends HealthCocoActivity {
    private Fragment loginSignupFragment;
    private Intent intent;
    private FragmentTransaction transaction;
    private Toolbar toolbar;
    private int fragmentOrdinal;
    private CommonOpenUpFragmentType fragmentType;
    private WebViewFragments webViewFragments;
    private LinearLayout patientProfileLayout;

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
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_SEND, R.string.help_us_to_improve, new FeedbackFragment());
                break;
            case CONTACTS_LIST:
                openFragment(ActionbarType.HIDDEN, 0, new ContactsListFragment());
                break;
            case ADD_EDIT_CLINIC_IMAGE:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_DONE, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.clinic_photos, new AddEditClinicImageFragment());
                break;
            case SETTINGS_GROUPS:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.patient, new SettingsNameHideActivateTabFragment());
                break;
            case SETTINGS_CLINICAL_NOTES:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.clinical_notes, new SettingsNameHideActivateTabFragment());
                break;
            case SETTINGS_PRESCRIPTION:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.prescriptions, new SettingsNameHideActivateTabFragment());
                break;
            case SETTINGS_TEMPLATE:
//                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, R.string.templates, new SettingUIPermissionsFragment());
                break;
            case SETTING_UI_PERMISSION:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.WITH_SYNC, R.string.ui_permission, new SettingUIPermissionsFragment());
                break;
            case SETTING_PRINT:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, R.string.print_setting, new SettingPrintSetupFragment());
                break;
            case SETTING_SMS:
//                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, R.string.sms, new SettingUIPermissionsFragment());
                break;
          /*  case SETTINGS_GROUPS:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.groups, new SettingsNameEditDeleteFragment());
                break;
            case SETTINGS_REFERENCE:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.referred_by_settings, new SettingsNameHideActivateFragment());
                break;
           */
            case SETTINGS_HISTORY:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.history_small, new SettingsNameHideActivateFragment());
                break;
            case SETTINGS_TREATMENT:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.treatment, new SettingsNameHideActivateFragment());
                break;
          /*  case SETTINGS_DISEASE:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.direction, new SettingsNameHideActivateFragment());
                break;
            case SETTINGS_FREQUENCY:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.frequency, new SettingsNameHideActivateFragment());
                break;
            case SETTINGS_DRUG:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.drug, new SettingsNameHideActivateFragment());
                break;
          */
            case TEMPLATE_LIST:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.templates, new TemplateListFragment());
                break;
//            case SYNC_CONTACT:
//                openFragment(ActionbarType.TITLE, fragmentType.getTitleId(), ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, new SyncContactListFragment());
//                break;
            case SETTING_ABOUT_US:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.about_us, new AboutUsFragment());
                break;
            case SETTINGS_UI_PERMISSION_PRESCRIPTION:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, R.string.prescription_ui_permission_details, new CommonUiPermissionsFragment());
                break;
            case SETTINGS_UI_PERMISSION_CLINICAL_NOTES:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, R.string.clinical_notes_ui_permission_details, new CommonUiPermissionsFragment());
                break;
            case SETTINGS_UI_PERMISSION_VISITS:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, R.string.visits_ui_permission_details, new CommonUiPermissionsFragment());
                break;
            case SETTINGS_UI_PERMISSION_PATIENT_TAB:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, R.string.patient_tab_ui_permission_details, new CommonUiPermissionsFragment());
                break;
            case SETTING_KIOSK:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, R.string.kiosk_setting, new SettingKioskFragment());
                break;
            case ADD_NEW_PRESCRIPTION:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, R.string.new_prescription, new AddEditNormalVisitPrescriptionFragment());
                break;
            case PATIENT_REGISTRATION:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, fragmentType.getTitleId(), new PatientRegistrationFragment());
                break;
            case PATIENT_DETAIL:
                hideSoftKeyboardOnStartUp();
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.WITH_GLOBAL_ACCESS_BUTTON, R.string.patient_profile, new CommonOpenUpPatientDetailFragment());
                break;
            case HISTORY_DISEASE_LIST:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, R.string.past_history, new DiseaseListFragment());
                break;
            case ADD_NEW_TEMPLATE:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, R.string.new_template, new AddNewTemplateFragment());
                break;
            case NOTIFICATION_RESPONSE_DATA:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, fragmentType.getTitleId(), new NotificationResponseDataFragment());
                break;
            case ENLARGED_MAP_VIEW_FRAGMENT:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.location, new EnlargedMapViewFragment());
                break;
           /* case ADD_NEW_TREATMENT:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS,ActionbarLeftRightActionTypeDrawables.WITH_SAVE, fragmentType.getTitleId(), new AddNewTreatmentDialogFragment());
                break;*/
            case ADD_VISITS:
                openFragment(ActionbarType.HIDDEN, 0, new AddEditNormalVisitsFragment());
                break;
            case ADD_INVOICE:
                openFragment(ActionbarType.HIDDEN, 0, new AddInvoiceFragment());
                break;
            case ADD_RECEIPT:
                openFragment(ActionbarType.HIDDEN, fragmentType.getTitleId(), new AddReceiptFragment());
                break;
            case SELECT_DIAGRAM:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, fragmentType.getTitleId(), new DiagramsListFragment());
                break;
            case SELECTED_DIAGRAM_DETAIL:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.WITH_SAVE, fragmentType.getTitleId(), new SelectedDiagramDetailFragment());
                break;
            case ADD_CLINICAL_NOTE:
                openFragment(ActionbarType.HIDDEN, 0, new AddClinicalNotesVisitNormalFragment());
                break;
            case ADD_TREATMENT:
                openFragment(ActionbarType.HIDDEN, 0, new AddNewTreatmentFragment());
                break;
            case SYNC:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.sync, new SyncFragment());
                break;
            case PLAY_VIDEO:
                openFragment(ActionbarType.HIDDEN, new VideoFragment());
                break;
            case PATIENT_REGISTRATION_TABS:
                openFragment(ActionbarType.HIDDEN, new PatientRegistrationTabsFragment());
                break;
            case ABOUT_DOCTOR:
                openFragment(ActionbarType.HIDDEN, new AboutDoctorFragment());
                break;
            case BLOGS:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.blogs, new FeedsFragment());
                break;
            case BLOG_DETAIL:
                openFragment(ActionbarType.HIDDEN, new BlogDetailFragment());
                break;
            case FEEDBACK_DOCTOR:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.WITH_SUBMIT, fragmentType.getTitleId(), new AppointmentFeedbackFragment());
                break;
            case EDUCATION_VIDEO:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_BACK, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.videos, new PatientEducationVideoListFragment());
                break;
            case DOCTOR_DETAILS:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.about_doctor, new DoctorDetailsFragment());
                break;
            case CHANGE_PIN:
                openFragment(ActionbarType.TITLE, ActionbarLeftRightActionTypeDrawables.WITH_CROSS, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, R.string.change_pin, new ChangePinFragment());
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
        initActionBar(actionbarType, actionBarTitle, leftAction, rightAction);
        transaction.add(R.id.layout_fragment_common_open_up, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

    private void openFragment(ActionbarType actionbarType, CommonOpenUpFragmentType fragmentType, ActionbarLeftRightActionTypeDrawables leftAction, ActionbarLeftRightActionTypeDrawables rightAction, HealthCocoFragment fragment) {
        initActionBar(actionbarType, fragmentType.getTitleId(), leftAction, rightAction);
        transaction.add(R.id.layout_fragment_common_open_up, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

    public void initActionbarTitle(int titleId) {
        try {
            initActionbarTitle(getResources().getString(titleId));
        } catch (Exception e) {
            initActionbarTitle(getResources().getString(R.string.app_name));
        }
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
                        finishThisActivity();
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

    public void finishThisActivity() {
        if (fragmentType != null) {
            switch (fragmentType) {
                case ADD_NEW_PRESCRIPTION:
                case ADD_CLINICAL_NOTE:
//                case ADD_DRUG_DETAIL:
                case ADD_NEW_TEMPLATE:
//                case ADD_RECORD_DETAIL:
                case PATIENT_REGISTRATION:
                case ADD_VISITS:
                case ADD_INVOICE:
                case ADD_RECEIPT:
                case BOOK_APPOINTMENT:
                case PATIENT_REGISTRATION_TABS:
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

    public void initSaveButton(View.OnClickListener listener) {
        Button btSave = (Button) findViewById(R.id.bt_save);
        if (btSave != null)
            btSave.setOnClickListener(listener);
    }

    public LinearLayout initActionbarRightAction(View.OnClickListener listener) {
        LinearLayout rightAction = (LinearLayout) findViewById(R.id.container_right_action);
        rightAction.setOnClickListener(listener);
        return rightAction;
    }

    public void showRightAction(boolean show) {
        LinearLayout rightAction = (LinearLayout) findViewById(R.id.container_right_action);
        if (rightAction != null) {
            if (!show)
                rightAction.setVisibility(View.GONE);
            else
                rightAction.setVisibility(View.VISIBLE);
        }
    }

    public void enableRightActionButton(boolean isEnabled) {
        LinearLayout rightAction = (LinearLayout) findViewById(R.id.container_right_action);
        if (rightAction != null) {
            Util.enableAllChildViews(rightAction, isEnabled);
//            Button rightActionButton = (Button) rightAction.getChildAt(0);
//            if (rightActionButton != null)
//                rightActionButton.setEnabled(isEnabled);
        }
    }

    public void initRightActionView(ActionbarLeftRightActionTypeDrawables rightAction, View actionbar) {
        LinearLayout containerRightAction = (LinearLayout) actionbar.findViewById(R.id.container_right_action);
        if (containerRightAction != null) {
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
        }
    }

}
