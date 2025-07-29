package com.cn.service;

import org.springframework.stereotype.Service;

/**
 * 接口上添加@Service是不会注册到bean的，一定需要在实现类上@Service才会注册都bean
 */
//@Service("testService")
public class TestServiceImpl implements TestService{
}
