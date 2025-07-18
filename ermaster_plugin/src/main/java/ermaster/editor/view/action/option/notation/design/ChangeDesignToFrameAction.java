package ermaster.editor.view.action.option.notation.design;

import ermaster.editor.ERDiagramEditor;

public class ChangeDesignToFrameAction extends AbstractChangeDesignAction {

	public static final String ID = ChangeDesignToFrameAction.class.getName();

	public static final String TYPE = "frame";

	public ChangeDesignToFrameAction(ERDiagramEditor editor) {
		super(ID, TYPE, editor);
	}

}
