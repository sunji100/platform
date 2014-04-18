package org.yixun.platform.core.workflow;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.yixun.platform.core.security.Identity;
import org.yixun.platform.core.security.Role;

import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.QuerySettings;

@Entity
@Table(name="bpm_starter_conf")
public class BpmStarterConf extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6893467001274703611L;
	
	@Column(name="PROC_DEF_ID_")
	private String procDefId;
	
	@Column(name="ROLE_ID")
	private Long roleId;
	
	@Column(name="USER_ID")
	private Long userId;
	
	public String getProcDefId() {
		return procDefId;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	private static List<BpmStarterConf> findByNamedQuery(String queryName,Object[] params){
		return getRepository().findByNamedQuery(queryName, params, BpmStarterConf.class);
	}
	
	public static List<BpmStarterConf> find(String propertyName, Object propertyValue){
		return getRepository().find(QuerySettings.create(BpmStarterConf.class).eq(propertyName, propertyValue));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((procDefId == null) ? 0 : procDefId.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		BpmStarterConf other = (BpmStarterConf) obj;
		if (procDefId == null) {
			if (other.procDefId != null)
				return false;
		} else if (!procDefId.equals(other.procDefId))
			return false;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BpmStarterConf [procDefId=" + procDefId + ", roleId=" + roleId + ", userId=" + userId + "]";
	}

}
