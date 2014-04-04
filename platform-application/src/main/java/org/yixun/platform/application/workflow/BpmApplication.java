package org.yixun.platform.application.workflow;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

public interface BpmApplication {
	public List<Map<String, Object>> listProcessDefinitions() throws Exception;
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
	public void doDelegate(String taskId,String attorney) throws Exception;
	public List<Map<String, Object>> listDelegatedTasks(Long userId) throws Exception;
	public Integer withdraw(String taskId) throws Exception;
	public List<Map<String, Object>> listGroupTasks(Long userId) throws Exception;
	public void claimTask(Long userId,String taskId) throws Exception;
}
