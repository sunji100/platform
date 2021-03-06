package org.yixun.platform.core.wssecurity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dayatang.domain.AbstractEntity;

@Entity
@Table(name="ws_role")
public class WsRole extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3315183712047905368L;

	
	@Column(name="ABOLISH_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date abolishDate;
	
	@Column(name="CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="SERIAL_NUMBER")
	private String serialNumber;
	
	@Column(name="SORT_ORDER")
	private int sortOrder;
	
	@Column(name="CREATE_OWNER")
	private String createOwner;
	
	@Column(name="ISVALID",columnDefinition="char(1)")
	private boolean isValid;
	
	@Column(name="ROLE_DESC")
	private String roleDesc; 
	
	@ManyToMany
	@JoinTable(name="ws_role_user_auth",joinColumns={@JoinColumn(name="ROLE_ID")},inverseJoinColumns={@JoinColumn(name="USER_ID")})
	private Set<WsUser> users = new HashSet<WsUser>();
	
	@ManyToMany
	@JoinTable(name="ws_role_resource_auth",joinColumns={@JoinColumn(name="ROLE_ID")},inverseJoinColumns={@JoinColumn(name="RESOURCE_ID")})
	private Set<WsResource> resources = new HashSet<WsResource>();
	
	public Set<WsUser> getUsers() {
		return users;
	}

	public void setUsers(Set<WsUser> users) {
		this.users = users;
	}

	public Set<WsResource> getResources() {
		return resources;
	}

	public void setResources(Set<WsResource> resources) {
		this.resources = resources;
	}

	public Date getAbolishDate() {
		return abolishDate;
	}

	public void setAbolishDate(Date abolishDate) {
		this.abolishDate = abolishDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getCreateOwner() {
		return createOwner;
	}

	public void setCreateOwner(String createOwner) {
		this.createOwner = createOwner;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abolishDate == null) ? 0 : abolishDate.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((createOwner == null) ? 0 : createOwner.hashCode());
		result = prime * result + (isValid ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((roleDesc == null) ? 0 : roleDesc.hashCode());
		result = prime * result + ((serialNumber == null) ? 0 : serialNumber.hashCode());
		result = prime * result + sortOrder;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WsRole other = (WsRole) obj;
		if (abolishDate == null) {
			if (other.abolishDate != null)
				return false;
		} else if (!abolishDate.equals(other.abolishDate))
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (createOwner == null) {
			if (other.createOwner != null)
				return false;
		} else if (!createOwner.equals(other.createOwner))
			return false;
		if (isValid != other.isValid)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (roleDesc == null) {
			if (other.roleDesc != null)
				return false;
		} else if (!roleDesc.equals(other.roleDesc))
			return false;
		if (serialNumber == null) {
			if (other.serialNumber != null)
				return false;
		} else if (!serialNumber.equals(other.serialNumber))
			return false;
		if (sortOrder != other.sortOrder)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Role [abolishDate=" + abolishDate + ", createDate=" + createDate + ", name=" + name + ", serialNumber=" + serialNumber + ", sortOrder=" + sortOrder + ", createOwner=" + createOwner
				+ ", isValid=" + isValid + ", roleDesc=" + roleDesc + "]";
	}

}
