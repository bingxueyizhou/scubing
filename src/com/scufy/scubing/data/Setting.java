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
	/* ��Ϣ����
	 * 1���Ƿ��Զ���¼
	 * 2���Ƿ񱣴��˺�
	 */
	/*�ೣ��*/
	private final String XML_NAME = "setting";
	private final String SET_ATO_LOGIN = "ato_login";
	private final String SET_SAVE_ACCOUNT = "save_account";
	//�����
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
	
	/**��ȡ 	�Ƿ��Զ���¼
	 * @return �����е��Զ���¼��ֵ
	 */
	public boolean getData_atoLogin(){
		return spV.getBoolean(SET_ATO_LOGIN, false);
	}
	
	/**����  �Ƿ��Զ���¼
	 * @param b �Զ���¼��ֵ
	 */
	public void setData_atoLogin(boolean b){
		spEditor.putBoolean(SET_ATO_LOGIN, b);
		save();
	}
	/**��ȡ 	�Ƿ񱣴��˺�
	 * @return �Ƿ񱣴��˺�
	 */
	public boolean getData_saveAccount(){
		return spV.getBoolean(SET_SAVE_ACCOUNT, false);
	}
	
	/**����  �Ƿ񱣴��˺�
	 * @param b �Ƿ񱣴��˺�
	 */
	public void setData_saveAccount(boolean b){
		spEditor.putBoolean(SET_SAVE_ACCOUNT, b);
		save();
	}
	
	/** ������Ϣ�ĵ���ÿ���ļ����ö���Ҫ�� **/
	private void save(){
		spEditor.commit();
	}
}
