package org.yixun.platform.application.process.util;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.yixun.platform.application.crud.dto.ProductDTO;
import org.yixun.platform.core.crud.Product;

public class ProductBeanUtil {
	public static void domainToDTO(ProductDTO dest,Product orig){
		try {
			BeanUtils.copyProperties(dest, orig);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void dtoToDomain(Product dest,ProductDTO orig){
		try {
			BeanUtils.copyProperties(dest, orig);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
