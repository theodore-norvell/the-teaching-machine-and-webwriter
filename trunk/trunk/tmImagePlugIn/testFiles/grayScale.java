/**** grayScale.java *******************
 *
 * @Date 14-May-2008
 *
 ********************************************/

import tm.scripting.ScriptManager;
import java.io.InputStream;
import java.io.IOException;


public class GrayScale {


	public GrayScale(){
		
	}
	
	public void doTests(){
		ScriptManager.relay("imagePlugIn", "loadInputImage");
		int width = ScriptManager.relayRtnInt("imagePlugIn", "getImageWidth");
	    int height = ScriptManager.relayRtnInt("imagePlugIn", "getImageHeight");
	    for (int x = 0; x < width; x++)
	        for (int y = 0; y < height; y++) {
	            int pixel = ScriptManager.relayRtnInt("imagePlugIn", "getPixelAt", x, y);
	            int red = (pixel & 0xff0000)/ (256 * 256);
	            int green = (pixel & 0x00ff00)/ (256);
	            int blue = pixel & 0x0000ff;
	            int gray = (red + blue + green)/3;
	            ScriptManager.relay("imagePlugIn", "setPixelAt", x, y,
	            		gray + 256 * gray + 256 * 256 * gray);
	        }
		ScriptManager.relay("imagePlugIn", "displayOutputImage");
	}
	    
	

	public static void main() {
		GrayScale gs = new GrayScale();
		gs.doTests();
	}
}
