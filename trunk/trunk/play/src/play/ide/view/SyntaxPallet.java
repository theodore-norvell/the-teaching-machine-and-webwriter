/**
 * SyntaxPallet.java - play.ide.view - PLAY
 *
 * Created on 2012-2-15 by Kai Zhu
 */
package play.ide.view;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import play.higraph.model.PLAYTag;
import play.higraph.swing.SyntaxTransferHandler;

/**
 * @author Kai Zhu
 * 
 */
public class SyntaxPallet extends JPanel {

    private static final long serialVersionUID = 59182330371195540L;

    private JList<PLAYTag> syntaxList;

    private DefaultListModel<PLAYTag> syntaxListModel;

    public SyntaxPallet() {
	this.syntaxList = new JList<>();
	this.syntaxListModel = new DefaultListModel<PLAYTag>();

	this.syntaxListModel.addElement(PLAYTag.CLASS);
	this.syntaxListModel.addElement(PLAYTag.SEQ);
	this.syntaxListModel.addElement(PLAYTag.ASSIGN);
	this.syntaxListModel.addElement(PLAYTag.VARDECL);
	this.syntaxListModel.addElement(PLAYTag.IF);
	this.syntaxListModel.addElement(PLAYTag.WHILE);
	this.syntaxListModel.addElement(PLAYTag.PLACEHOLDER);

	this.syntaxList.setModel(this.syntaxListModel);
	this.syntaxList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	this.syntaxList.setVisibleRowCount(-1);
	this.syntaxList.setDragEnabled(true);
	this.syntaxList.setTransferHandler(new SyntaxTransferHandler());
	this.syntaxList.setBackground(this.getBackground());

	this.add(this.syntaxList);
    }

}
