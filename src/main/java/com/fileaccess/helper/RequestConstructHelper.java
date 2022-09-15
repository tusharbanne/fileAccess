package com.fileaccess.helper;


public class RequestConstructHelper {

	public static String createPath(String... repoDetails) {
		String url = "";
		for(String input: repoDetails) {
			url=url+input;
		}
		return url;
	}
	
}
