package org.yixun.platform.application.security.dto;

import java.util.List;

public class OrgDTO {
	private Long id;
	private String text;
	private int level;
	private int sortOrder;
	private String orgDesc;
	private String type;
	private String icon;
	private Long parentId;
	private List<OrgDTO> children;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getOrgDesc() {
		return orgDesc;
	}
	public void setOrgDesc(String orgDesc) {
		this.orgDesc = orgDesc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public List<OrgDTO> getChildren() {
		return children;
	}
	public void setChildren(List<OrgDTO> children) {
		this.children = children;
	}
	@Override
	public String toString() {
		return "OrgDTO [id=" + id + ", text=" + text + ", level=" + level + ", sortOrder=" + sortOrder + ", orgDesc=" + orgDesc + ", type=" + type + ", icon=" + icon + ", parentId=" + parentId + "]";
	}
}
