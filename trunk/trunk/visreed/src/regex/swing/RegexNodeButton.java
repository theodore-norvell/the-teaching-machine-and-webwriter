/**
 * RegexNodeButton.java
 * 
 * @date: Jul 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.swing;

import javax.swing.JButton;

import regex.model.RegexNode;
import regex.model.RegexPayload;
import regex.model.RegexTag;
import regex.model.RegexWholeGraph;

/**
 * RegexNodeButton defines the node button from which the user can drag
 * a new node to the graph.
 * @author Xiaoyu Guo
 *
 */
public class RegexNodeButton extends JButton {

    private static final long serialVersionUID = -1396883999157984743L;

//    private RegexTag tag;
//    private RegexWholeGraph wholeGraph;
    public RegexNodeButton(RegexWholeGraph wg, RegexTag tag){
        super();
//        this.tag = tag;
//        this.wholeGraph = wg;
    }
    
    public RegexNodeButton() {
        super();
    }

//    public RegexNode getNewNode(){
//        RegexPayload pl = this.tag.defaultPayload();
//        
//        RegexNode node = this.wholeGraph.makeRootNode(pl);
//        
//        return node;
//    }
}
