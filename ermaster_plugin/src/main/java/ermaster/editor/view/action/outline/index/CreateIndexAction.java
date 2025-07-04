package ermaster.editor.view.action.outline.index;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.PlatformUI;
import ermaster.ResourceString;
import ermaster.editor.controller.command.diagram_contents.not_element.index.CreateIndexCommand;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.diagram_contents.element.node.table.ERTable;
import ermaster.editor.view.action.outline.AbstractOutlineBaseAction;
import ermaster.editor.view.dialog.element.table.sub.IndexDialog;

public class CreateIndexAction extends AbstractOutlineBaseAction {

	public static final String ID = CreateIndexAction.class.getName();

	public CreateIndexAction(TreeViewer treeViewer) {
		super(ID,
				ResourceString.getResourceString("action.title.create.index"),
				treeViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Event event) {

		ERDiagram diagram = this.getDiagram();

		List selectedEditParts = this.getTreeViewer().getSelectedEditParts();
		EditPart editPart = (EditPart) selectedEditParts.get(0);
		ERTable table = (ERTable) editPart.getModel();

		IndexDialog dialog = new IndexDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), null, table);

		if (dialog.open() == IDialogConstants.OK_ID) {
			CreateIndexCommand command = new CreateIndexCommand(diagram, dialog
					.getResultIndex());

			this.execute(command);
		}
	}

}
