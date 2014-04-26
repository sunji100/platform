package org.yixun.support.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WorkbookFactory {
	
	public static Workbook createWorkbook(File excelFile) {
		try {
			return createWorkbook(new FileInputStream(excelFile), Version.of(excelFile.getName()));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File " + excelFile.getPath() + " not exists.", e);
		}
	}
	
	public static Workbook createWorkbook(InputStream in, Version version) {
			try {
				if (version == Version.XLSX) {
					return new XSSFWorkbook(in);
				} else {
					return new HSSFWorkbook(in);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
	}
}
