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
import regex.view.layout.KleenePlusLayoutManager;
import regex.view.layout.RegexNodeLayoutManager;
import tm.backtrack.BTTimeManager;

public class KleenePlusNodeView extends RegexNodeView {

	public KleenePlusNodeView(
			HigraphView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> v,
			RegexNode node,
			BTTimeManager timeMan) {
		super(v, node, timeMan);
	}

    /* (non-Javadoc)
     * @see regex.view.RegexNodeView#drawNode(java.awt.Graphics2D)
     */
    @Override
    protected void drawNode(Graphics2D screen) {
        RegexNodeView kid = this.getRegexChild(0);
        
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
                ArrowStyle.DEFAULT, 
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
     * @see regex.view.RegexNodeView#getLayoutHelper()
     */
    @Override
    protected RegexNodeLayoutManager getLayoutHelper() {
        return KleenePlusLayoutManager.getInstance();
    }

    /* (non-Javadoc)
     * @see regex.view.RegexNodeView#getEntryPoint()
     */
    @Override
    public Point2D getEntryPoint(){
        Rectangle2D extent = this.getNextShapeExtent();
        
        RegexNodeView kid = this.getRegexChild(0);
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
     * @see regex.view.RegexNodeView#getExitPoint()
     */
    @Override
    public Point2D getExitPoint(){
        Rectangle2D extent = this.getNextShapeExtent();
        
        RegexNodeView kid = this.getRegexChild(0);
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
