/**
 * PLAYHigraphJComponentKeyAdapter.java - play.higraph.swing - PLAY
 * 
 * Created on 2012-03-21 by Kai Zhu
 */
package play.higraph.swing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYNodeView;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYHigraphJComponentKeyAdapter implements KeyListener {

    private PLAYHigraphView higraphView;

    public PLAYHigraphJComponentKeyAdapter(PLAYHigraphView higraphView) {
	this.higraphView = higraphView;
    }

    /**
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    @Override
    public void keyTyped(KeyEvent e) {
	System.out.println("key typed" + e.getKeyChar() + "-" + e.getKeyCode());
	PLAYNodeView nodeView = this.higraphView.getCurrentNodeView();
	if (nodeView != null) {
	    StringBuffer expBuffer = new StringBuffer(nodeView.getLabel()
		    .getTheLabel());
	    if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE
		    || e.getKeyChar() == KeyEvent.VK_DELETE) {
		if (expBuffer.length() > 1) {
		    expBuffer.delete((expBuffer.length() - 2),
			    expBuffer.length());
		} else {
		    expBuffer.deleteCharAt(expBuffer.length() - 1);
		}
	    } else {
		if (expBuffer.length() > 0) {
		    expBuffer.deleteCharAt(expBuffer.length() - 1).append(
			    e.getKeyChar());
		} else {
		    expBuffer.append(e.getKeyChar());
		}
	    }
	    nodeView.getLabel().setTheLabel(expBuffer.toString() + '|');
	    this.higraphView.refresh();
	    this.higraphView.getDisplay().repaint();
	}
    }

    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {
    }

    /**
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }

}
