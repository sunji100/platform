package org.yixun.platform.application.crud.dto;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.openkoala.koala.springmvc.JsonDateSerializer;
import org.springframework.format.annotation.DateTimeFormat;

public class ProductDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5873825807708108495L;
	private Long id;
	private String product_name;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date product_date;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date product_dateEnd;
	
	public Date getProduct_dateEnd() {
		return product_dateEnd;
	}
	public void setProduct_dateEnd(Date product_dateEnd) {
		this.product_dateEnd = product_dateEnd;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getProduct_date() {
		return product_date;
	}
	public void setProduct_date(Date product_date) {
		this.product_date = product_date;
	}
}
