/**
 * VisreedFrameTransferActionListener.java
 * 
 * @date: Nov 22, 2011
 * @author: Xiaoyu Guo
 * This file is part of the Teaching Machine project.
 */
package visreed.swing;

import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JComponent;

import visreed.swing.editor.VisreedTextArea;

/**
 * This listener can forward the Cut&Paste menu item to suitable components.
 * @see http://docs.oracle.com/javase/tutorial/uiswing/examples/dnd/ListCutPasteProject/src/dnd/TransferActionListener.java  
 * @author Xiaoyu Guo
 */
public class VisreedFrameTransferActionListener
implements ActionListener, PropertyChangeListener {
	private JComponent focusOwner = null;

	public VisreedFrameTransferActionListener() {
		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addPropertyChangeListener("permanentFocusOwner", this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		Object o = e.getNewValue();
		if (o instanceof VisreedTextArea) {
			focusOwner = (VisreedTextArea) o;
		} else if (o instanceof VisreedJComponent){
			focusOwner = (VisreedJComponent) o;
		} else {
			focusOwner = null;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (focusOwner == null)
			return;
		String action = e.getActionCommand();
		Action a = focusOwner.getActionMap().get(action);
		if (a != null) {
			a.actionPerformed(
				new ActionEvent(
					focusOwner,
					ActionEvent.ACTION_PERFORMED,
					null
				)
			);
		}
	}
}
