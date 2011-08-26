package tmImagePlugIn;

import javax.swing.JComponent;

import tm.interfaces.DisplayContextInterface;

import mmgood.MediaTypeI;
import mmgood.StatusI;

public class ImageMediaType implements MediaTypeI {
	private JComponent leftComponent ;
	private JComponent rightComponent ;

	public ImageMediaType(StatusI pStatus, DisplayContextInterface context ) {
		leftComponent = new ImageIOComponent(pStatus, "Left Image", context ) ;
		rightComponent = new ImageIOComponent(pStatus, "Right Image", context ) ;
	}
	
	public JComponent getLeftComponent() {
		return leftComponent;
	}

	public JComponent getRightComponent() {
		return rightComponent;
	}

}
