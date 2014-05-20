package org.yixun.platform.application.wssecurity;

import org.yixun.platform.application.crud.dto.UserDetails;
import org.yixun.platform.application.security.dto.IdentityDTO;
import org.yixun.platform.application.wssecurity.dto.UserDTO;

import com.dayatang.querychannel.support.Page;

public interface UserApplication {
	/**
	 * 查询全部user
	 * @param queryDTO
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	public Page<UserDTO> pageQueryUser(UserDTO queryDTO,int page,int pagesize) throws Exception;
	/**
	 * 保存用户
	 * @param userDTO
	 * @return
	 * @throws Exception
	 */
	public UserDTO saveUser(UserDTO userDTO) throws Exception; 
	/**
	 * 更新用户
	 * @param userDTO
	 * @throws Exception
	 */
	public void updateUser(UserDTO userDTO) throws Exception;
	/**
	 * 删除用户
	 * @param ids
	 * @throws Exception
	 */
	public void removeUser(Long[] ids) throws Exception;
	/**
	 * 查询指定id用户
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public UserDTO findUserById(Long id) throws Exception;
	/**
	 * 获得已分配到角色上的用户
	 * @param queryDTO
	 * @param roleId 角色ID
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Page<UserDTO> pageQueryUserByRoleId(UserDTO queryDTO,Long roleId,int page,int pageSize) throws Exception;
	/**
	 * 为角色分配用户
	 * @param roleId
	 * @param Ids
	 * @throws Exception
	 */
	public void assignUserToRole(Long roleId,Long[] userIds) throws Exception;
	
	/**
	 * 删除指定角色已分配的用户
	 * @param roleId 角色ID
	 * @param userIds 用户ID数组
	 * @return
	 * @throws Exception
	 */
	public void removeUserForRole(Long roleId,Long[] userIds) throws Exception;
	/**
	 * 获得所有未分配到指定角色的用户
	 * @param identityDTO
	 * @param roleId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Page<UserDTO> findNotAssignUserByRole(UserDTO queryDTO,Long roleId,int page,int pageSize) throws Exception; 
	/**
	 * 查询指定用户的信息
	 * @param useraccount
	 * @return
	 * @throws Exception
	 */
	public UserDTO loadUserByUseraccount(String useraccount) throws Exception;
}
