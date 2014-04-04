package org.yixun.platform.workflow.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.IdentityLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yixun.platform.core.security.Identity;
import org.yixun.platform.core.security.Org;
import org.yixun.platform.core.security.Role;
import org.yixun.platform.core.workflow.BpmConfUser;
import org.yixun.platform.workflow.service.ConfUserTaskService;


public class ConfUserTaskListener extends DefaultTaskListener {
	private static Logger logger = LoggerFactory
            .getLogger(ConfUserTaskListener.class);
	
	@Inject
	private ConfUserTaskService confUserTaskService;
	
	@Override
	public void onCreate(DelegateTask delegateTask) throws Exception {
		String processDefinitionId = delegateTask.getProcessDefinitionId();
		String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
		
		ProcessDefinitionEntity processDefinitionEntity = Context.getProcessEngineConfiguration().getProcessDefinitionCache().get(delegateTask.getProcessDefinitionId());
		ActivityImpl startActivity = processDefinitionEntity.getInitial();
		if (startActivity.getOutgoingTransitions().size() != 1) {
			throw new IllegalStateException("start activity outgoing transitions cannot more than 1, now is : " + startActivity.getOutgoingTransitions().size());
		}

		PvmTransition pvmTransition = startActivity.getOutgoingTransitions().get(0);
		PvmActivity targetActivity = pvmTransition.getDestination();

		if (!"userTask".equals(targetActivity.getProperty("type"))) {
			logger.debug("first activity is not userTask, just skip");
		}
		
		String loginUserId = String.valueOf(delegateTask.getVariable("loginUserId"));
		//设置FirstTask的Assignee为登录用户
		if(targetActivity.getId().equals(delegateTask.getExecution().getCurrentActivityId())){
			if(null != loginUserId){
				delegateTask.setAssignee(loginUserId);
				return;
			}
		}
		
		
//		Object firstTask = delegateTask.getVariable("firstTask");
//		if(null != firstTask){
//			if(taskDefinitionKey.equals(firstTask.toString())){
//				Object loginUserId = delegateTask.getVariable("loginUserId");
//				if(null != loginUserId){
//					delegateTask.setAssignee(loginUserId.toString());
//					return;
//				}
//			}
//		}
		
		//获得当前task执行人及角色
		List<BpmConfUser> bpConfUsers = BpmConfUser.findBpmConf(processDefinitionId, taskDefinitionKey);
		Set<Identity> users = bpConfUsers.get(0).getUsers();
		Set<Role> roles = bpConfUsers.get(0).getRoles();
		
		
		if(roles.size() ==0 && users.size() == 1){//当前task只分配一个用户
			Identity user = users.iterator().next();
			delegateTask.setAssignee(String.valueOf(user.getId()));
			return;
		}
		
		if(roles.size() ==1 && users.size() == 0){//当前task只分配一个角色
			Role role = roles.iterator().next();
			Set<Identity> roleHasUsers = role.getIdentities();
			if(roleHasUsers.size() == 1){//角色中只有一个用户
				Identity user = roleHasUsers.iterator().next();
				delegateTask.setAssignee(String.valueOf(user.getId()));
				return;
			}
		}
		if(roles.size() ==1 && users.size() == 1){//只有一个角色和用户
			Role role = roles.iterator().next();
			Identity user = users.iterator().next();
			Set<Identity> roleHasUsers = role.getIdentities();
			if(roleHasUsers.size() == 1){//角色中只有一个用户
				Identity roleHasUser = roleHasUsers.iterator().next();
				if(user.getId() == roleHasUser.getId()){//角色中用户与分配的用户是同一个
					delegateTask.setAssignee(String.valueOf(user.getId()));
					return;
				}
			}
		} 
		
		List<String> candidateUserList = new ArrayList<String>();
		for(Identity user:users){
			candidateUserList.add(String.valueOf(user.getId()));
		}
		for(Role role:roles){
			Org org = confUserTaskService.findOrgByIdentityId(Long.parseLong(loginUserId));//用户所在部门
			List<Long> userIdList = confUserTaskService.findIdentityByOrgId(org.getId());//获得当前部门及所有上级部门中人员
			for (Long userId : userIdList) {
				List<Long> roleIdList = confUserTaskService.findRoleByUserId(userId);//人员所有角色及从组织中继承的角色
				for (Long roleId : roleIdList) {
					if(role.getId() == roleId){
						if(!candidateUserList.contains(String.valueOf(userId))){
							candidateUserList.add(String.valueOf(userId));
						}
					}
				}
			}
		}
		delegateTask.addCandidateUsers(candidateUserList);
		
	}

	
}
