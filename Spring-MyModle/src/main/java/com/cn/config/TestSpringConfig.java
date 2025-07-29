package com.cn.config;

import com.cn.pojo.User;
import com.cn.service.XService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


/**
 * @Description: 容器构造的配置类是可以不需要定义为bean对象，默认是会加载到容器对象中的
 * 但如果不加上 @ComponentScan(basePackages = {"com.cn"}) 扫描包，会导致其他标记 @Component的注解无法加载到ioc容器
 * @time: 2020/7/21 15:46
 */
//@Configuration
@ComponentScan(basePackages = {"com.cn"})
//@ComponentScan(basePackages = {"com.test"})
public class TestSpringConfig {

	/**
	 * 程序定义的时候可以存在两个相同的beanName，容器默认会选择先执行的放到容器中
	 * <p>
	 * 虽然都是返回User对象，但和 UserFactoryBean 在容器中的beanName不同
	 */
//	@Bean
//	public User user() {
//		return new User("a", "a");
//	}
//
//	@Bean
//	public User user2() {
//		return new User("b", "b");
//	}
//
//
//	@Bean
//	public XService xService() {
//		return new XService();
//	}

}
