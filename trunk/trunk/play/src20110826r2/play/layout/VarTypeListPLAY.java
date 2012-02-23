package play.layout;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import play.observer.PalletTransferHandler;
import play.tags.PLAYTags;

public class VarTypeListPLAY extends JList{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VarTypeListPLAY() {
		DefaultListModel listModel = new DefaultListModel();
		listModel.addElement(PLAYTags.LOCALVAR);
		listModel.addElement(PLAYTags.FIELDVAR);
		listModel.addElement(PLAYTags.WORLDVAR);
		listModel.addElement(PLAYTags.EMPTY);
		

		setModel(listModel);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setVisibleRowCount(-1);

		setDropMode(DropMode.ON_OR_INSERT);
		setDragEnabled(true);
		setTransferHandler(new PalletTransferHandler());
	}

}
