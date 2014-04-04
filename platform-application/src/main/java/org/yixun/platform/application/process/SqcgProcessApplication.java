package org.yixun.platform.application.process;

import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.yixun.platform.application.crud.dto.ProductDTO;

public interface SqcgProcessApplication {
	public ProcessInstance saveWorkflow(ProductDTO entity,String suggestion,String procDefKey,Long userId,Map<String, Object> variables) throws Exception;
	public List<Map<String,Object>> viewHistoryVariables(String processInstanceId) throws Exception;
	public ProductDTO loadBusinessData(String processInstanceId) throws Exception;
	public void completeTask(ProductDTO entity,String suggestion,String procDefKey,Long userId,Map<String, Object> variables,String taskId) throws Exception;
}
