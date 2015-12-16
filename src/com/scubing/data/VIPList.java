package com.scubing.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

//website: http://blog.csdn.net/bear_huangzhen/article/details/24692881

public class VIPList {
	/*
	private String[] list = new String[]{
			 "2013141461028" //邹兵
			,"2013141461005" //何漪澜
			,"2013141461018" //陈德欣
			,"2013141461025" //刘岱鑫
			,"2013141461040" //陈培
			,"2013141461003" //张坤
			,"2013141461035" //陈增超
			,"2013141461022" //杨涛
	};
	*/
	/*
	int l = list.length;
	for(int i=0;i<l;i++){
		if(v.equals(list[i])) return true;
	}*/
	private Context cxt;
	private ArrayList<Viper> mVips;
	private boolean xmlParsered = false;
	
	public VIPList(Context c) throws IOException, Exception{
		this.cxt = c;
		try {
			VipXmlExample mXml = new VipXmlExample();
			mVips = (ArrayList<Viper>) mXml.parse(cxt.getAssets().open("vip.xml"));
		} catch (IOException e) {
			Log.e("VIPList", "error in IO of 'vip.xml'");
			throw new IOException(e.getMessage());
		} catch (Exception e) {
			Log.e("VIPList", "error in 'vip.xml'");
			throw new Exception(e);
		}
		xmlParsered = true;
	}
	
	public List<Viper> getList(){
		return mVips;
	}
	
	public boolean isVIP(String v){
		if (!xmlParsered) return false;
		
		int l = mVips.size();
		for(int i=0;i<l;i++){
			if(v.equals(mVips.get(i).getStdId() ))
				return true;
		}
		return false;
	}
	
	/**
	 * @author 邹兵
	 * class of VIP person
	 */
	public class Viper{
		private String stdName = "";
		private String stdID   = "";
		public void setName(String name){
			this.stdName = name;
		}
		
		public void setStdId(String id){
			this.stdID = id;
		}
		
		public String getName(){
			return this.stdName;
		}
		
		public String getStdId(){
			return this.stdID;
		}
		
		@Override
		public String toString(){
			return "{姓名:"+this.stdName+",学号:"+this.stdID+"}";
		}
	}
	
	public class VipXmlExample implements IXmlParser<Viper>{

		@Override
		public List<Viper> parse(InputStream is) throws Exception {
			ArrayList<Viper> mList = null;
			Viper viper = null;
			
			XmlPullParser xpp = Xml.newPullParser();
			// 设置输入流 并指明编码方式
			xpp.setInput(is,"UTF-8");
			// 产生第一个事件
			int eventType = xpp.getEventType();
			
			while (eventType != XmlPullParser.END_DOCUMENT){
				 switch (eventType) {
		             // 判断当前事件是否为文档开始事件
		             case XmlPullParser.START_DOCUMENT:
		            	 //初始化list集合
		                 mList = new ArrayList<Viper>(); 
		                 break;
		             // 判断当前事件是否为标签元素开始事件
		             case XmlPullParser.START_TAG:
		                 //判断开始标签元素是否是item
		                 if (xpp.getName().equals("item")) { 
		                     viper = new Viper();
		                     //得到标签的属性值，并设置VIP的值
		                     viper.setName(xpp.getAttributeValue(null, "name"));
		                     eventType = xpp.next();
		                     viper.setStdId(xpp.getText());
		                 }
		                 break;
		  
		             // 判断当前事件是否为标签元素结束事件
		             case XmlPullParser.END_TAG:
		                 //判断结束标签元素是否是item
		            	 //将item添加到list集合
		                 if (xpp.getName().equals("item")) { 
		                     mList.add(viper);
		                     viper = null;
		                 }
		                 break;
		             }
		             // 进入下一个元素并触发相应事件
		             eventType = xpp.next();
		         }
		         return mList;
		}

		@Override
		public String serialize(List<Viper> obj) throws Exception {
			return null;
		}
		
	}
	
}
