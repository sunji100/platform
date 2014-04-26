package org.yixun.platform.web.excel.view;

//[{"cell":[{"columnname":"name","text":"资源类型名称","colspan":"2","width":"400px"}]},{"cell":[{"columnname":"name","text":"资源类型名称","width":"200px"},{"columnname":"name","text":"aaaa","width":"200px"}]}] 
public class ExcelCell {
	private String columnname;
	private String text;
	private String colspan;
	private String rowspan;
	private String align;
	private String valign;
	private String width;
	private String heigth;
	public String getColumnname() {
		return columnname;
	}
	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getColspan() {
		return colspan;
	}
	public void setColspan(String colspan) {
		this.colspan = colspan;
	}
	public String getRowspan() {
		return rowspan;
	}
	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public String getValign() {
		return valign;
	}
	public void setValign(String valign) {
		this.valign = valign;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeigth() {
		return heigth;
	}
	public void setHeigth(String heigth) {
		this.heigth = heigth;
	}
}
