package com.myProject.web.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class OfferDao {
	
	private NamedParameterJdbcTemplate jdbc;

	public OfferDao() {
		System.out.println(" successfully loaded offer Dao");
	}
	
	@Autowired
	public void setDataSource(DataSource jdbc){
		this.jdbc = new NamedParameterJdbcTemplate(jdbc);
	}
 

}
