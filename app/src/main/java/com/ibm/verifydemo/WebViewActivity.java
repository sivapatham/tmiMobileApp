package com.ibm.verifydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ibm.security.verifysdk.ContextHelper;
import com.ibm.security.verifysdk.NetworkHandler;
import com.ibm.security.verifysdk.OAuthContext;

import java.util.HashMap;

import okhttp3.logging.HttpLoggingInterceptor;

public class WebViewActivity extends AppCompatActivity {

    private final String TAG = "WebViewActivity";
    private WebView webView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CookieManager.getInstance().removeAllCookies(null);

        final boolean ignoreSsl = false;
        webView = new WebView(ContextHelper.sharedInstance().getContext());
        webView.loadUrl(TrustMeInsuranceApp.getAuthUrl() + "?client_id=" + TrustMeInsuranceApp.getAppClientId() + "&response_type=code&scope=openid&redirect_uri=oidc://callback&login_hint={\"realm\":\"www.google.com\"}");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString(System.getProperty("http.agent"));
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d(TAG, "Url: " + request.getUrl());
                Log.d(TAG, "Scheme: " + request.getUrl().getScheme());

                if (!"oidc".equals(request.getUrl().getScheme())) {
                    return false;
                }

                String code = request.getUrl().getQueryParameter("code");
                NetworkHandler.sharedInstance().setLoggingInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
                OAuthContext.sharedInstance().authorize(TrustMeInsuranceApp.getTokenUrl(), TrustMeInsuranceApp.getAppClientId(), code, new HashMap<String, Object>() {{
                    put("redirect_uri", "oidc://callback");
                    put("client_secret", TrustMeInsuranceApp.getAppClientSecret());
                }}, ignoreSsl, (oAuthToken, e) -> {
                    if (e != null)  {
                        setResult(RESULT_OK,  new Intent().putExtra("error", e));
                    } else {
                        setResult(RESULT_OK,  new Intent().putExtra("token", oAuthToken));
                    }
                    if (!isFinishing()) {
                        finish();
                    }
                });
                return true;
            }
        });
        setContentView(webView);
    }
}