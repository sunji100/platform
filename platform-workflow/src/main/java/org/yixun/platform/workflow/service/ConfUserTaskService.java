package org.yixun.platform.workflow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.core.security.Identity;
import org.yixun.platform.core.security.Org;
import org.yixun.platform.core.security.Role;

@Named
@Transactional
public class ConfUserTaskService {
	/**
	 * 获得用户所在部门
	 * @param userId
	 * @return
	 */
	public Org findOrgByIdentityId(Long userId){
		Identity identity = Identity.load(Identity.class, userId);
		Set<Org> orgs = identity.getOrgs();
		if(null != orgs && !orgs.isEmpty()){
			return orgs.iterator().next();
		}
		return null;
	}
	
	/**
	 * 获得当前部门及所有上级部门中人员
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public List<Long> findIdentityByOrgId(Long orgId) {
		Org org = Org.load(Org.class, orgId);
		Set<Identity> identities = org.getIdentities();
		List<Long> userIdList = new ArrayList<Long>();
		for (Identity identity : identities) {
			userIdList.add(identity.getId());
		}
		
		findAllParentUserId(org,userIdList);
		
		
		return userIdList;
	}
	
	/**
	 * 获得用户的所有角色及从组织中继承的角色
	 * @param userId
	 * @return
	 */
	public List<Long> findRoleByUserId(Long userId) {
		Identity identity = Identity.load(Identity.class, userId);
		Set<Role> roles = identity.getRoles();
		List<Long> roleIdList = new ArrayList<Long>();
		for (Role role : roles) {
			roleIdList.add(role.getId());
		}
		
		Set<Org> orgs = identity.getOrgs();
		for (Org org : orgs) {
			findAllParentRoleId(org,roleIdList);
		}
		
		return roleIdList;
	}
	
	/**
	 * 递归查找，当前部门所有上级部门中人员
	 * @param org
	 * @param userIdList
	 */
	private void findAllParentUserId(Org org,List<Long> userIdList){
		Set<Org> parents = org.getParents();
		if(null != parents && parents.size() != 0){
			for (Org parentOrg : parents) {
				Set<Identity> identities = parentOrg.getIdentities();
				for (Identity identity : identities) {
					userIdList.add(identity.getId());
				}
				findAllParentUserId(parentOrg,userIdList);
			}
		}
	}
	
	/**
	 * 递归查找，当前部门所有上级部门拥有的角色
	 * @param org
	 * @param orgIdList
	 */
	private void findAllParentRoleId(Org org,List<Long> roleIdList){
		Set<Org> parents = org.getParents();
		if(null != parents && parents.size() != 0){
			for (Org parentOrg : parents) {
				Set<Role> roles = parentOrg.getRoles();
				for (Role role : roles) {
					roleIdList.add(role.getId());
				}
				findAllParentRoleId(parentOrg,roleIdList);
			}
		}
	}
}
