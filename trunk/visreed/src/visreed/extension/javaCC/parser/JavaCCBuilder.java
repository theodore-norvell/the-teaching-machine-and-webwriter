package visreed.extension.javaCC.parser;

import visreed.extension.javaCC.model.payload.JavaCodeProductionPayload;
import visreed.extension.javaCC.model.payload.ProductionPayload;
import visreed.model.VisreedNode;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.SequencePayload;
import visreed.parser.VisreedBuilder;

public class JavaCCBuilder extends VisreedBuilder {
    
    public JavaCCBuilder(VisreedWholeGraph wg){
        super(wg);
    }
    
    /**
     * @param productionName
     */
    public void buildProduction(String productionName){
    	buildAndPushNodeWithNoSeq(new ProductionPayload(productionName), 1);
    }
    
    public void buildJavaCode(String image) {
        buildAndPushNodeWithNoSeq(new JavaCodeProductionPayload(image), 0);
    }
    
    /* (non-Javadoc)
     * @see visreed.parser.VisreedBuilder#makeRoot()
     */
    @Override
    public void makeRoot() {
        VisreedNode root = this.peak();
        if(root.getPayload() instanceof SequencePayload){
            // do nothing
        } else {
            // create a big SEQ that contains every node in the stack
            this.buildSequence(this.getStackSize());
        }
    }
}
