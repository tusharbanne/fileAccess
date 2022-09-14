package com.fileaccess.model;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


public class GitTreeData {

	private String sha;
	
	private String url;
	
	private List<GitTree> tree;

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<GitTree> getTree() {
		return tree;
	}

	public void setTree(List<GitTree> tree) {
		this.tree = tree;
	}
	
	
}
