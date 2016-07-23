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

package tm.portableDisplays;

import telford.common.Graphics;
import telford.common.Point;
import tm.displayEngine.Attachment1;
import tm.displayEngine.D3Iterator1;
import tm.displayEngine.Link1;
import tm.displayEngine.LinkedD3Iterator;
import tm.displayEngine.LinkedDisplay;
import tm.interfaces.DataDisplayView;
import tm.interfaces.Datum;
import tm.interfaces.PointerInterface;
import tm.utilities.Assert;


/**
* Class to represent the display information for a single datum
* to be shown on a linked display.
* 
* @author Michael Bruce-Lockhart
* @since April 12, 2001
* @see LinkedDisplay
* @see LinkedD3Iterator
* @see DatumDisplay
*/



public class LinkedDatumDisplay extends DatumDisplay {

//Over-rides of sizes
protected static int VALUE_W = 70;  // width & position
protected static int NAME_W = 55;  // memory display boxes
protected static int STRING_OFFSET = 5; // String offset from box edge
static int MY_WIDTH = VALUE_W + NAME_W;
static int BASE_HEIGHT = 14;   // Used to set scroll inc as well

/*  static value providing functions. Most of them provide
x positions for the internal components which are likely to vary from sub-class
to sub-class. These are over-rides of the parent class. 
*/
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



// Fixed attachment points
public static final int RCI = 0;    // Right Center, internal
public static final int RCE = RCI + 1;    // Right Center, external
public static final int RT = RCE + 1;     // Right top - for compound connect
public static final int RU = RT + 1;    // Right upper
public static final int RL = RU + 1;    // Right lower
public static final int LC = RL + 1;    // Left center
public static final int LU = LC + 1;    // Left upper
public static final int LL = LU + 1;    // Left lower
public static final int LT = LL + 1;     // Left top corner, for compounds

protected static int POINTER_STUB = 10;
public static final int INSET_RIGHT = 10;
public static final int GAP_LEFT = 3;



// variables for linked view support
//private boolean linked = false; //true if already linked into display
private int step = LAST_STEP;   // step in animation morph
private int indirectionLevel = 0;   // how deep is my indirection

private Point oldLocation;      // to support morphing in linked view

private Attachment1[] attachments = new Attachment1[LT+1];
private Link1 myLink=null;       // Only not null if represents a pointer
private int stain;              // left by iterators when visiting
private LinkedDatumDisplay backPointer = null;   // the datumDisplay which LAST dereferenced this one

//=================================================================
//Constructor
//=================================================================

/**
* Constructs not only datum but every datum that is part of datums subtree. Thus
*     this is a recursive constructor
* 
* @param datum The datum with which this LinkedDatumDisplay object is to be uniquely
* associated. The two together constitute a D3 pair.
* @param top The particular DataDisplayView, ie, in this case, some LinkedDisplay
* object.
* @param level The indirection level of the associated Datum. That is, how
* deeply it is nested in the pointer tree.
*/


public LinkedDatumDisplay(Datum datum, DataDisplayView top, int level){
 super(datum, top, true);
 indirectionLevel = level;
 extent.width = MY_WIDTH;  // over-ride width in super
 oldLocation = new Point(0,0);
 for (int i = 0; i <=LT; i++)
     attachments[i] = new Attachment1(this, i);
 if (datum instanceof PointerInterface) {
     Datum deref = ((PointerInterface)datum).deref();
     if (deref != null) {
         Datum parent;   // Make sure we've got top level datum
         Datum kid = deref;
         while( (parent = kid.getParent()) != null)
             kid = parent;
         LinkedDatumDisplay target = LinkedDatumDisplay.getLDD(kid, top);
         if (target == null) { // recursive construction of top level targets
             target = new LinkedDatumDisplay(kid, top, indirectionLevel + 1); 
         }
         if (kid != deref) { // Pointing inside of new LDD
             int indLevel = target.indirectionLevel;
             target = LinkedDatumDisplay.getLDD(deref, top); // Target for link is still internal D3
             if (target == null) {
                 target = new LinkedDatumDisplay(deref, top, indLevel); 
             }
             myLink = new Link1(attachments[RCI],
                 target.getAttachment(
                     target.indirectionLevel > indirectionLevel ? 
                     LC : RU));
         }
         else
         
         myLink = new Link1(attachments[RCI],
             target.getAttachment(
                 target.indirectionLevel > indirectionLevel ? 
                 LT : RT));
     }
     else myLink = new Link1(attachments[RCI], null);
 }
// Recursively construct DatumDisplay objects for any kids
	else for (int i = 0; i < myDatum.getNumChildren(); i++) {
     LinkedDatumDisplay kid = getLDD(myDatum.getChildAt(i), top);
     if (kid == null) {
         kid = new LinkedDatumDisplay(myDatum.getChildAt(i), top, indirectionLevel);  // recursive
     }
	}
}

/**
* Get the LinkedDatumDisplay object associated with the datum. 
*/
public static LinkedDatumDisplay getLDD(Datum datum, DataDisplayView view){
 return (LinkedDatumDisplay)(DatumDisplay.getAssociated(datum, view));
}

/* Resize is responsible for locating all subcomponents properly. Over-rides default
code, components are now laid out from left to right starting with the expander
gap. After that comes the nameBox followed by the valueBox. NameBox is fixed width so
valueBox shrinks as gap increases. Handles placement. Draw handles the appearance.

public void resize(int nWidth, int vGap, int vWidth, int aWidth){
 super.resize(nWidth, vGap, vWidth, aWidth);
 // Now just move components around a bit
 nameBox.move(vGap, 0); //move (vGap,0)
 if (myExpander != null) {   // Compound Datum
     myExpander.move(vGap - Expander.EXPAND_OFFSET + Expander.EXPAND_X, Expander.EXPAND_Y);
     }
 }
*/   


public Point getInterimLocation() { 
 return new Point( morph((int)oldLocation.getX(), extent.x),
                   morph((int)oldLocation.getY(), extent.y) );
}
 
//public boolean isLinked(){return linked;}

//   public void setLinked(boolean l){linked = l;}

public LinkedDatumDisplay getBackPointer(){
 return backPointer;
}

public void setBackPointer(LinkedDatumDisplay from){
 backPointer = from;
}

public int getIndirection(){return indirectionLevel;}

public void setIndirection(int level){
 indirectionLevel = level;
}


public void setStep(int s){
 if (s<0) step = 0;
 else if (s > LAST_STEP) s = LAST_STEP;
 else s = step;
}

public Attachment1 getAttachment(int i){
 Assert.check( i>=RCI && i <= LT);
 return attachments[i];
}

// Get co-ordinates of attachment point
public Point getAPoint(int i){
 Point point = new Point(extent.x,extent.y);
 switch(i){
     case RCI:   // right centre, internal
         point.setX(point.getX() + extent.width - INSET_RIGHT);
         point.setY(point.getY() + extent.height/2);
         break;
     
     case RCE:   // Right centre, external
    	 point.setX(point.getX() + extent.width);
    	 point.setY(point.getY() + extent.height/2);
         break;
         
     case RT:   // Right top, 
    	 point.setX(point.getX() + extent.width);
         break;
         
     case RU:    // Right upper   
    	 point.setX(point.getX() + extent.width);
    	 point.setY(point.getY() + extent.height/4);
         break;
     case RL:    // Right lower
    	 point.setX(point.getX() + extent.width);
    	 point.setY(point.getY() + (3*extent.height)/4);
         break;
     case LC:    // Left center
    	 point.setX(point.getX() + nameBox.getStringLeft() - GAP_LEFT);
    	 point.setY(point.getY() + extent.height/2);
         break;
     case LU:    // left upper
    	 point.setX(point.getX() + nameBox.getStringLeft() - GAP_LEFT);
    	 point.setY(point.getY() + extent.height/4);
         break;
     case LL:    // left lower
    	 point.setX(point.getX() + nameBox.getStringLeft() - GAP_LEFT);
    	 point.setY(point.getY() + (3*extent.height)/4);
         break;
     case LT:    // Left top
    	 point.setX(point.getX() + NAME_W);
         break;
 }
 
 return point;
}

public Point getAStub(int i){
 Point point = getAPoint(i);
 if (i == RCI)
	 point.setX(point.getX() + POINTER_STUB + INSET_RIGHT);
 else if (i < LC)  // Right side
	 point.setX(point.getX() + POINTER_STUB);
 else
	 point.setX(point.getX() - POINTER_STUB);
 return point;
}

/*    void setLink(Link link){
 myLink = link;
}
*/   
public Link1 getLink(){
 return myLink;
}

public void setStain(int s){
 stain = s;
}
public int getStain(){
 return stain;
}

protected void drawScalar(Graphics screen){

 Point position = new Point(extent.x, extent.y);

	screen.setFont(nameFont);
	nameBox.draw(screen, position, -1);

//No address box in linked view	
	//Color.yellow #FFFF00; Color.lightgrey #d3d3d3; Color.pink
	
	int color = (myDatum.getHighlight() == Datum.HIGHLIGHTED) ? (0XFFFF00)
					: (isGrayedOut() ? 0Xd3d3d3 : -1);
	screen.setFont(valueFont);
	
	if (!(myDatum instanceof PointerInterface))
	    valueBox.setString(myDatum.getValueString());
	else
	    valueBox.setString("");
	valueBox.draw(screen, position, color);

} // end draw method

public void drawLinks(Graphics screen){
 D3Iterator1 iterator = getIterator();    // get one of my iterators
 while (!iterator.atEnd()){
     Link1 link = ((LinkedDatumDisplay)iterator.getNode()).getLink();
     if (link != null)
         link.draw(screen);
     iterator.increment();
 }
}


private int morph(int old, int now){ //added March, 2001 to support linked view
 return (old + (int)(  (double)(now-old) * ( ((double)step) /LAST_STEP ) + 0.5 )  );
}


public String toString(){
 return "LinkedDatumDisplay for " + myDatum.toString();
}


}