/**
 * VisreedPointDecorator.java
 * 
 * @date: Jul 3, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view;

import higraph.view.HigraphView;
import higraph.view.PointDecorator;

import java.awt.Graphics2D;

import tm.backtrack.BTTimeManager;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.VisreedPayload;

/**
 * VisreedPointDecorator is the base class for handling {@link higraph.view.PointDecorator}s.
 * Child classes should handle arrowheads
 * @author Xiaoyu Guo
 */
public abstract class VisreedPointDecorator extends
    PointDecorator<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> {

    /**
     * @param view
     * @param tm
     */
    protected VisreedPointDecorator(
        HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> view,
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
        PointDecorator<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>
        makeDecorator(
            PointDecorator<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> pd,
            HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> view,
            BTTimeManager tm);

    /* (non-Javadoc)
     * @see higraph.view.PointDecorator#draw(java.awt.Graphics2D)
     */
    @Override
    public abstract void draw(Graphics2D screen);

}
