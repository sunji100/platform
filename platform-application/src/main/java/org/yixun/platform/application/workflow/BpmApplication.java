package org.yixun.platform.application.workflow;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface BpmApplication {
	public List<Map<String, Object>> listProcessDefinitions() throws Exception;
	/**
	 * 获得用户可发起的流程定义
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> listProcessDefinitionsByUserId(Long userId) throws Exception;
	public String findFormUrlByProcDefId(String procDefId) throws Exception;
	public List<Map<String, Object>> listPersonalTasks(Long userId) throws Exception;
	public String findFormUrlByProcDefIdOrTaskDefKey(String procDefId,String taskDefKey) throws Exception;
	public List<Map<String, Object>> viewHistoryTasks(String processInstanceId) throws Exception;
	public InputStream graphHistoryProcessInstance(String processInstanceId) throws Exception;
	public List<Map<String, Object>> listHistoryTasks(Long userId) throws Exception;
	public List<Map<String, Object>> listRunningProcessInstances(Long userId) throws Exception;
	public List<Map<String, Object>> listCompletedProcessInstances(Long userId) throws Exception;
	public String findProcTitle(String processInstanceId) throws Exception;
	public String findTaskSuggestion(String taskId) throws Exception;
	public void rollback(String taskId,String suggestion) throws Exception;
	/**
	 * 任务代理
	 * @param taskId 任务ID
	 * @param attorney 代理userID
	 * @throws Exception
	 */
	public void doDelegate(String taskId,String attorney) throws Exception;
	public List<Map<String, Object>> listDelegatedTasks(Long userId) throws Exception;
	public Integer withdraw(String taskId) throws Exception;
	public List<Map<String, Object>> listGroupTasks(Long userId) throws Exception;
	/**
	 * 认领任务
	 * @param userId 认领userID
	 * @param taskId 任务ID
	 * @throws Exception
	 */
	public void claimTask(Long userId,String taskId) throws Exception;
}
