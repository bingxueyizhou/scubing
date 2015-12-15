package com.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


public class MHttpClient {

    HttpClient httpclient;
    
	public MHttpClient(HttpClient h){
		this.httpclient = h;
	}
	
	public String doGet(String url,String params){
		String uriAPI = url+"?"+params;
		String result= "";
		//HttpGet httpRequst = new HttpGet(URI uri);
		//HttpGet httpRequst = new HttpGet(String uri);
		//创建HttpGet或HttpPost对象，将要请求的URL通过构造方法传入HttpGet或HttpPost对象
		System.out.println(uriAPI);
		HttpGet httpRequst = new HttpGet(uriAPI);
	
	    try {
	    	//new DefaultHttpClient().execute(httpRequst);
	    	//使用DefaultHttpClient类的execute方法发送HTTP GET请求，并返回HttpResponse对象。
			//其中HttpGet是HttpUriRequst的子类
			HttpResponse httpResponse = this.httpclient.execute(httpRequst);
		    if(httpResponse.getStatusLine().getStatusCode() == 200){
		    	HttpEntity httpEntity = httpResponse.getEntity();
		    	httpEntity=httpResponse.getEntity();
            	result = EntityUtils.toString(httpEntity);//取出应答字符串
            	//一般来说都要删除多余的字符 
            	result.replaceAll("\r", "");
            	//去掉返回结果中的"\r"字符，否则会在结果字符串后面显示一个小方格 
	    	}else{
	    		System.out.println(httpResponse.getStatusLine().getStatusCode());
	    	}
		 }catch(ClientProtocolException e) {
			e.printStackTrace();
			//result = e.getMessage().toString();
		} catch (IOException e) {
			e.printStackTrace();
			//result = e.getMessage().toString();
		}

		return result;
	}
    	
	
	public String doPost(String url,ArrayList<NameValuePair> p){
    	String result = "";
    	HttpPost httpRequst = new HttpPost(url);//创建HttpPost对象
    	
    	/*
    	List <NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("str", "I am Post String"));
		//HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequst);
    	*/
    	
    	try {
			httpRequst.setEntity(new UrlEncodedFormEntity(p,HTTP.UTF_8));
			HttpResponse httpResponse = this.httpclient.execute(httpRequst);
			System.out.println(httpRequst.getEntity().getContentLength());
			System.out.println("qline:"+httpRequst.getRequestLine());
			
		    if(httpResponse.getStatusLine().getStatusCode() == 200){
		    	HttpEntity httpEntity = httpResponse.getEntity();
		    	result = EntityUtils.toString(httpEntity);//取出应答字符串
		    }else{
	    		System.out.println(httpResponse.getStatusLine().getStatusCode());
	    	}
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
			//result = e.getMessage().toString();
		}catch (ClientProtocolException e) {
			e.printStackTrace();
			//result = e.getMessage().toString();
		}catch (IOException e) {
			e.printStackTrace();
			//result = e.getMessage().toString();
		}
    	return result;
	}
	
}
