package tm.displayEngine;

import java.awt.Component;
import java.awt.Graphics2D;
import java.util.Vector;

import tm.configuration.Configuration;
import tm.displayEngine.GeneratorInterface;
import tm.interfaces.Datum;
import tm.interfaces.DisplayManagerInterface;
import tm.subWindowPkg.ToolBar;
import tm.subWindowPkg.WorkArea;

/** A base implementation of DisplayInterface which can be used as
 * a base class for Display Plugins. It provides default versions of
 * the functions required of any {@link DisplayInterface}. A minimal useful
 * display can be created by specializing this class and over-riding
 * the (empty) drawArea() function
 * @since March 21, 2007
 */
public class DisplayAdapter extends WorkArea implements DisplayInterface{
	
	private static final long serialVersionUID = 633568484394506313L;
	protected DisplayManagerInterface manager;
	protected Vector<Datum> mySelection;
	protected boolean explicitMode; // true if datums have to be added explicitly
	
	
/**
 * Constructor.
 * @param dm the displayManager managing this display
 * @param configId the id used in the config file for this display
 */
	public DisplayAdapter(DisplayManagerInterface dm, String configId) {
		super(dm.getImageSource(), configId);		// Automatic scrollbars
		manager = dm;
		mySelection = new Vector<Datum>();
		/* configId is the string used to label a configuration (in the
		 * configuration file) for this object. Thus, in the case of multiple
		 * DisplayObjects, each can (and generally must) have its own
		 * configuration information. Doubles then to simply
		 * distinguish different objects of the display class.
		 */			
		setPreferredSize(this.getViewportSize());
    	mySubWindow.addWorkArea(this, null);
		mySubWindow.setVisible(true);
		loadInitConfig();
		explicitMode = false;
	}

	public ToolBar getToolBar() {
		// TODO Auto-generated method stub
		return null;
	}
/**
 * This default implementation of refresh effectively implements implicit
 * mode by resetting {@code mySelection} to the
 * current selection of the displayManager, then calls winnow() to
 * allow mySelection to be pruned to only those datums of interest
 * to the particular display. Since mySelection copies the references
 * pruning it will not affect other displays' use of the displayManager's
 * selection set.
 * Has no effect in explicit mode which requires that datums be added to
 * the selection explicitly
 * Classes which override this method must either call super.refresh() or
 * call their own drawing routines themselves.
 */
	public void refresh() {
		if (!explicitMode) {
			mySelection.clear();
			mySelection.addAll(manager.getSelection(this));
			winnow();
		}
		super.refresh(); 
	}
	
/**
 * Adds datums explicitly to the visualazation. Has no effect if explicitMode is
 * not set
 */
	public void addToSelection(Datum datum){
		if (!explicitMode) return;
		mySelection.add(datum);
		winnow();
	}
	
	public void clearSelection(){
		mySelection.clear();
	}
	
	public void setExplicitMode(boolean on){
		explicitMode = on;
		mySelection.clear();
	}
	
	/**
	 * Overide to reduce the general selection to only that part of
	 * it which the particular display is designed to handle. May be left empty.
	 */
	protected void winnow(){
	}
	
	/** The display is being terminated.
	 * Any clean-up code needed should be put here. Usually empty.
	 */
	public void dispose() {
		
	}
	
	

	public void notifyOfLoad(Configuration config) {
		super.notifyOfLoad(config);
		/* TODO if extra configuration info is required overload
		 * this method but make sure super.notifyOfLoad is called 
		 */
		
	}

	public void notifyOfSave(Configuration config) {
		super.notifyOfSave(config);
		/* TODO if extra configuration info is required overload
		 * this method but make sure super.notifyOfSave is called 
		 */
		
	}

	public Vector<Datum> getSelected() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void drawArea(Graphics2D screen){
		/* This method is the drawing code and should be overridden
		by actual displays */
	}

}
