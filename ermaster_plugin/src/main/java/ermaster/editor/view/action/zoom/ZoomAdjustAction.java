package ermaster.editor.view.action.zoom;

import org.eclipse.draw2d.zoom.ZoomListener;
import org.eclipse.gef.Disposable;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.jface.action.Action;
import ermaster.ERDiagramActivator;
import ermaster.ImageKey;
import ermaster.ResourceString;

public class ZoomAdjustAction extends Action implements ZoomListener,
		Disposable {

	public static final String ID = ZoomAdjustAction.class.getName();

	protected ZoomManager zoomManager;

	public ZoomAdjustAction(ZoomManager zoomManager) {
		super(ResourceString.getResourceString("action.title.zoom.adjust"),
				ERDiagramActivator.getImageDescriptor(ImageKey.ZOOM_ADJUST));
		this.zoomManager = zoomManager;
		zoomManager.addZoomListener(this);

		setToolTipText(ResourceString
				.getResourceString("action.title.zoom.adjust"));
		setId(ID);
	}

	public void dispose() {
		this.zoomManager.removeZoomListener(this);
	}

	@Override
	public void run() {
		this.zoomManager.setZoomAsText(ZoomManager.FIT_ALL);
	}

	public void zoomChanged(double zoom) {
		setEnabled(true);
	}

}
