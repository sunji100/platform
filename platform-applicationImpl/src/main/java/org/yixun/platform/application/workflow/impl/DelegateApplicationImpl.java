package org.yixun.platform.application.workflow.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.workflow.DelegateApplication;
import org.yixun.platform.application.workflow.dto.DelegateInfoDTO;
import org.yixun.platform.application.workflow.util.BpmDelegateInfoBeanUtil;
import org.yixun.platform.core.workflow.BpmDelegateInfo;
import org.yixun.platform.core.workflow.BpmDelegateProcInfo;

@Named
@Transactional
public class DelegateApplicationImpl implements DelegateApplication {

	@Inject
	private JdbcTemplate jdbcTemplate;
	@Inject
	private RepositoryService repositoryService;
	
	/**
	 * 添加委托
	 */
	@Override
	public void addDelegateInfo(DelegateInfoDTO delegateInfo) throws Exception {
//		String sql = "insert into bpm_delegate_info(assignee,attorney,start_time,end_time,process_definition_id,status) values(?,?,?,?,?,?)";
//        jdbcTemplate.update(sql, delegateInfo.getAssignee(), delegateInfo.getAttorney(), delegateInfo.getStartTime(), delegateInfo.getEndTime(),delegateInfo.getProcessDefinitionId(), 1);
		BpmDelegateInfo bpmDelegateInfo = new BpmDelegateInfo();
		BpmDelegateInfoBeanUtil.dtoToDomain(bpmDelegateInfo, delegateInfo);
		bpmDelegateInfo.setStatus(true);
		bpmDelegateInfo.save();
	}

	/**
	 * 全部委托列表
	 */
	@Override
	public List<DelegateInfoDTO> listDelegateInfos() throws Exception {
		List<BpmDelegateInfo> bpmDelegateInfos = BpmDelegateInfo.findAll(BpmDelegateInfo.class);
		
		List<DelegateInfoDTO> delegateInfoDTOs = new ArrayList<DelegateInfoDTO>();
		DelegateInfoDTO delegateInfoDTO =  null;
		for (BpmDelegateInfo bpmDelegateInfo : bpmDelegateInfos) {
			delegateInfoDTO = new DelegateInfoDTO();
			BpmDelegateInfoBeanUtil.domainToDTO(delegateInfoDTO, bpmDelegateInfo);
			
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(bpmDelegateInfo.getProcDefId()).singleResult();
			delegateInfoDTO.setProcDefName(processDefinition.getName());
			
			delegateInfoDTOs.add(delegateInfoDTO);
		}
		
		return delegateInfoDTOs;
	}

	/**
	 * 指定用户的委托列表
	 */
	@Override
	public List<DelegateInfoDTO> listMyDelegateInfos(Long userId) throws Exception {
		List<BpmDelegateInfo> bpmDelegateInfos = BpmDelegateInfo.findBy("assignee", userId);
		
		List<DelegateInfoDTO> delegateInfoDTOs = new ArrayList<DelegateInfoDTO>();
		DelegateInfoDTO delegateInfoDTO =  null;
		for (BpmDelegateInfo bpmDelegateInfo : bpmDelegateInfos) {
			delegateInfoDTO = new DelegateInfoDTO();
			BpmDelegateInfoBeanUtil.domainToDTO(delegateInfoDTO, bpmDelegateInfo);
			
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(bpmDelegateInfo.getProcDefId()).singleResult();
			delegateInfoDTO.setProcDefName(processDefinition.getName());
			
			delegateInfoDTOs.add(delegateInfoDTO);
		}
		
		return delegateInfoDTOs;
	}

	/**
	 * 删除委托
	 */
	@Override
	public void removeDelegateInfo(Long[] idList) throws Exception {
		for (Long id : idList) {
			BpmDelegateInfo bpmDelegateInfo = BpmDelegateInfo.load(BpmDelegateInfo.class, id);
			bpmDelegateInfo.remove();
		}
	}

}
