package ermaster.editor.controller.command.testdata;

import java.util.List;

import ermaster.editor.controller.command.AbstractCommand;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.testdata.TestData;

public class ChangeTestDataCommand extends AbstractCommand {

	private ERDiagram diagram;

	private List<TestData> oldTestDataList;

	private List<TestData> newTestDataList;

	public ChangeTestDataCommand(ERDiagram diagram,
			List<TestData> newTestDataList) {
		this.diagram = diagram;
		this.oldTestDataList = diagram.getDiagramContents().getTestDataList();
		this.newTestDataList = newTestDataList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		this.diagram.getDiagramContents().setTestDataList(newTestDataList);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		this.diagram.getDiagramContents().setTestDataList(oldTestDataList);
	}

}
