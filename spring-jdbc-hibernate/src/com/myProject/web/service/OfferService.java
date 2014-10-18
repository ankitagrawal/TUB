package com.myProject.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.myProject.web.dao.Circle;
import com.myProject.web.dao.OfferDao;

@Component
public class OfferService {
	
	@Autowired
	OfferDao offerDao;
	
	public List<String> getOffers(){
		System.out.println(" i m in service");
		return offerDao.getOffers();
	}
	
	
	

	public void saveCircle(Circle circle){
		System.out.println(" i m in service");
		offerDao.saveCircle(circle);
	}

}
