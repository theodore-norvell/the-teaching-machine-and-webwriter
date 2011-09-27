/**
 * JavaCCWholeGraph.java
 * 
 * @date: Sep 13, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model;

import tm.backtrack.BTTimeManager;
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
	/**
	 * @return the productionManager
	 */
	public ProductionManager getProductionManager() {
		return productionManager;
	}

	private String parserName;
	public String getParserName(){
		return this.parserName;
	}
	public void setParserName(String value){
		this.parserName = value;
	}
	
	private JavaCCOptions options;
	public JavaCCOptions getOptions(){
		return this.options;
	}
	
	private String compilationUnit;
	public String getCompilationUnit(){
		return this.compilationUnit;
	}
	
	public void setCompilationUnit(String value){
		this.compilationUnit = value;
	}
	
}
