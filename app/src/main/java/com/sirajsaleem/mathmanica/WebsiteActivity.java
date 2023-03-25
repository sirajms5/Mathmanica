package com.sirajsaleem.mathmanica;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebsiteActivity extends AppCompatActivity implements MethodsFactory {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        findViews();

        if (getIntent().getBooleanExtra("back", false)) {
            overridePendingTransition(R.anim.navigation_back_slide_in, R.anim.navigation_back_slide_out);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("sirajsaleem.com");
        }

        webView.loadUrl("https://sirajsaleem.com");
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void findViews() {
        webView = findViewById(R.id.webView);
    }

    @Override
    public void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("back", true);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Pattern pattern = Pattern.compile("https://sirajsaleem\\.com/#.*");
        Matcher matcher = pattern.matcher(webView.getUrl());
        if (matcher.matches()) {
            goBack();
        } else {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                goBack();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Pattern pattern = Pattern.compile("https://sirajsaleem\\.com/#.*");
            Matcher matcher = pattern.matcher(webView.getUrl());
            if (matcher.matches()) {
                goBack();
            } else {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    goBack();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}