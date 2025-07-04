package ermaster.editor.controller.editpart.outline.group;

import java.util.List;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.tools.SelectEditPartTracker;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.PlatformUI;
import ermaster.ERDiagramActivator;
import ermaster.ImageKey;
import ermaster.editor.controller.command.diagram_contents.not_element.group.ChangeGroupCommand;
import ermaster.editor.controller.editpart.DeleteableEditPart;
import ermaster.editor.controller.editpart.outline.AbstractOutlineEditPart;
import ermaster.editor.controller.editpolicy.not_element.group.GroupComponentEditPolicy;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.diagram_contents.not_element.group.ColumnGroup;
import ermaster.editor.model.diagram_contents.not_element.group.CopyGroup;
import ermaster.editor.model.diagram_contents.not_element.group.GroupSet;
import ermaster.editor.view.dialog.group.GroupDialog;

public class GroupOutlineEditPart extends AbstractOutlineEditPart implements
		DeleteableEditPart {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshOutlineVisuals() {
		ColumnGroup columnGroup = (ColumnGroup) this.getModel();

		this.setWidgetText(this.getDiagram().filter(columnGroup.getName()));
		this.setWidgetImage(ERDiagramActivator.getImage(ImageKey.GROUP));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void performRequest(Request request) {
		ColumnGroup columnGroup = (ColumnGroup) this.getModel();
		ERDiagram diagram = this.getDiagram();

		GroupSet groupSet = diagram.getDiagramContents().getGroups();

		if (request.getType().equals(RequestConstants.REQ_OPEN)) {
			GroupDialog dialog = new GroupDialog(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), groupSet, diagram,
					groupSet.indexOf(columnGroup));

			if (dialog.open() == IDialogConstants.OK_ID) {
				List<CopyGroup> newColumnGroups = dialog.getCopyColumnGroups();

				Command command = new ChangeGroupCommand(diagram, groupSet,
						newColumnGroups);

				this.execute(command);
			}
		}

		super.performRequest(request);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DragTracker getDragTracker(Request req) {
		return new SelectEditPartTracker(this);
	}

	public boolean isDeleteable() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createEditPolicies() {
		this.installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new GroupComponentEditPolicy());
	}
}
