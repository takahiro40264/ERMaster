package ermaster.editor.controller.command.edit;

import ermaster.editor.controller.command.AbstractCommand;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.search.ReplaceManager;
import ermaster.editor.model.search.ReplaceResult;

public class ReplaceCommand extends AbstractCommand {

	private int type;

	private Object object;

	private String keyword;

	private String replaceWord;

	private ReplaceResult result;

	private ERDiagram diagram;

	public ReplaceCommand(ERDiagram diagram, int type, Object object,
			String keyword, String replaceWord) {
		this.diagram = diagram;

		this.type = type;
		this.object = object;
		this.keyword = keyword;
		this.replaceWord = replaceWord;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		this.result = ReplaceManager.replace(this.type, this.object,
				this.keyword, this.replaceWord, this.diagram.getDatabase());

		this.diagram.refreshVisuals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		if (this.result != null) {
			ReplaceManager.undo(this.type, this.object,
					this.result.getOriginal(), this.diagram.getDatabase());

			this.diagram.refreshVisuals();
		}
	}

}
