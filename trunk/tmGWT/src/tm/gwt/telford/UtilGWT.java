package tm.gwt.telford;

import java.util.logging.Level;
/**
 * To provide all logger interface
 * */
import java.util.logging.Logger;
import telford.common.MouseListener;
import tm.gwt.telford.CanvasPeerGWT.MyCanvas;

public class UtilGWT {
	public static Logger logger = Logger.getLogger("gwtLog");
	
	//GWT error codes 
	public final static int COLOR_INVALID_ARGUMENT = 000;
	public final static int LAYOUT_INVALID_ARGUMENT = 001;
	public final static int ERROR_NOT_SUPPORT_CANVAS = 100;
	
	public final static char MARKER1 = '\uffff'; // red
	public final static char MARKER2 = '\ufffe';// non underline
	public final static char MARKER3 = '\ufffd';// underline
	public final static char MARKER4 = '\ufffc';// blue
	public final static char ENDMARKER = '\ufffb';
	public static String error(int error_code, String location) {
		String err_desc = "[ERROR CODE:+ " + error_code + "] ";
		switch (error_code) {
		case 000:
			err_desc = err_desc.concat("invalid argument to initialize a color.");
			break;
		case 001:
			err_desc = err_desc.concat("invalid argument for layout location.");
			break;	
		case 100:
			err_desc = err_desc.concat("browser may not support gwt canvas.");
		default:
			break;
		}
		return err_desc;
	}
	
	public static void wrtLog2Console(String desc){
		logger.log(Level.INFO, desc);
	}
}
