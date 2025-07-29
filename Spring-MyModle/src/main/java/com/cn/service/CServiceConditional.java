package com.cn.service;

import com.cn.config.TestConditionImpl;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/*
	@Conditional({TestConditionImpl.class})
		1.根据参数类TestConditionImpl来判断是否注入
		2.参数要实现Condition接口，并重写matches方法
		3.matches方法返回false则表示不注入，为true则注入
		4.多个@Conditional也是同理，只要有一个为false不注入，对应的类就不会注入到ioc容器中
	类比springboot中的很多类似@ConditionalOnMissBean（A.class） -->  如果配置类中不存在A类则注入
*/
@Conditional({TestConditionImpl.class})
@Component
public class CServiceConditional {
}
