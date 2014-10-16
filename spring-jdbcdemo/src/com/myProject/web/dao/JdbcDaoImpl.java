package com.myProject.web.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class JdbcDaoImpl {

	
	public Circle getCircle(int id){
		Connection conn = null;
		try{
		String driver = "";
		Class.forName(driver).newInstance();
		conn= (Connection) DriverManager.getConnection("");
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
