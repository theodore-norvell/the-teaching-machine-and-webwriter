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
import visreed.view.layout.KleeneStarLayoutManager;
import visreed.view.layout.VisreedNodeLayoutManager;

public class KleeneStarNodeView extends VisreedNodeView {

	public KleeneStarNodeView(
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
        return KleeneStarLayoutManager.getInstance();
    }

    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#drawNode(java.awt.Graphics2D)
     */
    @Override
    protected void drawNode(Graphics2D screen) {
        VisreedNodeView kid = this.getVisreedChild(0);
        
        if(this.getColor() != null){
            screen.setColor(this.getColor());
        }
        if(this.getStroke() != null){
            screen.setStroke(this.getStroke());
        }
        
        ArrowDirection dir = ArrowDirection.LEFT;
        if(this.getCurrentDirection().equals(Direction.WEST)){
            dir = dir.getReverseDirection();
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
            
            /*
            // outgoing arrow
            GraphicsHelper.drawArrow(
                screen, 
                ArrowStyle.DEFAULT, 
                0, 
                this.getExitPoint()
            );
            //*/
            
            // exit -> kid.entry (entry is on the right)
            GraphicsHelper.drawHorizontalConnectionCurve(
                screen, 
                this.getExitPoint().getX(), 
                this.getExitPoint().getY(),
                kid.getEntryPoint().getX(),
                kid.getEntryPoint().getY()
            );

            GraphicsHelper.drawArrow(
                screen, 
                ArrowStyle.ARROW_AT_THE_BACK, 
                dir, 
                kid.getEntryPoint()
            );

            // kid.exit -> loop
            GraphicsHelper.drawHorizontalConnectionCurve(
                screen, 
                this.getEntryPoint().getX(), 
                this.getEntryPoint().getY(), 
                kid.getExitPoint().getX(), 
                kid.getExitPoint().getY()
            );
            
            GraphicsHelper.drawArrow(
                screen, 
                ArrowStyle.DEFAULT, 
                ArrowDirection.UP, 
                new Point2D.Double(
                    (kid.getExitPoint().getX() + this.getEntryPoint().getX()) / 2.0,
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
            extent.getY() + KleeneStarLayoutManager.getVSpaceTop()
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
            extent.getY() + KleeneStarLayoutManager.getVSpaceTop()
        );
    }

}
