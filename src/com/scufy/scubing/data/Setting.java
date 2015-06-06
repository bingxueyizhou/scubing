package com.scufy.scubing.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author �ޱ�
 * ���ñ������ȡ��
 */

public class Setting {
	/*
	 * file:setting
	 * set0:ato_login
	 * set1:save_account
	 */
	/*value of xml file structure*/
	private final String XML_NAME 			= "setting";
	private final String SET_ATO_LOGIN 		= "ato_check";
	private final String SET_SAVE_ACCOUNT 	= "save_account";
	
	//other value
	private SharedPreferences spV;
	private Editor spEditor;
	private Context TheCXT;
	
	public Setting(Context cxt) {
		TheCXT = cxt;
		spV = TheCXT.getSharedPreferences(XML_NAME,
				Activity.MODE_PRIVATE);
		spEditor = spV.edit();
		save();
	}
	
	/**
	 * if it's automatic check
	 * @return
	 */
	public boolean isAtoLogin(){
		return spV.getBoolean(SET_ATO_LOGIN, false);
	}
	
	/**
	 * set if it need automatic check
	 * @param b
	 */
	public void setAtoLogin(boolean b){
		spEditor.putBoolean(SET_ATO_LOGIN, b);
		save();
	}
	/**
	 * if it need save account
	 * @return
	 */
	public boolean isSaveAccount(){
		return spV.getBoolean(SET_SAVE_ACCOUNT, false);
	}
	
	/**
	 * to set Save Account
	 * @param b
	 */
	public void setSaveAccount(boolean b){
		spEditor.putBoolean(SET_SAVE_ACCOUNT, b);
		save();
	}
	
	/** file must commit if it need save **/
	private void save(){
		spEditor.commit();
	}
}
