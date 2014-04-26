package org.yixun.support.excel;

public class ExcelCellStyle {
	private int colspan;
	private int width;
	private String align;
	private String valign;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getColspan() {
		return colspan;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
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
}
