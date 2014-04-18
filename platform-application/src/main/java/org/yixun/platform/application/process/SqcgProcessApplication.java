package org.yixun.platform.application.process;

import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.yixun.platform.application.crud.dto.ProductDTO;

public interface SqcgProcessApplication {
	/**
	 * 保存业务数据+启动流程
	 * @param entity 业务实例
	 * @param suggestion 签字意见
	 * @param procDefKey 流程定义key
	 * @param userId 登录人
	 * @param variables 流程变量
	 * @return
	 * @throws Exception
	 */
	public ProcessInstance saveWorkflow(ProductDTO entity,String suggestion,String procDefKey,Long userId,Map<String, Object> variables) throws Exception;
	public List<Map<String,Object>> viewHistoryVariables(String processInstanceId) throws Exception;
	/**
	 *  获得流程实例的业务数据
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	public ProductDTO loadBusinessData(String processInstanceId) throws Exception;
	/**
	 * 完成任务
	 * @param entity
	 * @param suggestion
	 * @param procDefKey
	 * @param userId
	 * @param variables
	 * @param taskId
	 * @throws Exception
	 */
	public void completeTask(ProductDTO entity,String suggestion,String procDefKey,Long userId,Map<String, Object> variables,String taskId) throws Exception;
}
