package ermaster.editor.view.action.outline.tablespace;

import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Event;
import ermaster.ERDiagramActivator;
import ermaster.ResourceString;
import ermaster.db.EclipseDBManagerFactory;
import ermaster.editor.controller.command.diagram_contents.not_element.tablespace.CreateTablespaceCommand;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.view.action.outline.AbstractOutlineBaseAction;
import ermaster.editor.view.dialog.outline.tablespace.TablespaceDialog;

public class CreateTablespaceAction extends AbstractOutlineBaseAction {

	public static final String ID = CreateTablespaceAction.class.getName();

	public CreateTablespaceAction(TreeViewer treeViewer) {
		super(ID, ResourceString
				.getResourceString("action.title.create.tablespace"),
				treeViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Event event) {
		ERDiagram diagram = this.getDiagram();

		TablespaceDialog dialog = EclipseDBManagerFactory.getEclipseDBManager(
				diagram).createTablespaceDialog();
		if (dialog == null) {
			ERDiagramActivator
					.showMessageDialog("dialog.message.tablespace.not.supported");

		} else {
			dialog.init(null, diagram);

			if (dialog.open() == IDialogConstants.OK_ID) {
				CreateTablespaceCommand command = new CreateTablespaceCommand(
						diagram, dialog.getResult());
				this.execute(command);
			}
		}
	}

}
