/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import com.dapinder.data.feed.exception.DataFeedRuntimeException;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 *
 * @author: Dapinder Singh
 *
 */
public class ExcelReporter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReporter.class);
	private Set<String> fieldNames = new LinkedHashSet<String>();
	private XSSFWorkbook workbook = null;
	private String workbookName = "workbook.xls";

	public ExcelReporter(String workbookName) {
		setWorkbookName(workbookName);
		initialize();
	}

	private void initialize() {
		setWorkbook(new XSSFWorkbook());
	}

	public void closeWorksheet() {
		// Write the output to a file
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(getWorkbookName());
			getWorkbook().write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e) {
			throw new DataFeedRuntimeException(e);
		} catch (IOException e) {
			throw new DataFeedRuntimeException(e);
		}
	}

	/**
	 * Setup fields for class.
	 *
	 * @param clazz
	 *            the clazz
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	private boolean setupFieldsForClass(Class<?> clazz) throws Exception {
		String[] annotatedFieldNames = null;
		Annotation[] annotations = clazz.getAnnotations();
		if (null != annotations && annotations.length > 0) {
			for (Annotation annotation : annotations) {
				if (annotation instanceof JsonPropertyOrder) {
					JsonPropertyOrder jsonPropertyOrder = (JsonPropertyOrder) annotation;
					annotatedFieldNames = jsonPropertyOrder.value();
					for (String fieldName : annotatedFieldNames) {
						fieldNames.add(fieldName);
					}
					return true;
				}
			}
		}
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fieldNames.add(fields[i].getName());
		}
		return true;
	}

	private void initializeForRead() throws InvalidFormatException, IOException {
		LOGGER.info("Open file");
		InputStream inp = new FileInputStream(getWorkbookName());
		LOGGER.info("create wb");
		workbook = new XSSFWorkbook(inp);
	}

	private static String getCellTypeAsString(int type) {
		String cellType = StringUtils.EMPTY;
		switch (type) {
		case Cell.CELL_TYPE_NUMERIC:
			cellType = "CELL_TYPE_NUMERIC";
			break;
		case Cell.CELL_TYPE_STRING:
			cellType = "CELL_TYPE_STRING";
			break;
		case Cell.CELL_TYPE_FORMULA:
			cellType = "CELL_TYPE_FORMULA";
			break;
		case Cell.CELL_TYPE_BLANK:
			cellType = "CELL_TYPE_BLANK";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			cellType = "CELL_TYPE_BOOLEAN";
			break;
		case Cell.CELL_TYPE_ERROR:
			cellType = "CELL_TYPE_ERROR";
			break;
		default:
			cellType = "NO CELL TYPE";
		}
		return cellType;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> readData(String classname) throws Exception {

		initializeForRead();
		/*
		 * Change made by Dapinder, not to have condition of having Sheet Name
		 * equals full POJO class name. Also rather than then picking up the
		 * class name from worksheet and then loading it, better to use provided
		 * class name.
		 */
		XSSFSheet sheet = workbook.getSheetAt(0);
		final int totalPhysicalRows = sheet.getPhysicalNumberOfRows();

		LOGGER.info("The class is: " + classname);
		Class clazz = Class.forName(classname);
		setupFieldsForClass(clazz);
		List<T> result = new ArrayList<T>();
		XSSFRow row;
		for (int rowCount = 1; rowCount < totalPhysicalRows; rowCount++) {
			T one = (T) clazz.newInstance();
			row = sheet.getRow(rowCount);
			if (null != row) {
				result.add(one);
				int j = 0;
				for (String fieldName : fieldNames) {
					XSSFCell cell = row.getCell(j);
					LOGGER.info("Method: set" + capitalize(fieldName));
					Method method = constructMethod(clazz, fieldName);

					if (null != cell) {
						int type = cell.getCellType();
						LOGGER.info("Cell Type: " + type);
						final Class<?> methodParamType = method.getParameterTypes()[0];
						if (type == Cell.CELL_TYPE_BLANK) {
							try {
								if (ClassUtils
										.isPrimitiveWrapper(methodParamType)) {
									method.invoke(one, new Object[] { null });
								}
							} catch (Exception e) {
								logError(clazz, method, row, cell, e);
								throw e;
							}
						} else if (type == Cell.CELL_TYPE_STRING || type == Cell.CELL_TYPE_FORMULA) {
							final String value = cell.getStringCellValue();
							try {
								if (methodParamType == String.class) {
									method.invoke(one, String.valueOf(value));
								} else {
									method.invoke(one, new Object[] { null });
								}
							} catch (Exception e) {
								logError(clazz, method, row, cell, e);
								throw e;
							} catch (Throwable e) {
								logError(clazz, method, row, cell, e);
								throw e;
							}
						} else {
							final Double value = cell.getNumericCellValue();
							try {
								if (methodParamType == Integer.class || methodParamType == int.class) {
									method.invoke(one, value.intValue());
								} else if (methodParamType == Long.class || methodParamType == long.class) {
									method.invoke(one, value.longValue());
								} else if (methodParamType == Double.class || methodParamType == double.class) {
									method.invoke(one, value);
								} else if (methodParamType == Float.class || methodParamType == float.class) {
									method.invoke(one, value.floatValue());
								} else if (methodParamType == BigDecimal.class) {
									method.invoke(one, BigDecimal.valueOf(value));
								} else if (methodParamType == String.class) {
									method.invoke(one, String.valueOf(value.intValue()));
								} else {
									throw new Exception("Method: {" + method + "} parameter: \"" + methodParamType
											+ "\" not supported. Please change it to Integer, int, Double, double, Long, long, Float, float, BigDecimal.");
								}
							} catch (Exception e) {
								logError(clazz, method, row, cell, e);
								throw e;
							} catch (Throwable e) {
								logError(clazz, method, row, cell, e);
								throw e;
							}
						}
					}
					j++;
				}
			}
		}

		LOGGER.info("The result set contains: " + result.size() + " items.");
		return result;
	}

	private void logError(Class clazz, Method method, XSSFRow row, XSSFCell cell, Exception exception) {
		LOGGER.error("******* Exception while processing Excel: " + this.workbookName);
		LOGGER.error("Class Name: " + clazz);
		LOGGER.error("Method Name: " + method.getName());
		LOGGER.error("Row Number:" + (row.getRowNum() + 1) + ", CELL NUMBER:" + (cell.getColumnIndex() + 1)
				+ ", Cell Type: " + getCellTypeAsString(cell.getCellType()));
		LOGGER.error("Exception Details: " + ExceptionUtils.getStackTrace(exception));
	}

	private void logError(Class clazz, Method method, XSSFRow row, XSSFCell cell, Throwable exception) {
		LOGGER.error("******* Exception while processing Excel: " + this.workbookName);
		LOGGER.error("Class Name: " + clazz);
		LOGGER.error("Method Name: " + method.getName());
		LOGGER.error("Row Number:" + (row.getRowNum() + 1) + ", CELL NUMBER:" + (cell.getColumnIndex() + 1)
				+ ", Cell Type: " + getCellTypeAsString(cell.getCellType()));
		LOGGER.error("Exception Details: " + ExceptionUtils.getStackTrace(exception));
	}

	private Class<?> getGetterReturnClass(Class<?> clazz, String fieldName) {
		String methodName = "get" + capitalize(fieldName);
		Class<?> returnType = null;
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName)) {
				returnType = method.getReturnType();
				break;
			}
		}
		return returnType;
	}

	@SuppressWarnings("unchecked")
	private Method constructMethod(Class clazz, String fieldName) throws SecurityException, NoSuchMethodException {
		Class<?> fieldClass = getGetterReturnClass(clazz, fieldName);
		return clazz.getMethod("set" + capitalize(fieldName), fieldClass);
	}

	public <T> void writeReportToExcel(List<T> data) throws Exception {
		Sheet sheet = getWorkbook().createSheet(data.get(0).getClass().getName());
		setupFieldsForClass(data.get(0).getClass());
		// Create a row and put some cells in it. Rows are 0 based.
		int rowCount = 0;
		int columnCount = 0;

		Row row = sheet.createRow(rowCount++);
		for (String fieldName : fieldNames) {
			Cell cel = row.createCell(columnCount++);
			cel.setCellValue(fieldName);
		}
		Class<? extends Object> classz = data.get(0).getClass();
		for (T t : data) {
			row = sheet.createRow(rowCount++);
			columnCount = 0;
			for (String fieldName : fieldNames) {
				Cell cel = row.createCell(columnCount);
				Method method = classz.getMethod("get" + capitalize(fieldName));
				Object value = method.invoke(t, (Object[]) null);
				if (value != null) {
					if (value instanceof String) {
						cel.setCellValue((String) value);
					} else if (value instanceof Long) {
						cel.setCellValue((Long) value);
					} else if (value instanceof Integer) {
						cel.setCellValue((Integer) value);
					} else if (value instanceof Double) {
						cel.setCellValue((Double) value);
					}
				}
				columnCount++;
			}
		}
	}

	public String capitalize(String string) {
		String capital = string.substring(0, 1).toUpperCase();
		return capital + string.substring(1);
	}

	public String getWorkbookName() {
		return workbookName;
	}

	public void setWorkbookName(String workbookName) {
		this.workbookName = workbookName;
	}

	void setWorkbook(XSSFWorkbook workbook) {
		this.workbook = workbook;
	}

	Workbook getWorkbook() {
		return workbook;
	}

}
