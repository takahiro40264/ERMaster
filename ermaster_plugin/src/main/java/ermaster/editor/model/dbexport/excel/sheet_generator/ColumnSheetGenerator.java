package ermaster.editor.model.dbexport.excel.sheet_generator;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.ObjectModel;
import ermaster.editor.model.dbexport.excel.ExportToExcelManager.LoopDefinition;
import ermaster.editor.model.diagram_contents.element.node.table.ERTable;
import ermaster.editor.model.diagram_contents.element.node.table.column.ColumnSet;
import ermaster.editor.model.diagram_contents.element.node.table.column.NormalColumn;
import ermaster.editor.model.progress_monitor.ProgressMonitor;
import ermaster.util.POIUtils;
import ermaster.util.POIUtils.CellLocation;

public class ColumnSheetGenerator extends AbstractSheetGenerator {

	private static final String KEYWORD_SHEET_NAME = "$SHTN";

	private ColumnTemplate columnTemplate;

	private void clear() {
		this.columnTemplate = null;
	}

	public void setAllColumnsData(ProgressMonitor monitor,
			XSSFWorkbook workbook, XSSFSheet sheet, ERDiagram diagram)
			throws InterruptedException {
		this.clear();

		CellLocation cellLocation = POIUtils.findCell(sheet,
				FIND_KEYWORDS_OF_COLUMN);

		if (cellLocation != null) {
			int rowNum = cellLocation.r;
			XSSFRow templateRow = sheet.getRow(rowNum);

			if (this.columnTemplate == null) {
				this.columnTemplate = this.loadColumnTemplate(workbook, sheet,
						cellLocation);
			}

			int order = 1;

			for (ERTable table : diagram.getDiagramContents().getContents()
					.getTableSet()) {

				if (diagram.getCurrentCategory() != null
						&& !diagram.getCurrentCategory().contains(table)) {
					continue;
				}

				monitor.subTaskWithCounter(sheet.getSheetName() + " - "
						+ table.getName());

				for (NormalColumn normalColumn : table.getExpandedColumns()) {

					XSSFRow row = POIUtils.insertRow(sheet, rowNum++);
					this.setColumnData(this.keywordsValueMap, columnTemplate,
							row, normalColumn, table, order);
					order++;
				}

				monitor.worked(1);
			}

			this.setCellStyle(columnTemplate, sheet, cellLocation.r, rowNum
					- cellLocation.r, templateRow.getFirstCellNum());
		}
	}

	public String getSheetName() {
		String name = this.keywordsValueMap.get(KEYWORD_SHEET_NAME);

		if (name == null) {
			name = "all attributes";
		}

		return name;
	}

	@Override
	public void generate(ProgressMonitor monitor, XSSFWorkbook workbook,
			int sheetNo, boolean useLogicalNameAsSheetName,
			Map<String, Integer> sheetNameMap,
			Map<String, ObjectModel> sheetObjectMap, ERDiagram diagram,
			Map<String, LoopDefinition> loopDefinitionMap)
			throws InterruptedException {
		String name = this.getSheetName();
		XSSFSheet newSheet = createNewSheet(workbook, sheetNo, name,
				sheetNameMap);

		String sheetName = workbook.getSheetName(workbook
				.getSheetIndex(newSheet));

		sheetObjectMap.put(sheetName, new ColumnSet());

		this.setAllColumnsData(monitor, workbook, newSheet, diagram);
	}

	@Override
	public String getTemplateSheetName() {
		return "column_template";
	}

	@Override
	public int getKeywordsColumnNo() {
		return 20;
	}

	@Override
	public String[] getKeywords() {
		return new String[] { KEYWORD_LOGICAL_TABLE_NAME,
				KEYWORD_PHYSICAL_TABLE_NAME, KEYWORD_TABLE_DESCRIPTION,
				KEYWORD_ORDER, KEYWORD_LOGICAL_COLUMN_NAME,
				KEYWORD_PHYSICAL_COLUMN_NAME, KEYWORD_TYPE, KEYWORD_LENGTH,
				KEYWORD_DECIMAL, KEYWORD_PRIMARY_KEY, KEYWORD_NOT_NULL,
				KEYWORD_UNIQUE_KEY, KEYWORD_FOREIGN_KEY,
				KEYWORD_LOGICAL_REFERENCE_TABLE_KEY,
				KEYWORD_PHYSICAL_REFERENCE_TABLE_KEY,
				KEYWORD_LOGICAL_REFERENCE_TABLE,
				KEYWORD_PHYSICAL_REFERENCE_TABLE,
				KEYWORD_LOGICAL_REFERENCE_KEY, KEYWORD_PHYSICAL_REFERENCE_KEY,
				KEYWORD_AUTO_INCREMENT, KEYWORD_DEFAULT_VALUE,
				KEYWORD_DESCRIPTION, KEYWORD_SHEET_NAME };
	}

	@Override
	public int count(ERDiagram diagram) {
		int count = 0;

		for (ERTable table : diagram.getDiagramContents().getContents()
				.getTableSet()) {

			if (diagram.getCurrentCategory() != null
					&& !diagram.getCurrentCategory().contains(table)) {
				continue;
			}

			count++;
		}

		return count;
	}

}
