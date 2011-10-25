/**
 * FunctionCallPayload.java
 * 
 * @date: Oct 25, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.payload;

import java.util.ArrayList;
import java.util.List;

import visreed.extension.javaCC.parser.JavaCCBuilder;
import visreed.extension.javaCC.parser.Token;

/**
 * @author Xiaoyu Guo
 *
 */
public class FunctionCallPayload extends JavaCCLinkPayload {

	/**
	 * @param productionName
	 */
	public FunctionCallPayload(String productionName) {
		super(productionName);
		this.parameters = new ArrayList<Token>();
		this.returnVariableName = "";
	}

	private List<Token> parameters;
	private String returnVariableName;
	
	/* (non-Javadoc)
	 * @see visreed.extension.javaCC.model.payload.JavaCCLinkPayload#dump(java.lang.StringBuffer, int)
	 */
	@Override
    public StringBuffer dump(StringBuffer sb, int indentLevel) {
    	sb = JavaCCBuilder.dumpPrefix(sb, indentLevel);
    	
    	if(this.returnVariableName.length() > 0){
    		sb.append(returnVariableName);
    		sb.append(" = ");
    	}
    	
    	sb.append(this.productionName);
    	
    	sb.append("(");
    	if(parameters.size() > 0){
    		JavaCCBuilder.dumpJavaCode(
				sb, 
				0, 
				parameters.get(0), 
				parameters.get(parameters.size()-1)
			);
    	}
    	sb.append(")");
    	return sb;
    }

	public List<Token> getParameters() {
		return parameters;
	}

	public String getReturnVariableName() {
		return returnVariableName;
	}

	public void setReturnVariableName(String returnVariableName) {
		this.returnVariableName = returnVariableName;
	}
}
