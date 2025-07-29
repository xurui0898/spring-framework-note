package com.test.config;

import com.cn.service.XService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TestConfig {

	@Bean
	public XService xService() {
		return new XService();
	}

}
