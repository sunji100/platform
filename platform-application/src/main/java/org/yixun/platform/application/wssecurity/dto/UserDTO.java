package org.yixun.platform.application.wssecurity.dto;

import java.util.Date;
import java.util.List;

public class UserDTO {
	private Long id;
	private String name;
	private int sortOrder;
	private boolean isValid;
	private Date lastLoginTime;
	private Date lastModifyTime;
	private String userAccount;
	private String userDesc;
	private String userPassword;
	private Long orgId;
	private String orgName;
	private List<String> roles;
	
	
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public Date getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getUserDesc() {
		return userDesc;
	}
	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	@Override
	public String toString() {
		return "IdentityDTO [id=" + id + ", name=" + name + ", sortOrder=" + sortOrder + ", isValid=" + isValid + ", lastLoginTime=" + lastLoginTime + ", lastModifyTime=" + lastModifyTime
				+ ", userAccount=" + userAccount + ", userDesc=" + userDesc + ", userPassword=" + userPassword + ", orgId=" + orgId + ", orgName=" + orgName + "]";
	}
}
