package ermaster.editor.model.dbexport.ddl.validator.rule.table.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import ermaster.ResourceString;
import ermaster.editor.model.dbexport.ddl.validator.ValidateResult;
import ermaster.editor.model.dbexport.ddl.validator.rule.table.TableRule;
import ermaster.editor.model.diagram_contents.element.node.table.ERTable;
import ermaster.editor.model.diagram_contents.element.node.table.column.Column;
import ermaster.editor.model.diagram_contents.element.node.table.column.NormalColumn;
import ermaster.editor.model.diagram_contents.not_element.group.ColumnGroup;
import ermaster.util.Format;

public class DuplicatedColumnNameRule extends TableRule {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validate(ERTable table) {
		Set<String> columnNameSet = new HashSet<String>();

		for (Column column : table.getColumns()) {
			if (column instanceof ColumnGroup) {
				ColumnGroup columnGroup = (ColumnGroup) column;

				for (NormalColumn normalColumn : columnGroup.getColumns()) {
					String columnName = Format.null2blank(
							normalColumn.getPhysicalName()).toLowerCase();

					if (columnNameSet.contains(columnName)) {
						ValidateResult result = new ValidateResult();
						result
								.setMessage(ResourceString
										.getResourceString("error.validate.duplicated.column.name1")
										+ table.getPhysicalName()
										+ ResourceString
												.getResourceString("error.validate.duplicated.column.name2")
										+ columnName);
						result.setLocation(table.getLogicalName());
						result.setSeverity(IMarker.SEVERITY_WARNING);
						result.setObject(table);

						this.addError(result);
					}

					columnNameSet.add(columnName);
				}
			} else if (column instanceof NormalColumn) {
				NormalColumn normalColumn = (NormalColumn) column;

				String columnName = normalColumn.getPhysicalName();

				if (columnNameSet.contains(columnName)) {
					ValidateResult result = new ValidateResult();
					result
							.setMessage(ResourceString
									.getResourceString("error.validate.duplicated.column.name1")
									+ table.getPhysicalName()
									+ ResourceString
											.getResourceString("error.validate.duplicated.column.name2")
									+ columnName);
					result.setLocation(table.getLogicalName());
					result.setSeverity(IMarker.SEVERITY_WARNING);
					result.setObject(table);

					this.addError(result);
				}

				columnNameSet.add(columnName);
			}
		}

		return true;
	}
}
