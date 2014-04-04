package org.yixun.platform.application.workflow.util;

import org.yixun.platform.application.workflow.dto.DelegateInfoDTO;
import org.yixun.platform.core.workflow.BpmDelegateInfo;

public class BpmDelegateInfoBeanUtil {
	public static void domainToDTO(DelegateInfoDTO dest,BpmDelegateInfo orig){
		dest.setId(orig.getId());
		dest.setAssignee(orig.getAssignee());
		dest.setAttorney(orig.getAttorney());
		dest.setStartTime(orig.getStartTime());
		dest.setEndTime(orig.getEndTime());
		dest.setProcDefId(orig.getProcDefId());
	}
	
	public static void dtoToDomain(BpmDelegateInfo dest,DelegateInfoDTO orig){
		dest.setId(orig.getId());
		dest.setAssignee(orig.getAssignee());
		dest.setAttorney(orig.getAttorney());
		dest.setStartTime(orig.getStartTime());
		dest.setEndTime(orig.getEndTime());
		dest.setProcDefId(orig.getProcDefId());
	}
}
