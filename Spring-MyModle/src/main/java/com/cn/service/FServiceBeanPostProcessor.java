package com.cn.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 实现BeanPostProcessor的接口，在bean初始化的时候会调用重写的两个方法逻辑
 *
 * bean的生命周期：
 * 		实例化，属性赋值，初始化，bean的销毁
 * 		（实例化，属性赋值，aware，BeanPostProcessor前置，InitializingBean，BeanPostProcessor后置，bean的销毁）
 *
 * bean的初始化流程：
 * 	1.调用的Aware方法
 * 	2.调用BeanPostProcessor的前置方法postProcessBeforeInitialization
 * 	3.调用初InitializingBean的初始化方法
 * 	4.调用BeanPostProcessor的后置方法postProcessAfterInitialization
 *
 *
 * 	todo BeanPostProcessor 其实不是增对于当前定义的BeanPostProcessor的扩展，而是对其他的bean的扩展
 *  	例如：
 *  	1.定义了abc三个bean，和一个 FServiceBeanPostProcessor的BeanPostProcessor
 *  	2.在走bean生命周期之前的 registerBeanPostProcessors(beanFactory);方法的时候，就会初始化好FServiceBeanPostProcessor，并放到容器的 getBeanPostProcessors() --> return this.beanPostProcessors;对象中
 *  	3.而FServiceBeanPostProcessor对bean的增强体现在，后续走abc生命周期的时候，会拿到之前定义的所有BeanPostProcessor，然后循环BeanPostProcessor集合，分别对当前正常进行生命周期的bean进行逻辑增强。
 *      （
 *      	a生命周期的时候，实例化 --> 属性赋值 --> 初始化
 *      				 Aware --> BeanPostProcessor.postProcessBeforeInitialization --> InitializingBean -->BeanPostProcessor.postProcessAfterInitialization
 *           BeanPostProcessor.postProcessBeforeInitialization和BeanPostProcessor.postProcessAfterInitialization就是拿的之前创建的BeanPostProcessor集合，进行执行。
 *           即a生命周期走到BeanPostProcessor.postProcessBeforeInitializatio的时候
 *           会遍历容器中所有的beanPostProcessors，然后依次执行对应定义的postProcessBeforeInitialization
 *     	）
 *
 *
 *
 */
//@Component
public class FServiceBeanPostProcessor implements BeanPostProcessor {

	/**
	 * 前后置入参bean就是当前bean对象，可以对bean对象进行修改然后返回
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//		if(!"testSpringConfig".equals(beanName)){
//			System.out.println(bean+" 执行了postProcessBeforeInitialization前置方法");
//		}
		System.out.println(beanName+" 执行了postProcessBeforeInitialization前置方法");
		return bean;
	}

	/**
	 *  aop逻辑就在后置处理方法实现的，返回一个代理对象回去
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//		if(!"testSpringConfig".equals(beanName)){
//			System.out.println(bean+" 执行了postProcessAfterInitialization后置方法");
//			System.out.println("===============================");
//		}
		System.out.println(beanName+" 执行了postProcessAfterInitialization后置方法");
		System.out.println("===============================");
		return bean;
	}
}
