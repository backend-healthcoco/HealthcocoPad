package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.WebViewType;

/**
 * Created by neha on 21/01/16.
 */
public class AboutUsFragment extends HealthCocoFragment implements View.OnClickListener {
    private static final String LINK_LINKED_IN = "http://www.linkedin.com/company/healthcoco";
    private static final String LINK_GOOGLE_PLUS = "https://plus.google.com/101326805468618396011";
    private static final String LINK_TWITTER = "http://www.twitter.com/healthcocoCOM";


    private TextView btPrivacyPolicy;
    private TextView btTermsAndConditions;
    private Button btGooglePlus;
    private Button btLinkedIn;
    private Button btTwitter;
    private TextView tvVersionName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_us, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
    }

    @Override
    public void initViews() {
        btPrivacyPolicy = (TextView) view.findViewById(R.id.bt_privacy_policy);
        btTermsAndConditions = (TextView) view.findViewById(R.id.bt_terms_and_conditions);
        btGooglePlus = (Button) view.findViewById(R.id.bt_google_plus);
        btLinkedIn = (Button) view.findViewById(R.id.bt_linked_in);
        btTwitter = (Button) view.findViewById(R.id.bt_twitter);
        tvVersionName = (TextView) view.findViewById(R.id.tv_version_name);
        tvVersionName.setText(mActivity.getFormattedVersionName());
    }

    @Override
    public void initListeners() {
        btPrivacyPolicy.setOnClickListener(this);
        btTermsAndConditions.setOnClickListener(this);
        btGooglePlus.setOnClickListener(this);
        btTwitter.setOnClickListener(this);
        btLinkedIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.bt_privacy_policy) {
            openCommonOpenUpActivity(CommonOpenUpFragmentType.PRIVACY_POLICY, WebViewFragments.TAG_WEB_VIEW_TYPE, WebViewType.PRIVACY_POLICY.ordinal(), 0);
        } else if (id == R.id.bt_terms_and_conditions) {
            openCommonOpenUpActivity(CommonOpenUpFragmentType.TERMS_OF_SERVICE, WebViewFragments.TAG_WEB_VIEW_TYPE, WebViewType.TERMS_OF_SERVICE.ordinal(), 0);
        } else if (id == R.id.bt_google_plus) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(LINK_GOOGLE_PLUS)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.bt_twitter) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(LINK_TWITTER)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.bt_linked_in) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(LINK_LINKED_IN)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
