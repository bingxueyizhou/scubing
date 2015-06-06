package com.scufy.scubing.app;


/**
 * @author 邹兵
 */

import com.scufy.scubing.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class MainAbout extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_about);
		
	}
	
}
