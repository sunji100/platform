package org.yixun.platform.application.security.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.security.ResourceApplication;
import org.yixun.platform.application.security.dto.ResourceDTO;
import org.yixun.platform.core.security.Resource;
import org.yixun.platform.core.security.Role;

@Named
@Transactional
public class ResourceApplicationImpl implements ResourceApplication {

	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<ResourceDTO> findTopLevelMenuByUser(String userAccount) {
		List<Resource> resources = Resource.findTopLevelResourceByUser(userAccount);
		
		List<ResourceDTO> resourceDTOs = new ArrayList<ResourceDTO>();
		for (Resource resource : resources) {
			ResourceDTO resourceDTO = new ResourceDTO();
			try {
				BeanUtils.copyProperties(resourceDTO, resource);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resourceDTOs.add(resourceDTO);
		}
		return resourceDTOs;
	}
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<ResourceDTO> findMenuByUser(String userAccount) {
		List<Resource> topMenuList = Resource.findTopLevelResourceByUser(userAccount);
		
		List<ResourceDTO> topMenuDTOList = new ArrayList<ResourceDTO>();
		for (Resource topMenu : topMenuList) {
			ResourceDTO topMenuDTO = new ResourceDTO();
			topMenuDTO.domainToDTO(topMenu);
			
			Long parentId = topMenu.getId();
			findAllSubMenu(parentId, topMenuDTO);
			
			topMenuDTOList.add(topMenuDTO);
		}
		return topMenuDTOList;
	}
	
	private void findAllSubMenu(Long parentId,ResourceDTO topMenuDTO){
		List<Resource> subMenuList = Resource.findResourceByParentId(parentId);
		if(null != subMenuList && subMenuList.size() != 0){
			List<ResourceDTO> subMenuDTOList = new ArrayList<ResourceDTO>();
			for (Resource subMenu : subMenuList) {
				ResourceDTO subMenuDTO = new ResourceDTO();
				subMenuDTO.domainToDTO(subMenu);
				
				findAllSubMenu(subMenu.getId(), subMenuDTO);
				subMenuDTOList.add(subMenuDTO);
			}
			topMenuDTO.setChildren(subMenuDTOList);
		}
	}
	
	private void findAllSubMenu(Resource topMenu,ResourceDTO topMenuDTO){
		Set<Resource> subMenuList = topMenu.getChilds();
		if(null != subMenuList && subMenuList.size() != 0){
			List<ResourceDTO> subMenuDTOList = new ArrayList<ResourceDTO>();
			for (Resource subMenu : subMenuList) {
				ResourceDTO subMenuDTO = new ResourceDTO();
				subMenuDTO.domainToDTO(subMenu);
				
				findAllSubMenu(subMenu, subMenuDTO);
				subMenuDTOList.add(subMenuDTO);
			}
			topMenuDTO.setChildren(subMenuDTOList);
		}
	}

}
