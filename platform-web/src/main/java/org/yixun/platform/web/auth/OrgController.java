package org.yixun.platform.web.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.security.OrgApplication;
import org.yixun.platform.application.security.dto.OrgDTO;

@Controller
@RequestMapping("/org")
public class OrgController {
	
	@Inject
	private OrgApplication orgApplication;
	/**
	 * 获得组织机构树
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findOrgTree")
	public Map<String, Object> findOrgTree(HttpServletRequest request) throws Exception {
		List<OrgDTO> orgTree = orgApplication.findOrgTree();
		
		for (OrgDTO orgDTO : orgTree) {
			orgDTO.setIcon(request.getContextPath() + "/" + orgDTO.getIcon());
			modifyAllSubIcon(orgDTO,request);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", orgTree);
		
		return result;
	}
	/**
	 * 处理组织图标
	 * @param orgDTO
	 * @param request
	 */
	private void modifyAllSubIcon(OrgDTO orgDTO,HttpServletRequest request){
		if(null != orgDTO.getChildren()){
			for (OrgDTO subOrgDTO : orgDTO.getChildren()) {
				subOrgDTO.setIcon(request.getContextPath() + "/" + subOrgDTO.getIcon());
				modifyAllSubIcon(subOrgDTO,request);
			}
		}
	}
	/**
	 * 组织机构及组织中用户树
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findOrgAndIdentityTree")
	public Map<String, Object> findOrgAndIdentityTree(HttpServletRequest request) throws Exception {
		List<OrgDTO> orgTree = orgApplication.findOrgAndIdentityTree();
		
		for (OrgDTO orgDTO : orgTree) {
			orgDTO.setIcon(request.getContextPath() + "/" + orgDTO.getIcon());
			modifyAllSubIcon(orgDTO,request);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", orgTree);
		
		return result;
	}
	/**
	 * 为组织分配角色
	 * @param orgId
	 * @param roleIds
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/assignRoleToOrg")
	public Map<String, Object> assignRoleToOrg(Long orgId,String roleIds) throws Exception {
		String[] value = roleIds.split(",");
		Long[] idArrs = new Long[value.length];
		for(int i=0;i<value.length;i++){
			idArrs[i] = Long.parseLong(value[i]);
		}
		
		orgApplication.assignRoleToOrg(orgId, idArrs);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	/**
	 * 删除为组织分配的角色
	 * @param orgId
	 * @param roleIds
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/removeRoleForOrg")
	public Map<String, Object> removeRoleForOrg(Long orgId,String roleIds) throws Exception {
		String[] value = roleIds.split(",");
		Long[] idArrs = new Long[value.length];
		for(int i=0;i<value.length;i++){
			idArrs[i] = Long.parseLong(value[i]);
		}
		
		orgApplication.removeRoleForOrg(orgId, idArrs);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	/**
	 * 获得组织信息
	 * @param orgId 组织ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findOrgById/{id}")
	public Map<String, Object> findOrgById(@PathVariable("id")Long orgId) throws Exception{
		OrgDTO orgDTO = orgApplication.findOrgById(orgId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", orgDTO);
		return result;
	}
}
