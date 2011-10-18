/**
 * SyntaxNodeView.java
 * 
 * @date: 2011-6-14
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view;

import higraph.view.HigraphView;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import tm.backtrack.BTTimeManager;
import visreed.awt.GraphicsHelper;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.VisreedPayload;
import visreed.view.layout.SyntaxTreeLayoutManager;
import visreed.view.layout.VisreedNodeLayoutManager;

/**
 * SyntaxNodeView is used in represent an unified(and simplier) tree view.
 * 
 * @author Xiaoyu Guo
 */
public class SyntaxNodeView extends VisreedNodeView {

    public SyntaxNodeView(
            HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v,
            VisreedNode node,
            BTTimeManager timeMan) {
        super(v, node, timeMan);
    }
    
    public Color getFillColor(){
        if(this.fillColorVar.get() == this.getHigraphView().getDefaultNodeFillColor()){
            return Color.lightGray;
        } else {
            return super.getFillColor();
        }
    }

    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#drawNode(java.awt.Graphics2D)
     */
    @Override
    protected void drawNode(Graphics2D screen) {
        if (this.getOutlineOnly() == false && this.getFillColor() != null) {
            Paint previousPaint = screen.getPaint();
            Paint gradientPaint = new GradientPaint(
                (float) this.getNextShapeExtent().getX(), 
                (float) this.getNextShapeExtent().getY(),
                GraphicsHelper.getGradientColor(getFillColor()),
                (float) this.getNextShapeExtent().getX(), 
                (float) this.getNextShapeExtent().getMaxY(),
                this.getFillColor()
            );
            screen.setPaint(gradientPaint);
            screen.fill(this.getShape());
            screen.setPaint(previousPaint);
        }
        
        if (this.getColor() != null && this.getStroke() != null) {
            screen.setColor(this.getColor());
            Stroke previousStroke = screen.getStroke();
            screen.setStroke(this.getStroke());
            screen.draw(this.getShape());
            screen.setStroke(previousStroke);
        }
        if (this.getBranch() != null) {
            this.getBranch().draw(screen);
        }
        
        Rectangle2D extent = this.getShape().getBounds2D();
        VisreedNode node = this.getNode();
        VisreedPayload payload = node.getPayload();
        String text = payload.getTag().getDescription();

        GraphicsHelper.paintCenteredString(
            screen,
            text,
            extent.getCenterX(),
            extent.getCenterY()
        );
    }

    /*
     * (non-Javadoc)
     * 
     * @see visreed.view.VisreedNodeView#getLayoutHelper()
     */
    @Override
    protected VisreedNodeLayoutManager getLayoutHelper() {
        return SyntaxTreeLayoutManager.getInstance();
    }

}
