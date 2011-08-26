package play.layout;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import play.observer.PalletTransferHandler;
import play.tags.PLAYTags;

public class PalletListPLAY extends JList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PalletListPLAY() {
		DefaultListModel listModel = new DefaultListModel();
		listModel.addElement(PLAYTags.ROOT);
		listModel.addElement(PLAYTags.CLASS);
		listModel.addElement(PLAYTags.VARDECL);
		listModel.addElement(PLAYTags.WHILE);
		listModel.addElement(PLAYTags.IF);
		listModel.addElement(PLAYTags.SEQ);
		listModel.addElement(PLAYTags.DOT);
		listModel.addElement(PLAYTags.THIS);
		listModel.addElement(PLAYTags.CALL);
		listModel.addElement(PLAYTags.WORLDCALL);
		listModel.addElement(PLAYTags.ASSIGN);
		listModel.addElement(PLAYTags.METHOD);
		listModel.addElement(PLAYTags.COMM);
		listModel.addElement(PLAYTags.EQUAL_TO);

		setModel(listModel);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setVisibleRowCount(-1);

		setDropMode(DropMode.ON_OR_INSERT);
		setDragEnabled(true);
		setTransferHandler(new PalletTransferHandler());
	}
}
