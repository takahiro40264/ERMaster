package ermaster.editor.model.dbexport.ddl.validator.rule.view;

import java.util.ArrayList;
import java.util.List;

import ermaster.db.DBManager;
import ermaster.db.DBManagerFactory;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.dbexport.ddl.validator.ValidateResult;
import ermaster.editor.model.dbexport.ddl.validator.rule.BaseRule;
import ermaster.editor.model.diagram_contents.element.node.view.View;

public abstract class ViewRule extends BaseRule {

	private List<ValidateResult> errorList;

	private String database;

	public ViewRule() {
		this.errorList = new ArrayList<ValidateResult>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addError(ValidateResult errorMessage) {
		this.errorList.add(errorMessage);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ValidateResult> getErrorList() {
		return this.errorList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		this.errorList.clear();
	}

	public boolean validate(ERDiagram diagram) {
		this.database = diagram.getDatabase();

		for (View view : diagram.getDiagramContents().getContents()
				.getViewSet()) {
			if (!this.validate(view)) {
				return false;
			}
		}

		return true;
	}

	protected DBManager getDBManager() {
		return DBManagerFactory.getDBManager(this.database);
	}

	abstract public boolean validate(View view);
}
