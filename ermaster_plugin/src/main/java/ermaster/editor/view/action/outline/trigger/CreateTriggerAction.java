package ermaster.editor.view.action.outline.trigger;

import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.PlatformUI;
import ermaster.ResourceString;
import ermaster.editor.controller.command.diagram_contents.not_element.trigger.CreateTriggerCommand;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.view.action.outline.AbstractOutlineBaseAction;
import ermaster.editor.view.dialog.outline.trigger.TriggerDialog;

public class CreateTriggerAction extends AbstractOutlineBaseAction {

	public static final String ID = CreateTriggerAction.class.getName();

	public CreateTriggerAction(TreeViewer treeViewer) {
		super(ID, ResourceString
				.getResourceString("action.title.create.trigger"), treeViewer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Event event) {
		ERDiagram diagram = this.getDiagram();

		TriggerDialog dialog = new TriggerDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), null);

		if (dialog.open() == IDialogConstants.OK_ID) {
			CreateTriggerCommand command = new CreateTriggerCommand(diagram,
					dialog.getResult());
			this.execute(command);
		}
	}

}
