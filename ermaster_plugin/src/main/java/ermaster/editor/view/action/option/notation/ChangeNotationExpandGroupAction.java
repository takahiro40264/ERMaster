package ermaster.editor.view.action.option.notation;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Event;
import ermaster.ResourceString;
import ermaster.editor.ERDiagramEditor;
import ermaster.editor.controller.command.display.notation.ChangeNotationExpandGroupCommand;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.view.action.AbstractBaseAction;

public class ChangeNotationExpandGroupAction extends AbstractBaseAction {

	public static final String ID = ChangeNotationExpandGroupAction.class
			.getName();

	public ChangeNotationExpandGroupAction(ERDiagramEditor editor) {
		super(ID, null, IAction.AS_CHECK_BOX, editor);
		this.setText(ResourceString
				.getResourceString("action.title.notation.expand.group"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Event event) {
		ERDiagram diagram = this.getDiagram();

		ChangeNotationExpandGroupCommand command = new ChangeNotationExpandGroupCommand(
				diagram, this.isChecked());

		this.execute(command);
	}
}
