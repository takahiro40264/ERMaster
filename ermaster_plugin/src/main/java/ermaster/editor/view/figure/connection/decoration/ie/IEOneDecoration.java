package ermaster.editor.view.figure.connection.decoration.ie;

import org.eclipse.draw2d.geometry.PointList;
import ermaster.editor.view.figure.connection.decoration.ERDecoration;

public class IEOneDecoration extends ERDecoration {

	public IEOneDecoration() {
		super();

		PointList pointList = new PointList();

		pointList.addPoint(-13, -12);
		pointList.addPoint(-13, 12);

		this.setTemplate(pointList);
		this.setScale(1, 1);
	}

}
