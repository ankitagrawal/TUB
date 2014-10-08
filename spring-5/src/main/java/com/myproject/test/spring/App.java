package com.myproject.test.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext applicationContext = new FileSystemXmlApplicationContext("beans.xml");
       Person p = (Person)applicationContext.getBean("person");
       p.speak();
	}

}
