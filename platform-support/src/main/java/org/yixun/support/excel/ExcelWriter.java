package org.yixun.support.excel;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Excel书写类。向Excel文件中填写内容
 * 
 * @author yyang
 * 
 */
public class ExcelWriter {

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private ExcelWriterTemplate excelTemplate;

	public ExcelWriter(File outputFile) {
		excelTemplate = new ExcelWriterTemplate(outputFile);
	}

	public ExcelWriter(OutputStream out) {
		excelTemplate = new ExcelWriterTemplate(out);
	}

	public void setTemplateFile(File templateFile) {
		excelTemplate.setTemplateFile(templateFile);
	}
	
	public void setTemplateFile(InputStream in, Version version) {
		excelTemplate.setTemplateFile(in, version);
	}
	
	

	/**
	 * 用一批数据填写指定工作表中的一个区域
	 * 
	 * @param sheetName
	 * @param rowFrom
	 * @param colFrom
	 * @param data
	 */
	public void write(final int sheetIndex, final int rowFrom, final int colFrom, final List<Object[]> data) {
		excelTemplate.execute(new ExcelWriterCallback() {

			@Override
			public void doInPoi(Workbook workbook) {
				Sheet sheet;
				if (workbook.getNumberOfSheets() == 0) {
					sheet = workbook.createSheet("sheet1");
				} else {
					sheet = workbook.getSheetAt(sheetIndex);
				}
				write(sheet, rowFrom, colFrom, data);
			}
		});
	}

	/**
	 * 用一批数据填写指定工作表中的一个区域
	 * 
	 * @param sheetName
	 * @param rowFrom
	 * @param colFrom
	 * @param data
	 */
	public<T> void write(final String sheetName, final int rowFrom, final int colFrom, final List<T> data) {
		excelTemplate.execute(new ExcelWriterCallback() {

			@Override
			public void doInPoi(Workbook workbook) {
				Sheet sheet = workbook.getSheet(sheetName);
				if (sheet == null) {
					sheet = workbook.createSheet(sheetName);
				}
				write(sheet, rowFrom, colFrom, data);
			}
		});
	}
	
	

	/**
	 * 向指定工作表的特定单元格填充内容
	 * 
	 * @param sheetIndex
	 * @param row
	 * @param col
	 * @param value
	 */
	public void write(final int sheetIndex, final int rowIndex, final int colIndex, final Object value) {
		excelTemplate.execute(new ExcelWriterCallback() {

			@Override
			public void doInPoi(Workbook workbook) {
				Sheet sheet;
				if (workbook.getNumberOfSheets() == 0) {
					sheet = workbook.createSheet("sheet1");
				} else {
					sheet = workbook.getSheetAt(sheetIndex);
				}
				Row row = sheet.createRow(rowIndex);
				Cell cell = row.createCell(colIndex);
				setCellValue(cell, value);
			}
		});
	}
 
	private<T> void write(Sheet sheet, int rowFrom, int colFrom, List<T> data) {
		int rowIndex = rowFrom;
		for (T dataRow : data) {
			Row row = sheet.createRow(rowIndex);
			int colIndex = colFrom;
			if(dataRow instanceof Object[]){//不带样式的纯数据
				for (Object dataCell : (Object[])dataRow) {
					Cell cell = row.createCell(colIndex);
					setCellValue(cell, dataCell);
					colIndex++;
				} 
			} else if(dataRow instanceof ExcelRowInfo){//带样式的数据
				ExcelRowInfo dataRowInfo = (ExcelRowInfo)dataRow;
				//设置行高
				if(0 != dataRowInfo.getHeight()){
					row.setHeightInPoints(dataRowInfo.getHeight());
				}
				//获取每个单元格
				for (ExcelCellInfo dataCellInfo : dataRowInfo.getCells()) {
					
					Cell cell = row.createCell(colIndex);
					//设置单元格值
					setCellValue(cell, dataCellInfo.getContent());
					//获得传递过来的单元格样式
					ExcelCellStyle excelCellStyle = dataCellInfo.getCellStyle();
					//单元格合并(只考虑列合并)
					if (0 != excelCellStyle.getColspan()) {
						CellRangeAddress region = new CellRangeAddress(rowIndex, rowIndex, colIndex, colIndex + excelCellStyle.getColspan() - 1);
						//设置合并单元格的边框样式
						setRegionStyle(sheet,region);
						sheet.addMergedRegion(region);
					}
					//设置单元格宽度
					if(0 != excelCellStyle.getWidth()){
						sheet.setColumnWidth(colIndex, excelCellStyle.getWidth()/10%255*256);
					}
					
					CellStyle cellStyle = row.getSheet().getWorkbook().createCellStyle();
					if("center".equalsIgnoreCase(excelCellStyle.getAlign())){
						cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
					} else if("left".equalsIgnoreCase(excelCellStyle.getAlign())){
						cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
					} else if("right".equalsIgnoreCase(excelCellStyle.getAlign())){
						cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
					}
					
					if("center".equalsIgnoreCase(excelCellStyle.getValign())){
						cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
					} else if("top".equalsIgnoreCase(excelCellStyle.getValign())){
						cellStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
					} else if("bottom".equalsIgnoreCase(excelCellStyle.getValign())){
						cellStyle.setVerticalAlignment(CellStyle.VERTICAL_BOTTOM);
					}
					
					cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
					cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
					cellStyle.setBorderRight(CellStyle.BORDER_THIN);
					cellStyle.setBorderTop(CellStyle.BORDER_THIN);
					cell.setCellStyle(cellStyle);
					colIndex++;
				}
			}
			rowIndex++;
		}
	}
	/**
	 * 设置合并单元格的边框样式（暂只考虑列合并）
	 * @param sheet
	 * @param region
	 */
	private void setRegionStyle(Sheet sheet,CellRangeAddress region){
		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		for(int i=region.getFirstRow();i<=region.getLastRow();i++){
			Row row = sheet.getRow(i);
			for(int j=region.getFirstColumn();j<=region.getLastColumn();j++){
				Cell cell = row.getCell(j);
				if(null == cell){
					cell = row.createCell(j);
				}
				cell.setCellStyle(cellStyle);
			}
		}
	}

	private void setCellValue(Cell cell, Object data) {
		if (data == null) {
			cell.setCellValue("");
			return;
		}
		if (data instanceof Date) {
			cell.setCellValue((Date) data);
			cell.setCellStyle(getDateStyle(DATE_FORMAT, cell.getRow().getSheet().getWorkbook()));
			return;
		}
		if (data instanceof Boolean) {
			cell.setCellValue(((Boolean) data).booleanValue());
			return;
		}
		if (data instanceof Number) {
			cell.setCellValue(((Number) data).doubleValue());
			return;
		}
		cell.setCellValue(data.toString());
	}

	private CellStyle getDateStyle(String dateFormat, Workbook workbook) {
		DataFormat format = workbook.createDataFormat();
		CellStyle result = workbook.createCellStyle();
		result.setDataFormat(format.getFormat("yyyy-MM-dd"));
		return result;
	}

}
