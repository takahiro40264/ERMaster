package ermaster.editor.model.dbexport.excel.sheet_generator;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.ObjectModel;
import ermaster.editor.model.dbexport.excel.ExportToExcelManager.LoopDefinition;
import ermaster.editor.model.diagram_contents.element.node.table.ERTable;
import ermaster.editor.model.diagram_contents.element.node.table.column.NormalColumn;
import ermaster.editor.model.diagram_contents.element.node.table.index.Index;
import ermaster.editor.model.progress_monitor.ProgressMonitor;
import ermaster.util.POIUtils;
import ermaster.util.POIUtils.CellLocation;

public class IndexSheetGenerator extends AbstractSheetGenerator {

	private static final String KEYWORD_PHYSICAL_INDEX_NAME = "$PIN";

	private static final String KEYWORD_INDEX_TYPE = "$ITYP";

	private static final String KEYWORD_UNIQUE_INDEX = "$IU";

	private static final String KEYWORD_INDEX_DESCRIPTION = "$IDSC";

	private ColumnTemplate columnTemplate;

	protected void clear() {
		this.columnTemplate = null;
	}

	@Override
	public void generate(ProgressMonitor monitor, XSSFWorkbook workbook,
			int sheetNo, boolean useLogicalNameAsSheetName,
			Map<String, Integer> sheetNameMap,
			Map<String, ObjectModel> sheetObjectMap, ERDiagram diagram,
			Map<String, LoopDefinition> loopDefinitionMap)
			throws InterruptedException {
		this.clear();

		for (ERTable table : diagram.getDiagramContents().getContents()
				.getTableSet()) {
			if (diagram.getCurrentCategory() != null
					&& !diagram.getCurrentCategory().contains(table)) {
				continue;
			}

			for (Index index : table.getIndexes()) {
				String name = index.getName();

				XSSFSheet newSheet = createNewSheet(workbook, sheetNo, name,
						sheetNameMap);

				String sheetName = workbook.getSheetName(workbook
						.getSheetIndex(newSheet));
				monitor.subTaskWithCounter("[Index] " + sheetName);

				sheetObjectMap.put(sheetName, index);

				this.setIndexData(workbook, newSheet, index);
				monitor.worked(1);
			}
		}
	}

	/**
	 * �C���f�b�N�X�V�[�g�Ƀf�[�^��ݒ肵�܂�.
	 * 
	 * @param workbook
	 * @param sheet
	 * @param index
	 */
	public void setIndexData(XSSFWorkbook workbook, XSSFSheet sheet, Index index) {
		POIUtils.replace(sheet, KEYWORD_PHYSICAL_INDEX_NAME, this.getValue(
				this.keywordsValueMap, KEYWORD_PHYSICAL_INDEX_NAME,
				index.getName()));
		POIUtils.replace(
				sheet,
				KEYWORD_INDEX_TYPE,
				this.getValue(this.keywordsValueMap, KEYWORD_INDEX_TYPE,
						index.getType()));
		POIUtils.replace(sheet, KEYWORD_UNIQUE_INDEX, this.getValue(
				this.keywordsValueMap, KEYWORD_UNIQUE_INDEX,
				!index.isNonUnique()));
		POIUtils.replace(sheet, KEYWORD_PHYSICAL_TABLE_NAME, this.getValue(
				this.keywordsValueMap, KEYWORD_PHYSICAL_TABLE_NAME, index
						.getTable().getPhysicalName()));
		POIUtils.replace(sheet, KEYWORD_LOGICAL_TABLE_NAME, this.getValue(
				this.keywordsValueMap, KEYWORD_LOGICAL_TABLE_NAME, index
						.getTable().getLogicalName()));
		POIUtils.replace(sheet, KEYWORD_INDEX_DESCRIPTION, this.getValue(
				this.keywordsValueMap, KEYWORD_INDEX_DESCRIPTION,
				index.getDescription()));

		CellLocation cellLocation = POIUtils.findCell(sheet,
				FIND_KEYWORDS_OF_COLUMN);

		if (cellLocation != null) {
			int rowNum = cellLocation.r;
			XSSFRow templateRow = sheet.getRow(rowNum);

			if (this.columnTemplate == null) {
				this.columnTemplate = loadColumnTemplate(workbook, sheet,
						cellLocation);
			}

			int order = 1;

			for (NormalColumn normalColumn : index.getColumns()) {
				XSSFRow row = POIUtils.insertRow(sheet, rowNum++);
				this.setColumnData(this.keywordsValueMap, columnTemplate, row,
						normalColumn, index.getTable(), order);
				order++;
			}

			this.setCellStyle(columnTemplate, sheet, cellLocation.r, rowNum
					- cellLocation.r, templateRow.getFirstCellNum());
		}
	}

	@Override
	public String getTemplateSheetName() {
		return "index_template";
	}

	@Override
	public String[] getKeywords() {
		return new String[] { KEYWORD_PHYSICAL_INDEX_NAME, KEYWORD_INDEX_TYPE,
				KEYWORD_UNIQUE_INDEX, KEYWORD_INDEX_DESCRIPTION, KEYWORD_ORDER,
				KEYWORD_LOGICAL_TABLE_NAME, KEYWORD_PHYSICAL_TABLE_NAME,
				KEYWORD_TABLE_DESCRIPTION, KEYWORD_LOGICAL_COLUMN_NAME,
				KEYWORD_PHYSICAL_COLUMN_NAME, KEYWORD_TYPE, KEYWORD_LENGTH,
				KEYWORD_DECIMAL, KEYWORD_PRIMARY_KEY, KEYWORD_NOT_NULL,
				KEYWORD_UNIQUE_KEY, KEYWORD_FOREIGN_KEY,
				KEYWORD_LOGICAL_REFERENCE_TABLE_KEY,
				KEYWORD_PHYSICAL_REFERENCE_TABLE_KEY,
				KEYWORD_LOGICAL_REFERENCE_TABLE,
				KEYWORD_PHYSICAL_REFERENCE_TABLE,
				KEYWORD_LOGICAL_REFERENCE_KEY, KEYWORD_PHYSICAL_REFERENCE_KEY,
				KEYWORD_AUTO_INCREMENT, KEYWORD_DEFAULT_VALUE,
				KEYWORD_DESCRIPTION };
	}

	@Override
	public int getKeywordsColumnNo() {
		return 4;
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
			count += table.getIndexes().size();
		}

		return count;
	}

}
