package org.yixun.platform.application.security.util;

import java.util.Date;

import org.yixun.platform.application.security.dto.RoleDTO;
import org.yixun.platform.core.security.Role;

import com.dayatang.utils.DateUtils;

public class RoleBeanUtil {
	public static void domainToDTO(RoleDTO dest,Role orig){
		dest.setId(orig.getId());
		dest.setName(orig.getName());
		dest.setRoleDesc(orig.getRoleDesc());
		dest.setSortOrder(orig.getSortOrder());
		dest.setValid(orig.isValid());
	}
	
	public static void dtoToDomain(Role dest,RoleDTO orig){
		dest.setId(orig.getId());
		dest.setName(orig.getName());
		dest.setRoleDesc(orig.getRoleDesc());
		dest.setSortOrder(orig.getSortOrder());
		dest.setValid(orig.isValid());
		
		dest.setAbolishDate(DateUtils.MAX_DATE);
		dest.setCreateDate(new Date());
		
	}
}
