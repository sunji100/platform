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
	/**
	 * 查询用户所拥有的角色
	 * @param roleDTO
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Page<RoleDTO> pageQueryRoleByUserId(RoleDTO roleDTO,Long userId,int page,int pageSize) throws Exception;
	/**
	 * 为用户分配角色
	 * @param userId
	 * @param roleIds
	 * @throws Exception
	 */
	public void assignRoleToUser(Long userId,Long[] roleIds) throws Exception;
	/**
	 * 删除用户分配的角色
	 * @param userId
	 * @param roleIds
	 * @throws Exception
	 */
	public void removeRoleForUser(Long userId,Long[] roleIds) throws Exception;
	/**
	 * 没有分配给指定用户的角色
	 * @param roleDTO
	 * @param userId 用户ID
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Page<RoleDTO> findNotAssignRoleByUser(RoleDTO roleDTO,Long userId,int page,int pageSize) throws Exception;
	/**
	 * 为角色分配资源
	 * @param roleId 角色ID
	 * @param resourceIds
	 * @throws Exception
	 */
	public void assignResourceToRole(Long roleId,Long[] resourceIds) throws Exception;
	/**
	 * 删除分配的资源
	 * @param roleId 角色ID
	 * @param resourceIds
	 * @throws Exception
	 */
	public void removeResourceForRole(Long roleId,Long[] resourceIds) throws Exception;
}
