/**
 * LexicalAlternationPayload.java
 * 
 * @date: Oct 31, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.javaCC.model.payload;

import visreed.extension.javaCC.model.tag.JavaCCTag;
import visreed.extension.javaCC.parser.JavaCCBuilder;
import visreed.model.payload.AlternationPayload;

/**
 * LexicalAlternation Payload is the ALT for lexical grammars. <br />
 * No brackets () will be generated in dump().
 * @author Xiaoyu Guo
 */
public class LexicalAlternationPayload extends AlternationPayload {
	public LexicalAlternationPayload() {
		super(JavaCCTag.LEXICAL_ALTERNATION);
	}
	
	/* (non-Javadoc)
	 * @see visreed.model.payload.VisreedPayload#dump(java.lang.StringBuffer, int)
	 */
	@Override
	public StringBuffer dump(StringBuffer sb, int indentLevel) {
		sb = JavaCCBuilder.dumpPrefix(sb, 0);
		int numOfChildren = this.getNode().getNumberOfChildren();
		
		for(int i = 0; i < numOfChildren; i++){
			JavaCCBuilder.dumpPrefix(sb, indentLevel);
			if (i == 0){
				sb.append("  ");
			} else {
				sb.append("| ");
			}
			this.getNode().getChild(i).getPayload().dump(sb, 0);
			sb.append("\n");
		}
		return sb;
	}
}
