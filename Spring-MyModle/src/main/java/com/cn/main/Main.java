package com.cn.main;

import com.cn.config.TestSpringConfig;
import com.cn.service.AService;
import com.cn.service.XService;
import com.cn.service.YAopService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 核心模块
 * Spring Core：提供了Spring框架的基础设施，包括IoC容器、资源管理、事件机制等功能，是Spring框架最核心的模块之一。
 * Spring Context：提供了Spring IoC容器和Spring AOP框架的实现，是Spring框架的核心模块之一。
 * Spring AOP：封装了基于AspectJ的AOP实现，允许将面向切面的编程应用到Spring应用程序中。
 * Spring JDBC：提供了对JDBC的封装，简化了JDBC的使用方式。
 * Spring ORM：提供了对ORM框架的支持，包括Hibernate、JPA等。
 */
public class Main {

	/**
	 * 启动异常解决：
	 * 1.启动报错尝试将file - setting - compiler - java compiler - project bytecode version 设置成11
	 * 2.报错，jdk.jfr.Category不存在： File - Settings - Build, Execution, Deployment - Build Tools -Gradle  右边的Gradle JVM改为11
	 * 	 参考：https://blog.csdn.net/weixin_43872111/article/details/119525655
	 * 3.编译报错 Task :kotlin-coroutines:compileKotlin FAILED
	 *   将父项目的build.gradle中的compileKotlin - kotlinOptions - languageVersion，apiVersion由1.5改为1.4
	 * 4.编译报错：
	 *   Kotlin: warnings found and -Werror specified
	 *   Errors occurred while compiling module 'spring.spring-beans.main'
	 *   直接点击超链接进到项目模块配置，选择Kotlin - Additional command line parameters - 删除后面的 -Werror
	 *   参考： https://blog.csdn.net/qq_39410381/article/details/121954762
	 * 5.程序包org.springframework.cglib.core.internal不存在
	 *   gradle先clean，再build下解决
	 *   参考：https://blog.csdn.net/jiguang127/article/details/122172676
	 * 6.main方法启动报GBK乱码
	 *   Settings - Editor - File Encodings
	 *   下方UTF-8旁边 Transparent native-to-ascii conversion 的勾勾上
	 *   1.参考：https://blog.csdn.net/MoveCode/article/details/107869306
	 *   2.参考：https://blog.csdn.net/csdnlijingran/article/details/90231832
	 */
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestSpringConfig.class);

		System.out.println();
		System.out.println("==========================");
		for (String beanDefinitionName : context.getBeanDefinitionNames()) {
			System.out.println(beanDefinitionName);
		}
		System.out.println("==========================");

//		AService aService = (AService) context.getBean("AService");
//		System.out.println(aService);

		XService xService = (XService) context.getBean("xService");
		System.out.println(xService);

//		//多例bean的循环依赖复现，一定是要在手动getBean的时候才会复现
//		// 因为多例bean在容器创建的时候不会创建，只有在调用的时候才会创建
//		// todo 所以spring没有解决多例bean的循环依赖问题，循环依赖只是针对于单例bean
//		AService aService = context.getBean("AService", AService.class);


		/**
		 * 手动关闭容器，就会走bean的销毁逻辑，
		 * 	1.实现了DisposableBean接口的类，调用重写的方法destroy
		 * 	2.含有@PreDestroy注解的方法，调用改方法
		 */
//		context.close();


//		User user = (User) context.getBean("userFactoryBean");
//		System.out.println(user);


//		UserFactoryBean userFactoryBean = (UserFactoryBean) context.getBean("&userFactoryBean");
//		System.out.println(userFactoryBean);

		//No bean named 'user' available
		//容器中只有 userFactoryBean ，没有user，如果user对象就需要通过userFactoryBean来拿到user对象
//		User user = (User) context.getBean("user");
//		System.out.println(user);

//		user.setId("xxx");
//		user.setName("123");
//
//		user = (User) context.getBean("user");
//		System.out.println(user);


//		//容器中User类型的bean有两个
//		UserService userService = (UserService) context.getBean("userService");
//		System.out.println(userService.getUser());
//		//但是容器中beanName为user的只有一个，注册的时候两个beanName相同，则容器中会留下先创建的beanName的bean对象值（不会报错）
//		User user = (User) context.getBean("user");
//		System.out.println(user);

		// 手动关闭容器，将容器中需要销毁的bean移除1 2 3级缓存
//		context.close();

//		/**
//		 *   这里的 yAopService 是cglib生成的代理对象
//		 *   对象中的第一个参数中
//		 *   	CGLIB$CALLBACK_0 --> advised --> advisors
//		 *   封装了aop中定义的切面方法集合
//		 *   	1.around
//		 *   	2.logstart
//		 *   	3.logend
//		 */
//		YAopService yAopService = context.getBean("YAopService", YAopService.class);
//		// 代理对象 执行方法，会被cglib的拦截器 intercept拦截
//		yAopService.testAop();

	}
}
