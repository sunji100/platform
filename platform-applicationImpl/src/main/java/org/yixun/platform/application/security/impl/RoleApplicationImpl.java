package org.yixun.platform.application.security.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.security.RoleApplication;
import org.yixun.platform.application.security.dto.IdentityDTO;
import org.yixun.platform.application.security.dto.RoleDTO;
import org.yixun.platform.application.security.util.IdentityBeanUtil;
import org.yixun.platform.application.security.util.RoleBeanUtil;
import org.yixun.platform.core.security.Identity;
import org.yixun.platform.core.security.Role;
import org.yixun.support.exception.BusinessException;

import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

@Named
@Transactional
public class RoleApplicationImpl implements RoleApplication {
	
	@Inject
	private QueryChannelService queryChannelService;
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public Page<RoleDTO> pageQueryRole(RoleDTO queryDTO, int page, int pageSize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _role from Role _role where 1=1");
		List<Object> conditionVals = new ArrayList<Object>();
		
		if(!StringUtils.isBlank(queryDTO.getName())){
			jpql.append(" and _role.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getName()));
		}
		
		Page<Role> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pageSize);
		
		List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
		RoleDTO roleDTO = null;
		for(Role role:pages.getResult()){
			roleDTO = new RoleDTO();
			RoleBeanUtil.domainToDTO(roleDTO, role);
			roleDTOs.add(roleDTO);
		}
		return new Page(pages.getCurrentPageNo(),pages.getTotalCount(),pages.getPageSize(),roleDTOs);
	}

	@Override
	public void updateRole(RoleDTO roleDTO) throws Exception {
		Role role = Role.load(Role.class, roleDTO.getId());
		RoleBeanUtil.dtoToDomain(role,roleDTO);
		
	}

	@Override
	public void removeRole(Long[] ids) throws Exception {
		for (Long id : ids) {
			Role role = Role.load(Role.class, id);
			role.remove();
		}
		
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public RoleDTO findRoleById(Long id) throws Exception {
		Role role = Role.load(Role.class, id);
		RoleDTO roleDTO = new RoleDTO();
		RoleBeanUtil.domainToDTO(roleDTO, role);
		return roleDTO;
	}

	@Override
	public RoleDTO saveRole(RoleDTO roleDTO) throws Exception {
		Role role = new Role();
		RoleBeanUtil.dtoToDomain(role,roleDTO);
		if(role.isRoleExist()){
			throw new BusinessException("角色已存在!!");
		}
		role.setId(null);
		role.save();
		
		roleDTO.setId(role.getId());
		return roleDTO;
	}

	@Override
	public void assignRoleToUser(Long userId, Long[] roleIds) throws Exception {
		Identity identity = Identity.load(Identity.class, userId);
		for(Long roleId:roleIds){
			Role role = Role.load(Role.class, roleId);
			identity.getRoles().add(role);
		}
		
	}

	@Override
	public void removeRoleForUser(Long userId, Long[] roleIds) throws Exception {
		Identity identity = Identity.load(Identity.class, userId);
		for(Long roleId:roleIds){
			Role role = Role.load(Role.class, roleId);
			identity.getRoles().remove(role);
		}
		
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public Page<RoleDTO> pageQueryRoleByUserId(RoleDTO queryDTO, Long userId, int page, int pageSize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _role from Role _role inner join _role.identities _identity where 1=1");
		List<Object> conditionVals = new ArrayList<Object>();
		
		if(null != userId){
			jpql.append(" and _identity.id = ?");
			conditionVals.add(userId);
		}
		
		if(!StringUtils.isBlank(queryDTO.getName())){
			jpql.append(" and _role.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getName()));
		}
		
		Page<Role> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pageSize);
		
		List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
		RoleDTO roleDTO = null;
		for(Role role:pages.getResult()){
			roleDTO = new RoleDTO();
			RoleBeanUtil.domainToDTO(roleDTO, role);
			roleDTOs.add(roleDTO);
		}
		return new Page<RoleDTO>(pages.getCurrentPageNo(),pages.getTotalCount(),pages.getPageSize(),roleDTOs);
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public Page<RoleDTO> findNotAssignRoleByUser(RoleDTO queryDTO, Long userId, int page, int pageSize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _role from Role _role where 1=1 and "
				+ "_role.id not in (select _role.id from Role _role inner join _role.identities _identity where _identity.id = ?)");
		List<Object> conditionVals = new ArrayList<Object>();
		
		conditionVals.add(userId);
		
		if(!StringUtils.isBlank(queryDTO.getName())){
			jpql.append(" and _role.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getName()));
		}
		
		Page<Role> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pageSize);
		
		List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
		RoleDTO roleDTO = null;
		for(Role role:pages.getResult()){
			roleDTO = new RoleDTO();
			RoleBeanUtil.domainToDTO(roleDTO, role);
			roleDTOs.add(roleDTO);
		}
		return new Page<RoleDTO>(pages.getCurrentPageNo(),pages.getTotalCount(),pages.getPageSize(),roleDTOs);
	}

}
