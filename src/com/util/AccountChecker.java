package com.util;

import java.io.IOException;

import android.os.Handler;
import android.os.Message;

public class AccountChecker implements Runnable{
	
	private final int MSG_LOGIN_SUCCESS  = 1;
	private final int MSG_LOGIN_FAIL 	 = 2;
	private final int MSG_NO_NETWORK	 = 3;
	private final int MSG_UNDEFINE_ERROR = 4;
	
	
	private String account;
	private String key;
	private Handler handler;
	
	public AccountChecker(String a,String k,Handler h){
		this.account = a;
		this.key     = k;
		this.handler = h;
	};

	@Override
	public void run() {
		String log_url 		= "http://202.115.47.141//loginAction.do";
		String log_params 	= "zjh="+ this.account + "&mm=" + this.key;
		
		String res;
		System.out.println("消息:进入");
		Message msg = new Message();
		try {
			res = HttpRequest.sendGet(log_url, log_params);
			System.out.println(res);
			if (res.length() < 4000){
				if (res.length() < 800){
					msg.what = MSG_LOGIN_SUCCESS;
				}else{
					msg.what = MSG_UNDEFINE_ERROR;
				}
			}else{
				msg.what = MSG_LOGIN_FAIL;
			}
		} catch (IOException e) {
			msg.what = MSG_NO_NETWORK;
		}
		System.out.println("消息"+msg.what);
		handler.sendMessage(msg);
	}

}
