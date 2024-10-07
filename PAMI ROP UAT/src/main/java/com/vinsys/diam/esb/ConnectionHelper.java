package com.vinsys.diam.esb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.vinsys.diam.utils.DBUtils;

/***
 * 
 * @author Amey Gangakhedkar
 *
 */
@Component
public class ConnectionHelper {
	
	
	public Connection getConnection() {
		Connection con = null;
		try {
			Class.forName(DBUtils.properties.get(IConstants.ORACLE_CLASSNAME));
			con = DriverManager.getConnection(DBUtils.properties.get(IConstants.ORACLE_DBURL),
					DBUtils.properties.get(IConstants.ORACLE_DBUSERNAME),
					DBUtils.properties.get(IConstants.ORACLE_DBPASSWORD));
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public Connection getAmrOracleConnection() {
		Connection con = null;
		try {
			Class.forName(DBUtils.properties.get(IConstants.AMR_ORACLE_CLASSNAME));
			con = DriverManager.getConnection(DBUtils.properties.get(IConstants.AMR_ORACLE_DBURL),
					DBUtils.properties.get(IConstants.AMR_ORACLE_USERNAME),
					DBUtils.properties.get(IConstants.AMR_ORACLE_PASSWORD));
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public Connection getMMIntegrationOracleConnection() {
		Connection con=null;
		try {
			Class.forName(DBUtils.properties.get(IConstants.MMINTEGRATION_ORACLE_CLASSNAME));
			con = DriverManager.getConnection(DBUtils.properties.get(IConstants.MMINTEGRATION_ORACLE_DBURL),
			      DBUtils.properties.get(IConstants.MMINTEGRATION_ORACLE_USERNAME),
			      DBUtils.properties.get(IConstants.MMINTEGRATION_ORACLE_PASSWORD));
			      
		}catch (SQLException | ClassNotFoundException e) {
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
