/**
 * RegexJList.java
 * 
 * @date: Jul 26, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.extension.regex.swing;

import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedWholeGraph;
import visreed.model.payload.SequencePayload;
import visreed.model.tag.VisreedTag;

/**
 * The default tool bar does not support drag, so use a JList with custom
 * renderer instead.
 * @author Xiaoyu Guo
 */
public class RegexJList extends JList {
    private static final long serialVersionUID = 8675362402938781706L;

    private VisreedWholeGraph wholeGraph;
    
    public RegexJList(VisreedWholeGraph wg){
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
        new RegexIconData("Sequence",       "/images/toolbar_node_seq.png",     VisreedTag.SEQUENCE),
        new RegexIconData("Alternation",    "/images/toolbar_node_alt.png",     VisreedTag.ALTERNATION),
        new RegexIconData("Kleene +",       "/images/toolbar_node_kln+.png",    VisreedTag.KLEENE_PLUS),
        new RegexIconData("Kleene *",       "/images/toolbar_node_klnstar.png", VisreedTag.KLEENE_STAR),
        new RegexIconData("Terminal",       "/images/toolbar_node_ter.png",     VisreedTag.TERMINAL)
    };

    private static DefaultListModel getDefaultData() {
        DefaultListModel model = new DefaultListModel();

        for(int i = 0; i < PREDEFINED_NODE_DATA.length; i++){
            model.add(0, PREDEFINED_NODE_DATA[i]);
        }
        return model;
    }
    
    /**
     * Creates a new {@link VisreedNode} object, using the selected payload.
     * @return a new {@link VisreedNode} or <code>null</code> if nothing is selected.
     */
    public VisreedNode createSelectedNode(){
        RegexIconData selectedData = (RegexIconData) this.getSelectedValue();
        if(selectedData == null){
            return null;
        }
        VisreedPayload pl = selectedData.getTag().defaultPayload();
        
        VisreedNode node = this.wholeGraph.makeRootNode(pl);
        
        VisreedTag tag = selectedData.getTag();
        if(tag.equals(VisreedTag.ALTERNATION)){
            node.appendChild(this.wholeGraph.makeRootNode(new SequencePayload()));
            node.appendChild(this.wholeGraph.makeRootNode(new SequencePayload()));
        } else if (tag.equals(VisreedTag.KLEENE_PLUS) || tag.equals(VisreedTag.KLEENE_STAR) || tag.equals(VisreedTag.OPTIONAL)){
            node.appendChild(this.wholeGraph.makeRootNode(new SequencePayload()));
        }

        return node;
    }
}
