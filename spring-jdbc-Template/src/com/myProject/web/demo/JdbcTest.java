package com.myProject.web.demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.myProject.web.dao.Circle;
import com.myProject.web.dao.JdbcDaoImpl;

public class JdbcTest {
	
	
	
	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		JdbcDaoImpl jd =context.getBean("jdbcDaoImpl", JdbcDaoImpl.class);
		
		
		Circle circle= jd.getCircle(1);
		System.out.println(circle.getName());
	}

}
