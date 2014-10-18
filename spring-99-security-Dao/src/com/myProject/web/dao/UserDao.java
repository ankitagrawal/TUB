package com.myProject.web.dao;

import javax.transaction.Transaction;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao  {
	

	
	public void create(User user){
	 	System.out.println(" hi I AM  CREATING USER");
	    HibernateDaoImpl jd =	DaoManager.getHibernateTemplate();
	    Session sx = jd.getSessionFactory().openSession();
	    sx.beginTransaction();
	    sx.save(user);
        sx.getTransaction().commit();	    
	    
	}
 

}
