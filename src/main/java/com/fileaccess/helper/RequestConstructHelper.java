package com.fileaccess.helper;


public class RequestConstructHelper {

	public static String getRepoUrl(String... repoDetails) {
		String url = "";
		for(String input: repoDetails) {
			url=url+input;
		}
		return url;
	}
	
}
