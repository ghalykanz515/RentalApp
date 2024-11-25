package com.example.rentalapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    private static final String EXTRA_URL = "URL";

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webView = findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        String url = getIntent().getStringExtra(EXTRA_URL);
        if (url != null) {
            webView.loadUrl(url);
        }
    }
}