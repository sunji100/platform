package org.yixun.platform.application.security.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.yixun.platform.application.crud.dto.UserDetails;
import org.yixun.platform.application.security.AuthDataService;
import org.yixun.platform.core.security.Identity;
import org.yixun.platform.core.security.Resource;
import org.yixun.platform.core.security.Role;

import com.dayatang.querychannel.service.QueryChannelService;

@Named
public class AuthDataServiceImpl implements AuthDataService {

	@Inject
	private QueryChannelService queryChannelService;
	
	public UserDetails loadUserByUseraccount(String useraccount) {
		String jpql = "select _identity from Identity _identity where _identity.userAccount = ?";
		List<Object> conditionVals = new ArrayList<Object>();
		conditionVals.add(useraccount);
		
		Identity identity = queryChannelService.querySingleResult(jpql, conditionVals.toArray());
		if(null != identity){
			UserDetails userDetails = new UserDetails();
			userDetails.setUseraccount(identity.getUserAccount());
			userDetails.setPassword(identity.getUserPassword());
			userDetails.setEnabled(identity.isValid());
			
//			jpql = "select _role from Identity _identity inner join _identity.roles _role where _identity.userAccount = ?";
//			List<Role> roles = queryChannelService.queryResult(jpql, conditionVals.toArray());
			Set<Role> roles = identity.getRoles();
			
			if(null != roles){
				List<String> roleList = new ArrayList<String>();
				for (Role role : roles) {
					roleList.add(role.getName());
				}
				userDetails.setRoles(roleList);
			}
			return userDetails;
		}
		return null;
	}

	public Map<String, List<String>> getAllResourceAndRoles() {
		
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
