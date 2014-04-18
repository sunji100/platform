package org.yixun.platform.application.workflow.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.workflow.ModelerApplication;
import org.yixun.support.date.util.DateUtils;

@Named
@Transactional
public class ModelerApplicationImpl implements ModelerApplication {

	@Inject
	private RepositoryService repositoryService;
	@Inject
	private ManagementService managementService;
	/**
	 * 流程模型定义列表
	 */
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<Map<String, Object>> list() throws Exception {
		List<Model> modelList = repositoryService.createModelQuery().list();
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String, Object> modelMap = null;
		for (Model model : modelList) {
			modelMap = new HashMap<String, Object>();
			modelMap.put("id", model.getId());
			modelMap.put("key", model.getKey());
			modelMap.put("name", model.getName());
			modelMap.put("version", model.getVersion());
			modelMap.put("category", model.getCategory());
			modelMap.put("createTime", DateUtils.convertDateTimeToString(model.getCreateTime()));
			modelMap.put("lastUpdateTime", DateUtils.convertDateTimeToString(model.getLastUpdateTime()));
			modelMap.put("deploymentId", model.getDeploymentId());
			modelMap.put("metaInfo", model.getMetaInfo());
			resultList.add(modelMap);
		}
		return resultList;
	}
	
	/**
	 * 创建新模型，并返回模型ID
	 */
	@Override
	public String createModel() throws Exception {
		Model model = repositoryService.newModel();
		repositoryService.saveModel(model);
		return model.getId();
	}

	/**
	 * 删除流程定义模型
	 */
	@Override
	public void remove(String id) throws Exception {
		repositoryService.deleteModel(id);
		
	}

	/**
	 * 通过Model发布流程
	 */
	@Override
	public void deploy(String id) throws Exception {
		Model modelData = repositoryService.getModel(id);
		ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
		byte[] bpmnBytes = null;

		BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
		bpmnBytes = new BpmnXMLConverter().convertToXML(model);

		String processName = modelData.getName() + ".bpmn20.xml";
		Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes, "UTF-8")).deploy();
		modelData.setDeploymentId(deployment.getId());
		repositoryService.saveModel(modelData);
		repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();

	}

}
