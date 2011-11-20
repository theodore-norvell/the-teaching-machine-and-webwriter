/**
 * RegexNodeButton.java
 * 
 * @date: Jul 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.regex.swing;

import javax.swing.JButton;

import visreed.model.VisreedTag;
import visreed.model.VisreedWholeGraph;

/**
 * RegexNodeButton defines the node button from which the user can drag
 * a new node to the graph.
 * @author Xiaoyu Guo
 *
 */
public class RegexNodeButton extends JButton {

    private static final long serialVersionUID = -1396883999157984743L;

//    private VisreedTag tag;
//    private VisreedWholeGraph wholeGraph;
    public RegexNodeButton(VisreedWholeGraph wg, VisreedTag tag){
        super();
//        this.tag = tag;
//        this.wholeGraph = wg;
    }
    
    public RegexNodeButton() {
        super();
    }

//    public RegexNode getNewNode(){
//        VisreedPayload pl = this.tag.defaultPayload();
//        
//        RegexNode node = this.wholeGraph.makeRootNode(pl);
//        
//        return node;
//    }
}
