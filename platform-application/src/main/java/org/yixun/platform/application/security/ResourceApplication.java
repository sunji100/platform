package org.yixun.platform.application.security;

import java.util.List;

import org.yixun.platform.application.security.dto.ResourceDTO;
import org.yixun.platform.application.security.dto.ResourceTypeDTO;

import com.dayatang.querychannel.support.Page;

public interface ResourceApplication {
	public Page<ResourceDTO> pageQueryResource(ResourceDTO queryDTO,int page,int pagesize) throws Exception;
	public List<ResourceTypeDTO> findAllResourceType() throws Exception;
	public void saveResource(ResourceDTO resourceDTO) throws Exception;
	public ResourceDTO findResourceById(Long id) throws Exception;
	public void updateResource(ResourceDTO resourceDTO) throws Exception;
	public void removeResource(Long[] ids) throws Exception;
	public List<ResourceDTO> findResourceByRole(Long roleId) throws Exception;
	public Page<ResourceDTO> findResourceNotAssignToRole(ResourceDTO queryDTO,Long roleId,int page,int pagesize) throws Exception;
}
