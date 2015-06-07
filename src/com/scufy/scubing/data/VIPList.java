package com.scufy.scubing.data;

import android.content.Context;

public class VIPList {
	
	private String[] list = new String[]{
			 "2013141461028" //邹兵
			,"2013141461005" //何漪澜
			,"2013141461018" //陈德欣
			,"2013141461025" //刘岱鑫
			,"2013141461040" //陈培
	};
	@SuppressWarnings("unused")
	private Context cxt;
	
	public VIPList(Context c) {
		this.cxt = c;
	}
	
	public boolean isVIP(String v){
		int l = list.length;
		for(int i=0;i<l;i++){
			if(v.equals(list[i])) return true;
		}
		return false;
	}
	
}
