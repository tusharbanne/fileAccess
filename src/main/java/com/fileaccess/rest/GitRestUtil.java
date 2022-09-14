package com.fileaccess.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fileaccess.model.GitTreeData;

@Component
public class GitRestUtil {

	@Autowired
	private WebClient.Builder webClientBuilder;
	
	public GitTreeData getAllFiles(String url){
		
		return webClientBuilder.build()
				.get()
				.uri(url)
				.retrieve()
				.bodyToMono(GitTreeData.class)
				.block();
	}
	
}
