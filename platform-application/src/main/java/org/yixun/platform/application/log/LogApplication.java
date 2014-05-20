package org.yixun.platform.application.log;

import org.yixun.platform.application.log.dto.LogDTO;

import com.dayatang.querychannel.support.Page;

public interface LogApplication {
	/**
	 * 增加日志
	 * @param logDTO
	 * @throws Exception
	 */
	public void addLog(LogDTO logDTO) throws Exception;
	
	/**
	 * 
	 * @param queryDTO
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	public Page<LogDTO> pageQueryLog(LogDTO queryDTO,int page,int pagesize) throws Exception;
}
