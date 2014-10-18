package com.myProject.web.demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.myProject.web.dao.Circle;
import com.myProject.web.dao.HibernateDaoImpl;
import com.myProject.web.dao.JdbcDaoImpl;

public class JdbcTest {
	
	
	
	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-dao.xml");
		
		HibernateDaoImpl jd =context.getBean("hibernateDaoImpl", HibernateDaoImpl.class);
		System.out.println(" i am calling dj");
		int  count= jd.getCircleCount();
		System.out.println(count);
	}

}
