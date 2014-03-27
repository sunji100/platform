package org.yixun.platform.application.security.dto;

import org.yixun.platform.application.security.util.RoleBeanUtil;
import org.yixun.platform.core.security.Role;

public class RoleDTO {
	private Long id;
	private String name;
	private int sortOrder;
	private boolean isValid;
	private String roleDesc;
	private Long orgId;
	private String orgName;
	
	public RoleDTO(){
		
	}
	
	public RoleDTO(Role role,Long orgId,String orgName){
		RoleBeanUtil.domainToDTO(this, role);
		this.setOrgId(orgId);
		this.setOrgName(orgName);
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
	public String getRoleDesc() {
		return roleDesc;
	}
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Override
	public String toString() {
		return "RoleDTO [id=" + id + ", name=" + name + ", sortOrder=" + sortOrder + ", isValid=" + isValid + ", roleDesc=" + roleDesc + ", orgId=" + orgId + ", orgName=" + orgName + "]";
	}
	
}
