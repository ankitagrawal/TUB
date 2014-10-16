package com.myProject.web.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.PreparedStatement;
@Component
public class JdbcDaoImpl {
	
	// still this is a very old method not very efficient not implement connection pooling one example is dbcp
    @Autowired    
	private DataSource datasource;
	
	public Circle getCircle(int id){
		java.sql.Connection conn = null;
		try{
		/*String driver = "";
		Class.forName(driver).newInstance();
		conn= (Connection) DriverManager.getConnection("");*/
		// above lines is not going to change so we should make it configuration, connection is standard interface
	    conn= datasource.getConnection();		
			
		PreparedStatement ps = (PreparedStatement) conn.prepareStatement("select * from circle where id = ?");
		ps.setInt(1, id);
		Circle circle = null;
		
		ResultSet rs = ps.executeQuery();
		if (rs.next()){
			circle = new Circle(id, rs.getString("name"));
		}
		rs.close();
		ps.close();
		return circle;
		
		}catch(Exception e){
			throw new RuntimeException();
		}finally{
			try{
				conn.close();
			}catch(SQLException e){}
		
		}
	}
}
