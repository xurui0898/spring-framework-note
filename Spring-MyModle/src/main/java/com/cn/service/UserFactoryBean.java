package com.cn.service;

import com.cn.pojo.User;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

//@Component
public class UserFactoryBean implements FactoryBean<User> {

	@Override
	public User getObject() throws Exception {
		return new User("1", "Jason");
	}

	@Override
	public Class<?> getObjectType() {
		return User.class;
	}

	@Override
	public String toString() {
		return "UserFactoryBean{}";
	}
}
