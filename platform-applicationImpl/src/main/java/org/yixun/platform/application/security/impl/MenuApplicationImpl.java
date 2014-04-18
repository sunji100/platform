package org.yixun.platform.application.security.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.security.MenuApplication;
import org.yixun.platform.application.security.dto.MenuDTO;
import org.yixun.platform.application.security.util.MenuBeanUtil;
import org.yixun.platform.core.security.Resource;
import org.yixun.platform.core.security.ResourceType;

@Named
@Transactional
public class MenuApplicationImpl implements MenuApplication {

	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<MenuDTO> findTopLevelMenuByUser(String userAccount) {
		List<Resource> menus = Resource.findTopLevelMenuByUser(userAccount);
		
		List<MenuDTO> menuDTOs = new ArrayList<MenuDTO>();
		for (Resource menu : menus) {
			MenuDTO menuDTO = new MenuDTO();
			MenuBeanUtil.domainToDTO(menuDTO, menu);
			menuDTOs.add(menuDTO);
		}
		return menuDTOs;
	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<MenuDTO> findMenuByUser(String userAccount) {
		List<Resource> topMenuList = Resource.findTopLevelMenuByUser(userAccount);
		
		List<MenuDTO> topMenuDTOList = new ArrayList<MenuDTO>();
		for (Resource topMenu : topMenuList) {
			MenuDTO topMenuDTO = new MenuDTO();
			MenuBeanUtil.domainToDTO(topMenuDTO, topMenu);
			
			Long parentId = topMenu.getId();
			findAllSubMenuByUser(userAccount,parentId, topMenuDTO);
			
			topMenuDTOList.add(topMenuDTO);
		}
		return topMenuDTOList;
	}
	/**
	 * 获得指定用户所有子菜单项
	 * @param userAccount  用户名
	 * @param parentId  父菜单ID
	 * @param topMenuDTO 父菜单对象
	 */
	private void findAllSubMenuByUser(String userAccount,Long parentId,MenuDTO topMenuDTO){
		List<Resource> subMenuList = Resource.findMenuByUserAndParentId(userAccount, parentId);
		if(null != subMenuList && subMenuList.size() != 0){
			List<MenuDTO> subMenuDTOList = new ArrayList<MenuDTO>();
			for (Resource subMenu : subMenuList) {
				MenuDTO subMenuDTO = new MenuDTO();
				MenuBeanUtil.domainToDTO(subMenuDTO, subMenu);
				
				findAllSubMenuByUser(userAccount,subMenu.getId(), subMenuDTO);
				subMenuDTOList.add(subMenuDTO);
			}
			topMenuDTO.setChildren(subMenuDTOList);
		}
	}
	
	/**
	 * 获得全部菜单的菜单树
	 */
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<MenuDTO> findMenu() throws Exception {
		List<Resource> topMenuList = Resource.findTopLevelMenu();
		
		List<MenuDTO> topMenuDTOList = new ArrayList<MenuDTO>();
		for (Resource topMenu : topMenuList) {
			MenuDTO topMenuDTO = new MenuDTO();
			MenuBeanUtil.domainToDTO(topMenuDTO, topMenu);
			
			Long parentId = topMenu.getId();
			findAllSubMenu(parentId, topMenuDTO);
			
			topMenuDTOList.add(topMenuDTO);
		}
		return topMenuDTOList;
	}
	
	private void findAllSubMenu(Long parentId,MenuDTO topMenuDTO){
		List<Resource> subMenuList = Resource.findMenuByParentId(parentId);
		if(null != subMenuList && subMenuList.size() != 0){
			List<MenuDTO> subMenuDTOList = new ArrayList<MenuDTO>();
			for (Resource subMenu : subMenuList) {
				MenuDTO subMenuDTO = new MenuDTO();
				MenuBeanUtil.domainToDTO(subMenuDTO, subMenu);
				
				findAllSubMenu(subMenu.getId(), subMenuDTO);
				subMenuDTOList.add(subMenuDTO);
			}
			topMenuDTO.setChildren(subMenuDTOList);
		}
	}
	
//	private void findAllSubMenu(Resource topMenu,MenuDTO topMenuDTO){
//		Set<Resource> subMenuList = topMenu.getChilds();
//		if(null != subMenuList && subMenuList.size() != 0){
//			List<MenuDTO> subMenuDTOList = new ArrayList<MenuDTO>();
//			for (Resource subMenu : subMenuList) {
//				MenuDTO subMenuDTO = new MenuDTO();
//				MenuBeanUtil.domainToDTO(subMenuDTO, subMenu);
//				
//				findAllSubMenu(subMenu, subMenuDTO);
//				subMenuDTOList.add(subMenuDTO);
//			}
//			topMenuDTO.setChildren(subMenuDTOList);
//		}
//	}

	@Override
	public void saveMenu(MenuDTO menuDTO) throws Exception {
		Resource resource = new Resource();
		MenuBeanUtil.dtoToDomain(resource, menuDTO);
		
		if(null == menuDTO.getParentId()){
			resource.setLevel("1");
		} else {
			Resource parent = Resource.load(Resource.class, menuDTO.getParentId());
			resource.getParents().add(parent);
		}
		
		ResourceType resourceType = ResourceType.load(ResourceType.class, menuDTO.getMenuTypeId());
		resource.setResourceType(resourceType);
		
		resource.save();
	}

	@Override
	public void removeMenu(Long[] ids) throws Exception {
		for (Long id : ids) {
			Resource resource = Resource.load(Resource.class, id);
			resource.remove();
		}
		
	}

	@Override
	public void updateMenu(MenuDTO menuDTO) throws Exception {
		Resource menu = Resource.load(Resource.class, menuDTO.getId());
		MenuBeanUtil.dtoToDomain(menu, menuDTO);
		
		if(null != menuDTO.getParentId()){
			Set<Resource> parents = new HashSet<Resource>();
			Resource parent = Resource.load(Resource.class, menuDTO.getParentId());
			parents.add(parent);
			menu.setParents(parents);
			
			menu.setLevel("2");//设置菜单上一级目录后，level设为不是1
		} else {
			if(menu.getParents().size() != 0){
				menu.setParents(null);
				menu.setLevel("1");//没有上一级目录后，level设为1
			}
		}
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public MenuDTO findMenuById(Long id) throws Exception {
		Resource menu = Resource.load(Resource.class, id);
		
		MenuDTO menuDTO = new MenuDTO();
		MenuBeanUtil.domainToDTO(menuDTO, menu);
		
		ResourceType resourceType = menu.getResourceType();
		if(null != resourceType){
			menuDTO.setMenuTypeId(resourceType.getId());
		}
		//获得上一级目录信息
		Set<Resource> parents = menu.getParents();
		for (Resource parent : parents) {
			menuDTO.setParentId(parent.getId());
			menuDTO.setParentText(parent.getName());
		}
		
		return menuDTO;
	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<MenuDTO> findDiretory(Long selfId) throws Exception {
		List<Resource> topDiretoryList = Resource.findTopLevelDiretory();
		
		List<MenuDTO> topDiretoryDTOList = new ArrayList<MenuDTO>();
		for (Resource topDiretory : topDiretoryList) {
			if(selfId != topDiretory.getId()){
				MenuDTO topDiretoryDTO = new MenuDTO();
				MenuBeanUtil.domainToDTO(topDiretoryDTO, topDiretory);
				
				Long parentId = topDiretory.getId();
				findAllSubDiretory(selfId,parentId, topDiretoryDTO);
				
				topDiretoryDTOList.add(topDiretoryDTO);
			}
		}
		return topDiretoryDTOList;
	}
	
	/**
	 * 获得父目录的所有子目录
	 * @param selfId 不显示的目录
	 * @param parentId 父目录ID
	 * @param topDiretoryDTO 父目录对象
	 */
	private void findAllSubDiretory(Long selfId,Long parentId,MenuDTO topDiretoryDTO){
		List<Resource> subDiretoryList = Resource.findDiretoryByParentId(parentId);
		if(null != subDiretoryList && subDiretoryList.size() != 0){
			List<MenuDTO> subDiretoryDTOList = new ArrayList<MenuDTO>();
			for (Resource subDiretory : subDiretoryList) {
				if(selfId != subDiretory.getId()){
					MenuDTO subDiretoryDTO = new MenuDTO();
					MenuBeanUtil.domainToDTO(subDiretoryDTO, subDiretory);
					
					findAllSubMenu(subDiretory.getId(), subDiretoryDTO);
					subDiretoryDTOList.add(subDiretoryDTO);
				}
			}
			topDiretoryDTO.setChildren(subDiretoryDTOList);
		}
	}

	@Override
	public void updateMenuSortOrder(MenuDTO[] menuDTOList) throws Exception {
		for (MenuDTO menuDTO : menuDTOList) {
			Resource menu = Resource.load(Resource.class, menuDTO.getId());
			menu.setSortOrder(menuDTO.getSortOrder());
		}
		
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<MenuDTO> findMenuByRole(Long roleId) throws Exception {
		List<Resource> resources = Resource.findMenuByRole(roleId);
		
		List<MenuDTO> menuList = new ArrayList<MenuDTO>();
		MenuDTO menuDTO = null;
		for (Resource resource : resources) {
			menuDTO = new MenuDTO();
			MenuBeanUtil.domainToDTO(menuDTO, resource);
			menuList.add(menuDTO);
		}
		
		return menuList;
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<MenuDTO> findMenuTreeNoAssignToRole(Long roleId) throws Exception {
		List<Resource> topMenuList = Resource.findTopLevelMenu();
		
		List<MenuDTO> topMenuDTOList = new ArrayList<MenuDTO>();
		for (Resource topMenu : topMenuList) {
			MenuDTO topMenuDTO = new MenuDTO();
			MenuBeanUtil.domainToDTO(topMenuDTO, topMenu);
			
			Long parentId = topMenu.getId();
			if(findSubMenuNoAssignToRole(roleId,parentId, topMenuDTO)){
				topMenuDTOList.add(topMenuDTO);
			}
		}
		return topMenuDTOList;
	}
	
	private boolean findSubMenuNoAssignToRole(Long roleId,Long parentId,MenuDTO topMenuDTO){
		List<Resource> subMenuList = Resource.findMenuByParentIdAndNoAssignToRole(parentId, roleId);
		if(null != subMenuList && subMenuList.size() != 0){
			List<MenuDTO> subMenuDTOList = new ArrayList<MenuDTO>();
			for (Resource subMenu : subMenuList) {
				MenuDTO subMenuDTO = new MenuDTO();
				MenuBeanUtil.domainToDTO(subMenuDTO, subMenu);
				//没有下一级，并且为目录时，不添加子菜单
				if(findSubMenuNoAssignToRole(roleId,subMenu.getId(), subMenuDTO) && subMenu.getResourceType().getId() == 1){
					subMenuDTOList.add(subMenuDTO);
				}
			}
			//下一级都是目录
			if(subMenuDTOList.size() == 0){
				return false;
			}
			topMenuDTO.setChildren(subMenuDTOList);
			return true;
		}
		return false;
	}

	/**
	 * 获得角色所拥有的菜单树
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<MenuDTO> findMenuTreeByRole(Long roleId) throws Exception {
		List<Resource> resources = Resource.findTopLevelMenuByRole(roleId);
		
		List<MenuDTO> menuList = new ArrayList<MenuDTO>();
		MenuDTO menuDTO = null;
		for (Resource resource : resources) {
			menuDTO = new MenuDTO();
			MenuBeanUtil.domainToDTO(menuDTO, resource);
			
			Long parentId = resource.getId();
			findAllSubMenuByRole(roleId, parentId, menuDTO);
			menuList.add(menuDTO);
		}
		return menuList;
	}
	
	/**
	 * 获得用户拥有顶级菜单相应的子菜单
	 * @param roleId
	 * @param parentId
	 * @param topMenuDTO
	 */
	private void findAllSubMenuByRole(Long roleId,Long parentId,MenuDTO topMenuDTO){
		List<Resource> subMenuList = Resource.findMenuByParentIdAndRoleId(parentId, roleId);
		if(null != subMenuList && subMenuList.size() != 0){
			List<MenuDTO> subMenuDTOList = new ArrayList<MenuDTO>();
			for (Resource subMenu : subMenuList) {
				MenuDTO subMenuDTO = new MenuDTO();
				MenuBeanUtil.domainToDTO(subMenuDTO, subMenu);
				
				findAllSubMenuByRole(roleId,subMenu.getId(), subMenuDTO);
				subMenuDTOList.add(subMenuDTO);
			}
			topMenuDTO.setChildren(subMenuDTOList);
		}
	}

	

}
