package org.yixun.support.excel;

public class ExcelCellInfo {
	private Object content;
	private ExcelCellStyle cellStyle;
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	public ExcelCellStyle getCellStyle() {
		return cellStyle;
	}
	public void setCellStyle(ExcelCellStyle cellStyle) {
		this.cellStyle = cellStyle;
	}
}
