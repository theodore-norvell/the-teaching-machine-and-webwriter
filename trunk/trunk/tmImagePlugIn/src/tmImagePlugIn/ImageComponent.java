package tmImagePlugIn;


import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import mmgood.StatusI;

public class ImageComponent extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final StatusI mStatus ;
	
	protected final ImagePanel mImagePanel = new ImagePanel() ;
	
	public ImageComponent(StatusI pStatus ) {
		mStatus = pStatus ;
	}
	
		
}