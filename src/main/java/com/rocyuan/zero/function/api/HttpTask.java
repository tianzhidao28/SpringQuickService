
package com.rocyuan.zero.function.api;

import java.util.concurrent.Callable;

import org.apache.http.client.utils.HttpClientUtils;
import org.springframework.http.ResponseEntity;

import com.rocyuan.zero.common.utils.HttpClientUtil;

/**
 * 
 * @author yuanzp@jpush.cn
 * @date 2015-6-26 
 * @desc  执行Http请求
 */
public class HttpTask  implements Callable<String>{
	String url ;
	String params ;	
	String method = params == null ? "GET":"POST";	
	
	public HttpTask(String url) {
		super();
		this.url = url;
	}

	public HttpTask(String url, String params) {
		super();
		this.url = url;
		this.params = params;
	}

	public HttpTask(String url, String params, String method) {
		super();
		this.url = url;
		this.params = params;
		this.method = method;
	}

	@Override
	public String call() throws Exception {
		System.out.println(Thread.currentThread().getName()+" start ");
		String result = HttpClientUtil.httpRequest(url, method, params);
		System.out.println(Thread.currentThread().getName()+" end ");
		return result ;
	}
	
//	public static void main(String[] args) {
//		ResponseEntity<String> result = RestTemplateClient.getRestTemplate().getForEntity("http://www.tuling123.com/openapi/api?key=3b04ca26d757733020115c20f757a467&info=hello", String.class);
//		System.out.println(result.getBody());
//		System.out.println( HttpClientUtil.httpRequest("http://www.tuling123.com/openapi/api", "GET", "key=3b04ca26d757733020115c20f757a467&info=hello") );
//	}

}
