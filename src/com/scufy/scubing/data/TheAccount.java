package com.scufy.scubing.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author �ޱ�
 * �˻��������
 */
public class TheAccount {
	/* ��Ϣ����
	 * 1��ѧ��
	 * 2����������
	 */
	/*�ೣ��*/
	private final String XML_NAME = "account";
	private final String INFO_STD_ID  = "std_id";
	private final String INFO_JWC_PWD = "jwc_pwd";
	//�����
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
	
	/**��ȡ 	ѧ��
	 * @return ѧ��
	 */
	public String getData_stdId(){
		return spV.getString(INFO_STD_ID, "");
	}
	
	/**����  ѧ��
	 * @param s ѧ��
	 */
	public void setData_stdId(String s){
		spEditor.putString(INFO_STD_ID, s);
		save();
	}
	
	/**��ȡ 	��������
	 * @return ��������
	 */
	public String getData_jwcPwd(){
		return spV.getString(INFO_JWC_PWD, "");
	}
	
	/**����  ��������
	 * @param s ��������
	 */
	public void setData_jwcPwd(String s){
		spEditor.putString(INFO_JWC_PWD, s);
		save();
	}
	
	/** ������Ϣ�ĵ���ÿ���ļ����ö���Ҫ�� **/
	private void save(){
		spEditor.commit();
	}
}
