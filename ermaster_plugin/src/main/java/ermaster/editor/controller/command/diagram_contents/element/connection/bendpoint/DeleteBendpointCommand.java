package ermaster.editor.controller.command.diagram_contents.element.connection.bendpoint;

import ermaster.editor.controller.command.AbstractCommand;
import ermaster.editor.model.diagram_contents.element.connection.Bendpoint;
import ermaster.editor.model.diagram_contents.element.connection.ConnectionElement;

public class DeleteBendpointCommand extends AbstractCommand {

	private ConnectionElement connection;

	private Bendpoint oldBendpoint;

	private int index;

	public DeleteBendpointCommand(ConnectionElement connection, int index) {
		this.connection = connection;
		this.index = index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		this.oldBendpoint = this.connection.getBendpoints().get(index);
		this.connection.removeBendpoint(index);
		
		this.connection.refreshBendpoint();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		this.connection.addBendpoint(index, oldBendpoint);
		
		this.connection.refreshBendpoint();
	}
}
