#!/bin/bash
#
# mvnで依存関係を更新したライブラリをlib/にコピーしてMANIFEST.MFに追記するスクリプト
# MANIFEST.MFの内容をスクリプト内で直書きしているので注意
#
# 2025/6/28 TakahiroOta
#
/bin/rm -f target/dependency/*.jar
mvn dependency:copy-dependencies -DincludeScope=runtime
/bin/cp -f target/dependency/*.jar lib/
/bin/cp -f META-INF/MANIFEST.MF MANIFEST.MF.bk

# Bundle-ClassPath の冒頭部分を manifest 変数に格納
# 注意: 最後の行のコンマは、JARリストが続くためここでは付けない
manifest="Manifest-Version: 1.0
Bundle-ManifestVersion: 2
Bundle-Name: ermaster-c_plugin
Bundle-SymbolicName: ermaster-c_plugin;singleton:=true
Bundle-Version: 0.0.1
Bundle-Activator: ermaster.Activator
Bundle-Localization: plugin
Require-Bundle: org.eclipse.ui,
 org.eclipse.core.runtime,
 org.eclipse.gef,
 org.eclipse.ui.views,
 org.eclipse.ui.ide,
 org.eclipse.core.resources,
 org.eclipse.core.expressions,
 org.eclipse.ui.editors,
 org.eclipse.text,
 org.eclipse.jface.text,
 org.eclipse.ui.workbench.texteditor,
 org.apache.ant,
 org.eclipse.core.filesystem;bundle-version=\"1.2.1\",
 org.eclipse.search;bundle-version=\"3.7.0\",
 org.eclipse.pde.api.tools.ui;bundle-version=\"1.0.301\",
 org.eclipse.pde.ds.ui;bundle-version=\"1.0.100\",
 org.eclipse.pde.ua.ui;bundle-version=\"1.0.100\",
 org.eclipse.pde.ui;bundle-version=\"3.6.100\"
Bundle-ActivationPolicy: lazy
Bundle-ClassPath: .,
 icons/,
lib/commons-codec-1.13.jar,
lib/commons-collections4-4.4.jar,
lib/commons-compress-1.19.jar,
lib/commons-math3-3.6.1.jar,
lib/curvesapi-1.06.jar,
lib/logback-classic-1.5.11.jar,
lib/logback-core-1.5.11.jar,
lib/poi-4.1.2.jar,
lib/poi-ooxml-4.1.2.jar,
lib/poi-ooxml-schemas-4.1.2.jar,
lib/slf4j-api-2.0.11.jar,
lib/slf4j-api-2.0.17.jar,
lib/SparseBitSet-1.2.jar,
lib/super-csv-2.4.0.jar,
lib/xmlbeans-3.1.0.jar
# libディレクトリ内の .jar ファイルをヌル文字区切りで配列に格納
jar_files_array=()
while IFS= read -r -d '' file_path; do
    jar_files_array+=("$file_path")
done < <(find lib -name "*.jar" -print0)

# Bundle-ClassPath の JAR エントリ部分を構築
classpath_jars_string=""
for i in "${!jar_files_array[@]}"; do
    file_path="${jar_files_array[i]}"
    
    # 各JARエントリは改行とスペースで始まり、コンマで終わる（最後の要素以外）
    classpath_jars_string+="\n $file_path"
    if [[ "$i" -lt $(( ${#jar_files_array[@]} - 1 )) ]]; then
        classpath_jars_string+=","
    fi
done

# 構築したJARエントリを manifest 変数に追加
if [[ -n "$classpath_jars_string" ]]; then
    manifest+="$classpath_jars_string"
fi

# manifest 変数の残りの部分を追加
manifest+="
Bundle-RequiredExecutionEnvironment: JavaSE-1.8
Eclipse-LazyStart: true
Bundle-Vendor: HighWideSystem
Export-Package: ermaster,
 ermaster.common.dialog,
 ermaster.common.exception,
 ermaster.common.widgets,
 ermaster.common.widgets.table,
 ermaster.db,
 ermaster.db.impl.db2,
 ermaster.db.impl.db2.tablespace,
 ermaster.db.impl.hsqldb,
 ermaster.db.impl.mysql,
 ermaster.db.impl.mysql.tablespace,
 ermaster.db.impl.oracle,
 ermaster.db.impl.oracle.tablespace,
 ermaster.db.impl.postgres,
 ermaster.db.impl.postgres.tablespace,
 ermaster.db.impl.sqlite,
 ermaster.db.impl.sqlserver,
 ermaster.db.impl.sqlserver.tablespace,
 ermaster.db.impl.standard_sql,
 ermaster.db.sqltype,
 ermaster.editor,
 ermaster.editor.controller.command,
 ermaster.editor.controller.command.category,
 ermaster.editor.controller.command.common,
 ermaster.editor.controller.command.common.notation,
 ermaster.editor.controller.command.dbimport,
 ermaster.editor.controller.command.diagram_contents.element.connection,
 ermaster.editor.controller.command.diagram_contents.element.connection.relation,
 ermaster.editor.controller.command.diagram_contents.element.connection.relation.bendpoint,
 ermaster.editor.controller.command.diagram_contents.element.node,
 ermaster.editor.controller.command.diagram_contents.element.node.category,
 ermaster.editor.controller.command.diagram_contents.element.node.note,
 ermaster.editor.controller.command.diagram_contents.element.node.table_view,
 ermaster.editor.controller.command.diagram_contents.not_element.dictionary,
 ermaster.editor.controller.command.diagram_contents.not_element.group,
 ermaster.editor.controller.command.diagram_contents.not_element.index,
 ermaster.editor.controller.command.diagram_contents.not_element.sequence,
 ermaster.editor.controller.command.diagram_contents.not_element.tablespace,
 ermaster.editor.controller.command.diagram_contents.not_element.trigger,
 ermaster.editor.controller.command.edit,
 ermaster.editor.controller.command.testdata,
 ermaster.editor.controller.command.tracking,
 ermaster.editor.controller.editpart,
 ermaster.editor.controller.editpart.element,
 ermaster.editor.controller.editpart.element.connection,
 ermaster.editor.controller.editpart.element.node,
 ermaster.editor.controller.editpart.element.node.column,
 ermaster.editor.controller.editpart.element.node.removed,
 ermaster.editor.controller.editpart.outline,
 ermaster.editor.controller.editpart.outline.dictionary,
 ermaster.editor.controller.editpart.outline.group,
 ermaster.editor.controller.editpart.outline.index,
 ermaster.editor.controller.editpart.outline.sequence,
 ermaster.editor.controller.editpart.outline.table,
 ermaster.editor.controller.editpart.outline.tablespace,
 ermaster.editor.controller.editpart.outline.trigger,
 ermaster.editor.controller.editpart.outline.view,
 ermaster.editor.controller.editpolicy,
 ermaster.editor.controller.editpolicy.element.connection,
 ermaster.editor.controller.editpolicy.element.node,
 ermaster.editor.controller.editpolicy.element.node.note,
 ermaster.editor.controller.editpolicy.element.node.table_view,
 ermaster.editor.controller.editpolicy.not_element,
 ermaster.editor.controller.editpolicy.not_element.group,
 ermaster.editor.controller.editpolicy.not_element.index,
 ermaster.editor.controller.editpolicy.not_element.sequence,
 ermaster.editor.controller.editpolicy.not_element.tablespace,
 ermaster.editor.controller.editpolicy.not_element.trigger,
 ermaster.editor.model,
 ermaster.editor.model.dbexport.db,
 ermaster.editor.model.dbexport.ddl,
 ermaster.editor.model.dbexport.ddl.validator,
 ermaster.editor.model.dbexport.ddl.validator.rule,
 ermaster.editor.model.dbexport.ddl.validator.rule.all,
 ermaster.editor.model.dbexport.ddl.validator.rule.column,
 ermaster.editor.model.dbexport.ddl.validator.rule.column.impl,
 ermaster.editor.model.dbexport.ddl.validator.rule.table,
 ermaster.editor.model.dbexport.ddl.validator.rule.table.impl,
 ermaster.editor.model.dbexport.ddl.validator.rule.tablespace,
 ermaster.editor.model.dbexport.ddl.validator.rule.tablespace.impl,
 ermaster.editor.model.dbexport.ddl.validator.rule.view,
 ermaster.editor.model.dbexport.ddl.validator.rule.view.impl,
 ermaster.editor.model.dbexport.excel,
 ermaster.editor.model.dbexport.excel.sheet_generator,
 ermaster.editor.model.dbexport.html,
 ermaster.editor.model.dbexport.html.page_generator,
 ermaster.editor.model.dbexport.html.page_generator.impl,
 ermaster.editor.model.dbexport.image,
 ermaster.editor.model.dbexport.testdata,
 ermaster.editor.model.dbexport.testdata.impl,
 ermaster.editor.model.dbimport,
 ermaster.editor.model.diagram_contents,
 ermaster.editor.model.diagram_contents.element.connection,
 ermaster.editor.model.diagram_contents.element.node,
 ermaster.editor.model.diagram_contents.element.node.category,
 ermaster.editor.model.diagram_contents.element.node.model_properties,
 ermaster.editor.model.diagram_contents.element.node.note,
 ermaster.editor.model.diagram_contents.element.node.table,
 ermaster.editor.model.diagram_contents.element.node.table.column,
 ermaster.editor.model.diagram_contents.element.node.table.index,
 ermaster.editor.model.diagram_contents.element.node.table.properties,
 ermaster.editor.model.diagram_contents.element.node.table.unique_key,
 ermaster.editor.model.diagram_contents.element.node.view,
 ermaster.editor.model.diagram_contents.element.node.view.properties,
 ermaster.editor.model.diagram_contents.not_element.dictionary,
 ermaster.editor.model.diagram_contents.not_element.group,
 ermaster.editor.model.diagram_contents.not_element.sequence,
 ermaster.editor.model.diagram_contents.not_element.tablespace,
 ermaster.editor.model.diagram_contents.not_element.trigger,
 ermaster.editor.model.edit,
 ermaster.editor.model.search,
 ermaster.editor.model.settings,
 ermaster.editor.model.testdata,
 ermaster.editor.model.tracking,
 ermaster.editor.persistent,
 ermaster.editor.persistent.impl,
 ermaster.editor.view,
 ermaster.editor.view.action,
 ermaster.editor.view.action.category,
 ermaster.editor.view.action.dbexport,
 ermaster.editor.view.action.dbimport,
 ermaster.editor.view.action.edit,
 ermaster.editor.view.action.group,
 ermaster.editor.view.action.line,
 ermaster.editor.view.action.option,
 ermaster.editor.view.action.option.notation,
 ermaster.editor.view.action.option.notation.design,
 ermaster.editor.view.action.option.notation.level,
 ermaster.editor.view.action.option.notation.system,
 ermaster.editor.view.action.option.notation.type,
 ermaster.editor.view.action.outline,
 ermaster.editor.view.action.outline.index,
 ermaster.editor.view.action.outline.notation.type,
 ermaster.editor.view.action.outline.sequence,
 ermaster.editor.view.action.outline.tablespace,
 ermaster.editor.view.action.outline.trigger,
 ermaster.editor.view.action.printer,
 ermaster.editor.view.action.search,
 ermaster.editor.view.action.testdata,
 ermaster.editor.view.action.tracking,
 ermaster.editor.view.action.translation,
 ermaster.editor.view.action.zoom,
 ermaster.editor.view.contributor,
 ermaster.editor.view.dialog.category,
 ermaster.editor.view.dialog.common,
 ermaster.editor.view.dialog.dbexport,
 ermaster.editor.view.dialog.dbimport,
 ermaster.editor.view.dialog.edit,
 ermaster.editor.view.dialog.element,
 ermaster.editor.view.dialog.element.relation,
 ermaster.editor.view.dialog.element.table,
 ermaster.editor.view.dialog.element.table.sub,
 ermaster.editor.view.dialog.element.table.tab,
 ermaster.editor.view.dialog.element.view,
 ermaster.editor.view.dialog.element.view.tab,
 ermaster.editor.view.dialog.group,
 ermaster.editor.view.dialog.option,
 ermaster.editor.view.dialog.option.tab,
 ermaster.editor.view.dialog.outline.sequence,
 ermaster.editor.view.dialog.outline.tablespace,
 ermaster.editor.view.dialog.outline.trigger,
 ermaster.editor.view.dialog.printer,
 ermaster.editor.view.dialog.search,
 ermaster.editor.view.dialog.testdata,
 ermaster.editor.view.dialog.testdata.detail,
 ermaster.editor.view.dialog.testdata.detail.tab,
 ermaster.editor.view.dialog.tracking,
 ermaster.editor.view.dialog.translation,
 ermaster.editor.view.dialog.word,
 ermaster.editor.view.dialog.word.column,
 ermaster.editor.view.dialog.word.column.real,
 ermaster.editor.view.dialog.word.word,
 ermaster.editor.view.drag_drop,
 ermaster.editor.view.editmanager,
 ermaster.editor.view.figure,
 ermaster.editor.view.figure.anchor,
 ermaster.editor.view.figure.border,
 ermaster.editor.view.figure.connection,
 ermaster.editor.view.figure.connection.decoration,
 ermaster.editor.view.figure.connection.decoration.idef1x,
 ermaster.editor.view.figure.connection.decoration.ie,
 ermaster.editor.view.figure.handle,
 ermaster.editor.view.figure.layout,
 ermaster.editor.view.figure.table,
 ermaster.editor.view.figure.table.column,
 ermaster.editor.view.figure.table.style,
 ermaster.editor.view.figure.table.style.frame,
 ermaster.editor.view.figure.table.style.funny,
 ermaster.editor.view.figure.table.style.simple,
 ermaster.editor.view.figure.view,
 ermaster.editor.view.outline,
 ermaster.editor.view.property_source,
 ermaster.editor.view.tool,
 ermaster.extention,
 ermaster.preference,
 ermaster.preference.jdbc,
 ermaster.preference.template,
 ermaster.preference.translation,
 ermaster.test,
 ermaster.util,
 ermaster.util.io,
 ermaster.wizard,
 ermaster.wizard.page
"
echo -e "$manifest" > META-INF/MANIFEST.MF

