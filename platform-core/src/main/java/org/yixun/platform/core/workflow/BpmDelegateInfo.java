package org.yixun.platform.core.workflow;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.QuerySettings;

@Entity
@Table(name="bpm_delegate_info")
public class BpmDelegateInfo extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7675248872511544145L;
	@Column(name="assignee")
	private String assignee;
	
	@Column(name="attorney")
	private String attorney;
	
	@Column(name="start_time")
	private Date startTime;
	
	@Column(name="end_time")
	private Date endTime;
	
	@Column(name="proc_def_id")
	private String procDefId;
	
	@Column(name="status",columnDefinition="char(1)")
	private boolean status;
	

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getAttorney() {
		return attorney;
	}

	public void setAttorney(String attorney) {
		this.attorney = attorney;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	

	public String getProcDefId() {
		return procDefId;
	}

	public void setProcDefId(String procDefId) {
		this.procDefId = procDefId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	

	public static List<BpmDelegateInfo> findBy(String propertyName,Object propertyValue){
		return getRepository().find(QuerySettings.create(BpmDelegateInfo.class).eq(propertyName, propertyValue));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assignee == null) ? 0 : assignee.hashCode());
		result = prime * result + ((attorney == null) ? 0 : attorney.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((procDefId == null) ? 0 : procDefId.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + (status ? 1231 : 1237);
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
		BpmDelegateInfo other = (BpmDelegateInfo) obj;
		if (assignee == null) {
			if (other.assignee != null)
				return false;
		} else if (!assignee.equals(other.assignee))
			return false;
		if (attorney == null) {
			if (other.attorney != null)
				return false;
		} else if (!attorney.equals(other.attorney))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (procDefId == null) {
			if (other.procDefId != null)
				return false;
		} else if (!procDefId.equals(other.procDefId))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BpmDelegateInfo [assignee=" + assignee + ", attorney=" + attorney + ", startTime=" + startTime + ", endTime=" + endTime + ", procDefId=" + procDefId + ", status=" + status + "]";
	}

}
