package com.cn.config;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: lijiaxing
 * @Date: 2021/12/6 14:53
 * @description
 *
 *  @EnableAspectJAutoProxy 会像容器中注册一个bean AnnotationAwareAspectJAutoProxyCreator
 *  (
 *  	beanName：org.springframework.aop.config.internalAutoProxyCreator
 *  	beanclass：	AnnotationAwareAspectJAutoProxyCreator
 *  )
 *  AnnotationAwareAspectJAutoProxyCreator 后续就是处理aop的处理器，用于生成代理对象，
 *  在初始化时  applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName); 后置执行这个类的创建代理对象的逻辑
 */
// 事务注解会像容器中导入 TransactionManagementConfigurationSelector挑选器，
// 实现和aop思路大概一致，存放在容器的是代理对象
//@EnableTransactionManagement
//@EnableAspectJAutoProxy
//@Aspect
//@Configuration
public class AopConfig {


    //拦截所有public修饰
    //抽取公共部分
    /**
     *  更新：
     *       @Pointcut("execution(public * com.example.demo.controller.*.*(..)) ")
     *       这样写只能拦截到com.example.demo.controller包下面的所有类，但是拦截不到子包的类，就比如com.example.demo.controller.mqconroller包里面的类是拦截不到的
     *       @Pointcut("execution(public * com.example.demo.controller..*(..)) ")
     *       这样写就可以拦截到包下所有的类，（包括子包里面的类）
     *
     *  更新：
     *      @Before("execution(public String com.example.demo.control.AopTest.test())")
     *                      访问修饰符 返回值  包名.包名.包名…              类名.方法名(参数列表)
     *
     *      //多个方法同时需要加入横切方法就可以直接 .*,不用指定方法名
     *      @Before("execution(public String com.example.demo.control.AopTest.*(..))")
     */
    @Pointcut("execution(public * com.cn.service.YAopService.*(..))")
    public void Pointcut() {
    }


    @Before("Pointcut()")
    public void logstart() {
        System.out.println("方法调用前:logstart()");
    }

    @After("Pointcut()")
    public void logend() {
        System.out.println("方法调用后:logend() ");
    }

    @Around("Pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("环绕方法Pointcut()");
		return joinPoint.proceed();
	}



//    @AfterReturning(returning = "rvt",
//            pointcut = "(within(@org.springframework.web.bind.annotation.RestController *)" +
//                    " || within(@org.springframework.stereotype.Controller *))")
//    public Object afterExec(JoinPoint joinPoint, Object rvt) {
//		System.out.println("方法执行后:afterExec()");
//        return rvt;
//    }



}
