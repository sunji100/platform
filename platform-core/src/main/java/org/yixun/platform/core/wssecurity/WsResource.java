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

@Entity(name="WsResource")
@Table(name = "ws_resource")
public class WsResource extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4596687893069233928L;
	
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
	
	@Column(name="ISVALID")
	private boolean isValid;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="IDENTIFIER")
	private String identifier;
	
	@Column(name="LEVEL")
	private String level;
	
	@Column(name="MENU_ICON")
	private String menuIcon;
	
	@ManyToMany
	@JoinTable(name="ws_role_resource_auth",joinColumns={@JoinColumn(name="RESOURCE_ID")},inverseJoinColumns={@JoinColumn(name="ROLE_ID")})
	private Set<WsRole> roles = new HashSet<WsRole>();
	
	public Set<WsRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<WsRole> roles) {
		this.roles = roles;
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

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abolishDate == null) ? 0 : abolishDate.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + (isValid ? 1231 : 1237);
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((menuIcon == null) ? 0 : menuIcon.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		WsResource other = (WsResource) obj;
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
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (isValid != other.isValid)
			return false;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (menuIcon == null) {
			if (other.menuIcon != null)
				return false;
		} else if (!menuIcon.equals(other.menuIcon))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
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
		return "Resource [abolishDate=" + abolishDate + ", createDate=" + createDate + ", name=" + name + ", serialNumber=" + serialNumber + ", sortOrder=" + sortOrder + ", isValid=" + isValid
				+ ", description=" + description + ", identifier=" + identifier + ", level=" + level + ", menuIcon=" + menuIcon + "]";
	}

}
