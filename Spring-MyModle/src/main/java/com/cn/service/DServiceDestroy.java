package com.cn.service;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

//@Component
public class DServiceDestroy implements DisposableBean {

	/*
		bean的销毁：
			即当容器关闭的时候（context.close();），会调用这个方法
			在某一个bean的方法上加上@PreDestroy注解也是同理
	*/
	@Override
	public void destroy() throws Exception {
		System.out.println("DServiceDestroy：执行销毁方法");
	}
}
