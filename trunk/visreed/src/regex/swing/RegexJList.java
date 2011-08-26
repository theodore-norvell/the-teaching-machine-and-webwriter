/**
 * RegexJList.java
 * 
 * @date: Jul 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package regex.swing;

import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import regex.model.RegexNode;
import regex.model.RegexPayload;
import regex.model.RegexTag;
import regex.model.RegexWholeGraph;
import regex.model.payload.SequencePayload;

/**
 * The default toolbar does not support drag, so use a JList with custom
 * renderer instead.
 * @author Xiaoyu Guo
 */
public class RegexJList extends JList {
    private static final long serialVersionUID = 8675362402938781706L;

    private RegexWholeGraph wholeGraph;
    
    public RegexJList(RegexWholeGraph wg){
        // fills the list
        super(getDefaultData());
        
        this.wholeGraph = wg;
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setAutoscrolls(true);
        this.setDragEnabled(true);
        this.setLayoutOrientation(JList.VERTICAL);
        
        this.setBackground(SystemColor.control);
        this.setBorder(BorderFactory.createEmptyBorder());
        
        this.setCellRenderer(new RegexNodeCellRenderer(wg));
        
        this.setTransferHandler(new RegexNodeToolBarTransferHandler());
    }
    
    private static final RegexIconData[] PREDEFINED_NODE_DATA = new RegexIconData[]{
        new RegexIconData("Sequence",       "/images/toolbar_node_seq.png",     RegexTag.SEQUENCE),
        new RegexIconData("Alternation",    "/images/toolbar_node_alt.png",     RegexTag.ALTERNATION),
        new RegexIconData("Kleene +",       "/images/toolbar_node_kln+.png",    RegexTag.KLEENE_PLUS),
        new RegexIconData("Kleene *",       "/images/toolbar_node_klnstar.png", RegexTag.KLEENE_STAR),
        new RegexIconData("Terminal",       "/images/toolbar_node_ter.png",     RegexTag.TERMINAL)
    };

    private static DefaultListModel getDefaultData() {
        DefaultListModel model = new DefaultListModel();

        for(int i = 0; i < PREDEFINED_NODE_DATA.length; i++){
            model.add(0, PREDEFINED_NODE_DATA[i]);
        }
        return model;
    }
    
    /**
     * Creates a new {@link RegexNode} object, using the selected payload.
     * @return a new {@link RegexNode} or <code>null</code> if nothing is selected.
     */
    public RegexNode createSelectedNode(){
        RegexIconData selectedData = (RegexIconData) this.getSelectedValue();
        if(selectedData == null){
            return null;
        }
        RegexPayload pl = selectedData.getTag().defaultPayload();
        
        RegexNode node = this.wholeGraph.makeRootNode(pl);
        
        switch(selectedData.getTag()){
        case ALTERNATION:
            node.appendChild(this.wholeGraph.makeRootNode(new SequencePayload()));
            node.appendChild(this.wholeGraph.makeRootNode(new SequencePayload()));
            break;
        case KLEENE_PLUS:
        case KLEENE_STAR:
        case OPTIONAL:
            node.appendChild(this.wholeGraph.makeRootNode(new SequencePayload()));
            break;
        default:
            break;
        }
        return node;
    }
}
