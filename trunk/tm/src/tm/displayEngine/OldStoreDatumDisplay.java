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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import tm.interfaces.DataDisplayView;
import tm.interfaces.Datum;


public class OldStoreDatumDisplay extends OldDatumDisplay{
    
/*  static value providing functions. Most of them provide
    x positions for the internal components which are likely to vary from sub-class
    to sub-class. These are over-rides of the parent class. 

    public static int getNameX(int nestLevel){ return 0; }
    public static int getNameW(int nestLevel) { return NAME_W; }
    public static int getValueX(int nestLevel){
            return NAME_W + (nestLevel+1)* Expander.EXPAND_OFFSET; }
    public static int getValueW(int nestLevel){return VALUE_W - (nestLevel)*Expander.EXPAND_OFFSET; }
    public static int getDescenderX(int nestLevel){
            return getExpanderX(nestLevel) + Expander.EXPAND_W/2; }
    public static int getExpanderX(int nestLevel){
            return NAME_W + nestLevel*Expander.EXPAND_OFFSET + Expander.EXPAND_X; }
    public static int getCompoundX(int nestLevel){ return NAME_W; }
    public static int getCompoundW(int nestLevel){ return VALUE_W + Expander.EXPAND_OFFSET; }

 */   

    protected Vector valueBoxes = new Vector();  // Potentially a bunch in BINARY view

// =================================================================
// Constructor
// =================================================================

/* 
*/

    public OldStoreDatumDisplay(Datum datum, DataDisplayView top, boolean expand){
        super(datum, top, expand);
		addressBox = new OldStringBox(Integer.toString(myDatum.getAddress()),false, OldStringBox.LEFT, OldStringBox.TOP);
		addressBox.nudge(STRING_OFFSET,0);

	// Recursively construct DatumDisplay objects for any kids
		for (int i = 0; i < datum.getNumChildren(); i++) {
		    Datum kid = myDatum.getChildAt(i);
		    OldDatumDisplay dd = getAssociated(kid,top);
		    if (dd == null) 
		        dd = new OldStoreDatumDisplay(kid, top, false);
		}
}

    public void resize(int nWidth, int vGap, int vWidth, int aWidth){
        super.resize(nWidth, vGap, vWidth, aWidth);
        if (myDatum.getNumChildren() > 0) return;  // Compound datums handle this by accretion
	
	    if (myDataView.getDatumView() != OldStoreDisplay.LOGICAL) {
            extent.height = myDatum.getNumBytes() * baseHeight;
        }
        valueBox.resize(valueBox.getExtent().width,
            myDataView.getDatumView() == OldStoreDisplay.SCALED ?
            extent.height : baseHeight );
        setValueBoxes();
    }

    protected void drawScalar(Graphics2D screen){
  
  // We need local co-ordinates for accumulating
        Point position = new Point(extent.x, extent.y);
        
        
		screen.setFont(nameFont);
		nameBox.draw(screen, position, null);

		screen.setFont(addressFont);
		addressBox.draw(screen, position, null);

		Color color = (myDatum.getHighlight() == Datum.HIGHLIGHTED) ?
		    Color.yellow : (isGrayedOut() ? Color.lightGray : null);
		if (isSelected()) color = Color.pink;
		screen.setFont(valueFont);
	// Added 2001.12.15: puts a box around area where an expander would go so scalar datums have same width as compound ones
        int width = valueBox.getExtent().x + valueBox.getExtent().width - nameBox.getExtent().width;
        screen.draw(new Rectangle(position.x+nameBox.getExtent().width,
            position.y, width, extent.height));
		
//		setValueBoxes();
		valueBox.draw(screen, position, color);  // primary value box

// In binary view we have to draw all the extra internal byte boxes
	    if (myDataView.getDatumView() == OldStoreDisplay.BINARY) 
		    for (int i = 0, d = baseHeight; i < valueBoxes.size(); i++) {
		        OldStringBox vBox = (OldStringBox)valueBoxes.elementAt(i);
		        vBox.move(valueBox.getExtent().x,d);
		        vBox.resize(valueBox.getExtent().width,valueBox.getExtent().height);
		        vBox.draw(screen, position, color);
		        d += baseHeight;
		    }

    } // end draw method

/*  valueBoxes are complicated by BINARY view since multiple boxes need to be
    created in that case (one for each byte)
    Mar 13, 2001: Since DatumDisplays are no longer recreated every time there is a change.
        The code has been rewritten to allow value boxes to update the value. Major change
        is to add boolean useOld and stay in if non-binary values, allowing strings to be
        reset.
*/
    private void setValueBoxes(){
        boolean binary = (myDataView.getDatumView() == OldStoreDisplay.BINARY);
        boolean useOld = false;     // Using existing value boxes
  // 3/13/2001: useOld added, no longer exit routine if we have the correct no. of boxes already          
        if (binary && valueBoxes.size() == (myDatum.getNumBytes() - 1)) useOld = true;
        if (!binary && valueBoxes.size() == 0) useOld = true;
/* If we get here we've got some building to do
    All of this hair is to support binary view which requires multiple boxes
    I did some changes to support primary valueBox + extras (1 -> 0 in next line)
    but this won't work, needs farther ammendment.
*/
        if (!useOld) valueBoxes.setSize(binary ? myDatum.getNumBytes()-1 : 0);
        if (!binary)
            valueBox.setString(myDatum.getValueString());    // Otherwise we still need an empty box
        else {
            valueBox.setString(binaryByte(myDatum.getByte(0)));
            for (int i = 0; i < valueBoxes.size(); i++) {
                OldStringBox sb = (OldStringBox)valueBoxes.elementAt(i);
                if (sb == null){
                    sb = new OldStringBox(binaryByte(myDatum.getByte(i+1)), true, OldStringBox.LEFT, OldStringBox.MIDDLE);
                    valueBoxes.setElementAt(sb, i);
		            sb.nudge(STRING_OFFSET, 0);
                }
                else sb.setString(binaryByte(myDatum.getByte(i+1)));
            }
        }
            
    }
    
	// Returns hex byte for binary mode
	private static String hexByte(int i) {
		char fullRep[] = {'0','x', '0', '0'};	// full size representation
		String hex = Integer.toHexString(i & 0xff);
	// copy in the hex string, right justified, so extra zeroes on left
		hex.getChars(0,hex.length(),fullRep,4-hex.length());
		return new String(fullRep);
	}

	// Returns hex byte for binary mode
	private static String binaryByte(int i) {
		char fullRep[] = {'0','0', '0', '0','0','0', '0', '0'};	// full size representation
		String binary = Integer.toBinaryString(i & 0xff);
	// copy in the hex string, right justified, so extra zeroes on left
		binary.getChars(0,binary.length(),fullRep,8-binary.length());
		return new String(fullRep);
	}

   
    public String toString(){
        return "DatumDisplayStore for " + myDatum.toString();
    }


}