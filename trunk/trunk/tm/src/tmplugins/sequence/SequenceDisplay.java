package tmplugins.sequence;

import java.awt.Color;
import java.awt.Graphics2D;

import tm.clc.datum.AbstractArrayDatum;
import tm.clc.datum.AbstractIntDatum;
import tm.displayEngine.DataVisualizerAdapter;
import tm.displayEngine.DisplayManager; 
import tm.interfaces.Datum;

public class SequenceDisplay extends DataVisualizerAdapter {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SequenceDisplay(DisplayManager dm, String configId) {
		super(dm, configId);	// Needed
		setGeneratorMode(MALLEABLE);
	}
	
/**
 * winnow eliminates any datums from the generator that don't fit THIS display's
 * criteria. This allows for the possibility of one generator driving multiple displays
 * each displaying what it knows how to do.
 */

	protected void winnow() {
		if (myGenerator != null && myGenerator.getNumChildren() > 0) {
			for (int i = myGenerator.getNumChildren() - 1; i >= 0; i-- ) { // take only arrays of ints
				// AbstractXxxxxDatums are language independent
				AbstractArrayDatum aDatum = extractArrayDatum(i);
				if (aDatum == null || aDatum.getNumberOfElements() == 0
						|| !(aDatum.getElement(0) instanceof AbstractIntDatum) )
					myGenerator.remove(aDatum);
			}		
		}
	}
	
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
//		System.out.println("setColor in ArrayBarDisplay");
		int f = (int)(((AbstractIntDatum)from).getValue());
		int t = (int)(((AbstractIntDatum)to).getValue());
		int c = (int)(((AbstractIntDatum)color).getValue());
		AbstractArrayDatum arrayDatum = extractArrayDatum(0);
		if (arrayDatum != null) { // There is a selection 
			for(int i = f; i < t; i++){
				arrayDatum.getElement(i).setProperty("color",c);				
			}
		}
//		System.out.println("set color of indices " + f + " to " + t + " to " + c);

	}

	/* All Visualization plugins require a drawArea method. This method will be called
	 * whenever the system needs to paint or repaint the visualization (for example,
	 * because its window is brought to the front, or the Teaching Machine has changed
	 * state or because a request to make a snapshot of the visualization has been made).
	 * This code colourizes the array data based on how old it is, not on any calls to 
	 * setColor
	 */
	
	public void drawArea(Graphics2D screen){
		if (screen == null || myGenerator == null || myGenerator.getNumChildren() == 0) return;
		AbstractArrayDatum arrayDatum = extractArrayDatum(0); // Only takes the first array in the selection
		
		if (arrayDatum != null) { // There is a selection 
			long length = arrayDatum.getNumberOfElements();
			int x = 10;
			int width = 20;
			int height = 20;
			int y = 10;
			screen.setColor(Color.black);
			
			for (int i = 0; i < length; i++) {
				long value = getValue(arrayDatum, i);
				screen.drawRect(x, y, width, height);
				screen.drawString(String.valueOf(value), x+4, y+15);
				x+=width;
				
			}
		}
	}
	
	
	
	private long getValue(AbstractArrayDatum array, int i){
		return  ( (AbstractIntDatum)(array.getElement(i)) ).getValue();
	}

public String toString() { return "SequenceDisplay";}	

}
