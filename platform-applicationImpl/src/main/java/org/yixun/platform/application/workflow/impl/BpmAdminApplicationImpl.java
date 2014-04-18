package org.yixun.platform.application.workflow.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.security.dto.IdentityDTO;
import org.yixun.platform.application.security.dto.RoleDTO;
import org.yixun.platform.application.security.util.IdentityBeanUtil;
import org.yixun.platform.application.security.util.RoleBeanUtil;
import org.yixun.platform.application.workflow.BpmAdminApplication;
import org.yixun.platform.application.workflow.dto.BpmConfUserDTO;
import org.yixun.platform.application.workflow.dto.BpmStarterConfDTO;
import org.yixun.platform.application.workflow.util.BpmConfUserBeanUtil;
import org.yixun.platform.core.security.Identity;
import org.yixun.platform.core.security.Role;
import org.yixun.platform.core.workflow.BpmConfUser;
import org.yixun.platform.core.workflow.BpmStarterConf;
import org.yixun.platform.workflow.cmd.ProcessDefinitionDiagramCmd;
import org.yixun.platform.workflow.listener.ConfUserTaskListener;

import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

@Named
@Transactional
public class BpmAdminApplicationImpl implements BpmAdminApplication {
	private static Logger logger = LoggerFactory
            .getLogger(BpmAdminApplicationImpl.class);
	
	@Inject
	private RepositoryService repositoryService;
	@Inject
	private ManagementService managementService;
	@Inject
	private TaskService taskService;
	@Inject
	private HistoryService historyService;
	@Inject
	private RuntimeService runtimeService;
	@Inject
	private QueryChannelService queryChannelService;
	
	/**
	 * 显示流程定义中的userTask列表
	 */
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<Map<String, Object>> findTaskDefinitionsByPdId(String processDefinitionId) throws Exception {
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(processDefinitionId);
		List<ActivityImpl> activities = processDefinitionEntity.getActivities();
		
		ActivityImpl startActivity = processDefinitionEntity.getInitial();
		if (startActivity.getOutgoingTransitions().size() != 1) {
			throw new IllegalStateException("start activity outgoing transitions cannot more than 1, now is : " + startActivity.getOutgoingTransitions().size());
		}

		PvmTransition pvmTransition = startActivity.getOutgoingTransitions().get(0);
		PvmActivity targetActivity = pvmTransition.getDestination();

		String firstTaskActivityId = "";
		if (!"userTask".equals(targetActivity.getProperty("type"))) {
			logger.debug("first activity is not userTask, just skip");
		} else {
			firstTaskActivityId = targetActivity.getId();
		}
		
		List<Map<String, Object>> userTaskList = new ArrayList<Map<String,Object>>();
		for (ActivityImpl activityImpl : activities) {
			String type = activityImpl.getProperties().get("type").toString();
			if("userTask".equals(type) && !firstTaskActivityId.equals(activityImpl.getId())){
				Map<String, Object> userTaskInfo = new HashMap<String, Object>();
				String procDefId = activityImpl.getProcessDefinition().getId();
				userTaskInfo.put("procDefId", procDefId);
				String procDefKey = activityImpl.getProcessDefinition().getKey();
				userTaskInfo.put("procDefKey", procDefKey);
				String procName = activityImpl.getProcessDefinition().getName();
				userTaskInfo.put("procName", procName);
				TaskDefinition taskDefinition = ((UserTaskActivityBehavior)activityImpl.getActivityBehavior()).getTaskDefinition();
				String taskDefKey = taskDefinition.getKey();
				userTaskInfo.put("taskDefKey", taskDefKey);
				String taskName = String.valueOf(taskDefinition.getNameExpression());
				userTaskInfo.put("taskName", taskName);
				userTaskList.add(userTaskInfo);
			}
		}
		return userTaskList;
	}
	
	/**
	 * 显示流程定义列表
	 */
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<Map<String, Object>> listProcessDefinitions() throws Exception {
		List<ProcessDefinition> pdList = repositoryService.createProcessDefinitionQuery().list();
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for(ProcessDefinition pd:pdList){
			Map<String, Object> pdMap = new HashMap<String, Object>();
			pdMap.put("id", pd.getId());
			pdMap.put("key", pd.getKey());
			pdMap.put("name", pd.getName());
			pdMap.put("category", pd.getCategory());
			pdMap.put("version", pd.getVersion());
			pdMap.put("description", pd.getDescription());
			pdMap.put("suspended", pd.isSuspended());
			pdMap.put("deploymentId", pd.getDeploymentId());
			resultList.add(pdMap);
		}
		return resultList;
	}
	
	/**
	 * 暂停流程定义
	 */
	@Override
	public void suspendProcessDefinition(String processDefinitionId) throws Exception {
		repositoryService.suspendProcessDefinitionById(processDefinitionId,true,null);
	}
	
	/**
	 * 恢复流程定义
	 */
	@Override
	public void activeProcessDefinition(String processDefinitionId) throws Exception {
		repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
	}
	
	/**
	 * 显示流程定义图形
	 */
	@Override
	public InputStream graphProcessDefinition(String processDefinitionId) throws Exception {
		Command<InputStream> cmd = new ProcessDefinitionDiagramCmd(processDefinitionId);
        InputStream is = managementService.executeCommand(cmd);
        
		return is;
	}
	
	/**
	 * 查看usertask的办理人或办理人角色
	 */
	@Override
	public List<BpmConfUserDTO> findUserTaskAssign(String procDefId, String procDefKey, String taskDefKey) throws Exception {
		List<BpmConfUser> bpmConfUsers = BpmConfUser.findBpmConfUser(procDefId, procDefKey, taskDefKey);
		
		List<BpmConfUserDTO> bpmConfUserDTOs = new ArrayList<BpmConfUserDTO>();
		BpmConfUserDTO bpmConfUserDTO = null;
		for (BpmConfUser bpmConfUser : bpmConfUsers) {
	
			Set<Identity> users = bpmConfUser.getUsers();
			if(null != users && users.size() > 0){
				for (Identity identity : users) {
					bpmConfUserDTO = new BpmConfUserDTO();
					BpmConfUserBeanUtil.domainToDTO(bpmConfUserDTO, bpmConfUser);
					
					bpmConfUserDTO.setUserId(identity.getId());
					bpmConfUserDTO.setUserName(identity.getName());
					bpmConfUserDTO.setUserType("user");
					
					bpmConfUserDTOs.add(bpmConfUserDTO);
				}
			}
			
			Set<Role> roles = bpmConfUser.getRoles();
			if(null != roles && roles.size() > 0){
				for (Role role : roles) {
					bpmConfUserDTO = new BpmConfUserDTO();
					BpmConfUserBeanUtil.domainToDTO(bpmConfUserDTO, bpmConfUser);
					
					bpmConfUserDTO.setUserId(role.getId());
					bpmConfUserDTO.setUserName(role.getName());
					bpmConfUserDTO.setUserType("role");
					
					bpmConfUserDTOs.add(bpmConfUserDTO);
				}
			}
			
		}
		return bpmConfUserDTOs;
	}

	/**
	 * 查看可以分配到userTask的角色
	 */
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public Page<RoleDTO> findNotAssignRoleByUserTask(BpmConfUserDTO queryDTO,int page,int pagesize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _role from Role _role where _role.id not in "
				+ "(select _role.id from BpmConfUser _bpmConfUser inner join _bpmConfUser.roles _role "
				+ "where _bpmConfUser.procDefId = ? and _bpmConfUser.procDefKey = ? and _bpmConfUser.taskDefKey = ?)");
		List<Object> conditionVals = new ArrayList<Object>();
		
		conditionVals.add(queryDTO.getProcDefId());
		conditionVals.add(queryDTO.getProcDefKey());
		conditionVals.add(queryDTO.getTaskDefKey());
		
		Page<Role> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pagesize);
		
		List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
		RoleDTO roleDTO = null;
		for(Role role:pages.getResult()){
			roleDTO = new RoleDTO();
			RoleBeanUtil.domainToDTO(roleDTO, role);
			roleDTOs.add(roleDTO);
		}
		return new Page<RoleDTO>(pages.getCurrentPageNo(),pages.getTotalCount(),pages.getPageSize(),roleDTOs);
	}
	
	/**
	 * 为UserTask分配角色
	 */
	@Override
	public void assignRoleToUserTask(BpmConfUserDTO bpmConfDTO, Long[] roleIds) throws Exception {
		Long bpmConfId = isExistsBpmConf(bpmConfDTO);
		
		BpmConfUser bpmConfUser = null;
		if(bpmConfId != 0){
			bpmConfUser = BpmConfUser.load(BpmConfUser.class, bpmConfId);
		} else {
			bpmConfUser = new BpmConfUser();
			BpmConfUserBeanUtil.dtoToDomain(bpmConfUser, bpmConfDTO);
		}
		
		for (Long roleId : roleIds) {
			Role role = Role.load(Role.class, roleId);
			bpmConfUser.getRoles().add(role);
		}
		
		if(bpmConfId == 0){
			bpmConfUser.save();
		}
	}
	
	/**
	 * 判断bpm_conf是否存在usertask的记录
	 * @param bpmConfDTO
	 * @return
	 */
	private Long isExistsBpmConf(BpmConfUserDTO bpmConfDTO){
		List<BpmConfUser> bpmConfUsers = BpmConfUser.findBpmConfUser(bpmConfDTO.getProcDefId(), bpmConfDTO.getProcDefKey(), bpmConfDTO.getTaskDefKey());
		if(null != bpmConfUsers && bpmConfUsers.size() != 0){
			return bpmConfUsers.get(0).getId();
		} else {
			return Long.valueOf(0);
		}
	}
	/**
	 * 查看可以分配到UserTask的用户
	 */
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public Page<IdentityDTO> findNotAssignUserByUserTask(BpmConfUserDTO queryDTO, int page, int pagesize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _user from Identity _user where _user.id not in "
				+ "(select _user.id from BpmConfUser _bpmConfUser inner join _bpmConfUser.users _user "
				+ "where _bpmConfUser.procDefId = ? and _bpmConfUser.procDefKey = ? and _bpmConfUser.taskDefKey = ?)");
		List<Object> conditionVals = new ArrayList<Object>();
		
		conditionVals.add(queryDTO.getProcDefId());
		conditionVals.add(queryDTO.getProcDefKey());
		conditionVals.add(queryDTO.getTaskDefKey());
		
		Page<Identity> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pagesize);
		
		List<IdentityDTO> identityDTOs = new ArrayList<IdentityDTO>();
		IdentityDTO identityDTO = null;
		for (Identity identity : pages.getResult()) {
			identityDTO = new IdentityDTO();
			IdentityBeanUtil.domainToDTO(identityDTO, identity);
			identityDTOs.add(identityDTO);
		}
		return new Page<IdentityDTO>(pages.getCurrentPageNo(),pages.getTotalCount(),pages.getPageSize(),identityDTOs);
	}
	
	/**
	 * 为UserTask分配用户
	 */
	@Override
	public void assignUserToUserTask(BpmConfUserDTO bpmConfDTO, Long[] userIds) throws Exception {
		Long bpmConfId = isExistsBpmConf(bpmConfDTO);
		
		BpmConfUser bpmConfUser = null;
		if(bpmConfId != 0){
			bpmConfUser = BpmConfUser.load(BpmConfUser.class, bpmConfId);
		} else {
			bpmConfUser = new BpmConfUser();
			BpmConfUserBeanUtil.dtoToDomain(bpmConfUser, bpmConfDTO);
		}
		
		for (Long userId : userIds) {
			Identity identity = Identity.load(Identity.class, userId);
			bpmConfUser.getUsers().add(identity);
		}
		
		if(bpmConfId == 0){
			bpmConfUser.save();
		}
	}
	
	/**
	 * 删除UserTask分配的执行人/执行角色
	 */
	@Override
	public void removeAssignForUserTask(BpmConfUserDTO[] bpmConfDTOs) throws Exception {
		Long bpmConfId = isExistsBpmConf(bpmConfDTOs[0]);
		
		BpmConfUser bpmConfUser = BpmConfUser.load(BpmConfUser.class, bpmConfId);
		
		for(BpmConfUserDTO bpmConfUserDTO:bpmConfDTOs){
			if("user".equals(bpmConfUserDTO.getUserType())){
				Identity user = Identity.load(Identity.class, bpmConfUserDTO.getUserId());
				bpmConfUser.getUsers().remove(user);
			} else if("role".equals(bpmConfUserDTO.getUserType())){
				Role role = Role.load(Role.class, bpmConfUserDTO.getUserId());
				bpmConfUser.getRoles().remove(role);
			}
		}
	}
	
	/**
	 * 查看所有运行中的任务
	 */
	@Override
	public List<Map<String, Object>> listTasks() throws Exception {
		List<Task> taskList = taskService.createTaskQuery().orderByTaskCreateTime().desc().list();
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for(Task task:taskList){
			Map<String, Object> taskMap = new HashMap<String, Object>();
			taskMap.put("id", task.getId());
			taskMap.put("name", task.getName());
			taskMap.put("createTime", task.getCreateTime());
			taskMap.put("taskDefKey", task.getTaskDefinitionKey());
			taskMap.put("taskAssignee", task.getAssignee());
			String processInstanceId = task.getProcessInstanceId();
			taskMap.put("processInstanceId", processInstanceId);
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			taskMap.put("started", historicProcessInstance.getStartUserId());
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();
			taskMap.put("procName", processDefinition.getName());
			taskMap.put("procDefId", processDefinition.getId());
			
			taskMap.put("procTitle", findProcTitle(historicProcessInstance.getId()));
			resultList.add(taskMap);
		}
		return resultList;
	}
	
	/**
	 * 获得流程实例标题
	 */
	private String findProcTitle(String processInstanceId) throws Exception {
		List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
		for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
			if("procTitle".equals(historicVariableInstance.getVariableName())){
				return String.valueOf(historicVariableInstance.getValue());
			}
		}
		return null;
	}

	/**
	 * 所有运行中的流程实例
	 */
	@Override
	public List<Map<String, Object>> listProcessInstances() throws Exception {
		List<ProcessInstance> procInsList = runtimeService.createProcessInstanceQuery().orderByProcessInstanceId().desc().list();
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for (ProcessInstance processInstance : procInsList) {
			Map<String, Object> hiMap = new HashMap<String, Object>();
			hiMap.put("procInsId", processInstance.getId());
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
			hiMap.put("procInsStartTime", historicProcessInstance.getStartTime());
			hiMap.put("suspended", processInstance.isSuspended());
			
			Task currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
			hiMap.put("currentTaskName", currentTask.getName());
			hiMap.put("currentTaskAssign", currentTask.getAssignee());
			
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
			hiMap.put("procName", processDefinition.getName());
			hiMap.put("procDefId", processDefinition.getId());
			hiMap.put("procDefKey", processDefinition.getKey());
			
			hiMap.put("procTitle", findProcTitle(processInstance.getId()));
			resultList.add(hiMap);
		}
		return resultList;
	}

	/**
	 * 暂停流程实例
	 */
	@Override
	public void suspendProcessInstanceId(String processInstanceId) throws Exception {
		runtimeService.suspendProcessInstanceById(processInstanceId);
		
	}

	/**
	 * 激活流程实例
	 */
	@Override
	public void activeProcessInstanceId(String processInstanceId) throws Exception {
		runtimeService.activateProcessInstanceById(processInstanceId);
		
	}

	/**
	 * 删除流程实例
	 */
	@Override
	public void removeProcessInstanceId(String processInstanceId) throws Exception {
		runtimeService.deleteProcessInstance(processInstanceId, "管理员删除");
		
	}

	/**
	 * 删除流程定义
	 */
	@Override
	public void removeProcessDeployment(String deploymentId) throws Exception {
		repositoryService.deleteDeployment(deploymentId, true);
		
	}
	
	/**
	 * 查询流程定义的可发起人
	 */
	@Override
	public List<BpmStarterConfDTO> findStarterByProcDefId(String procDefId) throws Exception {
		List<BpmStarterConf> bpmStarterConfList = BpmStarterConf.find("procDefId", procDefId);
		
		List<BpmStarterConfDTO> bpmStarterConfDTOList = new ArrayList<BpmStarterConfDTO>();
		BpmStarterConfDTO bpmStarterConfDTO = null;
		for (BpmStarterConf bpmStarterConf : bpmStarterConfList) {
			bpmStarterConfDTO = new BpmStarterConfDTO();
			bpmStarterConfDTO.setId(bpmStarterConf.getId());
			bpmStarterConfDTO.setProcDefId(procDefId);
			Long roleId = bpmStarterConf.getRoleId();
			if(null != roleId && 0 != roleId){
				bpmStarterConfDTO.setStarterId(roleId);
				Role role = Role.load(Role.class, roleId);
				if(null != role){
					bpmStarterConfDTO.setStarter(role.getName());
				}
				bpmStarterConfDTO.setStarterType("role");
			}
			Long userId = bpmStarterConf.getUserId();
			if(null != userId && 0 != userId){
				bpmStarterConfDTO.setStarterId(userId);
				Identity user = Identity.load(Identity.class, userId);
				if(null != user){
					bpmStarterConfDTO.setStarter(user.getName());
				}
				bpmStarterConfDTO.setStarterType("user");
			}
			bpmStarterConfDTOList.add(bpmStarterConfDTO);
		}
		return bpmStarterConfDTOList;
	}

	/**
	 * 查询未被分配到流程定义上的角色
	 */
	@Override
	public Page<RoleDTO> findNotAssignRoleByProcDef(String procDefId, int page, int pagesize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _role from Role _role where not exists "
				+ "(select _bpmStarterConf.roleId from BpmStarterConf _bpmStarterConf where _bpmStarterConf.procDefId = ? and _bpmStarterConf.roleId = _role.id )");
		List<Object> conditionVals = new ArrayList<Object>();
		
		conditionVals.add(procDefId);
		
		Page<Role> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pagesize);
		
		List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
		RoleDTO roleDTO = null;
		for(Role role:pages.getResult()){
			roleDTO = new RoleDTO();
			RoleBeanUtil.domainToDTO(roleDTO, role);
			roleDTOs.add(roleDTO);
		}
		return new Page<RoleDTO>(pages.getCurrentPageNo(),pages.getTotalCount(),pages.getPageSize(),roleDTOs);
	}
	
	/**
	 * 查询未被分配到流程定义上的用户
	 */
	@Override
	public Page<IdentityDTO> findNotAssignUserByProcDef(String procDefId, int page, int pagesize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _user from Identity _user where not exists "
				+ "(select _bpmStarterConf.userId from BpmStarterConf _bpmStarterConf where _bpmStarterConf.procDefId = ? and  _bpmStarterConf.userId = _user.id)");
		List<Object> conditionVals = new ArrayList<Object>();
		
		conditionVals.add(procDefId);
		
		Page<Identity> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pagesize);
		
		List<IdentityDTO> identityDTOs = new ArrayList<IdentityDTO>();
		IdentityDTO identityDTO = null;
		for (Identity identity : pages.getResult()) {
			identityDTO = new IdentityDTO();
			IdentityBeanUtil.domainToDTO(identityDTO, identity);
			identityDTOs.add(identityDTO);
		}
		return new Page<IdentityDTO>(pages.getCurrentPageNo(),pages.getTotalCount(),pages.getPageSize(),identityDTOs);
	}

	/**
	 * 为流程定义分配可执行角色
	 */
	@Override
	public void assignRoleToProcDef(String procDefId, Long[] roleIds) throws Exception {
		for (Long roleId : roleIds) {
			BpmStarterConf bpmStarterConf = new BpmStarterConf();
			bpmStarterConf.setProcDefId(procDefId);
			bpmStarterConf.setRoleId(roleId);
			bpmStarterConf.save();
		}
		
	}
	
	/**
	 * 为流程定义分配可执行用户
	 */
	@Override
	public void assignUserToProcDef(String procDefId, Long[] userIds) throws Exception {
		for (Long userId : userIds) {
			BpmStarterConf bpmStarterConf = new BpmStarterConf();
			bpmStarterConf.setProcDefId(procDefId);
			bpmStarterConf.setUserId(userId);
			bpmStarterConf.save();
		}
	}

	/**
	 * 删除流程定义可执行用户或角色
	 */
	@Override
	public void removeAssignForProcDef(Long[] ids) throws Exception {
		for (Long id : ids) {
			BpmStarterConf bpmStarterConf = BpmStarterConf.load(BpmStarterConf.class, id);
			bpmStarterConf.remove();
		}
		
	}

}
