package ermaster.editor.model.dbexport.ddl.validator.rule.table.impl;

import org.eclipse.core.resources.IMarker;
import ermaster.ResourceString;
import ermaster.editor.model.dbexport.ddl.validator.ValidateResult;
import ermaster.editor.model.dbexport.ddl.validator.rule.table.TableRule;
import ermaster.editor.model.diagram_contents.element.node.table.ERTable;

public class NoColumnRule extends TableRule {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(ERTable table) {
		if (table.getColumns().isEmpty()) {
			ValidateResult validateResult = new ValidateResult();
			validateResult.setMessage(ResourceString
					.getResourceString("error.validate.no.column")
					+ table.getPhysicalName());
			validateResult.setLocation(table.getLogicalName());
			validateResult.setSeverity(IMarker.SEVERITY_WARNING);
			validateResult.setObject(table);

			this.addError(validateResult);
		}

		return true;
	}
}
