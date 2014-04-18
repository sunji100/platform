package org.yixun.platform.application.workflow;

import java.util.List;

import org.yixun.platform.application.workflow.dto.DelegateInfoDTO;

public interface DelegateApplication {
	/**
	 * 添加委托
	 * @param delegateInfo 
	 * @throws Exception
	 */
	public void addDelegateInfo(DelegateInfoDTO delegateInfo) throws Exception;
	/**
	 * 全部委托列表
	 * @return
	 * @throws Exception
	 */
	public List<DelegateInfoDTO> listDelegateInfos() throws Exception;
	/**
	 * 指定用户的委托列表
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<DelegateInfoDTO> listMyDelegateInfos(Long userId) throws Exception;
	/**
	 * 删除委托
	 * @param idList
	 * @throws Exception
	 */
	public void removeDelegateInfo(Long[] idList) throws Exception;
}
