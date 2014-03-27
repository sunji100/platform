package org.yixun.platform.application.security;

import java.util.List;

import org.yixun.platform.application.security.dto.RoleDTO;

import com.dayatang.querychannel.support.Page;

public interface RoleApplication {
	public Page<RoleDTO> pageQueryRole(RoleDTO roleDTO,int page,int pageSize) throws Exception;
	public RoleDTO findRoleById(Long id) throws Exception;
	public void updateRole(RoleDTO roleDTO) throws Exception;
	public void removeRole(Long[] ids) throws Exception;
	public RoleDTO saveRole(RoleDTO roleDTO) throws Exception;
	public void assignRoleToUser(Long userId,Long[] roleIds) throws Exception;
	public void removeRoleForUser(Long userId,Long[] roleIds) throws Exception;
	public Page<RoleDTO> pageQueryRoleByUserId(RoleDTO roleDTO,Long userId,int page,int pageSize) throws Exception;
	public Page<RoleDTO> findNotAssignRoleByUser(RoleDTO roleDTO,Long userId,int page,int pageSize) throws Exception;
	public void assignMenuToRole(Long roleId,Long[] menuIds) throws Exception;
	public void removeMenuForRole(Long roleId,Long[] menuIds) throws Exception;
	public List<RoleDTO> findRoleByOrgId(RoleDTO queryDTO,Long orgId) throws Exception;
	public List<RoleDTO> findParentRoleByOrgId(RoleDTO queryDTO,Long orgId) throws Exception;
	public List<RoleDTO> findRoleByIdentityId(RoleDTO queryDTO, Long identityId) throws Exception;
	public List<RoleDTO> findRoleByOrgIdAndIdentityId(RoleDTO queryDTO, Long orgId,Long identityId) throws Exception;
	public Page<RoleDTO> findRoleByNoAssignToIdentityIdOrOrgId(RoleDTO queryDTO,Long orgId,Long identityId,int page,int pageSize) throws Exception;
	public void assignResourceToRole(Long roleId,Long[] resourceIds) throws Exception;
	public void removeResourceForRole(Long roleId,Long[] resourceIds) throws Exception;
}
