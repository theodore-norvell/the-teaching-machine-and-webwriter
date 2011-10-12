package visreed.extension.javaCC.parser;

import java.util.ArrayList;
import java.util.List;

import visreed.extension.javaCC.model.payload.JavaCCLinkPayload;
import visreed.extension.javaCC.model.payload.JavaCCRootPayload;
import visreed.extension.javaCC.model.payload.ProductionPayload;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedWholeGraph;
import visreed.parser.VisreedBuilder;

public class JavaCCBuilder extends VisreedBuilder {
    
    public JavaCCBuilder(VisreedWholeGraph wg){
        super(wg);
        this.productionPayloads = new ArrayList<ProductionPayload>();
        this.productionNodes = new ArrayList<VisreedNode>();
    }
    
    private List<ProductionPayload> productionPayloads;
    private List<VisreedNode> productionNodes;
    
    /**
     * Build a production node. The production node will be treated as direct leaf
     * of the root.
     * Also adds a link node to the stack. 
     * @param productionName
     * @param numOfChild the child of the production node
     */
    public void buildProduction(ProductionPayload pl, int numOfChild){
    	buildSequence(numOfChild);
    	buildAndPushNodeWithSeq(pl, 1);
    	
    	this.productionPayloads.add(pl);
    	this.productionNodes.add(this.pop());
    	
    	JavaCCLinkPayload linkPl = new JavaCCLinkPayload(pl);
    	buildAndPushNodeWithSeq(linkPl, 0);
    }
    
    public void buildLink(String image){
    	JavaCCLinkPayload linkPl = new JavaCCLinkPayload(image);
    	buildAndPushNodeWithSeq(linkPl, 0);
    }
    
    /* (non-Javadoc)
     * @see visreed.parser.VisreedBuilder#makeRoot()
     */
    @Override
    public void makeRoot() {
    	// at this point, the stack should be empty, and all the productions 
    	// are saved in the productions array.

    	this.stack.clear();
    	this.stack.addAll(productionNodes);
    	int count = getStackSize();
    	
    	// now the stack have all the production nodes, say N = count 
    	
    	for(int i = 0; i < count; i++){
    		VisreedPayload linkPl = new JavaCCLinkPayload(productionPayloads.get(i));
    		buildAndPushNodeWithNoSeq(linkPl, 0);
    	}
    	
    	// now the stack have 2*N nodes, and the last N nodes are Links
    	
    	VisreedPayload rootPl = new JavaCCRootPayload();
    	this.buildAndPushNodeWithNoSeq(rootPl, count);
    	// now the stack have (N+1) nodes, the last one is a root node
    }
}
