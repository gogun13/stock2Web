package com.enjoy.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class ExcelUtil {
	public static Workbook getWorkbook(File f) throws IOException {
		String contentType = getContenttype(f);
		InputStream is = null;
		try {
			if (contentType.indexOf(".zip") >= 0
					|| contentType.indexOf(".7z") >= 0) {
				return getWorkbookZip(f);
			} else {
				is = new FileInputStream(f);
				return createWookbook(is);
			}
		} finally {
			is = null;
		}
	}

	private static String getContenttype(File f) {
		// String contentType = new MimetypesFileTypeMap().getContentType(f);
		System.out.println("-- File name : " + f.getName());
		return f.getName();
	}

	private static Workbook getWorkbookZip(File f) throws IOException {
		ZipFile zipFile = new ZipFile(f);
		Enumeration enumZip = zipFile.getEntries();// entries();
		InputStream is = null;

		while (enumZip.hasMoreElements()) {
			ZipEntry entryZip = (ZipEntry) enumZip.nextElement();
			if (!entryZip.isDirectory()) {
				is = zipFile.getInputStream(entryZip);
				break;
			}
		}
		if (is == null) {
			System.out.println("zipFile.getInputStream is null");
			throw new IOException(f.getName());
		}
		return createWookbook(is);
	}

	private static Workbook getWorkbookZip(String url) throws IOException {
		ZipFile zipFile = new ZipFile(url);
		Enumeration enumZip = zipFile.getEntries();// entries();
		InputStream is = null;

		while (enumZip.hasMoreElements()) {
			ZipEntry entryZip = (ZipEntry) enumZip.nextElement();
			if (!entryZip.isDirectory()) {
				is = zipFile.getInputStream(entryZip);
			}
		}
		if (is == null) {
			throw new IOException(url);
		}
		return createWookbook(is);
	}

	private static Workbook createWookbook(InputStream is) throws IOException {

		Workbook wb;
		try {
			wb = WorkbookFactory.create(is);
			System.out.println("wb :: " + wb);
			return wb;
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
	}

	public static Row[] getAllRows(Sheet sheet) {
		int startRow = sheet.getFirstRowNum();
		int endRow = sheet.getPhysicalNumberOfRows();
		Row[] rowArray = new Row[endRow];
		for (int j = startRow; j < endRow; j++) {
			rowArray[j] = sheet.getRow(j);
		}
		return rowArray;
	}
}
