package com.healthcoco.healthcocopad.dialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Achievement;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AchievementDetailItemListner;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.AchievementDetailListViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 18-02-2017.
 */
public class AddEditDoctorAwardAndPublicationDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, AchievementDetailItemListner {
    private LinearLayout containerItemsAwardsPublicationDetail;
    private List<Achievement> awardsPublicationList = new ArrayList<>();
    private FloatingActionButton btAddMore;
    private User user;
    private ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_add_edit_education_detail, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        setWidthHeight(0.50, 0.80);
    }

    @Override
    public void init() {
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            awardsPublicationList = LocalDataServiceImpl.getInstance(mApp).getAwardsPublicationDetailsList(user.getUniqueId());
            initViews();
            initListeners();
            notifyAdapter();
        }
    }

    @Override
    public void initViews() {
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        containerItemsAwardsPublicationDetail = (LinearLayout) view.findViewById(R.id.container_items_education_detail);
        btAddMore = (FloatingActionButton) view.findViewById(R.id.bt_add_more);
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
        initActionbarTitle(getResources().getString(R.string.awards_and_publication));
        btAddMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                Object object = validateData(true);
                if (object instanceof Boolean && (Boolean) object) {
                    addEditAwardsPublicationDetails();
                }

                break;
            case R.id.bt_add_more:
                Object object1 = validateData(false);
                if (object1 instanceof Boolean && (Boolean) object1) {
                    addEditAwardsPublicationItem(null, true);
                }
                break;
        }
    }

    private void addEditAwardsPublicationItem(Achievement achievement, boolean isAddMoreClicked) {
        AchievementDetailListViewHolder viewHolder = new AchievementDetailListViewHolder(mActivity);
        viewHolder.setData(achievement, this, containerItemsAwardsPublicationDetail.getChildCount());
        containerItemsAwardsPublicationDetail.addView(viewHolder);
        if (isAddMoreClicked)
            scrollView.postDelayed(new Runnable() {
                public void run() {
                    scrollView.fullScroll(HorizontalScrollView.FOCUS_DOWN);
                }
            }, 100L);
    }

    private void addEditAwardsPublicationDetails() {
        LogUtils.LOGD(TAG, "Educations List Size " + awardsPublicationList.size());
        mActivity.showLoading(false);
        DoctorProfile doctorProfile = new DoctorProfile();
        doctorProfile.setDoctorId(user.getUniqueId());
        doctorProfile.setAchievements(awardsPublicationList);
        WebDataServiceImpl.getInstance(mApp).addUpdateAchievments(DoctorProfile.class, doctorProfile, this, this);
    }

    @Override
    public void initData() {

    }

    private Object validateData(boolean isOnSaveClick) {
        awardsPublicationList = new ArrayList<>();
        Object object = true;
        if (containerItemsAwardsPublicationDetail.getChildCount() > 0) {
            for (int i = 0; i < containerItemsAwardsPublicationDetail.getChildCount(); i++) {
                AchievementDetailListViewHolder viewHolder = (AchievementDetailListViewHolder) containerItemsAwardsPublicationDetail.getChildAt(i);
                object = viewHolder.getErrorMessageOrTrueIfValidated(isOnSaveClick);
                if (object instanceof String)
                    return object;
            }
        }
        return true;
    }

    private void notifyAdapter() {
        containerItemsAwardsPublicationDetail.removeAllViews();
        if (Util.isNullOrEmptyList(awardsPublicationList)) {
            awardsPublicationList = new ArrayList<>();
            awardsPublicationList.add(null);
        }
        for (Achievement achievement : awardsPublicationList) {
            addEditAwardsPublicationItem(achievement, false);
        }
    }

    @Override
    public void onDeleteAchievementDetailClicked(View view, Achievement achievement) {
        showConfirmationAlert(view, achievement);
    }

    @Override
    public void addAchievementDetailToList(Achievement achievement) {
        if (achievement == null)
            awardsPublicationList = new ArrayList<>();
        awardsPublicationList.add(achievement);
    }

    private void showConfirmationAlert(final View viewToDelete, final Achievement achievement) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.this_cannot_be_undone);
        alertBuilder.setMessage(getResources().getString(
                R.string.do_you_want_to_delete_this_item));
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    containerItemsAwardsPublicationDetail.removeView(viewToDelete);
                    if (awardsPublicationList.contains(achievement))
                        awardsPublicationList.remove(achievement);
                    if (containerItemsAwardsPublicationDetail.getChildCount() == 0)
                        addEditAwardsPublicationItem(null, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        switch (response.getWebServiceType()) {
            case ADD_UPDATE_ACHIEVEMENTS_DETAIL:
                if (response.getData() != null && response.getData() instanceof DoctorProfile) {
                    DoctorProfile doctorProfileResponse = (DoctorProfile) response.getData();
                    LocalDataServiceImpl.getInstance(mApp).addAchievements(user.getUniqueId(), doctorProfileResponse.getAchievements());
                }
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                getDialog().dismiss();
                break;
        }
        mActivity.hideLoading();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        }
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }
}
