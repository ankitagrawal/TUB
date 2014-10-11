package com.myproject.test.spring;

import org.springframework.stereotype.Component;


@Component
public class Filewritter {
	public void write(String text){
		System.out.println("in file " + text);
	}
	

}
