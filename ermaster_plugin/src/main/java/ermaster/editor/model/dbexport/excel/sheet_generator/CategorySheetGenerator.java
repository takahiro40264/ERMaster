package ermaster.editor.model.dbexport.excel.sheet_generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.ObjectModel;
import ermaster.editor.model.dbexport.excel.ExportToExcelManager.LoopDefinition;
import ermaster.editor.model.diagram_contents.element.node.category.Category;
import ermaster.editor.model.diagram_contents.element.node.table.ERTable;
import ermaster.editor.model.progress_monitor.ProgressMonitor;
import ermaster.util.POIUtils;

public class CategorySheetGenerator extends TableSheetGenerator {

	@Override
	public void generate(ProgressMonitor monitor, XSSFWorkbook workbook,
			int sheetNo, boolean useLogicalNameAsSheetName,
			Map<String, Integer> sheetNameMap,
			Map<String, ObjectModel> sheetObjectMap, ERDiagram diagram,
			Map<String, LoopDefinition> loopDefinitionMap)
			throws InterruptedException {
		this.clear();

		if (diagram.getCurrentCategory() != null) {
			return;
		}

		LoopDefinition loopDefinition = loopDefinitionMap.get(this
				.getTemplateSheetName());
		XSSFSheet oldSheet = workbook.getSheetAt(sheetNo);

		List<ERTable> allTables = new ArrayList<ERTable>(diagram
				.getDiagramContents().getContents().getTableSet().getList());

		for (Category category : diagram.getDiagramContents().getSettings()
				.getCategorySetting().getSelectedCategories()) {
			XSSFSheet newSheet = createNewSheet(workbook, sheetNo,
					category.getName(), sheetNameMap);

			String sheetName = workbook.getSheetName(workbook
					.getSheetIndex(newSheet));
			monitor.subTaskWithCounter("[Category] " + sheetName);

			sheetObjectMap.put(sheetName, category);

			boolean first = true;

			for (ERTable table : category.getTableContents()) {
				if (allTables.contains(table)) {
					allTables.remove(table);
					monitor.worked(1);
				}

				if (first) {
					first = false;

				} else {
					POIUtils.copyRow(oldSheet, newSheet,
							loopDefinition.startLine - 1,
							oldSheet.getLastRowNum(), newSheet.getLastRowNum()
									+ loopDefinition.spaceLine + 1);
				}

				this.setTableData(workbook, newSheet, table);

				newSheet.setRowBreak(newSheet.getLastRowNum()
						+ loopDefinition.spaceLine);
			}

			if (first) {
				int rowIndex = loopDefinition.startLine - 1;

				while (rowIndex <= newSheet.getLastRowNum()) {
					XSSFRow row = newSheet.getRow(rowIndex);
					if (row != null) {
						newSheet.removeRow(row);
					}

					rowIndex++;
				}
			}

			monitor.worked(1);
		}

		if (!allTables.isEmpty()) {
			XSSFSheet newSheet = createNewSheet(workbook, sheetNo,
					loopDefinition.sheetName, sheetNameMap);

			String sheetName = workbook.getSheetName(workbook
					.getSheetIndex(newSheet));

			sheetObjectMap.put(sheetName, diagram.getDiagramContents()
					.getContents().getTableSet());

			boolean first = true;

			for (ERTable table : allTables) {
				monitor.subTaskWithCounter("[Category] "
						+ newSheet.getSheetName() + " - " + table.getName());

				if (first) {
					first = false;

				} else {
					POIUtils.copyRow(oldSheet, newSheet,
							loopDefinition.startLine - 1,
							oldSheet.getLastRowNum(), newSheet.getLastRowNum()
									+ loopDefinition.spaceLine + 1);
				}

				this.setTableData(workbook, newSheet, table);
				newSheet.setRowBreak(newSheet.getLastRowNum()
						+ loopDefinition.spaceLine);

				monitor.worked(1);
			}
		}
	}

	@Override
	public String getTemplateSheetName() {
		return "category_template";
	}

	@Override
	public int count(ERDiagram diagram) {
		int count = diagram.getDiagramContents().getSettings()
				.getCategorySetting().getSelectedCategories().size();

		count += diagram.getDiagramContents().getContents().getTableSet()
				.getList().size();

		return count;
	}

}
