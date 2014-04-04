package org.yixun.platform.application.workflow;

import java.util.List;

import org.yixun.platform.application.workflow.dto.DelegateInfoDTO;

public interface DelegateApplication {
	public void addDelegateInfo(DelegateInfoDTO delegateInfo) throws Exception;
	public List<DelegateInfoDTO> listDelegateInfos() throws Exception;
	public List<DelegateInfoDTO> listMyDelegateInfos(Long userId) throws Exception;
	public void removeDelegateInfo(Long[] idList) throws Exception;
}
