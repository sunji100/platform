package org.yixun.platform.web.auth;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.security.MenuApplication;
import org.yixun.platform.application.security.dto.MenuDTO;
import org.yixun.platform.web.auth.util.AuthDetailUtil;

@Controller
@RequestMapping("/menu")
public class MenuController {
	
	@Inject
	private MenuApplication menuApplication;
	
	@ResponseBody
	@RequestMapping("/findTopLevelMenuByUser")
	public Map<String, Object> findTopLevelMenuByUser(){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<MenuDTO> topMenuList = menuApplication.findTopLevelMenuByUser(userDetails.getUsername());
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", topMenuList);
		return result;
	}
	/**
	 * 获得登录用户的菜单项
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/findMenuByUser")
	public Map<String, Object> findMenuByUser(HttpServletRequest request){
		
		List<MenuDTO> menuList = menuApplication.findMenuByUser(AuthDetailUtil.getLoginName());
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", menuList);
		return result;
	}
	
	/**
	 * 获得目录树
	 * @param selfId 不显示的目录ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findDiretoryTree")
	public List<MenuDTO> findDiretoryTree(Long selfId) throws Exception{
		
		List<MenuDTO> diretoryList = menuApplication.findDiretory(selfId);
		
		return diretoryList;
	}
	
	/**
	 * 获得全部菜单的菜单树
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findMenuTree")
	public Map<String, Object> findMenuTree() throws Exception{
		
		List<MenuDTO> menuList = menuApplication.findMenu();
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", menuList);
		return result;
	}
	
	/**
	 * 获得角色所拥有的菜单树
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findMenuTreeByRole")
	public Map<String, Object> findMenuTreeByRole(Long roleId) throws Exception{
		
		List<MenuDTO> menuList = menuApplication.findMenuTreeByRole(roleId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", menuList);
		return result;
	}
	/**
	 * 新增菜单
	 * @param menuDTO 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/saveMenu")
	public Map<String, Object> saveMenu(MenuDTO menuDTO) throws Exception{
		
		menuApplication.saveMenu(menuDTO);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/removeMenu")
	public Map<String, Object> removeMenu(String ids) throws Exception{
		
		String[] value = ids.split(",");
		Long[] idArrs = new Long[value.length];
		for(int i=0;i<value.length;i++){
			idArrs[i] = Long.parseLong(value[i]);
		}
		
		menuApplication.removeMenu(idArrs);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	/**
	 * 修改菜单信息
	 * @param menuDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/updateMenu")
	public Map<String, Object> updateMenu(MenuDTO menuDTO) throws Exception{
		
		menuApplication.updateMenu(menuDTO);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		return result;
	}
	
	/**
	 * 获得菜单信息
	 * @param id  要获得的菜单id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findMenuById/{id}")
	public Map<String, Object> findMenuById(@PathVariable("id")Long id) throws Exception{
		
		MenuDTO menuDTO = menuApplication.findMenuById(id);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", menuDTO);
		return result;
	}
	
	/**
	 * 菜单排序 
	 * @param menuDTOList
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/updateMenuSortOrder",method={RequestMethod.POST})
	public Map<String, Object> updateMenuSortOrder(@RequestBody MenuDTO[] menuDTOList) throws Exception {
		menuApplication.updateMenuSortOrder(menuDTOList);
		
		Map<String, Object> result = new HashMap<String, Object>();
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/findMenuByRole")
	public Map<String, Object> findMenuByRole(Long roleId) throws Exception {
		List<MenuDTO> menuList = menuApplication.findMenuByRole(roleId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", menuList);
		return result;
	}

	@ResponseBody
	@RequestMapping("/findMenuTreeNoAssignToRole")
	public Map<String, Object> findMenuTreeNoAssignToRole(Long roleId) throws Exception {
		List<MenuDTO> menuList = menuApplication.findMenuTreeNoAssignToRole(roleId);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", menuList);
		return result;
	}
	
	/**
	 * 获得所有图标资源
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getIconNames")
	public Map<String,Object> getIconNames(HttpServletRequest request) {
		Map<String, Object> dataMap = new HashMap<String,Object>();
		Collection<String> filenames = new ArrayList<String>();
		String realPath = request.getSession().getServletContext().getRealPath("/images/icons/menu");
		File iconFiles = new File(realPath);
		File[] acceptFiles = getAcceptFiles(iconFiles);
		for (File iconFile : acceptFiles) {
			filenames.add(iconFile.getPath().substring(iconFile.getPath().indexOf("images")) //
					.replaceAll("\\\\", "/"));
		}
		dataMap.put("data", filenames);
		return dataMap;
	}
	
	private File[] getAcceptFiles(File iconFiles) {
		return iconFiles.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				String suffix = pathname.getPath().substring(pathname.getPath().lastIndexOf(".") + 1);
				if (suffix.equalsIgnoreCase("gif") || suffix.equalsIgnoreCase("png") || suffix.equalsIgnoreCase("jpg")) {
					return true;
				}
				return false;
			}
		});
	}
	
	
}
