package org.yixun.platform.application.security.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.security.OrgApplication;
import org.yixun.platform.application.security.dto.IdentityDTO;
import org.yixun.platform.application.security.dto.OrgDTO;
import org.yixun.platform.application.security.util.OrgBeanUtil;
import org.yixun.platform.core.security.Identity;
import org.yixun.platform.core.security.Org;
import org.yixun.platform.core.security.Role;

@Named
@Transactional
public class OrgApplicationImpl implements OrgApplication {

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<OrgDTO> findOrgTree() throws Exception {
		List<Org> topOrgList = Org.findTopLevelOrg();
		
		List<OrgDTO> topOrgDTOList = new ArrayList<OrgDTO>();
		OrgDTO topOrgDTO = null;
		for (Org topOrg : topOrgList) {
			topOrgDTO = new OrgDTO();
			OrgBeanUtil.domainToDTO(topOrgDTO, topOrg);
			
			findAllSubOrg(topOrgDTO);
			
			topOrgDTOList.add(topOrgDTO);
		}
		return topOrgDTOList;
	}
	
	private void findAllSubOrg(OrgDTO topOrgDTO){
		List<Org> subOrgList = Org.findOrgByParentId(topOrgDTO.getId());
		
		if(null != subOrgList && subOrgList.size() !=0){
			List<OrgDTO> subOrgDTOList = new ArrayList<OrgDTO>();
			OrgDTO subOrgDTO = null;
			for (Org subOrg : subOrgList) {
				subOrgDTO = new OrgDTO();
				OrgBeanUtil.domainToDTO(subOrgDTO, subOrg);
				
				findAllSubOrg(subOrgDTO);
				
				subOrgDTOList.add(subOrgDTO);
			}
			topOrgDTO.setChildren(subOrgDTOList);
		}
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<IdentityDTO> findIdentityByOrgId(Long orgId) throws Exception {
		Org org = Org.load(Org.class, orgId);
		Set<Identity> identities = org.getIdentities();
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<OrgDTO> findOrgAndIdentityTree() throws Exception {
		List<Org> topOrgList = Org.findTopLevelOrg();
		
		List<OrgDTO> topOrgDTOList = new ArrayList<OrgDTO>();
		OrgDTO topOrgDTO = null;
		for (Org topOrg : topOrgList) {
			topOrgDTO = new OrgDTO();
			OrgBeanUtil.domainToDTO(topOrgDTO, topOrg);
			
			findAllSubOrgAndIdentity(topOrg,topOrgDTO);
			
			topOrgDTOList.add(topOrgDTO);
		}
		return topOrgDTOList;
	}
	
	private void findAllSubOrgAndIdentity(Org topOrg,OrgDTO topOrgDTO){
		
		List<Identity> nextLevelIdentityList = topOrg.findNextLevelIdentity();
		if(null != nextLevelIdentityList && nextLevelIdentityList.size() != 0){
			List<OrgDTO> subIdentityDTOList = new ArrayList<OrgDTO>();
			OrgDTO subIdentityDTO = null;
			for (Identity identity : nextLevelIdentityList) {
				subIdentityDTO = new OrgDTO();
				subIdentityDTO.setId(identity.getId());
				subIdentityDTO.setText(identity.getName());
				subIdentityDTO.setType("user");
				subIdentityDTO.setParentId(topOrg.getId());
				subIdentityDTO.setIcon("images/icons/menu/role.gif");
				
				subIdentityDTOList.add(subIdentityDTO);
			}
			topOrgDTO.setChildren(subIdentityDTOList);
		}
		
		List<Org> subOrgList = Org.findOrgByParentId(topOrgDTO.getId());
		if(null != subOrgList && subOrgList.size() !=0){
			List<OrgDTO> subOrgDTOList = new ArrayList<OrgDTO>();
			OrgDTO subOrgDTO = null;
			for (Org subOrg : subOrgList) {
				subOrgDTO = new OrgDTO();
				OrgBeanUtil.domainToDTO(subOrgDTO, subOrg);
				
				findAllSubOrgAndIdentity(subOrg,subOrgDTO);
				
				subOrgDTOList.add(subOrgDTO);
			}
			if(null != topOrgDTO.getChildren()){
				topOrgDTO.getChildren().addAll(subOrgDTOList);
			} else {
				topOrgDTO.setChildren(subOrgDTOList);
			}
		}
	}

	@Override
	public void assignRoleToOrg(Long orgId, Long[] roleIds) throws Exception {
		Org org = Org.load(Org.class, orgId);
		for (Long roleId : roleIds) {
			Role role = Role.load(Role.class, roleId);
			org.getRoles().add(role);
		}
		
	}

	@Override
	public void removeRoleForOrg(Long orgId, Long[] roleIds) throws Exception {
		Org org = Org.load(Org.class, orgId);
		for (Long roleId : roleIds) {
			Role role = Role.load(Role.class, roleId);
			org.getRoles().remove(role);
		}
		
	}

	@Override
	public OrgDTO findOrgById(Long id) throws Exception {
		Org org = Org.load(Org.class, id);
		OrgDTO orgDTO = new OrgDTO();
		OrgBeanUtil.domainToDTO(orgDTO, org);
		return orgDTO;
	}

	@Override
	public OrgDTO findOrgByIdentityId(Long userId) throws Exception {
		List<Org> orgList = Org.findOrgByIdentityId(userId);
		return null;
	}

}
