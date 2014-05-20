package org.yixun.platform.application.security;

import java.util.List;

import org.yixun.platform.application.crud.dto.ProductDTO;
import org.yixun.platform.application.security.dto.IdentityDTO;

import com.dayatang.querychannel.support.Page;

public interface IdentityApplication {
	public IdentityDTO getIdentity(String userAccount);
	public List<IdentityDTO> findAllIdentity();
	/**
	 * 获得所有未分配到指定角色的用户
	 * @param identityDTO
	 * @param roleId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Page<IdentityDTO> findNotAssignUserByRole(IdentityDTO identityDTO,Long roleId,int page,int pageSize) throws Exception; 
	public Page<IdentityDTO> pageQueryIdentity(IdentityDTO identityDTO,int page,int pageSize);
	/**
	 * 获得已分配到角色上的用户
	 * @param identityDTO
	 * @param roleId 角色ID
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Page<IdentityDTO> pageQueryIdentityByRoleId(IdentityDTO identityDTO,Long roleId,int page,int pageSize) throws Exception;
	/**
	 * 新增用户
	 * @param identityDTO
	 * @return
	 * @throws Exception
	 */
	public IdentityDTO saveIdentity(IdentityDTO identityDTO) throws Exception;
	/**
	 * 修改用户信息
	 * @param identityDTO
	 */
	public void updateIdentity(IdentityDTO identityDTO);
	/**
	 * 获得用户信息
	 * @param id
	 * @return
	 */
	public IdentityDTO findIdentityById(Long id);
	/**
	 * 删除用户
	 * @param ids 要删除的用户id数组
	 */
	public void removeIdentity(Long[] ids);
	public void assignUserToRole(Long roleId,Long[] identityIds) throws Exception;
	
	/**
	 * 删除指定角色已分配的用户
	 * @param roleId 角色ID
	 * @param identityIds 用户ID数组
	 * @return
	 * @throws Exception
	 */
	public void removeUserForRole(Long roleId,Long[] identityIds) throws Exception;
	/**
	 * 获得指定组织下所有用户
	 * @param queryDTO
	 * @param page
	 * @param pageSize
	 * @param orgId 组织ID
	 * @return
	 * @throws Exception
	 */
	public Page<IdentityDTO> pageQueryIdentityByOrgId(IdentityDTO queryDTO, int page, int pageSize,Long orgId) throws Exception;
	public List<IdentityDTO> findIdentityByOrgId(Long orgId) throws Exception;
	/**
	 * 修改密码
	 * @param userId
	 * @param newPassword
	 * @param oldPassword
	 */
	public void modifyPassword(Long userId,String newPassword,String oldPassword) throws Exception;
}
