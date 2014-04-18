package org.yixun.platform.core.workflow;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.yixun.platform.core.security.Identity;
import org.yixun.platform.core.security.Role;

import com.dayatang.domain.AbstractEntity;

@Entity
@Table(name="bpm_task_conf")
public class BpmConfUser extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -520348646712427457L;
	
	@Column(name="PROC_DEF_ID_")
	private String procDefId;
	
	@Column(name="TASK_DEF_KEY_")
	private String taskDefKey;
	
	@Column(name="PROC_DEF_KEY_")
	private String procDefKey;
	
	@ManyToMany
	@JoinTable(name="bpm_task_conf_role",joinColumns={@JoinColumn(name="BPM_CONF_ID")},inverseJoinColumns={@JoinColumn(name="ROLE_ID")})
	private Set<Role> roles = new HashSet<Role>();
	
	@ManyToMany
	@JoinTable(name="bpm_task_conf_user",joinColumns={@JoinColumn(name="BPM_CONF_ID")},inverseJoinColumns={@JoinColumn(name="USER_ID")})
	private Set<Identity> users = new HashSet<Identity>();

	public String getProcDefId() {
		return procDefId;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public String getProcDefKey() {
		return procDefKey;
	}

	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}

	
	
	

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Identity> getUsers() {
		return users;
	}

	public void setUsers(Set<Identity> users) {
		this.users = users;
	}

	private static List<BpmConfUser> findByNamedQuery(String queryName,Object[] params){
		return getRepository().findByNamedQuery(queryName, params, BpmConfUser.class);
	}
	
	public static List<BpmConfUser> findBpmConfUser(String procDefId,String procDefKey,String taskDefKey){
		return findByNamedQuery("findBpmConfUser",new Object[]{procDefId,procDefKey,taskDefKey});
	}
	
	public static List<BpmConfUser> findBpmConf(String procDefId,String taskDefKey){
		return findByNamedQuery("findBpmConf",new Object[]{procDefId,taskDefKey});
	}
	
	public static List<Role> findNotAssignRoleByUserTask(String procDefId, String procDefKey, String taskDefKey){
		return getRepository().findByNamedQuery("findNotAssignRoleByUserTask",new Object[]{procDefId,procDefKey,taskDefKey},Role.class);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((procDefId == null) ? 0 : procDefId.hashCode());
		result = prime * result + ((procDefKey == null) ? 0 : procDefKey.hashCode());
		result = prime * result + ((taskDefKey == null) ? 0 : taskDefKey.hashCode());
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
		BpmConfUser other = (BpmConfUser) obj;
		if (procDefId == null) {
			if (other.procDefId != null)
				return false;
		} else if (!procDefId.equals(other.procDefId))
			return false;
		if (procDefKey == null) {
			if (other.procDefKey != null)
				return false;
		} else if (!procDefKey.equals(other.procDefKey))
			return false;
		if (taskDefKey == null) {
			if (other.taskDefKey != null)
				return false;
		} else if (!taskDefKey.equals(other.taskDefKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BpmConfUser [procDefId=" + procDefId + ", taskDefKey=" + taskDefKey + ", procDefKey=" + procDefKey + "]";
	}

}
