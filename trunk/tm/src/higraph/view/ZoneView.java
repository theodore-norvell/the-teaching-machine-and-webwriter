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

package higraph.view;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.Iterator;

import tm.backtrack.*;
import tm.utilities.Assert;

import higraph.model.interfaces.*;

/**
 * ZoneView represents mouse sensitive areas that have no counterparts in the
 * original model. They are used to permit mouse detection in areas rather
 * than of model components. For example, the region between two child nodes
 * could be a ZoneView object that responds to mouseovers by showing a
 * cursor between the children showing a node could be dropped there.
 * 
 * Such specific requirements should be realized by specializing the class.
 * In the example above , the drawArea method would have to be overloaded
 * to show the cursor.
 *
 * @param NP The Node Payload type
 * @param EP The Edge Payload type
 * @param HG The Higraph type 
 * @param WG The Whole Graph type
 * @param SG The Subgraph type
 * @param N  The Node type
 * @param E  The Edge type
 * 
 * @author mpbl
*/

public abstract class ZoneView 	
    < NP extends Payload<NP>,
      EP extends Payload<EP>,
      HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
      WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
      SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
      N extends Node<NP,EP,HG,WG,SG,N,E>, 
      E extends Edge<NP,EP,HG,WG,SG,N,E>
    >
extends ComponentView<NP,EP,HG,WG,SG,N,E>
{
	/** Standard Reference point positions for ZoneViews wrt to Shape of their associatedComponent.
	 */
	public static final int CENTER = 0;        // (CenterX, CenterY)
	public static final int EAST = 1;          // (MaxX, CenterY)
	public static final int NORTHEAST = 2;     // (MaxX, MinY)
	public static final int NORTH = 3;         // (CenterX, MinY)
	public static final int NORTHWEST = 4;     // (MinX, MinY)
	public static final int WEST = 5;          // (MinX, CenterY)
	public static final int SOUTHWEST = 6;     // (MinX, MaxY)
	public static final int SOUTH = 7;         // (CenterX, MaxY)
	public static final int SOUTHEAST = 8;     // (MaxX, MaxY)
	public static final int POSITION_MIN = CENTER;     // Enumerated position limits
	public static final int POSITION_MAX = SOUTHEAST;
	
	// Positions for zones on edges and other linear shapes.
	//public static final int CENTER = 0;       
    public static final int PORT = 1;          
    public static final int PORTSTERN = 2;    
    public static final int STERN = 3;        
    public static final int STARBOARDSTERN = 4;    
    public static final int STARBOARD = 5;        
    public static final int STARBOARDBOW = 6;    
    public static final int BOW = 7;     
    public static final int PORTBOW = 8;
	
    // Invariant: The values of shapeVar and nextShapeVar are local to this object in the
    // sense that they are not shared with other objects.
    // Thus we always clone when setting or getting these.
    // The shapeVar and nextShapeVar may however have values that are the same object.
    protected final BTVar<RectangularShape> shapeVar;
    protected final BTVar<RectangularShape> nextShapeVar;
    // ZoneViews are always associated with a componentView
    protected final BTVar<ComponentView<NP,EP,HG,WG,SG,N,E>> associatedComponentVar;
    /** The reference point position (see above) which is combined with a nudgePoint
     *  (see below) to specify the ZoneView's position wrt to its associated ComponentView.
     * Layout of ZoneView wrt to that position varies and is controlled by the component.
     * For example a ZoneView placed at the East position might be layed out centered
     * vertically, but with its left edge, on the specified point
     */
    protected final BTVar<Integer> placementVar;
    
    /**
     * The nudgePoint is used as an offset from the standard reference point to produce
     * the actual reference point for the ZoneView
     */
    protected final BTVar<Point2D.Double> nudgeVar;

	
	protected ZoneView(ComponentView<NP,EP,HG,WG,SG,N,E> view, BTTimeManager timeMan) {
		super(view.getHigraphView(), timeMan);
		shapeVar = new BTVar<RectangularShape>(timeMan);  // there is no existing shape
		nextShapeVar = new BTVar<RectangularShape>(timeMan, view.getHigraphView().getDefaultZoneShape()) ;
		associatedComponentVar = new BTVar<ComponentView<NP,EP,HG,WG,SG,N,E>>(timeMan, view);
		placementVar = new BTVar<Integer>(timeMan);
		nudgeVar = new BTVar<Point2D.Double>(timeMan, new Point2D.Double()) ;
	}

	public ComponentView <NP,EP,HG,WG,SG,N,E> getAssociatedComponent() {
	    return associatedComponentVar.get() ;
	}
	
	public int getPlacement(){
		return placementVar.get();
	}

	
	public void setPlacement(int p){
		placementVar.set(new Integer(p));
		getAssociatedComponent().moveZone(this);
	}

	public Point2D.Double getNudge(){
		return nudgeVar.get();
	}

	
	public void setNudge(double dx, double dy){
		nudgeVar.set(new Point2D.Double(dx, dy));
		getAssociatedComponent().moveZone(this);
	}

	
	@Override
	public Shape getShape() {
		return shapeVar.get() == null ? null : (Shape)shapeVar.get().clone();
	}	


	@Override
	public void startTransition() {
		//TODO
	}
	
	@Override
	public void advanceTransition(double degree) {
		//TODO
	}
	
	@Override
	public void finishTransition() {
		RectangularShape shape = nextShapeVar.get() ;
		Assert.check(shape != null, "trying to update to a null nextShape");
		shapeVar.set(shape); // Used to clone.  Why?
	}

	/**
	 * Default is true, override for componentViews for which it is false
	 * 
	 * @return true if labels may be added to this component, false otherwise
	 */
	@Override
	public boolean mayAddLabels(){
		return false;
	}
	
	
	/*****************************************
	 *  implementation of Layable interface
	 *  */
	public boolean isPinned(){
		return false;
	}
	
	public RectangularShape getNextShape() {
		return (RectangularShape) nextShapeVar.get().clone();
	}
	
	/** 
	 * @param shape must be a RectangularShape
	 */
	@Override 
	public void setNextShape(Shape shape) {
	    Assert.check( shape instanceof RectangularShape ) ;
	    RectangularShape newShape = (RectangularShape) ((RectangularShape) shape).clone() ;
		nextShapeVar.set( newShape ) ;
	}


	public void translateNext(double dx, double dy) {
	    RectangularShape newShape = getNextShape();
		Rectangle2D r = newShape.getFrame(); 
		newShape.setFrame(r.getMinX() + dx, r.getMinY() + dy, r.getWidth(), r.getHeight()) ;
		nextShapeVar.set(newShape) ;        
        super.translateNext(dx, dy);
	}
	
/*	public void moveYourself(){
		ComponentView<NP,EP,HG,WG,SG,N,E> cv = associatedComponentVar.get();
		Rectangle2D cr = cv.getNextShapeExtent();
		Rectangle2D zr = getNextBaseExtent();
		double dx = cr.getCenterX() - zr.getCenterX();
		double dy = cr.getCenterY() - zr.getCenterY();
		switch(placementVar.get()){
		case CENTER:
			break;
		case EAST:
			dx = cr.getMaxX() - zr.getMinX();
			break;
		case NORTHEAST:
			dx = cr.getMaxX() - zr.getMinX();
			dy = cr.getMinY() - zr.getMaxY();
			break;
		case NORTH:
			dy = cr.getMinY() - zr.getMaxY();
			break;
		case NORTHWEST:
			dx = cr.getMinX() - zr.getMaxX();
			dy = cr.getMinY() - zr.getMaxY();
			break;
		case WEST:
			dx = cr.getMinX() - zr.getMaxX();
			break;
		case SOUTHWEST:
			dx = cr.getMinX() - zr.getMaxX();
			dy = cr.getMaxY() - zr.getMinY();
			break;
		case SOUTH:
			dy = cr.getMaxY() - zr.getMinY();
			break;
		case SOUTHEAST:
			dx = cr.getMaxX() - zr.getMinX();
			dy = cr.getMaxY() - zr.getMinY();
			break;
		}
		Point2D.Double p = nudgeVar.get();
		translateNext(dx + p.x, dy + p.y);
	}*/
	

	protected ZoneView<NP,EP,HG,WG,SG,N,E> getThis(){return this;}
	

    public String toString(){
    	return "zoneView";
    }
}
