package com.hk.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnector {

  String jdbcDriver = "com.mysql.jdbc.Driver";
  String datasource = "java:comp/env/jdbc/healthkartds";

  Connection connection = null;

  public Connection getDBConnection() throws ClassNotFoundException,
      SQLException, NamingException {

    Class.forName(jdbcDriver);
    Context ctx = new InitialContext();
    DataSource ds = (DataSource) ctx.lookup(datasource);
    connection = ds.getConnection();

    return connection;
  }

}
