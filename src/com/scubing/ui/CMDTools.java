package com.scubing.ui;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

import com.scufy.scubing.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class CMDTools extends Activity{
	//control
	private TextView main_content;
	private EditText main_input;

	private final int MSG_SUCCESS = 0;
	private final int MSG_FAIL	  = 1;
	private final int MSG_OUTTIME = 2;
	
	@SuppressLint("HandlerLeak")
	private Handler uihandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_SUCCESS:
				reValueText(msg.obj.toString());
				break;
			case MSG_FAIL:
				reValueText("command fail\n"+msg.obj.toString());
				break;
			case MSG_OUTTIME:
				reValueText("command out time");
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mapp_cmd);
		
		initialize();
		
		setListener();
	}
	
	private void initialize(){
		main_content = (TextView)findViewById(R.id.cmd_tV_content);
		main_input   = (EditText)findViewById(R.id.cmd_eT_input);
		
		reValueText(main_content.getText().toString());
	}
	
	private void reValueText(String s){
		SpannableString msp = new SpannableString(s);
		Log.i("cmd_info",s);
		msp.setSpan(new TypefaceSpan("msyh.TTF"), 0, s.length(), 
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		main_content.setText(msp);
	}

	private void setListener() {
		main_input.setOnKeyListener(OnEnter);
	}
	
	/**
	 * exc linux shell
	 * @param c
	 * @return
	 */
	private String exec(String c){
		Message msg = new Message();
		try { 
            Process process = Runtime.getRuntime().exec(c);
            
            LineNumberReader br = new LineNumberReader(new InputStreamReader(  
                    process.getInputStream()));  
            StringBuffer sb = new StringBuffer();  
            String line;
            while ((line = br.readLine()) != null) {  
                System.out.println(line);  
                sb.append(line).append("\n");  
            }

            process.waitFor();
			msg.obj = sb.toString();
			msg.what = MSG_SUCCESS;
			uihandler.sendMessage(msg);
			
            return sb.toString();  
        } catch (Exception e) {  
            e.printStackTrace();
            
			msg.obj = e.toString();
			msg.what = MSG_FAIL;
			uihandler.sendMessage(msg);
			
            return e.toString();
        }
	}
	
	private OnKeyListener OnEnter = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			
			if(keyCode == KeyEvent.KEYCODE_ENTER){  
				InputMethodManager imm = (InputMethodManager)
						v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
				  
				if(imm.isActive()){  
					imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );  
				}
				/*
				Toast.makeText(CMDTools.this, main_input.getText().toString(),
						Toast.LENGTH_SHORT).show();*/
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						exec(main_input.getText().toString());
					}
				}).start();
				return true;
			} 
			return false;
		}
	};
}