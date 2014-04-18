package org.yixun.platform.web.workflow;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.security.dto.IdentityDTO;
import org.yixun.platform.application.security.dto.RoleDTO;
import org.yixun.platform.application.workflow.BpmAdminApplication;
import org.yixun.platform.application.workflow.dto.BpmConfUserDTO;
import org.yixun.platform.application.workflow.dto.BpmStarterConfDTO;
import org.yixun.platform.workflow.cmd.ProcessDefinitionDiagramCmd;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/bpmAdmin")
public class BpmAdminController {
	@Inject
	private RepositoryService repositoryService;
	@Inject
	private ManagementService managementService;
	@Inject
	private BpmAdminApplication bpmAdminApplication;
	
	/**
	 * 显示流程定义列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/listProcessDefinitions")
	public Map<String, Object> listProcessDefinitions() throws Exception {
		List<Map<String, Object>> resultList = bpmAdminApplication.listProcessDefinitions();
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", resultList);
		
		return result;
	}
	
	/**
	 * 暂停流程定义
	 * @param processDefinitionId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/suspendProcessDefinition")
	public Map<String, Object> suspendProcessDefinition(String processDefinitionId) throws Exception {
		bpmAdminApplication.suspendProcessDefinition(processDefinitionId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	/**
	 * 恢复流程定义
	 * @param processDefinitionId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/activeProcessDefinition")
	public Map<String, Object> activeProcessDefinition(String processDefinitionId) throws Exception {
		bpmAdminApplication.activeProcessDefinition(processDefinitionId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	/**
	 * 显示流程定义图形
	 * @param processDefinitionId
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/graphProcessDefinition")
	public void graphProcessDefinition(String processDefinitionId,HttpServletResponse response) throws Exception {
		InputStream is = bpmAdminApplication.graphProcessDefinition(processDefinitionId);
        
		response.setContentType("image/png");
        IOUtils.copy(is, response.getOutputStream());
	}
	
	/**
	 * 显示流程定义中的userTask列表
	 * @param processDefinitionId 流程定义ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/listProcessDefinitionUserTasks")
	public Map<String, Object> listProcessDefinitionUserTasks(String processDefinitionId) throws Exception {
		List<Map<String, Object>> resultList = bpmAdminApplication.findTaskDefinitionsByPdId(processDefinitionId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", resultList);
		
		return result;
	}
	
	/**
	 * 查看usertask的办理人或办理人角色
	 * @param procDefId
	 * @param procDefKey
	 * @param taskDefKey
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findUserTaskAssign")
	public Map<String, Object> findUserTaskAssign(String procDefId, String procDefKey, String taskDefKey) throws Exception {
		List<BpmConfUserDTO> bpmConfUserDTOs = bpmAdminApplication.findUserTaskAssign(procDefId, procDefKey, taskDefKey);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", bpmConfUserDTOs);
		
		return result;
	}
	
	/**
	 * 查看可以分配到userTask的角色
	 * @param procDefId
	 * @param procDefKey
	 * @param taskDefKey
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findNotAssignRoleByUserTask")
	public Map<String, Object> findNotAssignRoleByUserTask(BpmConfUserDTO queryDTO,int page,int pagesize) throws Exception {
		Page<RoleDTO> pages = bpmAdminApplication.findNotAssignRoleByUserTask(queryDTO,page,pagesize);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", pages.getResult());
		result.put("Total", pages.getTotalCount());
		
		return result;
	}
	
	/**
	 * 为UserTask分配办理角色
	 * @param bpmConfDTO
	 * @param roleIds
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/assignRoleToUserTask")
	public Map<String, Object> assignRoleToUserTask(BpmConfUserDTO bpmConfDTO,String roleIds) throws Exception {
		String[] value = roleIds.split(",");
		Long[] idArrs = new Long[value.length];
		for(int i=0;i<value.length;i++){
			idArrs[i] = Long.parseLong(value[i]);
		}
		
		bpmAdminApplication.assignRoleToUserTask(bpmConfDTO, idArrs);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	/**
	 * 查看可以分配到UserTask的用户
	 * @param queryDTO
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findNotAssignUserByUserTask")
	public Map<String, Object> findNotAssignUserByUserTask(BpmConfUserDTO queryDTO,int page,int pagesize) throws Exception {
		Page<IdentityDTO> pages = bpmAdminApplication.findNotAssignUserByUserTask(queryDTO, page, pagesize);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", pages.getResult());
		result.put("Total", pages.getTotalCount());
		
		return result;
	}
	
	/**
	 * 为UserTask分配用户
	 * @param bpmConfDTO
	 * @param userIds
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/assignUserToUserTask")
	public Map<String, Object> assignUserToUserTask(BpmConfUserDTO bpmConfDTO,String userIds) throws Exception {
		String[] value = userIds.split(",");
		Long[] idArrs = new Long[value.length];
		for(int i=0;i<value.length;i++){
			idArrs[i] = Long.parseLong(value[i]);
		}
		
		bpmAdminApplication.assignUserToUserTask(bpmConfDTO, idArrs);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	/**
	 * 删除UserTask分配的执行人/执行角色
	 * @param bpmConfUserDTOs
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/removeAssignForUserTask")
	public Map<String, Object> removeAssignForUserTask(@RequestBody BpmConfUserDTO[] bpmConfUserDTOs) throws Exception {
		
		bpmAdminApplication.removeAssignForUserTask(bpmConfUserDTOs);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	/**
	 * 查看所有运行中的任务
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/listTasks")
	public Map<String, Object> listTasks() throws Exception {
		
		List<Map<String, Object>> resultList = bpmAdminApplication.listTasks();
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", resultList);
		
		return result;
	}
	
	/**
	 * 查看所有运行中的流程实例
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/listProcessInstances")
	public Map<String, Object> listProcessInstances() throws Exception {
		
		List<Map<String, Object>> resultList = bpmAdminApplication.listProcessInstances();
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", resultList);
		
		return result;
	}
	
	/**
	 * 暂停流程实例
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/suspendProcessInstanceId")
	public Map<String, Object> suspendProcessInstanceId(String processInstanceId) throws Exception {
		
		bpmAdminApplication.suspendProcessInstanceId(processInstanceId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		
		return result;
	}
	
	/**
	 * 激活流程实例
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/activeProcessInstanceId")
	public Map<String, Object> activeProcessInstanceId(String processInstanceId) throws Exception {
		
		bpmAdminApplication.activeProcessInstanceId(processInstanceId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		
		return result;
	}
	
	/**
	 * 删除流程实例
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/removeProcessInstanceId")
	public Map<String, Object> removeProcessInstanceId(String processInstanceId) throws Exception {
		
		bpmAdminApplication.removeProcessInstanceId(processInstanceId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		
		return result;
	}
	
	/**
	 * 删除流程定义
	 * @param deploymentId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/removeProcessDeployment")
	public Map<String, Object> removeProcessDeployment(String deploymentId) throws Exception {
		bpmAdminApplication.removeProcessDeployment(deploymentId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		
		return result;
	}
	
	/**
	 * 获得流程实例的已有授权人或角色
	 * @param procDefId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findStarterByProcDefId")
	public Map<String, Object> findStarterByProcDefId(String procDefId) throws Exception {
		List<BpmStarterConfDTO> bpmStarterConfDTOs = bpmAdminApplication.findStarterByProcDefId(procDefId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", bpmStarterConfDTOs);
		
		return result;
	}
	
	/**
	 * 查询未被分配到流程定义上的角色
	 * @param procDefId
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findNotAssignRoleByProcDef")
	public Map<String, Object> findNotAssignRoleByProcDef(String procDefId,int page,int pagesize) throws Exception {
		Page<RoleDTO> pages = bpmAdminApplication.findNotAssignRoleByProcDef(procDefId,page,pagesize);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", pages.getResult());
		result.put("Total", pages.getTotalCount());
		
		return result;
	}
	
	/**
	 * 查询未被分配到流程定义上的用户
	 * @param procDefId
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findNotAssignUserByProcDef")
	public Map<String, Object> findNotAssignUserByProcDef(String procDefId,int page,int pagesize) throws Exception {
		Page<IdentityDTO> pages = bpmAdminApplication.findNotAssignUserByProcDef(procDefId, page, pagesize);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", pages.getResult());
		result.put("Total", pages.getTotalCount());
		
		return result;
	}
	
	/**
	 * 为流程定义分配可执行角色
	 * @param procDefId
	 * @param roleIds
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/assignRoleToProcDef")
	public Map<String, Object> assignRoleToProcDef(String procDefId,Long[] roleIds) throws Exception {
		bpmAdminApplication.assignRoleToProcDef(procDefId, roleIds);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		
		return result;
	}
	
	/**
	 * 为流程定义分配可执行用户
	 * @param procDefId
	 * @param userIds
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/assignUserToProcDef")
	public Map<String, Object> assignUserToProcDef(String procDefId,Long[] userIds) throws Exception {
		bpmAdminApplication.assignUserToProcDef(procDefId, userIds);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		
		return result;
	}
	
	/**
	 * 删除流程定义可执行用户或角色
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/removeAssignForProcDef")
	public Map<String, Object> removeAssignForProcDef(Long[] ids) throws Exception {
		bpmAdminApplication.removeAssignForProcDef(ids);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		
		return result;
	}
}
