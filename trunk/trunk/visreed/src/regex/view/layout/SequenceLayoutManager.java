/**
 * SequenceLayoutManager.java
 * 
 * @date: 2011-6-14
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.view.layout;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import regex.model.Direction;
import regex.view.RegexDropZone;
import regex.view.RegexNodeView;
import regex.view.SequenceNodeView;
import tm.utilities.Assert;

/**
 * The SequenceLayoutManager put all its children in a horizontal line.
 * @author Xiaoyu Guo
 */
public class SequenceLayoutManager extends RegexNodeLayoutManager {

    private static final double HSPACE_PIXEL = 15;
    private static final double VSPACE_PIXEL = 0;
    private static final double EMPTY_VSPACE_PIXEL = 10;
    protected SequenceLayoutManager() {
        super();
    }

    /* (non-Javadoc)
     * @see regex.view.layout.RegexNodeLayoutManager#layoutNode(regex.view.RegexNodeView)
     */
    @Override
    public void layoutNode(RegexNodeView nv, double px, double py) {
        if(nv == null){
            return;
        }
        int numChildren = nv.getNumChildren();
        Rectangle2D[] childrenExtents = new Rectangle2D[numChildren];
        
        double maxOffsetTopY = 0.0;     /** entryY - nextY */
        double maxBottomOffsetY = 0.0;  /** nextBottomY - entryY */
        
        /** entryY - nextY */
        double[] offsetTopYs = new double[numChildren];
        /** pixels to move the child downwards */
        double[] moveDownDistances = new double[numChildren];
        
        double currentX = HSPACE_PIXEL;
        
        // TODO lock(view)
        for(int i = 0; i < numChildren; i++){
            RegexNodeView kid = nv.getRegexChild(i);
            if(nv.getCurrentDirection() .equals(Direction.WEST)){
                kid = nv.getRegexChild(numChildren - i - 1);
            }
            
            // layout a child at (currentX, 0)
            layoutNodes(kid, px + currentX, py);
            childrenExtents[i] = kid.getNextShapeExtent();
            
            // handling baseline
            double offsetTopY = 0.0, offsetBottomY = 0.0;
            if(kid instanceof RegexNodeView){
                // possible to use EntryPoint 
                offsetTopY = ((RegexNodeView)kid).getEntryPoint().getY() - childrenExtents[i].getY();
            } else{
                // default NodeView, EntryPoints located at the center of the extent
                offsetTopY = childrenExtents[i].getCenterY() - childrenExtents[i].getY();
            }
            offsetBottomY = childrenExtents[i].getHeight() - offsetTopY;
            offsetTopYs[i] = offsetTopY;
            if(maxOffsetTopY < offsetTopY){
                maxOffsetTopY = offsetTopY;
            }
            if (maxBottomOffsetY < offsetBottomY){
                maxBottomOffsetY = offsetBottomY;
            }
            
            currentX += childrenExtents[i].getWidth();

            currentX += HSPACE_PIXEL;
        }
        if(numChildren == 0){
            // if no children, then fix the padding_right
            currentX += HSPACE_PIXEL;
        }
        
        // currentX now equals to total width of the seq
        
        for(int i = 0; i < numChildren; i++){
            moveDownDistances[i] = maxOffsetTopY - offsetTopYs[i];
        }
        
        // move all the children to the base line
        for(int i = 0; i < numChildren; i++){
            // currently at y=0, go down by moveDownDistances[i]
            
            /*/
            nv.getChild(i).placeNext(
                nv.getChild(i).getNextX(), 
                nv.getChild(i).getNextY() + VSPACE_PIXEL + moveDownDistances[i]
            );
            /*/
            nv.getChild(i).translateNextHierarchy(
                0, 
                VSPACE_PIXEL + moveDownDistances[i]
            );
            //*/
        }

        nv.placeNext(px, py);
        
        Rectangle2D myNextExtent = null;
        // set the size to adapt the children
        if(nv.getNumChildren() == 0){
            myNextExtent = new Rectangle2D.Double(
                px, 
                py, 
                currentX, 
                maxOffsetTopY + maxBottomOffsetY + EMPTY_VSPACE_PIXEL * 2
            );
        } else {
            myNextExtent = new Rectangle2D.Double(
                px, 
                py, 
                currentX, 
                maxOffsetTopY + maxBottomOffsetY + VSPACE_PIXEL * 2
            );
        }
        
        // stretch
        if(nv instanceof RegexNodeView){
            if(numChildren > 0){
                // modify entry and exit point
                double entryPointY = myNextExtent.getY() + VSPACE_PIXEL + maxOffsetTopY;
                if(nv.getCurrentDirection().equals(Direction.EAST)){
                    ((RegexNodeView)nv).setEntryPoint(new Point2D.Double(myNextExtent.getX(), entryPointY));
                    ((RegexNodeView)nv).setExitPoint(new Point2D.Double(myNextExtent.getMaxX(), entryPointY));
                } else if (nv.getCurrentDirection().equals(Direction.WEST)){
                    ((RegexNodeView)nv).setExitPoint(new Point2D.Double(myNextExtent.getX(), entryPointY));
                    ((RegexNodeView)nv).setEntryPoint(new Point2D.Double(myNextExtent.getMaxX(), entryPointY));
                }
            }
        }
        nv.setNextShape(myNextExtent);
        
        // TODO unlock(view)
    }
    
    /* (non-Javadoc)
     * @see regex.view.layout.RegexNodeLayoutManager#handleStretch(regex.view.RegexNodeView)
     */
    @Override
    public void handleStretch(RegexNodeView view) {
        // now the Y of all the children are right, only handle X
        Rectangle2D myNextShape = view.getNextShapeExtent();
        Rectangle2D stretchExtent = view.getStretch();
        if(stretchExtent.getWidth() >= myNextShape.getWidth() || stretchExtent.getHeight() >= myNextShape.getHeight()){
            int numChildren = view.getNumChildren();
            double[] childXs = new double[numChildren];
            if(numChildren > 0){
                
                if(view.getCurrentDirection().equals(Direction.EAST)){
                    for(int i = 0; i < numChildren; i++){
                        childXs[i] = view.getChild(i).getNextShapeExtent().getX();
                    }
                } else {
                    for(int i = 0; i < numChildren; i++){
                        childXs[i] = view.getChild(numChildren - i - 1).getNextShapeExtent().getX();
                    }
                }
                
                if(numChildren == 1){
                    // move the only child at the center
                    double newX = stretchExtent.getWidth() - view.getChild(0).getNextShapeExtent().getWidth();
                    newX /= 2.0;
                    newX += myNextShape.getX();
                    view.getChild(0).placeNext(newX, view.getChild(0).getNextShapeExtent().getY());
                } else {
                    // calculates the average horizontal space
                    double averageHspace = (stretchExtent.getWidth() - HSPACE_PIXEL * 2);
                    for(int i = 0; i < numChildren; i++){
                        averageHspace -= view.getChild(i).getNextShapeExtent().getWidth();
                    }
                    averageHspace /= (numChildren - 1);
                    
                    // move the kids to the place
                    double currentX = myNextShape.getX() + HSPACE_PIXEL;
                    for(int i = 0; i < numChildren; i++){
                        RegexNodeView kid = view.getRegexChild(i);
                        if(view.getCurrentDirection().equals(Direction.WEST)){
                            kid = view.getRegexChild(numChildren - i - 1);
                        }
                        
                        kid.placeNext(currentX, kid.getNextShapeExtent().getY());
                        currentX += kid.getNextShapeExtent().getWidth();
                        currentX += averageHspace;
                    }
                }
            }
            
            // expand the shape of the SEQ
            // update the exit point
            if(view.getCurrentDirection().equals(Direction.EAST)){
                Point2D oldExitPoint = view.getExitPoint();
                view.setExitPoint(new Point2D.Double(
                    oldExitPoint.getX() + stretchExtent.getWidth() - myNextShape.getWidth(),
                    oldExitPoint.getY()
                ));
            } else if (view.getCurrentDirection().equals(Direction.WEST)){
                Point2D oldEntryPoint = view.getEntryPoint();
                view.setEntryPoint(new Point2D.Double(
                    oldEntryPoint.getX() + stretchExtent.getWidth() - myNextShape.getWidth(),
                    oldEntryPoint.getY()
                ));
            }
            
            // do the stretch on shape
            Rectangle2D.union(
                myNextShape, 
                new Rectangle2D.Double(
                    myNextShape.getX(), 
                    myNextShape.getY(), 
                    Math.max(myNextShape.getWidth(), stretchExtent.getWidth()), 
                    Math.max(myNextShape.getHeight(), stretchExtent.getHeight())
                ), 
                myNextShape
            );
        }
        
        view.setNextShape(myNextShape);
    }

    private static SequenceLayoutManager instance = new SequenceLayoutManager();
    
    /**
     * Gets the globally unique instance of this class. (Singleton Pattern)
     * @return
     */
    public static RegexNodeLayoutManager getInstance() {
        return instance;
    }

    /* (non-Javadoc)
     * @see regex.view.layout.RegexNodeLayoutManager#layoutZones(regex.view.RegexNodeView, regex.view.RegexDropZone)
     */
    @Override
    public void layoutZones(RegexNodeView view, RegexDropZone zone){
        Rectangle2D extent = view.getNextShapeExtent();
        
        String zoneIdStr = zone.getId();
        int zoneId = zone.getNodeNumber();
        
        double zoneX = extent.getX();
        double zoneY = extent.getY();
        double zoneWidth = 0.0, zoneHeight = 0.0;
        
        // since there is no layoutZones(), we have to do the layout one by one...
        if(zoneIdStr.compareTo(SequenceNodeView.ID_DROPZONE_HEAD) == 0){
            // handling HEAD drop zone
            zoneWidth = HSPACE_PIXEL;
            if(view.getNumChildren() == 0){
                // if there is now children, then the HEAD is the only visible zone
//                zoneX = extent.getX();
//                zoneY = extent.getY();
                zoneWidth = extent.getWidth();
                zoneHeight = extent.getHeight();
            } else {
                if(view.getCurrentDirection().equals(Direction.EAST)){
                    zoneWidth = view.getChild(0).getNextShapeExtent().getX() - extent.getX();
                    zoneX = extent.getX();
                } else if(view.getCurrentDirection().equals(Direction.WEST)){
                    zoneWidth = extent.getMaxX() - view.getChild(0).getNextShapeExtent().getMaxX();
                    zoneX = view.getChild(0).getNextShapeExtent().getMaxX();
                }
                zoneY = extent.getY() + VSPACE_PIXEL;
                zoneHeight = extent.getHeight() - VSPACE_PIXEL * 2;
            }
//        } else if (zoneIdStr.compareTo(SequenceNodeView.ID_DROPZONE_NORTH) == 0) {
//            // handling NORTH drop zone
//            if(view.getNumChildren() > 0){
////                zoneX = extent.getX();
////                zoneY = extent.getY();
//                zoneWidth = extent.getWidth();
//                zoneHeight = VSPACE_PIXEL;
//            }
//        } else if (zoneIdStr.compareTo(SequenceNodeView.ID_DROPZONE_SOUTH) == 0) {
//            // handling NORTH drop zone
//            if(view.getNumChildren() > 0){
////                zoneX = extent.getX();
//                zoneY = extent.getMaxY() - VSPACE_PIXEL;
//                zoneWidth = extent.getWidth();
//                zoneHeight = VSPACE_PIXEL;
//            }
        } else {
            // handling other drop nodes before each child
            Assert.check(view.getNumChildren() >= zoneId);
            zoneWidth = HSPACE_PIXEL;
            zoneHeight = extent.getHeight() - VSPACE_PIXEL * 2;
            zoneY = extent.getY() + VSPACE_PIXEL;
            
            if(view.getCurrentDirection().equals(Direction.EAST)){
                zoneX = view.getChild(zoneId - 1).getNextShapeExtent().getMaxX();
                
                if(zoneId == view.getNumChildren()){
                    // the last one
                    zoneWidth = extent.getMaxX() - zoneX;
                } else {
                    // middle ones
                    zoneWidth = view.getChild(zoneId).getNextShapeExtent().getX() - zoneX;
                }
            } else if(view.getCurrentDirection().equals(Direction.WEST)){
                if(zoneId == view.getNumChildren()){
                    // the leftmost one
                    zoneX = extent.getX();
                    zoneWidth = view.getChild(zoneId -1).getNextShapeExtent().getX() - extent.getX();
                } else {
                    zoneX = view.getChild(zoneId).getNextShapeExtent().getMaxX();
                    zoneWidth = view.getChild(zoneId).getNextShapeExtent().getX() - zoneX;
                } 
            }
        }
        
        zone.setNextShape(new Rectangle2D.Double(
            zoneX, zoneY, zoneWidth, zoneHeight
        ));
        zone.placeNext(zoneX, zoneY);
    }
}
