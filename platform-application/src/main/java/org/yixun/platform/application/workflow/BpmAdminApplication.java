package org.yixun.platform.application.workflow;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.yixun.platform.application.security.dto.IdentityDTO;
import org.yixun.platform.application.security.dto.RoleDTO;
import org.yixun.platform.application.workflow.dto.BpmConfUserDTO;
import org.yixun.platform.application.workflow.dto.BpmStarterConfDTO;

import com.dayatang.querychannel.support.Page;

public interface BpmAdminApplication {
	/**
	 * 显示流程定义中的userTask列表
	 * @param processDefinitionId 流程定义ID
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findTaskDefinitionsByPdId(String processDefinitionId) throws Exception;
	/**
	 * 显示流程定义列表
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> listProcessDefinitions() throws Exception;
	/**
	 * 删除流程定义
	 * @param deploymentId
	 * @return
	 * @throws Exception
	 */
	public void removeProcessDeployment(String deploymentId) throws Exception;
	/**
	 * 暂停流程定义
	 * @param processDefinitionId
	 * @return
	 * @throws Exception
	 */
	public void suspendProcessDefinition(String processDefinitionId) throws Exception;
	/**
	 * 恢复流程定义
	 * @param processDefinitionId
	 * @return
	 * @throws Exception
	 */
	public void activeProcessDefinition(String processDefinitionId) throws Exception;
	public InputStream graphProcessDefinition(String processDefinitionId) throws Exception;
	/**
	 * 查看usertask的办理人或办理人角色
	 * @param procDefId
	 * @param procDefKey
	 * @param taskDefKey
	 * @return
	 * @throws Exception
	 */
	public List<BpmConfUserDTO> findUserTaskAssign(String procDefId, String procDefKey, String taskDefKey) throws Exception;
	/**
	 * 查看可以分配到userTask的角色
	 * @param procDefId
	 * @param procDefKey
	 * @param taskDefKey
	 * @return
	 * @throws Exception
	 */
	public Page<RoleDTO> findNotAssignRoleByUserTask(BpmConfUserDTO queryDTO,int page,int pagesize) throws Exception;
	/**
	 * 查看可以分配到UserTask的用户
	 * @param queryDTO
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	public Page<IdentityDTO> findNotAssignUserByUserTask(BpmConfUserDTO queryDTO,int page,int pagesize) throws Exception;
	/**
	 *  为UserTask分配办理角色
	 * @param bpmConfDTO
	 * @param roleIds
	 * @throws Exception
	 */
	public void assignRoleToUserTask(BpmConfUserDTO bpmConfDTO,Long[] roleIds) throws Exception;
	/**
	 * 为UserTask分配用户
	 * @param bpmConfDTO
	 * @param userIds
	 * @throws Exception
	 */
	public void assignUserToUserTask(BpmConfUserDTO bpmConfDTO,Long[] userIds) throws Exception;
	/**
	 * 删除UserTask分配的执行人/执行角色
	 * @param bpmConfUserDTOs
	 * @return
	 * @throws Exception
	 */
	public void removeAssignForUserTask(BpmConfUserDTO[] bpmConfDTOs) throws Exception;
	/**
	 * 查看所有运行中的任务
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> listTasks() throws Exception;
	/**
	 * 查看所有运行中的流程实例
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> listProcessInstances() throws Exception;
	/**
	 * 暂停流程实例
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	public void suspendProcessInstanceId(String processInstanceId) throws Exception;
	/**
	 * 激活流程实例
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	public void activeProcessInstanceId(String processInstanceId) throws Exception;
	/**
	 * 删除流程实例
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	public void removeProcessInstanceId(String processInstanceId) throws Exception;
	/**
	 * 获得流程实例的已有授权人或角色
	 * @param procDefId
	 * @return
	 * @throws Exception
	 */
	public List<BpmStarterConfDTO> findStarterByProcDefId(String procDefId) throws Exception;
	/**
	 * 查询未被分配到流程定义上的角色
	 * @param procDefId
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	public Page<RoleDTO> findNotAssignRoleByProcDef(String procDefId,int page,int pagesize) throws Exception;
	public Page<IdentityDTO> findNotAssignUserByProcDef(String procDefId,int page,int pagesize) throws Exception;
	/**
	 * 为流程定义分配可执行角色
	 * @param procDefId
	 * @param roleIds
	 * @throws Exception
	 */
	public void assignRoleToProcDef(String procDefId,Long[] roleIds) throws Exception;
	/**
	 * 为流程定义分配可执行用户
	 * @param procDefId
	 * @param userIds
	 * @throws Exception
	 */
	public void assignUserToProcDef(String procDefId,Long[] userIds) throws Exception;
	/**
	 * 删除流程定义可执行用户或角色
	 * @param ids
	 * @throws Exception
	 */
	public void removeAssignForProcDef(Long[] ids) throws Exception;
	
}
