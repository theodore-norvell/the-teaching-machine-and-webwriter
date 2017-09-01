//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

package tm.displayEngine;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;
import java.util.Hashtable;

import tm.clc.datum.AbstractArrayDatum;
import tm.clc.datum.AbstractIntDatum;
import tm.clc.datum.AbstractPointerDatum;
import tm.configuration.Configuration;
import tm.displayEngine.generators.AbstractGenerator;  //Rev94 all three
import tm.displayEngine.generators.MalleableGenerator;
import tm.displayEngine.generators.SelectionGenerator;
import tm.interfaces.Datum;
import tm.utilities.Assert;  //Rev 94
import tm.utilities.Debug;

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
public abstract class DataVisualizerAdapter extends OldDisplayAdapter{
	
	private static final long serialVersionUID = 633568484394506313L;
	public final int SELECTION = 0; //Rev94 all three
	public final int MALLEABLE = 1;
	public final int LOCAL = 2;
	
	/* The current generator for the display */  //Rev 94 these replaced vectors and EXPLICIT_MODE
	protected AbstractGenerator myGenerator;
	/* Allows a subClass to use a local generator. */
	protected AbstractGenerator localGenerator;
	
	protected int generatorMode = SELECTION; 
	
	protected Hashtable<String,Image> snapshots;
	Vector<Boolean> comparisonSet; // Set of comparisons called for

	
	
/**
 * 
 * @param dc the display context provided by the object managing this display
 * @param configId the id used in the config file for this display
 */
	public DataVisualizerAdapter(DisplayManager dm, String configId) {
		super(dm, configId);		// Automatic scrollbars
		myGenerator = new SelectionGenerator(dm, this);		
//		setPreferredSize(this.getViewportSize());
		generatorMode = SELECTION;
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
	public void refresh() { //Rev 94 - changed 
		myGenerator.refresh();
		if (myGenerator.isMalleable())
			winnow();
		super.refresh(); 
	}
	
/**
 * Internal Scripting Function. Adds datums explicitly to the  generator.
 * Has no effect if generatorMode is not MALLEABLE
 * 
 * @param datum the datum to be added to the generator
 */
	public final void addToGenerator(Datum datum){
		Debug.getInstance().msg(Debug.DISPLAY, "addToGenerator(" + datum + ")");
		Assert.check(myGenerator.isMalleable(), AbstractGenerator.CANT_ADD); //Rev 94 used to just return
		myGenerator.add(datum);
		winnow();
	}
	
/**
 *  Clear the generator. Although created as an internal scripting function
 *  it can be called generally as it has no arguments.
 *
 */
	public final void clearGenerator(){ //Rev94 added assertion
		Assert.check(myGenerator.isMalleable(), AbstractGenerator.CANT_CLEAR);
		myGenerator.clear();
	}
	
	
		
	
	/**
	 *  Internal Scripting Function
	 *  check whether datum1 is equal to datum2. Both datums must be in memory
	 *  Because the model code is allowed to specify an arbitrary pair of datums
	 *  (and datums do not exist, as such, in model code, only variables which
	 *  end up as being represented by datums in the Teaching Machine (this) code)
	 *  compile time checking has to be relaxed. So far only the relayCall function
	 *  relaxes compile time argument checking so compareDatums can only be invoked
	 *  via the relayCall method. Which means it cannot return its result to the
	 *  model code side. Instead it accumulates it in the comparisonSet vector
	 *
	 */
	//Rev94 removed SetReference and CompareReference methods
	public final void compareDatums(Datum datum1, Datum datum2){
    	if (comparisonSet == null) comparisonSet = new Vector<Boolean>();
    	// Since isEqual is no longer available to the visualization side,
    	// we replace `boolean result = datum1.isEqual(datum2);` with
    	boolean result = datum1.getValueString().equals( datum2.getValueString() ) ;
    	Debug.getInstance().msg(Debug.DISPLAY, "result is " + result);
    	comparisonSet.add(new Boolean(result));
	}
	
		
/**
 * Sets the mode for generating datums for this visualizer.
 * General control, won't work for internal scripting (see below).
 * 
 * @param genMode one of SELECTION, MALLEABLE, LOCAL
 */	
	//Rev 94 replaced setExplicitMode
	public final void setGeneratorMode(int genMode){
		if (generatorMode == genMode) return;
		generatorMode = genMode;
		switch(genMode){
		case SELECTION: 
			myGenerator = new SelectionGenerator(context, this);
			break;
		case MALLEABLE:
			myGenerator = new MalleableGenerator(context, this);
			break;
		case LOCAL:
			Assert.check(localGenerator != null, "Can't switch to nonexistent local generator.");
			myGenerator = localGenerator;
			break;			
		}
	}
	
	/**
	 * Internal scripting version of setGeneratorMode (arguments sent in by
	 * relayCall from within the model code are always encapsulated
	 * as datums).
	 * 
	 * Sets the mode for generating datums for this visualizer.
	 * 
	 * @param genMode an integer datum holding one of
	 *                   SELECTION, MALLEABLE, LOCAL
	 */	
	public final void setGeneratorMode(Datum genMode){
		Assert.check(genMode instanceof AbstractIntDatum, "Generator mode must be an integer");
		setGeneratorMode((int)(((AbstractIntDatum)genMode).getValue()));
	}

	
	/**
	 * Overide to reduce the generator to only that part of
	 * it which the particular display is designed to handle. May be left empty.
	 */
	protected void winnow(){
	}

/********* External Scripting Controls *****************/

	
	/**
	 * External Scripting Funtion for retreiving comparisons created
	 * by internal scripting.
	 * @param n the n'th comparison (must be between 0 and size of comparison
	 *           set - 1)
	 * @return the (boolean) result of the n'th comparison
	 */
	
	public final Boolean getComparison(int n){
		if (n >=0 && n < comparisonSet.size())
			return comparisonSet.get(n);
		return new Boolean(false);
	}

	public final void snapshot(String porthole){
		Image snap = createImage(getWidth(),getHeight());
		Graphics2D offscreen = (Graphics2D)snap.getGraphics();
		refresh();  // Make sure everything is up to date
		Debug.getInstance().msg(Debug.DISPLAY, "Taking snapshot, drawing to image " + snap);
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
	
/***** End of External Scripting Controls **************/
	
	
	/** A Datum conversion routine to extract an ArrayDatum 
	 * in a language independent way. Passing an entire Array
	 * to a PlugIn function does not always result in an actual
	 * ArrayDatum. For example, in Java, an array declaration
	 * actually creates a reference to the Array itself which
	 * has to be created on the heap using the New operator.
	 * Thus the Datum passed is a reference to a ArrayDatum.
	 * 
	 * Moreover, multiple Datums can overlay the same address.
	 * In C++, the declaration double p[WIDTH][HEIGHT]; creates
	 * three datums starting at the beginning address of the array.
	 * The 2D array datum whose zero'th row (zero'th child) is
	 * a 1D array datum plus the element at p[0][]0] which is a
	 * double. calls in C could produce any of the three. This
	 * routine always returns the top level datum.
	 * 
	 * @param i the i'th Datum in myGenerator
	 * @return the top level AbstractArrayDatum which includes the Datum 
	 */
	
	
	protected AbstractArrayDatum extractArrayDatum(int i) {
		if( !(0 <= i &&  i < myGenerator.getNumChildren()) ) return null ;
		Datum datum = myGenerator.getChildAt(i);
		if (datum == null) return null;
		/* It's possible the datum is a reference to an array 
		 * or if the model code is C++ it might even be a
		 * reference to the 0'th element of the array */
		 if (datum instanceof AbstractPointerDatum)
			datum = ((AbstractPointerDatum) datum).deref();
		//System.out.println("Got datum " + datum);

		/* In C++, an array of type can also be represented as a
		 * pointer to type. Once dereferenced the datum will be
		 * the zeroth element in the array rather than the array.
		 * However, the datum's parent will be an array. The
		 * while handles the multidimensional case
		 */
		Datum parent = datum.getParent();
		while (parent != null && parent instanceof AbstractArrayDatum){
			datum = parent;
			parent = datum.getParent();
		}
		if (datum instanceof AbstractArrayDatum)
			return (AbstractArrayDatum) datum;
		return null;
	}

	
	
	/** The display is being terminated.
	 * Any extra clean-up code needed should be put here. 
	 */
	public void dispose() {
		super.dispose();		
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
