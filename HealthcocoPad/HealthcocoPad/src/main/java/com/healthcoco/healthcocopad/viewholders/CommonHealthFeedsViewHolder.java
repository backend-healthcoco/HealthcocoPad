package com.healthcoco.healthcocopad.viewholders;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.HomeActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.HealthcocoBlogResponse;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.BlogDetailFragment;
import com.healthcoco.healthcocopad.fragments.CommonHealthFeedsFragment;
import com.healthcoco.healthcocopad.listeners.ImageLoadedListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.MetricsLightTextView;
import com.healthcoco.healthcocopad.views.MetricsMediumTextView;

/**
 * Created by Shreshtha on 23-Sep-17.
 */

public class CommonHealthFeedsViewHolder extends HealthCocoViewHolder implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, ImageLoadedListener {
    public static final String DATE_FORMAT = "dd MMM,yyyy";
    private HealthcocoBlogResponse objData;
    private ImageView ivTitleImage;
    private ImageView ivFav;
    private MetricsMediumTextView tvTitle;
    private MetricsLightTextView tvDescription;
    private MetricsMediumTextView tvCreatedByName;
    private MetricsLightTextView tvCreatedTime;
    private MetricsLightTextView tvTotalLikes;
    private MetricsLightTextView tvTotalShare;
    private ImageView btShare;
    private ImageView btLike;
    private LinearLayout containerBlogItem;
    private ImageView loadingGif;

    public CommonHealthFeedsViewHolder(HealthCocoActivity mActivity) {
        super(mActivity);
    }

    @Override
    public void setData(Object object) {
        this.objData = (HealthcocoBlogResponse) object;
    }

    @Override
    public void applyData() {
        if (objData != null) {
            //set image
            if (!Util.isNullOrBlank(objData.getTitleImage()))
                DownloadImageFromUrlUtil.loadImageUsingImageLoaderUsingDefaultImage(R.drawable.bg_doctor_img, ivTitleImage, objData.getTitleImage(), this);
            else onImageLoaded(null);
            if (objData.isFavourite())
                setFavoriteSelected(true);
            else setFavoriteSelected(false);

            if (objData.isliked())
                setLikesSelected(true);
            else setLikesSelected(false);
            tvTitle.setText(Util.getValidatedValue(objData.getTitle()));
            tvDescription.setText(Util.getValidatedValue(objData.getShortDesc()));
            tvCreatedByName.setText("By " + Util.getValidatedValue(objData.getPostBy()));
            tvCreatedTime.setText(Util.getValidatedValue(DateTimeUtil.getFormatedDateAndTime(DATE_FORMAT, objData.getCreatedTime())));
            tvTotalLikes.setText(Util.getValidatedValue(objData.getNoOfLikes()));
            tvTotalShare.setText(Util.getValidatedValue(objData.getViews()));
        }
    }

    @Override
    public View getContentView() {
        View convertView = inflater.inflate(R.layout.list_item_feeds, null);
        initViews(convertView);
        initListener();
        return convertView;
    }

    private void initViews(View convertView) {
        ivTitleImage = (ImageView) convertView.findViewById(R.id.iv_title_image);
        ivFav = (ImageView) convertView.findViewById(R.id.iv_fav);
        tvTitle = (MetricsMediumTextView) convertView.findViewById(R.id.tv_title);
        tvDescription = (MetricsLightTextView) convertView.findViewById(R.id.tv_description);
        tvCreatedByName = (MetricsMediumTextView) convertView.findViewById(R.id.tv_created_by_name);
        tvCreatedTime = (MetricsLightTextView) convertView.findViewById(R.id.tv_created_time);
        tvTotalLikes = (MetricsLightTextView) convertView.findViewById(R.id.tv_total_likes);
        tvTotalShare = (MetricsLightTextView) convertView.findViewById(R.id.tv_total_share);
        btShare = (ImageView) convertView.findViewById(R.id.bt_share);
        btLike = (ImageView) convertView.findViewById(R.id.bt_like);
        loadingGif = (ImageView) convertView.findViewById(R.id.loading_gif);
        containerBlogItem = (LinearLayout) convertView.findViewById(R.id.container_blog_item);

        tvTotalLikes.setVisibility(View.GONE);
        tvTotalShare.setVisibility(View.GONE);
        btShare.setVisibility(View.GONE);
        btLike.setVisibility(View.GONE);
    }

    private void initListener() {
        tvTotalLikes.setOnClickListener(this);
        tvTotalShare.setOnClickListener(this);
        btLike.setOnClickListener(this);
        btShare.setOnClickListener(this);
        ivFav.setOnClickListener(this);
        containerBlogItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_total_share:
            case R.id.bt_share:
//                mActivity.shareText(mActivity.getResources().getString(R.string.share_healthcoco_subject),
//                        mActivity.getResources().getString(R.string.share_healthcoco_message)
//                                + mActivity.getResources().getString(R.string.link_bit_share));
                break;
            case R.id.tv_total_likes:
            case R.id.bt_like:
//                if (patientDetails1 != null)
//                    performLikeOperation(patientDetails1.getUserId());
//                else {
//                    mActivity.openLoginSignupActivity(HealthCocoConstants.REQUEST_CODE_DOCTOR_PROFILE);
//                    Util.showToast(mActivity, R.string.please_login_first);
//                }
                break;
            case R.id.iv_fav:
//                if (patientDetails2 != null)
//                    performFavoritesOperation(patientDetails2.getUserId());
//                else {
//                    mActivity.openLoginSignupActivity(HealthCocoConstants.REQUEST_CODE_DOCTOR_PROFILE);
//                    Util.showToast(mActivity, R.string.please_login_first);
//                }
                break;
            case R.id.container_blog_item:
                openBlogDetailFragment();
                break;
        }
    }

//    private void performFavoritesOperation(String userId) {
//        if (objData != null)
//            if (objData.isFavourite())
//                showFavoritesConfirmationAlert(LikeType.UNFAVORITE, userId);
//            else
//                showFavoritesConfirmationAlert(LikeType.FAVORITE, userId);
//    }
//
/*
    private void showFavoritesConfirmationAlert(final LikeType likeType, final String selectedPatientUserId) {
        final android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(likeType.getTitle());
        alertBuilder.setMessage(mActivity.getResources().getString(likeType.getMessage()));
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mActivity.showLoading(false);
                WebDataServiceImpl.getInstance(mApp).addToFavorite(Boolean.class, objData.getUniqueId(), selectedPatientUserId,
                        CommonHealthFeedsViewHolder.this, CommonHealthFeedsViewHolder.this);
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
*/

    private void performLikeOperation(String userId) {
//        if (objData != null)
//            if (objData.isliked())
//                showLikesConfirmationAlert(R.string.confirm_unlike, R.string.confirm_unlike_blog_message, userId);
//            else
//                showLikesConfirmationAlert(R.string.confirm_like, R.string.confirm_like_blog_message, userId);
    }

    private void showLikesConfirmationAlert(int title, int message, final String selectedPatientUserId) {
//        mActivity.showLoading(false);
//        WebDataServiceImpl.getInstance(mApp).likeTheBlog(HealthcocoBlogResponse.class, objData.getUniqueId(), selectedPatientUserId,
//                CommonHealthFeedsViewHolder.this, CommonHealthFeedsViewHolder.this);
    }

    private void openBlogDetailFragment() {
        mActivity.openCommonOpenUpActivity(CommonOpenUpFragmentType.BLOG_DETAIL, BlogDetailFragment.TAG_UNIQUE_ID, objData.getUniqueId(), 0);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case ADD_TO_FAVORITE:
                    if (response.getData() instanceof Boolean) {
                        boolean isLiked = (boolean) response.getData();
                        objData.setFavourite(isLiked);
                        if (isLiked)
                            setFavoriteSelected(true);
                        else
                            setFavoriteSelected(false);
                    }
                    try {
                        Intent intent = new Intent(CommonHealthFeedsFragment.INTENT_REFRESH_FAV_BLOGS);
                        intent.putExtra(CommonHealthFeedsFragment.TAG_BLOG_UNIQUE_ID, objData.getUniqueId());
                        LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    Util.sendBroadcast(mApp, CommonHealthFeedsFragment.INTENT_REFRESH_FAV_BLOGS);
                    Util.sendBroadcast(mApp, CommonHealthFeedsFragment.INTENT_REFRESH_BLOGS);
                    break;
                case LIKE_THE_BLOG:
                    if (response.getData() != null && response.getData() instanceof HealthcocoBlogResponse) {
                        HealthcocoBlogResponse responseData = (HealthcocoBlogResponse) response.getData();
                        objData.setNoOfLikes(responseData.getNoOfLikes());
                        objData.setIsliked(responseData.isliked());
                        applyData();
                    }
                    break;
                default:
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        mActivity.hideLoading();
    }

    private void setLikesSelected(boolean isSelected) {
        btLike.setSelected(isSelected);
    }

    private void setFavoriteSelected(boolean isSelected) {
        ivFav.setSelected(isSelected);
    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        loadingGif.setVisibility(View.GONE);
    }
}
