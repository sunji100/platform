package org.yixun.platform.application.security.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.security.OrgApplication;
import org.yixun.platform.application.security.RoleApplication;
import org.yixun.platform.application.security.dto.RoleDTO;
import org.yixun.platform.application.security.util.RoleBeanUtil;
import org.yixun.platform.core.security.Identity;
import org.yixun.platform.core.security.Org;
import org.yixun.platform.core.security.Resource;
import org.yixun.platform.core.security.Role;
import org.yixun.support.exception.BizException;

import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

@Named
@Transactional
public class RoleApplicationImpl implements RoleApplication {
	
	@Inject
	private QueryChannelService queryChannelService;
	@Inject
	private OrgApplication orgApplication;
	
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
			throw new BizException("role.exist", "用户已存在.");
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

	@Override
	public void assignMenuToRole(Long roleId, Long[] menuIds) throws Exception {
		Role role = Role.load(Role.class, roleId);
		Set<Resource> menus = new HashSet<Resource>();
		for (Long menuId : menuIds) {
			Resource resource = Resource.load(Resource.class, menuId);
			menus.add(resource);
		}
		role.setResources(menus);
		
	}

	@Override
	public void removeMenuForRole(Long roleId, Long[] menuIds) throws Exception {
		Role role = Role.load(Role.class, roleId);
		for (Long menuId : menuIds) {
			Resource resource = Resource.load(Resource.class, menuId);
			role.getResources().remove(resource);
		}
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<RoleDTO> findRoleByOrgId(RoleDTO queryDTO, Long orgId) throws Exception {
		StringBuilder jpql = new StringBuilder("select _role from Role _role inner join _role.orgs _org where 1=1");
		List<Object> conditionVals = new ArrayList<Object>();
		
		if(null != orgId){
			jpql.append(" and _org.id = ?");
			conditionVals.add(orgId);
		}
		
		if(!StringUtils.isBlank(queryDTO.getName())){
			jpql.append(" and _role.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getName()));
		}
		
		List<Role> roles = queryChannelService.queryResult(jpql.toString(), conditionVals.toArray());
		
		List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
		RoleDTO roleDTO = null;
		for(Role role:roles){
			roleDTO = new RoleDTO();
			RoleBeanUtil.domainToDTO(roleDTO, role);
			roleDTOs.add(roleDTO);
		}
		return roleDTOs;
	}
	
	/**
	 * 获得组织及从上级组织继承的角色
	 */
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<RoleDTO> findParentRoleByOrgId(RoleDTO queryDTO, Long orgId) throws Exception {
		Org org = Org.load(Org.class, orgId);
		List<Long> orgIdList = new ArrayList<Long>();
		orgIdList.add(org.getId());
		
		findAllParentOrgId(org, orgIdList);
		
		StringBuilder jpql = new StringBuilder("select new org.yixun.platform.application.security.dto.RoleDTO(_role,_org.id,_org.name) from Role _role inner join _role.orgs _org where 1=1");
		List<Object> conditionVals = new ArrayList<Object>();
		
		if(null != orgIdList){	
			Collections.reverse(orgIdList);
			jpql.append(" and _org.id in ("+ StringUtils.join(orgIdList, ",") +")");
		}
		
		if(!StringUtils.isBlank(queryDTO.getName())){
			jpql.append(" and _role.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getName()));
		}
		
		List<RoleDTO> roles = queryChannelService.queryResult(jpql.toString(), conditionVals.toArray());
		
//		List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
//		RoleDTO roleDTO = null;
//		for(Role role:roles){
//			roleDTO = new RoleDTO();
//			RoleBeanUtil.domainToDTO(roleDTO, role);
//			roleDTOs.add(roleDTO);
//		}
		return roles;

	}
	/**
	 * 获得用户所拥有的角色
	 */
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<RoleDTO> findRoleByIdentityId(RoleDTO queryDTO, Long identityId) throws Exception {
		
		StringBuilder jpql = new StringBuilder("select new org.yixun.platform.application.security.dto.RoleDTO(_role,_identity.id,_identity.name) from Role _role inner join _role.identities _identity where 1=1 and _identity.id = ?");
		List<Object> conditionVals = new ArrayList<Object>();
		conditionVals.add(identityId);
		
		if(!StringUtils.isBlank(queryDTO.getName())){
			jpql.append(" and _role.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getName()));
		}
		
		List<RoleDTO> roles = queryChannelService.queryResult(jpql.toString(), conditionVals.toArray());
		
//		List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
//		RoleDTO roleDTO = null;
//		for(Role role:roles){
//			roleDTO = new RoleDTO();
//			RoleBeanUtil.domainToDTO(roleDTO, role);
//			roleDTOs.add(roleDTO);
//		}
		return roles;

	}
	/**
	 * 获得所有的上级组织
	 * @param org
	 * @param orgIdList
	 */
	private void findAllParentOrgId(Org org,List<Long> orgIdList){
		Set<Org> parents = org.getParents();
		if(null != parents && parents.size() != 0){
			for (Org parentOrg : parents) {
				orgIdList.add(parentOrg.getId());
				findAllParentOrgId(parentOrg,orgIdList);
			}
		}
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<RoleDTO> findRoleByOrgIdAndIdentityId(RoleDTO queryDTO, Long orgId, Long identityId) throws Exception {
		//组织所拥有的角色
		List<RoleDTO> parentRoleByOrgIdList = findParentRoleByOrgId(queryDTO, orgId);
		//角色所拥有的角色
		List<RoleDTO> roleByIdentityIdList = findRoleByIdentityId(queryDTO, identityId);
		
		List<RoleDTO> roleDTOList = new ArrayList<RoleDTO>();
		roleDTOList.addAll(parentRoleByOrgIdList);
		roleDTOList.addAll(roleByIdentityIdList);
		return roleDTOList;
	}

	@Override
	public Page<RoleDTO> findRoleByNoAssignToIdentityIdOrOrgId(RoleDTO queryDTO, Long orgId,Long identityId,int page,int pageSize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _role from Role _role where 1=1");
		List<Object> conditionVals = new ArrayList<Object>();
		if(null != orgId){
			jpql.append(" and _role.id not in (select _role.id from Org _org inner join _org.roles _role where _org.id = ?) ");
			conditionVals.add(orgId);
		}
		if(null != identityId){
			jpql.append(" and _role.id not in (select _role.id from Identity _identity inner join _identity.roles _role where _identity.id = ?) ");
			conditionVals.add(identityId);
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
	public void assignResourceToRole(Long roleId, Long[] resourceIds) throws Exception {
		Role role = Role.load(Role.class, roleId);
		for (Long resourceId : resourceIds) {
			Resource resource = Resource.load(Resource.class, resourceId);
			role.getResources().add(resource);
		}
		
	}

	@Override
	public void removeResourceForRole(Long roleId, Long[] resourceIds) throws Exception {
		Role role = Role.load(Role.class, roleId);
		for (Long resourceId : resourceIds) {
			Resource resource = Resource.load(Resource.class, resourceId);
			role.getResources().remove(resource);
		}
		
	}

	/**
	 * 
	 */
	@Override
	public List<RoleDTO> findRoleForOrgHasUserByUserId(Long userId) throws Exception {
		
		return null;
	}

}
