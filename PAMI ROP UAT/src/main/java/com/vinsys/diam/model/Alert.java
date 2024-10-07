package com.vinsys.diam.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Alert {

	@JsonProperty("service-point")
	private String servicePoint;
	@JsonProperty("badge-number")
	private String badgeNumber;
	@JsonProperty("alert-type")
	private String alertType;
	@JsonProperty("alert-desc")
	private String alertDesc;
	@JsonProperty("message-date")
	private String msgDate;
	@JsonProperty("message-text")
	private String msgText;
	@JsonProperty("batch-id")
	private String batchId;

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getServicePoint() {
		return servicePoint;
	}

	public void setServicePoint(String servicePoint) {
		this.servicePoint = servicePoint;
	}

	public String getBadgeNumber() {
		return badgeNumber;
	}

	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	public String getAlertDesc() {
		return alertDesc;
	}

	public void setAlertDesc(String alertDesc) {
		this.alertDesc = alertDesc;
	}

	public String getMsgDate() {
		return msgDate;
	}

	public void setMsgDate(String msgDate) {
		this.msgDate = msgDate;
	}

	public String getMsgText() {
		return msgText;
	}

	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}

}
