package org.yixun.platform.application.security.util;

import java.util.Date;

import org.yixun.platform.application.security.dto.MenuDTO;
import org.yixun.platform.core.security.Resource;

import com.dayatang.utils.DateUtils;

public class MenuBeanUtil {
	public static void domainToDTO(MenuDTO dest,Resource orig){
		dest.setId(orig.getId());
		dest.setText(orig.getName());
		dest.setSortOrder(orig.getSortOrder());
		dest.setDescription(orig.getDescription());
		dest.setIdentifier(orig.getIdentifier());
		dest.setLevel(orig.getLevel());
		dest.setIcon(orig.getMenuIcon());
	}
	
	public static void dtoToDomain(Resource dest,MenuDTO orig){
		dest.setId(orig.getId());
		dest.setName(orig.getText());
		dest.setSortOrder(orig.getSortOrder());
		dest.setDescription(orig.getDescription());
		dest.setIdentifier(orig.getIdentifier());
		dest.setMenuIcon(orig.getIcon());
		
		dest.setAbolishDate(DateUtils.MAX_DATE);
		dest.setCreateDate(new Date());
	}
}
