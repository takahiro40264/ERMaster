package ermaster.editor.model.dbexport.excel.sheet_generator;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.ObjectModel;
import ermaster.editor.model.dbexport.excel.ExportToExcelManager.LoopDefinition;
import ermaster.editor.model.diagram_contents.element.node.view.View;
import ermaster.editor.model.progress_monitor.ProgressMonitor;
import ermaster.util.POIUtils;

public class AllViewSheetGenerator extends ViewSheetGenerator {

	@Override
	public void generate(ProgressMonitor monitor, XSSFWorkbook workbook,
			int sheetNo, boolean useLogicalNameAsSheetName,
			Map<String, Integer> sheetNameMap,
			Map<String, ObjectModel> sheetObjectMap, ERDiagram diagram,
			Map<String, LoopDefinition> loopDefinitionMap)
			throws InterruptedException {
		this.clear();

		LoopDefinition loopDefinition = loopDefinitionMap.get(this
				.getTemplateSheetName());

		XSSFSheet newSheet = createNewSheet(workbook, sheetNo,
				loopDefinition.sheetName, sheetNameMap);

		String sheetName = workbook.getSheetName(workbook
				.getSheetIndex(newSheet));

		sheetObjectMap.put(sheetName, diagram.getDiagramContents()
				.getContents().getViewSet());

		XSSFSheet oldSheet = workbook.getSheetAt(sheetNo);

		boolean first = true;

		for (View view : diagram.getDiagramContents().getContents()
				.getViewSet()) {
			monitor.subTaskWithCounter(sheetName + " - " + view.getName());

			if (first) {
				first = false;

			} else {
				POIUtils.copyRow(oldSheet, newSheet,
						loopDefinition.startLine - 1, oldSheet.getLastRowNum(),
						newSheet.getLastRowNum() + loopDefinition.spaceLine + 1);
			}

			this.setViewData(workbook, newSheet, view);

			newSheet.setRowBreak(newSheet.getLastRowNum()
					+ loopDefinition.spaceLine);

			monitor.worked(1);
		}

		if (first) {
			for (int i = loopDefinition.startLine - 1; i <= newSheet
					.getLastRowNum(); i++) {
				XSSFRow row = newSheet.getRow(i);
				if (row != null) {
					newSheet.removeRow(row);
				}
			}
		}
	}

	@Override
	public String getTemplateSheetName() {
		return "all_view_template";
	}

}
