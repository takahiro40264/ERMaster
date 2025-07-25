package ermaster.editor.controller.command.diagram_contents.element.connection;

import ermaster.editor.controller.command.AbstractCommand;
import ermaster.editor.model.diagram_contents.element.connection.ConnectionElement;

public class DeleteConnectionCommand extends AbstractCommand {

	private ConnectionElement connection;

	public DeleteConnectionCommand(ConnectionElement connection) {
		this.connection = connection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		this.connection.delete();

		this.connection.getTarget().refreshTargetConnections();
		this.connection.getSource().refreshSourceConnections();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		this.connection.connect();
		
		this.connection.getTarget().refreshTargetConnections();
		this.connection.getSource().refreshSourceConnections();
	}
}
