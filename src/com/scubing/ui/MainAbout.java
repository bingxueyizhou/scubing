package com.scubing.ui;


/**
 * @author 邹兵
 */


import com.scufy.scubing.R;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class MainAbout extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_about);
		
		TextView version = (TextView)findViewById(R.id.tV_about_version);
		try {
	        PackageManager manager = this.getPackageManager();
	        PackageInfo info = manager.getPackageInfo( this.getPackageName(), 0);
			version.setText("版本号: v"+info.versionName);
	    } catch (Exception e) {
	        e.printStackTrace();
			version.setText("版本号: 0");
	    }
	}
	
}
