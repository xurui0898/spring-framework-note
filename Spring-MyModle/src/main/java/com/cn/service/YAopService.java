package com.cn.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
public class YAopService {

	@Value("111")
	private String Name;

	public void testAop(){
		System.out.println("======业务方法 YAopService->testAop() 执行======");
	}

}
