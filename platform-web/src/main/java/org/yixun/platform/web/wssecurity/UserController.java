package org.yixun.platform.web.wssecurity;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.wssecurity.UserApplication;
import org.yixun.platform.application.wssecurity.dto.UserDTO;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/ws/user")
public class UserController {
	@Inject
	private UserApplication userApplication;
	
	/**
	 * 查询全部user
	 * @param queryDTO
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/pageQueryUser")
	public Map<String, Object> pageQueryUser(UserDTO queryDTO,int page,int pagesize) throws Exception {
		
		Page<UserDTO> pages = userApplication.pageQueryUser(queryDTO, page, pagesize);
		
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
	@RequestMapping("/saveUser")
	public Map<String, Object> saveUser(UserDTO userDTO) throws Exception {
		userDTO = userApplication.saveUser(userDTO);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", userDTO);
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
	@RequestMapping("/updateUser")
	public Map<String, Object> updateUser(UserDTO userDTO) throws Exception {
		userApplication.updateUser(userDTO);
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
	@RequestMapping("/removeUser")
	public Map<String, Object> removeUser(Long[] ids) throws Exception {
		userApplication.removeUser(ids);
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
	@RequestMapping("/findUserById/{id}")
	public Map<String, Object> findUserById(@PathVariable("id")Long id) throws Exception {
		UserDTO userDTO = userApplication.findUserById(id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", userDTO);
		return result;
	}
}
