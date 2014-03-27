package org.yixun.platform.workflow;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.spring.factory.SpringInstanceProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/root.xml"})
@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback = false)
public class ActivitiTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Inject
	private RepositoryService repositoryService;
	@Inject
	private TaskService taskService;
	@Inject
	private RuntimeService runtimeService;
	
	@Before
	public void setup() {
        logger.debug("applicationContext: "+applicationContext);
        InstanceFactory.setInstanceProvider(new SpringInstanceProvider(applicationContext));
    }
	
	@Test
	public void delopy(){
		repositoryService.createDeployment().addClasspathResource("采购流程.bpmn").deploy();
//		repositoryService.deleteDeployment("901", true);
//		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionKey("sqcgProcess").list();
//		List<Task> list2 = taskService.createTaskQuery().processDefinitionKey("sqcgProcess").list();
//		for (Task task : list2) {
//			System.out.println(task.getName());
//		}
//		BpmnModel bpmnModel = repositoryService.getBpmnModel("sqcgProcess:1:304");
//		Map<String, ItemDefinition> itemDefinitions = bpmnModel.getItemDefinitions();
	
//		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("sqcgProcess").singleResult();
//		ProcessDefinitionEntity pd = (ProcessDefinitionEntity)((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition("sqcgProcess:1:904");
//		Map<String, TaskDefinition> taskDefinitions = pd.getTaskDefinitions();
//		List<ActivityImpl> activities = pd.getActivities();
//		for (ActivityImpl activityImpl : activities) {
////			
//		}
	
		
		
	}
	
	@Test
	public void createProcessInstance(){
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("sqcgProcess");
		System.out.println(processInstance.getId());
		
//		runtimeService.deleteProcessInstance("1401", "");
	}
	
	@Test
	public void getTask(){
//		List<Task> list = taskService.createTaskQuery().taskAssignee("2").list();
//		for (Task task : list) {
//			System.out.println(task.getName());
//			System.out.println(task.getId());
//		}
//		List<Task> list2 = taskService.createTaskQuery().taskCandidateGroup("1").list();
//		for (Task task : list2) {
//			System.out.println(task.getName());
//			System.out.println(task.getId());
//		}
//		List<Task> list3 = taskService.createTaskQuery().taskCandidateUser("2").list();
//		for (Task task : list3) {
//			System.out.println(task.getName());
//			System.out.println(task.getId());
//		}
		
		taskService.claim("1704", "6");
	}
	
	@Test
	public void completeTask(){
		taskService.complete("1704");
	}
}
