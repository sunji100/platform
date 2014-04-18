package org.yixun.platform.web.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.workflow.ModelerApplication;

@Controller
@RequestMapping("bpm/modeler")
public class ModelerController {
	@Inject
	private ModelerApplication modelerApplication;
	
	/**
	 * 流程模型定义列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/list")
	public Map<String, Object> list() throws Exception {
		List<Map<String, Object>> modelList = modelerApplication.list();
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", modelList);
		return result;
	}
	
	/**
	 * 创建新模型，并返回模型ID
	 * @return 模型ID
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/createModel")
	public Map<String, Object> createModel() throws Exception {
		String modelId = modelerApplication.createModel();
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", modelId);
		return result;
	}
	
	/**
	 * 删除流程定义模型
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/remove")
	public Map<String, Object> remove(String id) throws Exception {
		modelerApplication.remove(id);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	/**
	 * 通过Model发布流程
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/deploy")
	public Map<String, Object> deploy(String id) throws Exception {
		modelerApplication.deploy(id);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
}
