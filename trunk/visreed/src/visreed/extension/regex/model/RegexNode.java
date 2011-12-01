/**
 * RegexNode.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.regex.model;

import java.io.StringReader;

import tm.backtrack.BTTimeManager;
import visreed.extension.regex.parser.ParseException;
import visreed.extension.regex.parser.RegexParser;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedWholeGraph;

/**
 * @author Xiaoyu Guo
 */
public class RegexNode extends VisreedNode {

    protected RegexNode(VisreedWholeGraph higraph, VisreedPayload payload) {
        super(higraph, payload);
    }

    protected RegexNode(RegexNode original, RegexNode parent) {
        super(original, parent);
    }

    @Override
    protected RegexNode getThis() { return this ; }

    /**
     * Construct a tree structure from a given regular expression
     * @param wg the WholeGraph
     * @param timeMan the TimeManager
     * @param regexp  the regular expression
     * @return the root node of the node tree
     * @throws ParseException 
     */
    public static VisreedNode construct(
        VisreedWholeGraph wg,
        BTTimeManager timeMan,
        String regexp
    ) throws ParseException{
        StringReader reader = new StringReader(regexp);
        if(timeMan == null){
            timeMan = new BTTimeManager();
        }
        if(wg == null){
            wg = new VisreedWholeGraph(timeMan);
        }
        
        VisreedNode result = null;
        wg.clearAll();
        result = RegexParser.parse(wg, reader);
        return result;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        String desc = "";
        if(getPayload() == null || getPayload().getDescription() == null){
            desc = "null";
        } else {
            desc = getPayload().getDescription();
        }
        return "RegexNode_" + desc;
    }    
}
