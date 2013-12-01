package com.hk.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HCDBConnector {

	String jdbcDriver = "com.mysql.jdbc.Driver";
	String urlProd = "jdbc:mysql://localhost/HealthChakra";	
	String urlDev = "jdbc:mysql://localhost/HealthChakraDev";	
	String userName = "hcadmin";
	String password = "admin2K9!";

	Connection connection = null;

	public Connection getDBConnection() throws ClassNotFoundException,
			SQLException {
		Class.forName(jdbcDriver);
		
		connection = DriverManager.getConnection(urlProd, userName, password);		
		//connection = DriverManager.getConnection(urlDev, userName, password);
		
		return connection;
	}
}
