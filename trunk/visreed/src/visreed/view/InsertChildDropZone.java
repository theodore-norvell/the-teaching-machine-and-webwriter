/**
 * InsertChildDropZone.java
 * 
 * @date: Aug 7, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.view;

import higraph.view.NodeView;

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
    public void handleDrop(List<VisreedNode> nodes){
        if(nodes == null){
            return;
        }
        
        VisreedWholeGraph wg = this.getHigraphView().getHigraph().getWholeGraph();
        
        VisreedNode thisNode = ((VisreedNodeView)this.getAssociatedComponent()).getNode();
        
        boolean parentIsSeq = false;
        if(thisNode.getPayload().getTag() == VisreedTag.SEQUENCE){
            parentIsSeq = true;
        }
        
        int numChildren = nodes.size();
        // creates a temporary sequence and handle them all
        
        ArrayList<VisreedNode> seqList = new ArrayList<VisreedNode>();
        for(int i = 0; i < numChildren; i++){
            VisreedNode child = nodes.get(i);
            if(child == null){
                continue;
            }
            if(child.getPayload().getTag().equals(VisreedTag.SEQUENCE)){
                if(parentIsSeq){
                    // SEQ -> SEQ
                    for(int j = 0; j < child.getNumberOfChildren(); j++){
                        seqList.add(child.getChild(j));
                    }
                } else {
                    // nonSEQ -> SEQ
                    seqList.add(child);
                }
            } else {
                if(parentIsSeq){
                    // SEQ -> nonSeq
                    seqList.add(child);
                } else {
                    // nonSeq -> nonSeq
                	VisreedNode parent = child.getParent();
                	if(parent != null && parent.getNumberOfChildren() == 1){
                		if(parent.getPayload().getTag().equals(VisreedTag.SEQUENCE)){
                			seqList.add(parent);
                		}
                	} else {
	                    VisreedNode seq = wg.makeRootNode(new SequencePayload());
	                    seq.appendChild(child);
	                    seqList.add(seq);
                	}
                }
            }
        }
        // now inserts all the temporary nodes into the position
        int position = this.getNodeNumber();
        for(int i = 0; i < seqList.size(); i++){
        	//*/
        	// handle the situation of the node is already child of the target
        	VisreedNode temp = seqList.get(i).duplicate();
            thisNode.insertChild(position + i, temp);
            seqList.get(i).delete();
        	
        	/*/
            seqList.get(i).detach();
            thisNode.insertChild(position + i, seqList.get(i));
            //*/
        }
    }
}
