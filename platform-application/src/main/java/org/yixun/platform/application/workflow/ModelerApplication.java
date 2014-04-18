package org.yixun.platform.application.workflow;

import java.util.Map;
import java.util.List;

public interface ModelerApplication {
	/**
	 * 流程模型定义列表
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> list() throws Exception;
	/**
	 * 创建新模型，并返回模型ID
	 * @return 模型ID
	 * @throws Exception
	 */
	public String createModel() throws Exception;
	/**
	 * 删除流程定义模型
	 * @param id
	 * @throws Exception
	 */
	public void remove(String id) throws Exception;
	/**
	 * 通过Model发布流程
	 * @param id
	 * @throws Exception
	 */
	public void deploy(String id) throws Exception;
}
