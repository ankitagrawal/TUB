package com.myproject.test.spring;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class OfferDao {

	 private JdbcTemplate jdbc;
	public List<Offer> getOffers(){
		return null;
	}
	public JdbcTemplate getJdbc() {
		return jdbc;
	}
	@Autowired
	public void setDataSource(DataSource jdbc) {
		this.jdbc = new JdbcTemplate(jdbc);
	}
	
	
}
