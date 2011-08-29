/**
 * AlternationLayoutManager.java
 * 
 * @date: 2011-5-27
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view.layout;

import java.awt.geom.Rectangle2D;

import tm.utilities.Assert;
import visreed.view.AlternationNodeView;
import visreed.view.VisreedDropZone;
import visreed.view.VisreedNodeView;

/**
 * This is a horizontal simple tree layout manager. 
 * <p> This layout is used to represent alternation node.
 * @author Xiaoyu Guo
 *
 */
public class AlternationLayoutManager
extends VisreedNodeLayoutManager {
    
    private static final double HSPACE_PIXEL = 30;
    private static final double VSPACE_BETWEEN_LEVEL_PIXEL = 10;
    
    private static final AlternationLayoutManager instance = new AlternationLayoutManager();
    public static AlternationLayoutManager getInstance(){
        return instance;
    }


    /* (non-Javadoc)
     * @see visreed.view.layout.VisreedNodeLayoutManager#layoutNode(visreed.view.VisreedNodeView, double, double)
     */
    @Override
    public void layoutNode(VisreedNodeView view, double px, double py) {
        double myHeight = view.getNextShapeExtent().getHeight();
        int kids = view.getNumChildren();
        
        if(view.getDislocation() != null){
            px += view.getDislocation().getX();  // Currently dislocations are not allowed to be
            py += view.getDislocation().getY();  // negative so px, py cannot be negative
        }
        
        double localY = py + VSPACE_BETWEEN_LEVEL_PIXEL;
        double maxChildWidth = 0;
        
        // put kids to the right stepping over from top
        for (int i = 0; i < kids; i++){
            VisreedNodeView kid = view.getVisreedChild(i);
            if(i > 0){
                localY += VSPACE_BETWEEN_LEVEL_PIXEL;
            }
            layoutNodes(kid, px + HSPACE_PIXEL, localY);
            
            Rectangle2D kidExtent = kid.getNextShapeExtent();
            localY += kidExtent.getHeight();
            if(maxChildWidth < kidExtent.getWidth()){
                maxChildWidth = kidExtent.getWidth();
            }
        }
        
        // stretch all the children to the same width
        for(int i = 0; i < kids; i++){
            VisreedNodeView kid = view.getVisreedChild(i);
            
            /*
            // only stretch short children
            if(kid != null && kid.getNextShapeExtent().getWidth() < maxChildWidth){
            /*/
            // stretch every children 
            if(kid != null){
            //*/
                kid.setStretchWidth(maxChildWidth);
                kid.getLayoutManager().handleStretch(kid);
            }
        }

        Rectangle2D myNextExtent = view.getNextShapeExtent();
        
        if(kids > 0){
            // center me to the left of my kids
			double shapeCenterY = view.getChild(kids/2).getNextShapeExtent().getCenterY();
			if(kids%2 == 0) {
			    // average of center of two middle kids
				shapeCenterY = (shapeCenterY + view.getChild(kids/2-1).getNextShapeExtent().getCenterY())/2.0; 
			}
            double topToCenterY = myNextExtent.getCenterY() - view.getNextShapeExtent().getY();
            view.placeNext(px, shapeCenterY - topToCenterY);
        } else {
            view.placeNext(px, py);
        }

        myNextExtent = view.getNextShapeExtent();
        
        myHeight = localY - py;
        Rectangle2D.union(
            myNextExtent, 
            new Rectangle2D.Double(
                px, 
                py, 
                maxChildWidth + 2 * HSPACE_PIXEL, 
                myHeight + VSPACE_BETWEEN_LEVEL_PIXEL
            ), 
            myNextExtent
        );
        view.setNextShape(myNextExtent);
    }
    
    /* (non-Javadoc)
     * @see visreed.view.layout.VisreedNodeLayoutManager#layoutZones(visreed.view.VisreedNodeView, visreed.view.VisreedDropZone)
     */
    @Override
    public void layoutZones(VisreedNodeView view, VisreedDropZone zone){
        Rectangle2D extent = view.getNextShapeExtent();
        double zoneX = extent.getX();
        double zoneWidth = extent.getWidth();
        double zoneHeight = VSPACE_BETWEEN_LEVEL_PIXEL;
        
        // since there is no layoutZones(), we have to do the layout one by one...
        if(zone.getId().compareTo(AlternationNodeView.ID_DROPZONE_HEAD) == 0){
            // handling HEAD drop zone
            if(view.getNumChildren() == 0){
                zoneHeight = extent.getHeight();
            }
            
            zone.setNextShape(new Rectangle2D.Double(
                zoneX, 
                extent.getY(),
                zoneWidth,
                zoneHeight
            ));
            zone.placeNext(
                zoneX, 
                extent.getY()
            );
        } else {
            // handling other drop nodes below each child
            int i = zone.getNodeNumber();
            Assert.check(view.getNumChildren() >= i);
            Rectangle2D lastChildExtent = view.getChild(i-1).getNextShapeExtent(); 
            
            zone.setNextShape(new Rectangle2D.Double(
                extent.getX(),
                lastChildExtent.getMaxY(),
                zoneWidth,
                zoneHeight
            ));
            zone.placeNext(
                extent.getX(),
                lastChildExtent.getMaxY()
            );
        }
    }
}
