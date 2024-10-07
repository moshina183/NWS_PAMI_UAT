package com.vinsys.diam.filters;

import java.util.Iterator;
import java.util.List;

import com.vinsys.diam.model.Alert;

import exception.AlarmException;

public class ValidationFilter {

	public void ValidateAMRAlarms(List<Alert> amrAlarms) {
		System.out.println("Inside validation");
		Iterator<Alert> amrAlarmIterator = amrAlarms.iterator();
		while (amrAlarmIterator.hasNext()) {
			Alert alert = (Alert) amrAlarmIterator.next();
			if (alert.getBatchId() == null || alert.getBatchId().trim().isEmpty()) {
				throw new AlarmException("Invalid or Empty BatchId");
			}
			if (alert.getAlertDesc() == null || alert.getAlertDesc().trim().isEmpty()) {
				throw new AlarmException("Invalid Alert Description");
			}
			if (alert.getAlertType() == null || alert.getAlertType().trim().isEmpty()) {
				throw new AlarmException("Invalid Alert Type");
			}
			if (alert.getBadgeNumber() == null || alert.getBadgeNumber().trim().isEmpty()) {
				throw new AlarmException("Invalid Badge number");
			}
			if (alert.getServicePoint() == null || alert.getServicePoint().trim().isEmpty()) {
				throw new AlarmException("Invalid Service Point");
			}
			if (alert.getMsgText() == null || alert.getMsgText().trim().isEmpty()) {
				throw new AlarmException("Invalid Message Text");
			}
			if (alert.getMsgDate() == null || alert.getMsgDate().trim().isEmpty()) {
				throw new AlarmException("Invalid Message Date");
			}
		}
	}
}
