package org.yixun.platform.web.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.workflow.DelegateApplication;
import org.yixun.platform.application.workflow.dto.DelegateInfoDTO;
import org.yixun.platform.web.auth.util.AuthDetailUtil;

@Controller
@RequestMapping("/bpm/delegate")
public class DelegateController {
	
	@Inject
	private DelegateApplication delegateApplication;
	
	/**
	 * 添加委托
	 * @param delegateInfo
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/addDelegateInfo")
	public Map<String, Object> addDelegateInfo(DelegateInfoDTO delegateInfo,String[] procDefIdList) throws Exception {
		Long userId = AuthDetailUtil.getLoginUserId();
		delegateInfo.setAssignee(String.valueOf(userId));
		for (String procDefId : procDefIdList) {
			delegateInfo.setProcDefId(procDefId);
			delegateApplication.addDelegateInfo(delegateInfo);
		}
	
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	/**
	 * 全部委托列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/listDelegateInfos")
	public Map<String, Object> listDelegateInfos() throws Exception {
		List<DelegateInfoDTO> delegateInfoDTOs = delegateApplication.listDelegateInfos();
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", delegateInfoDTOs);
		return result;
	}
	
	/**
	 * 指定用户的委托列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/listMyDelegateInfos")
	public Map<String, Object> listMyDelegateInfos() throws Exception {
		Long userId = AuthDetailUtil.getLoginUserId();
		List<DelegateInfoDTO> delegateInfoDTOs = delegateApplication.listMyDelegateInfos(userId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", delegateInfoDTOs);
		return result;
	}
	
	/**
	 * 删除委托
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/removeDelegateInfo")
	public Map<String, Object> removeDelegateInfo(Long[] idList) throws Exception {
		delegateApplication.removeDelegateInfo(idList);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
}
