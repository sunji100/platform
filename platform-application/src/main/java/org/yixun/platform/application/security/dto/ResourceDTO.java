package org.yixun.platform.application.security.dto;

import java.util.List;

import org.yixun.platform.core.security.Resource;

public class ResourceDTO {
	private Long id;
	private String text;
	private int sortOrder;
	private String description;
	private String identifier;
	private String level;
	private String icon;
	private String menuType;
	private List<ResourceDTO> children;
	
	public void domainToDTO(Resource resource){
		this.setId(resource.getId());
		this.setText(resource.getName());
		this.setSortOrder(resource.getSortOrder());
		this.setDescription(resource.getDescription());
		this.setIdentifier(resource.getIdentifier());
		this.setLevel(resource.getLevel());
		this.setIcon(resource.getMenuIcon());
		this.setMenuType(resource.getResourceType().getName());
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
	public List<ResourceDTO> getChildren() {
		return children;
	}
	public void setChildren(List<ResourceDTO> children) {
		this.children = children;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	
}
