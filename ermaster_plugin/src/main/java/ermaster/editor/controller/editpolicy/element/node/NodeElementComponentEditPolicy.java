package ermaster.editor.controller.editpolicy.element.node;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import ermaster.ERDiagramActivator;
import ermaster.editor.controller.command.diagram_contents.element.connection.DeleteConnectionCommand;
import ermaster.editor.controller.command.diagram_contents.element.connection.relation.DeleteRelationCommand;
import ermaster.editor.controller.command.diagram_contents.element.node.DeleteElementCommand;
import ermaster.editor.controller.command.diagram_contents.element.node.category.DeleteCategoryCommand;
import ermaster.editor.controller.editpart.DeleteableEditPart;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.diagram_contents.element.connection.ConnectionElement;
import ermaster.editor.model.diagram_contents.element.connection.Relation;
import ermaster.editor.model.diagram_contents.element.node.NodeElement;
import ermaster.editor.model.diagram_contents.element.node.category.Category;

public class NodeElementComponentEditPolicy extends ComponentEditPolicy {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Command createDeleteCommand(GroupRequest request) {
		try {
			if (this.getHost() instanceof DeleteableEditPart) {
				DeleteableEditPart editPart = (DeleteableEditPart) this
						.getHost();

				if (!editPart.isDeleteable()) {
					return null;
				}

			} else {
				return null;
			}

			Set<NodeElement> targets = new HashSet<NodeElement>();

			for (Object object : request.getEditParts()) {
				EditPart editPart = (EditPart) object;

				Object model = editPart.getModel();

				if (model instanceof NodeElement) {
					targets.add((NodeElement) model);
				}
			}

			ERDiagram diagram = (ERDiagram) this.getHost().getRoot()
					.getContents().getModel();
			NodeElement element = (NodeElement) this.getHost().getModel();

			if (element instanceof Category) {
				return new DeleteCategoryCommand(diagram, (Category) element);
			}

			if (!diagram.getDiagramContents().getContents().contains(element)
					&& !(element instanceof Category)) {
				return null;
			}

			CompoundCommand command = new CompoundCommand();

			for (ConnectionElement connection : element.getIncomings()) {
				if (connection instanceof Relation) {
					command.add(new DeleteRelationCommand(
							(Relation) connection, true));

				} else {
					command.add(new DeleteConnectionCommand(connection));
				}
			}

			for (ConnectionElement connection : element.getOutgoings()) {

				NodeElement target = connection.getTarget();

				if (!targets.contains(target)) {
					if (connection instanceof Relation) {
						command.add(new DeleteRelationCommand(
								(Relation) connection, true));
					} else {
						command.add(new DeleteConnectionCommand(connection));
					}
				}
			}

			command.add(new DeleteElementCommand(diagram, element));

			return command.unwrap();

		} catch (Exception e) {
			ERDiagramActivator.showExceptionDialog(e);
		}

		return null;
	}

}
