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

import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

import tm.utilities.Assert;


/* A link is the on screen representation of a pointer connection between two datums.
 Jan 2007: Eliminated scrolling bug which occurred when we switched to swing
 			Draws were ofsetting x and y by scroll amounts which swing handles
 			automatically.
*/

public class Link extends Object{
    private Attachment start;
    private Attachment end;
    private Vector iPoints = null;
    private boolean isNull = false;  // Only used if end is null
    
/* Interior points on links have to be elastic. Links are hooked to attachments
    whose positions move as the LinkedDatumDisplays they are associated with move.
    Thus interior points represent an offset from the start point of a link plus
    a percentage of the distance (in both x and y) between the end and start points.
*/
    private class InteriorPoint{
        public int x; // x offset
        public int y;   // y offset
        public double xScale;
        public double yScale;
        public InteriorPoint(int x, double xScale, int y, double yScale){
            this.x = x; this.y = y;
            this.xScale = xScale; this.yScale = yScale;
        }
    }
    
    
    public Link(Attachment s, Attachment e){
        Assert.check(s != null);
        start = s;
        end = e;
        start.makeConnect(this, true);
        if (end != null){
            end.makeConnect(this, false);
        }
    }

/*  connect the end of my link somewhere else
*/
    public void update(LinkedDatumDisplay endLDD){
        if (end != null)
            end.breakConnect();
        if (endLDD == null)
            end = null;
        else {
            if(endLDD.getExpander() != null && endLDD.getExpander().getExpanded())
                end = endLDD.getAttachment(
                    endLDD.getIndirection() > start.getOwner().getIndirection() ?
                        LinkedDatumDisplay.LT :
                        LinkedDatumDisplay.RT);
            else
                end = endLDD.getAttachment(
                    endLDD.getIndirection() > start.getOwner().getIndirection() ?
                        LinkedDatumDisplay.LU :
                        LinkedDatumDisplay.RU);
                
            end.makeConnect(this, false);
            if(endLDD.getIndirection() == start.getOwner().getIndirection()) { // special case
                if (iPoints == null) iPoints = new Vector<InteriorPoint>(2);
                iPoints.setSize(2); // we want exactly 2 interior points
                iPoints.setElementAt(new InteriorPoint(14, 0., 0, 0.), 0); // 5 pixels right of start stub
                iPoints.setElementAt(new InteriorPoint(7, 0., 0, 1.), 1); // 5 pixels to right of end stup
            }
            else if (iPoints != null)
                iPoints.removeAllElements();
        }
    }
    
    public void setNull(boolean isN){
        Assert.check(end == null);
        isNull = isN;
    }
    
    
    public Attachment getStart(){
        return start;
    }
    
    public Attachment getEnd(){
        return end;
    }
    
/*    public int getNumIPoints(){
        return iPoints.size();
    }
    
    public InteriorPoint getIPoint(int i){
        Assert.check(iPoints != null && i >= 0 && i < iPoints.size());
        return new Point((Point)iPoints.elementAt(i));
    }
    
    public void addIPoint(Point point){
        if (iPoints == null)
            iPoints = new Vector(2,2);
        iPoints.addElement(point);
    }
    
    public void deleteIPoint(int i){
        Assert.check(iPoints != null && i >= 0 && i < iPoints.size());
        iPoints.removeElementAt(i);
    }
    
    public void moveIPoint(int i, int x, int y){
        Assert.check(iPoints != null && i >= 0 && i < iPoints.size());
        Point point = (Point)iPoints.elementAt(i);
        point.x = x;
        point.y = y;
    }
    */
    
    public void draw(Graphics screen){
    	// Draw the beginning stub
        Point from = start.getPoint();
        Point to = start.getStub();
        screen.drawLine(from.x, from.y, to.x, to.y);
        if (end == null)
            if (isNull)
                drawGround(screen, to.x, to.y);
            else
                drawInvalid(screen, to.x, to.y);
        else {
            from = to;
            to = end.getStub();
//            System.out.println("Drawing " + toString() + " from (" + from.x + ", " +
//            		from.y + ") to (" + to.x + ", " + to.y + ")");
            if (iPoints != null) { // interior points are relative to above end points
                int insideFromX = from.x;
                int insideFromY = from.y;
                for (int i = 0; i < iPoints.size(); i++){
                    InteriorPoint ip = (InteriorPoint)iPoints.elementAt(i);
                    int insideToX = from.x + ip.x + (int)(ip.xScale*(to.x - from.x) + 0.5);
                    int insideToY = from.y + ip.y + (int)(ip.yScale * (to.y - from.y) + 0.5);
                    screen.drawLine(insideFromX, insideFromY, insideToX, insideToY);
                    insideFromX = insideToX;
                    insideFromY = insideToY;
                }
                from.x = insideFromX;
                from.y = insideFromY;
            }
            screen.drawLine(from.x, from.y, to.x, to.y);
            from = to;
            to = end.getPoint();
            screen.drawLine(from.x, from.y, to.x, to.y);
            if (from.x > to.x)
                drawLeftArrowhead(screen, to.x, to.y);
            else
                drawRightArrowhead(screen, to.x, to.y);
        }
    }
    
    private void drawGround(Graphics screen, int x, int y){
        screen.drawLine(x, y, x, y+4);
        screen.drawLine(x-3, y+4, x+3, y+4);
        screen.drawLine(x-2, y+6, x+2, y+6);
        screen.drawLine(x-1, y+8, x+1, y+8);
        
    }
    private void drawInvalid(Graphics screen, int x, int y){
        screen.drawLine(x-3, y-3, x+3, y+3);
        screen.drawLine(x-3, y+3, x+3, y-3);
    }
    
    private void drawRightArrowhead(Graphics screen, int x, int y){
        screen.drawLine(x, y, x-4, y-2);
        screen.drawLine(x, y, x-4, y+2);
    }
 
    private void drawLeftArrowhead(Graphics screen, int x, int y){
        screen.drawLine(x, y, x+4, y-2);
        screen.drawLine(x, y, x+4, y+2);
    }
    
    public String toString(){
    	return "link from " +  start.toString() + 
    	((end == null)? "" : " to " + end.toString());
    }
}
