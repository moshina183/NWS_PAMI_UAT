package com.vinsys.diam.esb;

import java.util.Map;

public class Request {

	private Map<String, String> headers;
	private String payload;
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}


	
}
