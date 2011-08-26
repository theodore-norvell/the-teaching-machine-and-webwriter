package regex.view;

import higraph.view.HigraphView;

import java.awt.Graphics2D;

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
import regex.view.layout.RegexNodeLayoutManager;
import regex.view.layout.AlternationLayoutManager;
import tm.backtrack.BTTimeManager;

/**
 * AlternationNodeView represents the alternation node.
 * @author Xiaoyu Guo
 */
public class AlternationNodeView extends RegexNodeView {

	public AlternationNodeView(
			HigraphView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> v,
			RegexNode node, 
			BTTimeManager timeMan) {
		super(v, node, timeMan);
	}

	@Override
	protected void drawNode(Graphics2D screen) {
	    screen.setColor(this.getColor());
	    
	    for(int i = 0; i < this.getNumChildren(); i++){
            RegexNodeView child = this.getRegexChild(i);
            if(child == null){
                continue;
            }
            
	        // lines connecting the entry point and each of its children
	        GraphicsHelper.drawHorizontalConnectionCurve(
	            screen,
                this.getEntryPoint().getX(),
                this.getEntryPoint().getY(),
                child.getEntryPoint().getX(),
                child.getEntryPoint().getY()
            );
	        if(this.getCurrentDirection().equals(Direction.EAST)){
    	        GraphicsHelper.drawArrow(
    	            screen, 
    	            ArrowStyle.DEFAULT, 
    	            ArrowDirection.EAST, 
    	            child.getEntryPoint()
                );
	        } else if(this.getCurrentDirection().equals(Direction.WEST)){
                GraphicsHelper.drawArrow(
                    screen, 
                    ArrowStyle.DEFAULT, 
                    ArrowDirection.WEST, 
                    child.getExitPoint()
                );
	        }
            
	        // joining line at the exit point
	        GraphicsHelper.drawHorizontalConnectionCurve(
	            screen,
                this.getExitPoint().getX(),
                this.getExitPoint().getY(),
                child.getExitPoint().getX(),
                child.getExitPoint().getY()
            );
	    }
        
	    /*
	    // outgoing arrow
        GraphicsHelper.drawArrow(
            screen, 
            ArrowStyle.DEFAULT, 
            0, 
            this.getExitPoint()
        );
        //*/
	}

    /* (non-Javadoc)
     * @see regex.view.RegexNodeView#getLayoutHelper()
     */
    @Override
    protected RegexNodeLayoutManager getLayoutHelper() {
        return AlternationLayoutManager.getInstance();
    }
    
    /**
     * Handling DropZones
     * Rules for id: 
     * <li>"head"</li>
     * <li>"node_1"</li>
     * <li>"node_2"</li>
     */
    public static final String ID_DROPZONE_HEAD = "head";
    public static final String ID_PREFIX_DROPZONE_NODE = "node_";
    
    @Override
    protected boolean shouldRefreshDropZone(){
        // the HEAD and one after each children
        boolean shouldRefresh = (this.zones.size() != this.getNumChildren() + 1);
        shouldRefresh |= (this.findZone(ID_DROPZONE_HEAD) == null);

        if(shouldRefresh == false){
            for(int i = 0; i < this.getNumChildren(); i++){
                shouldRefresh |= (this.findZone(ID_PREFIX_DROPZONE_NODE + (i+1)) == null);
            }
        }
        return shouldRefresh;
    }
    
    @Override
    public void reCreateDropZone(){
        // For simplicity here we just wipe everything out and build them again.
        this.removeZones();
        RegexDropZone zone = null;
        zone = this.makeAndAddInsertChildDropZone(ID_DROPZONE_HEAD, 0);
        for(int i = 0; i < this.getNumChildren(); i++){
            zone = this.makeAndAddInsertChildDropZone(ID_PREFIX_DROPZONE_NODE + (i+1), i+1);
        }
    }
}
