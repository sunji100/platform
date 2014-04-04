package org.yixun.platform.workflow.listener;

import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.context.Context;
import org.yixun.platform.workflow.cmd.DelegateTaskCmd;
import org.yixun.platform.workflow.service.DelegateService;

public class DelegateTaskListener extends DefaultTaskListener {
	@Inject
    private DelegateService delegateService;

    @Override
    public void onAssignment(DelegateTask delegateTask) throws Exception {
        String assignee = delegateTask.getAssignee();
        String processDefinitionId = delegateTask.getProcessDefinitionId();
        Map<String, Object> delegateInfo = delegateService.getDelegateInfo(assignee,processDefinitionId);

        if (delegateInfo == null) {
            return;
        }

        String attorney = String.valueOf(delegateInfo.get("attorney"));
        new DelegateTaskCmd(delegateTask.getId(), attorney).execute(Context.getCommandContext());
    }
 
}
