package org.yixun.platform.web.wssecurity;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.wssecurity.RoleApplication;
import org.yixun.platform.application.wssecurity.dto.RoleDTO;
import org.yixun.platform.application.wssecurity.dto.UserDTO;

import com.dayatang.querychannel.support.Page;

@Controller
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
		
		Page<UserDTO> pages = roleApplication.pageQueryRole(queryDTO, page, pagesize);
		
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
}
