package ermaster.db.impl.mysql;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import ermaster.db.DBManagerBase;
import ermaster.db.impl.mysql.tablespace.MySQLTablespaceProperties;
import ermaster.db.sqltype.SqlTypeManager;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.dbexport.db.PreTableExportManager;
import ermaster.editor.model.dbexport.ddl.DDLCreator;
import ermaster.editor.model.dbimport.ImportFromDBManager;
import ermaster.editor.model.dbimport.PreImportFromDBManager;
import ermaster.editor.model.diagram_contents.element.node.category.Category;
import ermaster.editor.model.diagram_contents.element.node.table.ERTable;
import ermaster.editor.model.diagram_contents.element.node.table.properties.TableProperties;
import ermaster.editor.model.diagram_contents.not_element.tablespace.TablespaceProperties;

public class MySQLDBManager extends DBManagerBase {

	public static final String ID = "MySQL";

	private static final ResourceBundle CHARACTER_SET_RESOURCE = ResourceBundle
			.getBundle("mysql_characterset");

	public String getId() {
		return ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDriverClassName() {
		return "com.mysql.jdbc.Driver";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getURL() {
		return "jdbc:mysql://<SERVER NAME>:<PORT>/<DB NAME>";
	}

	public int getDefaultPort() {
		return 3306;
	}

	public SqlTypeManager getSqlTypeManager() {
		return new MySQLSqlTypeManager();
	}

	public TableProperties createTableProperties(TableProperties tableProperties) {
		if (tableProperties != null
				&& tableProperties instanceof MySQLTableProperties) {
			return tableProperties;
		}

		return new MySQLTableProperties();
	}

	public DDLCreator getDDLCreator(ERDiagram diagram, Category targetCategory,
			boolean semicolon) {
		return new MySQLDDLCreator(diagram, targetCategory, semicolon);
	}

	public List<String> getIndexTypeList(ERTable table) {
		List<String> list = new ArrayList<String>();

		list.add("BTREE");

		return list;
	}

	@Override
	protected int[] getSupportItems() {
		return new int[] { SUPPORT_AUTO_INCREMENT,
				SUPPORT_AUTO_INCREMENT_SETTING, SUPPORT_DESC_INDEX,
				SUPPORT_FULLTEXT_INDEX, SUPPORT_SCHEMA };
	}

	public ImportFromDBManager getTableImportManager() {
		return new MySQLTableImportManager();
	}

	public PreImportFromDBManager getPreTableImportManager() {
		return new MySQLPreTableImportManager();
	}

	public PreTableExportManager getPreTableExportManager() {
		return new MySQLPreTableExportManager();
	}

	public TablespaceProperties createTablespaceProperties() {
		return new MySQLTablespaceProperties();
	}

	public TablespaceProperties checkTablespaceProperties(
			TablespaceProperties tablespaceProperties) {

		if (!(tablespaceProperties instanceof MySQLTablespaceProperties)) {
			return new MySQLTablespaceProperties();
		}

		return tablespaceProperties;
	}

	public String[] getCurrentTimeValue() {
		return new String[] { "NOW(), SYSDATE()" };
	}

	public BigDecimal getSequenceMaxValue() {
		return null;
	}

	public static List<String> getCharacterSetList() {
		List<String> list = new ArrayList<String>();

		Enumeration<String> keys = CHARACTER_SET_RESOURCE.getKeys();

		while (keys.hasMoreElements()) {
			list.add(keys.nextElement());
		}

		return list;
	}

	public static List<String> getCollationList(String characterset) {
		List<String> list = new ArrayList<String>();

		if (characterset != null) {
			try {
				String values = CHARACTER_SET_RESOURCE.getString(characterset);

				if (values != null) {
					StringTokenizer tokenizer = new StringTokenizer(values, ",");

					while (tokenizer.hasMoreElements()) {
						String token = tokenizer.nextToken().trim();
						list.add(token);
					}
				}
			} catch (MissingResourceException e) {
			}
		}

		return list;
	}
}
