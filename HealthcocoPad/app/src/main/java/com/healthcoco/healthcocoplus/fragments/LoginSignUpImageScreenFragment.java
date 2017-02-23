package com.healthcoco.healthcocoplus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.enums.InitialScreenType;

/**
 * Created by Shreshtha on 20-01-2017.
 */

public class LoginSignUpImageScreenFragment extends HealthCocoFragment {
    private InitialScreenType initialScreenType;
    private ImageView ivImage;
    private TextView tvMessage;
    private TextView tvTitle;
    private View view;
    private HealthCocoActivity mActivity;
    public static final String TAG_ORDINAL = "ordinal";

    public LoginSignUpImageScreenFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_image_login_signup, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public void init() {
        Bundle bundle = getArguments();
        System.out.println("LoginSignupImageFragment" + bundle);
        int ordinal = bundle.getInt(TAG_ORDINAL, -1);
        if (ordinal != -1)
            initialScreenType = InitialScreenType.values()[ordinal];
        if (initialScreenType != null) {
            initViews();
            initListeners();
            initData();
        }
    }

    private void initData() {
        ivImage.setImageResource(initialScreenType.getDrawableId());
        System.out.println("LoginSignUpImageScreenFragment" + initialScreenType.getDrawableId());
        tvTitle.setText(initialScreenType.getTitleId());
        tvMessage.setText(initialScreenType.getMessageId());
    }

    public void initViews() {
        ivImage = (ImageView) view.findViewById(R.id.iv_image);

        int iconSizeId;
        try {
            iconSizeId = Util.getSizeFromDimen(mActivity, initialScreenType.getIconSizeId());
        } catch (Exception e) {
            iconSizeId = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        tvMessage = (TextView) view.findViewById(R.id.tv_message);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
    }

    public void initListeners() {

    }
}
