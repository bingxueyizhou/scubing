package com.scufy.scubing.app;

import com.scufy.scubing.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainGpa extends Activity{
	
	private WebView 		web_gpa;
	private LinearLayout    refresh_bar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mapp_gpa);
		
		//initial controls
		initialize();
		//load some source
		onLoad();
		//set controls' listener
		setListener();
	}

	private void setListener() {
		refresh_bar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainGpa.this, "刷新中", Toast.LENGTH_SHORT).show();
				web_gpa.reload();
			}
		});
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void onLoad() {
		web_gpa.getSettings().setJavaScriptEnabled(true);
		web_gpa.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		web_gpa.loadUrl("http://gpa.fyscu.com/");
		web_gpa.setWebViewClient(new WebViewClient(){
	           @Override
	           public boolean shouldOverrideUrlLoading(WebView view, String url) {
	               //if true,using WebView else using default web browser
	               view.loadUrl(url);
	               return true;
	           }
	          });
		//web_gpa.loadUrl("http://baidu.com");
	}

	private void initialize() {
		web_gpa 	= (WebView)findViewById(R.id.wV_app_gpa);
		refresh_bar = (LinearLayout)findViewById(R.id.title_wV_refresh);
	}
}
