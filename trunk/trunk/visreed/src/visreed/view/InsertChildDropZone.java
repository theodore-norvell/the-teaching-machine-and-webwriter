/**
 * InsertChildDropZone.java
 * 
 * @date: Aug 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view;

import higraph.view.NodeView;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import tm.backtrack.BTTimeManager;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.SequencePayload;
import visreed.model.tag.VisreedTag;

/**
 * @author Xiaoyu Guo
 *
 */
public class InsertChildDropZone extends VisreedDropZone {

    /**
     * @param nv
     * @param timeMan
     */
    public InsertChildDropZone(
            NodeView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> nv,
            BTTimeManager timeMan) {
        super(nv, timeMan);
    }

    /**
     * @return
     */
    @Override
    public boolean isDropAccepted(){
        return true;
    }
    
    /* (non-Javadoc)
     * @see visreed.view.VisreedDropZone#handleDrop(visreed.model.RegexNode[])
     */
    @Override
    public void handleDrop(MouseEvent e, List<VisreedNode> nodes){
        if(nodes == null){
            return;
        }
        
        VisreedWholeGraph wg = this.getHigraphView().getHigraph().getWholeGraph();
        wg.reduceSelection();
        
        VisreedNode thisNode = ((VisreedNodeView)this.getAssociatedComponent()).getNode();

        for(VisreedNode n : nodes){
        	// you can not drag a node into its children
        	if(n == thisNode || thisNode.isChildOf(n)){
        		nodes.remove(n);
        	}
        }
        boolean targetIsSeq = false;
        if(thisNode.getPayload().getTag().equals(VisreedTag.SEQUENCE)){
            targetIsSeq = true;
        }
        
        // creates a temporary sequence and handle them all
        
        ArrayList<VisreedNode> seqList = new ArrayList<VisreedNode>();
        ArrayList<VisreedNode> deleteList = new ArrayList<VisreedNode>();
        for(int i = 0; i < nodes.size(); i++){
            VisreedNode child = nodes.get(i);
            if(child == null){
                continue;
            }
            if(child.getPayload().getTag().equals(VisreedTag.SEQUENCE)){
                if(targetIsSeq){
                    // SEQ -> SEQ
                    for(int j = 0; j < child.getNumberOfChildren(); j++){
                        seqList.add(child.getChild(j).duplicate());
                    }
                    if(child.getNumberOfChildren() == 1){
                    	// TODO and the child.parent == KLN
                    	deleteList.add(child.getChild(0));
                    } else {
                    	deleteList.add(child);
                    }
                } else {
                    // nonSEQ -> SEQ
                    seqList.add(child.duplicate());
                    deleteList.add(child);
                }
            } else {
                if(targetIsSeq){
                    // SEQ -> nonSeq
                    seqList.add(child.duplicate());
                    deleteList.add(child);
                } else {
                    // nonSeq -> nonSeq
                    VisreedNode seq = wg.makeRootNode(new SequencePayload());
                    seq.appendChild(child.duplicate());
                    seqList.add(seq);
                    deleteList.add(child);
                }
            }
        }
        // now inserts all the temporary nodes into the position
        int position = this.getNodeNumber();
        for(int i = 0; i < seqList.size(); i++){
        	// handle the situation of the node is already child of the target
            thisNode.insertChild(position + i, seqList.get(i));
        }
        
        for(int i = 0; i < deleteList.size(); i++){
        	VisreedNode parent = deleteList.get(i).getParent();
        	if(parent != null){
        		parent.notifyObservers();
        	}
        	deleteList.get(i).delete();
        }
    }
}
