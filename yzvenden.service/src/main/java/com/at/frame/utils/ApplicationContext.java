package com.at.frame.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public final class ApplicationContext implements ApplicationContextAware {
	
	private static org.springframework.context.ApplicationContext ac;

	public void setApplicationContext(
			org.springframework.context.ApplicationContext applicationContext)
			throws BeansException {
		ac = applicationContext;
	}
	
	public static org.springframework.context.ApplicationContext getApplicationContext(){
		return ac;
	}
	
	public static <T> T getBean(Class<T> clazz){
		return (T)ac.getBean(clazz);
	}
	
	public static <T> T getBean(String beanName,Class<T> clazz){
		return (T)ac.getBean(beanName, clazz);
	}
	
	public static Object getBean(String beanName){
		return ac.getBean(beanName);
	}

}
