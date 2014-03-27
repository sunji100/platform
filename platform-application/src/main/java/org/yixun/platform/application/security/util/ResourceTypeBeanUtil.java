package org.yixun.platform.application.security.util;

import java.util.Date;

import org.yixun.platform.application.security.dto.ResourceTypeDTO;
import org.yixun.platform.application.security.dto.RoleDTO;
import org.yixun.platform.core.security.ResourceType;
import org.yixun.platform.core.security.Role;
import org.yixun.platform.infra.auth.AuthDetailUtil;

import com.dayatang.utils.DateUtils;

public class ResourceTypeBeanUtil {
	public static void domainToDTO(ResourceTypeDTO dest,ResourceType orig){
		dest.setId(orig.getId());
		dest.setName(orig.getName());
		dest.setSortOrder(orig.getSortOrder());
	}
	
	public static void dtoToDomain(ResourceType dest,ResourceTypeDTO orig){
		dest.setId(orig.getId());
		dest.setName(orig.getName());
		dest.setSortOrder(orig.getSortOrder());
		
		dest.setAbolishDate(DateUtils.MAX_DATE);
		dest.setCreateDate(new Date());
	}
}
