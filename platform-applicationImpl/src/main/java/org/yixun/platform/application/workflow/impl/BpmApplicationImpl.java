package org.yixun.platform.application.workflow.impl;

import java.io.InputStream;
import java.text.MessageFormat;
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
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.workflow.BpmApplication;
import org.yixun.platform.core.security.Identity;
import org.yixun.platform.core.security.Org;
import org.yixun.platform.core.security.Role;
import org.yixun.platform.core.workflow.BpmFormConf;
import org.yixun.platform.workflow.cmd.HistoryProcessInstanceDiagramCmd;
import org.yixun.platform.workflow.cmd.RollbackTaskCmd;
import org.yixun.platform.workflow.cmd.WithdrawTaskCmd;

import com.dayatang.querychannel.service.QueryChannelService;

@Named
@Transactional
public class BpmApplicationImpl implements BpmApplication {

	@Inject
	private RepositoryService repositoryService;
	@Inject
	private TaskService taskService;
	@Inject
	private RuntimeService runtimeService;
	@Inject
	private HistoryService historyService;
	@Inject
	private ManagementService managementService;
	@Inject
	private QueryChannelService queryChannelService;
	@Inject
	private JdbcTemplate jdbcTemplate;
	/**
	 * 显示用户可发起的流程定义
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
			resultList.add(pdMap);
		}
		return resultList;
	}
	/**
	 * 获得用户可发起的流程定义
	 */
	@Override
	public List<Map<String, Object>> listProcessDefinitionsByUserId(Long userId) throws Exception {
		Identity user = Identity.load(Identity.class, userId);
		List<Long> roleIds = findAllRolesByUser(user);
		List<String> procDefIds = findProcDefIdByUserIdOrRoleId(userId, roleIds);
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		for (String procDefId : procDefIds) {
			List<ProcessDefinition> pdList = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).list();
			for(ProcessDefinition pd:pdList){
				Map<String, Object> pdMap = new HashMap<String, Object>();
				pdMap.put("id", pd.getId());
				pdMap.put("key", pd.getKey());
				pdMap.put("name", pd.getName());
				pdMap.put("category", pd.getCategory());
				pdMap.put("version", pd.getVersion());
				pdMap.put("description", pd.getDescription());
				pdMap.put("suspended", pd.isSuspended());
				resultList.add(pdMap);
			}
		}
		
		return resultList;
	}
	/**
	 * 根据用户ID和角色ID查询可发起的流程定义
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	private List<String> findProcDefIdByUserIdOrRoleId(Long userId,List<Long> roleIds){
		String jpql = "select _bpmStarterConf.procDefId from BpmStarterConf _bpmStarterConf where _bpmStarterConf.userId = ? or _bpmStarterConf.roleId in ("+ StringUtils.join(roleIds, ",") +")";
		List<Object> conditionVals = new ArrayList<Object>();
		
		conditionVals.add(userId);
		
		return queryChannelService.queryResult(jpql, conditionVals.toArray());
	}
	/**
	 * 获得用户及其从组织中继承的全部角色
	 * @param user
	 * @return
	 */
	private List<Long> findAllRolesByUser(Identity user){
		//用户所拥有的角色
		Set<Role> roles = user.getRoles();
		List<Long> roleList = new ArrayList<Long>();
		if(null != roles){
			for (Role role : roles) {
				roleList.add(role.getId());
			}
		}
		
		//用户所在组织
		Set<Org> orgs = user.getOrgs();
		if(orgs != null){
			List<Long> orgIdList = new ArrayList<Long>();
			//获得用户所在组织及所有上级组织
			for (Org org : orgs) {
				orgIdList.add(org.getId());
				findAllParentOrgId(org,orgIdList);
			}
			//获得用户从组织中继承来的角色
			List<Long> orgRoleList = findRolesByOrgList(orgIdList);
			for (Long roleId : orgRoleList) {
				roleList.add(roleId);
			}
		}
		return roleList;
	}
	/**
	 * 获得组织所拥有的角色
	 * @param orgIdList
	 * @return
	 */
	private List<Long> findRolesByOrgList(List<Long> orgIdList){
		String jpql = "select _role.id from Role _role inner join _role.orgs _org where _org.id in ("+ StringUtils.join(orgIdList, ",") +")";
		return queryChannelService.queryResult(jpql, null);
	}
	
	/**
	 * 获得相应组织所有上级组织
	 * @param org 当前组织
	 * @param orgIdList 上级组织IDList
	 */
	private void findAllParentOrgId(Org org,List<Long> orgIdList){
		Set<Org> parents = org.getParents();
		if(null != parents && parents.size() != 0){
			for (Org parentOrg : parents) {
				orgIdList.add(parentOrg.getId());
				findAllParentOrgId(parentOrg,orgIdList);
			}
		}
	}
	
	/**
	 * 查询流程定义对应的业务表单url
	 */
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public String findFormUrlByProcDefId(String procDefId) throws Exception {
		List<BpmFormConf> bpmFormConfs = BpmFormConf.findBpmFormConfByPdId(procDefId);
		if(!bpmFormConfs.isEmpty()){
			return bpmFormConfs.iterator().next().getFormUrl();
		}
		return null;
	}

	/**
	 * 显示待办任务列表
	 */
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<Map<String, Object>> listPersonalTasks(Long userId) throws Exception {
		List<Task> taskList = taskService.createTaskQuery().taskAssignee(String.valueOf(userId)).active().orderByTaskCreateTime().desc().list();
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for(Task task:taskList){
			Map<String, Object> taskMap = new HashMap<String, Object>();
			taskMap.put("id", task.getId());
			taskMap.put("name", task.getName());
			taskMap.put("createTime", task.getCreateTime());
			taskMap.put("taskDefKey", task.getTaskDefinitionKey());
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
	 * 根据流程定义ID及task key查询相应表单url
	 */
	@Override
	public String findFormUrlByProcDefIdOrTaskDefKey(String procDefId, String taskDefKey) throws Exception {
		StringBuilder jpql = new StringBuilder("select _bpmFormConf from BpmFormConf _bpmFormConf where 1=1");
		List<Object> conditionVals = new ArrayList<Object>();
		
		if(!StringUtils.isEmpty(procDefId)){
			jpql.append(" and _bpmFormConf.procDefId like ?");
			conditionVals.add(MessageFormat.format("%{0}%", procDefId));
		}
		
		if(!StringUtils.isEmpty(taskDefKey)){
			jpql.append(" and _bpmFormConf.taskDefKey like ?");
			conditionVals.add(MessageFormat.format("%{0}%", taskDefKey));
		}
		
		List<BpmFormConf> bpmFormConfs = queryChannelService.queryResult(jpql.toString(), conditionVals.toArray());
		if(!bpmFormConfs.isEmpty()){
			return bpmFormConfs.get(0).getFormUrl();
		}
		return null;
	}
	
	/**
	 * 查看历史任务列表
	 */
	@Override
	public List<Map<String, Object>> viewHistoryTasks(String processInstanceId) throws Exception {
		List<HistoricTaskInstance> historicTasks = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).list();
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for (HistoricTaskInstance historicTaskInstance : historicTasks) {
			Map<String, Object> taskMap = new HashMap<String, Object>();
			taskMap.put("taskId", historicTaskInstance.getId());
			taskMap.put("taskName", historicTaskInstance.getName());
			taskMap.put("taskAssignee", historicTaskInstance.getAssignee());
			taskMap.put("startTime", historicTaskInstance.getStartTime());
			taskMap.put("endTime", historicTaskInstance.getEndTime());
			taskMap.put("taskReason", historicTaskInstance.getDeleteReason());
			
			List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().taskId(historicTaskInstance.getId()).list();
			String suggestion = "";
			for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
				if("suggestion".equals(historicVariableInstance.getVariableName())){
					suggestion  = String.valueOf(historicVariableInstance.getValue());
				}
			}
			taskMap.put("suggestion", suggestion);
			resultList.add(taskMap);
		}
		
		return resultList;
	}

	/**
	 * 流程跟踪
	 */
	@Override
	public InputStream graphHistoryProcessInstance(String processInstanceId) throws Exception {
		Command<InputStream> cmd = new HistoryProcessInstanceDiagramCmd(processInstanceId);
		InputStream is = managementService.executeCommand(cmd);
		return is;
	}

	/**
	 * 已办任务
	 */
	@Override
	public List<Map<String, Object>> listHistoryTasks(Long userId) throws Exception{
		List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery().taskAssignee(String.valueOf(userId)).finished().list();
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for(HistoricTaskInstance task:historicTasks){
			Map<String, Object> taskMap = new HashMap<String, Object>();
			taskMap.put("taskId", task.getId());
			taskMap.put("taskName", task.getName());
			taskMap.put("startTime", task.getStartTime());
			taskMap.put("endTime", task.getEndTime());
			taskMap.put("taskDefKey", task.getTaskDefinitionKey());
			String processInstanceId = task.getProcessInstanceId();
			taskMap.put("processInstanceId", processInstanceId);
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			taskMap.put("started", historicProcessInstance.getStartUserId());
			taskMap.put("procStartTime", historicProcessInstance.getStartTime());
			taskMap.put("procEndTime", historicProcessInstance.getEndTime());
			String currentTaskName = "";
			if(null == historicProcessInstance.getEndTime()){
				Task currentTask = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
				currentTaskName = currentTask.getName();
			}
			taskMap.put("currentTaskName", currentTaskName);
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();
			taskMap.put("procName", processDefinition.getName());
			taskMap.put("procDefId", processDefinition.getId());
			
			taskMap.put("procTitle", findProcTitle(historicProcessInstance.getId()));
			resultList.add(taskMap);
		}
		return resultList;
	}

	/**
	 * 用户发起的流程（未完成）
	 */
	@Override
	public List<Map<String, Object>> listRunningProcessInstances(Long userId) throws Exception {
		List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().startedBy(String.valueOf(userId)).unfinished().orderByProcessInstanceStartTime().desc().list();
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
			Map<String, Object> hiMap = new HashMap<String, Object>();
			hiMap.put("procInsId", historicProcessInstance.getId());
			hiMap.put("procInsStartTime", historicProcessInstance.getStartTime());
			
			Task currentTask = taskService.createTaskQuery().processInstanceId(historicProcessInstance.getId()).singleResult();
			hiMap.put("currentTaskName", currentTask.getName());
			hiMap.put("currentTaskAssign", currentTask.getAssignee());
			
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();
			hiMap.put("procName", processDefinition.getName());
			hiMap.put("procDefId", processDefinition.getId());
			hiMap.put("procDefKey", processDefinition.getKey());
			
			hiMap.put("procTitle", findProcTitle(historicProcessInstance.getId()));
			resultList.add(hiMap);
		}
		return resultList;
	}

	/**
	 * 用户发起的流程（已完成）
	 */
	@Override
	public List<Map<String, Object>> listCompletedProcessInstances(Long userId) throws Exception {
		List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery().startedBy(String.valueOf(userId)).finished().orderByProcessInstanceStartTime().desc().list();
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
			Map<String, Object> hiMap = new HashMap<String, Object>();
			hiMap.put("procInsId", historicProcessInstance.getId());
			hiMap.put("procInsStartTime", historicProcessInstance.getStartTime());
			hiMap.put("procInsEndTime", historicProcessInstance.getEndTime());
			
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();
			hiMap.put("procName", processDefinition.getName());
			hiMap.put("procDefId", processDefinition.getId());
			hiMap.put("procDefKey", processDefinition.getKey());
			
			hiMap.put("procTitle", findProcTitle(historicProcessInstance.getId()));
			resultList.add(hiMap);
		}
		return resultList;
	}
	
	/**
	 * 获得流程实例标题
	 */
	@Override
	public String findProcTitle(String processInstanceId) throws Exception {
		List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
		for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
			if("procTitle".equals(historicVariableInstance.getVariableName())){
				return String.valueOf(historicVariableInstance.getValue());
			}
		}
		return null;
	}

	/**
	 * 获得任务的签字意见
	 */
	@Override
	public String findTaskSuggestion(String taskId) throws Exception {
		List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().taskId(taskId).list();
		for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
			if("suggestion".equals(historicVariableInstance.getVariableName())){
				return String.valueOf(historicVariableInstance.getValue());
			}
		}
		return null;
	}

	
	/**
	 * 回退任务
	 */
	@Override
	public void rollback(String taskId,String suggestion) throws Exception {
		//保存签字意见
		taskService.setVariableLocal(taskId, "suggestion", suggestion);
		//回退操作
		Command<Integer> cmd = new RollbackTaskCmd(taskId);

        managementService.executeCommand(cmd);
		
	}
	
	
	/**
	 * 任务委托
	 */
	@Override
	public void doDelegate(String taskId, String attorney) throws Exception {
		taskService.delegateTask(taskId, attorney);
	}

	/**
	 * 代理中的任务（代理人还未完成该任务）
	 */
	@Override
	public List<Map<String, Object>> listDelegatedTasks(Long userId) throws Exception {
		
		List<Task> taskList = taskService.createTaskQuery().taskOwner(String.valueOf(userId)).taskDelegationState(DelegationState.PENDING).list();
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for(Task task:taskList){
			Map<String, Object> taskMap = new HashMap<String, Object>();
			taskMap.put("taskId", task.getId());
			taskMap.put("taskName", task.getName());
			taskMap.put("taskCreateTime", task.getCreateTime());
			taskMap.put("taskDefKey", task.getTaskDefinitionKey());
			taskMap.put("taskDelegate", task.getAssignee());
			String processInstanceId = task.getProcessInstanceId();
			taskMap.put("processInstanceId", processInstanceId);
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			taskMap.put("procStarted", historicProcessInstance.getStartUserId());
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();
			taskMap.put("procName", processDefinition.getName());
			taskMap.put("procDefId", processDefinition.getId());
			
			taskMap.put("procTitle", findProcTitle(historicProcessInstance.getId()));
			resultList.add(taskMap);
		}
		return resultList;
	}

	/**
	 * 撤消任务
	 */
	@Override
	public Integer withdraw(String taskId) throws Exception {
		//撤消签字意见
		List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().taskId(taskId).variableName("suggestion").list();
		for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
			jdbcTemplate.update("delete from ACT_HI_VARINST where id_=?", historicVariableInstance.getId());
		}
		//taskService.setVariableLocal(taskId, "suggestion", null);
		
		WithdrawTaskCmd withdrawTaskCmd = new WithdrawTaskCmd(taskId);
		Integer result = managementService.executeCommand(withdrawTaskCmd);
		return result;
	}

	/**
	 * 代领任务
	 */
	@Override
	public List<Map<String, Object>> listGroupTasks(Long userId) throws Exception {
		List<Task> taskList = taskService.createTaskQuery().taskCandidateUser(String.valueOf(userId)).active().list();
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for(Task task:taskList){
			Map<String, Object> taskMap = new HashMap<String, Object>();
			taskMap.put("id", task.getId());
			taskMap.put("name", task.getName());
			taskMap.put("createTime", task.getCreateTime());
			taskMap.put("taskDefKey", task.getTaskDefinitionKey());
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
	 * 认领任务
	 */
	@Override
	public void claimTask(Long userId, String taskId) throws Exception {
		taskService.claim(taskId, String.valueOf(userId));
		
	}

}
