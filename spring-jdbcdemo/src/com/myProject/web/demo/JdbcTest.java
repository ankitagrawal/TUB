package com.myProject.web.demo;

import com.myProject.web.dao.Circle;
import com.myProject.web.dao.JdbcDaoImpl;

public class JdbcTest {
	
	public static void main(String[] args) {
		
		JdbcDaoImpl jd = new JdbcDaoImpl();
		
		Circle circle= jd.getCircle(1);
		System.out.println(circle.getName());
	}

}
