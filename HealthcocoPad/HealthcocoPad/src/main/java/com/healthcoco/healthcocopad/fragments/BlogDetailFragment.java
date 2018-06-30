package com.healthcoco.healthcocopad.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.HealthcocoBlogResponse;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.MetricsLightTextView;

import org.parceler.Parcels;

/**
 * Created by Shreshtha on 25-Sep-17.
 */

public class BlogDetailFragment extends HealthCocoFragment implements Response.Listener<VolleyResponseBean>,
        GsonRequest.ErrorListener {
    public static final String TAG_UNIQUE_ID = "blogId";
    private String blogId;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private MetricsLightTextView tvBlogArticle;
    private ImageView ivBlogImage;
    private HealthcocoBlogResponse healthcocoBlogResponse;
    private MetricsLightTextView tvCreatedByName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_health_blog_detail, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = mActivity.getIntent();
        blogId = Parcels.unwrap(intent.getParcelableExtra(TAG_UNIQUE_ID));
//        blogId = intent.getStringExtra(TAG_UNIQUE_ID);
        if (!Util.isNullOrBlank(blogId)) init();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        mActivity.getMenuInflater().inflate(R.menu.menu_blog_detail_screen, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mActivity.finish();
                return true;
//            case R.id.item_share:
//                mActivity.shareText(mActivity.getResources().getString(R.string.share_healthcoco_subject),
//                        mActivity.getResources().getString(R.string.share_healthcoco_message)
//                                + mActivity.getResources().getString(R.string.link_bit_share));
//                return false;
//            case R.id.item_fav:
//                RegisteredPatientDetails patientDetails2 = ((CommonOpenUpActivity) mActivity).getSelectedRegisteredPatientDetails();
//                if (patientDetails2 != null)
//                    performFavoritesOperation();
//                else {
//                    openLoginSignupActivity(HealthCocoConstants.REQUEST_CODE_DOCTOR_PROFILE);
//                    Util.showToast(mActivity, R.string.please_login_first);
//                }
//                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        getBlogDetail();
    }

    private void getBlogDetail() {
        String selectedPatientUserId = null;
//        selectedPatient = ((CommonOpenUpActivity) mActivity).getSelectedRegisteredPatientDetails();
//        if (selectedPatient != null)
//            selectedPatientUserId = selectedPatient.getUserId();
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).getHealthBlogDetail(HealthcocoBlogResponse.class, blogId, selectedPatientUserId, this, this);
    }

    @Override
    public void initViews() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        //initialising toolbar views
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvBlogArticle = (MetricsLightTextView) view.findViewById(R.id.tv_blog_article);
        ivBlogImage = (ImageView) view.findViewById(R.id.iv_blog_image);
        tvCreatedByName = (MetricsLightTextView) view.findViewById(R.id.tv_created_by_name);
    }

    @Override
    public void initListeners() {

    }

    private void initData(HealthcocoBlogResponse healthcocoBlogResponseData) {
        if (healthcocoBlogResponseData != null) {
            collapsingToolbarLayout.setTitle(Util.getValidatedValue(healthcocoBlogResponse.getTitle()));

            //set image
            if (!Util.isNullOrBlank(healthcocoBlogResponseData.getTitleImage()))
                DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivBlogImage, healthcocoBlogResponseData.getTitleImage());
            tvCreatedByName.setText("By " + Util.getValidatedValue(healthcocoBlogResponseData.getPostBy()));

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tvBlogArticle.setText(Html.fromHtml(healthcocoBlogResponseData.getArticle(), Html.FROM_HTML_MODE_LEGACY));
            } else tvBlogArticle.setText(Html.fromHtml(healthcocoBlogResponseData.getArticle()));

//            if (healthcocoBlogResponseData.isFavourite())
//                setFavoriteSelected(true);
//            else setFavoriteSelected(false);
        }
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case GET_HEALTH_BLOG_BY_ID:
                    if (response.getData() != null && response.getData() instanceof HealthcocoBlogResponse) {
                        healthcocoBlogResponse = (HealthcocoBlogResponse) response.getData();
                        initData(healthcocoBlogResponse);
                    }
                    break;
                case ADD_TO_FAVORITE:
                    if (response.getData() instanceof Boolean) {
                        boolean isLiked = (boolean) response.getData();
                        healthcocoBlogResponse.setFavourite(isLiked);
                        initData(healthcocoBlogResponse);
                        try {
                            Intent intent = new Intent(CommonHealthFeedsFragment.INTENT_REFRESH_FAV_BLOGS);
                            intent.putExtra(CommonHealthFeedsFragment.TAG_BLOG_UNIQUE_ID, healthcocoBlogResponse.getUniqueId());
                            LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Util.sendBroadcast(mApp, CommonHealthFeedsFragment.INTENT_REFRESH_BLOGS);
                    }
                    break;
            }
            mActivity.hideLoading();
        }
    }

 /*   private void setFavoriteSelected(boolean isSelected) {
        MenuItem item = toolbar.getMenu().findItem(R.id.item_fav);
        if (isSelected)
            item.setIcon(R.drawable.ic_fav_selected);
        else item.setIcon(R.drawable.ic_fav_white);
//        item.setChecked(isSelected);
    }*/

   /* private void performFavoritesOperation() {
        if (healthcocoBlogResponse != null)
            if (healthcocoBlogResponse.isFavourite())
                showFavoritesConfirmationAlert(LikeType.UNFAVORITE);
            else
                showFavoritesConfirmationAlert(LikeType.FAVORITE);
    }*/

   /* private void showFavoritesConfirmationAlert(final LikeType likeType) {
        final android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(likeType.getTitle());
        alertBuilder.setMessage(mActivity.getResources().getString(likeType.getMessage()));
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mActivity.showLoading(false);
                WebDataServiceImpl.getInstance(mApp).addToFavorite(Boolean.class, healthcocoBlogResponse.getUniqueId(), HealthcocoActivity.SELECTED_PATIENT_ID, BlogDetailFragment.this, BlogDetailFragment.this);
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }*/
}
