package com.scufy.scubing.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import com.scufy.scubing.R;
import com.scufy.scubing.data.Setting;
import com.scufy.scubing.data.TheAccount;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainView extends Activity{
	
	//���ݿ��Ƶ���
	Setting mainSettings;
	TheAccount mainAccount;
	
	//һЩ�����ı���
	public static MainView instance = null;
	private TextView mTab1, mTab2, mTab3;
	private int i_zero = 0;// ����ͼƬƫ����
	private int i_one;//����ˮƽ����λ��
	private int i_two;
	private ViewPager vPager;
	//private View thisLayout;
	//private LayoutInflater inflater;
	private int TabIndex = 0;
	
	//�ؼ�
	private RelativeLayout click_about;
	private RelativeLayout click_allgrade;
	private RelativeLayout click_tools;
	private Button bu_login;
	private Button bu_reset;
	private CheckBox cBox_remember;
	private CheckBox cBox_atoLogin;
	private EditText ed_id;
	private EditText ed_pwd;
	
	//�������
	private static String log_url;
	private int log_status = 0;//0-δ��¼ 1-��¼�ɹ� 2,3-��¼ʧ�� 
	
	//����message���߳�
	private final int MSG_LOGIN_SUCCESS = 0;
	private final int MSG_LOGIN_FAIL = 1;
	@SuppressLint("HandlerLeak")
	private Handler uihandler = new Handler(){	
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_LOGIN_SUCCESS:
				Toast.makeText(MainView.this, "��¼�ɹ�", Toast.LENGTH_SHORT).show();
				break;
			case MSG_LOGIN_FAIL:
				Toast.makeText(MainView.this, "��¼ʧ��", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);

		//����activityʱ���Զ����������
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		instance = this;
		mainSettings = new Setting(this);
		mainAccount  = new TheAccount(this);
		
		
		mTab1 = (TextView) findViewById(R.id.tv_main_app);
		mTab1.setBackgroundColor(0xffe5e5e5);//���õ�һ������
		mTab1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vPager.setCurrentItem(0);
			}
		});
		mTab2 = (TextView) findViewById(R.id.tv_main_account);
		mTab2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vPager.setCurrentItem(1);
			}
		});
		mTab3 = (TextView) findViewById(R.id.tv_main_setting);
		mTab3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vPager.setCurrentItem(2);
			}
		});
		
		
		//��ȡ��Ļ��ǰ�ֱ���
		DisplayMetrics  dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);     
		int displayWidth = dm.widthPixels;
		//int displayHeight = dm.heightPixels;
		
		//����ˮƽ����ƽ�ƴ�С
        i_one = displayWidth/3;
        i_two = i_one*2;
       
		vPager = (ViewPager)findViewById(R.id.tabpager);
		vPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				Animation animation = null;
				switch (arg0) {
				case 0:
					mTab1.setBackgroundColor(0xffe5e5e5);
					if (TabIndex == 1) {
						animation = new TranslateAnimation(i_one, 0, 0, 0);
						mTab2.setBackgroundColor(0x00ffffff);
					} else if (TabIndex == 2) {
						animation = new TranslateAnimation(i_two, 0, 0, 0);
						mTab3.setBackgroundColor(0x00ffffff);
					}
					break;
				case 1:
					mTab2.setBackgroundColor(0xffe5e5e5);
					if (TabIndex == 0) {
						animation = new TranslateAnimation(i_zero, i_one, 0, 0);
						mTab1.setBackgroundColor(0x00ffffff);
					} else if (TabIndex == 2) {
						animation = new TranslateAnimation(i_two, i_one, 0, 0);
						mTab3.setBackgroundColor(0x00ffffff);
					}
					break;
				case 2:
					mTab3.setBackgroundColor(0xffe5e5e5);
					if (TabIndex == 0) {
						animation = new TranslateAnimation(i_zero, i_two, 0, 0);
						mTab1.setBackgroundColor(0x00ffffff);
					} else if (TabIndex == 1) {
						animation = new TranslateAnimation(i_one, i_two, 0, 0);
						mTab2.setBackgroundColor(0x00ffffff);
					}
					break;
				}
				TabIndex = arg0;
				animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
				animation.setDuration(150);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
	
		//��Ҫ��ҳ��ʾ��Viewװ��������
        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.mview_app, null);
        View view2 = mLi.inflate(R.layout.mview_account, null);
        View view3 = mLi.inflate(R.layout.mview_setting, null);
        
        //ÿ��ҳ���view����
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
		//���ViewPager������������
        PagerAdapter mPagerAdapter = new PagerAdapter() {
		
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}
		
			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
		
			//@Override
			//public CharSequence getPageTitle(int position) {
			//return titles.get(position);
			//}
			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));  
				return views.get(position);
			}
		};
		
		vPager.setAdapter(mPagerAdapter);

		
		/*ÿ����ҳ�ļ����¼�*/
		//-----����ҳ��-----
		click_about = (RelativeLayout)view3.findViewById(R.id.item_toclick_about);
		click_about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mainView_to_mainAbout = new Intent(MainView.this,MainAbout.class);
				startActivity(mainView_to_mainAbout);
				
			}
		});
		//-----Ӧ��ҳ��-----
		//���гɼ�����
		click_allgrade = (RelativeLayout)view1.findViewById(R.id.item_cliked_allGrade);
		click_allgrade.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (log_status != 1){
					Toast.makeText(MainView.this, "δ��¼", Toast.LENGTH_SHORT).show();
					return ;
				}else{
					Intent mainView_to_mainAllGrade = new Intent(
							MainView.this,MainAllGrade.class);
					startActivity(mainView_to_mainAllGrade);
				}
			}
		});
		//���߽���
		click_tools = (RelativeLayout)view1.findViewById(R.id.item_cliked_tools);
		click_tools.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mainView_to_devTools = new Intent(
						MainView.this,CMDTools.class);
				startActivity(mainView_to_devTools);
			}
		});
		//-----�˻�����-----
		//������ѡ��
		cBox_atoLogin = (CheckBox)view2.findViewById(R.id.cBox_isAtoLogin);
		cBox_remember = (CheckBox)view2.findViewById(R.id.cBox_isRemember_ID);
		cBox_atoLogin.setChecked(mainSettings.getData_atoLogin());
		cBox_remember.setChecked(mainSettings.getData_saveAccount());
		cBox_atoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mainSettings.setData_atoLogin(isChecked);
			}
		});
		cBox_remember.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mainSettings.setData_saveAccount(isChecked);
			}
		});
		//�����༭��
		ed_id  = (EditText)view2.findViewById(R.id.eT_student_num);
		ed_pwd = (EditText)view2.findViewById(R.id.eT_password);
		if (mainSettings.getData_saveAccount()){
			ed_id.setText(mainAccount.getData_stdId());
			ed_pwd.setText(mainAccount.getData_jwcPwd());
		}
		//���谴ť
		bu_reset = (Button)view2.findViewById(R.id.bu_reset);
		bu_reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ed_id.setText("");
				ed_pwd.setText("");
			}
		});
		//��¼��ť
		bu_login = (Button)view2.findViewById(R.id.bu_login);
		bu_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String std_num = ed_id.getText().toString();
				String jwc_pwd = ed_pwd.getText().toString();
				
				if (std_num.length() <= 1){
					Toast.makeText(MainView.this, "������ѧ��", Toast.LENGTH_SHORT).show();		
					return;
				}
				
				if (jwc_pwd.length() <= 1){
					Toast.makeText(MainView.this, "����������", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(mainSettings.getData_saveAccount()){
					mainAccount.setData_stdId(std_num);
					mainAccount.setData_jwcPwd(jwc_pwd);
				}
				
				Jwc_login(std_num,jwc_pwd);
			}
		});

		if(mainSettings.getData_atoLogin()){
			Jwc_login(mainAccount.getData_stdId(), mainAccount.getData_jwcPwd());
		}
	}
	

	private void Jwc_login(String num,String key){
		log_url = "http://202.115.47.141//loginAction.do?zjh="
				+ num + "&mm=" + key;
		
		log_status = 0;
		(new Thread(){
    		@Override
    		public void run(){
    			
    	        HttpPost httppost = new HttpPost(log_url);
    	        HttpClient httpclient = new DefaultHttpClient();
    	        HttpResponse httpResponse = null;//��Ӧ����
                
    	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	        
    	        //��½��ҳ����ȡcookie
    	        try {
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.ASCII));
					
					httpResponse = httpclient.execute(httppost);
					
					if (httpResponse.getStatusLine().getStatusCode() ==200){
						log_status = 1;
						Log.i("ģ���½", "�ɹ�");
					}else{
						log_status = 2;
						Log.i("ģ���½", "ʧ��");
					}
					
				} catch (Exception e) {
					log_status = 3;
					Log.i("", "��ʧ��");
					e.printStackTrace();
				}
    		}
    	}).start();
		Waiting(2000,"不知道");
	}

	private long waiting_long;
	private void SendMessageInfo(int code){
		Message msg = new Message();
		//code 0-��¼��Ϣ
		switch(code){
		case 0:
			if(log_status == 1){
				msg.what = MSG_LOGIN_SUCCESS;
				uihandler.sendMessage(msg);
			}else {
				msg.what = MSG_LOGIN_FAIL;
				uihandler.sendMessage(msg);
			}
			break;
		}
	}
	private void Waiting(long l,String info){
		waiting_long = l;
		final ProgressDialog proDialog = android.app.ProgressDialog.show(
				MainView.this, info, "登录中");
		Thread thread = new Thread(){
			public void run(){
				try{
					sleep(waiting_long);
		        }catch (InterruptedException e){
		            e.printStackTrace();
		        }
				SendMessageInfo(0);
		        proDialog.dismiss();//���򲻿�����䣬��������Ῠ����
		    }
		};
		thread.start();
	}
}
