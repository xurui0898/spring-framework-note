package com.cn.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 *  todo BeanPostProcessor / BeanFactoryPostProcessor 对比分析
 *
 *  BeanPostProcessor:
 *  	1.加载时机：在 refresh(); --> registerBeanPostProcessors(beanFactory); 并存放到一级缓存singletonObjects中
 *  	2.作用：
 *  		加载完成之后，会存放到容器的 getBeanPostProcessors() --> return this.beanPostProcessors;对象中	，
 *  		然后再后续每个bean走生命周期的时候都会再走一遍	BeanPostProcessors集合的前后置处理逻辑
 *
 *  BeanFactoryPostProcessor：
 *  	1.加载时机：在 refresh(); --> invokeBeanFactoryPostProcessors(beanFactory);并存放到一级缓存singletonObjects中（加载时机在BeanPostProcessor之前）
 *  	2.作用：
 *  		加载完成之后，会执行对应 BeanFactoryPostProcessor接口集合的 postProcessBeanFactory方法 （这个方法允许开发人员对 BeanDefinition 进行修改，进而影响容器中 Bean 的创建和配置过程。	）
 *  		进行逻辑回调
 *  		（主要是对具体的beanDefinition进行属性修改）
 *
 */
//@Component
public class FServiceBeanFactoryPostProcessor implements BeanFactoryPostProcessor /*, Ordered*/ {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("调用 FServiceBeanFactoryPostProcessor");
		BeanDefinition bd = beanFactory.getBeanDefinition("xService");
		System.out.println("属性值============" + bd.getPropertyValues().toString());
		MutablePropertyValues pv =  bd.getPropertyValues();
		// 修改XService中name的属性值
		pv.add("name", "BeanFactoryPostProcessor 修改值成功");
		//赋值的时候，如果bean对应的实体类中没有这个propertyName属性（即没有这个变量名），
		// 则在容器启动会报错 Invalid property 'a' of bean class  [com.cn.service.XService]: Bean property 'a' is not writable or has an invalid setter method. Did you mean 'id'?
//		pv.add("a", "a");
		bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
	}

	/**
	 *  如果 BeanFactoryPostProcessor 同时实现了 Ordered接口，并重写了方法getOrder()
	 *  那在加载BeanFactoryPostProcessor会将当前BeanFactoryPostProcessor放到 orderedPostProcessorNames中去
	 *
	 *  即执行顺序会和不加 Ordered接口的不同，
	 *  BeanFactoryPostProcessor 执行顺序
	 *  	1.实现了PriorityOrdered接口，且 getOrder()方法返回值小的
	 *  	2.实现了PriorityOrdered接口，且 getOrder()方法返回值小的
	 *  	3.实现了Ordered接口，且 getOrder()方法返回值小的
	 *  	4.实现了Ordered接口，且 getOrder()方法返回值大的
	 *  	5.未实现Ordered接口
	 */
//	@Override
//	public int getOrder() {
//		return 0;
//	}
}
