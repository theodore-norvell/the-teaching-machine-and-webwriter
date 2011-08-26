/**
 * OptionalNodeView.java
 * 
 * @date: Aug 1, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.view;

import higraph.view.HigraphView;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
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
import regex.view.layout.OptionalLayoutManager;
import regex.view.layout.RegexNodeLayoutManager;
import tm.backtrack.BTTimeManager;

/**
 * @author Xiaoyu Guo
 *
 */
public class OptionalNodeView extends RegexNodeView {

    /**
     * @param v
     * @param node
     * @param timeMan
     */
    public OptionalNodeView(
            HigraphView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> v,
            RegexNode node,
            BTTimeManager timeMan) {
        super(v, node, timeMan);
    }

    /* (non-Javadoc)
     * @see regex.view.RegexNodeView#getLayoutHelper()
     */
    @Override
    protected RegexNodeLayoutManager getLayoutHelper() {
        return OptionalLayoutManager.getInstance();
    }

    /* (non-Javadoc)
     * @see regex.view.RegexNodeView#drawNode(java.awt.Graphics2D)
     */
    protected void drawNode(Graphics2D screen) {
        RegexNodeView kid = this.getRegexChild(0);
        
        if(this.getColor() != null){
            screen.setColor(this.getColor());
        }
        if(this.getStroke() != null){
            screen.setStroke(this.getStroke());
        }
        
        if(kid != null){
            // entry -> exit
            GraphicsHelper.drawHorizontalConnectionCurve(
                screen, 
                this.getEntryPoint().getX(), 
                this.getEntryPoint().getY(), 
                this.getExitPoint().getX(), 
                this.getExitPoint().getY()
            );
            
            // entry -> kid.entry
            GraphicsHelper.drawHorizontalConnectionCurve(
                screen, 
                this.getEntryPoint().getX(), 
                this.getEntryPoint().getY(),
                kid.getEntryPoint().getX(),
                kid.getEntryPoint().getY()
            );

            GraphicsHelper.drawArrow(
                screen, 
                ArrowStyle.DEFAULT, 
                ArrowDirection.RIGHT, 
                kid.getEntryPoint()
            );

            // kid.exit -> exit
            GraphicsHelper.drawHorizontalConnectionCurve(
                screen, 
                this.getExitPoint().getX(), 
                this.getExitPoint().getY(),
                kid.getExitPoint().getX(), 
                kid.getExitPoint().getY()
            );
            
            GraphicsHelper.drawArrow(
                screen, 
                ArrowStyle.DEFAULT, 
                ArrowDirection.UP, 
                new Point2D.Double(
                    (kid.getExitPoint().getX() + this.getExitPoint().getX()) / 2.0,
                    this.getExitPoint().getY()
                )
            );
        }
    }

    /* (non-Javadoc)
     * @see regex.view.RegexNodeView#getEntryPoint()
     */
    @Override
    public Point2D getEntryPoint(){
        Rectangle2D extent = this.getNextShapeExtent();
        double x = extent.getX();
        if(this.getCurrentDirection().equals(Direction.WEST)){
            x = extent.getMaxX();
        }
        return new Point2D.Double(
            x, 
            extent.getY() + OptionalLayoutManager.getVSpaceTop()
        );
    }
    
    /* (non-Javadoc)
     * @see regex.view.RegexNodeView#getExitPoint()
     */
    @Override
    public Point2D getExitPoint(){
        Rectangle2D extent = this.getNextShapeExtent();
        double x = extent.getMaxX();
        if(this.getCurrentDirection().equals(Direction.WEST)){
            x = extent.getX();
        }
        return new Point2D.Double(
            x, 
            extent.getY() + OptionalLayoutManager.getVSpaceTop()
        );
    }
}
