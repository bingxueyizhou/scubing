package com.scubing.app;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.util.MHttpClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Evaluation {
	
	@SuppressWarnings("unused")
	private Context cxt;
	private int sum_success = 0;
	private int sum_fail 	= 0;
	private int sum_all		= 0;
	private String std		= "";
	private String key		= "";
	
	private boolean is_list_success = false;
	private boolean is_logined 		= false;
	private onEvalListener listener;
	
	private ArrayList<String> mainList = new ArrayList<String>();
	
	
	/*loging status
	 * 0-未变化 1-成功 2-失败 3-没有网络 4-未知错误
	 */
	//handler message
	private final int MSG_FINISHED = 1;
	private final int MSG_PERFORM  = 2;
	private final int MSG_STAT	   = 3;
	@SuppressLint("HandlerLeak")
	private Handler uihandler = new Handler(){	
		public void handleMessage(Message msg){
			Bundle data = msg.getData();
			switch(msg.what){
			case MSG_FINISHED:
				listener.onFinished(data.getBoolean("ok"), 
									data.getString("msg"));
				break;
			case MSG_PERFORM:
				listener.onPerform(	data.getInt("index"), 
									data.getString("lesson"), 
									data.getString("teacher"), 
									data.getBoolean("ok"));
				break;
			case MSG_STAT:
				listener.onStat(	data.getInt("index"), 
									data.getInt("success"), 
									data.getInt("fail"),
									data.getInt("sum"));
				break;
			}
		}
	};
	
	
	public interface onEvalListener{
		/**
		 * 当抓取结束的回调函数
		 */
		public void onFinished(boolean ok,String msg);
		
		/**
		 * 评教完一个后的回调
		 */
		public void onPerform(int index,String lesson,String teacher,boolean ok);
		
		/**
		 * 评教过程中的统计回调
		 */
		public void onStat(int index,int success,int fail,int sum);
		
	}
	
	/**
	 * The Class of Evaluation
	 * @param c The Main Context
	 * @param u the student id
	 * @param k	the student password
	 */
	public Evaluation(Context c,String u,String k){
		this.cxt = c;
		this.std = u;
		this.key = k;
	}
	
	@SuppressWarnings("unused")
	private Thread getOperation(){
		return new Thread(new Crawler());
	}
	
	public void setEvalListener(onEvalListener listener){
		this.listener = listener;
		new Thread(new Crawler()).start();
	}
	
	class Crawler implements Runnable{
		
		@Override
		public void run() {
			MHttpClient client   = new MHttpClient(new DefaultHttpClient());
        	Message finalMsg = new Message();
			
			//-------login----------
			String log_url 		= "http://202.115.47.141//loginAction.do";
			String log_params 	= "zjh=" + std +
								  "&mm=" + key;
			
			String log_res 		= client.doGet(log_url, log_params);
			System.out.println(log_res);
			if (log_res.length() < 4000){
				if (log_res.length() < 800 && log_res.length() > 1){
					is_logined = true;
				}
			}
					
	        if (!is_logined){
	        	finalMsg.what = MSG_FINISHED;
	        	Bundle data = new Bundle();
	        	data.putBoolean("ok", false);
	        	data.putString("msg", "模拟登录失败");
	        	finalMsg.setData(data);
				uihandler.sendMessage(finalMsg);
				return;
			}

			//---------get some data---------
			String list_res = client.doGet("http://202.115.47.141/jxpgXsAction.do",
											"oper=listWj&pageSize=300");
			        
			//System.out.println(list_res);
			if (list_res.indexOf("数据库忙请稍候再试") < 0 && list_res.length() > 1){
				is_list_success = true;
				Log.i("读取列表", "成功");
			}else{
				is_list_success = false;
				Log.w("读取列表","失败");
			}
			int index = 0;
			int end   = 0;
			int i_bool= 0;
			
			while(true){
				index = list_res.indexOf("<img name=",index);
				end   = list_res.indexOf("\" style",index);
				i_bool= list_res.lastIndexOf("否",index);
				if (index < 0 || end < 0) break;
				
				if (index - i_bool < 50 && i_bool>0){
					mainList.add(list_res.substring(index+11,end));
				}
				index = end;
			}
			
			if (!is_list_success){
				finalMsg.what = MSG_FINISHED;
	        	Bundle data = new Bundle();
	        	data.putBoolean("ok", true);
	        	data.putString("msg", "未定义错误");
	        	finalMsg.setData(data);
				uihandler.sendMessage(finalMsg);
				return;
			}
			//--------deal num data--------
			sum_all = mainList.size();
			//System.out.println(mainList);
			
			//---------post data-----------
			String last_res="";
			String post_url   = "http://202.115.47.141/jxpgXsAction.do?oper=wjpg";
			ArrayList<NameValuePair> post_params;
			String form_url   = "http://202.115.47.141/jxpgXsAction.do";
			ArrayList<NameValuePair> form_params;
			Log.i("消息:","a:"+sum_all);
			for(int i=0;i<mainList.size();i++){
				String[] r = mainList.get(i).split("#@");
				
				form_params = new ArrayList<NameValuePair>();
				form_params.add(new BasicNameValuePair("wjbm" , r[0]));
				form_params.add(new BasicNameValuePair("bpr"  , r[1]));
				form_params.add(new BasicNameValuePair("bprm" , r[2]));
				form_params.add(new BasicNameValuePair("wjmc" , r[3]));
				form_params.add(new BasicNameValuePair("pgnrm", r[4]));
				form_params.add(new BasicNameValuePair("pgnr" , r[5]));
				form_params.add(new BasicNameValuePair("oper" , "wjShow"));
	            
				client.doPost(form_url, form_params);
	                
				post_params = new ArrayList<NameValuePair>();
				post_params.add(new BasicNameValuePair("wjbm",r[0]) );
				post_params.add(new BasicNameValuePair("bpr", r[1]) );
				post_params.add(new BasicNameValuePair("pgnr",r[5]) );
				post_params.add(new BasicNameValuePair("zgpj", "Great") );
				
				if (r[1].indexOf("zj") < 0){
					post_params.add(new BasicNameValuePair("0000000005","10_1") );
					post_params.add(new BasicNameValuePair("0000000006","10_1") );
					post_params.add(new BasicNameValuePair("0000000007","10_1") );
					post_params.add(new BasicNameValuePair("0000000008","10_1") );
					post_params.add(new BasicNameValuePair("0000000009","10_1") );
					post_params.add(new BasicNameValuePair("0000000010","10_1") );
					post_params.add(new BasicNameValuePair("0000000035","10_1") );
				}else{
					post_params.add(new BasicNameValuePair("0000000028","10_1") );
					post_params.add(new BasicNameValuePair("0000000029","10_1") );
					post_params.add(new BasicNameValuePair("0000000030","10_1") );
					post_params.add(new BasicNameValuePair("0000000031","10_1") );
					post_params.add(new BasicNameValuePair("0000000032","10_1") );
					post_params.add(new BasicNameValuePair("0000000033","10_1") );
				}
				
				
				/*p = URLEncoder.encode(p,"UTF-8");*/
				//System.out.println(post_url+"?"+p);
				
				last_res = client.doPost(post_url, post_params);
				
				Message perMsg  = new Message();
				Message statMsg = new Message();
				Bundle perData  = new Bundle();
				Bundle statData = new Bundle();
				perMsg.what  = MSG_PERFORM;
				statMsg.what = MSG_STAT;
				if (last_res.indexOf("评估成功") > 0){
					perData.putBoolean("ok", true);
					sum_success ++;
				}else{
					perData.putBoolean("ok", false);
					sum_fail ++;
				}
				perData.putInt("index", i);
				perData.putString("lesson", r[0]);
				perData.putString("teacher", r[1]);
				perMsg.setData(perData);
				uihandler.sendMessage(perMsg);
				
				statData.putInt("index", i);
				statData.putInt("success", sum_success);
				statData.putInt("fail", sum_fail);
				statData.putInt("sum", sum_all);
				statMsg.setData(statData);
				uihandler.sendMessage(statMsg);

				//Log.i("result", last_res+"\n\nsize:"+last_res.length());//失败-286
				try {
					Thread.sleep(100);
				} catch (Exception e) {}
			}
			if(sum_all == 0){
				Message statMsg = new Message();
				Bundle statData = new Bundle();
				statMsg.what = MSG_STAT;
				statData.putInt("index", 0);
				statData.putInt("success", 0);
				statData.putInt("fail", 0);
				statData.putInt("sum", 0);
				statMsg.setData(statData);
				uihandler.sendMessage(statMsg);
			}
        	finalMsg.what = MSG_FINISHED;
        	Bundle data = new Bundle();
        	data.putBoolean("ok", true);
        	data.putString("msg", "成功");
        	finalMsg.setData(data);
			uihandler.sendMessage(finalMsg);
			//msg.what = MSG_FINISH_EXEC;
			//exechandler.sendMessage(msg);
		}
	}
}
