package com.yarncalc.app;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView web;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        prefs = getSharedPreferences("ui", MODE_PRIVATE);
        web = new WebView(this);
        setContentView(web);
        applyBars(prefs.getBoolean("dark", true));

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(false);
        s.setAllowFileAccessFromFileURLs(false);
        s.setAllowUniversalAccessFromFileURLs(false);
        s.setBlockNetworkLoads(true);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);

        web.addJavascriptInterface(new Bridge(), "Android");
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r) {
                return !r.getUrl().toString().startsWith("file:///android_asset/");
            }
        });

        if (saved != null) web.restoreState(saved);
        else web.loadUrl("file:///android_asset/index.html");
    }

    private void applyBars(boolean dark) {
        int bg = dark ? 0xFF141218 : 0xFFFEF7FF;
        Window w = getWindow();
        w.clearFlags(0x04000000 | 0x08000000);
        w.addFlags(0x80000000);
        w.setStatusBarColor(bg);
        w.setNavigationBarColor(bg);
        w.getDecorView().setSystemUiVisibility(dark ? 0 : (0x2000 | 0x10));
        if (web != null) web.setBackgroundColor(bg);
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) web.goBack();
        else super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        web.saveState(out);
    }

    @Override
    protected void onPause() { super.onPause(); web.onPause(); }

    @Override
    protected void onResume() { super.onResume(); web.onResume(); }

    @Override
    protected void onDestroy() {
        if (web != null) { web.removeJavascriptInterface("Android"); web.destroy(); }
        super.onDestroy();
    }

    private class Bridge {
        @JavascriptInterface
        public void setTheme(final String theme) {
            runOnUiThread(new Runnable() {
                public void run() {
                    boolean dark = "dark".equals(theme);
                    prefs.edit().putBoolean("dark", dark).apply();
                    applyBars(dark);
                }
            });
        }

        @JavascriptInterface
        public void copy(String text) {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) cm.setPrimaryClip(ClipData.newPlainText("Yarn Calculator", text));
        }
    }
}
