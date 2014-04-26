package org.yixun.platform.application.wssecurity.util;

import java.util.Date;

import org.yixun.platform.application.security.dto.IdentityDTO;
import org.yixun.platform.application.wssecurity.dto.UserDTO;
import org.yixun.platform.core.security.Identity;
import org.yixun.platform.core.wssecurity.WsUser;

import com.dayatang.utils.DateUtils;

public class UserBeanUtil {
	public static void domainToDTO(UserDTO dest,WsUser orig){
		dest.setId(orig.getId());
		dest.setName(orig.getName());
		dest.setSortOrder(orig.getSortOrder());
		dest.setValid(orig.isValid());
		dest.setLastLoginTime(orig.getLastLoginTime());
		dest.setLastModifyTime(orig.getLastModifyTime());
		dest.setUserAccount(orig.getUserAccount());
		dest.setUserDesc(orig.getUserDesc());
		dest.setUserPassword(orig.getUserPassword());
	}
	
	public static void dtoToDomain(WsUser dest,UserDTO orig){
		dest.setId(orig.getId());
		dest.setName(orig.getName());
		dest.setSortOrder(orig.getSortOrder());
		dest.setValid(orig.isValid());
		dest.setLastLoginTime(orig.getLastLoginTime());
		dest.setLastModifyTime(orig.getLastModifyTime());
		dest.setUserAccount(orig.getUserAccount());
		dest.setUserDesc(orig.getUserDesc());
		dest.setUserPassword(orig.getUserPassword());
		
		dest.setAbolishDate(DateUtils.MAX_DATE);
		dest.setCreateDate(new Date());
	}
}
