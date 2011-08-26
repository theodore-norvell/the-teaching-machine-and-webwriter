package regex.view;

import higraph.view.HigraphView;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import regex.awt.ArrowDirection;
import regex.awt.ArrowStyle;
import regex.awt.GraphicsHelper;
import regex.model.Direction;
import regex.model.RegexEdge;
import regex.model.RegexEdgeLabel;
import regex.model.RegexHigraph;
import regex.model.RegexNode;
import regex.model.RegexPayload;
import regex.model.RegexSubgraph;
import regex.model.RegexWholeGraph;
import regex.view.layout.RegexNodeLayoutManager;
import regex.view.layout.SequenceLayoutManager;
import tm.backtrack.BTTimeManager;

public class SequenceNodeView extends RegexNodeView {

    public SequenceNodeView(
            HigraphView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> v,
            RegexNode node,
            BTTimeManager timeMan
    ) {
        super(v, node, timeMan);
    }

    /* (non-Javadoc)
     * @see regex.view.RegexNodeView#drawNode(java.awt.Graphics2D)
     */
    @Override
    protected void drawNode(Graphics2D screen) {
        if(this.getColor() != null){
            screen.setColor(this.getColor());
        }
        ArrowDirection dir = ArrowDirection.RIGHT;
        if(this.getCurrentDirection().equals(Direction.WEST)){
            dir = dir.getReverseDirection();
        }
        
        for (int i = 0; i < this.getNumChildren(); i++) {
            RegexNodeView child = this.getRegexChild(i);
            if(child == null){
                continue;
            }
            
            RegexNodeView prevChild;
            if (i == 0) {
                // starting connection
                GraphicsHelper.drawHorizontalConnectionCurve(
                    screen,
                    this.getEntryPoint().getX(),
                    this.getEntryPoint().getY(),
                    child.getEntryPoint().getX(),
                    child.getEntryPoint().getY()
                );
            } else {
                prevChild = this.getRegexChild(i - 1);
                if(prevChild == null){
                    continue;
                }
                GraphicsHelper.drawHorizontalConnectionCurve(
                    screen,
                    prevChild.getExitPoint().getX(),
                    prevChild.getExitPoint().getY(),
                    child.getEntryPoint().getX(),
                    child.getEntryPoint().getY()
                );
            }

            if(i == this.getNumChildren() - 1){
                // last connection
                GraphicsHelper.drawHorizontalConnectionCurve(
                    screen,
                    child.getExitPoint().getX(),
                    child.getExitPoint().getY(),
                    this.getExitPoint().getX(),
                    this.getExitPoint().getY()
                );
            }
            
            /* Arrows */
            //*
            // connection arrows
            if(i > 0){
                // the first element does need the arrow, as the previous
                // element have the arrow already.
                GraphicsHelper.drawArrow(
                    screen, 
                    ArrowStyle.DEFAULT, 
                    dir, 
                    child.getEntryPoint()
                );
            }
            //*/
            
            //*
            if(this.getNumChildren() > 1 || this.getStretch().getWidth() > 0){
                // exit arrow
                GraphicsHelper.drawArrow(
                    screen, 
                    ArrowStyle.DEFAULT, 
                    dir, 
                    this.getExitPoint()
                );
            }
            //*/
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see regex.view.RegexNodeView#getLayoutHelper()
     */
    @Override
    protected RegexNodeLayoutManager getLayoutHelper() {
        return SequenceLayoutManager.getInstance();
    }
    
    /* (non-Javadoc)
     * @see regex.view.RegexNodeView#intersects(java.awt.geom.Rectangle2D)
     */
    @Override
    public boolean intersects(Rectangle2D marquee){
        Rectangle2D center = new Rectangle(
            (int)this.getNextBaseExtent().getCenterX(),
            (int)this.getNextBaseExtent().getCenterY(),
            1,
            1
        );
        return marquee.intersects(center);
    }
    
    /**
     * Handling DropZones
     * Rules for id: 
     * <li>"north"</li>
     * <li>"south"</li>
     * <li>"head"</li>
     * <li>"node_1"</li>
     * <li>"node_2"</li>
     */
    public static final String ID_DROPZONE_NORTH = "north";
    public static final String ID_DROPZONE_SOUTH = "south";
    public static final String ID_DROPZONE_HEAD = "head";
    public static final String ID_PREFIX_DROPZONE_NODE = "node_";
    
    /* (non-Javadoc)
     * @see regex.view.RegexNodeView#shouldRefreshDropZone()
     */
    protected boolean shouldRefreshDropZone(){
        // NORTH, SOUTH, the HEAD, and one after each children
        boolean shouldRefresh = (this.zones.size() != this.getNumChildren() + 1);
        shouldRefresh |= (this.findZone(ID_DROPZONE_HEAD) == null);
//        shouldRefresh |= (this.findZone(ID_DROPZONE_NORTH) == null);
//        shouldRefresh |= (this.findZone(ID_DROPZONE_SOUTH) == null);

        if(shouldRefresh == false){
            for(int i = 0; i < this.getNumChildren(); i++){
                shouldRefresh |= (this.findZone(ID_PREFIX_DROPZONE_NODE + (i + 1)) == null);
            }
        }
        return shouldRefresh;
    }
    
    /* (non-Javadoc)
     * @see regex.view.RegexNodeView#refreshDropZone()
     */
    @Override
    public void reCreateDropZone(){
        // For simplicity here we just wipe everything out and build them again.
        this.removeZones();
        this.makeAndAddInsertChildDropZone(ID_DROPZONE_HEAD, 0);
//        zone = this.makeAndAddDropZone(ID_DROPZONE_NORTH, 0);
//        zone = this.makeAndAddDropZone(ID_DROPZONE_SOUTH, 0);
        for(int i = 0; i < this.getNumChildren(); i++){
            this.makeAndAddInsertChildDropZone(ID_PREFIX_DROPZONE_NODE + (i + 1), i + 1);
        }
    }
}
