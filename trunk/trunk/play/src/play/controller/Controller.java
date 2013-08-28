/**
 * Controller.java - play.controller - PLAY
 * 
 * Created on Feb 13, 2012 by Kai Zhu
 */
package play.controller;

import java.awt.Color;

import play.higraph.model.PLAYNode;
import play.higraph.view.PLAYHigraphView;
import play.ide.handler.ViewHandler;
import tm.backtrack.BTTimeManager;

/**
 * @author Kai Zhu
 * 
 */
public class Controller {

    private static Controller INSTANCE = null;

    private static Object LOCK = new Object();

    private static boolean INITIALIZED = false;

    private ViewHandler viewHandler;

    private BTTimeManager btTimeManager;

    private Controller() {
	this.btTimeManager = new BTTimeManager();
    }

    /**
     * Get only one instance by using singleton Pattern
     * 
     * @return
     */
    public static Controller getInstance() {
	if (!Controller.INITIALIZED) {
	    synchronized (Controller.LOCK) {
		if (!Controller.INITIALIZED && (Controller.INSTANCE == null)) {
		    Controller.INSTANCE = new Controller();
		}
		synchronized (Controller.LOCK) {
		    Controller.INITIALIZED = true;
		}
	    }
	}
	return Controller.INSTANCE;
    }

    public void init() {
	this.viewHandler = new ViewHandler(this.btTimeManager);
    }

    /**
     * To start the application
     */
    public void startApplication() {
	this.viewHandler.showMainFrame();
    }

    /**
     * Set a check point and make description as a tip of action
     * 
     * @param description
     */
    public void setCheckPoint(String description) {
	this.btTimeManager.checkpoint(description);
	this.viewHandler.modifyUndoMenuItem(description);
    }

    /**
     * Execute undo
     */
    public void undo() {
	if (this.btTimeManager.canUndo()) {
	    this.btTimeManager.undo();
	    if (this.btTimeManager.canUndo()) {
		this.viewHandler.modifyUndoMenuItem(this.btTimeManager
			.getCurrentDescription());
	    } else {
		this.viewHandler.modifyUndoMenuItem(null);
	    }
	}
    }

    /**
     * Refresh the application modified
     * 
     * @param higraphView
     */
    public void refresh(PLAYHigraphView higraphView) {
	if (higraphView != null) {
	    higraphView.refresh();
	    higraphView.getDisplay().repaint();
	    this.viewHandler.updateNodesOutline(higraphView);
	    // this.viewHandler.updataPropertyPanel(higraphView.getHigraph()
	    //    .getWholeGraph().getPLAYViewSelectionModel());
	}
    }

    public void run() {
	// TODO
    }

    public void createNewClass() {
	  	this.viewHandler.newClass();
	  	System.out.println("class created");
    }
    
   
    public Color getViewDrawColor() {
	return this.viewHandler.getViewPropertiesPanel().getDrawColor();
    }

    public Color getViewFillColor() {
	return Color.LIGHT_GRAY;
	//return this.viewHandler.getViewPropertiesPanel().getFillColor();
    }

    public int getViewStroke() {
	return this.viewHandler.getViewPropertiesPanel().getStroke();
    }

    public boolean isViewFilled() {
	return this.viewHandler.getViewPropertiesPanel().isFilled();
    }

}
