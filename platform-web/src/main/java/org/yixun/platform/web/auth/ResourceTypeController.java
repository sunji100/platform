package org.yixun.platform.web.auth;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.security.ResourceTypeApplication;
import org.yixun.platform.application.security.dto.ResourceTypeDTO;
import org.yixun.support.exception.BizException;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/resourceType")
public class ResourceTypeController {
	
	@Inject
	private ResourceTypeApplication resourceTypeApplication;
	/**
	 * 显示所有资源类型
	 * @param resourceTypeDTO
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/pageQueryResourceType")
	public Map<String, Object> pageQueryResourceType(ResourceTypeDTO resourceTypeDTO,int page,int pagesize) throws Exception{
		Page<ResourceTypeDTO> pages = resourceTypeApplication.findResourceType(resourceTypeDTO,page,pagesize);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", pages.getResult());
		result.put("Total", pages.getTotalCount());
		return result;
	}
	/**
	 * 删除资源类型
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/removeResourceType")
	public Map<String, Object> removeResourceType(String ids) throws Exception {
		String[] value = ids.split(",");
		Long[] idArrs = new Long[value.length];
		for(int i=0;i<value.length;i++){
			idArrs[i] = Long.parseLong(value[i]);
		}
		
		resourceTypeApplication.removeResourceType(idArrs);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	/**
	 * 增加资源类型
	 * @param resourceTypeDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/saveResourceType")
	public Map<String, Object> saveResourceType(ResourceTypeDTO resourceTypeDTO) throws Exception {
		ResourceTypeDTO data = resourceTypeApplication.saveResourceType(resourceTypeDTO);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		result.put("data", data);
		return result;
	}
	/**
	 * 修改资源类型
	 * @param resourceTypeDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/updateResourceType")
	public Map<String, Object> updateResourceType(ResourceTypeDTO resourceTypeDTO) throws Exception {
		resourceTypeApplication.updateResourceType(resourceTypeDTO);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	/**
	 * 获得资源类型信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findResourceTypeById/{id}")
	public Map<String, Object> findResourceTypeById(@PathVariable("id")Long id) throws Exception {
		ResourceTypeDTO resourceTypeDTO = resourceTypeApplication.findResourceTypeById(id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", resourceTypeDTO);
		return result;
	}
}
