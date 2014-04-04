package org.yixun.platform.application.workflow;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.yixun.platform.application.security.dto.IdentityDTO;
import org.yixun.platform.application.security.dto.RoleDTO;
import org.yixun.platform.application.workflow.dto.BpmConfUserDTO;

import com.dayatang.querychannel.support.Page;

public interface BpmAdminApplication {
	public List<Map<String, Object>> findTaskDefinitionsByPdId(String processDefinitionId) throws Exception;
	public List<Map<String, Object>> listProcessDefinitions() throws Exception;
	public void suspendProcessDefinition(String processDefinitionId) throws Exception;
	public void activeProcessDefinition(String processDefinitionId) throws Exception;
	public InputStream graphProcessDefinition(String processDefinitionId) throws Exception;
	public List<BpmConfUserDTO> findUserTaskAssign(String procDefId, String procDefKey, String taskDefKey) throws Exception;
	public Page<RoleDTO> findNotAssignRoleByUserTask(BpmConfUserDTO queryDTO,int page,int pagesize) throws Exception;
	public Page<IdentityDTO> findNotAssignUserByUserTask(BpmConfUserDTO queryDTO,int page,int pagesize) throws Exception;
	public void assignRoleToUserTask(BpmConfUserDTO bpmConfDTO,Long[] roleIds) throws Exception;
	public void assignUserToUserTask(BpmConfUserDTO bpmConfDTO,Long[] userIds) throws Exception;
	public void removeAssignForUserTask(BpmConfUserDTO[] bpmConfDTOs) throws Exception;
	public List<Map<String, Object>> listTasks() throws Exception;
	public List<Map<String, Object>> listProcessInstances() throws Exception;
}
