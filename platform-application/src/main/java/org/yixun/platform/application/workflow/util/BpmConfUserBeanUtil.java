package org.yixun.platform.application.workflow.util;

import org.yixun.platform.application.workflow.dto.BpmConfUserDTO;
import org.yixun.platform.core.workflow.BpmConfUser;

public class BpmConfUserBeanUtil {
	public static void domainToDTO(BpmConfUserDTO dest,BpmConfUser orig){
		dest.setId(orig.getId());
		dest.setProcDefId(orig.getProcDefId());
		dest.setProcDefKey(orig.getProcDefId());
		dest.setTaskDefKey(orig.getTaskDefKey());
	}
	
	public static void dtoToDomain(BpmConfUser dest,BpmConfUserDTO orig){
		dest.setProcDefId(orig.getProcDefId());
		dest.setProcDefKey(orig.getProcDefKey());
		dest.setTaskDefKey(orig.getTaskDefKey());
	}
}
