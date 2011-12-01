package visreed.extension.javaCC.parser;

import java.util.List;

import visreed.extension.javaCC.model.JavaCCWholeGraph;
import visreed.extension.javaCC.model.payload.*;
import visreed.extension.javaCC.model.tag.JavaCCTag;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedTag;
import visreed.model.payload.TerminalPayload;
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
    	// subgraph automatically created in JavaCCWholeGraph.addChildrenHook()
    	buildNode(pl, numOfChild);
    	VisreedNode pNode = this.pop();
    	
    	if(pNode.getTag().equals(JavaCCTag.REGULAR_PRODUCTION)){
    		// search any children for RegexpSpec and attach the pNode with that
    		List<VisreedNode> specs = pNode.searchForTag(JavaCCTag.REGEXP_SPEC);
    		if(specs != null){
    			for(VisreedNode spec : specs){
    				((RegexpSpecPayload)spec.getPayload()).setProductionName(pl.getName());
    			}
    		}
    	}
    	
    	GrammarLinkPayload linkPl = new GrammarLinkPayload(pl.getName());
    	buildNode(linkPl, 0);
    }
    
    /**
     * Build a lexical Alternation node.
     * @param numOfChildren
     */
    public void buildAlternation(VisreedTag tag, int numOfChildren){
    	// alternation is special, as it comes with 2 empty sequences.
        if(this.getStackSize() < numOfChildren){
            return;
        }
        VisreedPayload payload = tag.defaultPayload();
        VisreedNode node = this.wholeGraph.makeRootNode(payload);
        for(int i = 0; i < numOfChildren; i++){
            VisreedNode kid = this.pop();
            
            if(i == numOfChildren - 1 || i == numOfChildren - 2){
            	node.getChild(numOfChildren - i - 1).replace(kid);
            } else {
            	node.insertChild(2, kid);
            }
        }
        this.push(node);
    }
    
    /**
     * Build a Link node with given name.
     * @param image
     */
    public void buildLink(VisreedTag tag, String image){
    	LexicalLinkPayload linkPl = null;
    	// hack: GrammarLink inherits from LexicalLink
    	if(tag.equals(JavaCCTag.GRAMMAR_LINK)){
    		linkPl = new GrammarLinkPayload(image);
    	} else {
    		linkPl = new LexicalLinkPayload(image);
    	}
    	buildNode(linkPl, 0);
    }
    
    /**
     * Build a regular expression specification node.
     * @param payload
     */
    @Deprecated
    public void buildRegexpSpec(RegexpSpecPayload payload){
    	buildNode(payload, 1);
    }
    
    @Deprecated
    public void buildLookAhead(){
    	buildNode(new LookAheadPayload(), 1);
    }
    
    public void buildJavaCode(String code){
    	JavaCodeBlockPayload pl = new JavaCodeBlockPayload(code);
    	buildNode(pl, 0);
    }
    
    public void buildTerminal(VisreedTag tag, String image){
    	buildNode(new TerminalPayload(tag, image), 0);
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
    	this.buildNode(rootPl, count);
    	
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
