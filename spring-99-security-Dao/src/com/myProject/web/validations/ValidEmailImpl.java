package com.myProject.web.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.validator.routines.EmailValidator;

public class ValidEmailImpl implements ConstraintValidator<ValidEmail, String> {

	private int min ;
	@Override
	public void initialize(ValidEmail constraintAnnotation) {
		// TODO Auto-generated method stub
		min =  constraintAnnotation.min();
		
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		if(email.length()<min){
			return false;
		}
		if(!EmailValidator.getInstance(false).isValid(email)){
			return false;
		}
		
		return true;
	}
	
	

}
