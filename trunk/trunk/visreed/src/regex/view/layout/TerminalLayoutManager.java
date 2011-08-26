/**
 * TerminalLayoutManager.java
 * 
 * @date: Jul 12, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.view.layout;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import regex.view.RegexNodeView;
import regex.view.TerminalNodeView;

/**
 * TerminalLayoutManager handles with dynamic-sized (according to the length of
 * terminals) node with no children. 
 * @author Xiaoyu Guo
 */
public class TerminalLayoutManager extends RegexNodeLayoutManager {
    protected TerminalLayoutManager() {
        super();
    }
    private static final double HSPACE_PIXEL = 5;
    private static final double VSPACE_PIXEL = 5;

    /* (non-Javadoc)
     * @see regex.view.layout.RegexNodeLayoutManager#layoutNode(regex.view.RegexNodeView, double, double)
     */
    @Override
    public void layoutNode(RegexNodeView nv, double px, double py) {
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
        
        nv.placeNext(px, py);
    }

    private static TerminalLayoutManager instance = new TerminalLayoutManager();
    
    /**
     * Gets the globally unique instance of this class. (Singleton Pattern)
     * @return
     */
    public static RegexNodeLayoutManager getInstance() {
        return instance;
    }

}
