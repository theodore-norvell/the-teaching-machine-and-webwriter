package tm.displayEngine;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;
import java.util.Hashtable;

import tm.configuration.Configuration;
import tm.interfaces.Datum;
import tm.interfaces.DisplayContextInterface;
import tm.subWindowPkg.ToolBar;
import tm.subWindowPkg.WorkArea;

/** Data Visualizers are a specialization of Display PlugIns designed to create a
 * visualization of data based on a <em>Generator</em>.
 *<p>
 * A <em>Generator</em>is a set of {@link tm.interfaces.Datum Datums} specifically selected
 * to provide the basis for a visualization. For example, the LinkedDisplay which has been
 * part of the TM since about 2002 is designed to provide a visualization of linked lists. It
 * uses any set of datums as a generator, displaying the generator Datums down the left side,
 * then <em>generating</em> the linked list by following down pointer chains. Any generator
 * Datum which is not a pointer simply doesn't generate a list. This is useful as it permits
 * simple variables (such as indices) to be added into the display if desired.
 * <p>
 * This abstract DataVisualizerAdapter provides default versions of most of the functions
 * required of any {@link DisplayInterface}. In addition it provides additional services
 * needed by Data Visualizer PlugIns such as selection controls. A minimal useful display 
 * can be created by specializing this class and implementing the abstract drawArea() function.
 * <p>
 * See the document <a href="doc-files/tmVisualizationPlugIns.html"> Building Teaching Machine Data Visualizer PlugIns</a>}
 * for details on how to build such a plugIn.
 * 
 * @since March 21, 2007
 * @author mpbl
 */
public abstract class DataVisualizerAdapter extends WorkArea implements DisplayInterface{
	
	private static final long serialVersionUID = 633568484394506313L;
	protected DisplayContextInterface context;
	
/* Note that there is a problem here in that while these two are conceptually generators
 * they do not conform to the generator interface. This needs to be fixed!
 */
	protected Vector<Datum> myGenerator;
	protected Vector<Datum> referenceState;
	protected boolean explicitMode; // true if datums have to be added explicitly
	protected Hashtable<String,Image> snapshots;	
	
	
/**
 * 
 * @param dc the display context provided by the object managing this display
 * @param configId the id used in the config file for this display
 */
	public DataVisualizerAdapter(DisplayContextInterface dc, String configId) {
		super(dc.getImageSource(), configId);		// Automatic scrollbars
		context = dc;
		myGenerator = new Vector<Datum>();
		
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
 * mode by resetting {@code myGenerator} to the
 * current generator of the displayManager, then calls winnow() to
 * allow myGenerator to be pruned to only those datums of interest
 * to the particular display. Since myGenerator copies the references
 * pruning it will not affect other displays' use of the displayManager's
 * generator set.
 * Has no effect in explicit mode which requires that datums be added to
 * the generator explicitly
 * Classes which override this method must either call super.refresh() or
 * call their own drawing routines themselves.
 */
	public void refresh() {
		if (!explicitMode) {
			myGenerator.clear();
			myGenerator.addAll(context.getSelection(this));
			winnow();
		}
		super.refresh(); 
	}
	
/**
 * Adds datums explicitly to the  generator. Has no effect if explicitMode is
 * not set.
 * 
 * @param datum the datum to be added to the generator
 */
	public final void addToGenerator(Datum datum){
//		System.out.println("addingToGenerator");
		if (!explicitMode) return;
		myGenerator.add(datum);
		winnow();
	}
	
/**
 *  clear the generator
 *
 */
	public final void clearGenerator(){
		myGenerator.clear();
	}
	
	
/**
 *  set the reference to the current state of the generator
 *
 */
	public final void setReference(){
		referenceState = new Vector<Datum>();
		for (int i = 0; i < myGenerator.size(); i++)
			referenceState.add(myGenerator.elementAt(i).copy());
		System.out.println("myGenerator");
		dumpGenerator(myGenerator);
		System.out.println("referenceState");
		dumpGenerator(referenceState);
	}
		
/**
 *  check whether the current state is equal to the reference state
 *  Note: this depends upon a reasonable definition of equals for
 *  the datums. Equals has been defined for scalar datums and Java
 *  array datums. Otherwise datums inherit Objects notion of equals
 *
 */
	public final boolean compareReference(){
		if (referenceState.size() != myGenerator.size()) return false;
		for (int i=0; i < myGenerator.size(); i++)
			if(!referenceState.elementAt(i).isEqual(myGenerator.elementAt(i)) ) return false;
		return true;
	}

	private void dumpGenerator(Vector<Datum> gen) {
		System.out.print("generator size " + gen.size() + "{");
		for(int i = 0; i < gen.size() - 1; i++)
			System.out.print(gen.elementAt(i).toString() + ", ");
		System.out.println(gen.elementAt(gen.size() - 1).toString() + "}");
	}
	

		
/**
 * Sets the mode for generating datums for this visualizer. In explicit mode datums have to
 * explicitly included via calls to addToGenerator. In implicit mode the generator is provided
 * by the DisplayManager by polling all displays for datums selected within their own work area.
 * 
 * @param on true for explicit mode, false for implicit mode
 */	
	public final void setExplicitMode(boolean on){
		explicitMode = on;
		myGenerator.clear();
	}
	
	/**
	 * Overide to reduce the generator to only that part of
	 * it which the particular display is designed to handle. May be left empty.
	 */
	protected void winnow(){
	}
	
	public final void snapshot(String porthole){
//		System.out.println("Taking snapshot");
		Image snap = createImage(getWidth(),getHeight());
		Graphics2D offscreen = (Graphics2D)snap.getGraphics();		
		drawArea(offscreen);
		if (snapshots == null) snapshots = new Hashtable<String, Image>();
		snapshots.put(porthole,snap);
	}
	
	public final Image getSnap(String porthole) {
//		System.out.println("snapshot requested from " + toString());
		return snapshots.get(porthole);
	}

	public final int getSnapWidth(String porthole) {
		return getWidth();
	}

	public final int getSnapHeight(String porthole) {
		return getHeight();
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

	/**
	 * Get datums selected within this visualizer. The display manager (@link DisplayManagerInterface)
	 * creates implicit datum generators by polling all displays via getSelected to see which ones
	 * have datum sets selected within their workArea (for example by clicking on a datum with
	 * a mouse) and concatenating the results. Normally only the StoreDisplays respond. Developers
	 * may over-ride this method if they want their Visualizer to act as a datum generator as well
	 * as a datum visualizer.
	 * 
	 * @return a vector holding all datums selected within this display or null if none or this
	 * visualizer does not act as a generator
	 */
	public Vector<Datum> getSelected() {
		// TODO Auto-generated method stub
		return null;
	}
	
/**
 * This method provides the drawing code 
 */
	public abstract void drawArea(Graphics2D screen);

}
