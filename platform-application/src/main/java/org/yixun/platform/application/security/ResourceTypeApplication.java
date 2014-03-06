package org.yixun.platform.application.security;

import java.util.List;

import org.yixun.platform.application.security.dto.ResourceTypeDTO;

public interface ResourceTypeApplication {
	public List<ResourceTypeDTO> findResourceType(ResourceTypeDTO queryDTO) throws Exception;
	public ResourceTypeDTO saveResourceType(ResourceTypeDTO resourceTypeDTO) throws Exception;
	public void removeResourceType(Long[] ids) throws Exception;
	public void updateResourceType(ResourceTypeDTO resourceTypeDTO) throws Exception;
}
