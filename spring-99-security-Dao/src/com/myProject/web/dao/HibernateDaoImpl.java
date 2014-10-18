package com.myProject.web.dao;


import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateDaoImpl {

	
	private SessionFactory sessionFactory;
	
	public int getCircleCount(){
		String hql="select count(c) from Circle c";
	   Query query = getSessionFactory().openSession().createQuery(hql);
	   return ((Long)query.uniqueResult()).intValue();
	}

	public int getUserCount(){
		String hql="select count(u) from User u";
	   Query query = getSessionFactory().openSession().createQuery(hql);
	   return ((Long)query.uniqueResult()).intValue();
	}
	
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
}
