package org.yixun.support.excel;

import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelReaderCallback<T> {
	T doInPoi(Workbook workbook);
}
