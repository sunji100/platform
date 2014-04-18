package org.yixun.platform.application.security.dto;

import java.io.Serializable;

public class ResourceTypeDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3257524183057689210L;
	private Long id;
	private String name;
	private int sortOrder;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	@Override
	public String toString() {
		return "ResourceTypeDTO [id=" + id + ", name=" + name + ", sortOrder=" + sortOrder + "]";
	}
}
