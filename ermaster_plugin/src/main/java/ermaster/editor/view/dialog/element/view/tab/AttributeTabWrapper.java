package ermaster.editor.view.dialog.element.view.tab;

import org.eclipse.swt.widgets.TabFolder;
import ermaster.editor.model.diagram_contents.element.node.view.View;
import ermaster.editor.view.dialog.element.table_view.tab.AbstractAttributeTabWrapper;
import ermaster.editor.view.dialog.element.view.ViewDialog;
import ermaster.editor.view.dialog.word.column.AbstractColumnDialog;
import ermaster.editor.view.dialog.word.column.ViewColumnDialog;

public class AttributeTabWrapper extends AbstractAttributeTabWrapper {

	private View copyData;

	public AttributeTabWrapper(ViewDialog viewDialog, TabFolder parent,
			View copyData) {
		super(viewDialog, parent, copyData);

		this.copyData = copyData;
	}

	@Override
	protected AbstractColumnDialog createColumnDialog() {
		return new ViewColumnDialog(this.getShell(), this.copyData);
	}

	@Override
	protected String getGroupAddButtonLabel() {
		return "label.button.add.group.to.view";
	}
}
