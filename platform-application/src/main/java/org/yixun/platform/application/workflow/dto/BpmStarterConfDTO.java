package org.yixun.platform.application.workflow.dto;

public class BpmStarterConfDTO {
	private Long id;
	private String procDefId;
	private Long starterId;
	private String starter;
	private String starterType;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProcDefId() {
		return procDefId;
	}
	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}
	public Long getStarterId() {
		return starterId;
	}
	public void setStarterId(Long starterId) {
		this.starterId = starterId;
	}
	public String getStarter() {
		return starter;
	}
	public void setStarter(String starter) {
		this.starter = starter;
	}
	public String getStarterType() {
		return starterType;
	}
	public void setStarterType(String starterType) {
		this.starterType = starterType;
	}
}
