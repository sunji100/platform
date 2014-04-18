package org.yixun.platform.application.security;

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
	public ResourceTypeDTO findResourceTypeById(Long id) throws Exception;
}
