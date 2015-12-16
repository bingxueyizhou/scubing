package com.scubing.ui;

import java.io.IOException;

import com.scubing.app.Evaluation;
import com.scubing.app.Evaluation.onEvalListener;
import com.scubing.data.TheAccount;
import com.scubing.data.VIPList;
import com.scufy.scubing.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainEvaluation extends Activity{
	//controls
	private Button buGo;
	private TextView tvMain;
	private TextView tvMore;
	//global value
	private static Context instance; 
	private TheAccount mainAccount;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mapp_evaluation);
		initControls();
		
		setListener();
	}
	
	/**
	 * set the Listener for all of the controls
	 */
	private void setListener() {
		buGo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				VIPList vipList = null;
				disableBuGo();
				try {
					vipList = new VIPList(instance);
				} catch (IOException e) {
					Toast.makeText(instance, "读取VIP列表出错", 
							Toast.LENGTH_SHORT).show();
					enableBuGo();
					return;
				} catch (Exception e) {
					Toast.makeText(instance, "VIP获取失败", 
							Toast.LENGTH_SHORT).show();
					enableBuGo();
					return;
				}
				
				//*
				if (!vipList.isVIP(mainAccount.getStdId() )  ){
					Toast.makeText(instance, "由于一些政策或其他原因该功能已经删除", 
							Toast.LENGTH_SHORT).show();
					enableBuGo();
					return;
				}
				if (!mainAccount.getLoginStatus()){
					Toast.makeText(MainEvaluation.this, "未验证学号", 
							Toast.LENGTH_SHORT).show();
					enableBuGo();
					return ;
				}else{
					Evaluation eval = new Evaluation(
											instance, 
											mainAccount.getStdId(), 
											mainAccount.getJWCPwd());
					eval.setEvalListener(new onEvalListener() {
						
						@Override
						public void onPerform(int i,String lesson, 
								String teacher, boolean ok) {
							String StrEnable = "";
							if (ok){
								StrEnable = "成功";
							}else{
								StrEnable = "失败";
							}
							
							String StrLine = "("+i+")"+lesson+"-"+
											 teacher+"["+StrEnable+"]\n";
							String StrAll  = tvMore.getText().toString();
							tvMore.setText(StrAll+StrLine);
						}

						@Override
						public void onStat(int index, int success, int fail,
								int sum) {
							tvMain.setText("评价:"+success+"\n"+
											"失败:"+fail+"\n"+
											"总计:"+sum);
							
						}
						
						@Override
						public void onFinished(boolean ok, String msg) {
							if(!ok) tvMain.setText(msg);
							enableBuGo();
						}
					});
				}
				//*/
			}
		});
	}
	
	/**
	 * initialize all the controls
	 */
	private void initControls(){
		buGo = (Button)findViewById(R.id.mapp_eval_main_bu);
		tvMain = (TextView)findViewById(R.id.mapp_eval_main_tV);
		tvMore = (TextView)findViewById(R.id.mapp_eval_more_tV);
		
		MainEvaluation.instance = this;
		mainAccount = new TheAccount(this);
	}
	
	private void enableBuGo(){
		buGo.setEnabled(true);
		buGo.setText("一键评教");
	}
	
	private void disableBuGo(){
		buGo.setText("评教中...");
		buGo.setEnabled(false);
	}
	
}
