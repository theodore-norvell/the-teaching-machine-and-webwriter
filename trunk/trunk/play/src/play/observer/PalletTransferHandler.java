package play.observer;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

public class PalletTransferHandler extends TransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PalletTransferHandler() {
		super();
	}

	@Override
	public int getSourceActions(JComponent arg0) {
		return DnDConstants.ACTION_COPY;
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		System.out.println("creating pallet transferable");
		JList list = (JList) c;
		Object[] values = list.getSelectedValues();
		Transferable result;

		if (values.length != 0)
			result = new ViewTransferObjectPLAY(values[0]);
		else
			result = null;
		return result;
	}
}