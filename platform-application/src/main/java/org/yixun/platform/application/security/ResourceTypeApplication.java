package org.yixun.platform.application.security;

import org.yixun.platform.application.security.dto.ResourceTypeDTO;

import com.dayatang.querychannel.support.Page;

public interface ResourceTypeApplication {
	public Page<ResourceTypeDTO> findResourceType(ResourceTypeDTO queryDTO,int page,int pagesize) throws Exception;
	public ResourceTypeDTO saveResourceType(ResourceTypeDTO resourceTypeDTO) throws Exception;
	public void removeResourceType(Long[] ids) throws Exception;
	public void updateResourceType(ResourceTypeDTO resourceTypeDTO) throws Exception;
	public ResourceTypeDTO findResourceTypeById(Long id) throws Exception;
}
