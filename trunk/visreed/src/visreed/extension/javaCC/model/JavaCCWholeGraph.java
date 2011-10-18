/**
 * JavaCCWholeGraph.java
 * 
 * @date: Sep 13, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model;

import java.io.StringReader;
import java.util.List;

import tm.backtrack.BTTimeManager;
import visreed.extension.javaCC.parser.JavaCCParser;
import visreed.extension.javaCC.parser.ParseException;
import visreed.extension.javaCC.parser.Token;
import visreed.model.VisreedNode;
import visreed.model.VisreedWholeGraph;

/**
 * @author Xiaoyu Guo
 */
public class JavaCCWholeGraph extends VisreedWholeGraph {

	public JavaCCWholeGraph(BTTimeManager timeMan) {
		super(timeMan);
		this.parserName = "";
		this.options = new JavaCCOptions();
		this.compilationUnit = "";
		this.productionManager = new ProductionManager();
	}
	
	private ProductionManager productionManager;
	private String parserName;
	private JavaCCOptions options;
	private String compilationUnit;
	private List<Token> tokenManagerDeclarations;
	
    /**
     * Construct a tree structure from a given regular expression
     * @param wg the WholeGraph
     * @param regexp  the regular expression
     * @return the root node of the node tree
     * @throws ParseException 
     */
    public static VisreedNode construct(
    	JavaCCWholeGraph wg,
        String regexp
    ) throws ParseException{
        StringReader reader = new StringReader(regexp);
        BTTimeManager timeMan = new BTTimeManager();
        if(wg == null){
            wg = new JavaCCWholeGraph(timeMan);
        } else {
        	for(VisreedNode n : wg.getTops()){
        		n.delete();
        	}
        }
        
        VisreedNode result = null;
        result = JavaCCParser.parse(wg, reader);
        return result;
    }
	
	/**
	 * @return the productionManager
	 */
	public ProductionManager getProductionManager() {
		return productionManager;
	}

	public String getParserName(){
		return this.parserName;
	}
	public void setParserName(String value){
		this.parserName = value;
	}
	
	public JavaCCOptions getOptions(){
		return this.options;
	}
	
	public String getCompilationUnit(){
		return this.compilationUnit;
	}
	
	public void setCompilationUnit(String value){
		this.compilationUnit = value;
	}

	public List<Token> getTokenManagerDeclarations() {
		return tokenManagerDeclarations;
	}

	public void setTokenManagerDeclarations(List<Token> tokenManagerDeclarations) {
		this.tokenManagerDeclarations = tokenManagerDeclarations;
	}
	
}
