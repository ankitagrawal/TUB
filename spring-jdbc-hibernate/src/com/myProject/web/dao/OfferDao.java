package com.myProject.web.dao;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class OfferDao extends BaseDaoImpl {
	


	public OfferDao() {
		System.out.println(" successfully loaded offer Dao");
	}
	
	public List<String> getOffers(){
	 String [] a = {"abc" , "pqr"};
	  
	  return Arrays.asList( a);
	}
 
	
	public void saveCircle(Circle circle){
		System.out.println(" i m in dao");
		getHibernateTemplate().save(circle);
		
	}

}
