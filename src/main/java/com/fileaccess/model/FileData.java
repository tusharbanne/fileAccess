package com.fileaccess.model;

public class FileData {

	private String name;
	
	private String lastUpdated;
	
	private String modidfiedBy;
	
	private Boolean dev;
	
	private Boolean si;
	
	private Boolean prod;
	
	private Integer total_count;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getModidfiedBy() {
		return modidfiedBy;
	}

	public void setModidfiedBy(String modidfiedBy) {
		this.modidfiedBy = modidfiedBy;
	}

	public Boolean getDev() {
		return dev;
	}

	public void setDev(Boolean dev) {
		this.dev = dev;
	}

	public Boolean getSi() {
		return si;
	}

	public void setSi(Boolean si) {
		this.si = si;
	}

	public Boolean getProd() {
		return prod;
	}

	public void setProd(Boolean prod) {
		this.prod = prod;
	}

	public Integer getTotal_count() {
		return total_count;
	}

	public void setTotal_count(Integer total_count) {
		this.total_count = total_count;
	}
}
