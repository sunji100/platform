package org.yixun.platform.web.log;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yixun.platform.application.log.LogApplication;
import org.yixun.platform.application.log.dto.LogDTO;

import com.dayatang.querychannel.support.Page;

@Controller
@RequestMapping("/log")
public class LogController {
	@Inject
	private LogApplication logApplication;
	
	/**
	 * 查询日志
	 * @param queryDTO
	 * @param page
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/pageQueryLog")
	public Map<String, Object> pageQueryUser(LogDTO queryDTO,int page,int pagesize) throws Exception {
		
		Page<LogDTO> pages = logApplication.pageQueryLog(queryDTO, page, pagesize);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("Rows", pages.getResult());
		result.put("Total", pages.getTotalCount());
		
		return result;
	}
}
