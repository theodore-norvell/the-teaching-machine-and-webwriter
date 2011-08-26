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
import regex.view.layout.KleeneStarLayoutManager;
import regex.view.layout.RegexNodeLayoutManager;
import tm.backtrack.BTTimeManager;

public class KleeneStarNodeView extends RegexNodeView {

	public KleeneStarNodeView(
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
        return KleeneStarLayoutManager.getInstance();
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
                ArrowStyle.DEFAULT, 
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
            extent.getY() + KleeneStarLayoutManager.getVSpaceTop()
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
            extent.getY() + KleeneStarLayoutManager.getVSpaceTop()
        );
    }

}
