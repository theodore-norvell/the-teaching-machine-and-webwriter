package tmplugins.arrayBar;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;

import tm.backtrack.BTVar;
import tm.clc.datum.AbstractArrayDatum;
import tm.clc.datum.AbstractIntDatum;
import tm.displayEngine.DataVisualizerAdapter;
import tm.displayEngine.DisplayManager ;
import tm.interfaces.Datum;
import tm.scripting.ScriptManager;
import tm.utilities.Assert;

public class ArrayBarDisplay extends DataVisualizerAdapter {
	private static final int BAR_THICKNESS = 12;

	private static final int BLACK = 0;
	private static final int BLUE = 1;
	private static final int GREEN = 2;
	private static final int CYAN = 3;
	private static final int RED = 4;
	private static final int MAGENTA = 5;
	private static final int YELLOW = 6;
	private static final int WHITE = 7;
	
	private static final int UNMARKED = GREEN;
	
	BTVar<AbstractArrayDatum> theArray ;
	
	private static final Color colours[]={Color.black, Color.blue, Color.green, Color.cyan,
		Color.red, Color.magenta, Color.yellow, Color.white};
//	private Vector<Datum> datums = new Vector<Datum>(1);
	private int singleValue;
	private int singleColour;
	private int singleHeight = -1;

	ArrayBarDisplay(DisplayManager dc, String configId) {
		super(dc, configId);	// Needed
		//setGeneratorMode(SELECTION); //reset from MALLEABLE 3.31.09 - need to do this better
		theArray = new BTVar<AbstractArrayDatum>( dc.getTimeManager() ) ;
		ScriptManager.getManager().register(this);
	}
	
	public void setArray( Datum d ) {
		if( d instanceof AbstractArrayDatum ) {
			theArray.set( (AbstractArrayDatum) d ) ;
		} else {
			System.err.println("Datum sent to ArrayBarDisplay.setArray is not an array" ) ;
		}
	}
	
	/**
	 * interface routine, designed to be called from within TeachingMachine code
	 * by a relayCall("ArrayBarDisplay", "setColour", from, to, intColour) to the
	 * scriptManager. The idea is to Color the subarray between from and to using
	 * the specified color. Note this doesn't use the Java Color model (because
	 * scripting calls can originate from within C++ code as well as Java).
	 * @param from - an AbstractIntDatum representing a valid index within the array
	 * @param to - an AbstractIntDatum representing a valid index within the same array
	 * @param colour - an int representing a colour - see constants above
	 * Note that this assumes there is only one array selected.
	 */
	public void setColour(long from, long to, long colour){
		int f = (int)from;
		int t = (int)to;
		int c = (int)colour;
		if (theArray.get() != null) { 
			for(int i = f; i <= t; i++){
				theArray.get().getElement(i).setProperty("colour",c);				
			}
		}
		refresh();
	}
	
	public void setInteger(Datum intDatum, Datum hDatum, Datum colour){
		singleValue = (int)(((AbstractIntDatum)intDatum).getValue());
		singleHeight = (int)(((AbstractIntDatum)hDatum).getValue());
		singleColour = (int)(((AbstractIntDatum)colour).getValue());
		refresh();
	}
	

	
	/**
	 * interface routine, designed to be called from within TeachingMachine code
	 * by a relayCall("ArrayBarDisplay", "setColor", i, intColor) to the
	 * scriptManager. Item i in the array is set to color intColor
	 * @param d - an AbstractIntDatum representing a valid index within the array
	 * @param colour a valid colour - RGB 8 colour model
	 * Note that this assumes there is only one array selected.
	 */
	public void setColour(long d, long colour){
		setColour(d, d, colour);
	}
	
	/**
	 * It's an American-centric world! Alternate spelling for setColour routine
	 */
	public void setColor(long d, long color){
		setColour(d, d, color);
	}
	/**
	 * It's an American-centric world! Alternate spelling for setColour routine
	 *
	 */
	public void setColor(long from, long to, long color){
		setColour(from, to, color);
	}

	
	private int getColour(AbstractArrayDatum arrayDatum, int i){
		return getColour(arrayDatum.getElement(i));
	}
	
	private int getColour(Datum datum){
		Object property = datum.getProperty("colour");
		if (property == null) return UNMARKED;
		return ( (Integer)(property)).intValue();		
	}
	

	/* All Visualization plugins require a drawArea method. This method will be called
	 * whenever the system needs to paint or repaint the visualization (for example,
	 * because its window is brought to the front, or the Teaching Machine has changed
	 * state or because a request to make a snapshot of the visualization has been made).
	 * This code colourizes the array data based on how old it is, not on any calls to 
	 * setColor
	 */
	
	public void drawArea(Graphics2D screen){
		if (screen == null ) return;
//		System.out.println("drawArea of ArrayBarDisplay");
		
		AbstractArrayDatum array = theArray.get() ;
		if (array == null) return;
	 // There is a selection 
		long length = array.getNumberOfElements();
		long maxValue = getMax(theArray.get()); 
		for (int i = 0; i < length; i++) {
			long value = getValue(array, i);
			
			/* This is the actual, simple-minded,  drawing code */
			int width = (int)(200*(value/(double)maxValue) + 0.5);
			int x = 10;   // singled-sided for PWV2008 demo
			screen.setColor(colours[getColour(array, i)]);
			screen.fillRect(x, i*BAR_THICKNESS, width, BAR_THICKNESS);
			screen.setColor(Color.black);
			screen.drawRect(x, i*BAR_THICKNESS, width, BAR_THICKNESS);
			screen.drawString(String.valueOf(value), 20, (i+1)*BAR_THICKNESS -1);
			if(singleHeight == i ){
				//					screen.drawString(singleDatum.getName(), 230, (i+1)*BAR_THICKNESS -1);		
				screen.setColor(colours[singleColour]);
				screen.fillRect(300, i*BAR_THICKNESS, 40, BAR_THICKNESS);
				screen.setColor(Color.black);
				screen.drawRect(300, i*BAR_THICKNESS, 40, BAR_THICKNESS);
				screen.drawString(String.valueOf(singleValue), 310, (i+1)*BAR_THICKNESS -1);
			}
		}
	}
	
	
	/* Gets the maximum value from the array */
	private long getMax(AbstractArrayDatum array){
		long max = getValue(array, 0);
		for (int i = 1; i < array.getNumberOfElements(); i++) {
			long iVal = getValue(array, i);
			if ( iVal > max) max = iVal;
		}
		return max;
	}
	
	private long getValue(AbstractArrayDatum array, int i){
		return  getValue(array.getElement(i));
	}
	
	private long getValue(Datum datum){
		return ((AbstractIntDatum)datum).getValue();
	}

public String toString() { return "ArrayBarDisplay"; }	

}

