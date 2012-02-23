/**
 * Controller.java - play.controller - PLAY
 * 
 * Created on Feb 13, 2012 by Kai Zhu
 */
package play.controller;

import play.ide.handler.ViewHandler;

/**
 * @author Kai Zhu
 * 
 */
public class Controller {

    private ViewHandler viewHandler;

    public Controller() {
	this.viewHandler = new ViewHandler();
    }

    public void startApplication() {
	this.viewHandler.showMainFrame();
    }

}
