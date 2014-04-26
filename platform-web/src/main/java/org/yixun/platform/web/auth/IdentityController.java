package org.yixun.platform.web.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.security.IdentityApplication;
import org.yixun.platform.application.security.dto.IdentityDTO;
import org.yixun.support.auth.util.SecurityMD5;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/identity")
public class IdentityController {
	
	@Inject
	private IdentityApplication identityApplication;
	
	@ResponseBody
	@RequestMapping("/pageQueryIdentity")
	public Map<String, Object> pageQueryIdentity(IdentityDTO identityDTO,@RequestParam("page")int pageNo,@RequestParam("pagesize")int pageSize) throws Exception{
		Page<IdentityDTO> page = identityApplication.pageQueryIdentity(identityDTO, pageNo, pageSize);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", page.getResult());
		result.put("Total", page.getTotalCount());
		
		return result;
	}
	/**
	 * 获得指定组织下所有用户
	 * @param identityDTO
	 * @param pageNo
	 * @param pageSize
	 * @param orgId 组织ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/pageQueryIdentityByOrgId")
	public Map<String, Object> pageQueryIdentityByOrgId(IdentityDTO identityDTO,@RequestParam("page")int pageNo,@RequestParam("pagesize")int pageSize,@RequestParam("orgId")Long orgId) throws Exception{
		Page<IdentityDTO> page = identityApplication.pageQueryIdentityByOrgId(identityDTO, pageNo, pageSize,orgId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", page.getResult());
		result.put("Total", page.getTotalCount());
		
		return result;
	}
	/**
	 * 获得已分配到角色上的用户
	 * @param identityDTO
	 * @param roleId 角色ID
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/pageQueryIdentityByRoleId")
	public Map<String, Object> pageQueryIdentityByRoleId(IdentityDTO identityDTO,Long roleId,@RequestParam("page")int pageNo,@RequestParam("pagesize")int pageSize) throws Exception{
		Page<IdentityDTO> page = identityApplication.pageQueryIdentityByRoleId(identityDTO, roleId, pageNo, pageSize);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", page.getResult());
		result.put("Total", page.getTotalCount());
		
		return result;
	}
	/**
	 * 获得所有未分配到指定角色的用户
	 * @param identityDTO
	 * @param roleId 角色ID
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findNotAssignUserByRole")
	public Map<String, Object> findNotAssignUserByRole(IdentityDTO identityDTO,@RequestParam("roleId")Long roleId,@RequestParam("page")int pageNo,@RequestParam("pagesize")int pageSize) throws Exception{
		Page<IdentityDTO> page = identityApplication.findNotAssignUserByRole(identityDTO,roleId,pageNo,pageSize);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", page.getResult());
		result.put("Total", page.getTotalCount());
		
		return result;
	}
	/**
	 * 向指定角色分配用户
	 * @param roleId 角色ID
	 * @param identityIds 用户ID数组
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/assignUserToRole")
	public Map<String, Object> assignUserToRole(Long roleId,String identityIds) throws Exception{
		String[] value = identityIds.split(",");
		Long[] identityIdArr = new Long[value.length];
		for(int i=0;i<value.length;i++){
			identityIdArr[i] = Long.parseLong(value[i]);
		}
		identityApplication.assignUserToRole(roleId, identityIdArr);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	/**
	 * 删除指定角色已分配的用户
	 * @param roleId 角色ID
	 * @param identityIds 用户ID数组
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/removeUserForRole")
	public Map<String, Object> removeUserForRole(Long roleId,String identityIds) throws Exception{
		String[] value = identityIds.split(",");
		Long[] identityIdArr = new Long[value.length];
		for(int i=0;i<value.length;i++){
			identityIdArr[i] = Long.parseLong(value[i]);
		}
		identityApplication.removeUserForRole(roleId, identityIdArr);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	/**
	 * 新增用户
	 * @param identityDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/saveIdentity")
	public Map<String, Object> saveIdentity(IdentityDTO identityDTO) throws Exception{
		identityDTO.setUserPassword(SecurityMD5.encode(identityDTO.getUserPassword(), identityDTO.getUserAccount()));
		identityDTO = identityApplication.saveIdentity(identityDTO);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", identityDTO);
		result.put("result", "success");
		return result;
	}
	/**
	 * 修改用户信息
	 * @param identityDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/updateIdentity")
	public Map<String, Object> updateIdentity(IdentityDTO identityDTO) throws Exception{
		identityApplication.updateIdentity(identityDTO);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	/**
	 * 获得用户信息
	 * @param id 用户ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findIdentityById/{id}")
	public Map<String, Object> findIdentityById(@PathVariable Long id) throws Exception{
		IdentityDTO identityDTO = identityApplication.findIdentityById(id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", identityDTO);
		return result;
	}
	/**
	 * 删除用户
	 * @param ids 要删除的用户id数组
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/removeIdentity")
	public Map<String, Object> removeIdentity(String ids) throws Exception{
		
		String[] value = ids.split(",");
		Long[] idArrs = new Long[value.length];
		for(int i=0;i<value.length;i++){
			idArrs[i] = Long.parseLong(value[i]);
		}
		
		identityApplication.removeIdentity(idArrs);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
}
