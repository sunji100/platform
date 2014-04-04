package org.yixun.platform.core.workflow;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dayatang.domain.AbstractEntity;

@Entity
@Table(name="bpm_delegate_proc_info")
public class BpmDelegateProcInfo extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3656976250506312550L;
	
	@ManyToOne
	@JoinColumn(name="bpm_delegate_info_id")
	private BpmDelegateInfo bpmDelegateInfo;
	
	@Column(name="process_definition_id")
	private String processDefinitionId;

	public BpmDelegateInfo getBpmDelegateInfo() {
		return bpmDelegateInfo;
	}

	public void setBpmDelegateInfo(BpmDelegateInfo bpmDelegateInfo) {
		this.bpmDelegateInfo = bpmDelegateInfo;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((processDefinitionId == null) ? 0 : processDefinitionId.hashCode());
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
		BpmDelegateProcInfo other = (BpmDelegateProcInfo) obj;
		if (processDefinitionId == null) {
			if (other.processDefinitionId != null)
				return false;
		} else if (!processDefinitionId.equals(other.processDefinitionId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BpmDelegateProcInfo [processDefinitionId=" + processDefinitionId + "]";
	}

}
