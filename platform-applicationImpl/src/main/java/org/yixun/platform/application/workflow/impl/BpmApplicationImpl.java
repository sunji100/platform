package org.yixun.platform.application.workflow.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.workflow.BpmApplication;
import org.yixun.platform.core.workflow.BpmFormConf;

@Named
@Transactional
public class BpmApplicationImpl implements BpmApplication {

	@Inject
	private RepositoryService repositoryService;
	/**
	 * 显示流程定义列表
	 */
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<Map<String, Object>> listProcessDefinitions() throws Exception {
		List<ProcessDefinition> pdList = repositoryService.createProcessDefinitionQuery().list();
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for(ProcessDefinition pd:pdList){
			Map<String, Object> pdMap = new HashMap<String, Object>();
			pdMap.put("id", pd.getId());
			pdMap.put("key", pd.getKey());
			pdMap.put("name", pd.getName());
			pdMap.put("category", pd.getCategory());
			pdMap.put("version", pd.getVersion());
			pdMap.put("description", pd.getDescription());
			pdMap.put("suspended", pd.isSuspended());
			resultList.add(pdMap);
		}
		return resultList;
	}

}
