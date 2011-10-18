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
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.VisreedPayload;
import visreed.view.layout.KleenePlusLayoutManager;
import visreed.view.layout.VisreedNodeLayoutManager;

public class KleenePlusNodeView extends VisreedNodeView {

	public KleenePlusNodeView(
			HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> v,
			VisreedNode node,
			BTTimeManager timeMan) {
		super(v, node, timeMan);
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
        
        if(kid != null){
            // entry -> kid entry
            screen.drawLine(
                (int)this.getEntryPoint().getX(),
                (int)this.getEntryPoint().getY(),
                (int)kid.getEntryPoint().getX(),
                (int)kid.getEntryPoint().getY()
            );
            
            GraphicsHelper.drawArrow(
                screen, 
                ArrowStyle.ARROW_AT_THE_BACK, 
                0, 
                kid.getEntryPoint()
            );

            // kid exit -> exit
            screen.drawLine(
                (int)kid.getExitPoint().getX(),
                (int)kid.getExitPoint().getY(),
                (int)this.getExitPoint().getX(),
                (int)this.getExitPoint().getY()
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
            
            // loop back
            Point2D pKidEnt = new Point2D.Double(
                kid.getEntryPoint().getX(),
                this.getExtent().getMaxY() - KleenePlusLayoutManager.getVSpaceLoopBottom()
            );
            
            Point2D pKidExit = new Point2D.Double(
                kid.getExitPoint().getX(),
                this.getExtent().getMaxY() - KleenePlusLayoutManager.getVSpaceLoopBottom()
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
        }
    }

    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#getLayoutHelper()
     */
    @Override
    protected VisreedNodeLayoutManager getLayoutHelper() {
        return KleenePlusLayoutManager.getInstance();
    }

    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#getEntryPoint()
     */
    @Override
    public Point2D getEntryPoint(){
        Rectangle2D extent = this.getNextShapeExtent();
        
        VisreedNodeView kid = this.getVisreedChild(0);
        double kidEntryPointY = 0.0;
        if(kid != null){
            kidEntryPointY = kid.getEntryPoint().getY();
        }
        double entryX = extent.getX();
        if(this.getCurrentDirection().equals(Direction.WEST)){
            entryX = extent.getMaxX();
        }
        
        return new Point2D.Double(
            entryX, 
            kidEntryPointY + KleenePlusLayoutManager.getVSpaceTop()
        );
    }
    
    /* (non-Javadoc)
     * @see visreed.view.VisreedNodeView#getExitPoint()
     */
    @Override
    public Point2D getExitPoint(){
        Rectangle2D extent = this.getNextShapeExtent();
        
        VisreedNodeView kid = this.getVisreedChild(0);
        double kidExitPointY = 0.0;
        if(kid != null){
            kidExitPointY = kid.getEntryPoint().getY();
        }
        double exitX = extent.getMaxX();
        if(this.getCurrentDirection().equals(Direction.WEST)){
            exitX = extent.getX();
        }
        return new Point2D.Double(
            exitX, 
            kidExitPointY + KleenePlusLayoutManager.getVSpaceTop()
        );
    }
}
