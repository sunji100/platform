package org.yixun.platform.application.impl.crud;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.mapping.Array;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.yixun.platform.core.crud.Product;
import org.yixun.support.jdbc.util.PageUtil;

import com.dayatang.querychannel.support.Page;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/root.xml"})
@TransactionConfiguration(transactionManager = "transactionManager",defaultRollback = false) 
public class JdbcTemplateTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Inject
	private JdbcTemplate jdbcTemplate;
	@Test
	public void query() {
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("select * from T_PRODUCT");
		for (Map<String, Object> map : queryForList) {
			System.out.println(map);
		}
	}
	
	@Test
	public void queryMapper(){
		List<Product> query = jdbcTemplate.query("select * from T_PRODUCT", new UserRowMapper());
		for (Product product : query) {
			System.out.println(product);
		}
	}
	
	class UserRowMapper implements RowMapper<Product>{

		@Override
		public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
			Product p = new Product();
			p.setProduct_name(rs.getString("PRODUCT_NAME"));
			return p;
		}
		
	}
	
	@Test
	public void page(){
		List<Object> cod = new ArrayList<>();
		cod.add("dasf");
		cod.add(1);
		//Page<Product> pages = PageUtil.queryPagedResultByPageNo("select * from T_PRODUCT where product_name=?", cod.toArray(), 1, 5, jdbcTemplate, new UserRowMapper());
		Page<Map<String, Object>> pages = PageUtil.queryPagedResultByPageNo("select * from T_PRODUCT where product_name=? and version = ?", cod.toArray(), 1, 5, jdbcTemplate);
		System.out.println(pages.getTotalCount());
	}
	/*
	public Pagination(String sql,int currentPage,int numPerPage,JdbcTemplate jTemplate){
	    if(jTemplate == null){
	      throw new IllegalArgumentException("com.deity.ranking.util.Pagination.jTemplate is null,please initial it first. ");
	    }else if(sql == null || sql.equals("")){
	      throw new IllegalArgumentException("com.deity.ranking.util.Pagination.sql is empty,please initial it first. ");
	    }
	    //设置每页显示记录数
	    setNumPerPage(numPerPage);
	    //设置要显示的页数
	    setCurrentPage(currentPage);
	    //计算总记录数
	    StringBuffer totalSQL = new StringBuffer(" SELECT count(*) FROM ( ");
	    totalSQL.append(sql);
	    totalSQL.append(" ) totalTable ");
	    //给JdbcTemplate赋值
	    setJdbcTemplate(jTemplate);
	    //总记录数
	    setTotalRows(getJdbcTemplate().queryForInt(totalSQL.toString()));
	    //计算总页数
	    setTotalPages();
	    //计算起始行数
	    setStartIndex();
	    //计算结束行数
	    setLastIndex();
	    System.out.println("lastIndex="+lastIndex);//////////////////
	    //构造oracle数据库的分页语句
	    StringBuffer paginationSQL = new StringBuffer(" SELECT * FROM ( ");
	    paginationSQL.append(" SELECT temp.* ,ROWNUM num FROM ( ");
	    paginationSQL.append(sql);
	    paginationSQL.append(" ) temp where ROWNUM <= " + lastIndex);
	    paginationSQL.append(" ) WHERE num > " + startIndex);
	    //装入结果集
	    setResultList(getJdbcTemplate().queryForList(paginationSQL.toString()));
	  }*/
}
