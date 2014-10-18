package com.myProject.web.dao;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class SimpleJdBcDaoImpl extends SimpleJdbcDaoSupport {
	
	
	
	public int getCircleCount(){
		 String sql = "Select Count(*) from circle";
	//	 jd.setDataSource(getDatasource());
		return this.getJdbcTemplate().queryForInt(sql);
	}



}
