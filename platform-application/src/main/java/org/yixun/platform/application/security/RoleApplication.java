package org.yixun.platform.application.security;

import java.util.List;

import org.yixun.platform.application.security.dto.RoleDTO;

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
	public Page<RoleDTO> pageQueryRole(RoleDTO roleDTO,int page,int pageSize) throws Exception;
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
	 * 为用户分配角色
	 * @param userId
	 * @param roleIds
	 * @throws Exception
	 */
	public void assignRoleToUser(Long userId,Long[] roleIds) throws Exception;
	public void removeRoleForUser(Long userId,Long[] roleIds) throws Exception;
	public Page<RoleDTO> pageQueryRoleByUserId(RoleDTO roleDTO,Long userId,int page,int pageSize) throws Exception;
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
	 * 为角色添加菜单
	 * @param roleId 角色ID
	 * @param menuIds 菜单ID数组
	 * @return
	 * @throws Exception
	 */
	public void assignMenuToRole(Long roleId,Long[] menuIds) throws Exception;
	/**
	 * 删除菜单分配
	 * @param roleId 角色ID
	 * @param menuIds 菜单ID数组
	 * @return
	 * @throws Exception
	 */
	public void removeMenuForRole(Long roleId,Long[] menuIds) throws Exception;
	public List<RoleDTO> findRoleByOrgId(RoleDTO queryDTO,Long orgId) throws Exception;
	/**
	 * 获得组织及从上级组织继承的角色
	 * @param queryDTO
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public List<RoleDTO> findParentRoleByOrgId(RoleDTO queryDTO,Long orgId) throws Exception;
	/**
	 * 获得用户所拥有的角色
	 * @param queryDTO
	 * @param identityId
	 * @return
	 * @throws Exception
	 */
	public List<RoleDTO> findRoleByIdentityId(RoleDTO queryDTO, Long identityId) throws Exception;
	/**
	 * 获得用户所拥有的角色及从组织中继承的角色
	 * @param queryDTO
	 * @param orgId 上一级组织
	 * @param identityId 用户
	 * @return
	 * @throws Exception
	 */
	public List<RoleDTO> findRoleByOrgIdAndIdentityId(RoleDTO queryDTO, Long orgId,Long identityId) throws Exception;
	/**
	 * 未被分配到用户或组织的角色
	 * @param queryDTO
	 * @param orgId 组织ID
	 * @param identityId 用户ID
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	public Page<RoleDTO> findRoleByNoAssignToIdentityIdOrOrgId(RoleDTO queryDTO,Long orgId,Long identityId,int page,int pageSize) throws Exception;
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
	public List<RoleDTO> findRoleForOrgHasUserByUserId(Long userId) throws Exception;
}
