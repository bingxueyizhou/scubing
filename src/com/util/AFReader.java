package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

/**
 * @author zoubing
 */
public class AFReader {
	
	private Context cxt;
	/**
	 * @param c Context
	 */
	public AFReader(Context c){
		this.cxt = c;
	}
	
	/**
	 * get string from file by fileName
	 * @param fileName
	 * @return the String of the file
	 * @throws Exception if Exception happen in operation
	 */
	public String readString(String fileName) throws IOException{
        InputStreamReader inputReader = new InputStreamReader(
        		this.cxt.getResources().getAssets().open(fileName) ); 
        BufferedReader bufReader = new BufferedReader(inputReader);
        String line="";
        String Result="";
        while((line = bufReader.readLine()) != null)
            Result += line;
        return Result;
	}
	
	/**
	 * get Input Stream form file by fileName
	 * @param fileName
	 * @return the Stream of the file
	 * @throws Exception if Exception happen in operation
	 */
	public InputStream readStream(String fileName) throws IOException{
		return this.cxt.getResources().getAssets().open(fileName);
	}
}
