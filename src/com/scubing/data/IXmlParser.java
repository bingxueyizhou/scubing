package com.scubing.data;

import java.io.InputStream;
import java.util.List;

public interface IXmlParser<T> {
	/** 
     *  
     * 解析输入流，获取<T>列表 
     * @param is 
     * @return list of T
     * @throws Exception 
     */  
    public List<T> parse(InputStream is) throws Exception;  
      
    /** 
     *  
     * 序列化<T>对象集合，得到XML形式的字符串 
     * @param objects 
     * @return String of T
     * @throws Exception 
     */  
    public String serialize(List<T> list) throws Exception;  
}
