package org.yixun.platform.application.security.util;

import java.util.Date;

import org.yixun.platform.application.security.dto.ResourceDTO;
import org.yixun.platform.core.security.Resource;

import com.dayatang.utils.DateUtils;

public class ResourceBeanUtil {
	public static void domainToDTO(ResourceDTO dest,Resource orig){
		dest.setId(orig.getId());
		dest.setText(orig.getName());
		dest.setSortOrder(orig.getSortOrder());
		dest.setDescription(orig.getDescription());
		dest.setIdentifier(orig.getIdentifier());
		dest.setLevel(orig.getLevel());
		dest.setIcon(orig.getMenuIcon());
	}
	
	public static void dtoToDomain(Resource dest,ResourceDTO orig){
		dest.setId(orig.getId());
		dest.setName(orig.getText());
		dest.setSortOrder(orig.getSortOrder());
		dest.setDescription(orig.getDescription());
		dest.setIdentifier(orig.getIdentifier());
		dest.setLevel(orig.getLevel());
		dest.setMenuIcon(orig.getIcon());
		
		dest.setAbolishDate(DateUtils.MAX_DATE);
		dest.setCreateDate(new Date());
	}
}
