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

import com.dayatang.querychannel.service.QueryChannelService;

@Named
@Transactional
public class ResourceTypeApplicationImpl implements ResourceTypeApplication {

	@Inject
	private QueryChannelService queryChannelService;
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<ResourceTypeDTO> findResourceType(ResourceTypeDTO queryDTO) throws Exception {
		StringBuilder jpql = new StringBuilder("select _resourceType from ResourceType _resourceType where 1=1");
		List<Object> conditionVals = new ArrayList<Object>();
		
		if(!StringUtils.isBlank(queryDTO.getName())){
			jpql.append(" and _resourceType.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getName()));
		}
		
		List<ResourceType> resourceTypes = queryChannelService.queryResult(jpql.toString(), conditionVals.toArray());
		
		List<ResourceTypeDTO> resourceTypeDTOs = new ArrayList<ResourceTypeDTO>();
		ResourceTypeDTO resourceTypeDTO = null;
		for (ResourceType resourceType : resourceTypes) {
			resourceTypeDTO = new ResourceTypeDTO();
			ResourceTypeBeanUtil.domainToDTO(resourceTypeDTO, resourceType);
			resourceTypeDTOs.add(resourceTypeDTO);
		}
		return resourceTypeDTOs;
	}

	@Override
	public ResourceTypeDTO saveResourceType(ResourceTypeDTO resourceTypeDTO) throws Exception {
		ResourceType resourceType = new ResourceType();
		ResourceTypeBeanUtil.dtoToDomain(resourceType, resourceTypeDTO);
		resourceType.save();
		resourceTypeDTO.setId(resourceType.getId());
		return resourceTypeDTO;
	}

	@Override
	public void removeResourceType(Long[] ids) throws Exception {
		for (Long id : ids) {
			ResourceType resourceType = ResourceType.load(ResourceType.class, id);
			resourceType.remove();
		}

	}

	@Override
	public void updateResourceType(ResourceTypeDTO resourceTypeDTO) throws Exception {
		ResourceType resourceType = ResourceType.load(ResourceType.class, resourceTypeDTO.getId());
		ResourceTypeBeanUtil.dtoToDomain(resourceType, resourceTypeDTO);

	}

}
