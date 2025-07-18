package ermaster.editor.view.dialog.dbexport;

import java.nio.charset.Charset;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import ermaster.common.widgets.CompositeFactory;
import ermaster.common.widgets.DirectoryText;
import ermaster.common.widgets.MultiLineCheckbox;
import ermaster.editor.model.dbexport.ExportWithProgressManager;
import ermaster.editor.model.dbexport.java.ExportToJavaManager;
import ermaster.editor.model.settings.ExportSetting;
import ermaster.editor.model.settings.Settings;
import ermaster.editor.model.settings.export.ExportJavaSetting;
import ermaster.util.Check;
import ermaster.util.Format;
import ermaster.util.io.FileUtils;

public class ExportToJavaDialog extends AbstractExportDialog {

	private DirectoryText outputDirText;

	private Text packageText;

	private Text classNameSuffixText;

	private MultiLineCheckbox withHibernateButton;

	private Combo fileEncodingCombo;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initialize(Composite parent) {
		this.packageText = CompositeFactory.createText(this, parent,
				"label.package.name", 2, false, true);

		this.classNameSuffixText = CompositeFactory.createText(this, parent,
				"label.class.name.suffix", 2, false, true);

		this.outputDirText = CompositeFactory.createDirectoryText(this, parent,
				"label.output.dir", this.getBaseDir(), "");

		this.fileEncodingCombo = CompositeFactory.createFileEncodingCombo(
				this.diagram.getEditor().getDefaultCharset(), this, parent,
				"label.output.file.encoding", 2);

		Composite checkboxArea = this.createCheckboxArea(parent);

		this.withHibernateButton = CompositeFactory.createMultiLineCheckbox(
				this, checkboxArea, "label.with.hibernate", false, 2);
	}

	@Override
	protected String getErrorMessage() {
		// if (this.outputDirText.isBlank()) {
		// return "error.output.dir.is.empty";
		// }

		if (!Charset.isSupported(this.fileEncodingCombo.getText())) {
			return "error.file.encoding.is.not.supported";
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setData() {
		Settings settings = this.diagram.getDiagramContents().getSettings();
		ExportJavaSetting exportSetting = settings.getExportSetting()
				.getExportJavaSetting();

		String outputPath = exportSetting.getJavaOutput();

		this.outputDirText.setText(FileUtils.getRelativeFilePath(
				this.getBaseDir(), outputPath));

		this.packageText.setText(Format.null2blank(exportSetting
				.getPackageName()));
		this.classNameSuffixText.setText(Format.null2blank(exportSetting
				.getClassNameSuffix()));

		if (!Check.isEmpty(exportSetting.getSrcFileEncoding())) {
			this.fileEncodingCombo.setText(exportSetting.getSrcFileEncoding());
		}

		this.withHibernateButton.setSelection(exportSetting.isWithHibernate());
	}

	@Override
	protected ExportWithProgressManager getExportWithProgressManager(
			ExportSetting exportSetting) {
		ExportJavaSetting exportJavaSetting = exportSetting
				.getExportJavaSetting();

		exportJavaSetting.setJavaOutput(this.outputDirText.getFilePath());
		exportJavaSetting.setPackageName(this.packageText.getText());
		exportJavaSetting
				.setClassNameSuffix(this.classNameSuffixText.getText());
		exportJavaSetting.setSrcFileEncoding(this.fileEncodingCombo.getText());
		exportJavaSetting.setWithHibernate(this.withHibernateButton
				.getSelection());

		return new ExportToJavaManager(exportJavaSetting);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getTitle() {
		return "dialog.title.export.java";
	}

}
