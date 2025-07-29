package com.cn.service;

import com.cn.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
public class UserService {

	@Autowired
	public List<User> user;

	public List<User> getUser() {
		return user;
	}
}
