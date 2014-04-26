package org.yixun.platform.application.security.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.yixun.platform.application.crud.dto.UserDetails;
import org.yixun.platform.application.security.AuthDataService;
import org.yixun.platform.core.security.Identity;
import org.yixun.platform.core.security.Org;
import org.yixun.platform.core.security.Resource;
import org.yixun.platform.core.security.Role;

import com.dayatang.querychannel.service.QueryChannelService;

@Named
public class AuthDataServiceImpl implements AuthDataService {

	@Inject
	private QueryChannelService queryChannelService;
	
	/**
	 * 获得用户的详细信息
	 */
	public UserDetails loadUserByUseraccount(String useraccount) throws Exception {
		String jpql = "select _identity from Identity _identity where _identity.userAccount = ?";
		List<Object> conditionVals = new ArrayList<Object>();
		conditionVals.add(useraccount);
		
		Identity identity = queryChannelService.querySingleResult(jpql, conditionVals.toArray());
		if(null != identity){
			UserDetails userDetails = new UserDetails();
			userDetails.setUserId(identity.getId());
			userDetails.setUseraccount(identity.getUserAccount());
			userDetails.setPassword(identity.getUserPassword());
			userDetails.setEnabled(identity.isValid());
			
//			jpql = "select _role from Identity _identity inner join _identity.roles _role where _identity.userAccount = ?";
//			List<Role> roles = queryChannelService.queryResult(jpql, conditionVals.toArray());
			//用户所拥有的角色
			Set<Role> roles = identity.getRoles();
			List<String> roleList = new ArrayList<String>();
			
			if(null != roles){
				for (Role role : roles) {
					roleList.add(role.getName());
				}
			}
			//用户所在组织
			Set<Org> orgs = identity.getOrgs();
			if(orgs != null){
				List<Long> orgIdList = new ArrayList<Long>();
				//获得用户所在组织及所有上级组织
				for (Org org : orgs) {
					orgIdList.add(org.getId());
					findAllParentOrgId(org,orgIdList);
//					Set<Role> orgRoles = org.getRoles();
//					for (Role role : orgRoles) {
//						roleList.add(role.getName());
//					}
				}
				//获得用户从组织中继承来的角色
				List<String> orgRoleList = findRolesByOrgList(orgIdList);
				for (String roleName : orgRoleList) {
					roleList.add(roleName);
				}
				
			}
			userDetails.setRoles(roleList);
			return userDetails;
		}
		return null;
	}
	
	/**
	 * 获得组织所拥有的角色
	 * @param orgIdList
	 * @return
	 */
	private List<String> findRolesByOrgList(List<Long> orgIdList){
		String jpql = "select _role.name from Role _role inner join _role.orgs _org where _org.id in ("+ StringUtils.join(orgIdList, ",") +")";
		return queryChannelService.queryResult(jpql, null);
	}
	
	/**
	 * 获得相应组织所有上级组织
	 * @param org 当前组织
	 * @param orgIdList 上级组织IDList
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

	public Map<String, List<String>> getAllResourceAndRoles() throws Exception {
		
		List<Resource> resources = Resource.findAll(Resource.class);
		
		Map<String, List<String>> resourceAndRoles = new HashMap<String, List<String>>();
		if(null != resources){
			for (Resource resource : resources) {
//				String jpql = "select _role from Role _role inner join _role.resources _resource where _resource.id = ?";
//				List<Object> conditionVals = new ArrayList<Object>();
//				conditionVals.add(resource.getId());
//				
//				List<Role> roles = queryChannelService.queryResult(jpql, conditionVals.toArray());
				Set<Role> roles = resource.getRoles();
				
				List<String> roleNames = new ArrayList<String>();
				if(null != roles){
					for (Role role : roles) {
						roleNames.add(role.getName());
					}
				}
				resourceAndRoles.put(resource.getIdentifier(), roleNames);
			}
		}
		return resourceAndRoles;
	}

}
