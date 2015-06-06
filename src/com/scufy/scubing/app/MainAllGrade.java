package com.scufy.scubing.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import com.scufy.scubing.R;
import com.scufy.scubing.data.DBAdapter;
import com.scufy.scubing.data.TheAccount;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainAllGrade extends Activity{
	
	private Context cxt;
	private int info_status = 0;
	private long waiting_long = 0;
	DBAdapter theDB;
	
	
	//receive message and deal it
	private final int MSG_GET_SUCCESS = 0;
	private final int MSG_GET_FAIL = 1;
	@SuppressLint("HandlerLeak")
	private Handler uihandler = new Handler(){	
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_GET_SUCCESS:
				Toast.makeText(MainAllGrade.this, "信息获取成功", Toast.LENGTH_SHORT).show();
				showLesson();
				break;
			case MSG_GET_FAIL:
				Toast.makeText(MainAllGrade.this, "信息获取失败", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mapp_allgrade);
		
		cxt = this;
		Http_Conn();
		Waiting(5000, "请耐心等待^_^");
	}
	
	private void showLesson(){
		theDB = new DBAdapter(cxt);
		theDB.open();
		Cursor mCur= theDB.getAllTitles();
		double allPoint = 0.0;
		
		ListView LV=(ListView)findViewById(R.id.mapp_allgrade_lV);
		List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
		while(mCur.moveToNext()){
			allPoint += Float.parseFloat(mCur.getString(5));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("tV" , mCur.getString(3));
			map.put("tV_2", mCur.getString(7));
			map.put("tV_3", mCur.getString(5));
			listItems.add(map);
		}
		SimpleAdapter lv_adapter = new SimpleAdapter(MainAllGrade.this, listItems,
				R.layout.mapp_grades_lv, new String[] { "tV","tV_2","tV_3"}, 
				new int[] {
					R.id.mshow_tV_Name_lv,R.id.mshow_tV_marks_lv,R.id.mshow_tV_point_lv});
		LV.setAdapter(lv_adapter);
		
		TextView InfoBar = (TextView)findViewById(R.id.mapp_allgrade_tV);
		InfoBar.setText("总学分:"+allPoint);
	}
	
	private void Http_Conn(){
		info_status = 0;
		(new Thread(){
    		@Override
    		public void run(){
    			String str_result = null;
    			String str_line = null;
    			
    			TheAccount acco = new TheAccount(cxt);
    			String log_url = "http://202.115.47.141//loginAction.do?zjh="
    					+ acco.getStdId() + "&mm=" + acco.getJWCPwd();
    			System.out.println(log_url);
    	        HttpPost httppost = new HttpPost(log_url);
    	        HttpClient httpclient = new DefaultHttpClient();
    	        HttpResponse httpResponse = null;
    	        HttpEntity httpEntity = null;
    	        InputStream inputStream = null;
                
    	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	        
    	        try {
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.ASCII));
					
					httpResponse = httpclient.execute(httppost);
					
					httpEntity = httpResponse.getEntity();
					if (httpResponse.getStatusLine().getStatusCode() ==200){
						Log.i("错误", "网络不好");
					}else{
						Log.i("OK", "网络可以");
					}
					
				} catch (Exception e) {
					Log.i("错误", "获取出错");
					e.printStackTrace();
				}
    	        
    	        //几个主要网址
    	        //http://202.115.47.141/gradeLnAllAction.do?type=ln&oper=qbinfo&lnsxdm=001
    	        //http://202.115.47.141/gradeLnAllAction.do?type=ln&oper=sxinfo&lnsxdm=001
    	        //http://202.115.47.141/gradeLnAllAction.do?type=ln&oper=bjg
    	        httppost = new HttpPost("http://202.115.47.141/gradeLnAllAction.do?type=ln&oper=qbinfo&lnsxdm=001");
    	        
    	        try {
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
					
					httpResponse = httpclient.execute(httppost);
					
					httpEntity = httpResponse.getEntity();
					
					if (httpResponse.getStatusLine().getStatusCode() == 200){
						info_status = 1;
						
						inputStream = httpEntity.getContent();
						
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(inputStream,"GB2312"));
						
						while((str_line = reader.readLine())!=null){
							str_result += str_line;
						}
						
						//*正则
						str_result = str_result.replaceAll("\r", "");
						str_result = str_result.replaceAll("\n", "");
						str_result = str_result.replaceAll("\t", "");
						str_result = str_result.replaceAll(" ", "");
						
						str_result = str_result.replaceAll("<tr", "\n<tr");
						str_result = str_result.replaceAll("<td", "#<td");

						str_result = str_result.replaceAll("优秀", "95.0");
						str_result = str_result.replaceAll("良好", "85.0");
						str_result = str_result.replaceAll("中等", "75.0");
						str_result = str_result.replaceAll("未通过", "0.0");
						str_result = str_result.replaceAll("通过", "60.0");
						str_result = str_result.replaceAll("不及格", "0.0");
						//*/
						
						RegularE(str_result);
						
					}else{
						info_status = 2;
						Log.i("失败", "00");
					}
					
				} catch (Exception e) {
					info_status = 3;
					Log.i("失败", "00");
					e.printStackTrace();
				}finally{
					try {
						inputStream.close();
					} catch (Exception e2) {
						Log.e("失败", "00");
					}
				}
    		}
    	}).start();
	}
	
	/**
	 * @function ��������
	 * @author �ޱ�
	 * @param cache ��Ҫ���������
	 * ��׼���õ��������򲢴������ݿ�
	 */
	void RegularE(String cache){
		theDB = new DBAdapter(cxt);

		cache = cache.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
				"<[^>]*>", "");
		cache = cache.replaceAll("[(/>)<]", "");
		
		cache = cache.replaceAll("\t", "");
		
		cache = cache.replace("�γ̺ſ���ſγ���Ӣ�Ŀγ���ѧ�ֿγ����Գɼ�", "");
		cache = cache.replace("##\n#\n#", "");
		cache = cache.replace("#\n#\n", "");
		cache = cache + "\n@@@@@";
		//System.out.println(cache);
		
		try{
			theDB.open();
			Log.w("警告","清空:"+theDB.clearALL()+"成功");
			String str_term = null;
			int sub_index = 0;
			for(;sub_index < cache.length();){
				if (cache.substring(sub_index,sub_index+5
						).compareTo("@@@@@") == 0)
					break;
				if (	cache.charAt(sub_index) == '#'&&
						cache.charAt(sub_index+1) == '#'){
					
					int t_i = cache.indexOf("��ѧ��#\n",sub_index);
					if (t_i - sub_index < 20){//14
						str_term = cache.substring(sub_index + 2, t_i);
					}
					sub_index += 18;
				}else {
					if (cache.charAt(sub_index) == '#'&& 
							cache.charAt(sub_index+1) != '#'){
						if (cache.substring(sub_index,sub_index+2
								).compareTo("#��") == 0){
							
							sub_index = cache.indexOf('\n',sub_index);
						}else{
							int end = cache.indexOf('\n',sub_index);
							int i=sub_index+1;						
							String str_cache[] = new String[7];	
							for(int j=0,line_index=0;i<end;i=j+1,line_index++){
								j=cache.indexOf("#",i);
								if (j >= end) j = end;
								
								str_cache[line_index] = cache.substring(i,j);
								
								if (str_cache[line_index] == null) 
									str_cache[line_index] = "null";
							}
							theDB.insertTitle(str_cache[0], Integer.parseInt(str_cache[1]),
									str_cache[2], str_cache[3], Float.parseFloat(str_cache[4]),
									str_cache[5], Float.parseFloat(str_cache[6]), str_term);
							sub_index = end;
						}
					}else{
						sub_index ++;
					}
				}
			}
		}catch (SQLException e1){
			Log.w("错误", e1.toString());
		}catch (Exception e2){
			Log.w("错误",e2.toString());
			e2.printStackTrace();
		}finally{
			theDB.close();
		}

		/*���ݿ��е������Ƿ���ȷ
		Cursor mCur;
		mCur = theDB.getAllTitles();
		Log.i("���ݿ��ѯ", "һ��"+mCur.getCount());
		while(mCur.moveToNext()){
			Log.i("�������ݿ��:",
					mCur.getString(1)+","+
					mCur.getInt(2)+","+
					mCur.getString(3)+","+
					mCur.getString(4)+","+
					mCur.getFloat(5)+","+
					mCur.getString(6)+","+
					mCur.getFloat(7)+","+
					mCur.getString(8));
		}//*/
	}
	
	private void Waiting(long l,String info){
		waiting_long = l;
		final ProgressDialog proDialog = android.app.ProgressDialog.show(
				MainAllGrade.this, info, "拼命处理中");
		Thread thread = new Thread(){
			public void run(){
				try{
					sleep(waiting_long);
		        }catch (InterruptedException e){
		            e.printStackTrace();
		        }
				Message msg = new Message();
				if(info_status == 1){
					msg.what = MSG_GET_SUCCESS;
					uihandler.sendMessage(msg);
				}else {
					msg.what = MSG_GET_FAIL;
					uihandler.sendMessage(msg);
				}
		        proDialog.dismiss();//if lose it will cause ANR
		    }
		};
		thread.start();
	}
	
	/*����ת�����㺯��
	private double Get_GAP(float Score){
		double GPA;
		
		if (Score < 60){
	        GPA = 0.0;
	    }else if (Score < 65 && Score >= 60){
	        GPA = 1.0;
	    }else if (Score < 70 && Score >= 65){
	        GPA = 1.7;
	    }else if (Score < 75 && Score >= 70){
	        GPA = 2.2;
	    }else if (Score < 80 && Score >= 75){
	        GPA = 2.7;
	    }else if (Score < 85 && Score >= 80){
	        GPA = 3.2;
	    }else if (Score < 90 && Score >= 85){
	        GPA = 3.6;
	    }else if (Score < 95 && Score >= 90){
	        GPA = 3.8;
	    }else{
	        GPA = 4.0;
	    }
		return GPA;
	}//*/
}
