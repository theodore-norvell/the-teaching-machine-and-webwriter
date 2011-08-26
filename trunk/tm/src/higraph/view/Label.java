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

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import tm.backtrack.BTTimeManager;
import tm.backtrack.BTVar;
import tm.utilities.Assert;

import higraph.model.interfaces.*;

/**
 * {@link Label} represents a label that can be added to a component. Although
 * technically a ZoneView, labels currently (see {@link ComponentView}) cannot
 * be selected by a mouse, nor do they add to the extent of the ComponentView
 * to which they are attached. 
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
public class Label
    < NP extends Payload<NP>,
      EP extends Payload<EP>,
      HG extends Higraph<NP,EP,HG,WG,SG,N,E>,
      WG extends WholeGraph<NP,EP,HG,WG,SG,N,E>,
      SG extends Subgraph<NP,EP,HG,WG,SG,N,E>,
      N extends Node<NP,EP,HG,WG,SG,N,E>, 
      E extends Edge<NP,EP,HG,WG,SG,N,E>
    >
extends ZoneView<NP, EP, HG, WG, SG, N, E> {
    
    public static final String TYPE_STRING = "Label" ;
    
	protected final BTVar<String> labelVar;
	
	/* Note this is a second layer of visibilty control. However it is not redundant as the
	 * visibilityVar in ComponentView is often used to control the visibility of whole components.
	 * If only the original visibility variable were used, a user might turn off a (possibly
	 * automatic) label to prevent it from showing only to find a layoutManager has later turned
	 * on the visibility of its component which would automatically turn the label back on. Two
	 * levels of control prevent this.
	 */
	protected final BTVar<Boolean> showVar;

	
	protected Label(String id, int position, ComponentView <NP,EP,HG,WG,SG,N,E> cv, BTTimeManager timeMan ){
		super(cv, timeMan);
		labelVar = new BTVar<String>(timeMan, "");
		showVar = new BTVar<Boolean>(timeMan, true);
		colorVar.set(cv.getHigraphView().getDefaultLabelColor());
		fillColorVar.set(cv.getHigraphView().getDefaultLabelFillColor());
		placementVar.set(position);
		idVar.set(id);
	}
	
	public void setTheLabel(String label){
		labelVar.set(label);
		setNextShape(getLabelMetrics());
		getAssociatedComponent().moveLabel(this);
	}
	
	public void setShow(boolean show){
		showVar.set(show);
		if (show) {
			setNextShape(getLabelMetrics());
			getAssociatedComponent().moveLabel(this);
		}
	}
	
	@Override
	public void setPlacement(int p){
		placementVar.set(new Integer(p));
		getAssociatedComponent().moveLabel(this);
	}

	@Override
	public void setNudge(double dx, double dy){
		nudgeVar.set(new Point2D.Double(dx, dy));
		getAssociatedComponent().moveLabel(this);
	}

	

	public Rectangle2D getLabelMetrics(){
        // Note. There is an assumption throughout this routine that the font of the screen
        // object is the same as the font for the screen object used during drawing in
        // routine draw. See comment there.
		Graphics2D screen = (Graphics2D)getHigraphView().getDisplay().getGraphics();
		// Should we worry about a null value for the label?
		return screen.getFontMetrics().getStringBounds(getTheLabel(), screen);		
	}
	
	public String getTheLabel(){
		return labelVar.get();		
	}
	
	/**
	 * Default is true, override for componentViews for which it is false
	 * 
	 * @return true if zones may be added to this component, false otherwise
	 */
	@Override
	public boolean mayAddZones(){
		return false;
	}

	
	public void draw(Graphics2D screen){
	    // Note. There is an assumption throughout this routine that the font of the screen
	    // object is the same as the font for the screen object used during layout in
	    // routine getLabelMetrics. See comment there.
		if(!getVisibility() || !showVar.get()) return;
		Shape shape = shapeVar.get();
		if (shape == null) return;
		Rectangle2D bounds  = shape.getBounds2D();
		if (bounds != null){
			String string = getTheLabel();
			if (string != null && string.length() > 0 ) {
				Color currentColor = screen.getColor();
				if (fillColorVar.get() != null){
					screen.setColor(fillColorVar.get());
					screen.fillRect((int)(bounds.getMinX()+.5), (int)(bounds.getMinY()+.5),
						(int)(bounds.getWidth()+.5), (int)(bounds.getHeight()+.5));
				}
				screen.setColor(colorVar.get());
				//System.out.println("Drawing " + string + " for label " + getId() + " in color " + screen.getColor() + " at (" + bounds.getMinX() + ", " + bounds.getMinY() + ")");
                
				// On my Mac anyway, the vertical metrics seem to depend only on the Graphics object
                // and the font. The string seems to be ignored. Thus "qg" and "AB" produce the
                // same height of box (14px) and the same descent (3px). Nevertheless,
				// this seems to be the "correct" way calculate the descent.
				// Note, I'm assuming that fm.getLineMetrics(...) is consistent with fm.getStringBounds(...).
				FontMetrics fm = screen.getFontMetrics() ;
				double descent = fm.getLineMetrics(string, screen).getDescent() ;
				screen.drawString(string, (float)bounds.getMinX(), (float)(bounds.getMaxY()-descent) );
				screen.setColor(currentColor);
			}
		}
	}
	

	public String toString(){
		return "Label: " + getTheLabel() + " for " + associatedComponentVar.get();
	}

	@Override
	public String getViewType() {
		return "Label";
	}
}
