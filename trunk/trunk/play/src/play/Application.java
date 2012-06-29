/**
 * Application.java - play - PLAY
 * 
 * Created on Feb 8, 2012 by Kai Zhu
 */
package play;

import javax.swing.UIManager;

import play.controller.Controller;

/**
 * This class creates the application window and initialize the controllers
 * 
 * @author Kai Zhu
 * 
 */
public class Application {

    /**
     * @param args
     */
    public static void main(String[] args) {
	// set UI look and feel
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	Controller controller = Controller.getInstance();
	controller.init();
	controller.startApplication();
    }

}
