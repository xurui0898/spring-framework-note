/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.aop.aspectj.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.reflect.PerClauseKind;

import org.springframework.aop.Advisor;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Helper for retrieving @AspectJ beans from a BeanFactory and building
 * Spring Advisors based on them, for use with auto-proxying.
 *
 * @author Juergen Hoeller
 * @since 2.0.2
 * @see AnnotationAwareAspectJAutoProxyCreator
 */
public class BeanFactoryAspectJAdvisorsBuilder {

	private final ListableBeanFactory beanFactory;

	private final AspectJAdvisorFactory advisorFactory;

	@Nullable
	private volatile List<String> aspectBeanNames;

	private final Map<String, List<Advisor>> advisorsCache = new ConcurrentHashMap<>();

	private final Map<String, MetadataAwareAspectInstanceFactory> aspectFactoryCache = new ConcurrentHashMap<>();


	/**
	 * Create a new BeanFactoryAspectJAdvisorsBuilder for the given BeanFactory.
	 * @param beanFactory the ListableBeanFactory to scan
	 */
	public BeanFactoryAspectJAdvisorsBuilder(ListableBeanFactory beanFactory) {
		this(beanFactory, new ReflectiveAspectJAdvisorFactory(beanFactory));
	}

	/**
	 * Create a new BeanFactoryAspectJAdvisorsBuilder for the given BeanFactory.
	 * @param beanFactory the ListableBeanFactory to scan
	 * @param advisorFactory the AspectJAdvisorFactory to build each Advisor with
	 */
	public BeanFactoryAspectJAdvisorsBuilder(ListableBeanFactory beanFactory, AspectJAdvisorFactory advisorFactory) {
		Assert.notNull(beanFactory, "ListableBeanFactory must not be null");
		Assert.notNull(advisorFactory, "AspectJAdvisorFactory must not be null");
		this.beanFactory = beanFactory;
		this.advisorFactory = advisorFactory;
	}


	/**
	 * Look for AspectJ-annotated aspect beans in the current bean factory,
	 * and return to a list of Spring AOP Advisors representing them.
	 * <p>Creates a Spring Advisor for each AspectJ advice method.
	 * @return the list of {@link org.springframework.aop.Advisor} beans
	 * @see #isEligibleBean
	 */
	public List<Advisor> buildAspectJAdvisors() {
		// 下面的循环会遍历容器中所有的bean，
		//然后将含有 @Aspect注解的bean放到这个集合中 aspectNames
		// （
		//		走 testSpringConfig 配置类的createBean的时候会第一次创建切面集合
		//		即 testSpringConfig 配置类在走创建bean的时候才会第一次加载这个方法
		//
		// 		testSpringConfig 的时候首次加载切面， 此时 List<String> aspectNames = this.aspectBeanNames; 为空
		//		testSpringConfig 配置类，是除去其他beanProcessor首个被加载的bean，
		//		第一个执行的bean会去初始化切面的逻辑，后续的bean加载走到这里都是拿到缓存的值
		//		即后续进来的bean，this.aspectBeanNames不为null
		// 	）
		List<String> aspectNames = this.aspectBeanNames;

		// todo
		//  当程序走到创建 testSpringConfig 配置类的bean的时候，第一次执行这个方法
		//  此时this.aspectBeanNames;为null，会进行创建切面，并会将切面集合存储到 advisorsCache 对象中
		//  后续bean通过createBean方法进来时，this.aspectBeanNames;不为null，则直接会走下面if的逻辑，从 advisorsCache 拿到切面集合返回
		//
		// advisorsCache对象  ==> private final Map<String, List<Advisor>> advisorsCache = new ConcurrentHashMap<>();
		//	key：切面的beanName
		// value:切面中的切点方法集合
		if (aspectNames == null) {
			synchronized (this) {
				aspectNames = this.aspectBeanNames;
				if (aspectNames == null) {
					List<Advisor> advisors = new ArrayList<>();
					aspectNames = new ArrayList<>();
					// 1.遍历所有bean
					String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
							this.beanFactory, Object.class, true, false);
					for (String beanName : beanNames) {
						if (!isEligibleBean(beanName)) {
							continue;
						}
						// We must be careful not to instantiate beans eagerly as in this case they
						// would be cached by the Spring container but would not have been weaved.
						Class<?> beanType = this.beanFactory.getType(beanName, false);
						if (beanType == null) {
							continue;
						}
						//todo
						// 2.判断是否是切面
						//   判断当前bean是否含有 @Aspect 注解 （当前bean没有 @Aspect注解，，后面的方法就不会执行，上面的for就会走下一个bean去了）
						// AnnotationUtils.findAnnotation(clazz, Aspect.class)
						if (this.advisorFactory.isAspect(beanType)) {
							aspectNames.add(beanName);
							AspectMetadata amd = new AspectMetadata(beanType, beanName);
							if (amd.getAjType().getPerClause().getKind() == PerClauseKind.SINGLETON) {
								MetadataAwareAspectInstanceFactory factory =
										new BeanFactoryAspectInstanceFactory(this.beanFactory, beanName);
								// todo
								//  3.获取所有的切面列表
								//  factory == > aopConfig bean,就是标有 @Aspect注解的bean对象
								//  这个方法就是 拿到标有  @Aspect注解的bean，解析其中的方法，并放回所有符合的切面集合
								//  并最后放到 advisorsCache 中 (this.advisorsCache.put(beanName, classAdvisors);)
								List<Advisor> classAdvisors = this.advisorFactory.getAdvisors(factory);
								if (this.beanFactory.isSingleton(beanName)) {
									//todo
									// 4.将Aspect 的切面列表到缓存 advisorsCache 中
									//	 后续对切面数据的获取，都是从缓存 advisorsCache 中拿到
									//  key : beanName ,value : 切面集合
									this.advisorsCache.put(beanName, classAdvisors);
								}
								else {
									this.aspectFactoryCache.put(beanName, factory);
								}
								advisors.addAll(classAdvisors);
							}
							else {
								// Per target or per this.
								if (this.beanFactory.isSingleton(beanName)) {
									throw new IllegalArgumentException("Bean with name '" + beanName +
											"' is a singleton, but aspect instantiation model is not singleton");
								}
								MetadataAwareAspectInstanceFactory factory =
										new PrototypeAspectInstanceFactory(this.beanFactory, beanName);
								this.aspectFactoryCache.put(beanName, factory);
								advisors.addAll(this.advisorFactory.getAdvisors(factory));
							}
						}
					}
					this.aspectBeanNames = aspectNames;
					return advisors;
				}
			}
		}

		if (aspectNames.isEmpty()) {
			return Collections.emptyList();
		}
		List<Advisor> advisors = new ArrayList<>();
		for (String aspectName : aspectNames) {
			// AspectJAwareAdvisorAutoProxyCreator
			//
			/**
			 * <p> 关于缓存 advisorsCache 的问题 </p>
			 * 	因为第一次缓存切面集合，以及后续从缓存中拿切面集合
			 * 	都是通过 AnnotationAwareAspectJAutoProxyCreator 这个类来的 ( 通过 @EnableAspectJAutoProxy 注解 @import到容器中的)
			 * 	而这个类是早在  registerBeanPostProcessors(beanFactory); 的时候
			 * 	就和其他 beanPostProcessors 一起注入到了容器中的，是单例对象
			 * 	所以这里存放和获取其实都是拿的同一个对象的变量值
			 */
			List<Advisor> cachedAdvisors = this.advisorsCache.get(aspectName);
			if (cachedAdvisors != null) {
				//   this.advisorsCache  ==> key : beanName ,value : 切面集合
				//  配置类 testSpringConfig 在创建的时候会走上面的逻辑（this.aspectBeanNames当时为null）创建切面集合，
				//  （
				//  	todo 这里第一次走这个方法的配置类 testSpringConfig 在创建完切面集合后，会给this.aspectBeanNames赋值，并给 this.advisorsCache 赋值
				//   ）
				//  这里的逻辑是供后续非配置类（this.aspectBeanNames不为null）的获取第一次创建的切面集合逻辑
				advisors.addAll(cachedAdvisors);
			}
			else {
				MetadataAwareAspectInstanceFactory factory = this.aspectFactoryCache.get(aspectName);
				advisors.addAll(this.advisorFactory.getAdvisors(factory));
			}
		}
		return advisors;
	}

	/**
	 * Return whether the aspect bean with the given name is eligible.
	 * @param beanName the name of the aspect bean
	 * @return whether the bean is eligible
	 */
	protected boolean isEligibleBean(String beanName) {
		return true;
	}

}
