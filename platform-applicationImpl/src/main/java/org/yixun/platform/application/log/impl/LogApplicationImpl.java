package org.yixun.platform.application.log.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yixun.platform.application.log.LogApplication;
import org.yixun.platform.application.log.dto.LogDTO;
import org.yixun.platform.application.log.util.LogBeanUtil;
import org.yixun.platform.application.wssecurity.dto.UserDTO;
import org.yixun.platform.application.wssecurity.util.UserBeanUtil;
import org.yixun.platform.core.log.Log;
import org.yixun.platform.core.wssecurity.WsUser;

import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

@Named
@Transactional
public class LogApplicationImpl implements LogApplication {

	@Inject
	private QueryChannelService queryChannelService;
	
	@Override
	public void addLog(LogDTO logDTO) throws Exception {
		Log log = new Log();
		LogBeanUtil.dtoToDomain(log, logDTO);
		log.save();
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS,readOnly=true)
	public Page<LogDTO> pageQueryLog(LogDTO queryDTO, int page, int pagesize) throws Exception {
		StringBuilder jpql = new StringBuilder("select _log from Log _log where 1=1");
		List<Object> conditionVals = new ArrayList<Object>();
		
		if(!StringUtils.isEmpty(queryDTO.getLoginName())){
			jpql.append(" and _log.loginName like ?");
			conditionVals.add(MessageFormat.format("%{0}%", queryDTO.getLoginName()));
		}
		
		if(null != queryDTO.getCreateDate()){
			jpql.append(" and _log.createDate between ? and ?");
			conditionVals.add(queryDTO.getCreateDate());
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
	   		gregorianCalendar.setTime(queryDTO.getCreateDate_end());
	   		gregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);
	   		gregorianCalendar.add(Calendar.SECOND, -1);
	   		Date endDate = gregorianCalendar.getTime();
	   		conditionVals.add(endDate);
		}
		
		jpql.append(" order by _log.createDate desc");
		
		Page<Log> pages = queryChannelService.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), page, pagesize);
		
		List<LogDTO> logDTOs = new ArrayList<LogDTO>();
		LogDTO logDTO = null;
		for (Log log : pages.getResult()) {
			logDTO = new LogDTO();
			LogBeanUtil.domainToDTO(logDTO, log);
			logDTOs.add(logDTO);
		}
		return new Page<LogDTO>(pages.getCurrentPageNo(),pages.getTotalCount(),pages.getPageSize(),logDTOs);
	}

}
