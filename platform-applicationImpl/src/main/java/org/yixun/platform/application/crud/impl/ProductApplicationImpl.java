package org.yixun.platform.application.crud.impl;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.crud.ProductApplication;
import org.yixun.platform.application.crud.dto.ProductDTO;
import org.yixun.platform.core.crud.Product;

import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

@Named
@Transactional
public class ProductApplicationImpl implements ProductApplication {
	
	@Inject
	private QueryChannelService queryChannel;
	
	public ProductDTO saveProduct(ProductDTO productDTO) {
		Product product = new Product();
		try {
			BeanUtils.copyProperties(product, productDTO);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		product.setId(null);//use EntityRepository persist method
		product.save();
		productDTO.setId(product.getId());
		return productDTO;
	}
	
	public void updateProduct(ProductDTO productDTO) {
		Product product = Product.get(Product.class, productDTO.getId());
		try {
			BeanUtils.copyProperties(product, productDTO);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<ProductDTO> findAllProduct(){
		List<ProductDTO> list = new ArrayList<ProductDTO>();
		List<Product> all = Product.findAll(Product.class);
		for (Product product : all) {
			ProductDTO productDTO = new ProductDTO();
			// 将domain转成VO
			try {
				BeanUtils.copyProperties(productDTO, product);
			} catch (Exception e) {
				e.printStackTrace();
			}
			list.add(productDTO);
		}
		return list;
	}
	
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public List<ProductDTO> queryProduct(ProductDTO queryVo,String sortname,String sortorder){
		StringBuilder jpql = new StringBuilder("select _product from Product _product where 1=1 ");
		List<Object> conditionVals = new ArrayList<Object>();
		
		if (queryVo.getProduct_name() != null && !"".equals(queryVo.getProduct_name())) {
	   		jpql.append(" and _product.product_name like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getProduct_name()));
	   	}
	   	
	   	if (queryVo.getProduct_date() != null) {
	   		jpql.append(" and _product.product_date between ? and ? ");
	   		conditionVals.add(queryVo.getProduct_date());
	   		GregorianCalendar gregorianCalendar = new GregorianCalendar();
	   		gregorianCalendar.setTime(queryVo.getProduct_dateEnd());
	   		gregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);
	   		gregorianCalendar.add(Calendar.SECOND, -1);
	   		Date endDate = gregorianCalendar.getTime();
	   		conditionVals.add(endDate);
	   	}
	   	
	   	if(sortname != null && !"".equals(sortname)){
	   		jpql.append(MessageFormat.format(" order by {0} {1} ", sortname,sortorder));
	   	}
		
	   	List<ProductDTO> result = new ArrayList<ProductDTO>();
	   	List<Product> productList = queryChannel.queryResult(jpql.toString(), conditionVals.toArray());
	   	
	   	ProductDTO productDTO = null;
	   	for (Product product : productList) {
	   		productDTO = new ProductDTO();
	   		try {
				BeanUtils.copyProperties(productDTO, product);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   		result.add(productDTO);
		}
		return result;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Page<ProductDTO> pageQueryProduct(ProductDTO queryVo, int currentPage, int pageSize) {
		List<ProductDTO> result = new ArrayList<ProductDTO>();
		List<Object> conditionVals = new ArrayList<Object>();
	   	StringBuilder jpql = new StringBuilder("select _product from Product _product where 1=1 ");
	
	   	if (queryVo.getProduct_name() != null && !"".equals(queryVo.getProduct_name())) {
	   		jpql.append(" and _product.product_name like ?");
	   		conditionVals.add(MessageFormat.format("%{0}%", queryVo.getProduct_name()));
	   	}
	   	
	   	if (queryVo.getProduct_date() != null) {
	   		jpql.append(" and _product.product_date between ? and ? ");
	   		conditionVals.add(queryVo.getProduct_date());
	   		GregorianCalendar gregorianCalendar = new GregorianCalendar();
	   		gregorianCalendar.setTime(queryVo.getProduct_dateEnd());
	   		gregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);
	   		Date endDate = gregorianCalendar.getTime();
	   		conditionVals.add(endDate);
	   	}
	
        Page<Product> pages = queryChannel.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), currentPage, pageSize);
        for (Product product : pages.getResult()) {
            ProductDTO productDTO = new ProductDTO();
            
             // 将domain转成VO
            try {
            	BeanUtils.copyProperties(productDTO, product);
            } catch (Exception e) {
            	e.printStackTrace();
            } 
            
            result.add(productDTO);
        }
        return new Page<ProductDTO>(pages.getCurrentPageNo(), pages.getTotalCount(), pages.getPageSize(), result);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ProductDTO getProduct(Long id) {
		Product product = Product.load(Product.class, id);
		ProductDTO productDTO = new ProductDTO();
		// 将domain转成VO
		try {
			BeanUtils.copyProperties(productDTO, product);
		} catch (Exception e) {
			e.printStackTrace();
		}
		productDTO.setId((java.lang.Long)product.getId());
		return productDTO;
	}

	public void removeProduct(Long id) {
		Product product = Product.load(Product.class, id);
		product.remove();
		
	}

	public void removeProducts(Long[] ids) {
		for (Long id : ids) {
			Product product = Product.load(Product.class, id);
			product.remove();
		}
		
	}

	

}
