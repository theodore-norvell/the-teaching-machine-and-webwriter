/**
 * Create on October 18, 2006
 */
package Viewer;

import java.awt.image.BufferedImage;

/**
 * @author Michael Bruce-Lockhart, based on code by Yan Zhang
 * 
 */
final public class ImageServer {

	private static BufferedImage viewBuff;

	private static ImageViewer viewer;
	
	private static int[][] inImage;
	private static int width;
	private static int height;

	private static int[][] outImage;
	
	private static ImageServer imageServer;

	private ImageServer() {
	}
	
	public static ImageServer getImageServer(){
		if(imageServer == null)
			imageServer = new ImageServer();
		return imageServer;
	}
	
	public void register (ImageViewer iv){
		viewer = iv;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ioMage.ImageArray#getImage()
	 */
	private static int[][] getImage() {
		System.out.println("Getting image from viewer " + viewer.toString());
		viewBuff = viewer.getImage();
		int[][] pixelArray;
		if (viewBuff == null) {
			pixelArray = null;
		} else {
			width = viewBuff.getWidth();
			height = viewBuff.getHeight();
			pixelArray = new int[height][width];
			for (int row = 0; row < height; row++)
				for (int col = 0; col < width; col++) {
					pixelArray[row][col] = viewBuff.getRGB(col, row);
				}
		}
		return pixelArray;
	}
	
	public static int getPixelAt(int row, int col){
		if (inImage == null) inImage = getImage();
		if (inImage != null) {
			return inImage[row][col];
		}
		return -1;
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ioMage.ImageArray#getImageHeight()
	 */
	public static int getImageHeight() {
		if (inImage == null)
			inImage = getImage();
		if (inImage != null)
			return height;
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ioMage.ImageArray#getImageWidth()
	 */
	public static int getImageWidth() {
		if (inImage == null)
			inImage = getImage();
		if (inImage != null)
			return width;
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ioMage.ImageArray#ouputImage(int[][], int, int)
	 */
	public static void createOutputImage(int height, int width){
		outImage = new int[height][width];		
	}
	public static void putPixelAt(int pixel, int row, int col){
		outImage[row][col] = pixel;		
	}


	public static void closeOutputImage(){
		viewer.outputImage(outImage);
	}
}
