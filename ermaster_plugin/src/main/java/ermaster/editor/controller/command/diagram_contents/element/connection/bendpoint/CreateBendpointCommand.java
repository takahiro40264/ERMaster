package ermaster.editor.controller.command.diagram_contents.element.connection.bendpoint;

import ermaster.editor.controller.command.AbstractCommand;
import ermaster.editor.model.diagram_contents.element.connection.Bendpoint;
import ermaster.editor.model.diagram_contents.element.connection.ConnectionElement;

public class CreateBendpointCommand extends AbstractCommand {

	private ConnectionElement connection;

	int x;

	int y;

	private int index;

	public CreateBendpointCommand(ConnectionElement connection, int x, int y,
			int index) {
		this.connection = connection;
		this.x = x;
		this.y = y;
		this.index = index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		Bendpoint bendpoint = new Bendpoint(this.x, this.y);
		connection.addBendpoint(index, bendpoint);
		
		connection.refreshBendpoint();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		connection.removeBendpoint(index);
		
		connection.refreshBendpoint();
	}
}
