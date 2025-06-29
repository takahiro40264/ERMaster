package ermaster.editor.controller.command.diagram_contents.element.node.category;

import java.util.ArrayList;
import java.util.List;

import ermaster.editor.controller.command.AbstractCommand;
import ermaster.editor.model.ERDiagram;
import ermaster.editor.model.diagram_contents.element.node.category.Category;
import ermaster.editor.model.settings.CategorySetting;

public class DeleteCategoryCommand extends AbstractCommand {

	private ERDiagram diagram;

	private CategorySetting categorySettings;

	private Category category;

	private List<Category> oldAllCategories;

	private List<Category> oldSelectedCategories;

	public DeleteCategoryCommand(ERDiagram diagram, Category category) {
		this.diagram = diagram;
		this.categorySettings = diagram.getDiagramContents().getSettings()
				.getCategorySetting();
		this.category = category;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		this.oldAllCategories = new ArrayList<Category>(this.categorySettings
				.getAllCategories());
		this.oldSelectedCategories = new ArrayList<Category>(
				this.categorySettings.getSelectedCategories());

		this.diagram.removeCategory(category);
		this.diagram.refreshChildren();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doUndo() {
		this.categorySettings.setAllCategories(oldAllCategories);
		this.categorySettings.setSelectedCategories(oldSelectedCategories);
		this.diagram.restoreCategories();
		this.diagram.refreshChildren();
	}
}
