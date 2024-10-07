package com.vinsys.diam.processors;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vinsys.diam.core.ISQLConstant;
import com.vinsys.diam.esb.ConnectionHelper;
import com.vinsys.diam.esb.ConnectionHelperSQLServer;
import com.vinsys.diam.esb.IConstants;
import com.vinsys.diam.model.Alert;
import com.vinsys.diam.model.TXNAuditLogs;

public class InsertionHelper {

	private static final Logger logger = LoggerFactory.getLogger(InsertionHelper.class);

	public String insertIntoDb(List<Alert> amrAlarmPayload) throws SQLException {

		ConnectionHelper conHelper = new ConnectionHelper();
		Connection con = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		String query = ISQLConstant.INSERT_INTO_ORACLE_DB;
		Alert payload = null;
		int recordUpdated = 0;

		try {
			con = conHelper.getAmrOracleConnection();
			for (int i = 0; i < amrAlarmPayload.size(); i++) {
				payload = amrAlarmPayload.get(i);

				try {
					logger.info("service point" + payload.getServicePoint());
					PreparedStatement ps = con.prepareStatement(query);
					ps.setString(1, payload.getServicePoint());
					ps.setString(2, payload.getBadgeNumber());
					ps.setString(3, payload.getAlertType());
					ps.setString(4, payload.getAlertDesc());
					
					java.util.Date date = simpleDateFormat.parse(payload.getMsgDate());
					System.out.println("After conversion :"+date);
					ps.setDate(5, new Date(date.getTime()));
					//ps.setString(5, payload.getMsgDate());
					ps.setString(6, payload.getMsgText());
					ps.setString(7, payload.getBatchId());
					logger.info("Before Execute Update");
					recordUpdated = ps.executeUpdate();
					logger.info("Execute Update Executed {} ",recordUpdated);
					logger.info("Query is {}",query);
					ps.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			System.out.println("record inserted ");
			conHelper.releaseConnection(con);
		}

		return "Record inserted successfully";
	}

	public String insertIntoSqlServerDb(List<Alert> amrAlarmPayload) throws SQLException {

		// ConnectionHelper conHelper=new ConnectionHelper();
		ConnectionHelperSQLServer conHelper = new ConnectionHelperSQLServer();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Connection con = null;

		String query = ISQLConstant.INSERT_INTO_SQLSERVER;
		Alert payload = null;
		int x = 0;

		try {
			con = conHelper.getConnection();
			for (int i = 0; i < amrAlarmPayload.size(); i++) {
				payload = amrAlarmPayload.get(i);
				System.out.println("service point" + payload.getServicePoint());
				
				try {
					PreparedStatement ps = con.prepareStatement(query);
					ps.setString(1, payload.getServicePoint());
					ps.setString(2, payload.getBadgeNumber());
					ps.setString(3, payload.getAlertType());
					ps.setString(4, payload.getAlertDesc());
					
					java.util.Date date = simpleDateFormat.parse(payload.getMsgDate());
					System.out.println("After conversion :"+date);
					ps.setDate(5, new Date(date.getTime()));
					
					//ps.setString(5, payload.getMsgDate());
					ps.setString(6, payload.getMsgText());
					ps.setString(7, payload.getBatchId());

					ps.executeUpdate();
					ps.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally {
			conHelper.releaseConnection(con);
		}
		System.out.println("record inserted ");
		return "Record inserted successfully";
	}
	
	
	public String insertMMDataIntoOracleDb(Element element) throws SQLException, DOMException, ParseException {

		ConnectionHelper conHelper = new ConnectionHelper();
		Connection con = null;
		
		String query = ISQLConstant.INSERT_INTO_MM_ORACLE_DB;
		
		int recordUpdated = 0;
	
		Date currentDate = new Date(Calendar.getInstance().getTime().getTime());
		
        String workStartDateIssued=element.getElementsByTagName("RECEIPT_DATE").item(0).getTextContent();
        String CRNumber=element.getElementsByTagName("OWNER_PARTY_NUMBER").item(0).getTextContent();
        String workCompletionDateIssued=element.getElementsByTagName("LETTER").item(0).getTextContent();
        String serviceRequestType=element.getElementsByTagName("SERVICE_REQUEST_TYPE").item(0).getTextContent();
        String ownerId=element.getElementsByTagName("OWNER_PARTY_NUMBER").item(0).getTextContent();
        String substringOfOwnerId=ownerId.substring(2);
        String ownerCode=ownerId.substring(0, 2);
 
		try {
			con = conHelper.getMMIntegrationOracleConnection();
			
				try {
					logger.info("Auditing into requests table");
					
					System.out.println("Inside getMMDataIntoOracleDb Method");
					PreparedStatement ps = con.prepareStatement(query);
					ps.setString(1,element.getElementsByTagName("SERVICE_REQUEST_NUMBER").item(0).getTextContent()!=null?element.getElementsByTagName("SERVICE_REQUEST_NUMBER").item(0).getTextContent():"");
					ps.setDate(2, currentDate);
					ps.setString(3,element.getElementsByTagName("CONSTRUCTION_PERMIT_NUMBER").item(0).getTextContent()!=null?element.getElementsByTagName("CONSTRUCTION_PERMIT_NUMBER").item(0).getTextContent():"");
					ps.setString(4,element.getElementsByTagName("OWNER_PARTY_NUMBER").item(0).getTextContent()!=null?substringOfOwnerId:"");
					ps.setString(5,"");
					ps.setString(6,element.getElementsByTagName("OWNER_PARTY_NAME").item(0).getTextContent()!=null?element.getElementsByTagName("OWNER_PARTY_NAME").item(0).getTextContent():"");
					ps.setString(7,element.getElementsByTagName("OWNER_PHONE_NUMBER").item(0).getTextContent()!=null?element.getElementsByTagName("OWNER_PHONE_NUMBER").item(0).getTextContent():"");
					ps.setString(8,(CRNumber!=null && CRNumber.startsWith("CR"))?element.getElementsByTagName("OWNER_PARTY_NUMBER").item(0).getTextContent():null);
					ps.setString(9,element.getElementsByTagName("CONSTRUCTION_PERMIT_NUMBER").item(0).getTextContent()!=null?element.getElementsByTagName("CONSTRUCTION_PERMIT_NUMBER").item(0).getTextContent():"");
					ps.setString(10,(workCompletionDateIssued!=null && workCompletionDateIssued.equals("N"))?element.getElementsByTagName("RECEIPT_DATE").item(0).getTextContent():null);
					ps.setString(11,"");
					ps.setString(12,element.getElementsByTagName("AREA_NAME").item(0).getTextContent()!=null?element.getElementsByTagName("AREA_NAME").item(0).getTextContent():"");
					ps.setString(13,element.getElementsByTagName("WAY_NAME").item(0).getTextContent()!=null?element.getElementsByTagName("WAY_NAME").item(0).getTextContent():"");
					ps.setString(14,element.getElementsByTagName("WAY_NAME").item(0).getTextContent()!=null?element.getElementsByTagName("WAY_NAME").item(0).getTextContent():"");
					ps.setString(15,"");
					ps.setString(16,element.getElementsByTagName("BLOCK_NAME").item(0).getTextContent()!=null?element.getElementsByTagName("BLOCK_NAME").item(0).getTextContent():"");
					ps.setString(17,element.getElementsByTagName("PLOT_NUMBER").item(0).getTextContent()!=null?element.getElementsByTagName("PLOT_NUMBER").item(0).getTextContent():"");
					ps.setString(18,element.getElementsByTagName("TYPE_OF_USE").item(0).getTextContent()!=null?element.getElementsByTagName("TYPE_OF_USE").item(0).getTextContent():"");
					ps.setString(19,element.getElementsByTagName("TYPE_OF_USE").item(0).getTextContent()!=null?element.getElementsByTagName("TYPE_OF_USE").item(0).getTextContent():"");
					ps.setString(20,"");
					ps.setString(21,"");
					ps.setString(22,"");
					ps.setString(23,"");
					ps.setString(24,(CRNumber!=null && CRNumber.startsWith("CR"))?"Commercial":"Residential");
					ps.setString(25,"");
					ps.setString(26,element.getElementsByTagName("BUILDING_PERMIT_NUMBER").item(0).getTextContent()!=null?element.getElementsByTagName("BUILDING_PERMIT_NUMBER").item(0).getTextContent():"");
					ps.setString(27,element.getElementsByTagName("BUILDING_NUMBER").item(0).getTextContent()!=null?element.getElementsByTagName("BUILDING_NUMBER").item(0).getTextContent():"");
					ps.setString(28,"Muscat");
					ps.setString(29,(CRNumber!=null && CRNumber.startsWith("CR"))?"COM":"RES");
					ps.setString(30, "CELL");
					ps.setString(31, "TRUE");
					ps.setString(32, "TRUE");
					ps.setString(33, "Muscat");
					ps.setString(34, "MAIN");
					ps.setString(35, "p");
					ps.setString(36, "P-W-SML");
					ps.setString(37,element.getElementsByTagName("AREA_NAME").item(1).getTextContent()!=null?element.getElementsByTagName("AREA_NAME").item(1).getTextContent():"");
					ps.setString(38, "NO");
					ps.setString(39,(workCompletionDateIssued!=null && workCompletionDateIssued.equals("Y"))?element.getElementsByTagName("RECEIPT_DATE").item(0).getTextContent():null);
					ps.setString(40, "N");
					ps.setString(41, element.getElementsByTagName("RECEIPT_NUMBER").item(0).getTextContent()!=null?element.getElementsByTagName("RECEIPT_NUMBER").item(0).getTextContent():null);
					ps.setString(42, (workCompletionDateIssued!=null && workCompletionDateIssued.equals("Y") && serviceRequestType.equals("MP: CP: Complete"))?"Work Compleation Certificate":"Construction Permit");
					ps.setString(43, (workCompletionDateIssued!=null && workCompletionDateIssued.equals("Y"))?element.getElementsByTagName("CONSTRUCTION_PERMIT_NUMBER").item(0).getTextContent():null);
					ps.setString(44, ownerCode);
					
					logger.info("Before Execute Update");
					recordUpdated = ps.executeUpdate();
					System.out.println("Records inserted into requests table");
					logger.info("Execute Update Executed {} ",recordUpdated);
					logger.info("Query is {}",query);		
					ps.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			System.out.println("record inserted ");
			conHelper.releaseConnection(con);
		}
		

		return "Record inserted successfully";
	}
	
	
	public String updateMedcDataIntoOracleDB(Document docSecondTarget, String substringOfOwnerId, String serviceRequestNumber, String iDCardNo) {
		ConnectionHelper conHelper=new ConnectionHelper();
		Connection con=null;
		int recordsUpdated=0;
		
		String query=ISQLConstant.UPDATE_MEDC_REQUESTS_TABLE;
		try {
			   con=conHelper.getMMIntegrationOracleConnection();
			   try {
				      logger.info("updating requests table");
				      
				      PreparedStatement ps=con.prepareStatement(query);
				      ps.setString(1, docSecondTarget.getElementsByTagName("EMail").item(0).getTextContent());
				      ps.setString(2, docSecondTarget.getElementsByTagName("OwnerName").item(0).getTextContent());
				      ps.setString(3, docSecondTarget.getElementsByTagName("Wilayat").item(0).getTextContent());
				      ps.setString(4, docSecondTarget.getElementsByTagName("N1").item(0).getTextContent());
				      ps.setString(5, docSecondTarget.getElementsByTagName("N2").item(0).getTextContent());
				      ps.setString(6, docSecondTarget.getElementsByTagName("E1").item(0).getTextContent());
				      ps.setString(7, docSecondTarget.getElementsByTagName("E2").item(0).getTextContent());
				      ps.setString(8, docSecondTarget.getElementsByTagName("MOBILE_NO").item(0).getTextContent());
				      ps.setString(9, iDCardNo);
				      ps.setString(10, substringOfOwnerId);
				      ps.setString(11, serviceRequestNumber);
				   
				     recordsUpdated=ps.executeUpdate();
				     logger.info("Execute Update Executed {} ",recordsUpdated);
					 logger.info("Query is {}",query);		
						ps.close();
				      
			   }catch (Exception e) {
					e.printStackTrace();
				}
			   
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		finally {
			System.out.println("record inserted ");
			conHelper.releaseConnection(con);
		}
		
		
		return "Record  of MEDC Deatils updated successfully";
		
	}

	
	
	
	
	public String updateMMDataIntoOracleDB(Document docThirdTarget, Element element1, String certifiedbuildingarea, String serviceRequestNumber, String substringOfOwnerId, String allowedPercentageArea) {
		ConnectionHelper conHelper=new ConnectionHelper();
		Connection con=null;
		int recordsUpdated=0;
		
		String query=ISQLConstant.UPDATE_MM_DATA_REQUESTS_TABLE;
		try {
			   con=conHelper.getMMIntegrationOracleConnection();
			   try {
				      logger.info("updating requests table");
				      
				      PreparedStatement ps=con.prepareStatement(query);
				      ps.setString(1, docThirdTarget.getElementsByTagName("KROOKIE").item(0).getTextContent());
				      ps.setString(2, docThirdTarget.getElementsByTagName("PLOT_SIZE").item(0).getTextContent());
				      ps.setString(3, certifiedbuildingarea);
				      ps.setString(4,allowedPercentageArea);
				      ps.setString(5, substringOfOwnerId);
				      ps.setString(6, serviceRequestNumber);
				     
				     recordsUpdated=ps.executeUpdate();
				     logger.info("Execute Update Executed {} ",recordsUpdated);
					 logger.info("Query is {}",query);		
						ps.close();
				      
			   }catch (Exception e) {
					e.printStackTrace();
				}
			   
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("record inserted ");
			conHelper.releaseConnection(con);
		}
		
		return "Record updated successfully";
		
	}
	
	
	
	public String updateMOSDDataIntoOracleDB(Document docFourthTarget, String substringOfOwnerId, String serviceRequestNumber) {
		ConnectionHelper conHelper=new ConnectionHelper();
		Connection con=null;
		int recordsUpdated=0;
		
		String query=ISQLConstant.UPDATE_MOSD_DATA_INTO_ORACLE_DB;
		String paymentStartdate=docFourthTarget.getElementsByTagName("PAYMENT_START_DATE").item(0).getTextContent();
		
		try {
			   con=conHelper.getMMIntegrationOracleConnection();
			   
			   try {
				      logger.info("updating requests table");
				      
				      PreparedStatement ps=con.prepareStatement(query);
				      ps.setString(1,docFourthTarget.getElementsByTagName("FAMILY_COUNT").item(0).getTextContent());
				      ps.setString(2,paymentStartdate!=null && paymentStartdate.equals("-")?null:paymentStartdate);
				      ps.setString(3,docFourthTarget.getElementsByTagName("FILE_RECID").item(0).getTextContent());
				      ps.setString(4, substringOfOwnerId);
				      ps.setString(5, serviceRequestNumber);
				     
				     
				     recordsUpdated=ps.executeUpdate();
				     logger.info("Execute Update Executed {} ",recordsUpdated);
					 logger.info("Query is {}",query);		
						ps.close();
				      
			   }catch (Exception e) {
					e.printStackTrace();
				}
			   
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("record inserted ");
			conHelper.releaseConnection(con);
		}
		
		return "Record updated successfully";
		
	}
	
	
	
	
	public String updateGISDetailsDataIntoOracleDB(String accountID, String dMAName, String withinDMA, String substringOfOwnerId, String serviceRequestNumber) {
		ConnectionHelper conHelper=new ConnectionHelper();
		Connection con=null;
		int recordsUpdated=0;
		
		String query=ISQLConstant.UPDATE_GISDETAILS_INTO_ORACLE_DB;
		try {
			   con=conHelper.getMMIntegrationOracleConnection();
			   try {
				      logger.info("updating requests table");
				      PreparedStatement ps=con.prepareStatement(query);
				      ps.setString(1, dMAName);
				      ps.setString(2, accountID);
				      ps.setString(3, withinDMA);
				      ps.setString(4, substringOfOwnerId);
				      ps.setString(5, serviceRequestNumber);
				     
				     
				     recordsUpdated=ps.executeUpdate();
				     logger.info("Execute Update Executed {} ",recordsUpdated);
					 logger.info("Query is {}",query);		
						ps.close();
				      
			   }catch (Exception e) {
					e.printStackTrace();
				}
			   
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("record inserted ");
			conHelper.releaseConnection(con);
		}
		
		return "Record updated successfully";
		
	}
	
	
	
	public String updateMedcDataAfterExecutionOfSchedularIntoOracleDB(Document docMedcTarget, String buildingPermitNo) {
		ConnectionHelper conHelper=new ConnectionHelper();
		Connection con=null;
		int recordsUpdated=0;
		
		String query=ISQLConstant.UPDATE_MEDC_REQUESTS_TABLE_AFTER_EXECUTION_OF_SCHEDULAR;
		try {
			   con=conHelper.getMMIntegrationOracleConnection();
			   try {
				      logger.info("updating requests table");
				      
				      PreparedStatement ps=con.prepareStatement(query);
				      ps.setString(1, docMedcTarget.getElementsByTagName("OwnerName").item(0).getTextContent());
				      ps.setString(2, docMedcTarget.getElementsByTagName("N1").item(0).getTextContent());
				      ps.setString(3, docMedcTarget.getElementsByTagName("N2").item(0).getTextContent());
				      ps.setString(4, docMedcTarget.getElementsByTagName("E1").item(0).getTextContent());
				      ps.setString(5, docMedcTarget.getElementsByTagName("E2").item(0).getTextContent());
				      ps.setString(6, docMedcTarget.getElementsByTagName("IDCardNo").item(0).getTextContent());
				      ps.setString(7, buildingPermitNo);
				   
				     recordsUpdated=ps.executeUpdate();
				     logger.info("Execute Update Executed {} ",recordsUpdated);
					 logger.info("Query is {}",query);		
						ps.close();
				      
			   }catch (Exception e) {
					e.printStackTrace();
				}
			   
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		finally {
			System.out.println("record inserted ");
			conHelper.releaseConnection(con);
		}
		
		
		return "Record  of MEDC Deatils updated successfully";
		
	}

	public List fetchDataFromRequestsWhereOwnerEnNameAndCoordinatesIsNull() {
		ConnectionHelper conHelper=new ConnectionHelper();
		Connection con=null;
		int recordsFetched=0;
		ResultSet rs=null;
		List<String> listOfBuildingPermitNo=new ArrayList<String>();
		
		String query="select * from REQUESTS where OWNER_EN_NAME IS NULL OR (COORDINATE_N1 IS NULL AND COORDINATE_N2 IS NULL AND COORDINATE_E1 IS NULL AND COORDINATE_E2 IS NULL)";
		try {
			con=conHelper.getMMIntegrationOracleConnection();
			try {
				  PreparedStatement ps=con.prepareStatement(query);
				  rs=ps.executeQuery();
				  while(rs.next()) {
					  
					  String buildingPermitNo=rs.getString("BUILDING_PERMIT_NUMBER");
					  listOfBuildingPermitNo.add(buildingPermitNo);
				  }
				
				
			}catch (Exception e) {
				logger.error(e.getLocalizedMessage());
			}
			
		}catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}finally {
			System.out.println("record fetched ");
			conHelper.releaseConnection(con);
		}
		
		
		return listOfBuildingPermitNo;
		
	}
	
	public Map fetchDataFromRequestWhereMMSERVTYPEIsWorkCompletionCertificate() {
		ConnectionHelper conHelper=new ConnectionHelper();
		Connection con=null;
		ResultSet resultset=null;
		Map<String,String> map=new HashMap<String,String>();
		
		String query="select * from REQUESTS where MM_SERV_TYPE='"+IConstants.MM_SERV_TYPE+"'";
		try {
			con=conHelper.getMMIntegrationOracleConnection();
			try {
				  PreparedStatement ps=con.prepareStatement(query);
				  resultset=ps.executeQuery();
                  while(resultset.next()) {
					  
                	  String requestId=resultset.getString("REQUEST_ID");
					  String parent=resultset.getString("PARENT");
					  map.put(requestId, parent);
					 
				  }
				
				
			}catch (Exception e) {
				logger.error(e.getLocalizedMessage());
			}
			
		}catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}finally {
			System.out.println("record fetched ");
			conHelper.releaseConnection(con);
		}
		
		
		return map;
	}
	
	public String insertMMNewDataIntoRequestsAfterExecutionOfSchedular(Document docForMMNew, String certifiedbuildingareaForMMNew, String allowedPercentageAreaAfterExecution, String requestId) {

		ConnectionHelper conHelper = new ConnectionHelper();
		Connection con = null;
		String query = ISQLConstant.INSERT_MMDATA_INTO_REQUESTS_TABLE_AFTER_EXECUTION_OF_SCHEDULAR;
		int recordInserted = 0;
		try {
			   con=conHelper.getMMIntegrationOracleConnection();
			   try {
				   PreparedStatement ps = con.prepareStatement(query);
				   ps.setString(1, requestId);
				   ps.setString(2, docForMMNew.getElementsByTagName("KROOKIE").item(0).getTextContent());
				   ps.setString(3, docForMMNew.getElementsByTagName("PLOT_SIZE").item(0).getTextContent());
				   ps.setString(4, certifiedbuildingareaForMMNew);
				   ps.setString(5, allowedPercentageAreaAfterExecution);
				   ps.setString(6, "Construction Permit");
				   
				   logger.info("Before Execute Update");
					recordInserted = ps.executeUpdate();
					System.out.println("Records inserted into requests table");
					logger.info("Execute Update Executed {} ",recordInserted);
					logger.info("Query is {}",query);		
					ps.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			System.out.println("record inserted ");
			conHelper.releaseConnection(con);
		}
		

		return "Record inserted successfully";
	}
	
				   
				   
			

}
