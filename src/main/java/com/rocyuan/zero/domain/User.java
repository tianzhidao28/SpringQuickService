package com.rocyuan.zero.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

//JPA实体类标识
@Entity
@Table(name = "ss_user")
public class User extends IdEntity {
	private String remarkName;
	private Long uin;
	private Date createDate;

	private String headImgUrl;
	private String city;

	private Integer sex;

	private String nickName;

	public User() {
	}

	public User(Long id) {
		this.id = id;
	}

	public String getRemarkName() {
		return remarkName;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}

	public Long getUin() {
		return uin;
	}

	public void setUin(Long uin) {
		this.uin = uin;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	// 设定JSON序列化时的日期格式
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}