/**
 * OptionalNodeView.java
 * 
 * @date: Aug 1, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view;

import higraph.view.HigraphView;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
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
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.view.layout.OptionalLayoutManager;
import visreed.view.layout.VisreedNodeLayoutManager;

/**
 * @author Xiaoyu Guo
 *
 */
public class OptionalNodeView extends VisreedNodeView {

    /**
     * @param v
     * @param node
     * @param timeMan
     */
    public OptionalNodeView(
            HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v,
            VisreedNode node,
            BTTimeManager timeMan) {
        super(v, node, timeMan);
    }

    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#getLayoutHelper()
     */
    @Override
    protected VisreedNodeLayoutManager getLayoutHelper() {
        return OptionalLayoutManager.getInstance();
    }

    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#drawNode(java.awt.Graphics2D)
     */
    protected void drawNode(Graphics2D screen) {
        VisreedNodeView kid = this.getVisreedChild(0);
        
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
                ArrowStyle.ARROW_AT_THE_BACK, 
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
     * @see visreed.view.VisreedNodeView#getEntryPoint()
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
     * @see visreed.view.VisreedNodeView#getExitPoint()
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
