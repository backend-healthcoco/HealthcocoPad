package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.InitialScreenViewPagerAapter;
import com.healthcoco.healthcocopad.dialogFragment.ContactUsDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.LoginDialogFragment;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.InitialScreenType;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by neha on 18/01/17.
 */
public class LoginSignupFragment extends HealthCocoFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String TAG_ORDINAL = "ordinal";
    public static boolean IS_FROM_CONTINUE_SIGNUP_SUCCESS = false;
    public static final String INTENT_SIGNUP_SUCCESS = "com.healthcoco.SIGNUP_SUCCESS";
    private Button btJoin;
    private Button btSignIn;
    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private int dotsCount;
    private TextView[] dots;
    private InitialScreenViewPagerAapter initialScreenViewPagerAapter;
    private LoginSignUpImageScreenFragment loginSignUpImageScreenFragment;
    private ArrayList<Fragment> fragmentsList;
    private CommonOpenUpFragmentType fragmentType;
    private boolean receiversRegistered;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login_signup, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public void init() {
        initViews();
        initListeners();
        initViewPagerAdapter();
        setUiPageViewController();
    }

    private void initViewPagerAdapter() {
        fragmentsList = new ArrayList<>();
        for (InitialScreenType initialScreenType : InitialScreenType.values()) {
            Bundle bundle = new Bundle();
            bundle.putInt(TAG_ORDINAL, initialScreenType.ordinal());
            loginSignUpImageScreenFragment = new LoginSignUpImageScreenFragment();
            loginSignUpImageScreenFragment.setArguments(bundle);
            fragmentsList.add(loginSignUpImageScreenFragment);
        }
        viewPager.setOffscreenPageLimit(fragmentsList.size());
        initialScreenViewPagerAapter = new InitialScreenViewPagerAapter(mFragmentManager);
        initialScreenViewPagerAapter.setFragmentsList(fragmentsList);
        viewPager.setAdapter(initialScreenViewPagerAapter);
    }

    private void setUiPageViewController() {
        dotsLayout = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);
        dotsCount = initialScreenViewPagerAapter.getCount();
        dots = new TextView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setPadding(5, 0, 5, 0);
            dots[i].setTextColor(getResources().getColor(R.color.grey_translucent));
            dotsLayout.addView(dots[i]);
        }
        dots[0].setTextColor(getResources().getColor(R.color.black_translucent));
    }

    private void setBulltSelected(int position, LinearLayout dotsLayout) {
        for (int i = 0; i < dotsLayout.getChildCount(); i++) {
            View view = dotsLayout.getChildAt(i);
            if (i == position)
                view.setSelected(true);
            else view.setSelected(false);
        }
    }

    public void initViews() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        btSignIn = (Button) view.findViewById(R.id.bt_sign_in);
        btJoin = (Button) view.findViewById(R.id.bt_join);
    }

    public void initListeners() {
        btSignIn.setOnClickListener(this);
        btJoin.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_sign_in) {
            openDialogFragment(new LoginDialogFragment());
        } else if (id == R.id.bt_join) {
            openDialogFragment(new ContactUsDialogFragment());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setTextColor(getResources().getColor(R.color.grey_translucent));
        }
        dots[position].setTextColor(getResources().getColor(R.color.black_translucent));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for signup success
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_SIGNUP_SUCCESS);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(signUpSuccessReceiver, filter);
            receiversRegistered = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(signUpSuccessReceiver);
    }

    BroadcastReceiver signUpSuccessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            LogUtils.LOGD(TAG, "Success DOCTOR_CONTACT_US received broadcast");
            String msg = getResources().getString(R.string.message_thank_you);
            if (intent != null && intent.hasExtra(HealthCocoConstants.TAG_BROADCAST_EXTRA))
                msg = Parcels.unwrap(intent.getParcelableExtra(HealthCocoConstants.TAG_BROADCAST_EXTRA));
            Util.showAlert(mActivity, getResources().getString(R.string.thank_you), msg);
        }
    };
}