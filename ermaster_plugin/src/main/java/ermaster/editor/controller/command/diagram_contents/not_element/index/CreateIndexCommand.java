package ermaster.editor.controller.command.diagram_contents.not_element.index;

import java.util.ArrayList;
import java.util.List;

import ermaster.editor.controller.command.AbstractCommand;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.diagram_contents.element.node.table.ERTable;
import ermaster.editor.model.diagram_contents.element.node.table.index.Index;
import ermaster.editor.model.diagram_contents.element.node.table.index.IndexSet;

public class CreateIndexCommand extends AbstractCommand {

	private ERTable table;

	private List<Index> oldIndexList;

	private List<Index> newIndexList;

	private IndexSet indexSet;

	public CreateIndexCommand(ERDiagram diagram, Index newIndex) {
		this.table = newIndex.getTable();
		this.indexSet = diagram.getDiagramContents().getIndexSet();
		
		this.oldIndexList = newIndex.getTable().getIndexes();
		this.newIndexList = new ArrayList<Index>(oldIndexList);

		this.newIndexList.add(newIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		this.table.setIndexes(this.newIndexList);
		this.indexSet.refresh();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		this.table.setIndexes(this.oldIndexList);
		this.indexSet.refresh();
	}
}
