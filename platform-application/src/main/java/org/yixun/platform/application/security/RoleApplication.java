package org.yixun.platform.application.security;

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

}
