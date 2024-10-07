package com.vinsys.diam.handler;

import java.io.IOException;
import java.net.ConnectException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.ResourceAccessException;

import com.vinsys.diam.esb.IConstants;
import com.vinsys.diam.esb.Request;
import com.vinsys.diam.model.TXNAuditLogs;
import com.vinsys.diam.processors.AuditLogProcessor;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public void handleNotFoundError(HttpServletRequest req, HttpServletResponse response, Exception e)
			throws IOException {
		e.printStackTrace();
		String s = req.getRequestURI();
		TXNAuditLogs auditLogs = (TXNAuditLogs) req.getAttribute(IConstants.CURRENT_AUDIT);
		
		if(s.equals("api/v1")) {
			response.setHeader("Content-Type", "application/json");
			if (e instanceof ResourceAccessException) {

				auditLogs.setResponseMsg("{\"errorCode\":7,\"errorMessage\":\"" + e.getMessage() + "\"}");
				req.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
				response.getOutputStream().print("{\"errorCode\":7,\"errorMessage\":\"" + e.getMessage() + "\"}");
			}
			if (e instanceof ConnectException) {

				auditLogs.setResponseMsg("{\"errorCode\":7,\"errorMessage\":\"" + e.getMessage() + "\"}");
				req.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
				response.getOutputStream().print("{\"errorCode\":7,\"errorMessage\":\"" + e.getMessage() + "\"}");
			}
			if (e instanceof NullPointerException) {
				String message = e.getMessage() == null ? "Got null value from Database" : e.getMessage();
				auditLogs.setResponseMsg("{\"errorCode\":7,\"errorMessage\":\"" + message + "\"}");
				req.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
				response.getOutputStream().print("{\"errorCode\":7,\"errorMessage\":\"" + e.getMessage() + "\"}");
			} else {
				auditLogs.setResponseMsg("{\"errorCode\":7,\"errorMessage\":\"" + e.getLocalizedMessage() + "\"}");
				req.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
				response.getOutputStream().print("{\"errorCode\":7,\"errorMessage\":\"" + e.getMessage() + "\"}");
			}
		}
		
		if(s.equals("api/v2")) {
			response.setHeader("Content-Type", "application/json");
			if (e instanceof ResourceAccessException) {

				auditLogs.setResponseMsg("{\r\n" + 
						"\"responseHeader\": {\r\n" + 
						"\"esbStatus\": \"SUCCESS\",\r\n" + 
						"\"targetStatus\": \" SUCCESS\",\r\n" + 
						"\"esbResponseCode\": \"200\"\r\n" + 
						"},\r\n" + 
						"\"responseBody\":"
						+ "{\"errorCode\":7,\"errorMessage\":\"" + e.getMessage() + "\"}" + 
						"}");
				req.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
				response.getOutputStream().print("{\r\n" + 
						"\"responseHeader\": {\r\n" + 
						"\"esbStatus\": \"SUCCESS\",\r\n" + 
						"\"targetStatus\": \" SUCCESS\",\r\n" + 
						"\"esbResponseCode\": \"200\"\r\n" + 
						"},\r\n" + 
						"\"responseBody\":"
						+ "{\"errorCode\":7,\"errorMessage\":\"" + e.getMessage() + "\"}" + 
						"}");
			}
			if (e instanceof ConnectException) {

				auditLogs.setResponseMsg("{\r\n" + 
						"\"responseHeader\": {\r\n" + 
						"\"esbStatus\": \"SUCCESS\",\r\n" + 
						"\"targetStatus\": \" SUCCESS\",\r\n" + 
						"\"esbResponseCode\": \"200\"\r\n" + 
						"},\r\n" + 
						"\"responseBody\": "
						+ "{\"errorCode\":7,\"errorMessage\":\"" + e.getMessage() + "\"}" + 
						"}");
				req.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
				response.getOutputStream().print("{\r\n" + 
						"\"responseHeader\": {\r\n" + 
						"\"esbStatus\": \"SUCCESS\",\r\n" + 
						"\"targetStatus\": \" SUCCESS\",\r\n" + 
						"\"esbResponseCode\": \"200\"\r\n" + 
						"},\r\n" + 
						"\"responseBody\":"
						+ "{\"errorCode\":7,\"errorMessage\":\"" + e.getMessage() + "\"}" + 
						"}");
			}
			if (e instanceof NullPointerException) {
				String message = e.getMessage() == null ? "Got null value from Database" : e.getMessage();
				auditLogs.setResponseMsg("{\r\n" + 
						"\"responseHeader\": {\r\n" + 
						"\"esbStatus\": \"SUCCESS\",\r\n" + 
						"\"targetStatus\": \" SUCCESS\",\r\n" + 
						"\"esbResponseCode\": \"200\"\r\n" + 
						"},\r\n" + 
						"\"responseBody\":"
						+ "{\"errorCode\":7,\"errorMessage\":\"" + e.getMessage() + "\"}" + 
						"}");
				req.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
				response.getOutputStream().print("{\r\n" + 
						"\"responseHeader\": {\r\n" + 
						"\"esbStatus\": \"SUCCESS\",\r\n" + 
						"\"targetStatus\": \" SUCCESS\",\r\n" + 
						"\"esbResponseCode\": \"200\"\r\n" + 
						"},\r\n" + 
						"\"responseBody\": "
						+ "{\"errorCode\":7,\"errorMessage\":\"" + e.getMessage() + "\"}" + 
						"}");
			} else {
				auditLogs.setResponseMsg("{\r\n" + 
						"\"responseHeader\": {\r\n" + 
						"\"esbStatus\": \"SUCCESS\",\r\n" + 
						"\"targetStatus\": \" SUCCESS\",\r\n" + 
						"\"esbResponseCode\": \"200\"\r\n" + 
						"},\r\n" + 
						"\"responseBody\":"
						+ "{\"errorCode\":7,\"errorMessage\":\"" + e.getMessage() + "\"}" + 
						"}");
				req.setAttribute(IConstants.CURRENT_AUDIT, auditLogs);
				response.getOutputStream().print("{\r\n" + 
						"\"responseHeader\": {\r\n" + 
						"\"esbStatus\": \"SUCCESS\",\r\n" + 
						"\"targetStatus\": \" SUCCESS\",\r\n" + 
						"\"esbResponseCode\": \"200\"\r\n" + 
						"},\r\n" + 
						"\"responseBody\":"
						+ "{\"errorCode\":7,\"errorMessage\":\"" + e.getMessage() + "\"}" + 
						"}");
			}
		}
	}

}
