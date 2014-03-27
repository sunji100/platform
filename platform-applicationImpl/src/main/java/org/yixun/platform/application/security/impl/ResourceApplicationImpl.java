package org.yixun.platform.application.security.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.security.ResourceApplication;
import org.yixun.platform.application.security.dto.ResourceDTO;
import org.yixun.platform.application.security.dto.ResourceTypeDTO;
import org.yixun.platform.application.security.util.ResourceBeanUtil;
import org.yixun.platform.application.security.util.ResourceTypeBeanUtil;
import org.yixun.platform.core.security.Resource;
import org.yixun.platform.core.security.ResourceType;

import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

@Named
@Transactional
public class ResourceApplicationImpl implements ResourceApplication {
	
	@Inject
	private QueryChannelService queryChannelService;

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public Page<ResourceDTO> pageQueryResource(ResourceDTO queryDTO, int page, int pagesize) throws Exception {
		StringBuilder jpql = new StringBuilder("select new org.yixun.platform.application.security.dto.ResourceDTO(_resource,_resourceType.name) from Resource _resource left join _resource.resourceType _resourceType where 1=1 "
				+ "and (_resource.resourceType is null or _resource.resourceType not in(1,2))");
		List<Object> conditionVals = new ArrayList<Object>();
		
		if(null != queryDTO.getResourceTypeId()){
			jpql.append(" and _resourceType.id = ?");
			conditionVals.add(queryDTO.getResourceTypeId());
		}
		
		if(!StringUtils.isBlank(queryDTO.getText())){
			jpql.append(" and _resource.text like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getText()));
		}
		
		
		Page<ResourceDTO> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pagesize);
		
		return pages;
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<ResourceTypeDTO> findAllResourceType() throws Exception {
		String jpql = "select _resourceType from ResourceType _resourceType where _resourceType.id not in(1,2)";
		List<ResourceType> resourceTypes = queryChannelService.queryResult(jpql, null);
		//List<ResourceType> resourceTypes = ResourceType.findAll(ResourceType.class);
		List<ResourceTypeDTO> resourceTypeDTOs = new ArrayList<ResourceTypeDTO>();
		ResourceTypeDTO resourceTypeDTO = null;
		for(ResourceType resourceType:resourceTypes){
			resourceTypeDTO = new ResourceTypeDTO();
			ResourceTypeBeanUtil.domainToDTO(resourceTypeDTO, resourceType);
			resourceTypeDTOs.add(resourceTypeDTO);
		}
		return resourceTypeDTOs;
	}

	@Override
	public void saveResource(ResourceDTO resourceDTO) throws Exception {
		
		Resource resource = new Resource();
		ResourceBeanUtil.dtoToDomain(resource,resourceDTO);
		
		if(null != resourceDTO.getParentIds()){
			String[] parentIds = resourceDTO.getParentIds().split(",");
			for (String parentId : parentIds) {
				Resource parentResource = Resource.load(Resource.class, Long.parseLong(parentId));
				resource.getParents().add(parentResource);
			}
		}
		
		if(null != resourceDTO.getResourceTypeId() && 0 != resourceDTO.getResourceTypeId()){
			ResourceType resourceType = ResourceType.load(ResourceType.class, resourceDTO.getResourceTypeId());
			resource.setResourceType(resourceType);
		}
		
		resource.save();
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public ResourceDTO findResourceById(Long id) throws Exception {
		Resource resource = Resource.load(Resource.class, id);
		
		ResourceDTO resourceDTO = new ResourceDTO();
		ResourceBeanUtil.domainToDTO(resourceDTO, resource);
		ResourceType resourceType = resource.getResourceType();
		if(null != resourceType){
			resourceDTO.setResourceTypeId(resourceType.getId());
		}
		
		Set<Resource> parentSet = resource.getParents();
		List<Long> parentIdList = new ArrayList<Long>();
		for (Resource parentResource : parentSet) {
			parentIdList.add(parentResource.getId());
		}
		String parentIds = StringUtils.join(parentIdList, ",");
		resourceDTO.setParentIds(parentIds);
		
		return resourceDTO;
	}

	@Override
	public void updateResource(ResourceDTO resourceDTO) throws Exception {
		Resource resource = Resource.load(Resource.class, resourceDTO.getId());
		ResourceBeanUtil.dtoToDomain(resource, resourceDTO);
		
		if(null != resourceDTO.getResourceTypeId() && 0 != resourceDTO.getResourceTypeId()){
			ResourceType resourceType = ResourceType.load(ResourceType.class, resourceDTO.getResourceTypeId());
			resource.setResourceType(resourceType);
		} else {
			resource.setResourceType(null);
		}
		
		if(null != resourceDTO.getParentIds()){
			String newParentIds = resourceDTO.getParentIds();
			String[] newParentIdStrArr = newParentIds.split(",");
//			Long[] newParentIdArr = new Long[newParentIdStrArr.length];
			Set<Resource> parentResourceSet = new HashSet<Resource>();
			for(int i=0;i<newParentIdStrArr.length;i++){
//				newParentIdArr[i] = Long.parseLong(newParentIdStrArr[i]);
				Resource parentResource = Resource.load(Resource.class, Long.parseLong(newParentIdStrArr[i]));
				parentResourceSet.add(parentResource);
			}
			resource.setParents(parentResourceSet);
//			
//			Set<Resource> oldParentSet = resource.getParents();
//			List<Long> oldParentIdList = new ArrayList<Long>();
//			for (Resource parentResource : oldParentSet) {
//				oldParentIdList.add(parentResource.getId());
//			}
			
			
			
			
		}
	}

	@Override
	public void removeResource(Long[] ids) throws Exception {
		for (Long id : ids) {
			Resource resource = Resource.load(Resource.class, id);
			resource.remove();
		}
		
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<ResourceDTO> findResourceByRole(Long roleId) throws Exception {
		List<Resource> resourceList = Resource.findResourceByRole(roleId);
		
		List<ResourceDTO> resourcerDTOList = new ArrayList<ResourceDTO>();
		ResourceDTO resourceDTO = null;
		for (Resource resource : resourceList) {
			resourceDTO = new ResourceDTO();
			ResourceBeanUtil.domainToDTO(resourceDTO, resource);
			resourcerDTOList.add(resourceDTO);
		}
		return resourcerDTOList;
	}

	@Override
	public Page<ResourceDTO> findResourceNotAssignToRole(ResourceDTO queryDTO, Long roleId, int page, int pagesize) throws Exception {
		StringBuilder jpql = new StringBuilder("select new org.yixun.platform.application.security.dto.ResourceDTO(_resource,_resourceType.name) from Resource _resource left join _resource.resourceType _resourceType where 1=1"
				+ " and (_resource.resourceType is null or _resource.resourceType not in(1,2))");
		List<Object> conditionVals = new ArrayList<Object>();
		
		if(null != roleId){
			jpql.append(" and _resource.id not in(select _resource.id from Role _role inner join _role.resources _resource where 1=1"
					+ " and (_resource.resourceType is null or _resource.resourceType not in(1,2)) and _role.id = ?)");
			conditionVals.add(roleId);
		}
		
		if(null != queryDTO.getResourceTypeId()){
			jpql.append(" and _resourceType.id = ?");
			conditionVals.add(queryDTO.getResourceTypeId());
		}
		
		if(!StringUtils.isBlank(queryDTO.getText())){
			jpql.append(" and _resource.text like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getText()));
		}
		
		
		Page<ResourceDTO> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pagesize);
		
		return pages;
	}

}
