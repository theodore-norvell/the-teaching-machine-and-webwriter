/**
 * RegexPointDecorator.java
 * 
 * @date: Jul 3, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.view;

import java.awt.Graphics2D;

import regex.model.*;
import tm.backtrack.BTTimeManager;
import higraph.view.HigraphView;
import higraph.view.PointDecorator;

/**
 * RegexPointDecorator is the base class for handling {@link higraph.view.PointDecorator}s.
 * Child classes should handle arrowheads
 * @author Xiaoyu Guo
 */
public abstract class RegexPointDecorator extends
    PointDecorator<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> {

    /**
     * @param view
     * @param tm
     */
    protected RegexPointDecorator(
        HigraphView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> view,
        BTTimeManager tm) {
        super(view, tm);
    }

    /* (non-Javadoc)
     * @see higraph.view.interfaces.Layable#getNextCenterX()
     */
    @Override
    public double getNextCenterX() {
        return this.getNextWidth() / 2.0;
    }

    /* (non-Javadoc)
     * @see higraph.view.interfaces.Layable#getNextCenterY()
     */
    @Override
    public double getNextCenterY() {
        return this.getNextHeight() / 2.0;
    }

    /* (non-Javadoc)
     * @see higraph.view.interfaces.Layable#getNextWidth()
     */
    @Override
    public abstract double getNextWidth();

    /* (non-Javadoc)
     * @see higraph.view.interfaces.Layable#getNextHeight()
     */
    @Override
    public abstract double getNextHeight();

    /* (non-Javadoc)
     * @see higraph.view.PointDecorator#makeDecorator(higraph.view.PointDecorator, higraph.view.HigraphView, tm.backtrack.BTTimeManager)
     */
    @Override
    public abstract
        PointDecorator<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge>
        makeDecorator(
            PointDecorator<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> pd,
            HigraphView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> view,
            BTTimeManager tm);

    /* (non-Javadoc)
     * @see higraph.view.PointDecorator#draw(java.awt.Graphics2D)
     */
    @Override
    public abstract void draw(Graphics2D screen);

}
