package com.scufy.scubing.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.scufy.scubing.data.TheAccount;

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
					break;
				}
			}
		};
	
	
	public Evaluation(Context c){
		this.cxt = c;
	}
	
	public void start(){
		new Thread(new Crawler()).start();
		Log.i("aa","enter");
	}
	
	class Crawler implements Runnable{
		
		public Crawler() {}
		
		@Override
		public void run() {
	        BufferedReader in = null;
			PrintWriter   out = null;
	        String line;
	        URLConnection connection;
	        
	        HttpResponse httpResponse = null;
	        HttpEntity httpEntity = null;
	        InputStream inputStream = null;
	        HttpClient httpclient = new DefaultHttpClient();
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	        
			//-------login----------
			String log_url 		= "http://202.115.47.141//loginAction.do";
			String log_params 	= "zjh=" + new TheAccount(cxt).getStdId() +
								  "&mm=" + new TheAccount(cxt).getJWCPwd();
			
	        HttpPost httppost = new HttpPost(log_url);
            
	        try {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.ASCII));
				httpResponse	= httpclient.execute(httppost);
				httpEntity 		= httpResponse.getEntity();
				if (httpResponse.getStatusLine().getStatusCode() ==200){
					is_logined = true;
					Log.i("200", "网络可以");
				}else{
					Log.i("~200", "网络不好");
				}
				
			} catch (Exception e) {
				Log.i("错误", "获取出错");
				e.printStackTrace();
			}
	        
	        if (!is_logined) return;
			

			//---------get some data---------
			String list_res = "";
			httppost = new HttpPost("http://202.115.47.141//jxpgXsAction.do?pageSize=300");
	        try {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
				httpResponse = httpclient.execute(httppost);
				httpEntity 	 = httpResponse.getEntity();
				
				if (httpResponse.getStatusLine().getStatusCode() == 200){
					
					inputStream = httpEntity.getContent();
					
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(inputStream,"GB2312"));
					
					while((line = reader.readLine())!=null){
						list_res += line;
					}
			        
					System.out.println(list_res);
					int index = 0;
					int end   = 0;
					int i_bool= 0;
					while(true){
						index = list_res.indexOf("<img name=",index);
						end   = list_res.indexOf("\" style",index);
						//i_bool= result.lastIndexOf("否",index);
						if (index < 0 || end < 0) break;
						
						//if (index - i_bool == 40 && i_bool>0){
							mainList.add(list_res.substring(index+11,end));
						//}
						index = end;
					}
				}
				is_list_success = true;
				Log.i("读取列表", "成功");
			} catch (IOException e) {
				Log.w("读取列表","失败");
				is_list_success = false;
				e.printStackTrace();
			}
			
			if (!is_list_success) return;
			//--------deal num data--------
			sum_all = mainList.size();
			System.out.println(mainList);
			
			//---------post data-----------
			String res="";
			Message msg = new Message();
			msg.what = MSG_FINISH_EXEC;
			for(int i=0;i<mainList.size();i++){
				String[] r = mainList.get(i).split("#@");
				try {
					String post_url   = "http://202.115.47.141/jxpgXsAction.do?oper=wjpg";
					String post_params= "wjbm="+r[0]+
										"bpr="+r[1]+
										"pgnr="+r[2]+
										"0000000005=10_1"+
										"0000000006=10_1"+
										"0000000007=10_1"+
										"0000000008=10_1"+
										"0000000009=10_1"+
										"0000000010=10_1"+
										"0000000035=10_1"+
										"zgpj=I Like IT!";
					
					httppost = new HttpPost(post_url);
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
					httpResponse = httpclient.execute(httppost);
					httpEntity 	 = httpResponse.getEntity();
					
					
					
					Log.i("result", res+"\n\nsize:"+res.length());
				} catch (IOException e) {
					Log.w("result", e.toString());
					msg.what = MSG_UNDEFINE_ERROR;
					e.printStackTrace();
				}
			}

			exechandler.sendMessage(msg);
		}
	}
}
