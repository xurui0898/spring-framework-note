package com.cn.service;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

//@Component
public class EServiceInitializing implements InitializingBean, BeanNameAware {

	private String beanName;

	/**
	 *bean的初始化
	 * 	   在bean的初始化的时候会调用这方法的逻辑
	 *     todo @PostConstruct 注解同理
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("EServiceInitializing：执行初始化方法");
	}

	@Override
	public void setBeanName(String beanName) {
		System.out.println("EServiceInitializing: 执行aware方法");
		this.beanName=beanName;
	}
}
