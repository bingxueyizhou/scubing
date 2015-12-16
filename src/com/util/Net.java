//*
package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Net {

	String ip = "";
	//*to get my IP
	public void getLocalIpAddress() {
        if((ip != null) && (!"".equals(ip))) {
            return;
        }
        (new Thread(new Runnable() {
			
			@Override
			public void run() {
				URL infoUrl;
                InputStream inStream;
                try {
                	//infoUrl = new URL("http://www.ip138.com/");
                	infoUrl = new URL("http://1111.ip138.com/ic.asp");
                    URLConnection connection = infoUrl.openConnection();
                    URLConnection httpConnection = (HttpURLConnection)connection;
                    int responseCode = ((HttpURLConnection) httpConnection).getResponseCode();
                    if(responseCode == 200) {
                        inStream = httpConnection.getInputStream();
                        BufferedReader reader = new BufferedReader(
                        		new InputStreamReader(inStream));
                        String strber = "";
                        String str_tmp;
                    	str_tmp = reader.readLine();
                        while(str_tmp != null) {
                            strber += str_tmp + "\n";
                        	str_tmp = reader.readLine();
                        }
                        inStream.close();
                        int start = strber.indexOf("[");
                        int end = strber.indexOf("]", (start + 1));
                        ip = strber.substring((start + 1), end);
                        System.out.println(ip);
                    }
                } catch(MalformedURLException e) {
                    e.printStackTrace();
                    return;
                } catch(IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
		})).start();
    }//*/
//}*/
}
