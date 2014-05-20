package org.yixun.platform.application.security;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.yixun.platform.application.security.dto.ResourceTypeDTO;

import com.dayatang.querychannel.support.Page;

public interface ResourceTypeApplication {
	/**
	 * 显示所有资源类型
	 * @param resourceTypeDTO
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	public Page<ResourceTypeDTO> findResourceType(ResourceTypeDTO queryDTO,int page,int pagesize) throws Exception;
	/**
	 * 增加资源类型
	 * @param resourceTypeDTO
	 * @return
	 * @throws Exception
	 */
	public ResourceTypeDTO saveResourceType(ResourceTypeDTO resourceTypeDTO) throws Exception;
	/**
	 * 删除资源类型
	 * @param ids
	 * @throws Exception
	 */
	public void removeResourceType(Long[] ids) throws Exception;
	/**
	 * 修改资源类型
	 * @param resourceTypeDTO
	 * @return
	 * @throws Exception
	 */
	public void updateResourceType(ResourceTypeDTO resourceTypeDTO) throws Exception;
	/**
	 * 获得资源类型信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	/*@GET
	@Path("findResourceTypeById/{id}")
	@Produces({MediaType.APPLICATION_XML})*/
	public ResourceTypeDTO findResourceTypeById(@PathParam("id")Long id) throws Exception;
}
