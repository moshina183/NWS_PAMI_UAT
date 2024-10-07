package com.vinsys.diam.esb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.vinsys.diam.utils.DBUtils;

public class ConnectionHelperSQLServer {
	static {
		try {
			Class.forName(DBUtils.properties.get(IConstants.AMR_SQLSERVER_CLASSNAME));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public Connection getConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(DBUtils.properties.get(IConstants.AMR_SQLSERVER_DBURL)
					,DBUtils.properties.get(IConstants.AMR_SQL_SERVER_DBUSERNAME),
					DBUtils.properties.get(IConstants.AMR_SQL_SERVER_DBPASSWORD));
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return con;
	}

	public void releaseConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}


}
