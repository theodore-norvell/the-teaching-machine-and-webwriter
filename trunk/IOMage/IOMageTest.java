import Viewer.ImageServer;

public class IOMageTest {
   
        
   public static void main(String[] args) {
        int width = ImageServer.getImageWidth();
        int height = ImageServer.getImageHeight();
        int[][] pixels = new int[width][height];
        ImageServer.createOutputImage(width, height);
        for(int col = 0; col < width; col++)
        	for (int row = 0; row < height; row++) {
        		int pixel = ImageServer.getPixelAt(row, col);
				int red = (pixel & 0xFF0000) >> 16;
				int green = (pixel & 0xFF00) >> 8;
				int blue = (pixel & 0xFF);
				int grey = (red + green + blue) / 3;
        		ImageServer.putPixelAt(grey,row,col);
        	}
        ImageServer.closeOutputImage();
        return ;
   }
}
