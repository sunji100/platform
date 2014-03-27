package org.yixun.platform.application.security.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.security.ResourceTypeApplication;
import org.yixun.platform.application.security.dto.ResourceTypeDTO;
import org.yixun.platform.application.security.util.ResourceTypeBeanUtil;
import org.yixun.platform.core.security.ResourceType;
import org.yixun.support.cache.annotation.Cache;
import org.yixun.support.cache.annotation.EvictCache;

import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

@Named
@Transactional
public class ResourceTypeApplicationImpl implements ResourceTypeApplication {

	@Inject
	private QueryChannelService queryChannelService;
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	@Cache
	public Page<ResourceTypeDTO> findResourceType(ResourceTypeDTO queryDTO,int page,int pagesize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _resourceType from ResourceType _resourceType where 1=1 and _resourceType.id not in(1,2)");
		List<Object> conditionVals = new ArrayList<Object>();
		
		if(!StringUtils.isBlank(queryDTO.getName())){
			jpql.append(" and _resourceType.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getName()));
		}
		
		Page<ResourceType> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pagesize);
		
		List<ResourceTypeDTO> resourceTypeDTOs = new ArrayList<ResourceTypeDTO>();
		ResourceTypeDTO resourceTypeDTO = null;
		for (ResourceType resourceType : pages.getResult()) {
			resourceTypeDTO = new ResourceTypeDTO();
			ResourceTypeBeanUtil.domainToDTO(resourceTypeDTO, resourceType);
			resourceTypeDTOs.add(resourceTypeDTO);
		}
		return new Page<ResourceTypeDTO>(pages.getCurrentPageNo(),pages.getTotalCount(),pages.getPageSize(),resourceTypeDTOs);
	}

	@Override
	@EvictCache
	public ResourceTypeDTO saveResourceType(ResourceTypeDTO resourceTypeDTO) throws Exception {
		ResourceType resourceType = new ResourceType();
		ResourceTypeBeanUtil.dtoToDomain(resourceType, resourceTypeDTO);
		resourceType.save();
		resourceTypeDTO.setId(resourceType.getId());
		return resourceTypeDTO;
	}

	@Override
	@EvictCache
	public void removeResourceType(Long[] ids) throws Exception {
		for (Long id : ids) {
			ResourceType resourceType = ResourceType.load(ResourceType.class, id);
			resourceType.remove();
		}

	}

	@Override
	@EvictCache
	public void updateResourceType(ResourceTypeDTO resourceTypeDTO) throws Exception {
		ResourceType resourceType = ResourceType.load(ResourceType.class, resourceTypeDTO.getId());
		ResourceTypeBeanUtil.dtoToDomain(resourceType, resourceTypeDTO);

	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public ResourceTypeDTO findResourceTypeById(Long id) throws Exception {
		ResourceType resourceType = ResourceType.load(ResourceType.class, id);
		ResourceTypeDTO resourceTypeDTO = new ResourceTypeDTO();
		ResourceTypeBeanUtil.domainToDTO(resourceTypeDTO, resourceType);
		return resourceTypeDTO;
	}

}
