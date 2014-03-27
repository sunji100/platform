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
		repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
		
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
	 * @param processDefinitionId
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
	 * 为UserTask分配角色
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
}
