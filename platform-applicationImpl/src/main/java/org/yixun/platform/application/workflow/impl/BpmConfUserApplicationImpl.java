package org.yixun.platform.application.workflow.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.yixun.platform.application.workflow.BpmConfUserApplication;
import org.yixun.platform.application.workflow.dto.BpmConfUserDTO;
import org.yixun.platform.application.workflow.util.BpmConfUserBeanUtil;
import org.yixun.platform.core.security.Identity;
import org.yixun.platform.core.security.Role;
import org.yixun.platform.core.workflow.BpmConfUser;

public class BpmConfUserApplicationImpl implements BpmConfUserApplication {

	/**
	 * 查看usertask的办理人或办理人角色
	 */
	@Override
	public List<BpmConfUserDTO> findBpmConfUser(String procDefId, String procDefKey, String taskDefKey) throws Exception {
		List<BpmConfUser> bpmConfUsers = BpmConfUser.findBpmConfUser(procDefId, procDefKey, taskDefKey);
		
		List<BpmConfUserDTO> bpmConfUserDTOs = new ArrayList<BpmConfUserDTO>();
		BpmConfUserDTO bpmConfUserDTO = null;
		for (BpmConfUser bpmConfUser : bpmConfUsers) {
	
			Set<Identity> users = bpmConfUser.getUsers();
			if(null != users && users.size() > 0){
				for (Identity identity : users) {
					bpmConfUserDTO = new BpmConfUserDTO();
					BpmConfUserBeanUtil.domainToDTO(bpmConfUserDTO, bpmConfUser);
					
					bpmConfUserDTO.setUserId(identity.getId());
					bpmConfUserDTO.setUserName(identity.getName());
					bpmConfUserDTO.setUserType("user");
					
					bpmConfUserDTOs.add(bpmConfUserDTO);
				}
			}
			
			Set<Role> roles = bpmConfUser.getRoles();
			if(null != roles && roles.size() > 0){
				for (Role role : roles) {
					bpmConfUserDTO = new BpmConfUserDTO();
					BpmConfUserBeanUtil.domainToDTO(bpmConfUserDTO, bpmConfUser);
					
					bpmConfUserDTO.setUserId(role.getId());
					bpmConfUserDTO.setUserName(role.getName());
					bpmConfUserDTO.setUserType("role");
					
					bpmConfUserDTOs.add(bpmConfUserDTO);
				}
			}
			
		}
		return bpmConfUserDTOs;
	}

}
