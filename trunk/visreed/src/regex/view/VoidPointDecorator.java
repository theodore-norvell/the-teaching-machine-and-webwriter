/**
 * VoidPointDecorator.java
 * 
 * @date: Jul 3, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.view;

import higraph.view.HigraphView;
import higraph.view.PointDecorator;

import java.awt.Graphics2D;

import regex.model.RegexEdge;
import regex.model.RegexEdgeLabel;
import regex.model.RegexHigraph;
import regex.model.RegexNode;
import regex.model.RegexPayload;
import regex.model.RegexSubgraph;
import regex.model.RegexWholeGraph;
import tm.backtrack.BTTimeManager;

/**
 * VoidPointDecorator will do nothing
 * @author Xiaoyu Guo
 */
public class VoidPointDecorator extends RegexPointDecorator {

    /**
     * @param view
     * @param tm
     */
    public VoidPointDecorator(
        HigraphView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> view,
        BTTimeManager tm) {
        super(view, tm);
    }

    /* (non-Javadoc)
     * @see regex.view.RegexPointDecorator#getNextWidth()
     */
    @Override
    public double getNextWidth() {
        return 0;
    }

    /* (non-Javadoc)
     * @see regex.view.RegexPointDecorator#getNextHeight()
     */
    @Override
    public double getNextHeight() {
        return 0;
    }

    /* (non-Javadoc)
     * @see regex.view.RegexPointDecorator#makeDecorator(higraph.view.PointDecorator, higraph.view.HigraphView, tm.backtrack.BTTimeManager)
     */
    @Override
    public
        PointDecorator<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge>
        makeDecorator(
            PointDecorator<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> pd,
            HigraphView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> view,
            BTTimeManager tm) {
        return new VoidPointDecorator(view, tm);
    }

    /* (non-Javadoc)
     * @see regex.view.RegexPointDecorator#draw(java.awt.Graphics2D)
     */
    @Override
    public void draw(Graphics2D screen) {
        // do nothing
    }

}
