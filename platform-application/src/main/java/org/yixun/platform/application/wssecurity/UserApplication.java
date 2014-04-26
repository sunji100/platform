package org.yixun.platform.application.wssecurity;

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
}
