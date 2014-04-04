package org.yixun.platform.application.process.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.hibernate.mapping.Array;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.crud.dto.ProductDTO;
import org.yixun.platform.application.process.SqcgProcessApplication;
import org.yixun.platform.application.process.util.ProductBeanUtil;
import org.yixun.platform.core.crud.Product;

@Named
@Transactional
public class SqcgProcessApplicationImpl implements SqcgProcessApplication {

	@Inject
	private RuntimeService runtimeService;
	@Inject
	private IdentityService identityService;
	@Inject
	private TaskService taskService;
	@Inject
	private HistoryService historyService;
	/**
	 * 保存业务数据+启动流程
	 */
	@Override
	public ProcessInstance saveWorkflow(ProductDTO entity,String suggestion,String procDefKey,Long userId,Map<String, Object> variables) throws Exception {
		String businessKey = saveBusinessData(entity);
		ProcessInstance processInstance = createProcessInstance(procDefKey, userId, variables, businessKey);
		
		Task firstTask = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).active().singleResult();
		Map<String, Object> taskVariables = new HashMap<String, Object>();
		taskVariables.put("suggestion", suggestion);
		taskService.setVariablesLocal(firstTask.getId(), taskVariables);
		
		return processInstance;
	}
	
	
	@Override
	public List<Map<String, Object>> viewHistoryVariables(String processInstanceId) throws Exception {
		List<HistoricVariableInstance> historicVariableInstances = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).list();
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
			Map<String, Object> varMap = new HashMap<String, Object>();
			resultList.add(varMap);
		}
		return resultList;
	}
	
	/**
	 * 获得流程实例的业务数据
	 */
	@Override
	public ProductDTO loadBusinessData(String processInstanceId) throws Exception {
//		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
//		String businessKey = processInstance.getBusinessKey();
		String businessKey = historicProcessInstance.getBusinessKey();
		Product product = Product.load(Product.class, Long.parseLong(businessKey));
		
		ProductDTO productDTO = new ProductDTO();
		ProductBeanUtil.domainToDTO(productDTO, product);
		return productDTO;
	}

	/**
	 * 完成任务并流转
	 */
	@Override
	public void completeTask(ProductDTO entity, String suggestion, String procDefKey, Long userId, Map<String, Object> variables, String taskId) throws Exception {
		//如果是新流程，保存业务数据并启动流程，完成第一个任务
		if(!StringUtils.isEmpty(procDefKey)){
			String businessKey = saveBusinessData(entity);
			ProcessInstance processInstance = createProcessInstance(procDefKey, userId, variables, businessKey);
			Task firstTask = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).active().singleResult();
			taskId = firstTask.getId();
		} else {
			identityService.setAuthenticatedUserId(String.valueOf(userId));
		}
		Map<String, Object> taskVariables = new HashMap<String, Object>();
		taskVariables.put("suggestion", suggestion);
		taskService.setVariablesLocal(taskId, taskVariables);
		taskService.complete(taskId);
		
	}
	
	private String saveBusinessData(ProductDTO entity){
		Product product = new Product();
		ProductBeanUtil.dtoToDomain(product, entity);
		product.setId(null);
		product.save();
		
		String businessKey = product.getId().toString();
		return businessKey;
	}
	
	private ProcessInstance createProcessInstance(String procDefKey,Long userId,Map<String, Object> variables,String businessKey){
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(String.valueOf(userId));
		variables.put("money", businessKey);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(procDefKey, businessKey,variables);
		return processInstance;
	}

}
