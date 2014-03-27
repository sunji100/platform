package org.yixun.platform.web.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.security.ResourceApplication;
import org.yixun.platform.application.security.dto.ResourceDTO;
import org.yixun.platform.application.security.dto.ResourceTypeDTO;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/resource")
public class ResourceController {
	
	@Inject
	private ResourceApplication resourceApplication;
	
	@ResponseBody
	@RequestMapping("/pageQueryResource")
	public Map<String, Object> pageQueryResource(ResourceDTO queryDTO,int page,int pagesize) throws Exception{
		Page<ResourceDTO> pages = resourceApplication.pageQueryResource(queryDTO, page, pagesize);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", pages.getResult());
		result.put("Total", pages.getTotalCount());
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/findAllResourceType")
	public Map<String, Object> findAllResourceType() throws Exception{
		List<ResourceTypeDTO> resourceTypes = resourceApplication.findAllResourceType();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", resourceTypes);
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/saveResource")
	public Map<String, Object> saveResource(ResourceDTO resourceDTO) throws Exception {
		resourceApplication.saveResource(resourceDTO);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/findResourceById/{id}")
	public Map<String, Object> findResourceById(@PathVariable("id")Long id) throws Exception {
		ResourceDTO resourceDTO = resourceApplication.findResourceById(id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", resourceDTO);
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/updateResource")
	public Map<String, Object> updateResource(ResourceDTO resourceDTO) throws Exception {
		resourceApplication.updateResource(resourceDTO);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/removeResource")
	public Map<String, Object> removeResource(String ids) throws Exception {
		String[] value = ids.split(",");
		Long[] idArrs = new Long[value.length];
		for(int i=0;i<value.length;i++){
			idArrs[i] = Long.parseLong(value[i]);
		}
		
		resourceApplication.removeResource(idArrs);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/findResourceByRole")
	public Map<String, Object> findResourceByRole(Long roleId) throws Exception {
		List<ResourceDTO> resourceDTOs = resourceApplication.findResourceByRole(roleId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", resourceDTOs);
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/findResourceNotAssignToRole")
	public Map<String, Object> findResourceNotAssignToRole(ResourceDTO queryDTO,Long roleId,int page,int pagesize) throws Exception{
		Page<ResourceDTO> pages = resourceApplication.findResourceNotAssignToRole(queryDTO, roleId,page, pagesize);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", pages.getResult());
		result.put("Total", pages.getTotalCount());
		return result;
	}
}
