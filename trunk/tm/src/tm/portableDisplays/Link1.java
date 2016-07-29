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

import java.util.Vector;

import telford.common.Graphics;
import telford.common.Line;
import telford.common.Point;


/* A link is the on screen representation of a pointer connection between two datums.
 Jan 2007: Eliminated scrolling bug which occurred when we switched to swing
 			Draws were ofsetting x and y by scroll amounts which swing handles
 			automatically.
*/

public class Link1 extends Object{
    private Attachment1 start;
    private Attachment1 end;
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
    
    
    public Link1(Attachment1 s, Attachment1 e){
        // Assert.check(s != null); // TODO Reinstate
        start = s;
        end = e;
        start.makeConnect(this, true);
        if (end != null){
            end.makeConnect(this, false);
        }
    }

/*  connect the end of my link somewhere else
*/
    public void update(tm.portableDisplays.LinkedDatumDisplay endLDD){
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
        // Assert.check(end == null); // TODO Reinstate
        isNull = isN;
    }
    
    
    public Attachment1 getStart(){
        return start;
    }
    
    public Attachment1 getEnd(){
        return end;
    }
    
    public void draw(Graphics screen){
    	// Draw the beginning stub
        Point from = start.getPoint();
        Point to = start.getStub();
        Line line = new Line(from, to);
        screen.draw(line);
        if (end == null)
            if (isNull)
                drawGround(screen, (int)to.getX(), (int)to.getY());
            else
                drawInvalid(screen, (int)to.getX(), (int)to.getY());
        else {
            from = to;
            to = end.getStub();
//            System.out.println("Drawing " + toString() + " from (" + from.x + ", " +
//            		from.y + ") to (" + to.x + ", " + to.y + ")");
            if (iPoints != null) { // interior points are relative to above end points
                int insideFromX = (int)from.getX();
                int insideFromY = (int)from.getY();
                for (int i = 0; i < iPoints.size(); i++){
                    InteriorPoint ip = (InteriorPoint)iPoints.elementAt(i);
                    int insideToX = (int)from.getX() + ip.x + (int)(ip.xScale*(to.getX() - from.getX()) + 0.5);
                    int insideToY = (int)from.getY() + ip.y + (int)(ip.yScale * (to.getY() - from.getY()) + 0.5);
                    line = new Line(new Point(insideFromX, insideFromY), new Point(insideToX, insideToY));
                    screen.draw(line);
                    insideFromX = insideToX;
                    insideFromY = insideToY;
                }
                from.setX(insideFromX);
                from.setY(insideFromY);
            }
            line = new Line(from, to);
            screen.draw(line);
            from = to;
            to = end.getPoint();
            line = new Line(from, to);
            screen.draw(line);
            if (from.getX() > to.getX())
                drawLeftArrowhead(screen, (int)to.getX(), (int)to.getY());
            else
                drawRightArrowhead(screen, (int)to.getX(), (int)to.getY());
        }
    }
    
    private void drawGround(Graphics screen, int x, int y){
    	Line line = new Line(new Point(x, y), new Point(x, y+4));
        screen.draw(line);
        
        line = new Line(new Point(x, y), new Point(x+3, y+4));
        screen.draw(line);
        
        line = new Line(new Point(x-2, y+6), new Point(x+2, y+6));
        screen.draw(line);
        
        line = new Line(new Point(x-1, y+8), new Point(x+1, y+8));
        screen.draw(line);
        
    }
    private void drawInvalid(Graphics screen, int x, int y){
    	Line line = new Line(new Point(x-3, y-3), new Point(x+3, y+3));
        screen.draw(line);
        
        line = new Line(new Point(x-3, y+3), new Point(x+3, y-3));
        screen.draw(line);
    }
    
    private void drawRightArrowhead(Graphics screen, int x, int y){
    	Line line = new Line(new Point(x, y), new Point(x+4, y-2));
        screen.draw(line);
        
        line = new Line(new Point(x, y), new Point(x-4, y+2));
        screen.draw(line);
    }
 
    private void drawLeftArrowhead(Graphics screen, int x, int y){
    	Line line = new Line(new Point(x, y), new Point(x+4, y-2));
        screen.draw(line);
        
        line = new Line(new Point(x, y), new Point(x+4, y+2));
        screen.draw(line);
    }
    
    public String toString(){
    	return "link from " +  start.toString() + 
    	((end == null)? "" : " to " + end.toString());
    }
}
