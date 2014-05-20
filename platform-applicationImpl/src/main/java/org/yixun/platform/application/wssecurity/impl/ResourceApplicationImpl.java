package org.yixun.platform.application.wssecurity.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.wssecurity.ResourceApplication;
import org.yixun.platform.application.wssecurity.dto.ResourceDTO;
import org.yixun.platform.application.wssecurity.util.ResourceBeanUtil;
import org.yixun.platform.core.wssecurity.WsResource;
import org.yixun.platform.core.wssecurity.WsRole;

import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

@Named("org.yixun.platform.application.wssecurity.impl.ResourceApplicationImpl")
@Transactional
public class ResourceApplicationImpl implements ResourceApplication {

	@Inject
	private QueryChannelService queryChannelService;

	@Override
	public Page<ResourceDTO> pageQueryResource(ResourceDTO queryDTO, int page, int pagesize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _resource from WsResource _resource where 1=1");
		List<Object> conditionVals = new ArrayList<Object>();

		if (!StringUtils.isBlank(queryDTO.getText())) {
			jpql.append(" and _resource.text like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getText()));
		}

		Page<WsResource> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pagesize);

		List<ResourceDTO> resourceDTOs = new ArrayList<ResourceDTO>();
		ResourceDTO resourceDTO = null;
		for (WsResource resource : pages.getResult()) {
			resourceDTO = new ResourceDTO();
			ResourceBeanUtil.domainToDTO(resourceDTO, resource);
			resourceDTOs.add(resourceDTO);
		}
		return new Page(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), resourceDTOs);
	}

	@Override
	public void saveResource(ResourceDTO resourceDTO) throws Exception {
		WsResource resource = new WsResource();
		ResourceBeanUtil.dtoToDomain(resource,resourceDTO);
		resource.save();
	}

	@Override
	public ResourceDTO findResourceById(Long id) throws Exception {
		WsResource resource = WsResource.load(WsResource.class, id);
		ResourceDTO resourceDTO = new ResourceDTO();
		ResourceBeanUtil.domainToDTO(resourceDTO, resource);
		return resourceDTO;
	}

	@Override
	public void updateResource(ResourceDTO resourceDTO) throws Exception {
		WsResource resource = WsResource.load(WsResource.class, resourceDTO.getId());
		ResourceBeanUtil.dtoToDomain(resource,resourceDTO);

	}

	@Override
	public void removeResource(Long[] ids) throws Exception {
		for (Long id : ids) {
			WsResource resource = WsResource.load(WsResource.class, id);
			resource.remove();
		}

	}

	@Override
	public Page<ResourceDTO> pageQueryResourceByRoleId(ResourceDTO queryDTO, Long roleId, int page, int pageSize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _resource from WsResource _resource inner join _resource.roles _role where 1=1");
		List<Object> conditionVals = new ArrayList<Object>();
		
		if(null != roleId){
			jpql.append(" and _role.id = ?");
			conditionVals.add(roleId);
		}
		
		if (!StringUtils.isBlank(queryDTO.getText())) {
			jpql.append(" and _resource.text like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getText()));
		}
		
		Page<WsResource> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pageSize);
		
		List<ResourceDTO> resourceDTOs = new ArrayList<ResourceDTO>();
		ResourceDTO resourceDTO = null;
		for (WsResource resource : pages.getResult()) {
			resourceDTO = new ResourceDTO();
			ResourceBeanUtil.domainToDTO(resourceDTO, resource);
			resourceDTOs.add(resourceDTO);
		}
		return new Page(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), resourceDTOs);
	}

	@Override
	public Page<ResourceDTO> findResourceNotAssignToRole(ResourceDTO queryDTO, Long roleId, int page, int pagesize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _resource from WsResource _resource where 1=1");
		List<Object> conditionVals = new ArrayList<Object>();
		
		if(null != roleId){
			jpql.append(" and not exists (select _innerresource from WsResource _innerresource inner join _innerresource.roles _role where _role.id = ? and _innerresource.id = _resource.id)");
			conditionVals.add(roleId);
		}
		
		Page<WsResource> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pagesize);

		List<ResourceDTO> resourceDTOs = new ArrayList<ResourceDTO>();
		ResourceDTO resourceDTO = null;
		for (WsResource resource : pages.getResult()) {
			resourceDTO = new ResourceDTO();
			ResourceBeanUtil.domainToDTO(resourceDTO, resource);
			resourceDTOs.add(resourceDTO);
		}
		return new Page(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), resourceDTOs);
	}

	@Override
	public Map<String, List<String>> getAllResourceAndRoles() throws Exception {
		List<WsResource> resources = WsResource.findAll(WsResource.class);
		
		Map<String, List<String>> resourceAndRoles = new HashMap<String, List<String>>();
		if(null != resources){
			for (WsResource resource : resources) {
				Set<WsRole> roles = resource.getRoles();
				
				List<String> roleNames = new ArrayList<String>();
				if(null != roles){
					for (WsRole role : roles) {
						roleNames.add(role.getName());
					}
				}
				resourceAndRoles.put(resource.getIdentifier(), roleNames);
			}
		}
		return resourceAndRoles;

	}

}
