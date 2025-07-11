package ermaster.editor.model.dbexport.ddl.validator.rule.view.impl;

import org.eclipse.core.resources.IMarker;
import ermaster.ResourceString;
import ermaster.editor.model.dbexport.ddl.validator.ValidateResult;
import ermaster.editor.model.dbexport.ddl.validator.rule.view.ViewRule;
import ermaster.editor.model.diagram_contents.element.node.view.View;

public class NoViewSqlRule extends ViewRule {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(View view) {
		if (view.getSql() == null || view.getSql().trim().equals("")) {
			ValidateResult validateResult = new ValidateResult();
			validateResult.setMessage(ResourceString
					.getResourceString("error.validate.no.view.sql"));
			validateResult.setLocation(view.getLogicalName());
			validateResult.setSeverity(IMarker.SEVERITY_WARNING);
			validateResult.setObject(view);

			this.addError(validateResult);
		}

		return true;
	}
}
