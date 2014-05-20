package org.yixun.support.jdbc.util;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.dayatang.querychannel.support.Page;

public class PageUtil {
	public static <T> Page<T> queryPagedResultByPageNo(String queryStr,
			Object[] params, int currentPage, int pageSize,JdbcTemplate jdbcTemplate,RowMapper<T> rowMapper){
		StringBuffer totalSQL = new StringBuffer(" SELECT count(*) FROM ( ");
	    totalSQL.append(queryStr);
	    totalSQL.append(" ) totalTable ");
	    int totalCount = jdbcTemplate.queryForInt(totalSQL.toString(),params);
	    
	    int startIndex = getStartOfPage(currentPage,pageSize);
	    int lastIndex = getLastOfPage(currentPage,pageSize);
	    StringBuffer paginationSQL = new StringBuffer(" SELECT * FROM ( ");
	    paginationSQL.append(" SELECT temp.* ,ROWNUM num FROM ( ");
	    paginationSQL.append(queryStr);
	    paginationSQL.append(" ) temp where ROWNUM <= " + lastIndex);
	    paginationSQL.append(" ) WHERE num > " + startIndex);
	    
	    List<T> data = jdbcTemplate.query(paginationSQL.toString(),params,rowMapper);
	    return new Page<T>(startIndex, totalCount, pageSize, data);
	
	}
	
	public static  Page<Map<String, Object>> queryPagedResultByPageNo(String queryStr,
			Object[] params, int currentPage, int pageSize,JdbcTemplate jdbcTemplate){
		StringBuffer totalSQL = new StringBuffer(" SELECT count(*) FROM ( ");
	    totalSQL.append(queryStr);
	    totalSQL.append(" ) totalTable ");
	    int totalCount = jdbcTemplate.queryForInt(totalSQL.toString(),params);
	    
	    int startIndex = getStartOfPage(currentPage,pageSize);
	    int lastIndex = getLastOfPage(currentPage,pageSize);
	    StringBuffer paginationSQL = new StringBuffer(" SELECT * FROM ( ");
	    paginationSQL.append(" SELECT temp.* ,ROWNUM num FROM ( ");
	    paginationSQL.append(queryStr);
	    paginationSQL.append(" ) temp where ROWNUM <= " + lastIndex);
	    paginationSQL.append(" ) WHERE num > " + startIndex);
	    
	    List<Map<String, Object>> data = jdbcTemplate.queryForList(paginationSQL.toString(), params);
	    return new Page<Map<String, Object>>(startIndex, totalCount, pageSize, data);
	
	}
	
	public static int getStartOfPage(int pageNo, int pageSize) {
		return (pageNo - 1) * pageSize;
	}
	
	public static int getLastOfPage(int pageNo, int pageSize) {
		return pageNo * pageSize;
	}
}
