package org.yixun.platform.workflow.listener;

import java.util.List;
import java.util.Set;

import org.activiti.engine.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yixun.platform.core.security.Identity;
import org.yixun.platform.core.security.Role;
import org.yixun.platform.core.workflow.BpmConfUser;


public class ConfUserTaskListener extends DefaultTaskListener {
	private static Logger logger = LoggerFactory
            .getLogger(ConfUserTaskListener.class);
	@Override
	public void onCreate(DelegateTask delegateTask) throws Exception {
		String processDefinitionId = delegateTask.getProcessDefinitionId();
		String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
		
		List<BpmConfUser> bpConfUsers = BpmConfUser.findBpmConf(processDefinitionId, taskDefinitionKey);
		
		Set<Identity> users = bpConfUsers.get(0).getUsers();
		Set<Role> roles = bpConfUsers.get(0).getRoles();
		
		
		if(roles.size() ==0 && users.size() == 1){
			Identity user = users.iterator().next();
			delegateTask.setAssignee(String.valueOf(user.getId()));
		} else if(roles.size() ==1 && users.size() == 0){
			Role role = roles.iterator().next();
			Set<Identity> roleHasUsers = role.getIdentities();
			if(roleHasUsers.size() == 1){
				Identity user = roleHasUsers.iterator().next();
				delegateTask.setAssignee(String.valueOf(user.getId()));
			}
		} else if(roles.size() ==1 && users.size() == 1){ 
			Role role = roles.iterator().next();
			Identity user = users.iterator().next();
			Set<Identity> roleHasUsers = role.getIdentities();
			if(roleHasUsers.size() == 1){
				Identity roleHasUser = roleHasUsers.iterator().next();
				if(user.getId() == roleHasUser.getId()){
					delegateTask.setAssignee(String.valueOf(user.getId()));
				} else {
					delegateTask.addCandidateGroup(String.valueOf(role.getId()));
				}
			} else {
				delegateTask.addCandidateGroup(String.valueOf(role.getId()));
				delegateTask.addCandidateUser(String.valueOf(user.getId()));
			}
		} else {
			for(Identity user:users){
				delegateTask.addCandidateUser(String.valueOf(user.getId()));
			}
			for(Role role:roles){
				delegateTask.addCandidateGroup(String.valueOf(role.getId()));
			}
		}
	}

	
}
