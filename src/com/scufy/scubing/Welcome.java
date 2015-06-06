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
	/*�߳̿����ź�*/
	final int MSG_GO 	= 1;	//�����¸�Activity
	final int MSG_CRASH = 2;	//����
	final int MSG_WEL 	= 3;	//��ӭ
	
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
		Toast.makeText(this, "出错", Toast.LENGTH_LONG).show();
		this.finish();
	}
	
	private void showWel(){
		Toast.makeText(this, "--冰雪一舟--", Toast.LENGTH_LONG).show();
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
