package com.rocyuan.zero.domain;

public class Api {
	
	String url ;
	String method ;
	String param;
	String resultType;
		
	public Api() {
		super();
	}
	public Api(String url, String method, String param) {
		super();
		this.url = url;
		this.method = method;
		this.param = param;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	
	
}
