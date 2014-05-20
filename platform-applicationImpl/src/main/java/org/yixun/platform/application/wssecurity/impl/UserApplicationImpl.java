package org.yixun.platform.application.wssecurity.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.security.dto.IdentityDTO;
import org.yixun.platform.application.security.util.IdentityBeanUtil;
import org.yixun.platform.application.wssecurity.UserApplication;
import org.yixun.platform.application.wssecurity.dto.UserDTO;
import org.yixun.platform.application.wssecurity.util.UserBeanUtil;
import org.yixun.platform.core.security.Identity;
import org.yixun.platform.core.wssecurity.WsRole;
import org.yixun.platform.core.wssecurity.WsUser;
import org.yixun.support.exception.BizException;

import com.dayatang.domain.QuerySettings;
import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

@Named
@Transactional
public class UserApplicationImpl implements UserApplication {
	@Inject
	private QueryChannelService queryChannelService;
	@Inject
	private JdbcTemplate jdbcTemplate;
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public Page<UserDTO> pageQueryUser(UserDTO queryDTO, int page, int pagesize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _user from WsUser _user where 1=1 and _user.abolishDate > ?");
		List<Object> conditionVals = new ArrayList<Object>();
		conditionVals.add(new Date());
		
		if(null != queryDTO.getName()){
			jpql.append(" and _user.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getName()));
		}
		
		if(null != queryDTO.getUserAccount()){
			jpql.append(" and _user.userAccount like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getUserAccount()));
		}
		
		Page<WsUser> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pagesize);
		
		List<UserDTO> userDTOs = new ArrayList<UserDTO>();
		UserDTO userDTO = null;
		for (WsUser user : pages.getResult()) {
			userDTO = new UserDTO();
			UserBeanUtil.domainToDTO(userDTO, user);
			userDTOs.add(userDTO);
		}
		return new Page<UserDTO>(pages.getCurrentPageNo(),pages.getTotalCount(),pages.getPageSize(),userDTOs);
	}

	@Override
	public UserDTO saveUser(UserDTO userDTO) throws Exception {
		WsUser user = new WsUser();
		UserBeanUtil.dtoToDomain(user,userDTO);
		if(user.isAccountExist()){
			throw new BizException("user.exist","用户已存在");
		}
		
		user.save();
		userDTO.setId(user.getId());
		return userDTO;
	}

	@Override
	public void updateUser(UserDTO userDTO) throws Exception {
		WsUser user = WsUser.get(WsUser.class, userDTO.getId());
		user.setName(userDTO.getName());
		user.setUserAccount(userDTO.getUserAccount());
		user.setUserDesc(userDTO.getUserDesc());
		user.setValid(userDTO.isValid());
		
	}

	@Override
	public void removeUser(Long[] ids) throws Exception {
		
		for (Long id : ids) {
			WsUser user = WsUser.load(WsUser.class, id);
			user.remove();
		}
		
	}

	@Override
	public UserDTO findUserById(Long id) throws Exception {
		WsUser user = WsUser.load(WsUser.class, id);
		UserDTO userDTO = new UserDTO();
		UserBeanUtil.domainToDTO(userDTO, user);
		
		return userDTO;
	}

	@Override
	public Page<UserDTO> pageQueryUserByRoleId(UserDTO queryDTO, Long roleId, int page, int pageSize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _user from WsUser _user inner join _user.roles _role where 1=1 and _user.abolishDate > ?");
		List<Object> conditionVals = new ArrayList<Object>();
		conditionVals.add(new Date());
		
		if(null != roleId){
			jpql.append(" and _role.id = ?");
			conditionVals.add(roleId);
		}
		
		if(null != queryDTO.getName()){
			jpql.append(" and _user.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getName()));
		}
		
		if(null != queryDTO.getUserAccount()){
			jpql.append(" and _user.userAccount like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getUserAccount()));
		}
		
		Page<WsUser> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pageSize);
		
		List<UserDTO> userDTOs = new ArrayList<UserDTO>();
		UserDTO userDTO = null;
		for (WsUser user : pages.getResult()) {
			userDTO = new UserDTO();
			UserBeanUtil.domainToDTO(userDTO,user);
			userDTOs.add(userDTO);
		}
		return new Page<UserDTO>(pages.getCurrentPageNo(),pages.getTotalCount(),pages.getPageSize(),userDTOs);
	}

	@Override
	public void assignUserToRole(Long roleId, Long[] userIds) throws Exception {
		WsRole role = WsRole.load(WsRole.class, roleId);
		for (Long id : userIds) {
			WsUser user = WsUser.load(WsUser.class, id);
			role.getUsers().add(user);
			
		}
		
	}

	@Override
	public void removeUserForRole(Long roleId, Long[] userIds) throws Exception {
		WsRole role = WsRole.load(WsRole.class, roleId);
		for (Long id : userIds) {
			WsUser user = WsUser.load(WsUser.class, id);
			role.getUsers().remove(user);
		}
		
	}

	@Override
	public Page<UserDTO> findNotAssignUserByRole(UserDTO queryDTO, Long roleId, int page, int pageSize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _user from WsUser _user where not exists (select _inneruser from WsUser _inneruser inner join _inneruser.roles _role where _role.id = ? and _inneruser.id=_user.id) and _user.abolishDate>?");
		List<Object> conditionVals = new ArrayList<Object>();
		
		conditionVals.add(roleId);
		conditionVals.add(new Date());
		
		if(null != queryDTO.getName()){
			jpql.append(" and _user.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getName()));
		}
		
		if(null != queryDTO.getUserAccount()){
			jpql.append(" and _user.userAccount like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getUserAccount()));
		}
		
		Page<WsUser> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pageSize);
		
		List<UserDTO> userDTOs = new ArrayList<UserDTO>();
		UserDTO userDTO = null;
		for (WsUser user : pages.getResult()) {
			userDTO = new UserDTO();
			UserBeanUtil.domainToDTO(userDTO, user);
			userDTOs.add(userDTO);
		}
		return new Page<UserDTO>(pages.getCurrentPageNo(),pages.getTotalCount(),pages.getPageSize(),userDTOs);
	}

	@Override
	public UserDTO loadUserByUseraccount(String useraccount) throws Exception {
		QuerySettings<WsUser> querySettings = QuerySettings.create(WsUser.class);
		querySettings.eq("userAccount", useraccount);
		WsUser user = WsUser.getRepository().getSingleResult(querySettings);
		
		Set<WsRole> roles = user.getRoles();
		List<String> roleNames = new ArrayList<String>();
		if(null != roles){
			for (WsRole role : roles) {
				roleNames.add(role.getName());
			}
		}
		
		UserDTO userDTO = new UserDTO();
		UserBeanUtil.domainToDTO(userDTO, user);
		userDTO.setRoles(roleNames);
		return userDTO;
	}

}
