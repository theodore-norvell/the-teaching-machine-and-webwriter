package visreed.extension.javaCC.parser;

import java.util.ArrayList;
import java.util.List;

import visreed.extension.javaCC.model.JavaCCWholeGraph;
import visreed.extension.javaCC.model.payload.JavaCCLinkPayload;
import visreed.extension.javaCC.model.payload.JavaCCRootPayload;
import visreed.extension.javaCC.model.payload.ProductionPayload;
import visreed.model.VisreedNode;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.VisreedPayload;
import visreed.parser.VisreedBuilder;

public class JavaCCBuilder extends VisreedBuilder {
    
    public JavaCCBuilder(VisreedWholeGraph wg){
        super(wg);
//        this.productionPayloads = new ArrayList<ProductionPayload>();
//        this.productionNodes = new ArrayList<VisreedNode>();
    }
    
//    private List<ProductionPayload> productionPayloads;
    private List<VisreedNode> productionNodes;
    
    public void setCompilationUnit(Token first, Token last){
    	StringBuffer sb = dumpJavaCode(first, last);
        ((JavaCCWholeGraph)this.wholeGraph).setCompilationUnit(sb.toString());
    }

	/**
	 * Generates well-formed Java Code by beginning token and last token
	 * @param first
	 * @param last
	 * @return
	 */
	public StringBuffer dumpJavaCode(Token first, Token last) {
		StringBuffer sb = new StringBuffer();
    	int currentIdentLevel = 0;
        while (first != last) {
        	String image = first.image;
            sb.append(image);
        	int length = sb.length();
            boolean lBracket = image.equals("{");
            boolean rBracket = image.equals("}");
            boolean newLine = image.equals(";") || lBracket || rBracket;
            if(newLine){
            	if(lBracket){
            		currentIdentLevel ++;
            	} else if(rBracket && sb.substring(length - 5).equals("    }")){
            		// cancel the current identation by one level 
            		sb.delete(length - 5, length - 1);
            		currentIdentLevel --;
            	} else if (!lBracket && !rBracket){
                	if(sb.charAt(length - 2) == ' '){
                		sb.delete(length - 2, length - 1);
                	}
            	}
            	sb.append("\n");
            	dumpPrefix(sb, currentIdentLevel);
            } else if (image.matches("^[;\\.\\(\\)\\[\\]]$")){
            	if(sb.charAt(length - 2) == ' '){
            		sb.delete(length - 2, length - 1);
            	}
            	if(image.equals("]")){
            		sb.append(" ");
            	}
            } else {
            	sb.append(" ");
            }
            first = first.next;
        }
		return sb;
	}
    
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
    	this.pop();
    	
//    	this.productionPayloads.add(pl);
//    	this.productionNodes.add(this.pop());
    	
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
    	/* at this point, the stack should contain all Link for all unused 
    	 * (root layer) productions, and all the productions are saved in 
    	 * the productions array. say count = N = (number of all productions) 
    	 */

    	int count = getStackSize();
    	
    	VisreedPayload rootPl = new JavaCCRootPayload((JavaCCWholeGraph) this.wholeGraph);
    	this.buildAndPushNodeWithNoSeq(rootPl, count);
    	
    	// now the stack have only one node (root), which contains N link children 
    }
    
    public static StringBuffer dumpPrefix(StringBuffer sb, int indentLevel){
    	if(sb == null){
    		sb = new StringBuffer();
    	}
        for (int i = 0; i < indentLevel; i++){
            sb.append("    ");
        }
    	return sb;
    }
}
