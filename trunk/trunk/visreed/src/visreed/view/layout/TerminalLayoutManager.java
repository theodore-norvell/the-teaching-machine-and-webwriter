/**
 * TerminalLayoutManager.java
 * 
 * @date: Jul 12, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view.layout;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import visreed.view.TerminalNodeView;
import visreed.view.VisreedDropZone;
import visreed.view.VisreedNodeView;

/**
 * TerminalLayoutManager handles with dynamic-sized (according to the length of
 * terminals) node with no children. 
 * @author Xiaoyu Guo
 */
public class TerminalLayoutManager extends VisreedNodeLayoutManager {
    protected TerminalLayoutManager() {
        super();
    }
    protected static final double HSPACE_PIXEL = 5;
    protected static final double VSPACE_PIXEL = 5;

    /* (non-Javadoc)
     * @see visreed.view.layout.VisreedNodeLayoutManager#layoutNode(visreed.view.VisreedNodeView, double, double)
     */
    @Override
    public void layoutNode(VisreedNodeView nv) {
        if(nv == null){
            return;
        }
        // terminal nodes does not have children
        // make the terminal node large enough
        // TODO handling size without a {@link Graphics2D} object 
        
        String text = nv.getNode().getPayload().getDescription();
        
        if(text == null){
            text = "";
        }
        
        double contentWidth = ((TerminalNodeView)nv).getDesiredWidth();
        
        Rectangle2D extent = nv.getNextShapeExtent();
        Rectangle.union(
            extent, 
            new Rectangle2D.Double(
                extent.getX(),
                extent.getY(),
                HSPACE_PIXEL * 2 + contentWidth,
                VSPACE_PIXEL * 2
            ), 
            extent
        );
        nv.setNextShape(extent);
        
        nv.placeNext(0, 0);
    }
    
    /* (non-Javadoc)
     * @see visreed.view.layout.VisreedNodeLayoutManager#layoutZones(visreed.view.VisreedNodeView, visreed.view.VisreedDropZone)
     */
    public void layoutZone(VisreedNodeView view, VisreedDropZone zone){
    	Rectangle2D extent = view.getNextShapeExtent();
    	zone.setNextShape(extent);
    	zone.placeNext(extent.getX(), extent.getY());
    }

    private static TerminalLayoutManager instance = new TerminalLayoutManager();
    
    /**
     * Gets the globally unique instance of this class. (Singleton Pattern)
     * @return
     */
    public static VisreedNodeLayoutManager getInstance() {
        return instance;
    }

}
