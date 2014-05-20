package org.yixun.platform.application.wssecurity.util;

import java.util.Date;

import org.yixun.platform.application.wssecurity.dto.ResourceDTO;
import org.yixun.platform.core.wssecurity.WsResource;

import com.dayatang.utils.DateUtils;

public class ResourceBeanUtil {
	public static void domainToDTO(ResourceDTO dest,WsResource orig){
		dest.setId(orig.getId());
		dest.setText(orig.getName());
		dest.setSortOrder(orig.getSortOrder());
		dest.setDescription(orig.getDescription());
		dest.setIdentifier(orig.getIdentifier());
		dest.setLevel(orig.getLevel());
		dest.setIcon(orig.getMenuIcon());
	}
	
	public static void dtoToDomain(WsResource dest,ResourceDTO orig){
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
