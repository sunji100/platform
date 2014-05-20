package org.yixun.platform.application.log.util;

import org.yixun.platform.application.log.dto.LogDTO;
import org.yixun.platform.core.log.Log;

public class LogBeanUtil {
	public static void domainToDTO(LogDTO dest,Log orig){
		dest.setId(orig.getId());
		dest.setArgs(orig.getArgs());
		dest.setClassName(orig.getClassName());
		dest.setCreateDate(orig.getCreateDate());
		dest.setIp(orig.getIp());
		dest.setLoginName(orig.getLoginName());
		dest.setMethod(orig.getMethod());
		dest.setRemark(orig.getRemark());
	}
	
	public static void dtoToDomain(Log dest,LogDTO orig){
		dest.setId(orig.getId());
		dest.setArgs(orig.getArgs());
		dest.setClassName(orig.getClassName());
		dest.setCreateDate(orig.getCreateDate());
		dest.setIp(orig.getIp());
		dest.setLoginName(orig.getLoginName());
		dest.setMethod(orig.getMethod());
		dest.setRemark(orig.getRemark());
	}
}
