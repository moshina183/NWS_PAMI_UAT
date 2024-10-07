package com.vinsys.diam.controllers;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
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
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
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

import com.diam.services.ClientStar;
import com.diam.services.ClientTrafficInformation;
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
import com.vinsys.diam.services.GetRecordCSHandler;
import com.vinsys.diam.services.GetRecordGISCSHandler;

@RestController
@RequestMapping("api/v1")
public class PAWESBServices

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

	@Value("${addpay-target-url}")
	private String addPayURL;

	@Value("${addpay-soap-action}")
	private String addPaySoapAction;

	@Value("${getcompanydata-target-url}")
	private String getCompanyDataURL;

	@Value("${getcompanydata-soap-action}")
	private String getCompanyDataSoapAction;

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

	@Value("${getsocialbenefitdata-target-request-body}")
	private String getSocialBenefitDataRequestBody;

	@Value("${geteddata-target-request-body}")
	private String getEDDataRequestBody;

	@Value("${getmedcdata-target-url}")
	private String getEDDatatargetURL;
	
	@Value("${getmmdata-target-url}")
	private String getMMDatatargetURL;

	@Value("${getmmdata-target-request-body}")
	private String getMMDatatargetRequestBody;

	@Value("${getmmdata-soap-action}")
	private String getMMdataSoapAction;

	@Autowired
	private GetRecordCSHandler csHandler;

	@Autowired
	private GetRecordGISCSHandler getRecordHandler;

	@Autowired
	private ESPHandler espHandler;

	@Value("${addpay.username}")
	private String addPayUsername;

	@Value("${addpay.pwd}")
	private String addPayPassword;

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

	@Value("${getpersoninformation-target-url}")
	private String getPersonInformationURL;

	@Value("${getpersoninformation-soap-action}")
	private String GetPersonInformationSoapAction;

	@Value("${getdata-target-url}")
	private String getDataURL;

	@Value("${getdata-soap-action}")
	private String getDataSoapAction;

	@Value("${getdata-target-request-body}")
	private String getdatatargetRequestBody;
	
	@Autowired
	private ClientStar clientStar;
	@Autowired
	private ClientTrafficInformation clienttrafficinformation;
	

	
	
	protected RestTemplate getRestTemplate() {
		if (logger.isInfoEnabled())
			logger.info("Entered::PAWESBServices::getRestTemplate()");
		RestTemplate restTemplate = new RestTemplate(
				new BufferingClientHttpRequestFactory(getClientHttpRequestFactory()));
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new com.vinsys.diam.core.LoggingRequestInterceptor());
		restTemplate.setInterceptors(interceptors);
		return restTemplate;
	}

	private SimpleClientHttpRequestFactory getClientHttpRequestFactory() {
		SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(5000);
		clientHttpRequestFactory.setReadTimeout(300000);
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
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);

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

		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);

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
	public ResponseEntity<String> getBillDataService(@RequestParam String BillId, @RequestParam String ccbAccount,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {
		String trustStorePathNew = System.getProperty("javax.net.ssl.trustStore");
        String trustStorePasswordNew = System.getProperty("javax.net.ssl.trustStorePassword");
        System.out.println("---------- Trust Store Path New---- : " + trustStorePathNew);
        System.out.println("---------- Trust Store Password New ----: " + trustStorePasswordNew);
        

		//System.setProperty("SIGN_LOCATION", "D:/Moshina/NWS-ROPservices/config/sign.properties");
		//System.setProperty("ENCRYPT_LOCATION", "D:/Moshina/NWS-ROPservices/config/encrypt.properties");
		//System.setProperty("DECRYPT_LOCATION", "D:/Moshina/NWS-ROPservices/config/decrypt.properties");
		//System.setProperty("VERIFY_LOCATION", "D:/Moshina/NWS-ROPservices/config/verify.properties");

        System.out.println("-SIGN_LOCATION:- : " + System.getProperty("SIGN_LOCATION"));
		System.out.println("-ENCRYPT_LOCATION:- : " + System.getProperty("ENCRYPT_LOCATION"));
		System.out.println("-DECRYPT_LOCATION:- : " + System.getProperty("DECRYPT_LOCATION"));
		System.out.println("-VERIFY_LOCATION:- : " + System.getProperty("VERIFY_LOCATION"));
		return espHandler.getBilldataServiceHandler(BillId, ccbAccount, request);
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

	@GetMapping(value = "/GetGroupBillsInfo", produces = "application/json")
	public ResponseEntity<String> getGroupBillInfo(@RequestParam String year, @RequestParam String month,
			@RequestParam String GroupId, HttpServletRequest request, HttpServletResponse response,
			@RequestHeader Map<String, String> headers) throws Exception {

		return espHandler.getGroupBillsInfoServiceHandler(year, month, GroupId, request);
	}

	@GetMapping(value = "/GetGroupBillDetails", produces = "application/json")
	public ResponseEntity<String> getGroupBillDetails(@RequestParam String year, @RequestParam String month,
			@RequestParam String GroupId, HttpServletRequest request, HttpServletResponse response,
			@RequestHeader Map<String, String> headers) throws Exception {

		String queryString = request.getQueryString();
		return espHandler.getGroupBillDetailsServiceHandler(year, month, GroupId, request);
	}

	@GetMapping(value = "/GetBill64", produces = "application/json")
	public ResponseEntity<String> getBill64(@RequestParam String BID, @RequestParam String ccbAccount,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {
		return espHandler.getBill64ServiceHandler(BID, ccbAccount, request);
	}

	@GetMapping(value = "/InquiryPayment", produces = "application/json")
	public ResponseEntity<String> inquiryPayment(@RequestParam String TrackID, @RequestParam String Gateway,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers)
			throws Exception {

		System.out.println("gateway" + Gateway);
		String queryString = request.getQueryString();
		return espHandler.inquiryPaymentServiceHandler(TrackID, Gateway, request);
	}

	@RequestMapping(value = "/addpay", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_XML_VALUE)
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
			auditLogs.setRequestMsg(xml);
			auditLogs.setTargetRequesMsg(xml);
			try {
				responseEntity = restTemplate.exchange(addPayURL, HttpMethod.POST, mrReadingEntity, String.class);

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

		} catch (IOException | JAXBException e) {
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

			HttpHeaders header = createHeaders();
			header.set("SOAPAction", getCompanyDataSoapAction);
			header.set("Content-Type", "text/xml;charset=UTF-8");

			String auth = getcompanyusername + ":" + getcompanypassword;
			String basicAuthValue = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes("utf-8"));
			header.set("Authorization", basicAuthValue);

			RestTemplate restTemplate = getRestTemplate();
			HttpEntity mrReadingEntity = new HttpEntity(xml, header);
			auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setRequestMsg(xml);
			auditLogs.setTargetRequesMsg(xml);
			try {
				responseEntity = restTemplate.exchange(getCompanyDataURL, HttpMethod.POST, mrReadingEntity,
						String.class);

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

		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}

	@RequestMapping(value = "/inquiryaccount", method = {
			RequestMethod.POST }, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getInquiryAccount(@RequestPayload Envelope envelope, HttpServletRequest request,
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
				responseEntity = restTemplate.exchange(inquiryAccountURL, HttpMethod.POST, mrReadingEntity,
						String.class);

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

		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getplotdetailsbycrookie", method = {
			RequestMethod.POST }, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getPlotDetailsByCrookie(@RequestPayload Envelope envelope, HttpServletRequest request,
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
				responseEntity = restTemplate.exchange(getPlotDetailsByCrookieURL, HttpMethod.POST, mrReadingEntity,
						String.class);

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

		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}

		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getsocialbenefitdata", method = {
			RequestMethod.POST }, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getSocialBenefitData(@RequestPayload Envelope envelope, HttpServletRequest request,
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
				responseEntity = restTemplate.exchange(getSocialBenefitDataURL, HttpMethod.POST, mrReadingEntity,
						String.class);

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

	@PostMapping(value = "/getpersoninformation", produces = "application/xml")
	public ResponseEntity<String> getPersonInformation(HttpServletRequest request, HttpServletResponse response) {
		//TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		TXNAuditLogs auditLogs = new TXNAuditLogs();
		ResponseEntity<String> responseEntity = null;
		try {
		String xml = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		
			builder = factory.newDocumentBuilder();

			Document doc = builder.parse(new InputSource(new StringReader(xml)));
			String crn = doc.getElementsByTagName("crn").item(0).getTextContent();
			String cardExpiryDate = doc.getElementsByTagName("cardExpiryDate").item(0).getTextContent();
			String crnOfRequest = doc.getElementsByTagName("crnOfRequest").item(0).getTextContent();
			System.out.println("------------------------------------------------------");
			System.out.println(crn + "\n" + cardExpiryDate + "\n" + crnOfRequest);
			try {

				System.out.println("-----------------------XML Body------------------------------- :: "+xml);
//				ClientStar clientStar = new ClientStar();
				
				//getpersoninformation auditing 
				//auditLogs.setRequestMsg(xml);
				
				return new ResponseEntity<String>(clientStar.CallROPService(crn,cardExpiryDate,crnOfRequest,auditLogs), HttpStatus.OK);
				
				
				
				} catch (HttpStatusCodeException e) {
					logger.error("Exception while processing ",e);
					
					e.printStackTrace();
					auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
					System.out.println("In exception  " + e.getResponseBodyAsString());
					auditLogs.setResponseMsg(e.getResponseBodyAsString());
				}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		
		return responseEntity;
		
	}
	
	
	/**
	 * @author rupesh
	 * @param request
	 * @param response
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/gettrafficinformaton", produces = "application/xml")
	public ResponseEntity<String> getTrafficInformaton(HttpServletRequest request, HttpServletResponse response) {
		//TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		TXNAuditLogs auditLogs = new TXNAuditLogs();
		ResponseEntity<String> responseEntity = null;
		try {
		String xml = new String(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())).getBytes(),"UTF-8");

			try {
				//gettrafficinformaton auditing 
				auditLogs.setRequestMsg(xml);
				
				return new ResponseEntity<String>(clienttrafficinformation.callTrafficInformation(xml,auditLogs), HttpStatus.OK);
				
				} catch (HttpStatusCodeException e) {
					logger.error("Exception while processing ",e);
					e.printStackTrace();
					auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
					System.out.println("In exception  " + e.getResponseBodyAsString());
					auditLogs.setResponseMsg(e.getResponseBodyAsString());
				}
		}catch (Exception e) {
			// TODO: handle exception
		}

		
		return responseEntity;
		
	}
	
	

	/**
	 * @author trupti
	 * @param request
	 * @param response
	 * @param headers
	 * @return
	 * @throws Exception
	 */

	@Scheduled(cron = "${getdata.cron.schedule}")
	public void getData() throws Exception {
		try{
		InsertionHelper insertionHelper = new InsertionHelper();
		TXNAuditLogs auditLogs = new TXNAuditLogs();
		AuditLogProcessor auditLogProcessor=new AuditLogProcessor();
		ExceptionProcessor exceptionLogprocessor=new ExceptionProcessor();
		ResponseEntity<String> targetResponseEntity = null;
		String nodeOfBuildingPermitNumberOfMedc=null;
		try {

			SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			Date dateBeforeCurrentDate = cal.getTime();
			String date=sdf.format(dateBeforeCurrentDate);
			String xml = String.format(getdatatargetRequestBody, date);
			
			
			HttpHeaders header = createHeaders();
			header.set("SOAPAction", getDataSoapAction);
			header.set("Content-Type", "text/xml;charset=UTF-8");
			RestTemplate restTemplate = getRestTemplate();
			HttpEntity mrReadingEntity = new HttpEntity(xml, header);
			auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setRequestMsg(xml);
			auditLogs.setEsbServiceCode("schedular");
			try {
				TXNAuditSubRequestsLogs subauditlogs=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(xml,auditLogs);
				auditLogs.setEsbReqId(UUID.randomUUID().toString());
				auditLogs.setRequestMsg(xml);
				auditLogs.setTargetRequesMsg(xml);
				logger.info("calling getData target for Insertion in Phase 1");
				targetResponseEntity = restTemplate.exchange(getDataURL, HttpMethod.POST, mrReadingEntity, String.class);
				
				auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setTargetResponseMsg(targetResponseEntity.getBody());
				auditLogs.setResponseMsg(targetResponseEntity.getBody());
				auditLogs.setTargetResponseCode(targetResponseEntity.getStatusCode().name());
				String responseEntity = targetResponseEntity.getBody();
				
				
				
				System.out.println("Before replacing target response body"+responseEntity);
				
			    if(responseEntity.contains("&gt;")) {
					responseEntity=responseEntity.replace("&gt;", ">");
				}
				if(responseEntity.contains("&lt;")) {
					responseEntity=responseEntity.replace("&lt;", "<");
				}
				
				if (responseEntity.contains("&")) {
					responseEntity = responseEntity.replaceAll("&", "&amp;");
				}
				if(responseEntity.contains("<![CDATA[")) {
					responseEntity=responseEntity.replace("<![CDATA[", "");
				}
				if(responseEntity.contains("]]>")) {
					responseEntity=responseEntity.replace("]]>", "");
				}
				responseEntity=responseEntity.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>", "");

				System.out.println("After replacing target response body"+responseEntity);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = null;
				try {
					builder = factory.newDocumentBuilder();
					
					Document doc = builder.parse(new InputSource(new StringReader(responseEntity)));
					NodeList constructionPermitList = doc.getElementsByTagName("CONSTRUCTION_PERMIT");
					for (int i = 0; i < constructionPermitList.getLength(); i++) {
						Element element = (Element) constructionPermitList.item(i);
						element.getElementsByTagName("TYPE_OF_USE").item(0).getTextContent().replace("&amp;", "&");
						String waterValue = element.getElementsByTagName("WATER").item(0).getTextContent();
						String letterValue=element.getElementsByTagName("LETTER").item(0).getTextContent();
						String serviceRequestType=element.getElementsByTagName("SERVICE_REQUEST_TYPE").item(0).getTextContent();
	                    logger.info("Inserting records into REQUESTS table from phase 1 ");
						if((letterValue.equalsIgnoreCase("Y") || letterValue.equalsIgnoreCase("N")) && serviceRequestType.startsWith("MP: CP") && waterValue.equals("1")) {
							insertionHelper.insertMMDataIntoOracleDb(element);
							
							}
						}
					logger.info("Entered in phase 2");
					        
							                  builder = factory.newDocumentBuilder();
							                 String mmreturnStatus=doc.getElementsByTagName("MM_RETURN_STATUS").item(0).getTextContent();
							                 if(mmreturnStatus.equalsIgnoreCase("S")) {
							                	 subauditlogs.setCallSequence(1);
							                	 TXNAuditSubRequestsLogs subAuditLogs=auditLogProcessor.setParameterOfInternalAuditLogAfterTargetCall(subauditlogs,targetResponseEntity);
							                	 auditLogProcessor.InternalsubAuditRequestsLogs(subAuditLogs);
							                       for(int i=0; i <constructionPermitList.getLength();  i++) {
							                	          Element element= (Element) constructionPermitList.item(i);
							                	          String waterValue=element.getElementsByTagName("WATER").item(0).getTextContent();
							                	          String ownerPartyNumber=element.getElementsByTagName("OWNER_PARTY_NUMBER").item(0).getTextContent();
							                	          String substringOfOwnerId=ownerPartyNumber.substring(2);
							                	          String serviceRequestNumber=element.getElementsByTagName("SERVICE_REQUEST_NUMBER").item(0).getTextContent();
							                	         String letterValue=element.getElementsByTagName("LETTER").item(0).getTextContent();
							        					 String serviceRequestType=element.getElementsByTagName("SERVICE_REQUEST_TYPE").item(0).getTextContent();
							                	          if((letterValue.equalsIgnoreCase("Y") || letterValue.equalsIgnoreCase("N")) && serviceRequestType.startsWith("MP: CP") && waterValue.equals("1")) {
							                	                String buildingPermitNumber=element.getElementsByTagName("BUILDING_PERMIT_NUMBER").item(0).getTextContent();
							                	                        
						                	                                        String xml1=String.format(getEDDataRequestBody, buildingPermitNumber);
							              			                                HttpHeaders header1=createHeaders();
							              			                                header1.set("Content-Type", "text/xml;charset=UTF-8");
							              			                                 //for second target call
							              			                                  RestTemplate restTemplate1 = getRestTemplate();
							              			                                  HttpEntity mrReadingEntity1 = new HttpEntity(xml1, header1);
							              			                                  ResponseEntity<String> targetResponseEntity1 = null;
							              			                               try{
							              			        	                         TXNAuditSubRequestsLogs subauditlogs1=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(xml1,auditLogs);
							              			        	                         HttpStatus responsestatus;
							              			        	                       logger.info("calling MEDC target");
							              							                     targetResponseEntity1 = restTemplate1.exchange(getEDDatatargetURL,HttpMethod.POST, mrReadingEntity1, String.class);
							              							                     
							              							                     auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              					                             auditLogs.setResponseTime(new Timestamp(System.currentTimeMillis()));
							              							                     auditLogs.setTargetResponseMsg(targetResponseEntity1.getBody());
							              							                     auditLogs.setResponseMsg(targetResponseEntity1.getBody());
							              							                     auditLogs.setTargetResponseCode(targetResponseEntity1.getStatusCode().name());
							              					
							              							                     subauditlogs1.setCallSequence(2);
							              							                     TXNAuditSubRequestsLogs subAuditLogs1=auditLogProcessor.setParameterOfInternalAuditLogAfterTargetCall(subauditlogs1,targetResponseEntity1);
							              							                     String secondResponseEntity=targetResponseEntity1.getBody();
							              							                     
							              							                     
							              							                   if(secondResponseEntity.contains("&gt;")) {
							              							                	 secondResponseEntity=secondResponseEntity.replace("&gt;", ">");
							              							 				   }
							              							 				   if(secondResponseEntity.contains("&lt;")) {
							              							 					secondResponseEntity=secondResponseEntity.replace("&lt;", "<");
							              							 				   }
							              							 				   
							              							 				  if(secondResponseEntity.contains("<![CDATA[")) {
							              							 					secondResponseEntity=secondResponseEntity.replace("<![CDATA[", "");
							              											   }
							              											   if(secondResponseEntity.contains("]]>")) {
							              												 secondResponseEntity=secondResponseEntity.replace("]]>", "");
							              											    }
							              											   
							              							                     DocumentBuilderFactory fatorySecondTarget=DocumentBuilderFactory.newInstance();
							              							                     DocumentBuilder builderSecondTarget=null;
							              							                            
							              							                            	          builderSecondTarget=fatorySecondTarget.newDocumentBuilder();   
							              							                            	          Document docSecondTarget=builderSecondTarget.parse(new InputSource(new StringReader(secondResponseEntity)));
							              							                            	          String statusCodeOfMedc=docSecondTarget.getElementsByTagName("StatusCode").item(0).getTextContent();
							              							                            	          if(statusCodeOfMedc.equals("00")) {
							              							                            
							              							                            	          	   String IDCardNo=docSecondTarget.getElementsByTagName("IDCardNo").item(0).getTextContent();
									              							                            	        nodeOfBuildingPermitNumberOfMedc=docSecondTarget.getElementsByTagName("BuildingPermit").item(0).getTextContent();
									              							                            	      logger.info("Updating REQUESTS table from MEDC ");
							              							                            	        	insertionHelper.updateMedcDataIntoOracleDB(docSecondTarget,substringOfOwnerId,serviceRequestNumber,IDCardNo);
							              							                            	        	 auditLogProcessor.InternalsubAuditRequestsLogs(subAuditLogs1);
							              							                            	           try {
							              							            		                    	 String xml2=String.format(getMMDatatargetRequestBody, nodeOfBuildingPermitNumberOfMedc);
							              							            		                 		HttpHeaders header2=createHeaders();
							              							            		                 		header2.set("SOAPAction", getMMdataSoapAction);
							              							            		                 		header2.set("Content-Type","text/xml;charset=UTF-8");
							              							            		                 		//for third target call 
							              							            		                 		RestTemplate restTemplate2=getRestTemplate();
							              							            		                 		HttpEntity mrReadingEntity2=new HttpEntity(xml2, header2);
							              							            		                 		ResponseEntity<String> targetResponseEntity2=null;
							              							            		                 		
							              							            		                 		                  try{
							              							            	        	                                                 TXNAuditSubRequestsLogs subauditlogs2=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(xml2,auditLogs);
							              							            	        	                                               logger.info("calling MM-New Target ");
							              							            	        	                                                 targetResponseEntity2 = restTemplate2.exchange(getMMDatatargetURL,HttpMethod.POST, mrReadingEntity2, String.class);
							              							            	              							                     
							              							            	              							                     auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              							            	              					                             auditLogs.setResponseTime(new Timestamp(System.currentTimeMillis()));
							              							            	              							                     auditLogs.setTargetResponseMsg(targetResponseEntity2.getBody());
							              							            	              							                     auditLogs.setResponseMsg(targetResponseEntity2.getBody());
							              							            	              							                     auditLogs.setTargetResponseCode(targetResponseEntity2.getStatusCode().name());
							              							            	              					
							              							            	              							                   subauditlogs2.setCallSequence(3);
							              							            	              							                     TXNAuditSubRequestsLogs subAuditLogs2=auditLogProcessor.setParameterOfInternalAuditLogAfterTargetCall(subauditlogs2,targetResponseEntity2);
							              							            	              							                     String thirdResponseEntity=targetResponseEntity2.getBody();
							              							            	              							                     
							              							            	              							                 if(thirdResponseEntity.contains("&gt;")) {
							              							            	              							                	thirdResponseEntity=thirdResponseEntity.replace("&gt;", ">");
							              											              							 				   }
							              											              							 				   if(thirdResponseEntity.contains("&lt;")) {
							              											              							 					thirdResponseEntity=thirdResponseEntity.replace("&lt;", "<");
							              											              							 				   }
							              											              							 				   
							              											              							 				  if(thirdResponseEntity.contains("<![CDATA[")) {
							              											              							 					thirdResponseEntity=thirdResponseEntity.replace("<![CDATA[", "");
							              											              											   }
							              											              											   if(thirdResponseEntity.contains("]]>")) {
							              											              												thirdResponseEntity=thirdResponseEntity.replace("]]>", "");
							              											              											    }
							              											              											   
							              											              											thirdResponseEntity=thirdResponseEntity.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>", "");
							              											              		                                        
							              											              											   
							              											              											  
							              							            	              							                     DocumentBuilderFactory fatoryThirdTarget=DocumentBuilderFactory.newInstance();
							              							            	              							                     DocumentBuilder builderThirdTarget=null;
							              							            	              							                            
							              							            	              							                            	          builderThirdTarget=fatoryThirdTarget.newDocumentBuilder();  
							              							            	              							                            	          Document docThirdTarget=builderThirdTarget.parse(new InputSource(new StringReader(thirdResponseEntity)));
							              							            	              							                            	          String mmReturnStatusOfMMNewResponse=docThirdTarget.getElementsByTagName("MM_RETURN_STATUS").item(0).getTextContent();
							              							            	              							                            	          if(mmReturnStatusOfMMNewResponse.equals("S")) {
							              							            	              							                            	        	  
							              							            	              							                            	        	 String plotSize=docThirdTarget.getElementsByTagName("PLOT_SIZE").item(0).getTextContent();
								              							            	              							                            	          String allowedPercentageArea=docThirdTarget.getElementsByTagName("ALLOWED_PERCENTAGE_AREA").item(0).getTextContent();
								              							            	              							                            	          
								              							            	              							                            	          int plotSizeIntegerValue=Integer.parseInt(plotSize);
								              							            	              							                            	          int allowedPrecentageAreaIntegerValue=Integer.parseInt(allowedPercentageArea);
								              							            	              							                            	        
								              							            	              							                            	         int certifiedBuildingArea=(plotSizeIntegerValue*allowedPrecentageAreaIntegerValue)/100;
								              							            	              							                            	         String certifiedbuildingarea=String.valueOf(certifiedBuildingArea);
								              							            	              							                            	          
							              							            	              							                            	        	   
							              							            	              							                            	        	    	Element element1=(Element) constructionPermitList.item(i);
							              							            	              							                            	        	       logger.info("updating REQUESTS table from MM-New");
							              							            	              							                            	        	    	insertionHelper.updateMMDataIntoOracleDB(docThirdTarget, element1,certifiedbuildingarea,serviceRequestNumber,substringOfOwnerId,allowedPercentageArea);
							              							            	              							                            	        	    	auditLogProcessor.InternalsubAuditRequestsLogs(subAuditLogs2);
							              							            	              							                            	        	    
							              							            	              							                            	        	    	          if(element1.getElementsByTagName("OWNER_PARTY_NUMBER").item(0).getTextContent().startsWith("CV")) {

							              							            	              							                            	        	    	        	  String ownerPartyNo=element1.getElementsByTagName("OWNER_PARTY_NUMBER").item(0).getTextContent();
							              							            	              							                            	        	    	        	 
							              							            	              							                            	        	    	            if(!checkIfNullOrEmpty(IDCardNo)) {
							              							            	              							                            	        	    	        	  try {
							              							            	              							                            	        	    	        		          String xml3=String.format(getSocialBenefitDataRequestBody, IDCardNo);
							              							            	              							                            	        	    	        		          HttpHeaders header3=new HttpHeaders();
							              							            	              							                            	        	    	        		          header3.set("SOAPAction", getSocialBenefitDataSoapAction);
							              							            	              							                            	        	    	      			          header3.set("Content-Type", "text/xml;charset=UTF-8");
							              							            	              							                            	        	    	      			          String auth = getSocialBenefitDataUsername + ":" + getSocialBenefitDataPassword;
							              							            	              							                            	        	    	      			          String basicAuthValue = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes("utf-8"));
							              							            	              							                            	        	    	      			          header3.set("Authorization", basicAuthValue);
							              							            	              							                            	        	    	      			          ResponseEntity<String> targetResponseEntity3=null;
							              							            	              							                            	        	    	      			          String fourthResponseEntity=null;
							              							            	              							                            	        	    	      			         //for forth target call
							              							            	              							                            	        	    	      			         RestTemplate restTemplate3 = getRestTemplate();
							              							            	              							                            	        	    	      			         HttpEntity mrReadingEntity3 = new HttpEntity(xml3, header3);
							              							            	              							                            	        	    	      			             try {
							              							            	              							                            	        	    	      			                      TXNAuditSubRequestsLogs subauditlogs3=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(xml3,auditLogs);
							              							            	              							                            	        	    	      			                      logger.info("calling MOSD target");
							              							            	              							                            	        	    	   				                      targetResponseEntity3 = restTemplate3.exchange(getSocialBenefitDataURL, HttpMethod.POST,
							              							            	              							                            	        	    	   						              mrReadingEntity3, String.class);

							              							            	              							                            	        	    	   				                      auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              							            	              							                            	        	    	   				                      auditLogs.setTargetResponseMsg(targetResponseEntity3.getBody());
							              							            	              							                            	        	    	   				                      auditLogs.setResponseMsg(targetResponseEntity3.getBody());
							              							            	              							                            	        	    	   				                      auditLogs.setTargetResponseCode(targetResponseEntity3.getStatusCode().name());
							              							            	              							                            	        	    	   				                      subauditlogs3.setCallSequence(4);
							              												              							            	              							                              TXNAuditSubRequestsLogs subAuditLogs3=auditLogProcessor.setParameterOfInternalAuditLogAfterTargetCall(subauditlogs3,targetResponseEntity3);
							              												              							            	              							                              fourthResponseEntity=targetResponseEntity3.getBody();
							              												              							            	              							                              
							              												              							            	              							                              if(fourthResponseEntity.contains("<![CDATA[")) {
							              												              							            	              							                                      fourthResponseEntity=fourthResponseEntity.replace("<![CDATA[", "");
							              												              							            	              							  				              }
							              												              							            	              							  				              if(fourthResponseEntity.contains("]]>")) {
							              												              							            	              							  				                      fourthResponseEntity=fourthResponseEntity.replace("]]>", "");
							              												              							            	              							  				              }
							              												              							            	              							  				              
							              												              							            	              							  				      if(fourthResponseEntity.contains("&gt;")) {
							              												              							            	              							  				      fourthResponseEntity=fourthResponseEntity.replace("&gt;", ">");
							              													              											              							 				   }
							              													              											              							 				   if(fourthResponseEntity.contains("&lt;")) {
							              													              											              							 			    	fourthResponseEntity=fourthResponseEntity.replace("&lt;", "<");
							              													              											              							 				   }
							              												              							            	              							  				              fourthResponseEntity=fourthResponseEntity.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>", "");
							              												              							            	              							  				             
							              												              							            	              							  				              
							              												              							            	              							  				              DocumentBuilderFactory fatoryFourthTarget=DocumentBuilderFactory.newInstance();
							              												              							            	              							                              DocumentBuilder builderFourthTarget=null;
							              												              							            	              							                              builderFourthTarget=fatoryFourthTarget.newDocumentBuilder(); 
							              												              							            	              							                              Document docFourthTarget=builderFourthTarget.parse(new InputSource(new StringReader(fourthResponseEntity)));
							              												              							            	              							                              String fileRecid=docFourthTarget.getElementsByTagName("FILE_RECID").item(0).getTextContent();
							              												              							            	              							                             
							              												              							            	              							                              
							              												              							            	              							                              
							              												              							            	              							                                            if(fileRecid.equals("9041")) {
							              												              							            	              							                                            	
							              												              							            	              							                                                logger.info("updating REQUESTS table from MOSD");
							              												              							            	              							                                            	insertionHelper.updateMOSDDataIntoOracleDB(docFourthTarget,substringOfOwnerId,serviceRequestNumber);
							              												              							            	              							                                                auditLogProcessor.InternalsubAuditRequestsLogs(subAuditLogs3);
							              												              							            	              							                                                
							              												              							            	              							                                                                    
							              												              							            	              							                                            }else if(fileRecid.equals("-")){
							              												              							            	              							                                          logger.error("getting error response from MOSD target"); 
							              											              							            	              							                            	        	 
							              											              							            	              							                            	        
							              											              							            	              							                   	                     TXNAuditSubRequestsLogs subauditlogsfourthCall=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(auditLogs.getRequestMsg(),auditLogs);
							              											              							            	              							               			                 subauditlogsfourthCall.setSubResponseMsg("<Response><errorMessage>receiving error response from Fourth target </errorMessage></Response>");
							              											              							            	              							               			                 subauditlogsfourthCall.setSubTargetResponseMsg(targetResponseEntity3.getBody());
							              											              							            	              							               			                 subauditlogsfourthCall.setSubResponseTime(new Timestamp(System.currentTimeMillis()));
							              											              							            	              							               			                 subauditlogsfourthCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              											              							            	              							               			                 subauditlogsfourthCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              											              							            	              							               			                 subauditlogsfourthCall.setCallSequence(4);
							              											              							            	              							               	                        exceptionLogprocessor.InternalsubAuditRequestsLogs(subauditlogsfourthCall);
							              											              							            	              							               			                auditLogs.setTargetResponseMsg(targetResponseEntity3.getBody());
							              											              							            	              							               			                auditLogs.setResponseMsg("<Response><errorMessage>receiving error response from Fourth target</errorMessage></Response>");
							              											              							            	              							                            	        	  
							              											              							            	              							                            	          }
							              												              							            	              							                              
							              							            	              							                            	        	    	   			                    }catch (HttpStatusCodeException e){
							              							  					              							            	 					              			                	
							              												              							            		              			                	                  TXNAuditSubRequestsLogs subauditlogsForthCall=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(xml3,auditLogs);
							              												              							            		              			                	            logger.error("Could not able to MOSD target");
							              												              							            		              			        			                  subauditlogsForthCall.setSubResponseMsg("<Response><errorMessage>Cannot get response from fourth target </errorMessage></Response>");
							              												              							            		              			        			                  subauditlogsForthCall.setSubTargetResponseMsg("<Response><errorMessage>Cannot get response from fourth target</errorMessage></Response>");
							              												              							            		              			        			                  subauditlogsForthCall.setSubResponseTime(new Timestamp(System.currentTimeMillis()));
							              												              							            		              			        			                  subauditlogsForthCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              												              							            		              			        		                   	  subauditlogsForthCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              												              							            		              			        			                  subauditlogsForthCall.setCallSequence(4);
							              												              							            		              			        	                          exceptionLogprocessor.InternalsubAuditRequestsLogs(subauditlogsForthCall);
							              												              							            		              			        			                 auditLogs.setTargetResponseMsg("<Response><errorMessage>Cannot get response from forth target</errorMessage></Response>");
							              												              							            		              			        			                 auditLogs.setResponseMsg("<Response><errorMessage>Cannot get response from forth target</errorMessage></Response>");
							              												              							            		              			      			
							              												              							            		              			                               }
							              							            	              							                            	        	    	        		  
							              							            	              							                            	        	    	        	  }catch (Exception e) {
							              							            	   					              							            		                    	 TXNAuditSubRequestsLogs subauditlogsForthCall=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(auditLogs.getRequestMsg(),auditLogs);
							              							            	   					              							            		                  logger.error("Error while executing MOSD target");
							              							           					              							            		      	        			subauditlogsForthCall.setSubResponseMsg("<Response><errorMessage>Dear Consumer, Your request could not be processed, please try again from  target</errorMessage></Response>");
							              							           					              							            		      	        			subauditlogsForthCall.setSubTargetResponseMsg("<Response><errorMessage>Dear Consumer, Your request could not be processed, please try again</errorMessage></Response>");
							              							           					              							            		      	        			subauditlogsForthCall.setSubResponseTime(new Timestamp(System.currentTimeMillis()));
							              							           					              							            		      	        			subauditlogsForthCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              							           					              							            		      	        			subauditlogsForthCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              							           					              							            		      	        			subauditlogsForthCall.setCallSequence(4);
							              							           					              							            		      	        	        exceptionLogprocessor.InternalsubAuditRequestsLogs(subauditlogsForthCall);
							              							           					              							            		      	        			
							              							           					              							            		      	        			auditLogs.setTargetResponseMsg("<Response><errorMessage>Dear Consumer, Your request could not be processed , please try again</errorMessage></Response>");
							              							           					              							            		      	        			System.out.println("In Exception " + e.getMessage());
							              							           					              							            		      	        			auditLogs.setResponseMsg("<Response><errorMessage>Dear Consumer, Your request could not be processed , please try again</errorMessage></Response>");
							              							           					              							            							    }
							              							            	              							                            	        	    	            
							              							            	              							                            	        	    	            
							              							            	              							                            	        	    	}else {
							              							            	              							                            	        	          System.out.println("=========receive IDCardNo as NULL=============");
							              							            	              							                            	        	          logger.info("==============receive IDCardNo as NULL from MEDC Target==================");
							              							            	              							                            	        	    		
							              							            	              							                            	        	    	}
							              							            	              							                            	        	    	            
							              							            	              							                            	        	    	}
							              							            	              							                            	        	    	      String coordinateN1=docSecondTarget.getElementsByTagName("N1").item(0).getTextContent();
							              								              							                                                                     String coordinateN2=docSecondTarget.getElementsByTagName("N2").item(0).getTextContent();
							              								              							                                                                     String coordinateE1=docSecondTarget.getElementsByTagName("E1").item(0).getTextContent();
							              								              							                                                                     String coordinateE2=docSecondTarget.getElementsByTagName("E2").item(0).getTextContent();
							              								              							                                                                
						              							            	              							                                                                    if(!checkIfNullOrEmpty(coordinateN1) && !checkIfNullOrEmpty(coordinateN2) && !checkIfNullOrEmpty(coordinateE1)  && !checkIfNullOrEmpty(coordinateE2)) {
						              							            	              							                                                                    	
						              							            	              							                                                                    logger.info("calling getRecordGIS target");
						              							            	              							                                                                    	
						              							            	              							                                                                    	String getGIStargetURL=getRecordGIS+IConstants.FWD_SLASH+"2"+IConstants.FWD_SLASH+coordinateE1+IConstants.FWD_SLASH+coordinateN1;
						              							            	              							                                                                        HttpHeaders header4 = createHeaders();
						              							            	              							                                                    		            RestTemplate restTemplate4 = getRestTemplate();
						              							            	              							                                                    		            HttpEntity<String> getRecordGISEntity = new HttpEntity<String>(header4);
						              							            	              							                                                    		            auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
						              							            	              							                                                    		                        try{
						              							            	              							                                                    		                        TXNAuditSubRequestsLogs subauditlogs4=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(getGIStargetURL,auditLogs);
						              							            	              							                                                    		                    
						              							            	              							                                                    			                          getRecordGISEntity = restTemplate4
						              							            	              							                                                    					                  .getForEntity(getGIStargetURL, String.class);
						              							            	              							                                                    			                         
						              							            	              							                                                    			                          auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
						              							            	              							                                                    			                          auditLogs.setResponseMsg(getRecordGISEntity.getBody());
						              							            	              							                                                    			                          auditLogs.setTargetResponseMsg(getRecordGISEntity.getBody());
						              							            	              							                                                    			                          
						              							            	              							                                                    			                          JSONObject obj=new JSONObject(getRecordGISEntity.getBody());
						              							            	              							                                                    			                          String accountID=obj.getJSONArray("WATER METER").getJSONObject(0).getString("ACCOUNT_ID");
						              							            	              							                                                    			                          String DMAName=obj.getJSONArray("DMA ZONE").getJSONObject(0).getString("DMA_NAME");
						              							            	              							                                                    			                          String withinDMA=obj.getJSONArray("DMA ZONE").getJSONObject(0).getString("WITHIN_DMA");
						              							            	              							                                                    			                          
						              							            	              							                                                    			                      logger.info("===============updating requests table from GISRecord========================"+coordinateN1+"\br"+coordinateN2+"\br"+coordinateE1+"\br"+coordinateE2); 
						              							            	              							                                                    			                         
						              							            	              							                                                    			                        	insertionHelper.updateGISDetailsDataIntoOracleDB(accountID, DMAName,withinDMA,substringOfOwnerId,serviceRequestNumber);
						              							            	              							                                                    			                            subauditlogs4.setCallSequence(5);
						              							            	              							                                                    			                          TXNAuditSubRequestsLogs subauditlogs5=auditLogProcessor.setParameterOfInternalAuditLogAfterTargetCallForGIS(subauditlogs4,getRecordGISEntity);
						              							            	              							                                                    			                        	  auditLogProcessor.InternalsubAuditRequestsLogs(subauditlogs4);
						              							            	              							                                                    			                          
						              							            	              							                                                    			                          
						              							            	              							                                                    			                                
						              							            	              							                                                    		                        }catch (HttpStatusCodeException e) {
						              							            	              							                                                    		                        	System.out.println(e.getLocalizedMessage());
						              							            	              							                                                    		                        TXNAuditSubRequestsLogs subauditFifthCall=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(getGIStargetURL,auditLogs);
						              							            	              							                                                    		                    logger.error("Could not able to call GetRecordGIS target");
						              							            	              							                                                    		                      subauditFifthCall.setSubResponseMsg("<Response><errorMessage>Cannot get response from fourth target </errorMessage></Response>");
					              												              							            		              			        			                  subauditFifthCall.setSubTargetResponseMsg("<Response><errorMessage>Cannot get response from fourth target</errorMessage></Response>");
					              												              							            		              			        			                  subauditFifthCall.setSubResponseTime(new Timestamp(System.currentTimeMillis()));
					              												              							            		              			        			                  subauditFifthCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
					              												              							            		              			        		                   	  subauditFifthCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
					              												              							            		              			        			                  subauditFifthCall.setCallSequence(5);
					              												              							            		              			        	                          exceptionLogprocessor.InternalsubAuditRequestsLogs(subauditFifthCall);
					              												              							            		              			        			                 auditLogs.setTargetResponseMsg("<Response><errorMessage>Cannot get response from forth target</errorMessage></Response>");
					              												              							            		              			        			                 auditLogs.setResponseMsg("<Response><errorMessage>Cannot get response from forth target</errorMessage></Response>");
						              							            	              							                                                    			                        
						              							            	              							                                                    		                       }
						              							            	              							                                                                    	
						              							            	              							                                                                 }else{
						              							            	              							                                                                	String getGIStargetURL=getRecordGIS+IConstants.FWD_SLASH+coordinateE1+IConstants.FWD_SLASH+coordinateN1;
						              							            	              							                                                                logger.error("=========================getting null coordinates=======================");
						              							            	              							                                                                	TXNAuditSubRequestsLogs subauditFifthCall=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(getGIStargetURL,auditLogs);
						       					              							            	              							               			                 subauditFifthCall.setSubResponseMsg("<Response><errorMessage>getting null coordinates </errorMessage></Response>");
						       					              							            	              							               			                 subauditFifthCall.setSubResponseTime(new Timestamp(System.currentTimeMillis()));
						       					              							            	              							               			                 subauditFifthCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
						       					              							            	              							               			                 subauditFifthCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
						       					              							            	              							               			                 subauditFifthCall.setCallSequence(5);
						       					              							            	              							               			                auditLogs.setResponseMsg("<Response><errorMessage>getting null coordinates</errorMessage></Response>");
						       					              							            	              							               			                exceptionLogprocessor.InternalsubAuditRequestsLogs(subauditFifthCall);
						              							            	              							                                                                	 
						              							            	              							                                                                 }
							              							            	              							                            	        	    	
							              							            	              							                            	        	    	
						              							            	              							                                                                    
							              							            	              							                            	        	    
							              							            	              							                            	          }else if(mmReturnStatusOfMMNewResponse.equals("E")){
							              							            	              							                            	        	logger.error("getting error response from MM-New target"); 
							              							            	              							                            	        	 
							              							            	              							                            	        
							              							            	              							                   	                     TXNAuditSubRequestsLogs subauditlogsthirdCall=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(auditLogs.getRequestMsg(),auditLogs);
							              							            	              							               			                 subauditlogsthirdCall.setSubResponseMsg("<Response><errorMessage>receiving error response from third target </errorMessage></Response>");
							              							            	              							               			                 subauditlogsthirdCall.setSubTargetResponseMsg(thirdResponseEntity);
							              							            	              							               			                 subauditlogsthirdCall.setSubResponseTime(new Timestamp(System.currentTimeMillis()));
							              							            	              							               			                 subauditlogsthirdCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              							            	              							               			                 subauditlogsthirdCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              							            	              							               			                 subauditlogsthirdCall.setCallSequence(3);
							              							            	              							               	                        exceptionLogprocessor.InternalsubAuditRequestsLogs(subauditlogsthirdCall);
							              							            	              							               			                auditLogs.setTargetResponseMsg(thirdResponseEntity);
							              							            	              							               			                auditLogs.setResponseMsg("<Response><errorMessage>receiving error response from third target</errorMessage></Response>");
							              							            	              							                            	        	  
							              							            	              							                            	          }
							              							            	              							                            	          
							              							            	        	                                                 
							              							            		                 		                    }catch (HttpStatusCodeException e){
							              							            	 					              			                	
							              							            		              			                	                  TXNAuditSubRequestsLogs subauditlogsThirdCall=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(auditLogs.getRequestMsg(),auditLogs);
							              							            		              			                	              logger.error("Could not able to call MM-New target");
							              							            		              			        			                  subauditlogsThirdCall.setSubResponseMsg("<Response><errorMessage>Cannot get response from third target </errorMessage></Response>");
							              							            		              			        			                  subauditlogsThirdCall.setSubTargetResponseMsg("<Response><errorMessage>Cannot get response from third target</errorMessage></Response>");
							              							            		              			        			                  subauditlogsThirdCall.setSubResponseTime(new Timestamp(System.currentTimeMillis()));
							              							            		              			        			                  subauditlogsThirdCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              							            		              			        		                   	  subauditlogsThirdCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              							            		              			        			                  subauditlogsThirdCall.setCallSequence(3);
							              							            		              			        	                          exceptionLogprocessor.InternalsubAuditRequestsLogs(subauditlogsThirdCall);
							              							            		              			        			                 auditLogs.setTargetResponseMsg("<Response><errorMessage>Cannot get response from third target</errorMessage></Response>");
							              							            		              			        			                 auditLogs.setResponseMsg("<Response><errorMessage>Cannot get response from third target</errorMessage></Response>");
							              							            		              			      			
							              							            		              			                               }
							              							            		             
							              							            		                     }catch (Exception e) {
							              							            		                    	 TXNAuditSubRequestsLogs subauditlogsThirdCall=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(auditLogs.getRequestMsg(),auditLogs);
							              							            		                    	logger.error("Error while executing MM-New target");
							              							            		      	        			subauditlogsThirdCall.setSubResponseMsg("<Response><errorMessage>Dear Consumer, Your request could not be processed, please try again from  target</errorMessage></Response>");
							              							            		      	        			subauditlogsThirdCall.setSubTargetResponseMsg("<Response><errorMessage>Dear Consumer, Your request could not be processed, please try again</errorMessage></Response>");
							              							            		      	        			subauditlogsThirdCall.setSubResponseTime(new Timestamp(System.currentTimeMillis()));
							              							            		      	        			subauditlogsThirdCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              							            		      	        			subauditlogsThirdCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              							            		      	        			subauditlogsThirdCall.setCallSequence(3);
							              							            		      	        	        exceptionLogprocessor.InternalsubAuditRequestsLogs(subauditlogsThirdCall);
							              							            		      	        			
							              							            		      	        			auditLogs.setTargetResponseMsg("<Response><errorMessage>Dear Consumer, Your request could not be processed , please try again</errorMessage></Response>");
							              							            		      	        			System.out.println("In Exception " + e.getMessage());
							              							            		      	        			auditLogs.setResponseMsg("<Response><errorMessage>Dear Consumer, Your request could not be processed , please try again</errorMessage></Response>");
							              							            							}
							              							                            	        	  
							              							                            	          }else if(statusCodeOfMedc.equals("12")){
							              							                            	        	  
							              							                            	        	logger.error("getting error response from MEDC target"); 
							              							                            	        	TXNAuditSubRequestsLogs subauditlogsSecondCall=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(auditLogs.getRequestMsg(),auditLogs);
							              							          			        			subauditlogsSecondCall.setSubResponseMsg("<Response><errorMessage>receiving error response from second target </errorMessage></Response>");
							              							          			        			subauditlogsSecondCall.setSubTargetResponseMsg(secondResponseEntity);
							              							          			        			subauditlogsSecondCall.setSubResponseTime(new Timestamp(System.currentTimeMillis()));
							              							          			        			subauditlogsSecondCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              							          			        			subauditlogsSecondCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              							          			        			subauditlogsSecondCall.setCallSequence(2);
							              							          			        	        exceptionLogprocessor.InternalsubAuditRequestsLogs(subauditlogsSecondCall);
							              							          			        			
							              							          			        			auditLogs.setTargetResponseMsg(secondResponseEntity);
							              							          			        			auditLogs.setResponseMsg("<Response><errorMessage>receiving error response from second target</errorMessage></Response>");
							              							                            	        	 
							              							                            	          }
							              							                            	          
							              			                              }catch (HttpStatusCodeException e) {
							              			                	
							              			                	
							              			                	                  TXNAuditSubRequestsLogs subauditlogsSecondCall=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(auditLogs.getRequestMsg(),auditLogs);
							              			                	                  logger.error("could not able to call MEDC target");
							              			        			                  subauditlogsSecondCall.setSubResponseMsg("<Response><errorMessage>Cannot get response from second target </errorMessage></Response>");
							              			        			                  subauditlogsSecondCall.setSubTargetResponseMsg("<Response><errorMessage>Cannot get response from second target</errorMessage></Response>");
							              			        			                  subauditlogsSecondCall.setSubResponseTime(new Timestamp(System.currentTimeMillis()));
							              			        			                  subauditlogsSecondCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              			        		                   	  subauditlogsSecondCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							              			        			                  subauditlogsSecondCall.setCallSequence(2);
							              			        	                          exceptionLogprocessor.InternalsubAuditRequestsLogs(subauditlogsSecondCall);
							              			        			                 auditLogs.setTargetResponseMsg("<Response><errorMessage>Cannot get response from second target</errorMessage></Response>");
							              			        			                 auditLogs.setResponseMsg("<Response><errorMessage>Cannot get response from second target</errorMessage></Response>");
							              			      			
							              			                               }
							              			                               
							              			                                           
							              			                                             
							               }
						            }
							  
						  }else {
							      
							           TXNAuditSubRequestsLogs subAuditLogsForErrorResponse=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(auditLogs.getRequestMsg(), auditLogs);
							           logger.error("getting error response from MMIntegration Phase 1 target");
							           subAuditLogsForErrorResponse.setSubResponseMsg("<Response><errorMessage>getting error response from first target</Response></errorMessage>");
							           subAuditLogsForErrorResponse.setCallSequence(2);
							           subAuditLogsForErrorResponse.setSubResponseTime(new Timestamp(System.currentTimeMillis()));
							           subAuditLogsForErrorResponse.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							           subAuditLogsForErrorResponse.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
							           auditLogs.setResponseMsg("<Response><errorMessage>getting error response from first target</Response></errorMessage>");
							           subAuditLogsForErrorResponse.setSubResponseTime(new Timestamp(System.currentTimeMillis()));
							           subAuditLogsForErrorResponse.setSubTargetResponseMsg(targetResponseEntity.getBody());
							           exceptionLogprocessor.InternalsubAuditRequestsLogs(subAuditLogsForErrorResponse);
							           
							  
							                 
		                          }
					
		        	 
		                    }catch (Exception e) {

		      	        			TXNAuditSubRequestsLogs subauditlogsSecondCall=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(auditLogs.getRequestMsg(),auditLogs);
		      	        			logger.error("error while executing MEDC target");
		      	        			subauditlogsSecondCall.setSubResponseMsg("<Response><errorMessage>Dear Consumer, Your request could not be processed, please try again from second target</errorMessage></Response>");
		      	        			subauditlogsSecondCall.setSubTargetResponseMsg("<Response><errorMessage>Dear Consumer, Your request could not be processed, please try again</errorMessage></Response>");
		      	        			subauditlogsSecondCall.setSubResponseTime(new Timestamp(System.currentTimeMillis()));
		      	        			subauditlogsSecondCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
		      	        			subauditlogsSecondCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
		      	        			subauditlogsSecondCall.setCallSequence(2);
		      	        	        exceptionLogprocessor.InternalsubAuditRequestsLogs(subauditlogsSecondCall);
		      	        			
		      	        			auditLogs.setTargetResponseMsg("<Response><errorMessage>Dear Consumer, Your request could not be processed, please try again</errorMessage></Response>");
		      	        			System.out.println("In Exception " + e.getMessage());
		      	        			auditLogs.setResponseMsg("<Response><errorMessage>Dear Consumer, Your request could not be processed, please try again</errorMessage></Response>");
					      }
					
					
				
				   }catch (HttpStatusCodeException e) {
		             	  
	                 TXNAuditSubRequestsLogs subauditlogsFirstCall=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(auditLogs.getRequestMsg(),auditLogs);
	                 logger.error("Could not able to call MMIntegration Phase 1 target");
	       			subauditlogsFirstCall.setSubResponseMsg("<Response><errorMessage>Cannot get response from first target </errorMessage></Response>");
	       			subauditlogsFirstCall.setSubTargetResponseMsg("<Response><errorMessage>Cannot get response from first target</errorMessage></Response>");
	       			subauditlogsFirstCall.setSubResponseTime(new Timestamp(System.currentTimeMillis()));
	       			subauditlogsFirstCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
	       			subauditlogsFirstCall.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
	       			subauditlogsFirstCall.setCallSequence(2);
	       	        exceptionLogprocessor.InternalsubAuditRequestsLogs(subauditlogsFirstCall);
	       			
	       			auditLogs.setTargetResponseMsg("<Response><errorMessage>Cannot get response from first target</errorMessage></Response>");
	       			System.out.println("In Exception " + e.getMessage());
	       			auditLogs.setResponseMsg("<Response><errorMessage>Cannot get response from first target</errorMessage></Response>");
	             }

			}catch (Exception e) {
				   
				   TXNAuditSubRequestsLogs subauditlogs=auditLogProcessor.setParametersOfInternalAuditLogBeforeTargetCall(auditLogs.getRequestMsg(),auditLogs);
				   logger.error("error while executing MMIntegration phase 1 target");
					subauditlogs.setSubResponseMsg("<Response><errorMessage>Dear Consumer, Your request could not be processed from first target, please try again</errorMessage></Response>");
					subauditlogs.setSubTargetResponseMsg("<Response><errorMessage>Dear Consumer, Your request could not be processed, please try again</errorMessage></Response>");
					subauditlogs.setSubResponseTime(new Timestamp(System.currentTimeMillis()));
					subauditlogs.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
					subauditlogs.setSubTargetResponseTime(new Timestamp(System.currentTimeMillis()));
					subauditlogs.setCallSequence(1);
			        exceptionLogprocessor.InternalsubAuditRequestsLogs(subauditlogs);
					
					auditLogs.setTargetResponseMsg("<Response><errorMessage>Dear Consumer, Your request could not be processed, please try again</errorMessage></Response>");
					System.out.println("In Exception " + e.getMessage());
					auditLogs.setResponseMsg("<Response><errorMessage>Dear Consumer, Your request could not be processed, please try again</errorMessage></Response>");
				   
				
			  }		

		 GetDetails getDetails = new GetDetails();
		  String getAggragationServiceStatus=getDetails.getAggregationStatusFromSubRequestsLogs("schedular");
		  if( getAggragationServiceStatus.equalsIgnoreCase("FAILED")) {
				ExceptionProcessor exceptionProcessor=new ExceptionProcessor();
				exceptionProcessor.process(auditLogs);
		  }else {
				auditLogProcessor.process(auditLogs);

		  }
		 
		  
		  List<String> listOfBuildingPermitNo=insertionHelper.fetchDataFromRequestsWhereOwnerEnNameAndCoordinatesIsNull();
		
		  for(int i=0;i<listOfBuildingPermitNo.size();i++) {
			  try {
			  String xmlRequest=String.format(getEDDataRequestBody, listOfBuildingPermitNo.get(i));
	            HttpHeaders headerParam=createHeaders();
	            headerParam.set("Content-Type", "text/xml;charset=UTF-8");

	              RestTemplate restTemplateAfterExecution = getRestTemplate();
	              HttpEntity mrReadingEntityAfterExecution = new HttpEntity(xmlRequest, headerParam);
	              ResponseEntity<String> targetResponseOfMEDCAfterExecution = null;
	                  try {
	                	  logger.info("calling MEDC target after complete execution of schedular");
	                	  targetResponseOfMEDCAfterExecution = restTemplateAfterExecution.exchange(getEDDatatargetURL,HttpMethod.POST, mrReadingEntityAfterExecution, String.class);
	                	 String targetResponseEntityAfterExecuteSchedular=targetResponseOfMEDCAfterExecution.getBody();
	                	 
	                	 if(targetResponseEntityAfterExecuteSchedular.contains("&gt;")) {
	                		  targetResponseEntityAfterExecuteSchedular=targetResponseEntityAfterExecuteSchedular.replace("&gt;", ">");
			 				   }
			 				   if(targetResponseEntityAfterExecuteSchedular.contains("&lt;")) {
			 					  targetResponseEntityAfterExecuteSchedular=targetResponseEntityAfterExecuteSchedular.replace("&lt;", "<");
			 				   }
			 				   
			 				  if(targetResponseEntityAfterExecuteSchedular.contains("<![CDATA[")) {
			 					 targetResponseEntityAfterExecuteSchedular=targetResponseEntityAfterExecuteSchedular.replace("<![CDATA[", "");
							   }
							   if(targetResponseEntityAfterExecuteSchedular.contains("]]>")) {
								   targetResponseEntityAfterExecuteSchedular=targetResponseEntityAfterExecuteSchedular.replace("]]>", "");
							    }
	                	  
							   DocumentBuilderFactory fatoryMedcTarget=DocumentBuilderFactory.newInstance();
			                     DocumentBuilder builderMedcTarget=null;
			                            
			                                          builderMedcTarget=fatoryMedcTarget.newDocumentBuilder();   
			                            	          Document docMedcTarget=builderMedcTarget.parse(new InputSource(new StringReader(targetResponseEntityAfterExecuteSchedular)));
			                            	          String statusCodeOfMedc=docMedcTarget.getElementsByTagName("StatusCode").item(0).getTextContent();
			                            	          if(statusCodeOfMedc.equals("00")) {
	                	  
	                	                                  insertionHelper.updateMedcDataAfterExecutionOfSchedularIntoOracleDB(docMedcTarget,listOfBuildingPermitNo.get(i));
	                	  
	                	  
			                            	          }
	                	  
	                	  
	                  }catch (HttpStatusCodeException e) {
						logger.error(e.getLocalizedMessage());
					}
			  }catch (Exception e) {
				logger.error(e.getLocalizedMessage());
			}
			  
		  }
		  
		  Map<String,String> mapOfParentAndRequestId=insertionHelper.fetchDataFromRequestWhereMMSERVTYPEIsWorkCompletionCertificate();
		  for(Map.Entry<String, String> entrySet:mapOfParentAndRequestId.entrySet()) {
			  try {
				  String plotSizeAfterExecution=null;
				   String requestPayloadOfMMNew=String.format(getMMDatatargetRequestBody, entrySet.getValue());
	         		HttpHeaders MMnewHeader=createHeaders();
	         		MMnewHeader.set("SOAPAction", getMMdataSoapAction);
	         		MMnewHeader.set("Content-Type","text/xml;charset=UTF-8");
	         		
	         		RestTemplate restTemplateToCallMMNew=getRestTemplate();
	         		HttpEntity mrReadingEntityforMMNew=new HttpEntity(requestPayloadOfMMNew, MMnewHeader);
	         		ResponseEntity<String> targetResponseEntityForMMNew=null;
	         		
	         		logger.info("calling MM-New Target after Execution Of Schedular");
	         		targetResponseEntityForMMNew = restTemplateToCallMMNew.exchange(getMMDatatargetURL,HttpMethod.POST, mrReadingEntityforMMNew, String.class);
	         		String ResponsePayloadForMMNew=targetResponseEntityForMMNew.getBody();
	         		 if(ResponsePayloadForMMNew.contains("&gt;")) {
	         			ResponsePayloadForMMNew=ResponsePayloadForMMNew.replace("&gt;", ">");
			 				   }
			 				   if(ResponsePayloadForMMNew.contains("&lt;")) {
			 					  ResponsePayloadForMMNew=ResponsePayloadForMMNew.replace("&lt;", "<");
			 				   }
			 				   
			 				  if(ResponsePayloadForMMNew.contains("<![CDATA[")) {
			 					 ResponsePayloadForMMNew=ResponsePayloadForMMNew.replace("<![CDATA[", "");
							   }
							   if(ResponsePayloadForMMNew.contains("]]>")) {
								   ResponsePayloadForMMNew=ResponsePayloadForMMNew.replace("]]>", "");
							    }
							   
							   ResponsePayloadForMMNew=ResponsePayloadForMMNew.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>", "");
	         		    
							   DocumentBuilderFactory builderFactoryForMMNew=DocumentBuilderFactory.newInstance();
				                     DocumentBuilder documentBuilderForMMNew=null;
				                            
				                     documentBuilderForMMNew=builderFactoryForMMNew.newDocumentBuilder();  
				                     Document docForMMNew=documentBuilderForMMNew.parse(new InputSource(new StringReader(ResponsePayloadForMMNew)));
				                     String mmReturnStatusOfMMNewResponseAfterExecutingSchedular=docForMMNew.getElementsByTagName("MM_RETURN_STATUS").item(0).getTextContent();
				                     if(mmReturnStatusOfMMNewResponseAfterExecutingSchedular.equals("S"))
				                      plotSizeAfterExecution=docForMMNew.getElementsByTagName("PLOT_SIZE").item(0).getTextContent();
	                       	          String allowedPercentageAreaAfterExecution=docForMMNew.getElementsByTagName("ALLOWED_PERCENTAGE_AREA").item(0).getTextContent();
	                       	          
	                       	          int plotSizeIntegerValueOfMMNew=Integer.parseInt(plotSizeAfterExecution);
	                       	          int allowedPrecentageAreaIntegerValue=Integer.parseInt(allowedPercentageAreaAfterExecution);
	                       	        
	                       	         int certifiedBuildingAreaAfterExecution=(plotSizeIntegerValueOfMMNew*allowedPrecentageAreaIntegerValue)/100;
	                       	         String certifiedbuildingareaForMMNew=String.valueOf(certifiedBuildingAreaAfterExecution);
	                       	            insertionHelper.insertMMNewDataIntoRequestsAfterExecutionOfSchedular(docForMMNew,certifiedbuildingareaForMMNew,allowedPercentageAreaAfterExecution,entrySet.getKey());
				                     
				  
			  }catch (Exception e) {
				logger.error(e.getLocalizedMessage());
			}
			 
		  }
	     
		  
		}catch (Exception e) {
		e.printStackTrace();
		}
		
		
		
	}
		

		

	@PostMapping(value = "/geteddata", produces = "application/xml")
	public ResponseEntity<String> getEDData(HttpServletRequest request, HttpServletResponse response) {
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		ResponseEntity<String> responseEntity = null;
		try {

			String xml = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			HttpHeaders header = createHeaders();
			header.set("Content-Type", "text/xml;charset=UTF-8");

			RestTemplate restTemplate = getRestTemplate();
			HttpEntity mrReadingEntity = new HttpEntity(xml, header);
			auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setRequestMsg(xml);
			try {

				responseEntity = restTemplate.exchange(getEDDatatargetURL, HttpMethod.POST, mrReadingEntity,
						String.class);
				auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setTargetResponseMsg(responseEntity.getBody());
				auditLogs.setResponseMsg(responseEntity.getBody());
				auditLogs.setTargetResponseCode(responseEntity.getStatusCode().name());

			} catch (HttpStatusCodeException e) {
				e.printStackTrace();
				auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
				System.out.println("In Exception " + e.getResponseBodyAsString());
				auditLogs.setResponseMsg(e.getResponseBodyAsString());

			}

		} catch (Exception e) {

			e.printStackTrace();
			auditLogs.setTargetResponseMsg(e.getMessage());
			System.out.println("In Exception " + e.getMessage());
			auditLogs.setResponseMsg(e.getMessage());

		}

		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}

	@PostMapping(value = "/getmmdata", produces = "application/xml")
	public ResponseEntity<String> getMMdata(HttpServletRequest request, HttpServletResponse response) {
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		ResponseEntity<String> responseEntity = null;
		try {

			String xml = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			HttpHeaders header = createHeaders();
			header.set("SOAPAction", getMMdataSoapAction);
			header.set("Content-Type", "text/xml;charset=UTF-8");

			RestTemplate restTemplate = getRestTemplate();
			HttpEntity mrReadingEntity = new HttpEntity(xml, header);
			auditLogs.setTargetRequestTime(new Timestamp(System.currentTimeMillis()));
			auditLogs.setRequestMsg(xml);
			try {

				responseEntity = restTemplate.exchange(getMMDatatargetURL, HttpMethod.POST, mrReadingEntity,
						String.class);
				auditLogs.setTargetResponseTime(new Timestamp(System.currentTimeMillis()));
				auditLogs.setTargetResponseMsg(responseEntity.getBody());
				auditLogs.setResponseMsg(responseEntity.getBody());
				auditLogs.setTargetResponseCode(responseEntity.getStatusCode().name());

			} catch (HttpStatusCodeException e) {
				e.printStackTrace();
				auditLogs.setTargetResponseMsg(e.getResponseBodyAsString());
				System.out.println("In Exception " + e.getResponseBodyAsString());
				auditLogs.setResponseMsg(e.getResponseBodyAsString());

			}

		} catch (Exception e) {

			e.printStackTrace();
			auditLogs.setTargetResponseMsg(e.getMessage());
			System.out.println("In Exception " + e.getMessage());
			auditLogs.setResponseMsg(e.getMessage());

		}

		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}

	private boolean checkIfNullOrEmpty(String parameter) {
		return parameter == null || parameter.trim().length() == 0;
	}

}
