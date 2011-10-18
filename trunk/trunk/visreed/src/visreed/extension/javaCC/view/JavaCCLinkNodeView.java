/**
 * JavaCCLinkNodeView.java
 * 
 * @date: Oct 12, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.view;

import higraph.view.HigraphView;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import tm.backtrack.BTTimeManager;
import visreed.awt.GraphicsHelper;
import visreed.extension.javaCC.view.layout.JavaCCLinkLayoutManager;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.VisreedPayload;
import visreed.view.TerminalNodeView;
import visreed.view.VisreedDropZone;
import visreed.view.layout.VisreedNodeLayoutManager;

/**
 * @author Xiaoyu Guo
 */
public class JavaCCLinkNodeView extends TerminalNodeView {
    protected static Color FILL_COLOR = Color.green;

	/**
	 * @param v
	 * @param node
	 * @param timeMan
	 */
	public JavaCCLinkNodeView(
			HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v,
			VisreedNode node, BTTimeManager timeMan) {
		super(v, node, timeMan);
	}

	/* (non-Javadoc)
	 * @see visreed.view.TerminalNodeView#shouldRefreshDropZone()
	 */
	@Override
    protected boolean shouldRefreshDropZone(){
    	boolean shouldRefresh = (this.zones.size() != 2);
    	shouldRefresh |= (this.findZone(ID_DROPZONE_TERMINAL) == null);
    	shouldRefresh |= (this.findZone(ID_DROPZONE_GO_TO_DEFINITION) == null); 
    	return shouldRefresh;
    }
    
    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#drawNode(java.awt.Graphics2D)
     */
    @Override
    protected void drawNode(Graphics2D screen) {
        Rectangle2D extent = this.getExtent();
        
        // border
        screen.setColor(this.getColor());
        screen.drawRoundRect(
            (int)extent.getX(), 
            (int)extent.getY(),
            (int)extent.getWidth(),
            (int)extent.getHeight(), 
            MAX_ROUND_RADIUS_PX,
            MAX_ROUND_RADIUS_PX
        );

        // content
        if(this.getOutlineOnly() == false){
            // dealing with filling color
            Paint previousPaint = screen.getPaint();
            Paint gradientPaint = new GradientPaint(
                (float) this.getNextShapeExtent().getX(), 
                (float) this.getNextShapeExtent().getY(),
                GraphicsHelper.getGradientColor(FILL_COLOR),
                (float) this.getNextShapeExtent().getX(), 
                (float) this.getNextShapeExtent().getMaxY(),
                FILL_COLOR
            );
            screen.setPaint(gradientPaint);
            screen.fillRoundRect(
                (int)extent.getX() + 1, 
                (int)extent.getY() + 1,
                (int)extent.getWidth() - 1,
                (int)extent.getHeight() - 1, 
                MAX_ROUND_RADIUS_PX,
                MAX_ROUND_RADIUS_PX
            );
            screen.setPaint(previousPaint);
        }
        
        screen.setColor(this.getColor());
        if(this.getColor() == null){
            screen.setColor(Color.black);
        }
        if(this.getNode() != null && this.getNode().getPayload() != null){
            Font previousFont = screen.getFont();
            
            screen.setFont(this.font);
            GraphicsHelper.paintCenteredString(
                screen, 
                this.getNode().getPayload().getDescription(),
                extent.getCenterX() - JavaCCLinkLayoutManager.GO_TO_DEF_WIDTH_PIXEL / 2,
                extent.getCenterY()
            );
            
            screen.setFont(previousFont);
        }
    }
	
    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#getLayoutHelper()
     */
    @Override
    protected VisreedNodeLayoutManager getLayoutHelper() {
        return JavaCCLinkLayoutManager.getInstance();
    }
	
	public static final String ID_DROPZONE_GO_TO_DEFINITION = "go_to_def";
    
    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#reCreateDropZone()
     */
    public void reCreateDropZone(){
    	super.reCreateDropZone();
    	if(this.mayAddZones() && this.findZone(ID_DROPZONE_GO_TO_DEFINITION) == null){
            VisreedDropZone zone = (
        		(JavaCCViewFactory)this.getNodeViewFactory()
    		).makeGoToDefinitionDropZone(this);
            zone.setId(ID_DROPZONE_GO_TO_DEFINITION);
            zone.setNodeNumber(0);
            this.addZone(zone);
    	}    
    }
    
    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#handleDoubleClick(java.awt.event.MouseEvent)
     */
    @Override
    public void handleDoubleClick(MouseEvent e) {
    }
}
