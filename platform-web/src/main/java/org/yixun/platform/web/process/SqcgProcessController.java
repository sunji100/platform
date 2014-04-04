package org.yixun.platform.web.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.crud.dto.ProductDTO;
import org.yixun.platform.application.process.SqcgProcessApplication;
import org.yixun.platform.web.auth.util.AuthDetailUtil;

@Controller
@RequestMapping("/process/sqcg")
public class SqcgProcessController {
	
	@Inject
	private SqcgProcessApplication sqcgProcessApplication;
	
	/**
	 * 保存业务数据+启动流程
	 * @param entity
	 * @param procDefKey
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/saveWorkflow")
	public Map<String, Object> saveWorkflow(ProductDTO entity,String suggestion,String procTitle,String procDefKey) throws Exception {
		
		//获得登录用户id
		Long userId = AuthDetailUtil.getLoginUserId();
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("firstTask", "sqcgtask");
		variables.put("loginUserId", String.valueOf(userId));
		variables.put("procTitle", procTitle);
		
		ProcessInstance processInstance = sqcgProcessApplication.saveWorkflow(entity, suggestion, procDefKey, userId, variables);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", processInstance.getId());
		
		return result;
	}
	
	/**
	 * 获得流程实例的业务数据
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/loadBusinessData")
	public Map<String, Object> loadBusinessData(String processInstanceId) throws Exception {
		
		ProductDTO productDTO = sqcgProcessApplication.loadBusinessData(processInstanceId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", productDTO);
		
		return result;
	}
	
	/**
	 * 完成任务
	 * @param entity
	 * @param procDefKey
	 * @param userId
	 * @param variables
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/completeTask")
	public Map<String, Object> completeTask(ProductDTO entity, String suggestion, String procTitle, String procDefKey, String taskId) throws Exception {
		
		//获得登录用户id
		Long userId = AuthDetailUtil.getLoginUserId();
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("loginUserId", String.valueOf(userId));
		variables.put("procTitle", procTitle);
				
		sqcgProcessApplication.completeTask(entity, suggestion,procDefKey, userId, variables, taskId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		
		return result;
	}
	
	
}
