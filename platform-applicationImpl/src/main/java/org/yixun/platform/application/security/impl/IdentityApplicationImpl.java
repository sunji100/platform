package org.yixun.platform.application.security.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.experimental.theories.ParametersSuppliedBy;
import org.openkoala.exception.extend.ApplicationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.crud.dto.ProductDTO;
import org.yixun.platform.application.security.IdentityApplication;
import org.yixun.platform.application.security.dto.IdentityDTO;
import org.yixun.platform.application.security.util.IdentityBeanUtil;
import org.yixun.platform.core.security.Identity;
import org.yixun.platform.core.security.Role;
import org.yixun.support.exception.BusinessException;

import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

@Named
@Transactional
public class IdentityApplicationImpl implements IdentityApplication {

	@Inject
	private QueryChannelService queryChannelService;
	
	public IdentityDTO getIdentity(String userAccount) {	
		return null;
	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<IdentityDTO> findAllIdentity() {
		List<Identity> identities = Identity.findAll(Identity.class);
		
		List<IdentityDTO> identityDTOs = new ArrayList<IdentityDTO>();
		IdentityDTO identityDTO = null;
		for (Identity identity : identities) {
			identityDTO = new IdentityDTO();
			IdentityBeanUtil.domainToDTO(identityDTO,identity);
			identityDTOs.add(identityDTO);
		}
		return identityDTOs;
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public Page<IdentityDTO> pageQueryIdentity(IdentityDTO queryDTO, int page, int pageSize) {
		StringBuilder jpql = new StringBuilder("select _identity from Identity _identity where 1=1");
		List<Object> conditionVals = new ArrayList<Object>();
		
		if(null != queryDTO.getName()){
			jpql.append(" and _identity.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getName()));
		}
		
		if(null != queryDTO.getUserAccount()){
			jpql.append(" and _identity.userAccount like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getUserAccount()));
		}
		
		Page<Identity> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pageSize);
		
		List<IdentityDTO> identityDTOs = new ArrayList<IdentityDTO>();
		IdentityDTO identityDTO = null;
		for (Identity identity : pages.getResult()) {
			identityDTO = new IdentityDTO();
			IdentityBeanUtil.domainToDTO(identityDTO, identity);
			identityDTOs.add(identityDTO);
		}
		return new Page<IdentityDTO>(pages.getCurrentPageNo(),pages.getTotalCount(),pages.getPageSize(),identityDTOs);
	}

	@Override
	public IdentityDTO saveIdentity(IdentityDTO identityDTO) throws BusinessException {
		Identity identity = new Identity();
		IdentityBeanUtil.dtoToDomain(identityDTO,identity);
		if(identity.isAccountExist()){
			throw new BusinessException("用户已存在");
		}
		identity.save();
		identityDTO.setId(identity.getId());
		return identityDTO;
	}

	@Override
	public void updateIdentity(IdentityDTO identityDTO) {
		Identity identity = Identity.get(Identity.class, identityDTO.getId());
		identity.setName(identityDTO.getName());
		identity.setUserAccount(identityDTO.getUserAccount());
		identity.setUserDesc(identityDTO.getUserDesc());
		identity.setValid(identityDTO.isValid());
	}

	@Override
	public IdentityDTO findIdentityById(Long id) {
		Identity identity = Identity.load(Identity.class, id);
		IdentityDTO identityDTO = new IdentityDTO();
		IdentityBeanUtil.domainToDTO(identityDTO, identity);
		return identityDTO;
	}
	
	@Override
	public void removeIdentity(Long[] ids) {
		for (Long id : ids) {
			Identity identity = Identity.load(Identity.class, id);
			identity.remove();
		}
		
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public Page<IdentityDTO> pageQueryIdentityByRoleId(IdentityDTO queryDTO, Long roleId, int page, int pageSize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _identity from Identity _identity inner join _identity.roles _role where 1=1");
		List<Object> conditionVals = new ArrayList<Object>();
		
		if(null != roleId){
			jpql.append(" and _role.id = ?");
			conditionVals.add(roleId);
		}
		
		if(null != queryDTO.getName()){
			jpql.append(" and _identity.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getName()));
		}
		
		if(null != queryDTO.getUserAccount()){
			jpql.append(" and _identity.userAccount like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getUserAccount()));
		}
		
		Page<Identity> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pageSize);
		
		List<IdentityDTO> identityDTOs = new ArrayList<IdentityDTO>();
		IdentityDTO identityDTO = null;
		for (Identity identity : pages.getResult()) {
			identityDTO = new IdentityDTO();
			IdentityBeanUtil.domainToDTO(identityDTO, identity);
			identityDTOs.add(identityDTO);
		}
		return new Page<IdentityDTO>(pages.getCurrentPageNo(),pages.getTotalCount(),pages.getPageSize(),identityDTOs);
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public Page<IdentityDTO> findNotAssignUserByRole(IdentityDTO queryDTO,Long roleId,int page,int pageSize) throws Exception{
		StringBuilder jpql = new StringBuilder("select _identity from Identity _identity where 1=1 and "
				+ "_identity.id not in (select _identity.id from Identity _identity inner join _identity.roles _role where _role.id = ?)");
		List<Object> conditionVals = new ArrayList<Object>();
		
		conditionVals.add(roleId);
		
		if(null != queryDTO.getName()){
			jpql.append(" and _identity.name like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getName()));
		}
		
		if(null != queryDTO.getUserAccount()){
			jpql.append(" and _identity.userAccount like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getUserAccount()));
		}
		
		Page<Identity> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pageSize);
		
		List<IdentityDTO> identityDTOs = new ArrayList<IdentityDTO>();
		IdentityDTO identityDTO = null;
		for (Identity identity : pages.getResult()) {
			identityDTO = new IdentityDTO();
			IdentityBeanUtil.domainToDTO(identityDTO, identity);
			identityDTOs.add(identityDTO);
		}
		return new Page<IdentityDTO>(pages.getCurrentPageNo(),pages.getTotalCount(),pages.getPageSize(),identityDTOs);
	}

	@Override
	public void assignUserToRole(Long roleId,Long[] identityIds) throws Exception {
		
		Role role = Role.load(Role.class, roleId);
		for (Long id : identityIds) {
			Identity identity = Identity.load(Identity.class, id);
			role.getIdentities().add(identity);
			
		}
		
	}

	@Override
	public void removeUserForRole(Long roleId, Long[] identityIds) throws Exception {
		Role role = Role.load(Role.class, roleId);
		for (Long id : identityIds) {
			Identity identity = Identity.load(Identity.class, id);
			role.getIdentities().remove(identity);
		}
		
	}

}