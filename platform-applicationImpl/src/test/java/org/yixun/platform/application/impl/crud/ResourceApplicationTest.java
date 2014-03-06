package org.yixun.platform.application.impl.crud;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.yixun.platform.application.security.ResourceApplication;
import org.yixun.platform.application.security.dto.ResourceDTO;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.spring.factory.SpringInstanceProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/root.xml"})
@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback = true) 
public class ResourceApplicationTest extends AbstractTransactionalJUnit4SpringContextTests {
	protected static final Logger logger = LoggerFactory.getLogger(ProductApplicationTest.class);

    @Before
    public void setup() {
        logger.debug("applicationContext: "+applicationContext);
        InstanceFactory.setInstanceProvider(new SpringInstanceProvider(applicationContext));
    }
    
    @Test
    public void findTopLevelMenu(){
    	ResourceApplication resourceApplication = applicationContext.getBean(ResourceApplication.class);
    	List<ResourceDTO> menus = resourceApplication.findMenuByUser("user1");
//    	List<ResourceDTO> menus = resourceApplication.findTopLevelMenuByUser("user1");
    	for (ResourceDTO menu : menus) {
			System.out.println(menu.getText());
			System.out.println(menu.getMenuType());
		}
    	
    }
    
   

    @After
    public void teardown() {}
}
