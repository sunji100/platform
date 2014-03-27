package org.yixun.platform.application.security.dto;

import org.yixun.platform.application.security.util.ResourceBeanUtil;
import org.yixun.platform.core.security.Resource;

public class ResourceDTO {
	private Long id;
	private String text;
	private int sortOrder;
	private String description;
	private String identifier;
	private String level;
	private String icon;
	private String resourceType;
	private Long resourceTypeId;
	private String parentIds;
	
	public ResourceDTO(){
		
	}
	
	public ResourceDTO(Resource resource,String resourceType){
		ResourceBeanUtil.domainToDTO(this,resource);
		this.resourceType = resourceType;
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

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Long getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@Override
	public String toString() {
		return "ResourceDTO [id=" + id + ", text=" + text + ", sortOrder=" + sortOrder + ", description=" + description + ", identifier=" + identifier + ", level=" + level + ", icon=" + icon
				+ ", resourceType=" + resourceType + ", resourceTypeId=" + resourceTypeId + ", parentIds=" + parentIds + "]";
	}

	

	
	
}
