/**
 * RepeatRangeNodeView.java
 * 
 * @date: Oct 4, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view;

import higraph.view.HigraphView;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

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
import visreed.model.payload.RepeatRangePayload;
import visreed.view.layout.RepeatRangeLayoutHelper;
import visreed.view.layout.VisreedNodeLayoutManager;

/**
 * @author Xiaoyu Guo
 *
 */
public class RepeatRangeNodeView extends VisreedNodeView {

	/**
	 * @param v
	 * @param node
	 * @param timeMan
	 */
	public RepeatRangeNodeView(
			HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v,
			VisreedNode node, BTTimeManager timeMan) {
		super(v, node, timeMan);
	}

	/* (non-Javadoc)
	 * @see visreed.view.VisreedNodeView#getLayoutHelper()
	 */
	@Override
	protected VisreedNodeLayoutManager getLayoutHelper() {
		return RepeatRangeLayoutHelper.getInstance();
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
        
        ArrowDirection dir = ArrowDirection.RIGHT;
        if(this.getCurrentDirection().equals(Direction.WEST)){
            dir = dir.getReverseDirection();
        }
        
        if(kid != null){
            // entry -> kid.entry
            GraphicsHelper.drawHorizontalConnectionCurve(
                screen, 
                this.getEntryPoint().getX(), 
                this.getEntryPoint().getY(), 
                kid.getEntryPoint().getX(), 
                kid.getEntryPoint().getY()
            );

            // arrow -> kid.entry
            GraphicsHelper.drawArrow(
                screen, 
                ArrowStyle.ARROW_AT_THE_BACK, 
                dir, 
                kid.getEntryPoint()
            );
            
            // kid.exit -> exit
            GraphicsHelper.drawHorizontalConnectionCurve(
                screen, 
                kid.getExitPoint().getX(), 
                kid.getExitPoint().getY(),
                this.getExitPoint().getX(),
                this.getExitPoint().getY()
            );

            // loop back
            Point2D pKidEnt = new Point2D.Double(
                kid.getEntryPoint().getX(),
                this.getExtent().getMaxY() - RepeatRangeLayoutHelper.getVSpacePixel() / 2.0
            );
            
            Point2D pKidExit = new Point2D.Double(
                kid.getExitPoint().getX(),
                this.getExtent().getMaxY() - RepeatRangeLayoutHelper.getVSpacePixel() / 2.0
            );
            
            GraphicsHelper.drawHorizontalConnectionCurve(
                screen, 
                this.getEntryPoint().getX(), 
                this.getEntryPoint().getY(), 
                pKidEnt.getX(), 
                pKidEnt.getY()
            );
            
            screen.drawLine(
                (int)pKidEnt.getX(),
                (int)pKidEnt.getY(),
                (int)pKidExit.getX(),
                (int)pKidExit.getY()
            );
            
            GraphicsHelper.drawHorizontalConnectionCurve(
                screen, 
                this.getExitPoint().getX(), 
                this.getExitPoint().getY(),
                pKidExit.getX(),
                pKidExit.getY()
            );

            GraphicsHelper.drawHorizontalConnectionCurve(
                screen, 
                this.getEntryPoint().getX(), 
                this.getEntryPoint().getY(), 
                this.getExitPoint().getX(), 
                this.getExitPoint().getY()
            );

            // arrow
            Point2D pEndOfArrow = new Point2D.Double(
                (this.getEntryPoint().getX() + kid.getEntryPoint().getX()) / 2.0,
                this.getEntryPoint().getY()
            );
            
            GraphicsHelper.drawArrow(
                screen, 
                ArrowStyle.DEFAULT, 
                ArrowDirection.UP, 
                pEndOfArrow
            );
            
            RepeatRangePayload payload = (RepeatRangePayload)this.getNode().getPayload();
            if(payload.getMinValue() == 0){
            	// draw the connection indicating {0}

                // loop back
                Point2D pLoopEnt = new Point2D.Double(
                    kid.getEntryPoint().getX(),
                    this.getExtent().getY() + RepeatRangeLayoutHelper.getVSpacePixel() / 2.0
                );
                
                Point2D pLoopExit = new Point2D.Double(
                    kid.getExitPoint().getX(),
                    this.getExtent().getY() + RepeatRangeLayoutHelper.getVSpacePixel() / 2.0
                );
                
                GraphicsHelper.drawHorizontalConnectionCurve(
                    screen, 
                    this.getEntryPoint().getX(), 
                    this.getEntryPoint().getY(), 
                    pLoopEnt.getX(), 
                    pLoopEnt.getY()
                );
                
                screen.drawLine(
                    (int)pLoopEnt.getX(),
                    (int)pLoopEnt.getY(),
                    (int)pLoopExit.getX(),
                    (int)pLoopExit.getY()
                );
                
                GraphicsHelper.drawHorizontalConnectionCurve(
                    screen, 
                    this.getExitPoint().getX(), 
                    this.getExitPoint().getY(),
                    pLoopExit.getX(),
                    pLoopExit.getY()
                );

                GraphicsHelper.drawHorizontalConnectionCurve(
                    screen, 
                    this.getEntryPoint().getX(), 
                    this.getEntryPoint().getY(), 
                    this.getExitPoint().getX(), 
                    this.getExitPoint().getY()
                );

                // arrow
                Point2D pEndOfLoopArrow = new Point2D.Double(
                    (this.getExitPoint().getX() + kid.getExitPoint().getX()) / 2.0,
                    this.getExitPoint().getY()
                );
                
                GraphicsHelper.drawArrow(
                    screen, 
                    ArrowStyle.DEFAULT, 
                    ArrowDirection.DOWN, 
                    pEndOfLoopArrow
                );
            }
            
            // draw the text form
            String repeatRange = payload.getDescription();
            Point2D pText = new Point2D.Double(
                kid.getExtent().getMaxX(),
                kid.getExtent().getY() - RepeatRangeLayoutHelper.getVSpacePixel() / 3.0
            );
            
            GraphicsHelper.paintCenteredString(
        		screen, 
        		repeatRange, 
        		pText.getX(), 
        		pText.getY()
    		);
        }
    }
}
