package com.myproject.test.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class App {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//ApplicationContext applicationContext = new FileSystemXmlApplicationContext("src/main/java/com/myproject/test/spring/beans/beans.xml");
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("com/myproject/test/spring/beans/beans.xml");
		Person p = (Person)applicationContext.getBean("person");
		System.out.println(p);
		
		Address ad = (Address)applicationContext.getBean("address");
		System.out.println(ad);
		
		Logger log = (Logger)applicationContext.getBean("logger");
		log.writeConsole("hello console");
		log.writeFile("hi");
		System.out.println(log);
		
		Robot rb = (Robot)applicationContext.getBean("robot");
		rb.speak();
       p.speak();
	}

}
