package org.yixun.platform.application.security;

import java.util.List;

import org.yixun.platform.application.security.dto.MenuDTO;

public interface MenuApplication {
	public List<MenuDTO> findTopLevelMenuByUser(String userAccount);
	/**
	 * 获得所有菜单的菜单树
	 * @return
	 * @throws Exception
	 */
	public List<MenuDTO> findMenu() throws Exception;
	/**
	 * 获得指定用户的菜单项
	 * @param userAccount
	 * @return
	 */
	public List<MenuDTO> findMenuByUser(String userAccount);
	/**
	 * 新增菜单
	 * @param menuDTO
	 * @throws Exception
	 */
	public void saveMenu(MenuDTO menuDTO) throws Exception;
	/**
	 * 删除菜单
	 * @param ids 要删除的菜单数组
	 * @throws Exception
	 */
	public void removeMenu(Long[] ids) throws Exception;
	/**
	 * 修改菜单信息
	 * @param menuDTO
	 * @throws Exception
	 */
	public void updateMenu(MenuDTO menuDTO) throws Exception;
	/**
	 * 获得菜单信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public MenuDTO findMenuById(Long id) throws Exception;
	/**
	 * 获得目录树
	 * @param selfId 不显示的目录ID
	 * @return
	 * @throws Exception
	 */
	public List<MenuDTO> findDiretory(Long selfId) throws Exception;
	/**
	 * 改变菜单排序
	 * @param menuDTOList
	 * @throws Exception
	 */
	public void updateMenuSortOrder(MenuDTO[] menuDTOList) throws Exception;
	public List<MenuDTO> findMenuByRole(Long roleId) throws Exception;
	/**
	 * 获得用户拥有的菜单树
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public List<MenuDTO> findMenuTreeByRole(Long roleId) throws Exception;
	public List<MenuDTO> findMenuTreeNoAssignToRole(Long roleId) throws Exception;
}
