import Viewer.Image2Array;

public class IOMageTest {
   
        
   public static void main(String[] args) {
        int width = Image2Array.getImageWidth();
        int height = Image2Array.getImageHeight();
        int[][] pixels = new int[width][height];
        for(int col = 0; col < width; coll++)
        	for (int row = 0; row < height; row++) {
        		pixels[row]col] = Image2Array.getPixelAt(row, col);
        	}
        return ;
   }
}
