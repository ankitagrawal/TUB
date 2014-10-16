package com.myproject.test.spring;

import org.springframework.stereotype.Component;

@Component
public class Robot {
	
	private String id;
	private String speech;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSpeech() {
		return speech;
	}
	public void setSpeech(String speech) {
		this.speech = speech;
	}
	
	
	public void speak(){
		System.out.println( id + ":" + speech);
	}
	

}
