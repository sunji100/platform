package org.yixun.platform.web.wssecurity;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.wssecurity.ResourceApplication;
import org.yixun.platform.application.wssecurity.dto.ResourceDTO;

import com.dayatang.querychannel.support.Page;

@Controller("org.yixun.platform.web.wssecurity.ResourceController")
@RequestMapping("/ws/resource")
public class ResourceController {
	
	@Inject
	private ResourceApplication resourceApplication;
	/**
	 * 查询所有资源
	 * @param queryDTO
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/pageQueryResource")
	public Map<String, Object> pageQueryResource(ResourceDTO queryDTO,int page,int pagesize) throws Exception{
		Page<ResourceDTO> pages = resourceApplication.pageQueryResource(queryDTO, page, pagesize);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", pages.getResult());
		result.put("Total", pages.getTotalCount());
		return result;
	}
	/**
	 * 增加资源
	 * @param resourceDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/saveResource")
	public Map<String, Object> saveResource(ResourceDTO resourceDTO) throws Exception {
		resourceApplication.saveResource(resourceDTO);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	/**
	 * 获得指定资源详细信息
	 * @param id 资源ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findResourceById/{id}")
	public Map<String, Object> findResourceById(@PathVariable("id")Long id) throws Exception {
		ResourceDTO resourceDTO = resourceApplication.findResourceById(id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", resourceDTO);
		return result;
	}
	/**
	 * 更新资源信息
	 * @param resourceDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/updateResource")
	public Map<String, Object> updateResource(ResourceDTO resourceDTO) throws Exception {
		resourceApplication.updateResource(resourceDTO);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	/**
	 * 删除资源
	 * @param ids 资源ID数组
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/removeResource")
	public Map<String, Object> removeResource(Long[] resourceIds) throws Exception {
		resourceApplication.removeResource(resourceIds);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	/**
	 * 获得角色所拥有的资源
	 * @param roleId 角色ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/pageQueryResourceByRoleId")
	public Map<String, Object> pageQueryResourceByRoleId(ResourceDTO queryDTO,Long roleId,int page,int pagesize) throws Exception {
		Page<ResourceDTO> pages = resourceApplication.pageQueryResourceByRoleId(queryDTO, roleId, page, pagesize);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", pages.getResult());
		result.put("Total", pages.getTotalCount());
		return result;
	}
	
	/**
	 * 没有分配到角色的资源
	 * @param queryDTO
	 * @param roleId
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findResourceNotAssignToRole")
	public Map<String, Object> findResourceNotAssignToRole(ResourceDTO queryDTO,Long roleId,int page,int pagesize) throws Exception {
		Page<ResourceDTO> pages = resourceApplication.findResourceNotAssignToRole(queryDTO, roleId, page, pagesize);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", pages.getResult());
		result.put("Total", pages.getTotalCount());
		return result;
	}
}
