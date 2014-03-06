package org.yixun.platform.application.security.util;

import java.util.Date;

import org.yixun.platform.application.security.dto.IdentityDTO;
import org.yixun.platform.application.security.dto.RoleDTO;
import org.yixun.platform.core.security.Identity;
import org.yixun.platform.core.security.Role;
import org.yixun.platform.infra.auth.AuthDetailUtil;

import com.dayatang.utils.DateUtils;

public class RoleBeanUtil {
	public static void domainToDTO(RoleDTO dest,Role orig){
		dest.setId(orig.getId());
		dest.setName(orig.getName());
		dest.setRoleDesc(orig.getRoleDesc());
		dest.setSortOrder(orig.getSortOrder());
		dest.setValid(orig.isValid());
	}
	
	public static void dtoToDomain(Role desc,RoleDTO orig){
		desc.setId(orig.getId());
		desc.setName(orig.getName());
		desc.setRoleDesc(orig.getRoleDesc());
		desc.setSortOrder(orig.getSortOrder());
		desc.setValid(orig.isValid());
		
		desc.setAbolishDate(DateUtils.MAX_DATE);
		desc.setCreateDate(new Date());
		
		desc.setCreateOwner(AuthDetailUtil.getLoginName());
	}
}
