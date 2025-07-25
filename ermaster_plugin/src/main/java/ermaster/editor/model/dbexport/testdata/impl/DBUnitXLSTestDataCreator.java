package ermaster.editor.model.dbexport.testdata.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.dbexport.testdata.TestDataCreator;
import ermaster.editor.model.diagram_contents.element.node.table.ERTable;
import ermaster.editor.model.diagram_contents.element.node.table.column.NormalColumn;
import ermaster.editor.model.testdata.RepeatTestData;
import ermaster.editor.model.testdata.RepeatTestDataDef;
import ermaster.util.Format;
import ermaster.util.POIUtils;
import ermaster.util.io.FileUtils;

public class DBUnitXLSTestDataCreator extends TestDataCreator {

	private XSSFWorkbook workbook;

	private Set<String> sheetNames;

	private XSSFSheet sheet;

	private int rowNum = 0;

	public DBUnitXLSTestDataCreator() {
	}

	@Override
	protected void openFile() throws IOException {
		this.workbook = new XSSFWorkbook();
		this.sheetNames = new HashSet<String>();
	}

	@Override
	protected void write() throws Exception {
		super.write();

		File file = new File(FileUtils.getFile(this.baseDir,
				this.exportTestDataSetting.getExportFilePath()),
				this.testData.getName() + ".xlsx");

		file.getParentFile().mkdirs();

		POIUtils.writeExcelFile(file, this.workbook);
	}

	@Override
	protected void closeFile() throws IOException {
	}

	@Override
	protected boolean skipTable(ERTable table) {
		String sheetName = table.getPhysicalName();

		if (this.sheetNames.contains(sheetName)) {
			return true;
		}

		this.sheetNames.add(sheetName);

		return false;
	}

	@Override
	protected void writeTableHeader(ERDiagram diagram, ERTable table) {
		String sheetName = table.getPhysicalName();
		this.sheet = this.workbook.createSheet(sheetName);

		this.rowNum = 0;
		XSSFRow row = this.sheet.createRow(this.rowNum++);

		int col = 0;

		for (NormalColumn column : table.getExpandedColumns()) {
			XSSFCell cell = row.createCell(col++);
			cell.setCellValue(new XSSFRichTextString(column.getPhysicalName()));
		}
	}

	@Override
	protected void writeTableFooter(ERTable table) {
	}

	@Override
	protected void writeDirectTestData(ERTable table,
			Map<NormalColumn, String> data, String database) {
		XSSFRow row = this.sheet.createRow(this.rowNum++);

		int col = 0;

		for (NormalColumn column : table.getExpandedColumns()) {
			XSSFCell cell = row.createCell(col++);

			String value = Format.null2blank(data.get(column));

			if (value == null || "null".equals(value.toLowerCase())) {

			} else {
				cell.setCellValue(new XSSFRichTextString(value));
			}
		}
	}

	@Override
	protected void writeRepeatTestData(ERTable table,
			RepeatTestData repeatTestData, String database) {

		for (int i = 0; i < repeatTestData.getTestDataNum(); i++) {
			XSSFRow row = this.sheet.createRow(this.rowNum++);

			int col = 0;

			for (NormalColumn column : table.getExpandedColumns()) {
				XSSFCell cell = row.createCell(col++);

				RepeatTestDataDef repeatTestDataDef = repeatTestData
						.getDataDef(column);

				String value = this.getMergedRepeatTestDataValue(i,
						repeatTestDataDef, column);

				if (value == null || "null".equals(value.toLowerCase())) {

				} else {
					cell.setCellValue(new XSSFRichTextString(value));
				}
			}
		}

	}

}
