package org.yixun.platform.web.auth;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.security.RoleApplication;
import org.yixun.platform.application.security.dto.RoleDTO;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/role")
public class RoleController {
	
	@Inject
	private RoleApplication roleApplication;
	
	@ResponseBody
	@RequestMapping("/pageQueryRole")
	public Map<String, Object> pageQueryRole(RoleDTO queryDTO,@RequestParam("page")int page,@RequestParam("pagesize")int pageSize) throws Exception{
		Page<RoleDTO> pages = roleApplication.pageQueryRole(queryDTO, page, pageSize);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Total", pages.getTotalCount());
		result.put("Rows", pages.getResult());
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/pageQueryRoleByUserId")
	public Map<String, Object> pageQueryRoleByUserId(RoleDTO queryDTO,Long userId,@RequestParam("page")int page,int pagesize) throws Exception{
		Page<RoleDTO> pages = roleApplication.pageQueryRoleByUserId(queryDTO, userId, page, pagesize);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Total", pages.getTotalCount());
		result.put("Rows", pages.getResult());
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/findNotAssignRoleByUser")
	public Map<String, Object> findNotAssignRoleByUser(RoleDTO queryDTO,Long userId,@RequestParam("page")int page,int pagesize) throws Exception{
		Page<RoleDTO> pages = roleApplication.findNotAssignRoleByUser(queryDTO, userId, page, pagesize);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Total", pages.getTotalCount());
		result.put("Rows", pages.getResult());
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/assignRoleToUser")
	public Map<String, Object> assignRoleToUser(Long userId, Long[] roleIds) throws Exception {
		roleApplication.assignRoleToUser(userId, roleIds);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/removeRoleForUser")
	public Map<String, Object> removeRoleForUser(Long userId, Long[] roleIds) throws Exception {
		roleApplication.removeRoleForUser(userId, roleIds);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/findRoleById/{id}")
	public Map<String, Object> findRoleById(@PathVariable("id")Long id) throws Exception{
		RoleDTO roleDTO = roleApplication.findRoleById(id);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", roleDTO);
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/updateRole")
	public Map<String, Object> updateRole(RoleDTO roleDTO) throws Exception{
		roleApplication.updateRole(roleDTO);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/removeRole")
	public Map<String, Object> removeRole(String ids) throws Exception{
		String[] value = ids.split(",");
		Long[] idArrs = new Long[value.length];
		for(int i=0;i<value.length;i++){
			idArrs[i] = Long.parseLong(value[i]);
		}
		
		roleApplication.removeRole(idArrs);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/saveRole")
	public Map<String, Object> saveRole(RoleDTO roleDTO) throws Exception{
		roleDTO = roleApplication.saveRole(roleDTO);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		result.put("data", roleDTO);
		return result;
	}
}
