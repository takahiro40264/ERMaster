package ermaster.editor.model.dbexport.excel.sheet_generator;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.ObjectModel;
import ermaster.editor.model.dbexport.excel.ExportToExcelManager.LoopDefinition;
import ermaster.editor.model.diagram_contents.element.node.table.column.NormalColumn;
import ermaster.editor.model.diagram_contents.element.node.view.View;
import ermaster.editor.model.progress_monitor.ProgressMonitor;
import ermaster.util.POIUtils;
import ermaster.util.POIUtils.CellLocation;

public class ViewSheetGenerator extends AbstractSheetGenerator {

	private static final String KEYWORD_LOGICAL_VIEW_NAME = "$LVN";

	private static final String KEYWORD_PHYSICAL_VIEW_NAME = "$PVN";

	private static final String KEYWORD_VIEW_DESCRIPTION = "$VDSC";

	private static final String KEYWORD_VIEW_SQL = "$SQL";

	private static final String[] FIND_KEYWORDS_OF_FK_COLUMN = {
			KEYWORD_LOGICAL_FOREIGN_KEY_NAME, KEYWORD_PHYSICAL_FOREIGN_KEY_NAME };

	private ColumnTemplate columnTemplate;

	private ColumnTemplate fkColumnTemplate;

	protected void clear() {
		this.columnTemplate = null;
		this.fkColumnTemplate = null;
	}

	@Override
	public void generate(ProgressMonitor monitor, XSSFWorkbook workbook,
			int sheetNo, boolean useLogicalNameAsSheetName,
			Map<String, Integer> sheetNameMap,
			Map<String, ObjectModel> sheetObjectMap, ERDiagram diagram,
			Map<String, LoopDefinition> loopDefinitionMap)
			throws InterruptedException {
		this.clear();

		List<View> nodeSet = null;

		if (diagram.getCurrentCategory() != null) {
			nodeSet = diagram.getCurrentCategory().getViewContents();
		} else {
			nodeSet = diagram.getDiagramContents().getContents().getViewSet()
					.getList();
		}

		for (View view : nodeSet) {
			String name = null;
			if (useLogicalNameAsSheetName) {
				name = view.getLogicalName();
			} else {
				name = view.getPhysicalName();
			}

			XSSFSheet newSheet = createNewSheet(workbook, sheetNo, name,
					sheetNameMap);

			String sheetName = workbook.getSheetName(workbook
					.getSheetIndex(newSheet));
			monitor.subTaskWithCounter("[View] " + sheetName);

			sheetObjectMap.put(sheetName, view);

			this.setViewData(workbook, newSheet, view);

			monitor.worked(1);
		}
	}

	/**
	 * �r���[�V�[�g�Ƀf�[�^��ݒ肵�܂�.
	 * 
	 * @param workbook
	 * @param sheet
	 * @param view
	 */
	public void setViewData(XSSFWorkbook workbook, XSSFSheet sheet, View view) {
		POIUtils.replace(sheet, KEYWORD_LOGICAL_VIEW_NAME, this.getValue(
				this.keywordsValueMap, KEYWORD_LOGICAL_VIEW_NAME,
				view.getLogicalName()));

		POIUtils.replace(sheet, KEYWORD_PHYSICAL_VIEW_NAME, this.getValue(
				this.keywordsValueMap, KEYWORD_PHYSICAL_VIEW_NAME,
				view.getPhysicalName()));

		POIUtils.replace(sheet, KEYWORD_VIEW_DESCRIPTION, this.getValue(
				this.keywordsValueMap, KEYWORD_VIEW_DESCRIPTION,
				view.getDescription()));

		POIUtils.replace(
				sheet,
				KEYWORD_VIEW_SQL,
				this.getValue(this.keywordsValueMap, KEYWORD_VIEW_SQL,
						view.getSql()));

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

			for (NormalColumn normalColumn : view.getExpandedColumns()) {
				XSSFRow row = POIUtils.insertRow(sheet, rowNum++);
				this.setColumnData(this.keywordsValueMap, columnTemplate, row,
						normalColumn, view, order);
				order++;
			}

			this.setCellStyle(columnTemplate, sheet, cellLocation.r, rowNum
					- cellLocation.r, templateRow.getFirstCellNum());
		}

		CellLocation fkCellLocation = POIUtils.findCell(sheet,
				FIND_KEYWORDS_OF_FK_COLUMN);

		if (fkCellLocation != null) {
			int rowNum = fkCellLocation.r;
			XSSFRow templateRow = sheet.getRow(rowNum);

			if (this.fkColumnTemplate == null) {
				this.fkColumnTemplate = this.loadColumnTemplate(workbook,
						sheet, fkCellLocation);
			}

			int order = 1;

			for (NormalColumn normalColumn : view.getExpandedColumns()) {
				if (normalColumn.isForeignKey()) {
					XSSFRow row = POIUtils.insertRow(sheet, rowNum++);
					this.setColumnData(this.keywordsValueMap,
							this.fkColumnTemplate, row, normalColumn, view,
							order);
					order++;
				}
			}

			this.setCellStyle(this.fkColumnTemplate, sheet, fkCellLocation.r,
					rowNum - fkCellLocation.r, templateRow.getFirstCellNum());
		}
	}

	@Override
	public String getTemplateSheetName() {
		return "view_template";
	}

	@Override
	public String[] getKeywords() {
		return new String[] { KEYWORD_LOGICAL_VIEW_NAME,
				KEYWORD_PHYSICAL_VIEW_NAME, KEYWORD_VIEW_DESCRIPTION,
				KEYWORD_VIEW_SQL, KEYWORD_ORDER, KEYWORD_LOGICAL_COLUMN_NAME,
				KEYWORD_PHYSICAL_COLUMN_NAME, KEYWORD_TYPE, KEYWORD_LENGTH,
				KEYWORD_DECIMAL, KEYWORD_PRIMARY_KEY, KEYWORD_NOT_NULL,
				KEYWORD_UNIQUE_KEY, KEYWORD_FOREIGN_KEY,
				KEYWORD_LOGICAL_REFERENCE_TABLE_KEY,
				KEYWORD_PHYSICAL_REFERENCE_TABLE_KEY,
				KEYWORD_LOGICAL_REFERENCE_TABLE,
				KEYWORD_PHYSICAL_REFERENCE_TABLE,
				KEYWORD_LOGICAL_REFERENCE_KEY, KEYWORD_PHYSICAL_REFERENCE_KEY,
				KEYWORD_AUTO_INCREMENT, KEYWORD_DEFAULT_VALUE,
				KEYWORD_DESCRIPTION, KEYWORD_LOGICAL_FOREIGN_KEY_NAME,
				KEYWORD_PHYSICAL_FOREIGN_KEY_NAME };
	}

	@Override
	public int getKeywordsColumnNo() {
		return 12;
	}

	@Override
	public int count(ERDiagram diagram) {
		return diagram.getDiagramContents().getContents().getViewSet()
				.getList().size();
	}

}
