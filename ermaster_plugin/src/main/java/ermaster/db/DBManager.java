package ermaster.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.List;

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

public interface DBManager {

	public static final int SUPPORT_AUTO_INCREMENT = 0;

	public static final int SUPPORT_AUTO_INCREMENT_SETTING = 1;

	public static final int SUPPORT_DESC_INDEX = 2;

	public static final int SUPPORT_FULLTEXT_INDEX = 3;

	public static final int SUPPORT_SCHEMA = 4;

	public static final int SUPPORT_SEQUENCE = 5;

	public static final int SUPPORT_SEQUENCE_NOCACHE = 6;

	public String getId();

	public String getURL(String serverName, String dbName, int port);

	public int getDefaultPort();

	public String getDriverClassName();

	public Class<Driver> getDriverClass(String driverClassName);

	public SqlTypeManager getSqlTypeManager();

	public TableProperties createTableProperties(TableProperties tableProperties);

	public TablespaceProperties createTablespaceProperties();

	public TablespaceProperties checkTablespaceProperties(
			TablespaceProperties tablespaceProperties);

	public DDLCreator getDDLCreator(ERDiagram diagram, Category targetCategory,
			boolean semicolon);

	public boolean isSupported(int support);

	public boolean doesNeedURLDatabaseName();

	public boolean doesNeedURLServerName();

	public boolean isReservedWord(String str);

	public List<String> getIndexTypeList(ERTable table);

	public PreImportFromDBManager getPreTableImportManager();

	public ImportFromDBManager getTableImportManager();

	public PreTableExportManager getPreTableExportManager();

	public String[] getCurrentTimeValue();

	public List<String> getImportSchemaList(Connection con) throws SQLException;

	public List<String> getSystemSchemaList();

	public BigDecimal getSequenceMaxValue();

	public List<String> getForeignKeyRuleList();
}
