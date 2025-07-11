package ermaster.extention;

import org.eclipse.jface.action.IAction;
import ermaster.editor.ERDiagramEditor;

/**
 * 拡張ポイントから読み込むクラスのインターフェイス
 */
public interface IERDiagramActionFactory {

	/**
	 * IAction を実装したクラスを返す
	 * 
	 * @param editor
	 * @return IAction
	 */
	public IAction createIAction(ERDiagramEditor editor);

}
