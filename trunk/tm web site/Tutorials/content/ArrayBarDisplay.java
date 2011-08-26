/*#H*/package Display;

import java.awt.Color;
import java.awt.Graphics2D;

import tm.clc.datum.AbstractArrayDatum;
import tm.clc.datum.AbstractIntDatum;
import tm.clc.datum.AbstractPointerDatum;
import tm.displayEngine.DisplayAdapter;
import tm.interfaces.Datum;
import tm.interfaces.DisplayManagerInterface;

public class ArrayBarDisplay extends DisplayAdapter {
	// Aging constants
	private static final int countDiff1 = 2;
	private static final int countDiff2 = 4;
	private static final int BAR_THICKNESS = 10;
	
	private int counter;
	private long [] oldData;
	private AbstractArrayDatum lastDatum = null;
	private boolean firstPass;
	
	
	public ArrayBarDisplay(DisplayManagerInterface dm, String configId) {
		super(dm, configId);	// Needed
		explicitMode = true;
	}
	
/**
 * winnow eliminates any datums from the selection that don't fit THIS display's
 * criteria. This allows for the possibility of one selection with different displays
 * each displaying what it knows how to do.
 */
/*#DA*/
	protected void winnow() {
		if (mySelection != null && mySelection.size() > 0) {
			for (int i = mySelection.size() - 1; i >= 0; i-- ) { // take only arrays of ints
				// AbstractXxxxxDatums are language independent
				AbstractArrayDatum aDatum = extractArrayDatum(i);
				if (aDatum == null || aDatum.getNumberOfElements() == 0
						|| !(aDatum.getElement(0) instanceof AbstractIntDatum) )
					mySelection.remove(i);
			}		
		}
	}/*#HA*/
	
	/**
	 * interface routine, designed to be called from within TeachingMachine code
	 * by a relayCall("ArrayBarDisplay", "setColor", from, to, intColor) to the
	 * scriptManager. The idea is to Color the subarray between from and to using
	 * the specified color. Note this doesn't actually do anything at the moment
	 * as the TM has no color model, but it does demonstrate the interface, particularly
	 * that even simply ints get sent by relayCall as datums 
	 * @param from - an AbstractIntDatum representing a valid index within the array
	 * @param to - an AbstractIntDatum representing a valid index within the same array
	 * @param color a valid color - no model yet
	 * Note that this assumes there is only one array selected.
	 */
	
	public void setColor(Datum from, Datum to, Datum color){
		System.out.println("setColor in ArrayBarDisplay");
		int f = (int)(((AbstractIntDatum)from).getValue());
		int t = (int)(((AbstractIntDatum)to).getValue());
		int c = (int)(((AbstractIntDatum)color).getValue());
		AbstractArrayDatum arrayDatum = extractArrayDatum(0);
		if (arrayDatum != null) { // There is a selection 
			for(int i = f; i < t; i++){
				arrayDatum.getElement(i).setProperty("color",c);				
			}
		}
		System.out.println("set color of indices " + f + " to " + t + " to " + c);

	}
/*#DB*/
	/* All Visualization plugins require a drawArea method. This method will be called
	 * whenever the system needs to paint or repaint the visualization (for example,
	 * because its window is brought to the front, or the Teaching Machine has changed
	 * state or because a request to make a snapshot of the visualization has been made).
	 * This code colourizes the array data based on how old it is, not on any calls to 
	 * setColor
	 */
	/
	public void drawArea(Graphics2D screen){
		if (screen == null || mySelection == null || mySelection.size() == 0) return;
		AbstractArrayDatum arrayDatum = extractArrayDatum(0); // Only takes the first array in the selection
		
		if (arrayDatum != null) { // There is a selection 
			int diff;
			long length = arrayDatum.getNumberOfElements();
			double max = (double)getMax(arrayDatum);  // force double arithmetic
			
			if (arrayDatum != lastDatum) { // Changed arrays, start over
				if (oldData == null || oldData.length != length)
					oldData = new long[(int)length];
				counter = countDiff2 + 1; // Start out with everything aged.
				firstPass = true;
			} else {
				counter++;
				firstPass = false;
			}
			for (int i = 0; i < length; i++) {
				long value = getValue(arrayDatum, i);
				if (firstPass){ // Artificially age all datums
					// Note you can attach any property you like to a datum as a name-value pair
					arrayDatum.getElement(i).setProperty("counter",new Integer(0));
					diff = counter;
				} else {
					if (value != oldData[i]) {
						arrayDatum.getElement(i).setProperty("counter",new Integer(counter));
						diff = 0;
					}
					else {
						diff = counter -
							( (Integer)arrayDatum.getElement(i).getProperty("counter")).intValue();
					}
				}
				
				/* This is the actual, simple-minded,  drawing code */
				int width = (int)(200*(value/max));
				int x;
				if (value < 0) {
					width = -width;
					x = 200 - width;
				} else x = 200;
				screen.setColor(
						(diff > countDiff2) ? Color.green : ( (diff > countDiff1) ? Color.yellow : Color.red) );
				screen.fillRect(x, i*BAR_THICKNESS, width, BAR_THICKNESS);
				screen.setColor(Color.black);
				screen.drawRect(x, i*BAR_THICKNESS, width, BAR_THICKNESS);
				oldData[i] = value;
			}
			lastDatum = arrayDatum;
		}
	}/*#HB*/
	
	/*#DC*/
	/* Language independent method to extract an array from the selection. Returns null if the
	 * datum selection(i) is not either an AbstractArrayDatum or an AbstractPointer to one.
	 */
	
	private AbstractArrayDatum extractArrayDatum(int i){ 
		Datum datum = mySelection.get(i);
		if (datum instanceof AbstractPointerDatum) // In java, given int [] a, a is a pointer
			datum = ((AbstractPointerDatum)datum).deref();
		if (datum instanceof AbstractArrayDatum)
			return (AbstractArrayDatum)datum;
		return null;
	}/*#HC*/
	
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
		return  ( (AbstractIntDatum)(array.getElement(i)) ).getValue();
	}

public String toString() { return "ArrayBarDisplay";}	

}
