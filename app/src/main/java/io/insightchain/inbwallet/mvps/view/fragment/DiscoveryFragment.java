package io.insightchain.inbwallet.mvps.view.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import io.insightchain.inbwallet.R;
import io.insightchain.inbwallet.base.BaseMvpFragment;
import io.insightchain.inbwallet.mvps.http.ApiServiceHelper;

public class DiscoveryFragment extends BaseMvpFragment {

    @BindView(R.id.webview)
    WebView mWebView;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_discovery;
    }

    @Override
    protected void init() {
        loadWebView();
    }

    private void loadWebView() {
        mWebView.setVisibility(View.VISIBLE);
        //启用支持Javascript
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.getSettings().setUserAgentString(ApiServiceHelper.getUserAgent());
        settings.setSupportZoom(false);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.requestFocus();
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //https与http混合资源处理,android5.0开始不能同时加载http和https资源
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //页面加载
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    mProgress.setVisibility(View.GONE);
////                    mWebView.loadUrl("getEToken('"+LocalData.getInstance().getEtoken()+"')");
//                } else {
//                    mProgress.setVisibility(View.VISIBLE);
//                    mProgress.setProgress(newProgress);
//                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                if(!TextUtils.isEmpty(mTitle)){
//                    mTopBar.setTitle(mTitle);
//                }else if (!TextUtils.isEmpty(title)) {
//                    mTopBar.setTitle(title);
//                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                view.getSettings().setUserAgentString(ApiServiceHelper.getUserAgent());
//                view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            //scheme跳转，重写shouldOverrideUrlLoading方法，scheme不起作用
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.getSettings().setUserAgentString(ApiServiceHelper.getUserAgent());
//                view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        mWebView.loadUrl("https://api-ropsten.etherscan.io/api?module=account&action=txlist&sort=desc&address=0xaa18a055AB2017a0Cd3fB7D70f269C9B80092206");

        //WebView加载页面优先使用缓存加载
//        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

    }
}
