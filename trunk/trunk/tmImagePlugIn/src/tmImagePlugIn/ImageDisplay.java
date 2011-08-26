package tmImagePlugIn;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import mmgood.MediaTypeI;
import mmgood.StatusI;

import tm.displayEngine.DisplayAdapter;
import tm.interfaces.Datum;
import tm.interfaces.DisplayContextInterface;
import tm.clc.datum.AbstractIntDatum;
import tm.clc.datum.AbstractPointerDatum;
import tm.utilities.Assert;

/* ImageDisplay is replacing MainPanel in mmgood */
public class ImageDisplay extends DisplayAdapter implements StatusI{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1723063487583071216L;
	protected static final String NOT_INT
	= "expected an integer argument";
	protected static final String NOT_LEFT_OR_RIGHT
	= "expected image LEFT (1) or RIGHT (2)";
	protected final static int LEFT = 1;
	protected final static int RIGHT = 2;
	
	protected JLabel mStatusLineLabel = new JLabel() ;
	final private MediaTypeI mMediaType  ;
	
	private ImageIOComponent leftComponent;
	private ImageIOComponent rightComponent;
	

	
	public ImageDisplay(DisplayContextInterface dc, String configId) {
		super(dc, configId);	// Needed
/*		JPanel displayPanel = new JPanel();
		BoxLayout topBoxLayout = new BoxLayout( displayPanel, BoxLayout.X_AXIS ) ;
		displayPanel.setLayout( topBoxLayout ) ;*/
		mMediaType = new ImageMediaType(this, dc) ;
		leftComponent = (ImageIOComponent)mMediaType.getLeftComponent();
		rightComponent = (ImageIOComponent)mMediaType.getRightComponent();
/*		displayPanel.add(leftComponent) ;
		displayPanel.add(rightComponent ) ;
		
		setLayout( new BorderLayout() ) ;
		add( displayPanel, BorderLayout.CENTER ) ;
		add( mStatusLineLabel, BorderLayout.SOUTH) ;
		*/
	}

	/* Implementation of StatusI interface */
	
	public void setStatusMessage( final String pMessage ) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mStatusLineLabel.setText( pMessage ) ; } } ) ;
	}
	
	public void popupWarning( final String pTitle, final String pMessage ) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null,
					    pMessage,  pTitle,
					    JOptionPane.WARNING_MESSAGE); } } ) ;
	}
	
	public void loadImage(long imageNo){
		getImageComponent((int)imageNo).invokeLoadAction();
	}
	
	public void createImage(long width, long height, long imageNo){
		System.out.println("createImage " + imageNo + " as " + width + " x " + height);
		ImageIOComponent theComponent = getImageComponent((int)imageNo);
		theComponent.setImage(new BufferedImage((int)width, (int) height, BufferedImage.TYPE_INT_ARGB));	
	}
	
/*	public void scaleImageUp(long imageNo){
		ImageIOComponent theComponent = getImageComponent((int) imageNo);
		BufferedImage scaled = (BufferedImage)(theComponent.getImage().getScaledInstance(
				ImagePanel.IMAGE_WIDTH, ImagePanel.IMAGE_HEIGHT, Image.SCALE_SMOOTH));
//		System.out.println("Scaled image is " + scaled.getWidth() + " x " + scaled.getHeight());
		theComponent.setImage(scaled);
	}*/

	
	public void updateImage(long imageNo){
		getImageComponent((int)imageNo).mImagePanel.updateImage();
	}
	
	public void saveImage(long imageNo){
		getImageComponent((int)imageNo).invokeSaveAction();
	}
	
	public int getImageWidth(long imageNo){
		return 	getImageComponent((int)imageNo).getImage().getWidth();
	}
	
	public int getImageHeight(long imageNo){
		return getImageComponent((int)imageNo).getImage().getHeight();
	}
	
	public int getPixelAt(long xPos, long yPos, long imageNo){
		int x = (int)xPos;
		int y = (int)yPos;
		int img = (int)imageNo;
//		System.out.println("retrieving pixel from (" + x + ", " + y + "): " );
		Assert.runTimeError(x >=0 && x < getImageWidth(img),"x out of range");
		Assert.runTimeError(y >= 0 && y < getImageHeight(img), "y out of range");
		int pixel = getImageComponent(img).getImage().getRGB(x,y);
//		System.out.println(pixel);
		return 0x00ffffff&pixel;
	}
	
	public void setPixelAt(long xPos, long yPos, long pix, long imageNo){
		int x = (int) xPos;
		int y = (int) yPos;
		int pixel = (int) pix;
		int img = (int) imageNo;
		Assert.runTimeError(x >=0 && x < getImageWidth(img),"x out of range");
		Assert.runTimeError(y >= 0 && y < getImageHeight(img), "y out of range");
		getImageComponent(img).getImage().setRGB(x,y, 0xff000000|pixel);
	}
	
/*	private ImageIOComponent getImageComponent(Datum imgDatum){
		return getImageComponent(getIntDatum(imgDatum));
	}*/
	
	private ImageIOComponent getImageComponent(int img){
		Assert.runTimeError(img==LEFT||img==RIGHT,NOT_LEFT_OR_RIGHT);
		if (img == LEFT) return leftComponent;
		return rightComponent;
	}
	

	
/*	private int getIntDatum(Datum datum){
		if (datum instanceof AbstractPointerDatum){ // Might be an address
			AbstractPointerDatum pointer = (AbstractPointerDatum)datum;
			Assert.check(pointer.getValue()!= 0, "Null pointer to int");
			datum = pointer.deref();
		}
		Assert.check(datum instanceof AbstractIntDatum,NOT_INT);
		return (int)((AbstractIntDatum)datum).getValue();
	}*/
	

	/* All Visualization plugins require a drawArea method. This method will be called
	 * whenever the system needs to paint or repaint the visualization (for example,
	 * because its window is brought to the front, or the Teaching Machine has changed
	 * state or because a request to make a snapshot of the visualization has been made).
	 * This code colourizes the array data based on how old it is, not on any calls to 
	 * setColor
	 */
	
	public void drawArea(Graphics2D screen){
		BufferedImage left = leftComponent.getImage();
		BufferedImage right = rightComponent.getImage();

//		screen.drawImage(left, 0, 0, Color.WHITE, leftComponent);
//		screen.drawImage(right, ImagePanel.IMAGE_WIDTH + 50, 0, Color.WHITE, rightComponent);

		int BORDER = 10;
		int pictureWidth = (getWidth() - 3* BORDER )/2;
		int pictureHeight = (getHeight() - 2* BORDER );
		
		int leftImageWidth = left.getWidth();
		int leftImageHeight = left.getHeight();
		int rightImageWidth = right.getWidth();
		int rightImageHeight = right.getHeight();
		int scaleLeftY = pictureHeight/leftImageHeight;
		int scaleLeftX = pictureWidth/leftImageWidth;
		int scaleRightY = pictureHeight/rightImageHeight;
		int scaleRightX = pictureWidth/rightImageWidth;
		for (int x = 0; x < leftImageWidth; x++)
			for (int y = 0; y < leftImageHeight; y++){
				screen.setColor(new Color(left.getRGB(x, y)));
				screen.fillRect(BORDER + x*scaleLeftX, BORDER + y*scaleLeftY, scaleLeftX, scaleLeftY);
//				screen.setColor(Color.BLACK);
//				screen.drawRect(x*scaleLeftX, y*scaleLeftY, scaleLeftX, scaleLeftY);
		}
		for (int x = 0; x < rightImageWidth; x++)
			for (int y = 0; y < rightImageHeight; y++){
				screen.setColor(new Color(right.getRGB(x, y)));
				screen.fillRect(2*BORDER + pictureWidth + x*scaleRightX, BORDER + y*scaleRightY, scaleRightX, scaleRightY);
//				screen.setColor(Color.BLACK);
//				screen.drawRect(ImagePanel.IMAGE_WIDTH + 50 + x*scaleRightX, y*scaleRightY, scaleRightX, scaleRightY);
		}
	}
	

	public String toString() { return "ImageDisplay";}	

}
