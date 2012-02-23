/**
 * SyntaxListPanel.java - play.ide.view - PLAY
 *
 * Created on 2012-2-15 by Kai Zhu
 */
package play.ide.view;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import play.higraph.model.PLAYTag;
import play.higraph.swing.SyntaxTransferHandler;

/**
 * @author Kai Zhu
 * 
 */
public class SyntaxListPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 59182330371195540L;

    private JList<PLAYTag> syntaxList;

    private DefaultListModel<PLAYTag> syntaxListModel;

    public SyntaxListPanel() {
	this.syntaxList = new JList<>();
	this.syntaxListModel = new DefaultListModel<PLAYTag>();
	
	this.syntaxListModel.addElement(PLAYTag.ASSIGN);
	this.syntaxListModel.addElement(PLAYTag.SEQ);
	
	this.syntaxList.setModel(this.syntaxListModel);
	this.syntaxList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	this.syntaxList.setVisibleRowCount(-1);
	this.syntaxList.setDropMode(DropMode.ON_OR_INSERT);
	this.syntaxList.setDragEnabled(true);
	this.syntaxList.setTransferHandler(new SyntaxTransferHandler());
	
	this.add(this.syntaxList);
    }

    /**
     * @return
     */
    public JComponent getList() {
	return this.syntaxList;
    }

}
