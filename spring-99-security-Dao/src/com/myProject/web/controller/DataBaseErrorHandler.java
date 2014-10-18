package com.myProject.web.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DataBaseErrorHandler {
	
	@ExceptionHandler(DataAccessException.class)
	public  String handleDatabaseException(DataAccessException ex){
		
		System.out.println(" i am in DataAccessException");
		ex.printStackTrace();
		return "error";
	}

}
