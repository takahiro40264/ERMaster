package ermaster.editor.view.action.option.notation.type;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Event;
import ermaster.ResourceString;
import ermaster.editor.ERDiagramEditor;
import ermaster.editor.controller.command.display.ChangeViewModeCommand;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.view.action.AbstractBaseAction;

public abstract class AbstractChangeViewAction extends AbstractBaseAction {

	public AbstractChangeViewAction(String id, String type,
			ERDiagramEditor editor) {
		super(id, null, IAction.AS_RADIO_BUTTON, editor);
		this.setText(ResourceString
				.getResourceString("action.title.change.mode.to." + type));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(Event event) {
		if (!this.isChecked()) {
			return;
		}

		ERDiagram diagram = this.getDiagram();

		ChangeViewModeCommand command = new ChangeViewModeCommand(diagram, this
				.getViewMode());

		this.execute(command);
	}

	protected abstract int getViewMode();
}
