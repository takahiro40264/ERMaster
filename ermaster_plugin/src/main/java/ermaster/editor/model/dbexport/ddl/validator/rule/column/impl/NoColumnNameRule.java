package ermaster.editor.model.dbexport.ddl.validator.rule.column.impl;

import org.eclipse.core.resources.IMarker;
import ermaster.ResourceString;
import ermaster.editor.model.dbexport.ddl.validator.ValidateResult;
import ermaster.editor.model.dbexport.ddl.validator.rule.column.ColumnRule;
import ermaster.editor.model.diagram_contents.element.node.table.ERTable;
import ermaster.editor.model.diagram_contents.element.node.table.column.NormalColumn;

public class NoColumnNameRule extends ColumnRule {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(ERTable table, NormalColumn column) {
		if (column.getPhysicalName() == null
				|| column.getPhysicalName().trim().equals("")) {
			ValidateResult validateResult = new ValidateResult();
			validateResult.setMessage(ResourceString
					.getResourceString("error.validate.no.column.name")
					+ table.getPhysicalName());
			validateResult.setLocation(table.getLogicalName());
			validateResult.setSeverity(IMarker.SEVERITY_WARNING);
			validateResult.setObject(table);

			this.addError(validateResult);
		}

		return true;
	}
}
