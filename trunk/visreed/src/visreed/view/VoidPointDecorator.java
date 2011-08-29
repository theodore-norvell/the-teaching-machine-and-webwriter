/**
 * VoidPointDecorator.java
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
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;

/**
 * VoidPointDecorator will do nothing
 * @author Xiaoyu Guo
 */
public class VoidPointDecorator extends VisreedPointDecorator {

    /**
     * @param view
     * @param tm
     */
    public VoidPointDecorator(
        HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> view,
        BTTimeManager tm) {
        super(view, tm);
    }

    /* (non-Javadoc)
     * @see visreed.view.VisreedPointDecorator#getNextWidth()
     */
    @Override
    public double getNextWidth() {
        return 0;
    }

    /* (non-Javadoc)
     * @see visreed.view.VisreedPointDecorator#getNextHeight()
     */
    @Override
    public double getNextHeight() {
        return 0;
    }

    /* (non-Javadoc)
     * @see visreed.view.VisreedPointDecorator#makeDecorator(higraph.view.PointDecorator, higraph.view.HigraphView, tm.backtrack.BTTimeManager)
     */
    @Override
    public
        PointDecorator<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge>
        makeDecorator(
            PointDecorator<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> pd,
            HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> view,
            BTTimeManager tm) {
        return new VoidPointDecorator(view, tm);
    }

    /* (non-Javadoc)
     * @see visreed.view.VisreedPointDecorator#draw(java.awt.Graphics2D)
     */
    @Override
    public void draw(Graphics2D screen) {
        // do nothing
    }

}
