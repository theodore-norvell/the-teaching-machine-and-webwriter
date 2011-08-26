/*
 * Created on 2009-09-08 by Theodore S. Norvell. 
 */
package regex.view;

import higraph.view.HigraphView;
import higraph.view.NodeView;
import higraph.view.ViewFactory;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import regex.awt.GraphicsHelper;
import regex.model.RegexEdge;
import regex.model.RegexEdgeLabel;
import regex.model.RegexHigraph;
import regex.model.RegexNode;
import regex.model.RegexPayload;
import regex.model.RegexSubgraph;
import regex.model.RegexWholeGraph;
import tm.backtrack.BTTimeManager;

public class RegexHigraphView
extends HigraphView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge>
{
    protected RegexHigraphView(
        ViewFactory<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> vf, 
        RegexHigraph theGraph, 
        Component component,
        BTTimeManager timeMan) {
        super(vf, theGraph, component, timeMan);
        this.positionTable = new HashMap<Rectangle2D, NodeView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge>>();
        Color fill = SystemColor.textHighlight;
        this.marqueeFillColor = new Color(fill.getRed(), fill.getGreen(), fill.getBlue(), 100);
    }
    
    private Color marqueeFillColor;
    private Color marqueeBorderColor = SystemColor.textHighlight.darker();

    /* (non-Javadoc)
     * @see higraph.view.HigraphView#drawArea(java.awt.Graphics2D)
     */
    @Override
    public void drawArea(Graphics2D screen){
        screen = GraphicsHelper.getAntiAliasingGraphic(screen);
        try{
            super.drawArea(screen);
        } catch (NullPointerException ex){
        }
        
        if(this.selectionMarquee != null){
            this.drawSelectionMarque(screen);
        }
    }
    
    private void drawSelectionMarque(Graphics2D screen) {
        if(this.selectionMarquee == null){
            return;
        }
        Color previousColor = screen.getColor();
        
        // fill
        screen.setColor(this.marqueeFillColor);
        screen.fill(this.selectionMarquee);
        
        // border
        screen.setColor(this.marqueeBorderColor);
        screen.draw(this.selectionMarquee);
        
        screen.setColor(previousColor);
    }

    private Map<Rectangle2D, NodeView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge>> positionTable;

    /**
     * After each layout, update the position for each node
     */
    public void updateNodePosition() {
        this.positionTable.clear();
        
        for(RegexNode node : this.getHigraph().getNodes()){
            NodeView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> nodeView = this.getNodeView(node);
            this.positionTable.put(nodeView.getNextShapeExtent(), nodeView);
        }
    }
    
    /* (non-Javadoc)
     * @see higraph.view.HigraphView#refresh()
     */
    @Override
    public void refresh(){
        this.getLayoutManager().layoutLocal(this);
        TopIterator iter = this.getTops();
        while(iter.hasNext()){
            iter.next().doTransition();
        }
    }
    
    /**
     * Selects all the nodes within the specified marquee
     */
    public void selectMarquee(){
        // clear all
        this.getHigraph().getWholeGraph().deSelectAll();
        
        if(this.selectionMarquee == null || this.selectionMarquee.isEmpty()){
            return;
        }
        
        // get all the nodeviews inside the marque
        List<RegexNode> selectedNodes = new ArrayList<RegexNode>();
        for(RegexNode node : this.getHigraph().getNodes()){
            RegexNodeView nodeView = (RegexNodeView)this.getNodeView(node);
            
            if(nodeView.intersects(selectionMarquee)){
                selectedNodes.add(node);
            }
        }
        
        // set these nodeviews selected
        if(selectedNodes.size() > 0){
            // select
            this.getHigraph().getWholeGraph().addToSelection(selectedNodes);
        }
    }
    
    protected Rectangle2D selectionMarquee;
    
    /**
     * Sets the selection Marquee for drawing
     * @param startPoint
     * @param endPoint
     */
    public void setMarquee(Point2D startPoint, Point2D endPoint){
        // normalize the marque, so that start => topleft, end => bottomright
        double startX, endX, startY, endY;
        startX = Math.min(startPoint.getX(), endPoint.getX());
        endX = Math.max(startPoint.getX(), endPoint.getX());
        startY = Math.min(startPoint.getY(), endPoint.getY());
        endY = Math.max(startPoint.getY(), endPoint.getY());
        Rectangle2D marque = new Rectangle(
            (int)startX, 
            (int)startY, 
            (int)(endX - startX), 
            (int)(endY - startY)
        );
        
        this.selectionMarquee = marque;
    }
    
    /**
     * Clears the selection marquee box
     */
    public void clearMarquee(){
        this.selectionMarquee = null;
    }
    
    /**
     * Gets the maximum drawing area containing graphics
     * @return
     */
    public Dimension getDrawingArea(){
        Rectangle2D area = new Rectangle(0,0);
        for(RegexNode node : this.getHigraph().getTops()){
            NodeView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> nodeView = this.getNodeView(node);
            Rectangle.union(
                area, 
                nodeView.getNextShapeExtent(), 
                area
            );
        }
        return new Dimension((int)area.getWidth() + 1, (int)area.getHeight() + 1);
    }
}
