package tmImagePlugIn;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import tm.utilities.TMFile;

public class ImagePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3448272452757601086L;

	private BufferedImage mImage = null ;
	private JScrollPane mScrollPane = null ;
	public final static int IMAGE_WIDTH = 256;
	public final static int IMAGE_HEIGHT = 256;
	public final static int MAX_IMAGE_SIZE = IMAGE_WIDTH * IMAGE_HEIGHT ;

	
	ImagePanel() {
		setLayout( new BorderLayout() ) ;
		BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB) ;
		changeImage( image ) ;
//		setDoubleBuffered(false);
	}
	
	void changeImage( BufferedImage pImage ) {
		// Out with the old
		if( mScrollPane != null ) {
			remove( mScrollPane ) ;
			mScrollPane = null ;
			mImage = null ;
		}
		
		// In with the new
		
		ImageIcon icon = new ImageIcon(	pImage , "" ) ;
		ScrollablePicture label = new ScrollablePicture(icon, 1) ;
		JScrollPane pane = new JScrollPane( label,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ) ;
		mImage = pImage ;
		mScrollPane = pane ;
		add(pane, BorderLayout.CENTER) ;
		revalidate();
	}
	
	void updateImage(){
		mScrollPane.update(getGraphics());
	}
	
	
	public void readFile( TMFile tmFile ) throws IOException {
        BufferedImage buffer = tmFile.readImage() ;
        if( buffer == null ) throw new IOException() ;
		int height = buffer.getHeight() ;
		int width = buffer.getWidth() ;
		if( width* height > MAX_IMAGE_SIZE )
			throw new IOException("Image is too big." );
        changeImage(buffer) ;
	}
	
	public void writeFile( File pFile ) throws IOException {
		if( mImage == null) throw new IOException() ;
        ImageIO.write( mImage, "jpeg", pFile ) ;
	}
	
	public BufferedImage getImage(){
		return mImage;
	}

}
