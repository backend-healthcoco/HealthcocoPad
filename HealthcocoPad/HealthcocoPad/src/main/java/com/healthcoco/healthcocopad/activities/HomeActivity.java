package com.healthcoco.healthcocopad.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.HealthcocoFCMListener;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.NotificationResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.ActionbarLeftRightActionTypeDrawables;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.DefaultSyncServiceType;
import com.healthcoco.healthcocopad.enums.FilterItemType;
import com.healthcoco.healthcocopad.enums.FragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.CalendarFragment;
import com.healthcoco.healthcocopad.fragments.ClinicalProfileFragment;
import com.healthcoco.healthcocopad.fragments.ContactsListFragment;
import com.healthcoco.healthcocopad.fragments.DoctorProfileFragment;
import com.healthcoco.healthcocopad.fragments.DoctorVideoListFragment;
import com.healthcoco.healthcocopad.fragments.EventFragment;
import com.healthcoco.healthcocopad.fragments.FilterFragment;
import com.healthcoco.healthcocopad.fragments.IssueTrackerFragment;
import com.healthcoco.healthcocopad.fragments.KioskFragment;
import com.healthcoco.healthcocopad.fragments.MenuDrawerFragment;
import com.healthcoco.healthcocopad.fragments.QueueFragment;
import com.healthcoco.healthcocopad.fragments.SettingsFragment;
import com.healthcoco.healthcocopad.fragments.SyncFragment;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.MyExceptionHandler;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.SlidingPaneDrawerLayout;

import java.util.List;

public class HomeActivity extends HealthCocoActivity implements View.OnClickListener, GsonRequest.ErrorListener,
        LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean> {
    public static final String INTENT_SYNC_SUCCESS = "com.healthcoco.INITIAL_SYNC_SUCCESS";
    public static final int REQUEST_PERMISSIONS = 101;
    public static final int REQUEST_CALL_PERMISSIONS = 102;
    private static final int MENU_SELECTION_TIME = 500;
    private ImageButton btMenu;
    private TextView tvTitle;
    private DrawerLayout drawerLayout;
    private LinearLayout layoutContactsFragment;
    private LinearLayout layoutOtherFragments;
    private FrameLayout layoutHomeActivity;
    private boolean receiversRegistered;
    private LinearLayout containerRightActionType;
    private LinearLayout containerMiddleAction;
    private RelativeLayout actionBarNormal;
    private User user;
    private SlidingPaneDrawerLayout sliding_pane_layout;
    private FragmentType selectedFramentType;
    private MenuDrawerFragment menuFragment;
    private ContactsListFragment contactsFragment;
    private FilterFragment filterFragment;
    BroadcastReceiver initialSyncSuccessreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(INTENT_SYNC_SUCCESS)) {
                refreshFragments();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            init();
        }
    }

    private void init() {
        Intent intent = getIntent();
        String notifficationResponseData = intent.getStringExtra(HealthcocoFCMListener.TAG_NOTIFICATION_RESPONSE);
        if (!Util.isNullOrBlank(notifficationResponseData)) {
            openNotificationResponseDataFragment(notifficationResponseData);
        }
        initViews();
        initListeners();
        Util.checkNetworkStatus(this);
        boolean exitApp = intent.getBooleanExtra(MyExceptionHandler.TAG_EXIT, false);
        if (exitApp)
            finish();
        if (HealthCocoConstants.isNetworkOnline) {
            mApp.clearLoginSignupActivityStack();
            boolean isFromLoginSignup = intent.getBooleanExtra(HealthCocoConstants.TAG_IS_FROM_LOGIN_SIGNUP, false);
            if (isFromLoginSignup) {
                int maxProgressValue = DefaultSyncServiceType.values().length - 1;
                LogUtils.LOGD(TAG, "Initial Sync " + maxProgressValue);
                openInitialSyncActivity(CommonOpenUpFragmentType.INITIAL_SYNC, isFromLoginSignup, maxProgressValue, 0);
                return;
            }
        }
        refreshFragments();
    }

    private void openNotificationResponseDataFragment(String notificationResponseData) {
        if (!Util.isNullOrBlank(notificationResponseData)) {
            NotificationResponse notificationResponse = new Gson().fromJson(notificationResponseData, NotificationResponse.class);
            if (notificationResponse.getNotificationType() != null)
                openCommonOpenUpActivity(CommonOpenUpFragmentType.NOTIFICATION_RESPONSE_DATA, HealthcocoFCMListener.TAG_NOTIFICATION_RESPONSE, notificationResponseData, 0);
        }
    }

    private void refreshFragments() {
        initFragments();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
        showLoading(false);
        new LocalDataBackgroundtaskOptimised(this, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initListeners() {
        btMenu.setOnClickListener(this);
        containerRightActionType.setOnClickListener(this);
    }

    private void initViews() {
        sliding_pane_layout = (SlidingPaneDrawerLayout) findViewById(R.id.sliding_pane_layout);
        containerRightActionType = (LinearLayout) findViewById(R.id.container_right_action);
        layoutHomeActivity = (FrameLayout) findViewById(R.id.layout_home_activity);
        containerMiddleAction = (LinearLayout) findViewById(R.id.container_middle_action);
        layoutContactsFragment = (LinearLayout) findViewById(R.id.layout_contacts_fragment);
        layoutOtherFragments = (LinearLayout) findViewById(R.id.layout_right_detail);
        actionBarNormal = (RelativeLayout) findViewById(R.id.action_bar_home_normal);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(getResources().getColor(R.color.black_translucent));
        btMenu = (ImageButton) findViewById(R.id.bt_menu);
        tvTitle = (TextView) findViewById(R.id.tv_title);

    }

    public void initFragment(FragmentType fragmentType) {
        actionBarNormal.setVisibility(View.VISIBLE);
        SlidingPaneLayout.LayoutParams params = new SlidingPaneLayout.LayoutParams(
                SlidingPaneLayout.LayoutParams.MATCH_PARENT,
                SlidingPaneLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins((int) getResources().getDimension(R.dimen.drawer_left_margin), 0, 0, 0);
        layoutHomeActivity.setLayoutParams(params);

        HealthCocoFragment fragment = null;
        switch (fragmentType) {
            case PROFILE:
                fragment = new DoctorProfileFragment();
                break;
            case CLINIC_PROFILE:
                fragment = new ClinicalProfileFragment();
                break;
            case QUEUE:
                fragment = new QueueFragment(this);
                actionBarNormal.setVisibility(View.GONE);
                break;
            case EVENTS:
                fragment = new EventFragment(this);
                actionBarNormal.setVisibility(View.GONE);
                break;
            case SYNC:
                fragment = new SyncFragment();
                break;
            case CONTACTS:
                setContactsFragmentVisibility(true, fragmentType);
                break;
            case CALENDAR:
                fragment = new CalendarFragment();
                break;
            case ISSUE_TRACKER:
                fragment = new IssueTrackerFragment();
                break;
            case SETTINGS:
                fragment = new SettingsFragment();
                break;
            case VIDEOS:
                fragment = new DoctorVideoListFragment();
                break;
//            case REGISTER:
             /*   actionBarNormal.setVisibility(View.GONE);
                params = new SlidingPaneLayout.LayoutParams(
                        SlidingPaneLayout.LayoutParams.MATCH_PARENT,
                        SlidingPaneLayout.LayoutParams.MATCH_PARENT
                );
                params.setMargins(0, 0, 0, 0);
                layoutHomeActivity.setLayoutParams(params);

                fragment = new KioskFragment();*/
//                Intent intent = new Intent(this, KioskActivity.class);
//                intent.putExtra(HealthcocoFCMListener.TAG_NOTIFICATION_RESPONSE, notificationResponseData);
//                startActivity(intent);
//                finish();
//
//                break;
            case HELP_IMPROVE:
                openCommonOpenUpActivity(CommonOpenUpFragmentType.FEEDBACK, null, 0);
                return;
            default:
                break;
        }
        if (fragment != null) {
            setContactsFragmentVisibility(false, selectedFramentType);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layout_right_detail, fragment, fragment.getClass().getSimpleName());
            fragmentTransaction.commit();
        }
        setMenuItemSelected(fragmentType);
        initRightAction(fragmentType);
        initMiddleAction(fragmentType);
    }

    private void initMiddleAction(FragmentType fragmentType) {
        ActionbarLeftRightActionTypeDrawables middleActionType = fragmentType.getMiddleActionType();
        containerMiddleAction.removeAllViews();
        if (middleActionType != null && middleActionType.getLayoutId() > 0) {
            containerMiddleAction.setVisibility(View.VISIBLE);
            View rightView = getLayoutInflater().inflate(middleActionType.getLayoutId(), null);
            if (rightView != null) {
                if (middleActionType.isDrawableBackground()) {
                    ImageButton imageButton = (ImageButton) rightView;
                    imageButton.setImageResource(middleActionType.getDrawableTitleId());
                } else {
                    TextView button = (TextView) rightView;
                    button.setText(middleActionType.getDrawableTitleId());
                }
                containerMiddleAction.addView(rightView);
            }
            containerMiddleAction.setTag(middleActionType);
        } else
            containerMiddleAction.setVisibility(View.GONE);
    }

    private void initRightAction(FragmentType fragmentType) {
        ActionbarLeftRightActionTypeDrawables rightActionType = fragmentType.getRightActionType();
        containerRightActionType.removeAllViews();
        if (rightActionType != null && rightActionType.getLayoutId() > 0) {
            containerRightActionType.setVisibility(View.VISIBLE);
            View rightView = getLayoutInflater().inflate(rightActionType.getLayoutId(), null);
            if (rightView != null) {
                if (rightActionType.isDrawableBackground()) {
                    ImageButton imageButton = (ImageButton) rightView;
                    imageButton.setImageResource(rightActionType.getDrawableTitleId());
                } else {
                    TextView button = (TextView) rightView;
                    button.setText(rightActionType.getDrawableTitleId());
                }
                containerRightActionType.addView(rightView);
            }
            containerRightActionType.setTag(rightActionType);
        } else
            containerRightActionType.setVisibility(View.GONE);
    }

    private void setContactsFragmentVisibility(boolean b, FragmentType type) {
        if (b) {
            layoutContactsFragment.setVisibility(View.VISIBLE);
            layoutOtherFragments.setVisibility(View.GONE);
            removeOtherFragmentInLayout();
            if (contactsFragment.getSelectedFilterType() == null
                    || contactsFragment.getSelectedFilterType() == FilterItemType.ALL_PATIENTS
                    || contactsFragment.getSelectedFilterType() == FilterItemType.SEARCH_PATIENT)
                setActionbarTitle(getResources().getString(type.getTitleId()));
            else {
                setActionbarTitle(filterFragment.getSelectedFilterTitle());
            }
        } else {
            layoutOtherFragments.setVisibility(View.VISIBLE);
            layoutContactsFragment.setVisibility(View.GONE);
            setActionbarTitle(getResources().getString(type.getTitleId()));
        }
        selectedFramentType = type;
    }

    private void removeOtherFragmentInLayout() {
        Fragment fragmentOther = getSupportFragmentManager().findFragmentById(layoutOtherFragments.getId());
        if (fragmentOther != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentOther).commit();
        }
    }

    private void setMenuItemSelected(final FragmentType fragmentType) {
        final Handler mHandler = new Handler();
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                if (menuFragment != null)
                    menuFragment.setMenuSelection(fragmentType);
            }
        };
        mHandler.postDelayed(runnable, MENU_SELECTION_TIME);
    }

    public void openInitialSyncActivity(CommonOpenUpFragmentType fragmentType, boolean isFromLoginSignup, int totalSyncServices, int i) {
        Intent intent = new Intent(this, InitialSyncActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_TOTAL_SYNC_SERVICES, totalSyncServices);
        intent.putExtra(HealthCocoConstants.TAG_IS_FROM_LOGIN_SIGNUP, isFromLoginSignup);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver to update existing contact
            IntentFilter updateExistingContact = new IntentFilter();
            updateExistingContact.addAction(INTENT_SYNC_SUCCESS);
            LocalBroadcastManager.getInstance(this).registerReceiver(initialSyncSuccessreceiver, updateExistingContact);
            receiversRegistered = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(initialSyncSuccessreceiver);
    }

    public void initFragments() {
        initMenuFragment();
        initFilterFragment();
        initContactsFragment();
        requestPermission();
    }

    private void initMenuFragment() {
        menuFragment = new MenuDrawerFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_master, menuFragment, menuFragment.getClass().getSimpleName());
        transaction.commitAllowingStateLoss();
    }

    public void initContactsFragment() {
        SlidingPaneLayout.LayoutParams params = new SlidingPaneLayout.LayoutParams(
                SlidingPaneLayout.LayoutParams.MATCH_PARENT,
                SlidingPaneLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins((int) getResources().getDimension(R.dimen.drawer_left_margin), 0, 0, 0);
        layoutHomeActivity.setLayoutParams(params);

        contactsFragment = new ContactsListFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout_contacts_fragment, contactsFragment, contactsFragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
        initFragment(FragmentType.CONTACTS);
    }

    private void initFilterFragment() {
        filterFragment = new FilterFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout_right_drawer, filterFragment, filterFragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void setActionbarTitle(String titleId) {
        tvTitle.setText(titleId);
    }

    public User getUser() {
        return user;
    }

    @Override
    public void onClick(View view) {
        hideSoftKeyboard();
        switch (view.getId()) {
            case R.id.bt_menu:
                if (sliding_pane_layout.isOpen()) {
                    sliding_pane_layout.closePane();
                } else {
                    sliding_pane_layout.openPane();
                }
                break;
            case R.id.container_right_action:
                switch (selectedFramentType) {
                    case CONTACTS:
                        if (contactsFragment != null)
                            drawerLayout.openDrawer(GravityCompat.END);
                        break;
                    case CALENDAR:
                        CalendarFragment calendarFragment = (CalendarFragment) getSupportFragmentManager().findFragmentByTag(CalendarFragment.class.getSimpleName());
                        if (calendarFragment != null) {
//                            calendarFragment.showOptionsWindow(v);
                        }
                        break;
                }
            default:
                break;
        }
    }

    public void closePaneLayout(FragmentType fragmentType) {
        selectedFramentType = fragmentType;
        sliding_pane_layout.closePane();
    }


    @Override
    public void onBackPressed() {

        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

        boolean handled = false;
        for (Fragment f : fragmentList) {
            if (f instanceof QueueFragment) {
                handled = ((QueueFragment) f).onBackPressed();
                if (handled) {
                    break;
                }
            }
        }
        if (!handled) {
            try {
                if (sliding_pane_layout.isOpen())
                    sliding_pane_layout.closePane();
                else if (drawerLayout.isDrawerOpen(GravityCompat.END))
                    drawerLayout.closeDrawers();
                else {
                    if (layoutOtherFragments.getVisibility() == View.VISIBLE) {
                        menuFragment.setMenuSelection(FragmentType.CONTACTS);
                        initFragment(FragmentType.CONTACTS);
                        closePaneLayout(FragmentType.CONTACTS);
                    } else {
                        mApp.cancelAllPendingRequests();
                        showFinishConfirmationAlert();
//                    super.onBackPressed();
                    }
                }
            } catch (Exception e) {
                mApp.cancelAllPendingRequests();
                super.onBackPressed();
            }
        }
    }

    private void showFinishConfirmationAlert() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.alert);
        alertBuilder.setMessage(R.string.are_you_want_to_exit);
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

    public void enableFilterButton() {
        containerRightActionType.setEnabled(true);
    }

    public void disableFilterButton() {
        containerRightActionType.setEnabled(false);
    }

    public void closeDrawer() {
        if (drawerLayout.isShown()) {
            drawerLayout.closeDrawers();
        }
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (response.getData() != null && response.getData() instanceof DoctorProfile) {
                        menuFragment.initData((DoctorProfile) response.getData());
//                        initContactsFragment();
                        WebDataServiceImpl.getInstance(mApp).getDoctorProfile(DoctorProfile.class, user.getUniqueId(), null, null, this, this);
                        return;
                    }
                    break;
                case GET_DOCTOR_PROFILE:
                    if (response.getData() != null && response.getData() instanceof DoctorProfile) {
                        DoctorProfile doctorProfile = (DoctorProfile) response.getData();
                        menuFragment.initData(doctorProfile);
                        LocalDataServiceImpl.getInstance(mApp).addDoctorProfile(doctorProfile);
                    }
                    initGCM();
                    break;
            }
        }
        hideLoading();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        }
        hideLoading();
        Util.showToast(this, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(this, R.string.user_offline);
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    user = doctor.getUser();
                    volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getDoctorProfileResponse(WebServiceType.GET_DOCTOR_PROFILE, user.getUniqueId(), null, null);
                    volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                }
                break;
            case ADD_DOCTOR_PROFILE:
                volleyResponseBean.setWebServiceType(response.getWebServiceType());
                LocalDataServiceImpl.getInstance(mApp).addDoctorProfile((DoctorProfile) response.getData());
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {

        } else {
//            Snackbar.make(findViewById(android.R.id.content), R.string.runtime_permissions_txt,
//                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent();
//                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            intent.addCategory(Intent.CATEGORY_DEFAULT);
//                            intent.setData(Uri.parse("package:" + getPackageName()));
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                            startActivity(intent);
//                        }
//                    }).show();
        }
    }

    public void requestAppPermissions(final String[] requestedPermissions, final int requestCode) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
        }
        ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
    }

    public void requestPermission() {
        requestAppPermissions(new
                String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CALL_PHONE,
                android.Manifest.permission.PROCESS_OUTGOING_CALLS,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
        }, REQUEST_PERMISSIONS);
    }
}
