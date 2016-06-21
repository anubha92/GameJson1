package com.game.url;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionReader {
	public static StringBuilder getURLConnectionData(String endPointURL)throws Exception {
		//URL url = new URL("http://play.eslgaming.com/api/leagues?types=cup&states=finished&limit.total=25&path=%2Fplay%2Fworldoftanks%2Feurope%2F");
		URL url = new URL(endPointURL);
		URLConnection yc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;
	    StringBuilder content = new StringBuilder();
	    while ((inputLine = in.readLine()) != null){
	    	//System.out.println("Test::"+inputLine);
	    	content.append(inputLine);
	    }
	    	
	    //System.out.println(content.toString());
	    in.close();
	    return content;
	}
}
