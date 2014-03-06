package org.yixun.platform.web.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.security.ResourceApplication;
import org.yixun.platform.application.security.dto.ResourceDTO;
import org.yixun.platform.infra.auth.AuthDetailUtil;

@Controller
@RequestMapping("/resource")
public class ResourceController {
	
	@Inject
	private ResourceApplication resourceApplication;
	
	@ResponseBody
	@RequestMapping("/findTopLevelMenuByUser")
	public Map<String, Object> findTopLevelMenuByUser(){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<ResourceDTO> topMenuList = resourceApplication.findTopLevelMenuByUser(userDetails.getUsername());
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", topMenuList);
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/findMenuByUser")
	public Map<String, Object> findMenuByUser(){
		
		List<ResourceDTO> menuList = resourceApplication.findMenuByUser(AuthDetailUtil.getLoginName());
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", menuList);
		return result;
	}
}
