package org.yixun.platform.core.security;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
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
@Table(name="ks_org")
public class Org extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8755192233082729134L;
	
	@Column(name="ABOLISH_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date abolishDate;
	
	@Column(name="CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="LEVEL")
	private int level;
	
	@Column(name="SERIAL_NUMBER")
	private String serialNumber; 
	
	@Column(name="SORT_ORDER")
	private int sortOrder;
	
	@Column(name="CREATE_OWNER")
	private String createOwner;
	
	@Column(name="ISVALID")
	private boolean isValid;
	
	@Column(name="ORG_DESC")
	private String orgDesc;
	
	@ManyToMany
	@JoinTable(name="ks_org_lineassignment",joinColumns=@JoinColumn(name="CHILD_ID"),inverseJoinColumns=@JoinColumn(name="PARENT_ID"))
	private Set<Org> parents = new HashSet<Org>();
	
	@ManyToMany(cascade=CascadeType.REMOVE)
	@JoinTable(name="ks_org_lineassignment",joinColumns=@JoinColumn(name="PARENT_ID"),inverseJoinColumns=@JoinColumn(name="CHILD_ID"))
	private Set<Org> childs = new HashSet<Org>();
	
	@ManyToMany
	@JoinTable(name="ks_org_user",joinColumns=@JoinColumn(name="ORG_ID"),inverseJoinColumns=@JoinColumn(name="IDENTITY_ID"))
	private Set<Identity> identities = new HashSet<Identity>();
	
	@ManyToMany
	@JoinTable(name="ks_role_org_auth",joinColumns=@JoinColumn(name="ORG_ID"),inverseJoinColumns=@JoinColumn(name="ROLE_ID"))
	private Set<Role> roles = new HashSet<Role>();

	public Set<Org> getParents() {
		return parents;
	}

	public void setParents(Set<Org> parents) {
		this.parents = parents;
	}

	public Set<Org> getChilds() {
		return childs;
	}

	public void setChilds(Set<Org> childs) {
		this.childs = childs;
	}

	public Set<Identity> getIdentities() {
		return identities;
	}

	public void setIdentities(Set<Identity> identities) {
		this.identities = identities;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	public String getOrgDesc() {
		return orgDesc;
	}

	public void setOrgDesc(String orgDesc) {
		this.orgDesc = orgDesc;
	}
	
	private static<T> List<T> findByNamedQuery(String queryName,Object[] params,Class<T> resultClass){
		return getRepository().findByNamedQuery(queryName, params, resultClass);
	}
	
	public static List<Org> findTopLevelOrg(){
		return findByNamedQuery("findTopLevelOrg",new Object[]{1},Org.class);
	}
	
	public static List<Org> findOrgByParentId(Long parentId){
		return findByNamedQuery("findOrgByParentId",new Object[]{parentId},Org.class);
	}
	
	public List<Identity> findNextLevelIdentity(){
		return findByNamedQuery("findNextLevelIdentity",new Object[]{new Date(),this.getId()},Identity.class);
	}
	
	public static List<Org> findOrgByIdentityId(Long userId) {
		return findByNamedQuery("findOrgByIdentityId",new Object[]{userId},Org.class);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abolishDate == null) ? 0 : abolishDate.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((createOwner == null) ? 0 : createOwner.hashCode());
		result = prime * result + (isValid ? 1231 : 1237);
		result = prime * result + level;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((orgDesc == null) ? 0 : orgDesc.hashCode());
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
		Org other = (Org) obj;
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
		if (level != other.level)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (orgDesc == null) {
			if (other.orgDesc != null)
				return false;
		} else if (!orgDesc.equals(other.orgDesc))
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
		return "Org [abolishDate=" + abolishDate + ", createDate=" + createDate + ", name=" + name + ", level=" + level + ", serialNumber=" + serialNumber + ", sortOrder=" + sortOrder
				+ ", createOwner=" + createOwner + ", isValid=" + isValid + ", orgDesc=" + orgDesc + "]";
	}

}
