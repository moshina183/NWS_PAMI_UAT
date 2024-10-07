package com.vinsys.diam.services;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.vinsys.diam.esb.IConstants;
import com.vinsys.diam.model.TXNAuditLogs;

@Service
public class ESPHandlerV2 {
	
	private static final Logger logger = LoggerFactory.getLogger(ESPHandler.class);
	
	
	@Value("${getbilldata-target-url}")
	private String getBillData;
	
	@Value("${getbilldetails-target-url}")
	private String getBillDetails;
	
	@Value("${getbillsummary-target-url}")
	private String getBillSummary;
	
	@Value("${getbillpdf-target-url}")
	private String getBillPDF;
	
	
	@Value("${getgroupbillinfo-target-url}")
	private String getGroupBillInfo;
	
	@Value("${getgroupbilldetails-target-url}")
	private String getGroupBillDetail;
	
	
	@Value("${getgbill64-target-url}")
	private String getBill64;
	
	@Value("${inquirypayment-target-url}")
	private String inquiryPaymentUrl;
	
	
	protected RestTemplate getRestTemplate() {
		if (logger.isInfoEnabled())
			logger.info("Entered::AbstractEmailDelivery::getRestTemplate()");
		RestTemplate restTemplate = new RestTemplate(
				new BufferingClientHttpRequestFactory(getClientHttpRequestFactory()));
		return restTemplate;
	}

	private SimpleClientHttpRequestFactory getClientHttpRequestFactory() {
		SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		return clientHttpRequestFactory;
	}

	public HttpHeaders createHeaders() {
		return new HttpHeaders();

	}
	
	
/**
 * @author dipak
 * @param billId
 * @param ccbAccount
 * @param request
 * @return
 */
	public ResponseEntity<String> getBilldataServiceHandler(String billId, String ccbAccount,HttpServletRequest request) 
	{
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		HttpEntity<String> getBillDataEntity = new HttpEntity<String>(header);
		
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("Before RestTemplateCall");
		String targetURI = "";
		if (!billId.isEmpty()) {
			targetURI = targetURI +IConstants.QM+"BillId="+ billId;
		}

		if (!ccbAccount.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"ccbAccount=" + ccbAccount;
		}
		
		String targetUrl=getBillData+targetURI;
		try {
			getBillDataEntity = restTemplate
					.getForEntity(targetUrl, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setResponseMsg(getBillDataEntity.getBody());
			auditLogs.setTargetResponseMsg(getBillDataEntity.getBody());
		} catch (HttpStatusCodeException e) {
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
			logger.error("Error while calling getbilldata URL {}", e.getMessage());
			e.printStackTrace();
		}

		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		String body=auditLogs.getResponseMsg();
		 
		String esbRespMessage = String.format(IConstants.ESB_SUCCESS_RESPONSE_TEMPLATE_JSON,(body!=null && !body.trim().isEmpty())?body:"{}");
			
		
			return new ResponseEntity<String>(esbRespMessage, HttpStatus.OK);
	}

	
	
	/**
	 * @author dipak
	 * @param ccbAccount
	 * @param Months
	 * @param request
	 * @return
	 */
	
	public ResponseEntity<String> getBillDetailServiceHandler(String ccbAccount, String Months,HttpServletRequest request) 
	{
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		HttpEntity<String> getBillDetailsEntity = new HttpEntity<String>(header);
		
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("Before RestTemplateCall");
		String targetURI = "";
		if (!ccbAccount.isEmpty()) {
			targetURI = targetURI +IConstants.QM+"ccbAccount="+ ccbAccount;
		}

		if (!Months.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"Months=" + Months;
		}
		
		String targetUrl=getBillDetails+targetURI;
		try {
			getBillDetailsEntity = restTemplate
					.getForEntity(targetUrl, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setResponseMsg(getBillDetailsEntity.getBody());
			auditLogs.setTargetResponseMsg(getBillDetailsEntity.getBody());
		} catch (HttpStatusCodeException e) {
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
			logger.error("Error while calling getBillDetail URL {}", e.getMessage());
			e.printStackTrace();
		}

		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		String body=auditLogs.getResponseMsg();
		 
		String esbRespMessage = String.format(IConstants.ESB_SUCCESS_RESPONSE_TEMPLATE_JSON,(body!=null && !body.trim().isEmpty())?body:"{}");
			
		
			return new ResponseEntity<String>(esbRespMessage, HttpStatus.OK);	
			
	}
	
	
	
	
	/**
	 * @author dipak
	 * @param ccbAccount
	 * @param Months
	 * @param request
	 * @return
	 */
	public ResponseEntity<String> getBillDetailsSummaryHandler(String ccbAccount, String Months,HttpServletRequest request) 
	{
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		HttpEntity<String> getBillDetailsEntity = new HttpEntity<String>(header);
		
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("Before RestTemplateCall");
		String targetURI = "";
		if (!ccbAccount.isEmpty()) {
			targetURI = targetURI +IConstants.QM+"ccbAccount="+ ccbAccount;
		}

		if (!Months.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"Months=" + Months;
		}
		
		String targetUrl=getBillSummary+targetURI;
		try {
			getBillDetailsEntity = restTemplate
					.getForEntity(targetUrl, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setResponseMsg(getBillDetailsEntity.getBody());
			auditLogs.setTargetResponseMsg(getBillDetailsEntity.getBody());
		} catch (HttpStatusCodeException e) {
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
			logger.error("Error while calling getBillDetail URL {}", e.getMessage());
			e.printStackTrace();
		}

		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		
		String body=auditLogs.getResponseMsg();
		 
		String esbRespMessage = String.format(IConstants.ESB_SUCCESS_RESPONSE_TEMPLATE_JSON,(body!=null && !body.trim().isEmpty())?body:"{}");
			
		
			return new ResponseEntity<String>(esbRespMessage, HttpStatus.OK);
	}
	
	/**
	 * @author dipak
	 * @param billID
	 * @param ccbAccount
	 * @param request
	 * @return
	 */
	

	public ResponseEntity<String> getBillPDFServiceHandler(String billID, String ccbAccount,HttpServletRequest request) 
	{
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		HttpEntity<String> getBillDataEntity = new HttpEntity<String>(header);
		
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("Before RestTemplateCall");
			
		
		String targetURI = "";
		if (!billID.isEmpty()) {
			targetURI = targetURI +IConstants.QM+"billID="+ billID;
		}

		if (!ccbAccount.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"ccbAccount=" + ccbAccount;
		}
		
		String targetUrl=getBillPDF+targetURI;
		try {
			getBillDataEntity = restTemplate
					.getForEntity(targetUrl, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setResponseMsg(getBillDataEntity.getBody());
			auditLogs.setTargetResponseMsg(getBillDataEntity.getBody());
		} catch (HttpStatusCodeException e) {
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
			logger.error("Error while calling getbilldata URL {}", e.getMessage());
			e.printStackTrace();
		}

		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		
		
		String body=auditLogs.getResponseMsg();
		 
		String esbRespMessage = String.format(IConstants.ESB_SUCCESS_RESPONSE_TEMPLATE_JSON,(body!=null && !body.trim().isEmpty())?body:"{}");
			
		
			return new ResponseEntity<String>(esbRespMessage, HttpStatus.OK);
	}

	public ResponseEntity<String> getGroupBillsInfoServiceHandler(String year, String month, String groupId,
			HttpServletRequest request) {
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		HttpEntity<String> getBillDataEntity = new HttpEntity<String>(header);
		
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("Before RestTemplateCall");
		
		String targetURI = "";
		if (!year.isEmpty()) {
			targetURI = targetURI +IConstants.QM+"year="+ year;
		}

		if (!month.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"month=" + month;
		}
		if (!month.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"GroupId=" + groupId;
		}
		
		String targetUrl=getGroupBillInfo+targetURI;
		try {
			getBillDataEntity = restTemplate
					.getForEntity(targetUrl, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setResponseMsg(getBillDataEntity.getBody());
			auditLogs.setTargetResponseMsg(getBillDataEntity.getBody());
		} catch (HttpStatusCodeException e) {
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
			logger.error("Error while calling getbilldata URL {}", e.getMessage());
			e.printStackTrace();
		}

		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		
		 System.out.println("response message +++++++"+auditLogs.getResponseMsg());
		  String body=auditLogs.getResponseMsg();
		 
	String esbRespMessage = String.format(IConstants.ESB_SUCCESS_RESPONSE_TEMPLATE_JSON,(body!=null && !body.trim().isEmpty())?body:"{}");
		
	
		return new ResponseEntity<String>(esbRespMessage, HttpStatus.OK);
		
	
	}

	public ResponseEntity<String> getGroupBillDetailsServiceHandler(String year, String month, String groupId,
			HttpServletRequest request) {
		
		
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		HttpEntity<String> getBillDataEntity = new HttpEntity<String>(header);
		
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("Before RestTemplateCall");
		String targetURI = "";
		if (!year.isEmpty()) {
			targetURI = targetURI +IConstants.QM+"year="+ year;
		}

		if (!month.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"month=" + month;
		}
		if (!month.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"GroupId=" + groupId;
		}
		
		String targetUrl=getGroupBillDetail+targetURI;
		try {
			getBillDataEntity = restTemplate
					.getForEntity(targetUrl, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setResponseMsg(getBillDataEntity.getBody());
			auditLogs.setTargetResponseMsg(getBillDataEntity.getBody());
		} catch (HttpStatusCodeException e) {
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
			logger.error("Error while calling getbilldata URL {}", e.getMessage());
			e.printStackTrace();
		}

		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		
		
		 System.out.println("response message +++++++"+auditLogs.getResponseMsg());
		  String body=auditLogs.getResponseMsg();
		 
	String esbRespMessage = String.format(IConstants.ESB_SUCCESS_RESPONSE_TEMPLATE_JSON,(body!=null && !body.trim().isEmpty())?body:"{}");
		
		
		
		
		return new ResponseEntity<String>(esbRespMessage, HttpStatus.OK);
		
		
	}

	public ResponseEntity<String> getBill64ServiceHandler(String billId, String ccbAccount,
			HttpServletRequest request) {
		
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		HttpEntity<String> getBillDataEntity = new HttpEntity<String>(header);
		
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("Before RestTemplateCall");
		String targetURI = "";
		if (!billId.isEmpty()) {
			targetURI = targetURI +IConstants.QM+"BID="+ billId;
		}

		if (!ccbAccount.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"ccbAccount=" + ccbAccount;
		}
		
		String targetUrl=getBill64+targetURI;
		try {
			getBillDataEntity = restTemplate
					.getForEntity(targetUrl, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setResponseMsg(getBillDataEntity.getBody());
			auditLogs.setTargetResponseMsg(getBillDataEntity.getBody());
		} catch (HttpStatusCodeException e) {
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
			logger.error("Error while calling getbilldata URL {}", e.getMessage());
			e.printStackTrace();
		}

		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		String body=auditLogs.getResponseMsg();
		 
		String esbRespMessage = String.format(IConstants.ESB_SUCCESS_RESPONSE_TEMPLATE_JSON,(body!=null && !body.trim().isEmpty())?body:"{}");
			
		
			return new ResponseEntity<String>(esbRespMessage, HttpStatus.OK);

	}

	public ResponseEntity<String> inquiryPaymentServiceHandler(String trackID, String gateWay,
			HttpServletRequest request) {
		
		System.out.println("ESPHandler.inquiryPaymentServiceHandler()");
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		HttpEntity<String> getBillDataEntity = new HttpEntity<String>(header);
		
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		System.out.println("Before RestTemplateCall");
			
		
		String targetURI = "";
		if (!trackID.isEmpty()) {
			targetURI = targetURI +IConstants.QM+"TrackID="+ trackID;
		}

		if (!gateWay.isEmpty()) {
			targetURI = targetURI +IConstants.AND+"Gateway=" + gateWay;
		}
		
		String targetUrl=inquiryPaymentUrl+targetURI;
		try {
			getBillDataEntity = restTemplate
					.getForEntity(targetUrl, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setResponseMsg(getBillDataEntity.getBody());
			auditLogs.setTargetResponseMsg(getBillDataEntity.getBody());
		} catch (HttpStatusCodeException e) {
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
			logger.error("Error while calling getbilldata URL {}", e.getMessage());
			e.printStackTrace();
		}

		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		String body=auditLogs.getResponseMsg();
		 
		String esbRespMessage = String.format(IConstants.ESB_SUCCESS_RESPONSE_TEMPLATE_JSON,(body!=null && !body.trim().isEmpty())?body:"{}");
			
		
			return new ResponseEntity<String>(esbRespMessage, HttpStatus.OK);
		
	}
	

}
