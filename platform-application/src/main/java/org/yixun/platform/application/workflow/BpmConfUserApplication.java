package org.yixun.platform.application.workflow;

import java.util.List;

import org.yixun.platform.application.workflow.dto.BpmConfUserDTO;

public interface BpmConfUserApplication {
	public List<BpmConfUserDTO> findBpmConfUser(String procDefId,String procDefKey,String taskDefKey) throws Exception;
}
