
package com.vinsys.diam.controllers;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.gson.Gson;
import com.vinsys.diam.esb.IConstants;
import com.vinsys.diam.esb.MeterReading;
import com.vinsys.diam.filters.GetDetails;
import com.vinsys.diam.model.Alert;
import com.vinsys.diam.model.Envelope;
import com.vinsys.diam.model.TXNAuditLogs;
import com.vinsys.diam.model.TXNAuditSubRequestsLogs;
import com.vinsys.diam.processors.AuditLogProcessor;
import com.vinsys.diam.processors.ExceptionProcessor;
import com.vinsys.diam.processors.InsertionHelper;
import com.vinsys.diam.services.ESPHandler;
import com.vinsys.diam.services.ESPHandlerV2;
import com.vinsys.diam.services.GetRecordCSHandler;
import com.vinsys.diam.services.GetRecordCSHandlerV2;
import com.vinsys.diam.services.GetRecordGISCSHandler;
import com.vinsys.diam.services.GetRecordGISCSHandlerv2;

@RestController
@RequestMapping("api/v2")

public class PAWESBServicesV2

{
	private static final Logger logger = LoggerFactory.getLogger(PAWESBServices.class);

	@Value("${getmeterreading-target-url}")
	private String getmeterreadingTargetUurl;

	@Value("${getrecordcs-target-url}")
	private String getRecordCS;

	@Value("${gis-getrecord-target-url}")
	private String getRecordGIS;

	@Value("${auth-username}")
	private String authUsername;

	@Value("${auth-password}")
	private String authPassword;

	@Autowired
	private GetRecordCSHandlerV2 csHandler;

	@Autowired
	private GetRecordGISCSHandlerv2 getRecordHandler;

	@Autowired
	private ESPHandlerV2 espHandler;

	@Value("${addpay.username}")
	private String addPayUsername;

	@Value("${addpay.pwd}")
	private String addPayPassword;

	@Value("${addpay-target-url}")
	private String addPayURL;

	@Value("${addpay-soap-action}")
	private String addPaySoapAction;
	
	@Value("${getcompanydata-target-url}")
	private String getCompanyDataURL;
	
	@Value("${getcompanydata-soap-action}")
	private String getCompanyDataSoapAction;
	
	@Value("${getcompanydata.username}")
	private String getcompanyusername;
	
	@Value("${getcompanydata.password}")
	private String getcompanypassword;
	
	@Value("${inquiryaccount.username}")
	private String inquiryAccountUsername;

	@Value("${inquiryaccount.password}")
	private String inquiryAccountPassword;
	
	@Value("${getplotdetailsbycrookie.username}")
	private String getPlotDetailsByCrookieUsername;

	@Value("${getplotdetailsbycrookie.password}")
	private String getPlotDetailsByCrookiePassword;
	
	@Value("${getsocialbenefitdata.username}")
	private String getSocialBenefitDataUsername;

	@Value("${getsocialbenefitdata.password}")
	private String getSocialBenefitDataPassword;
	
	@Value("${inquiryaccount-target-url}")
	private String inquiryAccountURL;
	
	@Value("${inquiryaccount-soap-action}")
	private String inquiryAccountSoapAction;
	
	@Value("${getplotdetailsbycrookie-target-url}")
	private String getPlotDetailsByCrookieURL;
	
	@Value("${getplotdetailsbycrookie-soap-action}")
	private String getPlotDetailsByCrookieSoapAction;
	
	@Value("${getsocialbenefitdata-target-url}")
	private String getSocialBenefitDataURL;
	
	@Value("${getsocialbenefitdata-soap-action}")
	private String getSocialBenefitDataSoapAction;
	
	@Value("${getpersoninformation-target-url}")
	private String getPersonInformationURL;
	
	@Value("${getpersoninformation-soap-action}")
	private String GetPersonInformationSoapAction;
	


	

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

	@PostMapping(value = "/getmeterreading", produces = "application/json")
	public ResponseEntity<String> getMeterReading(HttpServletRequest request, HttpServletResponse response,
			@RequestBody(required = false) MeterReading mrPayload, @RequestHeader Map<String, String> headers)
			throws Exception {
		HttpHeaders header = createHeaders();
		RestTemplate restTemplate = getRestTemplate();
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);

		String auth = authUsername + ":" + authPassword;
		String basicAuthValue = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes("utf-8"));
		header.add("Authorization", basicAuthValue);
		header.add("Content-Type", "application/json");

		auditLogs.setRequestMsg(new Gson().toJson(mrPayload));
		auditLogs.setTargetRequesMsg(new Gson().toJson(mrPayload));
		HttpEntity<MeterReading> mrReadingEntity = new HttpEntity<MeterReading>(mrPayload, header);
		auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(getmeterreadingTargetUurl, HttpMethod.POST,
					mrReadingEntity, String.class);
			auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setTargetResponseMsg(responseEntity.getBody());
			auditLogs.setResponseMsg(responseEntity.getBody());
			auditLogs.setTargetResponseCode(responseEntity.getStatusCode().name());
		} catch (HttpStatusCodeException e) {

			e.printStackTrace();
			auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
			System.out.println("In exception  " + e.getResponseBodyAsString());
			auditLogs.setResponseMsg(e.getResponseBodyAsString());
		}

		System.out.println("Audit in service ::" + request.getAttribute(IConstants.CURRENT_AUDIT));
		request.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
		String body = auditLogs.getResponseMsg();
		String esbRespMessage = String.format(IConstants.ESB_SUCCESS_RESPONSE_TEMPLATE_JSON,
				(body != null && !body.trim().isEmpty()) ? body : "{}");
		return new ResponseEntity<String>(esbRespMessage, HttpStatus.OK);

	}

	@GetMapping(value = "/GetGISMap/{accountId}/{lat}/{long}", produces = "application/json")
	public ResponseEntity<String> getRecordsCS(
			@PathVariable(name = "accountId", required = false, value = "") Optional<String> accountId,
			@PathVariable(name = "lat", required = false, value = "") Optional<String> latitude,
			@PathVariable(name = "long", required = false, value = "") Optional<String> longitude,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println(accountId);
		System.out.println(latitude);
		System.out.println(longitude);
		System.out.println("PAWESBServicesV2.getRecordsCS()");
		return csHandler.handleGetRecordCS(accountId, latitude, longitude, request, response);
	}

	@GetMapping(value = "/GetGISMap/{accountId}", produces = "application/json")
	public ResponseEntity<String> getRecordsCSOnlyAccountID(
			@PathVariable(name = "accountId", required = false, value = "") Optional<String> accountId,
			@PathVariable(name = "lat", required = false, value = "") Optional<String> latitude,
			@PathVariable(name = "long", required = false, value = "") Optional<String> longitude,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println(accountId);
		System.out.println(latitude);
		System.out.println(longitude);
		System.out.println("PAWESBServicesV2.getRecordsCSOnlyAccountID()");
		return csHandler.handleGetRecordCS(accountId, latitude, longitude, request, response);
	}

	@GetMapping(value = "/GetGISMap/{accountId}/{lat}", produces = "application/json")
	public ResponseEntity<String> getRecordsCSOnlyAccountIDAndLat(
			@PathVariable(name = "accountId", required = false, value = "") Optional<String> accountId,
			@PathVariable(name = "lat", required = false, value = "") Optional<String> latitude,
			@PathVariable(name = "long", required = false, value = "") Optional<String> longitude,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println(accountId);
		System.out.println(latitude);
		System.out.println(longitude);
		System.out.println("PAWESBServicesV2.getRecordsCSOnlyAccountIDAndLat()");
		return csHandler.handleGetRecordCS(accountId, latitude, longitude, request, response);
	}

	@GetMapping(value = "/GetGISDetails/{accountId}/{lat}/{long}", produces = "application/json")
	public ResponseEntity<String> getRecordsGISService(
			@PathVariable(name = "accountId", required = false) Optional<String> accountId,
			@PathVariable(name = "lat", required = false) Optional<String> latitude,
			@PathVariable(name = "long", required = false) Optional<String> longitude, HttpServletRequest request,
			HttpServletResponse response, @RequestHeader Map<String, String> headers) throws Exception {

		return getRecordHandler.getRecordGISHandler(accountId, latitude, longitude, request);
	}

	@GetMapping(value = "/GetGISDetails/{accountId}/{lat}", produces = "application/json")
	public ResponseEntity<String> getRecordsGISServiceWithAccontIdAndLat(
			@PathVariable(name = "accountId", required = false) Optional<String> accountId,
			@PathVariable(name = "lat", required = false) Optional<String> latitude,
			@PathVariable(name = "long", required = false) Optional<String> longitude, HttpServletRequest request,
			HttpServletResponse response, @RequestHeader Map<String, String> headers) throws Exception {

		return getRecordHandler.getRecordGISHandler(accountId, latitude, longitude, request);
	}

	@GetMapping(value = "/GetGISDetails/{accountId}", produces = "application/json")
	public ResponseEntity<String> getRecordsGISServiceWithAccontId(
			@PathVariable(name = "accountId", required = false) Optional<String> accountId,
			@PathVariable(name = "lat", required = false) Optional<String> latitude,
			@PathVariable(name = "long", required = false) Optional<String> longitude, HttpServletRequest request,
			HttpServletResponse response, @RequestHeader Map<String, String> headers) throws Exception {

		return getRecordHandler.getRecordGISHandler(accountId, latitude, longitude, request);
	}

	@PostMapping(value = "/sendamralarms", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> receiveAmrAlarmsForPortal(HttpServletRequest request, HttpServletResponse response,
			@RequestBody List<Alert> amrAlarmPayload, @RequestHeader Map<String, String> headers) throws Exception {
		InsertionHelper insertionHelper = new InsertionHelper();
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		auditLogs.setRequestMsg(new Gson().toJson(amrAlarmPayload));
		GetDetails getDetails = new GetDetails();
		String checkIfDuplicate = getDetails.getDuplicateRequestId(auditLogs.getEsbServiceCode());
		System.out.println("checkifduplicate" + checkIfDuplicate);
		if (checkIfDuplicate != null && checkIfDuplicate.equals("Y")) {
			System.out.println("In if condition " + getDetails.checkIfRecordExist(auditLogs.getEsbReqId()));
			if (getDetails.checkIfRecordExist(auditLogs.getEsbReqId())) {
				auditLogs.setResponseMsg(
						"{\r\n" + "\"errorCode\": 9,\r\n" + "\"errorMessage\": \"Duplicate Request ID\"\r\n" + "}");
			} else {
				insertionHelper.insertIntoDb(amrAlarmPayload);
				insertionHelper.insertIntoSqlServerDb(amrAlarmPayload);

				auditLogs.setResponseMsg("{\"status\":\"SUCCESS\",\"Message\":\"Submitted Successfully\"}");
			}

		}
		if (checkIfDuplicate != null && checkIfDuplicate.equals("N")) {
			insertionHelper.insertIntoDb(amrAlarmPayload);
			insertionHelper.insertIntoSqlServerDb(amrAlarmPayload);
			auditLogs.setResponseMsg("{\"status\":\"SUCCESS\",\"Message\":\"Submitted Successfully\"}");
		}

		String body = auditLogs.getResponseMsg();
		String esbRespMessage = String.format(IConstants.ESB_SUCCESS_RESPONSE_TEMPLATE_JSON,
				(body != null && !body.trim().isEmpty()) ? body : "{}");

		return new ResponseEntity<String>(esbRespMessage, HttpStatus.OK);

	}

	/**
	 * @author dipak
	 * @since 02-07-2021
	 * @param billId
	 * @param ccbAccount
	 * @param request
	 * @param response
	 * @param headers
	 * @return
	 * @throws Exception
	 * 
	 */

	@GetMapping(value = "/getBillData", produces = "application/json")
	public ResponseEntity<String> getBillDataService(@RequestParam String BillID, @RequestParam String ccbAccount,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {
		return espHandler.getBilldataServiceHandler(BillID, ccbAccount, request);
	}

	/**
	 * @author dipak
	 * @since 02-07-2021
	 * @param ccbAccount
	 * @param Months
	 * @param request
	 * @param response
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/GetBillsDetails", produces = "application/json")
	public ResponseEntity<String> getBillDetails(@RequestParam String ccbAccount, @RequestParam String Months,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {
		return espHandler.getBillDetailServiceHandler(ccbAccount, Months, request);
	}

	/**
	 * @author dipak
	 * @param ccbAccount
	 * @param Months
	 * @param request
	 * @param response
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/GetBillsSummary", produces = "application/json")
	public ResponseEntity<String> getBillSummary(@RequestParam String ccbAccount, @RequestParam String Months,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {

		return espHandler.getBillDetailsSummaryHandler(ccbAccount, Months, request);
	}

	/**
	 * @author dipak
	 * @param billID
	 * @param ccbAccount
	 * @param request
	 * @param response
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/GetBillPdf", produces = "application/json")
	public ResponseEntity<String> getBillPdf(@RequestParam String billID, @RequestParam String ccbAccount,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {

		String queryString = request.getQueryString();
		return espHandler.getBillPDFServiceHandler(billID, ccbAccount, request);
	}

	/**
	 * @author dipak
	 * @param year
	 * @param month
	 * @param GroupId
	 * @param request
	 * @param response
	 * @param headers
	 * @return
	 * @throws Exception
	 */

	@GetMapping(value = "/GetGroupBillsInfo", produces = "application/json")
	public ResponseEntity<String> getGroupBillInfo(@RequestParam String year, @RequestParam String month,
			@RequestParam String GroupId, HttpServletRequest request, HttpServletResponse response,
			@RequestHeader Map<String, String> headers) throws Exception {

		return espHandler.getGroupBillsInfoServiceHandler(year, month, GroupId, request);
	}

	/**
	 * @author dipak
	 * @param year
	 * @param month
	 * @param GroupId
	 * @param request
	 * @param response
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/GetGroupBillDetails", produces = "application/json")
	public ResponseEntity<String> getGroupBillDetails(@RequestParam String year, @RequestParam String month,
			@RequestParam String GroupId, HttpServletRequest request, HttpServletResponse response,
			@RequestHeader Map<String, String> headers) throws Exception {

		String queryString = request.getQueryString();
		return espHandler.getGroupBillDetailsServiceHandler(year, month, GroupId, request);
	}

	/**
	 * @author dipak
	 * @param billId
	 * @param ccbAccount
	 * @param request
	 * @param response
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/GetBill64", produces = "application/json")
	public ResponseEntity<String> getBill64(@RequestParam String BID, @RequestParam String ccbAccount,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {
		return espHandler.getBill64ServiceHandler(BID, ccbAccount, request);
	}

	/**
	 * @author dipak
	 * @param TrackID
	 * @param Gateway
	 * @param request
	 * @param response
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/InquiryPayment", produces = "application/json")
	public ResponseEntity<String> inquiryPayment(@RequestParam String TrackID, @RequestParam String Gateway,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {
		String queryString = request.getQueryString();
		return espHandler.inquiryPaymentServiceHandler(TrackID, Gateway, request);
	}

	@RequestMapping(value = "/addpay", method = {
			RequestMethod.POST }, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getAddPay(@RequestPayload Envelope envelope, HttpServletRequest request,
			HttpServletResponse response) {
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		ResponseEntity<String> responseEntity = null;
		try {
			// System.out.println(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
			JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			/*
			 * Envelope env = (Envelope) jaxbUnmarshaller.unmarshal(request.getReader());
			 * env.toString();
			 */
			String xml = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

			HttpHeaders header = createHeaders();
			header.set("SOAPAction", addPaySoapAction);
			header.set("Content-Type", "text/xml;charset=UTF-8");

			String auth = addPayUsername + ":" + addPayPassword;
			String basicAuthValue = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes("utf-8"));
			header.set("Authorization", basicAuthValue);

			RestTemplate restTemplate = getRestTemplate();
			HttpEntity mrReadingEntity = new HttpEntity(xml, header);
			auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
			try {
				responseEntity = restTemplate.exchange(addPayURL, HttpMethod.POST, mrReadingEntity, String.class);

				auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setTargetResponseMsg(responseEntity.getBody());
				String responseMsg = String.format(IConstants.ESB_XML_SUCCESS_RESPONSE_V2, responseEntity.getBody());
				responseMsg = responseMsg.replaceAll("(<\\?xml.*?\\?>)","");
				responseMsg = responseMsg.replaceAll("(?m)^[ \\t]*\\r?\\n", "");
				auditLogs.setResponseMsg(responseMsg);
				auditLogs.setResponseMsg(responseMsg);
				auditLogs.setTargetResponseCode(responseEntity.getStatusCode().name());
			} catch (HttpStatusCodeException e) {
				e.printStackTrace();
				auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
				System.out.println("In exception  " + e.getResponseBodyAsString());
				auditLogs.setResponseMsg(e.getResponseBodyAsString());
			}

		} catch (IOException | JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getCompanyData", method = {
			RequestMethod.POST }, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getCompanyData(@RequestPayload Envelope envelope, HttpServletRequest request,
			HttpServletResponse response) {
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		ResponseEntity<String> responseEntity = null;
		try {
			// System.out.println(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
			JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			/*
			 * Envelope env = (Envelope) jaxbUnmarshaller.unmarshal(request.getReader());
			 * env.toString();
			 */
			String xml = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			auditLogs.setRequestMsg(xml);
			auditLogs.setTargetRequesMsg(xml);
			HttpHeaders header = createHeaders();
			header.set("SOAPAction", getCompanyDataSoapAction);
			header.set("Content-Type", "text/xml;charset=UTF-8");

			String auth = getcompanyusername + ":" + getcompanypassword;
			String basicAuthValue = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes("utf-8"));
			header.set("Authorization", basicAuthValue);

			RestTemplate restTemplate = getRestTemplate();
			HttpEntity mrReadingEntity = new HttpEntity(xml, header);
			auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
			try {
				responseEntity = restTemplate.exchange(getCompanyDataURL, HttpMethod.POST, mrReadingEntity, String.class);

				auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setTargetResponseMsg(responseEntity.getBody());
				String responseMsg = String.format(IConstants.ESB_XML_SUCCESS_RESPONSE_V2, responseEntity.getBody());
				responseMsg = responseMsg.replaceAll("(<\\?xml.*?\\?>)","");
				responseMsg = responseMsg.replaceAll("(?m)^[ \\t]*\\r?\\n", "");
				auditLogs.setResponseMsg(responseMsg);
				auditLogs.setTargetResponseCode(responseEntity.getStatusCode().name());
			} catch (HttpStatusCodeException e) {
				e.printStackTrace();
				auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
				System.out.println("In exception  " + e.getResponseBodyAsString());
				auditLogs.setResponseMsg(String.format(IConstants.ESB_XML_SUCCESS_RESPONSE_V2, e.getResponseBodyAsString()));
			}

		} catch (IOException | JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}

	@RequestMapping(value = "/inquiryaccount", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getInquiryAccount(@RequestPayload Envelope envelope, HttpServletRequest request,
			HttpServletResponse response) {
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		ResponseEntity<String> responseEntity = null;
		try {
			//System.out.println(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
			JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);  
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); 
			/*
			 * Envelope env = (Envelope) jaxbUnmarshaller.unmarshal(request.getReader());
			 * env.toString();
			 */
			String xml = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

			HttpHeaders header = createHeaders();
			header.set("SOAPAction", inquiryAccountSoapAction);
			header.set("Content-Type", "text/xml;charset=UTF-8");

			String auth = inquiryAccountUsername + ":" + inquiryAccountPassword;
			String basicAuthValue = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes("utf-8"));
			header.set("Authorization", basicAuthValue);

			RestTemplate restTemplate = getRestTemplate();
			HttpEntity mrReadingEntity = new HttpEntity(xml, header);
			auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setRequestMsg(xml);
			auditLogs.setTargetRequesMsg(xml);
			try {
				responseEntity = restTemplate.exchange(inquiryAccountURL, HttpMethod.POST,
						mrReadingEntity, String.class);

				auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setTargetResponseMsg(responseEntity.getBody());
				String responseMsg = String.format(IConstants.ESB_XML_SUCCESS_RESPONSE_V2, responseEntity.getBody());
				responseMsg = responseMsg.replaceAll("(<\\?xml.*?\\?>)","");
				responseMsg = responseMsg.replaceAll("(?m)^[ \\t]*\\r?\\n", "");
				auditLogs.setResponseMsg(responseMsg);
				auditLogs.setTargetResponseCode(responseEntity.getStatusCode().name());
			}
			catch (HttpStatusCodeException e) {
				e.printStackTrace();
				auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
				System.out.println("In exception  "+e.getResponseBodyAsString());
				auditLogs.setResponseMsg(e.getResponseBodyAsString());
			}

		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/getplotdetailsbycrookie", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getPlotDetailsByCrookie(@RequestPayload Envelope envelope, HttpServletRequest request,
			HttpServletResponse response) {
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		ResponseEntity<String> responseEntity = null;
		try {
			//System.out.println(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
			JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);  
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); 
			/*
			 * Envelope env = (Envelope) jaxbUnmarshaller.unmarshal(request.getReader());
			 * env.toString();
			 */
			String xml = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

			HttpHeaders header = createHeaders();
			header.set("SOAPAction", getPlotDetailsByCrookieSoapAction);
			header.set("Content-Type", "text/xml;charset=UTF-8");

			String auth = getPlotDetailsByCrookieUsername + ":" + getPlotDetailsByCrookiePassword;
			String basicAuthValue = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes("utf-8"));
			header.set("Authorization", basicAuthValue);

			RestTemplate restTemplate = getRestTemplate();
			HttpEntity mrReadingEntity = new HttpEntity(xml, header);
			auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setRequestMsg(xml);
			auditLogs.setTargetRequesMsg(xml);
			try {
				responseEntity = restTemplate.exchange(getPlotDetailsByCrookieURL, HttpMethod.POST,
						mrReadingEntity, String.class);

				auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setTargetResponseMsg(responseEntity.getBody());
				String responseMsg = String.format(IConstants.ESB_XML_SUCCESS_RESPONSE_V2, responseEntity.getBody());
				responseMsg = responseMsg.replaceAll("(<\\?xml.*?\\?>)","");
				responseMsg = responseMsg.replaceAll("(?m)^[ \\t]*\\r?\\n", "");
				auditLogs.setResponseMsg(responseMsg);
				auditLogs.setTargetResponseCode(responseEntity.getStatusCode().name());
			}
			catch (HttpStatusCodeException e) {
				e.printStackTrace();
				auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
				System.out.println("In exception  "+ e.getResponseBodyAsString());
				auditLogs.setResponseMsg(e.getResponseBodyAsString());
			}

		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getsocialbenefitdata", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getSocialBenefitData(@RequestPayload Envelope envelope, HttpServletRequest request,
			HttpServletResponse response) {
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		ResponseEntity<String> responseEntity = null;
		try {
			//System.out.println(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
			JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);  
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); 
			/*
			 * Envelope env = (Envelope) jaxbUnmarshaller.unmarshal(request.getReader());
			 * env.toString();
			 */
			String xml = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

			HttpHeaders header = createHeaders();
			header.set("SOAPAction", getSocialBenefitDataSoapAction);
			header.set("Content-Type", "text/xml;charset=UTF-8");

			String auth = getSocialBenefitDataUsername + ":" + getSocialBenefitDataPassword;
			String basicAuthValue = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes("utf-8"));
			header.set("Authorization", basicAuthValue);

			RestTemplate restTemplate = getRestTemplate();
			HttpEntity mrReadingEntity = new HttpEntity(xml, header);
			auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setRequestMsg(xml);
			auditLogs.setTargetRequesMsg(xml);
			try {
				responseEntity = restTemplate.exchange(getSocialBenefitDataURL, HttpMethod.POST,
						mrReadingEntity, String.class);

				auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setTargetResponseMsg(responseEntity.getBody());
				String responseMsg = String.format(IConstants.ESB_XML_SUCCESS_RESPONSE_V2, responseEntity.getBody());
				responseMsg = responseMsg.replaceAll("(<\\?xml.*?\\?>)","");
				responseMsg = responseMsg.replaceAll("(?m)^[ \\t]*\\r?\\n", "");
				auditLogs.setResponseMsg(responseMsg);
				auditLogs.setTargetResponseCode(responseEntity.getStatusCode().name());
			}
			catch (HttpStatusCodeException e) {
				e.printStackTrace();
				auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
				System.out.println("In exception  "+ e.getResponseBodyAsString());
				auditLogs.setResponseMsg(e.getResponseBodyAsString());
			}

		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}
	
	/**
	 * @author trupti
	 * @param request
	 * @param response
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	
	
	@PostMapping(value = "/getpersoninformation" , produces = "application/xml")
	public ResponseEntity<String> getPersonInformation(HttpServletRequest request,HttpServletResponse resopnse){
		//TXNAuditLogs auditLogs=(TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		TXNAuditLogs auditLogs = new TXNAuditLogs();
		ResponseEntity<String> responseEntity=null;
		try {
			String xml=request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			HttpHeaders header=createHeaders();
			header.set("SOAPAction", GetPersonInformationSoapAction);
			header.set("Content-Type", "text/xml;charset=UTF-8");
			
			RestTemplate restTemplate = getRestTemplate();
			HttpEntity mrReadingEntity = new HttpEntity(xml, header);
			auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setRequestMsg(xml);
			auditLogs.setTargetRequesMsg(xml);
			try {
				
				responseEntity=restTemplate.exchange(getPersonInformationURL, HttpMethod.POST,
						mrReadingEntity,String.class);
				auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setTargetResponseMsg(responseEntity.getBody());
				String responseMsg=String.format(IConstants.ESB_XML_SUCCESS_RESPONSE_V2, responseEntity.getBody());
				responseMsg = responseMsg.replaceAll("(<\\?xml.*?\\?>)","");
				responseMsg = responseMsg.replaceAll("(?m)^[ \\t]*\\r?\\n", "");
				auditLogs.setResponseMsg(responseMsg);
				auditLogs.setTargetResponseCode(responseEntity.getStatusCode().name());	
				
			}catch(HttpStatusCodeException e) {
				e.printStackTrace();
				auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
				System.out.println("In exception  "+ e.getResponseBodyAsString());
				auditLogs.setResponseMsg(e.getResponseBodyAsString());
				}
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
		
	}
	
}
