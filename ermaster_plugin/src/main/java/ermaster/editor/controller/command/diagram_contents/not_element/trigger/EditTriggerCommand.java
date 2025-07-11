package ermaster.editor.controller.command.diagram_contents.not_element.trigger;

import ermaster.editor.controller.command.AbstractCommand;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.diagram_contents.not_element.trigger.Trigger;
import ermaster.editor.model.diagram_contents.not_element.trigger.TriggerSet;

public class EditTriggerCommand extends AbstractCommand {

	private TriggerSet triggerSet;

	private Trigger oldTrigger;

	private Trigger newTrigger;

	public EditTriggerCommand(ERDiagram diagram, Trigger oldTrigger,
			Trigger newTrigger) {
		this.triggerSet = diagram.getDiagramContents().getTriggerSet();
		this.oldTrigger = oldTrigger;
		this.newTrigger = newTrigger;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		this.triggerSet.remove(this.oldTrigger);
		this.triggerSet.addObject(this.newTrigger);
		
		this.triggerSet.refresh();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		this.triggerSet.remove(this.newTrigger);
		this.triggerSet.addObject(this.oldTrigger);

		this.triggerSet.refresh();
	}
}
