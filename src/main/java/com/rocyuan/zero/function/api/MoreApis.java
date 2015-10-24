package com.rocyuan.zero.function.api;

import java.util.List;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

import com.google.common.collect.Lists;
import com.rocyuan.zero.domain.Api;

public class MoreApis {

	public static String apiFirstResult(List<Api> apiList, String query) {

		ExecutorCompletionService<String> service = new ExecutorCompletionService<String>(
				Executors.newFixedThreadPool(10));

		for (int i = 0; i < 10; i++)
			service.submit(new HttpTask(""));

		return null;
	}
	
//	public static void main(String[] args) {
//		List<Api> list  =  Lists.newArrayList();
//		list.add(new Api("","",""));
//
//	}

}
