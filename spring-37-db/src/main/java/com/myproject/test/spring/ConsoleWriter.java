package com.myproject.test.spring;

import org.springframework.stereotype.Component;

@Component
public class ConsoleWriter {
	
	public void write(String text){
		System.out.println("in console " + text);
	}

}
