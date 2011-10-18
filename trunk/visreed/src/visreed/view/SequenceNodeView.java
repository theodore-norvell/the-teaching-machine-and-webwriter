package visreed.view;

import higraph.view.HigraphView;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import tm.backtrack.BTTimeManager;
import visreed.awt.ArrowDirection;
import visreed.awt.ArrowStyle;
import visreed.awt.GraphicsHelper;
import visreed.model.Direction;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.VisreedPayload;
import visreed.view.layout.SequenceLayoutManager;
import visreed.view.layout.VisreedNodeLayoutManager;

public class SequenceNodeView extends VisreedNodeView {

    public SequenceNodeView(
            HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v,
            VisreedNode node,
            BTTimeManager timeMan
    ) {
        super(v, node, timeMan);
    }

    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#drawNode(java.awt.Graphics2D)
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
        
        if(this.getNumChildren() == 0){
        	screen.drawLine(
        		(int)this.getEntryPoint().getX(),
        		(int)this.getEntryPoint().getY(),
        		(int)this.getExitPoint().getX(),
        		(int)this.getExitPoint().getY()
			);
        } else {
	        for (int i = 0; i < this.getNumChildren(); i++) {
	            VisreedNodeView child = this.getVisreedChild(i);
	            if(child == null){
	                continue;
	            }
	            
	            VisreedNodeView prevChild;
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
	                prevChild = this.getVisreedChild(i - 1);
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
    }

    /*
     * (non-Javadoc)
     * 
     * @see visreed.view.VisreedNodeView#getLayoutHelper()
     */
    @Override
    protected VisreedNodeLayoutManager getLayoutHelper() {
        return SequenceLayoutManager.getInstance();
    }
    
    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#intersects(java.awt.geom.Rectangle2D)
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
     * @see visreed.view.VisreedNodeView#shouldRefreshDropZone()
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
     * @see visreed.view.VisreedNodeView#refreshDropZone()
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
