package com.cn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("xService")
public class XService {

	@Value("100")
	private String id;

	/**
	 * static属性不能被注入值
	 *
	 * 1.当前bean进行属性赋值的时候，会遍历当前类中所有字段
	 * 2.判断字段是否含有 Autowired，Value，Inject注解
	 * 3.判断当前字段是否是static修饰
	 * 4.没问题就会缓存到 AutowiredAnnotationBeanPostProcessor --> this.injectionMetadataCache 属性中
	 * 5.后续通过反射对其中的字段进行赋值
	 *
	 * 总结：先判断哪些字段需要注入，在对需要注入的字段属性进行获取值/创建对象
	 */
	@Value("200")
	private static String id2;

	@Autowired
	AService aService;

	@Value("原始值")
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId2() {
		return id2;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}

	public AService getaService() {
		return aService;
	}

	public void setaService(AService aService) {
		this.aService = aService;
	}

	@Override
	public String toString() {
		return "XService{" +
				"id='" + id + '\'' +
				"id2='" + id2 + '\'' +
				", aService=" + aService +
				", name='" + name + '\'' +
				'}';
	}
}
