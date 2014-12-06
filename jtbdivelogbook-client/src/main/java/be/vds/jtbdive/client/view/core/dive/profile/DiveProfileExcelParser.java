/*
* Jt'B Dive Logbook - Electronic dive logbook.
* 
* Copyright (C) 2010  Gautier Vanderslyen
* 
* Jt'B Dive Logbook is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package be.vds.jtbdive.client.view.core.dive.profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import be.vds.jtbdive.client.view.utils.UIAgent;
import be.vds.jtbdive.core.core.DiveProfile;

public class DiveProfileExcelParser {

	public void export(Date date, DiveProfile diveProfile, File file)
			throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(UIAgent.getInstance().getExportFormatDateHoursFull().format(date));

		HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		cellStyle.setFont(font);

		int rowIndex = 0;
		int colIndex = 0;
		HSSFRow row = sheet.createRow(rowIndex++);
		HSSFCell cell = row.createCell(colIndex++);
		cell.setCellValue("Time (s)");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(colIndex++);
		cell.setCellValue("Depth (m)");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(colIndex++);
		cell.setCellValue("Ascent Warning");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(colIndex++);
		cell.setCellValue("Bottom Time Warning");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(colIndex++);
		cell.setCellValue("Deco Ceiling Warning");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(colIndex++);
		cell.setCellValue("Deco Entry");
		cell.setCellStyle(cellStyle);

		Map<Double, Double> entries = diveProfile.getDepthEntries();
		List<Double> seconds = new ArrayList<Double>(entries.keySet());
		Collections.sort(seconds);
		for (Double time : seconds) {
			colIndex = 0;
			row = sheet.createRow(rowIndex++);
			row.createCell(colIndex++, HSSFCell.CELL_TYPE_NUMERIC)
					.setCellValue(time);
			row.createCell(colIndex++, HSSFCell.CELL_TYPE_NUMERIC)
					.setCellValue(entries.get(time));
			row.createCell(colIndex++, HSSFCell.CELL_TYPE_BOOLEAN)
					.setCellValue(
							diveProfile.getAscentWarnings().contains(time));
			row.createCell(colIndex++, HSSFCell.CELL_TYPE_BOOLEAN)
					.setCellValue(
							diveProfile.getRemainingBottomTimeWarnings()
									.contains(time));
			row.createCell(colIndex++, HSSFCell.CELL_TYPE_BOOLEAN)
					.setCellValue(
							diveProfile.getDecoCeilingWarnings().contains(time));
			row.createCell(colIndex++, HSSFCell.CELL_TYPE_BOOLEAN)
					.setCellValue(diveProfile.getDecoEntries().contains(time));
		}

		for (int i = 0; i <= colIndex; i++) {
			sheet.autoSizeColumn(i);
		}

		FileOutputStream fileOut = new FileOutputStream(file);
		wb.write(fileOut);
		fileOut.close();
	}

	public DiveProfile read(File file) throws IOException {
		DiveProfile dp = new DiveProfile();
		Map<Double, Double> depthEntries = new HashMap<Double, Double>();
		Set<Double> ascentWarnings = new HashSet<Double>();
		Set<Double> decoWarnings = new HashSet<Double>();
		Set<Double> remainingBottomTimeWarnings = new HashSet<Double>();
		Set<Double> decoEntries = new HashSet<Double>();

		HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheet = wb.getSheetAt(0);

		int rowMax = sheet.getLastRowNum();
		for (int i = 1; i <= rowMax; i++) {
			HSSFRow row = sheet.getRow(i);
			double second = row.getCell(0).getNumericCellValue();
			depthEntries.put(second, row.getCell(1).getNumericCellValue());

			boolean b = row.getCell(2).getBooleanCellValue();
			if (b)
				ascentWarnings.add(second);

			b = row.getCell(3).getBooleanCellValue();
			if (b)
				remainingBottomTimeWarnings.add(second);

			b = row.getCell(4).getBooleanCellValue();
			if (b)
				decoWarnings.add(second);

			b = row.getCell(5).getBooleanCellValue();
			if (b)
				decoEntries.add(second);
		}

		dp.setDepthEntries(depthEntries);

		if (ascentWarnings.size() > 0)
			dp.setAscentWarnings(ascentWarnings);

		if (decoWarnings.size() > 0)
			dp.setDecoCeilingWarnings(decoWarnings);

		if (remainingBottomTimeWarnings.size() > 0)
			dp.setRemainingBottomTimeWarnings(remainingBottomTimeWarnings);

		if (decoEntries.size() > 0)
			dp.setDecoEntries(decoEntries);

		return dp;
	}

}
