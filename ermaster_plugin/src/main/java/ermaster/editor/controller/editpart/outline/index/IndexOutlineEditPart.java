package ermaster.editor.controller.editpart.outline.index;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.SelectEditPartTracker;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.PlatformUI;
import ermaster.ERDiagramActivator;
import ermaster.ImageKey;
import ermaster.editor.controller.command.diagram_contents.not_element.index.ChangeIndexCommand;
import ermaster.editor.controller.editpart.DeleteableEditPart;
import ermaster.editor.controller.editpart.outline.AbstractOutlineEditPart;
import ermaster.editor.controller.editpolicy.not_element.index.IndexComponentEditPolicy;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.diagram_contents.element.node.table.index.Index;
import ermaster.editor.view.dialog.element.table.sub.IndexDialog;

public class IndexOutlineEditPart extends AbstractOutlineEditPart implements
		DeleteableEditPart {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshOutlineVisuals() {
		Index index = (Index) this.getModel();

		this.setWidgetText(this.getDiagram().filter(index.getName()));
		this.setWidgetImage(ERDiagramActivator.getImage(ImageKey.INDEX));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createEditPolicies() {
		this.installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new IndexComponentEditPolicy());
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
	public void performRequest(Request request) {
		Index index = (Index) this.getModel();
		ERDiagram diagram = this.getDiagram();

		if (request.getType().equals(RequestConstants.REQ_OPEN)) {
			IndexDialog dialog = new IndexDialog(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), index,
					index.getTable());

			if (dialog.open() == IDialogConstants.OK_ID) {
				ChangeIndexCommand command = new ChangeIndexCommand(diagram,
						index, dialog.getResultIndex());

				this.execute(command);
			}
		}

		super.performRequest(request);
	}
}
