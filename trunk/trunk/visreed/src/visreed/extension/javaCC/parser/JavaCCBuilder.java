package visreed.extension.javaCC.parser;

import java.util.List;

import visreed.extension.javaCC.model.JavaCCWholeGraph;
import visreed.extension.javaCC.model.payload.JavaCCLinkPayload;
import visreed.extension.javaCC.model.payload.JavaCCRootPayload;
import visreed.extension.javaCC.model.payload.JavaCodeBlockPayload;
import visreed.extension.javaCC.model.payload.LexicalAlternationPayload;
import visreed.extension.javaCC.model.payload.LookAheadPayload;
import visreed.extension.javaCC.model.payload.ProductionPayload;
import visreed.extension.javaCC.model.payload.RegexpSpecPayload;
import visreed.model.VisreedNode;
import visreed.model.payload.SequencePayload;
import visreed.model.payload.VisreedPayload;
import visreed.model.tag.VisreedTag;
import visreed.parser.VisreedBuilder;

public class JavaCCBuilder extends VisreedBuilder {
    
	protected JavaCCWholeGraph wholeGraph;
	
    public JavaCCBuilder(JavaCCWholeGraph wg){
        super(wg);
        this.wholeGraph = wg;
    }
    
    public void setCompilationUnit(Token first, Token last){
    	StringBuffer sb = dumpJavaCode(first, last);
        this.wholeGraph.setCompilationUnit(sb.toString());
    }
    
    /**
     * Generates well-formed Java Code by beginning token and last token
     * @param sb
     * @param indentLevel
     * @param first
     * @param last
     * @return
     */
    public static StringBuffer dumpJavaCode(
		StringBuffer sb,
		int indentLevel,
		Token first, 
		Token last
	){
    	if(sb == null){
    		sb = new StringBuffer();
    	}

        while (first != last) {
        	String image = first.image;
            sb.append(image);
        	int length = sb.length();
            boolean lBracket = image.equals("{");
            boolean rBracket = image.equals("}");
            boolean newLine = image.equals(";") || lBracket || rBracket;
            if(newLine){
            	if(lBracket){
            		indentLevel ++;
            	} else if(rBracket && sb.substring(length - 5).equals("    }")){
            		// cancel the current indentation by one level 
            		sb.delete(length - 5, length - 1);
            		indentLevel --;
            	} else if (!lBracket && !rBracket){
                	if(sb.charAt(length - 2) == ' '){
                		sb.delete(length - 2, length - 1);
                	}
            	}
            	sb.append("\n");
            	dumpPrefix(sb, indentLevel);
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
//        sb.append(last.image);
        
        if(sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n'){
        	sb.deleteCharAt(sb.length() - 1);
        }
		return sb;
    }

	/**
	 * Generates well-formed Java Code by beginning token and last token
	 * @param first
	 * @param last
	 * @return
	 */
	public StringBuffer dumpJavaCode(Token first, Token last) {
		return dumpJavaCode(null, 0, first, last);
	}
	
	public StringBuffer dumpJavaCode(List<Token> tokenList){
		if(tokenList == null || tokenList.size() == 0){
			return new StringBuffer();
		}
		return dumpJavaCode(null, 0, tokenList.get(0), tokenList.get(tokenList.size() - 1));
	}
    
    /**
     * Build a production node. The production node will be treated as a
     * direct leaf of the root with all children specified. 
     * Also adds a link node to the stack. 
     * @param productionName
     * @param numOfChild the child of the production node
     */
    public void buildProduction(ProductionPayload pl, int numOfChild){
    	buildSequence(numOfChild);
    	buildAndPushNodeWithSeq(pl, 1);
    	this.pop();
    	
    	JavaCCLinkPayload linkPl = new JavaCCLinkPayload(pl);
    	buildAndPushNodeWithSeq(linkPl, 0);
    }
    
    /**
     * Build a lexical Alternation node.
     * @param numOfChildren
     */
    public void buildLexicalAlternation(int numOfChildren){
    	// alternation is special, as it comes with 2 empty sequences.
        if(this.getStackSize() < numOfChildren){
            return;
        }
        VisreedPayload payload = new LexicalAlternationPayload();
        VisreedNode node = this.wholeGraph.makeRootNode(payload);
        for(int i = 0; i < numOfChildren; i++){
            VisreedNode kid = this.pop();
            VisreedNode toInsert = null;
            if(kid.getPayload().getTag().equals(VisreedTag.SEQUENCE)){
            	// kid == seq
                toInsert = kid;
            } else {
            	// kid != seq
                VisreedNode seq = this.wholeGraph.makeRootNode(new SequencePayload());
                seq.insertChild(0, kid);
                toInsert = seq;
            }
            
            if(i == numOfChildren - 1 || i == numOfChildren - 2){
            	node.getChild(numOfChildren - i - 1).replace(toInsert);
            } else {
            	node.insertChild(2, toInsert);
            }
        }
        this.push(node);
    }
    
    /**
     * Build a Link node with given name.
     * @param image
     */
    public void buildLink(String image){
    	JavaCCLinkPayload linkPl = new JavaCCLinkPayload(image);
    	buildAndPushNodeWithSeq(linkPl, 0);
    }
    
    /**
     * Build a regular expression specification node.
     * @param payload
     */
    public void buildRegexpSpec(RegexpSpecPayload payload){
    	buildAndPushNodeWithSeq(payload, 1);
    }
    
    public void buildLookAhead(){
    	buildAndPushNodeWithSeq(new LookAheadPayload(), 1);
    }
    
    public void buildJavaCode(String code){
    	JavaCodeBlockPayload pl = new JavaCodeBlockPayload(code);
    	buildAndPushNodeWithSeq(pl, 0);
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
