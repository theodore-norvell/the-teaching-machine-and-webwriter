/**
 * SyntaxNodeView.java
 * 
 * @date: 2011-6-14
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.view;

import higraph.view.HigraphView;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import regex.awt.GraphicsHelper;
import regex.model.RegexEdge;
import regex.model.RegexEdgeLabel;
import regex.model.RegexHigraph;
import regex.model.RegexNode;
import regex.model.RegexPayload;
import regex.model.RegexSubgraph;
import regex.model.RegexWholeGraph;
import regex.view.layout.RegexNodeLayoutManager;
import regex.view.layout.SyntaxTreeLayoutManager;
import tm.backtrack.BTTimeManager;

/**
 * SyntaxNodeView is used in represent an unified(and simplier) tree view.
 * 
 * @author Xiaoyu Guo
 */
public class SyntaxNodeView extends RegexNodeView {

    public SyntaxNodeView(
            HigraphView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> v,
            RegexNode node,
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
     * @see regex.view.RegexNodeView#drawNode(java.awt.Graphics2D)
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
        RegexNode node = this.getNode();
        RegexPayload payload = node.getPayload();
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
     * @see regex.view.RegexNodeView#getLayoutHelper()
     */
    @Override
    protected RegexNodeLayoutManager getLayoutHelper() {
        return SyntaxTreeLayoutManager.getInstance();
    }

}
