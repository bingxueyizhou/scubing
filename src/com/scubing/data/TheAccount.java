package com.scubing.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author zoubing
 * User Account manager
 */
public class TheAccount {
	/*
	 * file: account
	 * id :std_id
	 * key:jwc_pwd
	 */
	/*value of xml file structure*/
	private final String XML_NAME	  = "account";
	private final String INFO_STD_ID  = "std_id";
	private final String INFO_JWC_PWD = "jwc_pwd";
	private final String INFO_IS_OK	  = "is_ok";
	
	private SharedPreferences spV;
	private Editor spEditor;
	private Context TheCXT;
	
	public TheAccount(Context cxt) {
		TheCXT = cxt;
		spV = TheCXT.getSharedPreferences(XML_NAME,
				Activity.MODE_PRIVATE);
		spEditor = spV.edit();
		save();
	}
	
	/**
	 * get student id
	 * @return
	 */
	public String getStdId(){
		return spV.getString(INFO_STD_ID, "");
	}
	
	/**
	 * set student id
	 * @param s student id
	 */
	public void setStdId(String s){
		spEditor.putString(INFO_STD_ID, s);
		save();
	}
	
	/**
	 * get jiaowuchu password
	 * @return String of jiaowuchu key
	 */
	public String getJWCPwd(){
		return spV.getString(INFO_JWC_PWD, "");
	}
	
	/**
	 * set JWC password
	 * @param s String of JWC key
	 */
	public void setJWCPwd(String s){
		spEditor.putString(INFO_JWC_PWD, s);
		save();
	}
	
	/**
	 * set login status success
	 */
	public void setLoginOK(){
		spEditor.putBoolean(INFO_IS_OK, true);
		save();
	}
	
	/**
	 * set login status fail
	 */
	public void setLoginError(){
		spEditor.putBoolean(INFO_IS_OK, false);
		save();
	}
	
	/**
	 * get login status
	 * @return the boolean of login
	 */
	public boolean getLoginStatus(){
		return spV.getBoolean(INFO_IS_OK, false);
	}
	
	/**
	 * main sava function
	 */
	private void save(){
		spEditor.commit();
	}
}
