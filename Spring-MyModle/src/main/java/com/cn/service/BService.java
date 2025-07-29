package com.cn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
//@Scope("prototype")
//@Scope("singleton")
public class BService {

    @Autowired
    AService aService;

//	@Autowired
//	public BService(AService aService) {
//		this.aService = aService;
//	}

	public AService getaService() {
		return aService;
	}

	public void setaService(AService aService) {
		this.aService = aService;
	}
}
