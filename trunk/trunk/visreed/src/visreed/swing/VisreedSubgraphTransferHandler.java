/**
 * VisreedSubgraphTransferHandler.java
 * 
 * @date: Jul 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing;

import higraph.swing.SubgraphTransferHandler;
import higraph.view.HigraphView;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import visreed.awt.VisreedSubgraphMouseAdapter;
import visreed.extension.regex.swing.RegexJList;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.view.VisreedHigraphView;

/**
 * The transfer handler is associated with {@link HigraphView}s.
 * @author Xiaoyu Guo
 */
public class VisreedSubgraphTransferHandler
extends SubgraphTransferHandler<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> 
{

    private static final long serialVersionUID = 6051659623878408438L;

    /**
     * 
     */
    public VisreedSubgraphTransferHandler(
        VisreedSubgraphMouseAdapter adapter
    ) {
        super(adapter);
    }
    
    @Override
    public boolean canImport(TransferHandler.TransferSupport support){
        return super.canImport(support);
    }

    @Override
    public int getSourceActions(JComponent comp) {
        if(comp instanceof RegexJList){
            return COPY;
        } else {
            return super.getSourceActions(comp);
        }
    }
    
    @Override
    protected Transferable createTransferable(JComponent container){
        if(container instanceof VisreedJComponent){
            VisreedHigraphView view = ((VisreedJComponent)container).getSubgraphView();
            return new VisreedHigraphTransferObject(view.getHigraph().getWholeGraph());
        } else {
            return super.createTransferable(container);
        }
    }
}
