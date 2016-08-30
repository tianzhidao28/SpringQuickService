package com.rocyuan.zero.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="company")
public class Company extends IdEntity{
	String name ;
	String address ;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
