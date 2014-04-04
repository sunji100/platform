package org.yixun.platform.application.security;

import java.util.List;

import org.yixun.platform.application.security.dto.MenuDTO;

public interface MenuApplication {
	public List<MenuDTO> findTopLevelMenuByUser(String userAccount);
	public List<MenuDTO> findMenu() throws Exception;
	public List<MenuDTO> findMenuByUser(String userAccount);
	public void saveMenu(MenuDTO menuDTO) throws Exception;
	public void removeMenu(Long[] ids) throws Exception;
	public void updateMenu(MenuDTO menuDTO) throws Exception;
	public MenuDTO findMenuById(Long id) throws Exception;
	public List<MenuDTO> findDiretory(Long selfId) throws Exception;
	public void updateMenuSortOrder(MenuDTO[] menuDTOList) throws Exception;
	public List<MenuDTO> findMenuByRole(Long roleId) throws Exception;
	public List<MenuDTO> findMenuTreeByRole(Long roleId) throws Exception;
	public List<MenuDTO> findMenuTreeNoAssignToRole(Long roleId) throws Exception;
}
