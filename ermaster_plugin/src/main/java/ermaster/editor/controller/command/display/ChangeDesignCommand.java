package ermaster.editor.controller.command.display;

import ermaster.editor.controller.command.AbstractCommand;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.settings.Settings;

public class ChangeDesignCommand extends AbstractCommand {

	private ERDiagram diagram;

	private String oldDesign;

	private String newDesign;

	private Settings settings;

	public ChangeDesignCommand(ERDiagram diagram, String design) {
		this.diagram = diagram;
		this.settings = this.diagram.getDiagramContents().getSettings();
		this.newDesign = design;
		this.oldDesign = this.settings.getTableStyle();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		this.settings.setTableStyle(this.newDesign);
		this.diagram.refreshSettings();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		this.settings.setTableStyle(this.oldDesign);
		this.diagram.refreshSettings();
	}
}
