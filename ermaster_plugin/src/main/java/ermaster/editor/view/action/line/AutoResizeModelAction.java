package ermaster.editor.view.action.line;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Event;
import ermaster.ERDiagramActivator;
import ermaster.ImageKey;
import ermaster.ResourceString;
import ermaster.editor.ERDiagramEditor;
import ermaster.editor.controller.command.diagram_contents.element.node.MoveElementCommand;
import ermaster.editor.controller.editpart.element.node.ERTableEditPart;
import ermaster.editor.controller.editpart.element.node.IResizable;
import ermaster.editor.controller.editpart.element.node.NodeElementEditPart;
import ermaster.editor.controller.editpart.element.node.NoteEditPart;
import ermaster.editor.model.diagram_contents.element.node.NodeElement;
import ermaster.editor.view.action.AbstractBaseSelectionAction;

public class AutoResizeModelAction extends AbstractBaseSelectionAction {

	public static final String ID = AutoResizeModelAction.class.getName();

	public AutoResizeModelAction(ERDiagramEditor editor) {
		super(ID, ResourceString.getResourceString("action.title.auto.resize"),
				editor);
		this.setImageDescriptor(ERDiagramActivator.getImageDescriptor(ImageKey.RESIZE));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<Command> getCommand(EditPart editPart, Event event) {
		List<Command> commandList = new ArrayList<Command>();

		if (editPart instanceof IResizable) {
			NodeElement nodeElement = (NodeElement) editPart.getModel();

			MoveElementCommand command = new MoveElementCommand(this
					.getDiagram(), ((NodeElementEditPart) editPart).getFigure()
					.getBounds(), nodeElement.getX(), nodeElement.getY(), -1,
					-1, nodeElement);

			commandList.add(command);
		}

		return commandList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean calculateEnabled() {
		GraphicalViewer viewer = this.getGraphicalViewer();

		for (Object object : viewer.getSelectedEditParts()) {
			if (object instanceof NodeElementEditPart) {
				NodeElementEditPart nodeElementEditPart = (NodeElementEditPart) object;

				if (nodeElementEditPart instanceof ERTableEditPart
						|| nodeElementEditPart instanceof NoteEditPart) {
					return true;
				}
			}
		}

		return false;
	}
}