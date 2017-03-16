package com.healthcoco.healthcocopad.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.ActionbarLeftRightActionTypeDrawables;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.DefaultSyncServiceType;
import com.healthcoco.healthcocopad.enums.FilterItemType;
import com.healthcoco.healthcocopad.enums.FragmentType;
import com.healthcoco.healthcocopad.fragments.CalendarFragment;
import com.healthcoco.healthcocopad.fragments.ClinicalProfileFragment;
import com.healthcoco.healthcocopad.fragments.ContactsListFragment;
import com.healthcoco.healthcocopad.fragments.DoctorProfileFragment;
import com.healthcoco.healthcocopad.fragments.FilterFragment;
import com.healthcoco.healthcocopad.fragments.IssueTrackerFragment;
import com.healthcoco.healthcocopad.fragments.MenuDrawerFragment;
import com.healthcoco.healthcocopad.fragments.SettingsFragment;
import com.healthcoco.healthcocopad.fragments.SyncFragment;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.SlidingPaneDrawerLayout;

public class HomeActivity extends HealthCocoActivity implements View.OnClickListener {
    public static final String INTENT_SYNC_SUCCESS = "com.healthcoco.INITIAL_SYNC_SUCCESS";
    private static final int MENU_SELECTION_TIME = 500;
    private ImageButton btMenu;
    private TextView tvTitle;
    private DrawerLayout drawerLayout;
    private LinearLayout layoutContactsFragment;
    private LinearLayout layoutOtherFragments;
    private boolean receiversRegistered;
    private LinearLayout containerRightActionType;
    private LinearLayout containerMiddleAction;
    private boolean loadDataOnDrawerClose;
    private User user;
    private SlidingPaneDrawerLayout sliding_pane_layout;
    private FragmentType selectedFramentType;
    private MenuDrawerFragment menuFragment;
    private ContactsListFragment contactsFragment;
    private LinearLayout layout_right_drawer;
    private FilterFragment filterFragment;

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
//        String notifficationResponseData = intent.getStringExtra(MyGcmListenerService.TAG_NOTIFICATION_RESPONSE);
//        if (!Util.isNullOrBlank(notifficationResponseData)) {
//            openNotificationResponseDataFragment(notifficationResponseData);
//        }
        initViews();
        initListeners();
        Util.checkNetworkStatus(this);
//        boolean exitApp = intent.getBooleanExtra(MyExceptionHandler.TAG_EXIT, false);
//        if (exitApp)
//            finish();
        if (HealthCocoConstants.isNetworkOnline) {
            mApp.clearLoginSignupActivityStack();
            boolean isFromLoginSignup = intent.getBooleanExtra(HealthCocoConstants.TAG_IS_FROM_LOGIN_SIGNUP, false);
            if (isFromLoginSignup) {
//                initGCM();
                int maxProgressValue = DefaultSyncServiceType.values().length - 1;
                LogUtils.LOGD(TAG, "Initial Sync " + maxProgressValue);
                openInitialSyncActivity(CommonOpenUpFragmentType.INITIAL_SYNC, isFromLoginSignup, maxProgressValue, 0);
                return;
            }
        }
        initFragments();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
    }

    private void initListeners() {
        btMenu.setOnClickListener(this);
        containerRightActionType.setOnClickListener(this);
//        drawerLayout.setDrawerListener();
    }

    private void initViews() {
        sliding_pane_layout = (SlidingPaneDrawerLayout) findViewById(R.id.sliding_pane_layout);
        containerRightActionType = (LinearLayout) findViewById(R.id.container_right_action);
        containerMiddleAction = (LinearLayout) findViewById(R.id.container_middle_action);
        layoutContactsFragment = (LinearLayout) findViewById(R.id.layout_contacts_fragment);
        layoutOtherFragments = (LinearLayout) findViewById(R.id.layout_right_detail);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(getResources().getColor(R.color.black_translucent));
        layout_right_drawer = (LinearLayout) findViewById(R.id.layout_right_drawer);
        btMenu = (ImageButton) findViewById(R.id.bt_menu);
        tvTitle = (TextView) findViewById(R.id.tv_title);
    }

    public void initFragment(FragmentType fragmentType) {
        HealthCocoFragment fragment = null;
        switch (fragmentType) {
            case PROFILE:
                fragment = new DoctorProfileFragment();
                break;
            case CLINIC_PROFILE:
                fragment = new ClinicalProfileFragment();
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
            case HELP_IMPROVE:
                openCommonOpenUpActivity(CommonOpenUpFragmentType.FEEDBACK, null, 0);
                break;
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

    BroadcastReceiver initialSyncSuccessreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(INTENT_SYNC_SUCCESS)) {
                initFragments();
            }
        }
    };

    public void initFragments() {
        initMenuFragment();
        initFilterFragment();
    }

    private void initMenuFragment() {
        menuFragment = new MenuDrawerFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_master, menuFragment, menuFragment.getClass().getSimpleName());
        transaction.commitAllowingStateLoss();
    }

    public void initContactsFragment() {
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
                    super.onBackPressed();
                }
            }
        } catch (Exception e) {
            mApp.cancelAllPendingRequests();
            super.onBackPressed();
        }
    }

    public void enableFilterButton() {
        containerRightActionType.setEnabled(true);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.END);
    }

    public void disableFilterButton() {
        containerRightActionType.setEnabled(false);
    }

    public void closePaneLayout() {
        sliding_pane_layout.closePane();
    }

    public void closeDrawer() {
        if (drawerLayout.isShown()) {
            drawerLayout.closeDrawers();
        }
    }

    public void disableRightDrawer(boolean disable) {
        if (disable)
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, GravityCompat.END);
        else
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.END);
    }
}
