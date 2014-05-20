package org.yixun.platform.web.wssecurity;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.wssecurity.RoleApplication;
import org.yixun.platform.application.wssecurity.dto.RoleDTO;

import com.dayatang.querychannel.support.Page;

@Controller("org.yixun.platform.web.wssecurity.RoleController")
@RequestMapping("/ws/role")
public class RoleController {
	@Inject
	private RoleApplication roleApplication;
	
	/**
	 * 查询全部user
	 * @param queryDTO
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/pageQueryRole")
	public Map<String, Object> pageQueryRole(RoleDTO queryDTO,int page,int pagesize) throws Exception {
		
		Page<RoleDTO> pages = roleApplication.pageQueryRole(queryDTO, page, pagesize);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", pages.getResult());
		result.put("Total", pages.getTotalCount());
		
		return result;
	}
	
	/**
	 * 保存用户
	 * @param userDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/saveRole")
	public Map<String, Object> saveRole(RoleDTO roleDTO) throws Exception {
		roleDTO = roleApplication.saveRole(roleDTO);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", roleDTO);
		result.put("result", "success");
		return result;
	}
	
	/**
	 * 更新用户
	 * @param userDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/updateRole")
	public Map<String, Object> updateRole(RoleDTO roleDTO) throws Exception {
		roleApplication.updateRole(roleDTO);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	/**
	 * 删除用户
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/removeRole")
	public Map<String, Object> removeRole(Long[] ids) throws Exception {
		roleApplication.removeRole(ids);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	/**
	 * 查询指定id用户
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findRoleById/{id}")
	public Map<String, Object> findRoleById(@PathVariable("id")Long id) throws Exception {
		RoleDTO roleDTO = roleApplication.findRoleById(id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", roleDTO);
		return result;
	}
	/**
	 * 查询用户所拥有的角色
	 * @param queryDTO
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/pageQueryRoleByUserId")
	public Map<String, Object> pageQueryRoleByUserId(RoleDTO queryDTO,Long userId,int page,int pagesize) throws Exception {
		
		Page<RoleDTO> pages = roleApplication.pageQueryRoleByUserId(queryDTO, userId, page, pagesize);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", pages.getResult());
		result.put("Total", pages.getTotalCount());
		
		return result;
	}
	
	/**
	 * 为用户分配角色
	 * @param userId
	 * @param roleIds
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/assignRoleToUser")
	public Map<String, Object> assignRoleToUser(Long userId, Long[] roleIds) throws Exception {
		
		roleApplication.assignRoleToUser(userId, roleIds);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	/**
	 * 删除为用户分配的角色
	 * @param userId
	 * @param roleIds
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/removeRoleForUser")
	public Map<String, Object> removeRoleForUser(Long userId, Long[] roleIds) throws Exception {
		roleApplication.removeRoleForUser(userId, roleIds);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	/**
	 * 没有分配给指定用户的角色
	 * @param queryDTO
	 * @param userId 用户ID
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findNotAssignRoleByUser")
	public Map<String, Object> findNotAssignRoleByUser(RoleDTO queryDTO,Long userId,@RequestParam("page")int page,int pagesize) throws Exception{
		Page<RoleDTO> pages = roleApplication.findNotAssignRoleByUser(queryDTO, userId, page, pagesize);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Total", pages.getTotalCount());
		result.put("Rows", pages.getResult());
		return result;
	}
	
	/**
	 * 为角色分配资源
	 * @param roleId
	 * @param resourceIds
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/assignResourceToRole")
	public Map<String, Object> assignResourceToRole(Long roleId, Long[] resourceIds) throws Exception {
		
		roleApplication.assignResourceToRole(roleId, resourceIds);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	/**
	 * 删除角色已分配的资源
	 * @param roleId
	 * @param resourceIds
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/removeResourceForRole")
	public Map<String, Object> removeResourceForRole(Long roleId, Long[] resourceIds) throws Exception {
		roleApplication.removeResourceForRole(roleId, resourceIds);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
}
