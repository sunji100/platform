package org.yixun.platform.core.workflow;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dayatang.domain.AbstractEntity;

@Entity
@Table(name="bpm_form_conf")
public class BpmFormConf extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7285709548694475869L;
	
	@Column(name="PROC_DEF_ID_")
	private String procDefId;
	
	@Column(name="PROC_DEF_KEY_")
	private String procDefKey;
	
	@Column(name="TASK_DEF_KEY_")
	private String taskDefKey;
	
	@Column(name="FORM_URL")
	private String formUrl;

	public String getProcDefId() {
		return procDefId;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	public String getProcDefKey() {
		return procDefKey;
	}

	public void setProcDefKey(String procDefKey) {
		this.procDefKey = procDefKey;
	}

	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public String getFormUrl() {
		return formUrl;
	}

	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}
	
	private static List<BpmFormConf> findByNamedQuery(String queryName,Object[] params){
		return getRepository().findByNamedQuery(queryName, params, BpmFormConf.class);
	}
	
	public static List<BpmFormConf> findBpmFormConfByPdId(String procDefId){
		return findByNamedQuery("findBpmFormConfByPdId",new Object[]{procDefId});
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((formUrl == null) ? 0 : formUrl.hashCode());
		result = prime * result + ((procDefId == null) ? 0 : procDefId.hashCode());
		result = prime * result + ((procDefKey == null) ? 0 : procDefKey.hashCode());
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
		BpmFormConf other = (BpmFormConf) obj;
		if (formUrl == null) {
			if (other.formUrl != null)
				return false;
		} else if (!formUrl.equals(other.formUrl))
			return false;
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
		return true;
	}

	@Override
	public String toString() {
		return "BpmFormConf [procDefId=" + procDefId + ", procDefKey=" + procDefKey + ", formUrl=" + formUrl + "]";
	}

}
