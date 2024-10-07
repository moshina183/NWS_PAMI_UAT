package com.vinsys.diam.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.vinsys.diam.esb.IConstants;

@Component
public class DBUtils {
	
	@Value("${esb-db-classname}")
	private String classname;

	@Value("${esb-db-url}")
	private String dbUrl;

	@Value("${esb-db-username}")
	private String dbUsername;

	@Value("${esb-db-password}")
	private String dbPassword;
	
	@Value("${amr-oracle-db-classname}")
	private String amrOracleclassname;

	@Value("${amr-oracle-db-url}")
	private String amrOracledbUrl;

	@Value("${amr-oracle-db-username}")
	private String amrOracledbUsername;

	@Value("${amr-oracle-db-password}")
	private String amrOracledbPassword;
	
	@Value("${timestamp.duration}")
	private String timestampDiff;
	
	@Value("${amr-sql-server-db-classname}")
	private String sqlServerClassname;
	
	@Value("${amr-sql-server-dbpassword}")
	private String sqlServerPassword;
	
	@Value("${amr-sql-server-dbusername}")
	private String sqlServerUsername;
	
	@Value("${amr-sql-server-dbpassword}")
	private String sqlServerPwd;
	
	@Value("${amr-sql-server-dburl}")
	private String amrSqlServerUrl;
	
	@Value("${content-length}")
	private String contentLength;
	
	@Value("${mmintegration-oracle-db-classname}")
	private String mmintegrationOracleClassname;
	
	@Value("${mmintegration-oracle-db-url}")
	private String mmintegrationOracleDbUrl;
	
	@Value("${mmintegration-oracle-db-username}")
	private String mmintegrationOracleDbUsername;
	
	@Value("${mmintegration-oracle-db-password}")
	private String mmintegrationOracleDbPassword;
	
	public static Map<String, String> properties = new HashMap<>();

	@Bean
	public void loadProperties() {
		System.out.println("Loading properties......");
		properties.put(IConstants.ORACLE_CLASSNAME, classname);
		properties.put(IConstants.ORACLE_DBURL, dbUrl);
		properties.put(IConstants.ORACLE_DBUSERNAME, dbUsername);
		properties.put(IConstants.ORACLE_DBPASSWORD, dbPassword);
		properties.put("timestampDifference", timestampDiff);
		properties.put(IConstants.AMR_ORACLE_CLASSNAME, amrOracleclassname);
		properties.put(IConstants.AMR_ORACLE_DBURL, amrOracledbUrl);
		properties.put(IConstants.AMR_ORACLE_USERNAME, amrOracledbUsername);
		properties.put(IConstants.AMR_ORACLE_PASSWORD, amrOracledbPassword);
		properties.put(IConstants.AMR_SQLSERVER_CLASSNAME, sqlServerClassname);
		properties.put(IConstants.AMR_SQLSERVER_DBURL, amrSqlServerUrl);
		properties.put(IConstants.AMR_SQL_SERVER_DBUSERNAME, sqlServerUsername);
		properties.put(IConstants.AMR_SQL_SERVER_DBPASSWORD, sqlServerPassword);
		properties.put(IConstants.CONTENT_LENGTH, contentLength);
		properties.put(IConstants.MMINTEGRATION_ORACLE_CLASSNAME, mmintegrationOracleClassname);
		properties.put(IConstants.MMINTEGRATION_ORACLE_DBURL, mmintegrationOracleDbUrl);
		properties.put(IConstants.MMINTEGRATION_ORACLE_USERNAME, mmintegrationOracleDbUsername);
		properties.put(IConstants.MMINTEGRATION_ORACLE_PASSWORD, mmintegrationOracleDbPassword);
		
		System.out.println("Properties loaded");
	}
}
