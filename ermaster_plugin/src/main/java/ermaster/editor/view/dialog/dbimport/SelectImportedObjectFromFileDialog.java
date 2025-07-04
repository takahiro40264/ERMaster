package ermaster.editor.view.dialog.dbimport;

import java.util.List;

import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import ermaster.common.exception.InputException;
import ermaster.common.widgets.CompositeFactory;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.dbimport.DBObject;
import ermaster.editor.model.dbimport.DBObjectSet;

public class SelectImportedObjectFromFileDialog extends
		AbstractSelectImportedObjectDialog {

	private Button mergeGroupButton;

	private boolean resultMergeGroup;

	public SelectImportedObjectFromFileDialog(Shell parentShell,
			ERDiagram diagram, DBObjectSet allObjectSet) {
		super(parentShell, diagram, allObjectSet);
	}

	@Override
	protected void initializeOptionGroup(Group group) {
		super.initializeOptionGroup(group);

		this.mergeGroupButton = CompositeFactory.createCheckbox(this, group,
				"label.merge.group", false);
		this.mergeGroupButton.setSelection(true);
	}

	@Override
	protected List<TreeNode> createTreeNodeList() {
		List<TreeNode> treeNodeList = super.createTreeNodeList();

		TreeNode topNode = createTopNode(DBObject.TYPE_NOTE,
				this.dbObjectSet.getNoteList());
		treeNodeList.add(topNode);
		topNode = createTopNode(DBObject.TYPE_GROUP,
				this.dbObjectSet.getGroupList());
		treeNodeList.add(topNode);

		return treeNodeList;
	}

	@Override
	protected void perfomeOK() throws InputException {
		super.perfomeOK();

		this.resultMergeGroup = this.mergeGroupButton.getSelection();
	}

	@Override
	public boolean isMergeGroup() {
		return this.resultMergeGroup;
	}

}
