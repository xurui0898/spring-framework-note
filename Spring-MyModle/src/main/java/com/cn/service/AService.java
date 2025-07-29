package com.cn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

//@EnableAsync
@Component
//@Scope("prototype")
//@Scope("singleton")
public class AService {


	/**
	 * 两个多例bean循环依赖会怎么样？
	 *
	 * 会有循环依赖的问题，但是只是在引用类的时候才会出现，不会影响容器的正常启动，类似于懒加载
	 * todo 多例bean在加载的时候是懒加载，只有调用的时候才会尝试创建
	 */

    @Autowired
    BService bService;


//	//spring的循环依赖只在set注入的时候会生效
//	//对于构造器时，是不会生效的
//	// 		构造器形式的：A需要B对象才能把A对象创建出来，此时A对象都没有，三级缓存解决不了构造器注入的循环依赖问题
//	//		set注入形式：A对象已经创建好了(空参构造?)，属性赋值的时候创建B对象,此时三级缓存可以解决循环依赖问题
//	@Autowired
//	public AService(BService bService) {
//		this.bService = bService;
//	}

	public BService getbService() {
		return bService;
	}

	public void setbService(BService bService) {
		this.bService = bService;
	}
}
