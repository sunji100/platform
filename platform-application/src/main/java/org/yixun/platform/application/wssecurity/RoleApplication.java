package org.yixun.platform.application.wssecurity;



import org.yixun.platform.application.wssecurity.dto.RoleDTO;

import com.dayatang.querychannel.support.Page;

public interface RoleApplication {
	/**
	 * 获得所有角色
	 * @param roleDTO
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Page<RoleDTO> pageQueryRole(RoleDTO roleDTO,int page,int pagesize) throws Exception;
	/**
	 * 获得角色信息
	 * @param id 角色ID
	 * @return
	 * @throws Exception
	 */
	public RoleDTO findRoleById(Long id) throws Exception;
	/**
	 * 修改角色
	 * @param roleDTO
	 * @throws Exception
	 */
	public void updateRole(RoleDTO roleDTO) throws Exception;
	/**
	 * 删除角色
	 * @param ids
	 * @throws Exception
	 */
	public void removeRole(Long[] ids) throws Exception;
	/**
	 * 增加角色
	 * @param roleDTO
	 * @return
	 * @throws Exception
	 */
	public RoleDTO saveRole(RoleDTO roleDTO) throws Exception;
}
