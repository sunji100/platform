package org.yixun.platform.application.security.util;

import java.util.Date;

import org.yixun.platform.application.security.dto.MenuDTO;
import org.yixun.platform.application.security.dto.OrgDTO;
import org.yixun.platform.core.security.Org;
import org.yixun.platform.core.security.Resource;

import com.dayatang.utils.DateUtils;

public class OrgBeanUtil {
	public static void domainToDTO(OrgDTO dest,Org orig){
		dest.setId(orig.getId());
		dest.setText(orig.getName());
		dest.setLevel(orig.getLevel());
		dest.setSortOrder(orig.getSortOrder());
		dest.setOrgDesc(orig.getOrgDesc());
		dest.setIcon("images/icons/menu/member.gif");
	}
	
	public static void dtoToDomain(Org dest,OrgDTO orig){
		dest.setId(orig.getId());
		dest.setName(orig.getText());
		dest.setLevel(orig.getLevel());
		dest.setSortOrder(orig.getSortOrder());
		dest.setOrgDesc(orig.getOrgDesc());
		
		dest.setAbolishDate(DateUtils.MAX_DATE);
		dest.setCreateDate(new Date());
	}
}
