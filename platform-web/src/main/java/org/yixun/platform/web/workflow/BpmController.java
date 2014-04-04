package org.yixun.platform.web.workflow;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.workflow.BpmApplication;
import org.yixun.platform.web.auth.util.AuthDetailUtil;

@Controller
@RequestMapping("/bpm")
public class BpmController {
	
	@Inject
	private BpmApplication bpmApplication;
	
	/**
	 * 显示用户可发起的流程定义
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/listProcessDefinitions")
	public Map<String, Object> listProcessDefinitions() throws Exception {
		List<Map<String, Object>> resultList = bpmApplication.listProcessDefinitions();
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", resultList);
		return result;
	}
	
	/**
	 * 根据流程定义ID及task key查询相应表单url
	 * @param procDefId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findFormUrlByProcDefIdOrTaskDefKey")
	public Map<String, Object> findFormUrlByProcDefIdOrTaskDefKey(String procDefId,String taskDefKey) throws Exception {
		String formUrl = bpmApplication.findFormUrlByProcDefIdOrTaskDefKey(procDefId,taskDefKey);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", formUrl);
		return result;
	}
	
	/**
	 * 根据流程定义ID查询相应表单url
	 * @param procDefId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findFormUrlByProcDefId")
	public Map<String, Object> findFormUrlByProcDefId(String procDefId) throws Exception {
		String formUrl = bpmApplication.findFormUrlByProcDefId(procDefId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", formUrl);
		return result;
	}
	
	/**
	 * 显示待办任务列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/listPersonalTasks")
	public Map<String, Object> listPersonalTasks() throws Exception {
		
		Long userId = AuthDetailUtil.getLoginUserId();
		List<Map<String, Object>> resultList = bpmApplication.listPersonalTasks(userId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", resultList);
		return result;
	} 
	
	/**
	 * 查看历史任务列表
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/viewHistoryTasks")
	public Map<String, Object> viewHistoryTasks(String processInstanceId) throws Exception {
		List<Map<String, Object>> taskList = bpmApplication.viewHistoryTasks(processInstanceId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", taskList);
		
		return result;
	}
	
	/**
	 * 流程跟踪
	 */
	@RequestMapping("/graphHistoryProcessInstance")
	public void graphHistoryProcessInstance(String processInstanceId,HttpServletResponse response) throws Exception {
		InputStream is = bpmApplication.graphHistoryProcessInstance(processInstanceId);
        
		response.setContentType("image/png");
        IOUtils.copy(is, response.getOutputStream());
	}
	
	/**
	 * 显示待办任务列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/listHistoryTasks")
	public Map<String, Object> listHistoryTasks() throws Exception {
		
		Long userId = AuthDetailUtil.getLoginUserId();
		List<Map<String, Object>> resultList = bpmApplication.listHistoryTasks(userId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", resultList);
		return result;
	} 
	
	/**
	 * 用户发起的流程（未完成）
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/listRunningProcessInstances")
	public Map<String, Object> listRunningProcessInstances() throws Exception {

		Long userId = AuthDetailUtil.getLoginUserId();
		List<Map<String, Object>> resultList = bpmApplication.listRunningProcessInstances(userId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", resultList);
		return result;
	} 
	
	/**
	 * 用户发起的流程（已完成）
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/listCompletedProcessInstances")
	public Map<String, Object> listCompletedProcessInstances() throws Exception {

		Long userId = AuthDetailUtil.getLoginUserId();
		List<Map<String, Object>> resultList = bpmApplication.listCompletedProcessInstances(userId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", resultList);
		return result;
	}
	
	/**
	 * 获得流程实例标题
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findProcTitle")
	public Map<String,Object> findProcTitle(String processInstanceId) throws Exception {
		
		String procTitle = bpmApplication.findProcTitle(processInstanceId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", procTitle);
		return result;
	}
	
	/**
	 * 获得任务的签字意见
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findTaskSuggestion")
	public Map<String, Object> findTaskSuggestion(String taskId) throws Exception {
		String suggestion = bpmApplication.findTaskSuggestion(taskId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", suggestion);
		return result;
	}
	
	/**
	 * 回退任务
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/rollback")
	public Map<String, Object> rollback(String taskId,String suggestion) throws Exception {
		bpmApplication.rollback(taskId,suggestion);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	/**
	 * 任务代理
	 * @param taskId
	 * @param attorney
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/doDelegate")
	public Map<String, Object> doDelegate(String taskId, String attorney) throws Exception {
		bpmApplication.doDelegate(taskId, attorney);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	/**
	 * 代理中的任务（代理人还未完成该任务）
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/listDelegatedTasks")
	public Map<String, Object> listDelegatedTasks() throws Exception {
		
		Long userId = AuthDetailUtil.getLoginUserId();
		List<Map<String, Object>> resultList = bpmApplication.listDelegatedTasks(userId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", resultList);
		return result;
	}
	
	/**
	 * 撤消任务
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/withdraw")
	public Map<String, Object> withdraw(String taskId) throws Exception {
		Integer withDrawresult = bpmApplication.withdraw(taskId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", withDrawresult);
		return result;
	}
	
	/**
	 * 代领任务
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/listGroupTasks")
	public Map<String, Object> listGroupTasks() throws Exception {
		
		Long userId = AuthDetailUtil.getLoginUserId();
		List<Map<String, Object>> resultList = bpmApplication.listGroupTasks(userId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", resultList);
		return result;
	}
	
	/**
	 * 认领任务(认领人为登录用户)
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/claimTask")
	public Map<String, Object> claimTask(String taskId) throws Exception {
		
		Long userId = AuthDetailUtil.getLoginUserId();
		bpmApplication.claimTask(userId, taskId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	/**
	 * 认领任务(指定认领人)
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/claimTaskForId")
	public Map<String, Object> claimTaskForId(String taskId,Long userId) throws Exception {
		
		bpmApplication.claimTask(userId, taskId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
}
