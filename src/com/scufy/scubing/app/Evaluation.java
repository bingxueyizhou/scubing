package com.scufy.scubing.app;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.scufy.scubing.data.TheAccount;
import com.scufy.util.MHttpClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class Evaluation {
	
	private Context cxt;
	private int sum_success = 0;
	private int sum_fail 	= 0;
	private int sum_all		= 0;
	
	private boolean is_list_success = false;
	private boolean is_logined 		= false;
	
	private ArrayList<String> mainList = new ArrayList<String>();
	
	//handler message
	private final int MSG_LOGIN_SUCCESS  = 1;
	private final int MSG_LOGIN_FAIL 	 = 2;
	private final int MSG_NO_NETWORK	 = 3;
	private final int MSG_UNDEFINE_ERROR = 4;
	private final int MSG_FINISH_EXEC	 = 100;
	@SuppressLint("HandlerLeak")
	private Handler exechandler = new Handler(){	
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_LOGIN_SUCCESS:
				Toast.makeText(cxt, "一键评教中……", Toast.LENGTH_SHORT).show();
				break;
			case MSG_LOGIN_FAIL:
				Toast.makeText(cxt, "失败", Toast.LENGTH_SHORT).show();
				break;
			case MSG_NO_NETWORK:
				Toast.makeText(cxt, "没有网络", Toast.LENGTH_SHORT).show();
				break;
			case MSG_UNDEFINE_ERROR:
				Toast.makeText(cxt, "未知错误", Toast.LENGTH_SHORT).show();
				break;
			case MSG_FINISH_EXEC:
				Toast.makeText(cxt, "评价:"+sum_success+"条\n"+
									 "失败"+sum_fail+"条\n共"+sum_all+"条", 
									 Toast.LENGTH_SHORT).show();
				Log.e("res","评价:"+sum_success+"条\n"+
						 		"失败"+sum_fail+"条\n共"+sum_all+"条");
				break;
			}
		}
	};
	
	
	public Evaluation(Context c){
		this.cxt = c;
	}
	
	public Thread getOperation(){
		return new Thread(new Crawler());
	}
	
	class Crawler implements Runnable{
		
		public Crawler() {}
		
		@Override
		public void run() {
			MHttpClient client   = new MHttpClient(new DefaultHttpClient());
			Message msg = new Message();
			
			//-------login----------
			String log_url 		= "http://202.115.47.141//loginAction.do";
			String log_params 	= "zjh=" + new TheAccount(cxt).getStdId() +
								  "&mm=" + new TheAccount(cxt).getJWCPwd();
			
			String log_res 		= client.doGet(log_url, log_params);
			System.out.println(log_res);
			if (log_res.length() < 4000){
				if (log_res.length() < 800 && log_res.length() > 1){
					is_logined = true;
				}
			}
					
	        if (!is_logined){
	        	msg.what = MSG_LOGIN_FAIL;
				exechandler.sendMessage(msg);
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
				msg.what = MSG_UNDEFINE_ERROR;
				exechandler.sendMessage(msg);
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
				post_params.add(new BasicNameValuePair("zgpj", "IlikeIT") );
				
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
				
				if (last_res.indexOf("评估成功") > 0){
					sum_success ++;
				}else{
					sum_fail ++;
				}

				//Log.i("result", last_res+"\n\nsize:"+last_res.length());//失败-286
				try {
					Thread.sleep(100);
				} catch (Exception e) {}
			}

			msg.what = MSG_FINISH_EXEC;
			exechandler.sendMessage(msg);
		}
	}
}
