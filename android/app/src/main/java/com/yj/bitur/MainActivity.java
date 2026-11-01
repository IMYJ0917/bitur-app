package com.yj.bitur;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/** Bitur 폰 앱 껍데기. 화면은 GitHub Pages에서 불러오므로 화면을 고쳐도 APK를 다시 받지 않아도 된다. 토큰은 넣지 않는다. */
public class MainActivity extends Activity {
    private static final String HOME = "https://imyj0917.github.io/bitur-app/";
    private WebView web;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        web = new WebView(this);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r) {
                Uri u = r.getUrl();
                if (u.toString().startsWith(HOME)) return false;
                try {   // 전화·문자·메일·지도·홈페이지는 폰의 다른 앱으로
                    startActivity(new Intent(Intent.ACTION_VIEW, u));
                } catch (ActivityNotFoundException e) {
                    return true;
                }
                return true;
            }
        });
        setContentView(web);
        if (state != null) web.restoreState(state);
        else web.loadUrl(startUrl(getIntent()));
    }

    private String startUrl(Intent i) {
        Uri u = i == null ? null : i.getData();
        if (u != null && "bitur".equals(u.getScheme()) && u.getFragment() != null) return HOME + "#" + u.getEncodedFragment();
        return HOME;
    }

    @Override
    protected void onNewIntent(Intent i) {
        super.onNewIntent(i);
        if (i.getData() != null) web.loadUrl(startUrl(i));
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        web.saveState(out);
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) web.goBack();
        else super.onBackPressed();
    }
}
