package telford.client.view;
import java.util.logging.Level;
/**
 * To provide all logger interface
 * */
import java.util.logging.Logger;

public class GWTUtil {
	public final static int ERROR_INVALID_ARGUMENT = 000;
	public static Logger logger = Logger.getLogger("gwtLog");
	
	public static String error(int error_code, String location) {
		String err_desc = "[ERROR CODE:+ " + error_code + "] ";
		switch (error_code) {
		case 000:
			err_desc = err_desc.concat("invalid argument to initialize a color.");
			break;

		default:
			break;
		}
		return err_desc;
	}
	
	public static void wrtLog2Console(String desc){
		logger.log(Level.INFO, desc);
	}
}
