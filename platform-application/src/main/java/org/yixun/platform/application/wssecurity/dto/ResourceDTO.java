package org.yixun.platform.application.wssecurity.dto;

public class ResourceDTO {
	private Long id;
	private String text;
	private int sortOrder;
	private String description;
	private String identifier;
	private String level;
	private String icon;

	public ResourceDTO() {

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

	

	@Override
	public String toString() {
		return "ResourceDTO [id=" + id + ", text=" + text + ", sortOrder=" + sortOrder + ", description=" + description + ", identifier=" + identifier + ", level=" + level + ", icon=" + icon + "]";
	}
}
