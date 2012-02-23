package play.layout;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import play.observer.PalletTransferHandler;
import play.tags.PLAYTags;

public class TypeListPLAY extends JList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TypeListPLAY() {
		DefaultListModel listModel = new DefaultListModel();
		listModel.addElement(PLAYTags.BOOLEAN);
		listModel.addElement(PLAYTags.STRING);
		listModel.addElement(PLAYTags.NUMBER);
		listModel.addElement(PLAYTags.EMPTY);
		listModel.addElement(PLAYTags.NULL);
		listModel.addElement(PLAYTags.ANY);

		setModel(listModel);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setVisibleRowCount(-1);

		setDropMode(DropMode.ON_OR_INSERT);
		setDragEnabled(true);
		setTransferHandler(new PalletTransferHandler());
	}

}
