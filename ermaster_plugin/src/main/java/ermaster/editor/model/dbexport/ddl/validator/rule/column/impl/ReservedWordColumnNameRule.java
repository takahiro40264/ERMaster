package ermaster.editor.model.dbexport.ddl.validator.rule.column.impl;

import org.eclipse.core.resources.IMarker;
import ermaster.ResourceString;
import ermaster.editor.model.dbexport.ddl.validator.ValidateResult;
import ermaster.editor.model.dbexport.ddl.validator.rule.column.ColumnRule;
import ermaster.editor.model.diagram_contents.element.node.table.ERTable;
import ermaster.editor.model.diagram_contents.element.node.table.column.NormalColumn;

public class ReservedWordColumnNameRule extends ColumnRule {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(ERTable table, NormalColumn column) {
		if (column.getPhysicalName() != null) {
			if (this.getDBManager().isReservedWord(column.getPhysicalName())) {
				ValidateResult validateResult = new ValidateResult();
				validateResult
						.setMessage(ResourceString
								.getResourceString("error.validate.reserved.column.name1")
								+ table.getPhysicalName()
								+ ResourceString
										.getResourceString("error.validate.reserved.column.name2")
								+ column.getPhysicalName());
				validateResult.setLocation(table.getLogicalName());
				validateResult.setSeverity(IMarker.SEVERITY_WARNING);
				validateResult.setObject(table);

				this.addError(validateResult);
			}
		}

		return true;
	}
}
