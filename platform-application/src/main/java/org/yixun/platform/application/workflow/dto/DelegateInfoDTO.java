package org.yixun.platform.application.workflow.dto;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.openkoala.koala.springmvc.JsonDateSerializer;
import org.springframework.format.annotation.DateTimeFormat;

public class DelegateInfoDTO {
	private Long id;
	private String assignee;
	private String attorney;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endTime;
	private String procDefId;
	private String procDefName;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getAttorney() {
		return attorney;
	}
	public void setAttorney(String attorney) {
		this.attorney = attorney;
	}
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	@JsonSerialize(using = JsonDateSerializer.class)
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getProcDefId() {
		return procDefId;
	}
	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}
	public String getProcDefName() {
		return procDefName;
	}
	public void setProcDefName(String procDefName) {
		this.procDefName = procDefName;
	}
	
}
