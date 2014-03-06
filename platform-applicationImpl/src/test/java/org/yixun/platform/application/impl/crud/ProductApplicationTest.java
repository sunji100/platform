package org.yixun.platform.application.impl.crud;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openkoala.koala.util.KoalaBaseSpringTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.crud.ProductApplication;
import org.yixun.platform.application.crud.dto.ProductDTO;

import com.dayatang.domain.EntityRepository;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.spring.factory.SpringInstanceProvider;

@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = {"classpath*:META-INF/spring/root.xml"})
@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback = false) 
public class ProductApplicationTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	protected static final Logger logger = LoggerFactory.getLogger(ProductApplicationTest.class);

    @Before
    public void setup() {
        logger.debug("applicationContext: "+applicationContext);
        InstanceFactory.setInstanceProvider(new SpringInstanceProvider(applicationContext));
    }
    
    @Test
    public void save(){
    	ProductApplication productApplication = applicationContext.getBean(ProductApplication.class);
    	
    	ProductDTO productDTO = new ProductDTO();
    	productDTO.setProduct_name("东巴拉");
    	productDTO.setProduct_date(new Date());
    	
    	productApplication.saveProduct(productDTO);
    }
    
    @Test
    public void findAll(){
    	ProductApplication productApplication = applicationContext.getBean(ProductApplication.class);
    	List<ProductDTO> list = productApplication.findAllProduct();
    }

    @After
    public void teardown() {}

}
