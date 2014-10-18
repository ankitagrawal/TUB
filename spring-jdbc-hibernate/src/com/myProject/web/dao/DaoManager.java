package com.myProject.web.dao;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DaoManager {
 
	private  static final HibernateDaoImpl jd;
	
	static{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com/myProject/web/config/spring-doa.xml");
		
		 jd =context.getBean("hibernateDaoImpl", HibernateDaoImpl.class);
	}
	
	
	public static HibernateDaoImpl getHibernateTemplate() {
		return jd;
	}
}
