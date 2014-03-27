package org.yixun.platform.application.security.dto;

import java.util.List;

import org.yixun.platform.core.security.Resource;

public class MenuDTO {
	private Long id;
	private String text;
	private int sortOrder;
	private String description;
	private String identifier;
	private String level;
	private String icon;
	private String menuType;
	private Long menuTypeId;
	private Long parentId;
	private String parentText;
	private List<MenuDTO> children;
	
	
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
	
	public List<MenuDTO> getChildren() {
		return children;
	}
	public void setChildren(List<MenuDTO> children) {
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
	public Long getMenuTypeId() {
		return menuTypeId;
	}
	public void setMenuTypeId(Long menuTypeId) {
		this.menuTypeId = menuTypeId;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getParentText() {
		return parentText;
	}
	public void setParentText(String parentText) {
		this.parentText = parentText;
	}
	@Override
	public String toString() {
		return "MenuDTO [id=" + id + ", text=" + text + ", sortOrder=" + sortOrder + ", description=" + description + ", identifier=" + identifier + ", level=" + level + ", icon=" + icon
				+ ", menuType=" + menuType + ", menuTypeId=" + menuTypeId + ", parentId=" + parentId + ", parentText=" + parentText + "]";
	}
	
	
}
