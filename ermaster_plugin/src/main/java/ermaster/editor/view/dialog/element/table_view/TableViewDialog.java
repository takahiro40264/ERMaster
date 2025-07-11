package ermaster.editor.view.dialog.element.table_view;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import ermaster.common.dialog.AbstractTabbedDialog;
import ermaster.editor.model.ERDiagram;

public abstract class TableViewDialog extends AbstractTabbedDialog {

	private EditPartViewer viewer;

	public TableViewDialog(Shell parentShell, EditPartViewer viewer) {
		super(parentShell);

		this.viewer = viewer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialize(Composite composite) {
		this.createTabFolder(composite);
	}

	public EditPartViewer getViewer() {
		return viewer;
	}

	public ERDiagram getDiagram() {
		return (ERDiagram) this.viewer.getContents().getModel();
	}

}
