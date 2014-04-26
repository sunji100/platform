package org.yixun.platform.web.excel.view;

import java.util.List;

public class ExcelRow {
	private List<ExcelCell> cell;
	private String height;

	public List<ExcelCell> getCell() {
		return cell;
	}

	public void setCell(List<ExcelCell> cell) {
		this.cell = cell;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
}
