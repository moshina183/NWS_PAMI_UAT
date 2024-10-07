package com.vinsys.diam.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import com.vinsys.diam.esb.IConstants;
import com.vinsys.diam.model.TXNAuditLogs;

public class ErrorController {

	@RequestMapping("/error")
	public ResponseEntity<String> handleError(HttpServletRequest request){
		TXNAuditLogs auditLogs = (TXNAuditLogs) request.getAttribute(IConstants.CURRENT_AUDIT);
		return new ResponseEntity<String>(auditLogs.getResponseMsg(), HttpStatus.OK);
	}
}
