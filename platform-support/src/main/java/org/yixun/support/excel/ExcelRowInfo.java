package org.yixun.support.excel;

import java.util.List;

public class ExcelRowInfo {
	private List<ExcelCellInfo> cells;
	private float height;
	public List<ExcelCellInfo> getCells() {
		return cells;
	}
	public void setCells(List<ExcelCellInfo> cells) {
		this.cells = cells;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
}
