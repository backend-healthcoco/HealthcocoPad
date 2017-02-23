package com.healthcoco.healthcocoplus.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.enums.WebViewType;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.Util;

/**
 * Created by Shreshtha on 23-01-2017.
 */

public class WebViewFragments extends HealthCocoFragment {
    private WebViewType webViewType;
    private WebView webView;
    private ProgressBar progressLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_webview, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Util.checkNetworkStatus(mActivity);
        if (HealthCocoConstants.isNetworkOnline)
            init();
        else
            mActivity.finish();
    }

    @Override
    public void init() {
        Intent intent = mActivity.getIntent();
        int ordinal = intent.getIntExtra(HealthCocoConstants.TAG_COMMON_OPENUP_INTENT_DATA, 0);
        webViewType = WebViewType.values()[ordinal];
        if (webViewType != null) {
            initViews();
            initListeners();
            initData();
        }
    }

    @Override
    public void initViews() {
        webView = (WebView) view.findViewById(R.id.webView);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
    }

    @Override
    public void initListeners() {

    }

    private void initData() {
        webView.setWebChromeClient(new MyWebViewChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(webViewType.getWebLink());
    }

    public class MyWebViewChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressLoading.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressLoading.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressLoading.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Util.showToast(mActivity, R.string.page_no_loaded);
            mActivity.finish();
        }
    }
}
