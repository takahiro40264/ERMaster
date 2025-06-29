package ermaster.editor.model.dbexport.ddl.validator.rule.all;

import org.eclipse.core.resources.IMarker;
import ermaster.ResourceString;
import ermaster.db.DBManager;
import ermaster.db.DBManagerFactory;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.dbexport.ddl.validator.ValidateResult;
import ermaster.editor.model.dbexport.ddl.validator.rule.BaseRule;
import ermaster.editor.model.diagram_contents.element.node.table.ERTable;
import ermaster.editor.model.diagram_contents.element.node.table.index.Index;
import ermaster.editor.model.diagram_contents.element.node.view.View;
import ermaster.editor.model.diagram_contents.not_element.sequence.Sequence;
import ermaster.editor.model.diagram_contents.not_element.trigger.Trigger;

public class ReservedNameRule extends BaseRule {

	public boolean validate(ERDiagram diagram) {
		DBManager dbManager = DBManagerFactory.getDBManager(diagram);

		for (ERTable table : diagram.getDiagramContents().getContents()
				.getTableSet()) {

			for (Index index : table.getIndexes()) {
				String indexName = index.getName().toLowerCase();

				if (dbManager.isReservedWord(indexName)) {
					ValidateResult validateResult = new ValidateResult();
					validateResult.setMessage(ResourceString
							.getResourceString("error.validate.reserved.name")
							+ " [INDEX] "
							+ indexName
							+ " ("
							+ table.getLogicalName() + ")");
					validateResult.setLocation(indexName);
					validateResult.setSeverity(IMarker.SEVERITY_WARNING);
					validateResult.setObject(index);

					this.addError(validateResult);
				}
			}
		}

		for (Sequence sequence : diagram.getDiagramContents().getSequenceSet()) {
			String name = sequence.getName().toLowerCase();

			if (dbManager.isReservedWord(name)) {
				ValidateResult validateResult = new ValidateResult();
				validateResult.setMessage(ResourceString
						.getResourceString("error.validate.reserved.name")
						+ " [SEQUENCE] " + name);
				validateResult.setLocation(name);
				validateResult.setSeverity(IMarker.SEVERITY_WARNING);
				validateResult.setObject(sequence);

				this.addError(validateResult);
			}
		}

		for (View view : diagram.getDiagramContents().getContents()
				.getViewSet()) {
			String name = view.getName().toLowerCase();

			if (dbManager.isReservedWord(name)) {
				ValidateResult validateResult = new ValidateResult();
				validateResult.setMessage(ResourceString
						.getResourceString("error.validate.reserved.name")
						+ " [VIEW] " + name);
				validateResult.setLocation(name);
				validateResult.setSeverity(IMarker.SEVERITY_WARNING);
				validateResult.setObject(view);

				this.addError(validateResult);
			}
		}

		for (Trigger trigger : diagram.getDiagramContents().getTriggerSet()) {
			String name = trigger.getName().toLowerCase();

			if (dbManager.isReservedWord(name)) {
				ValidateResult validateResult = new ValidateResult();
				validateResult.setMessage(ResourceString
						.getResourceString("error.validate.reserved.name")
						+ " [TRIGGER] " + name);
				validateResult.setLocation(name);
				validateResult.setSeverity(IMarker.SEVERITY_WARNING);
				validateResult.setObject(trigger);

				this.addError(validateResult);
			}
		}

		return true;
	}

}
