package com.scufy.scubing;

import com.scufy.scubing.app.MainView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

public class Welcome extends Activity {
	/*handle message*/
	final int MSG_GO 	= 1;	//if activity go
	final int MSG_CRASH = 2;	//if crash
	final int MSG_WEL 	= 3;	//if to welcome
	
	private final long waitTime = 1000;
	

	@SuppressLint("HandlerLeak")
	private Handler uihandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_GO:
				AppStart();
				break;
			case MSG_CRASH:
				showCrash();
				break;
			case MSG_WEL:
				showWel();
				break;
			}
		}
	};
	
	private void showCrash(){
		Toast.makeText(this, "程序出错", Toast.LENGTH_LONG).show();
		this.finish();
	}
	
	private void showWel(){
		//Toast.makeText(this, "--qvbrgw--", Toast.LENGTH_LONG).show();
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();  
	            msg.what = MSG_GO;
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {
					msg.what = MSG_CRASH;
					e.printStackTrace();
				}finally{
					uihandler.sendMessage(msg);
				}
			}
		}).start();
	}
	
	private void AppStart() {
		showWel();
		Intent intent =new Intent(Welcome.this,MainView.class);
		startActivity(intent);
		this.finish();
	}

}
