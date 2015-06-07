package com.scufy.scubing.app;

import java.util.ArrayList;

import com.scufy.scubing.R;
import com.scufy.scubing.data.Setting;
import com.scufy.scubing.data.TheAccount;
import com.scufy.util.AccountChecker;

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
	
	//other class
	Setting mainSettings;
	TheAccount mainAccount;
	
	//main value
	public static MainView instance = null;
	private TextView mTab1, mTab2, mTab3;
	private int i_zero = 0;	//
	private int i_one;		//
	private int i_two;
	private ViewPager vPager;
	//private View thisLayout;
	//private LayoutInflater inflater;
	private int TabIndex = 0;
	private boolean have_evaluation = false;
	
	//control
	private RelativeLayout click_about;
	private RelativeLayout click_allgrade;
	private RelativeLayout click_tools;
	private RelativeLayout click_evaluation;
	private Button bu_login;
	private Button bu_reset;
	private CheckBox cBox_remember;
	private CheckBox cBox_atoLogin;
	private EditText ed_id;
	private EditText ed_pwd;
	
	/*loging status
	 * 0-未变化 1-成功 2-失败 3-没有网络 4-未知错误
	 */
	private int log_status = 0;
	
	//handler message
	private final int MSG_LOGIN_SUCCESS  = 1;
	private final int MSG_LOGIN_FAIL 	 = 2;
	private final int MSG_NO_NETWORK	 = 3;
	private final int MSG_UNDEFINE_ERROR = 4;
	@SuppressLint("HandlerLeak")
	private Handler uihandler = new Handler(){	
		public void handleMessage(Message msg){
			log_status = msg.what;
			switch(msg.what){
			case MSG_LOGIN_SUCCESS:
				Toast.makeText(MainView.this, "成功", Toast.LENGTH_SHORT).show();
				break;
			case MSG_LOGIN_FAIL:
				Toast.makeText(MainView.this, "失败", Toast.LENGTH_SHORT).show();
				break;
			case MSG_NO_NETWORK:
				Toast.makeText(MainView.this, "没有网络", Toast.LENGTH_SHORT).show();
				break;
			case MSG_UNDEFINE_ERROR:
				Toast.makeText(MainView.this, "未知错误", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);

		//when activity start hide input software
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		instance = this;
		mainSettings = new Setting(this);
		mainAccount  = new TheAccount(this);
		
		
		mTab1 = (TextView) findViewById(R.id.tv_main_app);
		mTab1.setBackgroundColor(0xffe5e5e5);//background color
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
		
		
		//get screen size
		DisplayMetrics  dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);     
		int displayWidth = dm.widthPixels;
		//int displayHeight = dm.heightPixels;
		
		//split screen
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
				animation.setFillAfter(true);// True:baidu
				animation.setDuration(150);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
	
		//initialize View
        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.mview_app, null);
        View view2 = mLi.inflate(R.layout.mview_account, null);
        View view3 = mLi.inflate(R.layout.mview_setting, null);
        
        //add view to a List
        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
		//ViewPager adapter
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

		//-----setting page-----
		//when click about
		click_about = (RelativeLayout)view3.findViewById(R.id.item_toclick_about);
		click_about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mainView_to_mainAbout = new Intent(MainView.this,MainAbout.class);
				startActivity(mainView_to_mainAbout);
				
			}
		});
		//-----application page-----
		//when click all grade
		click_allgrade = (RelativeLayout)view1.findViewById(R.id.item_cliked_allGrade);
		click_allgrade.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (log_status != 1){
					Toast.makeText(MainView.this, "未验证学号", Toast.LENGTH_SHORT).show();
					return ;
				}else{
					Intent mainView_to_mainAllGrade = new Intent(
							MainView.this,MainAllGrade.class);
					startActivity(mainView_to_mainAllGrade);
				}
			}
		});
		//when click developer tools
		click_tools = (RelativeLayout)view1.findViewById(R.id.item_cliked_tools);
		click_tools.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mainView_to_devTools = new Intent(
						MainView.this,CMDTools.class);
				startActivity(mainView_to_devTools);
			}
		});
		//when click evaluation
		click_evaluation = (RelativeLayout)view1.findViewById(R.id.item_cliked_evaluation);
		click_evaluation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (have_evaluation){
					Toast.makeText(instance, "请退出后进行下次评教", 
							Toast.LENGTH_SHORT).show();
				}else{
					have_evaluation = true;
					new Evaluation(instance).getOperation().start();
					Waiting(5000, "显示成功前不要有操作");
				}
			}
		});
		//-----main page-----
		//two check box
		cBox_atoLogin = (CheckBox)view2.findViewById(R.id.cBox_isAtoLogin);
		cBox_remember = (CheckBox)view2.findViewById(R.id.cBox_isRemember_ID);
		cBox_atoLogin.setChecked(mainSettings.isAtoLogin());
		cBox_remember.setChecked(mainSettings.isSaveAccount());
		cBox_atoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mainSettings.setAtoLogin(isChecked);
			}
		});
		cBox_remember.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mainSettings.setSaveAccount(isChecked);
			}
		});
		//two Edit Text box
		ed_id  = (EditText)view2.findViewById(R.id.eT_student_num);
		ed_pwd = (EditText)view2.findViewById(R.id.eT_password);
		if (mainSettings.isSaveAccount()){
			ed_id.setText(mainAccount.getStdId());
			ed_pwd.setText(mainAccount.getJWCPwd());
		}
		//button reset
		bu_reset = (Button)view2.findViewById(R.id.bu_reset);
		bu_reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ed_id.setText("");
				ed_pwd.setText("");
			}
		});
		//button login
		bu_login = (Button)view2.findViewById(R.id.bu_login);
		bu_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String std_num = ed_id.getText().toString();
				String jwc_pwd = ed_pwd.getText().toString();
				
				if (std_num.length() <= 1){
					Toast.makeText(MainView.this, "请输入账号", Toast.LENGTH_SHORT).show();		
					return;
				}
				
				if (jwc_pwd.length() <= 1){
					Toast.makeText(MainView.this, "请输入密码", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(mainSettings.isSaveAccount()){
					mainAccount.setSetId(std_num);
					mainAccount.setJWCPwd(jwc_pwd);
				}
				
				Jwc_login(std_num,jwc_pwd);
			}
		});

		if(mainSettings.isAtoLogin()){
			Jwc_login(mainAccount.getStdId(), mainAccount.getJWCPwd());
		}
	}
	

	private void Jwc_login(String num,String key){
		new Thread(new AccountChecker(num, key, uihandler)).start();
		Waiting(2000,"账号检查中");
	}

	private long waiting_long;
	private void Waiting(long l,String info){
		waiting_long = l;
		final ProgressDialog proDialog = android.app.ProgressDialog.show(
				MainView.this, info, "请耐心等待");
		Thread thread = new Thread(){
			public void run(){
				try{
					sleep(waiting_long);
		        }catch (InterruptedException e){
		            e.printStackTrace();
		        }
		        proDialog.dismiss();//it will cause ANR without it
		    }
		};
		thread.start();
	}
}
