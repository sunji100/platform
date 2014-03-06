package org.yixun.platform.application.security.util;

import java.util.Date;

import org.yixun.platform.application.security.dto.IdentityDTO;
import org.yixun.platform.core.security.Identity;
import org.yixun.platform.infra.auth.AuthDetailUtil;

import com.dayatang.utils.DateUtils;

public class IdentityBeanUtil {
	public static void domainToDTO(IdentityDTO identityDTO,Identity identity){
		identityDTO.setId(identity.getId());
		identityDTO.setName(identity.getName());
		identityDTO.setSortOrder(identity.getSortOrder());
		identityDTO.setValid(identity.isValid());
		identityDTO.setLastLoginTime(identity.getLastLoginTime());
		identityDTO.setLastModifyTime(identity.getLastModifyTime());
		identityDTO.setUserAccount(identity.getUserAccount());
		identityDTO.setUserDesc(identity.getUserDesc());
		identityDTO.setUserPassword(identity.getUserPassword());
	}
	
	public static void dtoToDomain(IdentityDTO identityDTO,Identity identity){
		identity.setId(identityDTO.getId());
		identity.setName(identityDTO.getName());
		identity.setSortOrder(identityDTO.getSortOrder());
		identity.setValid(identityDTO.isValid());
		identity.setLastLoginTime(identityDTO.getLastLoginTime());
		identity.setLastModifyTime(identityDTO.getLastModifyTime());
		identity.setUserAccount(identityDTO.getUserAccount());
		identity.setUserDesc(identityDTO.getUserDesc());
		identity.setUserPassword(identityDTO.getUserPassword());
		
		identity.setAbolishDate(DateUtils.MAX_DATE);
		identity.setCreateDate(new Date());
		
		identity.setCreateOwner(AuthDetailUtil.getLoginName());
	}
}
