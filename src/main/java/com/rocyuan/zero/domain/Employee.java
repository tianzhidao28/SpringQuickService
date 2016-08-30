package com.rocyuan.zero.domain;

import java.util.List;

public class Employee extends Human {
	
	String position ; 
	
	List<Company> companyList ;
	
	String desc ;

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public List<Company> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<Company> companyList) {
		this.companyList = companyList;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	

	
}
