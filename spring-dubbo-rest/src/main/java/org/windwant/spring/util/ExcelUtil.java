package org.windwant.spring.util;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class ExcelUtil {
	
	public static void main(String[] args) {
		String name = "orderId";
		String a = name.substring(0, 1).toUpperCase();
		String methodName =  name.substring(1);
		System.out.println(a + methodName);
	}
	
	private final static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

	public final static Integer EXPORT_STREAM_LIMIT=10000;

	private static void createXTitle(XSSFWorkbook workbook, XSSFSheet sheet, String[] exportFields, String[] exportTitles) {
		XSSFCellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setFont(font);

		XSSFRow row = sheet.createRow(0);
		XSSFCell cell;
		for (int i=0; i<exportFields.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(exportTitles[i]);
			cell.setCellStyle(style);
		}
	}

	/**
	 * 导出
	 * @param list
	 * @param exportFields
	 * @param exportTitles
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	public static XSSFWorkbook getXExcel(List list, String[] exportFields, String[] exportTitles, String sheetName) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(sheetName);

		createXTitle(workbook, sheet, exportFields, exportTitles);
		int rowNum = 1;
		for (Object export: list) {
			XSSFRow row = sheet.createRow(rowNum);
			for (int i=0; i<exportFields.length; i++) {
				XSSFCell cell = row.createCell(i);
				String methodName = exportFields[i].substring(0, 1).toUpperCase() + exportFields[i].substring(1);
				Method method;
				String value;
				try {
					method = export.getClass().getDeclaredMethod("get" + methodName, new Class[]{});
				} catch (NoSuchMethodException e){
					method = export.getClass().getSuperclass().getDeclaredMethod("get" + methodName, new Class[]{});
				}
				value = String.valueOf(method.invoke(export, new Object[]{}));
				cell.setCellValue((StringUtils.isBlank(value) || "null".equals(value))? StringUtils.EMPTY:value);
			}
			rowNum++;
		}
		for (int i=0; i<exportFields.length; i++) {
			sheet.autoSizeColumn(i);
		}
		return workbook;
	}

	/**
	 * 导出
	 * @param list 导出数据list
	 * @param exportFields 字段
	 * @param exportTitles 标题
	 * @param sheetName 表单名
	 * @param fileName 文件名
	 * @param response 输出
	 */
	public static void exportExcel(List list, String[] exportFields, String[] exportTitles, String sheetName, String fileName, HttpServletResponse response){
		XSSFWorkbook wb = null;
		try {
			wb = ExcelUtil.getXExcel(list, exportFields, exportTitles, sheetName);
			response.setHeader("content-Type", "application/vnd.ms-excel;charset=UTF-8");
			fileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
			wb.write(response.getOutputStream());
			response.flushBuffer();
			logger.info("export excel file: {}", fileName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(wb != null) wb.close();
			} catch (IOException e1) {

			}
		}
	}


	private static void createSXTitle(SXSSFWorkbook workbook, SXSSFSheet sheet, String[] exportFields, String[] exportTitles) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setFont(font);

		SXSSFRow row = sheet.createRow(0);
		SXSSFCell cell;
		for (int i=0; i<exportFields.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(exportTitles[i]);
			cell.setCellStyle(style);
		}
	}

	/**
	 * 导出 stream
	 * @param list
	 * @param exportFields
	 * @param exportTitles
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	public static SXSSFWorkbook getSXExcel(List list, String[] exportFields, String[] exportTitles, String sheetName) throws Exception {
		SXSSFWorkbook workbook = new SXSSFWorkbook(5000);
		SXSSFSheet sheet = workbook.createSheet(sheetName);

		createSXTitle(workbook, sheet, exportFields, exportTitles);
		int rowNum = 1;
		for (Object export: list) {
			SXSSFRow row = sheet.createRow(rowNum);
			for (int i=0; i<exportFields.length; i++) {
				SXSSFCell cell = row.createCell(i);
				String methodName = exportFields[i].substring(0, 1).toUpperCase() + exportFields[i].substring(1);
				Method method;
				String value;
				try {
					method = export.getClass().getDeclaredMethod("get" + methodName, new Class[]{});
				} catch (NoSuchMethodException e){
					method = export.getClass().getSuperclass().getDeclaredMethod("get" + methodName, new Class[]{});
				}
				value = String.valueOf(method.invoke(export, new Object[]{}));
				cell.setCellValue((StringUtils.isBlank(value) || "null".equals(value))? StringUtils.EMPTY:value);
			}
			rowNum++;
		}
		sheet.trackAllColumnsForAutoSizing();
		for (int i=0; i<exportFields.length; i++) {
			sheet.autoSizeColumn(i);
		}
		return workbook;
	}

	/**
	 * 导出 stream
	 * @param list 导出数据list
	 * @param exportFields 字段
	 * @param exportTitles 标题
	 * @param sheetName 表单名
	 * @param fileName 文件名
	 * @param response 输出
	 */
	public static void exportStreams(List list, String[] exportFields, String[] exportTitles, String sheetName, String fileName, HttpServletResponse response){
		SXSSFWorkbook wb = null;
		try {
			wb = ExcelUtil.getSXExcel(list, exportFields, exportTitles, sheetName);
			response.setHeader("content-Type", "application/vnd.ms-excel;charset=UTF-8");
			fileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
			wb.write(response.getOutputStream());
			response.flushBuffer();
			logger.info("export excel file: {}", fileName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(wb != null) wb.close();
			} catch (IOException e1) {

			}
		}
	}

	public static void exports(List list, String[] exportFields, String[] exportTitles, String sheetName, String fileName, HttpServletResponse response){
		if(list == null || list.isEmpty() || exportFields == null || exportTitles == null || response == null) return;

		if(StringUtils.isBlank(sheetName)) {
			sheetName = "sheet1";
		}

		if(StringUtils.isBlank(fileName)) {
			sheetName = "导出数据文件";
		}

		if(list.size() > EXPORT_STREAM_LIMIT){
			ExcelUtil.exportStreams(list, exportFields, exportTitles, sheetName, fileName, response);
		}else {
			ExcelUtil.exportExcel(list, exportFields, exportTitles, sheetName, fileName, response);
		}
	}
}
