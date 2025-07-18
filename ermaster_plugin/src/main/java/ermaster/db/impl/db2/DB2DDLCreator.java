package ermaster.db.impl.db2;

import ermaster.db.impl.db2.tablespace.DB2TablespaceProperties;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.dbexport.ddl.DDLCreator;
import ermaster.editor.model.diagram_contents.element.node.category.Category;
import ermaster.editor.model.diagram_contents.element.node.table.column.NormalColumn;
import ermaster.editor.model.diagram_contents.not_element.sequence.Sequence;
import ermaster.editor.model.diagram_contents.not_element.tablespace.Tablespace;
import ermaster.util.Check;
import ermaster.util.Format;

public class DB2DDLCreator extends DDLCreator {

	public DB2DDLCreator(ERDiagram diagram, Category targetCategory,
			boolean semicolon) {
		super(diagram, targetCategory, semicolon);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getColulmnDDL(NormalColumn normalColumn) {
		StringBuilder ddl = new StringBuilder();

		ddl.append(super.getColulmnDDL(normalColumn));

		if (normalColumn.isAutoIncrement()) {
			ddl.append(" GENERATED ALWAYS AS IDENTITY ");

			Sequence sequence = normalColumn.getAutoIncrementSetting();

			if (sequence.getIncrement() != null || sequence.getStart() != null) {
				ddl.append("(START WITH ");
				if (sequence.getStart() != null) {
					ddl.append(sequence.getStart());

				} else {
					ddl.append("1");
				}

				if (sequence.getIncrement() != null) {
					ddl.append(", INCREMENT BY ");
					ddl.append(sequence.getIncrement());
				}

				ddl.append(")");
			}
		}

		return ddl.toString();
	}

	@Override
	protected String getDDL(Tablespace tablespace) {
		DB2TablespaceProperties tablespaceProperties = (DB2TablespaceProperties) tablespace
				.getProperties(this.environment, this.getDiagram());

		StringBuilder ddl = new StringBuilder();

		ddl.append("CREATE ");
		if (!Check.isEmpty(tablespaceProperties.getType())) {
			ddl.append(tablespaceProperties.getType());
			ddl.append(" ");
		}

		ddl.append("TABLESPACE ");
		ddl.append(filterName(tablespace.getName()));
		ddl.append(LF());

		if (!Check.isEmpty(tablespaceProperties.getPageSize())) {
			ddl.append(" PAGESIZE ");
			ddl.append(tablespaceProperties.getPageSize());
			ddl.append(LF());
		}

		ddl.append(" MANAGED BY ");
		ddl.append(tablespaceProperties.getManagedBy());
		ddl.append(" USING(");
		ddl.append(tablespaceProperties.getContainer());
		ddl.append(")" + LF());

		if (!Check.isEmpty(tablespaceProperties.getExtentSize())) {
			ddl.append(" EXTENTSIZE ");
			ddl.append(tablespaceProperties.getExtentSize());
			ddl.append(LF());
		}

		if (!Check.isEmpty(tablespaceProperties.getPrefetchSize())) {
			ddl.append(" PREFETCHSIZE ");
			ddl.append(tablespaceProperties.getPrefetchSize());
			ddl.append(LF());
		}

		if (!Check.isEmpty(tablespaceProperties.getBufferPoolName())) {
			ddl.append(" BUFFERPOOL ");
			ddl.append(tablespaceProperties.getBufferPoolName());
			ddl.append(LF());
		}

		if (this.semicolon) {
			ddl.append(";");
		}

		return ddl.toString();
	}

	@Override
	public String getDDL(Sequence sequence) {
		StringBuilder ddl = new StringBuilder();

		String description = sequence.getDescription();
		if (this.semicolon && !Check.isEmpty(description)
				&& this.ddlTarget.inlineTableComment) {
			ddl.append("-- ");
			ddl.append(replaceLF(description, LF() + "-- "));
			ddl.append(LF());
		}

		ddl.append("CREATE ");
		ddl.append("SEQUENCE ");
		ddl.append(filterName(this.getNameWithSchema(sequence.getSchema(),
				sequence.getName())));
		if (!Check.isEmpty(sequence.getDataType())) {
			ddl.append(" AS ");
			String dataType = sequence.getDataType();
			dataType = dataType.replaceAll("\\(p\\)",
					"(" + Format.toString(sequence.getDecimalSize() + ")"));
			ddl.append(dataType);
		}
		if (sequence.getIncrement() != null) {
			ddl.append(" INCREMENT BY ");
			ddl.append(sequence.getIncrement());
		}
		if (sequence.getMinValue() != null) {
			ddl.append(" MINVALUE ");
			ddl.append(sequence.getMinValue());
		}
		if (sequence.getMaxValue() != null) {
			ddl.append(" MAXVALUE ");
			ddl.append(sequence.getMaxValue());
		}
		if (sequence.getStart() != null) {
			ddl.append(" START WITH ");
			ddl.append(sequence.getStart());
		}
		if (!sequence.isNocache() && sequence.getCache() != null) {
			ddl.append(" CACHE ");
			ddl.append(sequence.getCache());
		}
		if (sequence.isCycle()) {
			ddl.append(" CYCLE");
		}
		if (sequence.isNocache()) {
			ddl.append(" NOCACHE");
		}
		if (sequence.isOrder()) {
			ddl.append(" ORDER");
		}
		if (this.semicolon) {
			ddl.append(";");
		}

		return ddl.toString();

	}

}
