/**
 * PLAYHigraphJComponentKeyAdapter.java - play.higraph.swing - PLAY
 * 
 * Created on 2012-03-21 by Kai Zhu
 */
package play.higraph.swing;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import play.controller.Controller;
import play.higraph.model.PLAYNode;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYNodeView;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYHigraphJComponentKeyAdapter implements KeyListener {

    private PLAYHigraphView higraphView;

    private Controller controller;

    public PLAYHigraphJComponentKeyAdapter(PLAYHigraphView higraphView) {
	this.higraphView = higraphView;
	this.controller = Controller.getInstance();
    }

    /**
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    @Override
    public void keyTyped(KeyEvent e) {
	System.out.println("key typed" + e.getKeyChar() + "-" + e.getKeyCode());
	PLAYNodeView nodeView = this.controller.getCurrentNodeView();
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
	    this.controller.refresh();
	}
    }

    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {
	System.out.println("key Pressed" + e.getKeyCode() + "-"
		+ e.getModifiersEx());
	PLAYNodeView nodeView = this.controller.getCurrentNodeView();
	if (nodeView != null) {
	    this.controller.setCheckPoint("KeyPressed");
	    if ((e.getKeyCode() == KeyEvent.VK_X)
		    && (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK)) {
		if (nodeView.getNode().canDelete()) {
		    nodeView.getNode().delete();
		    this.higraphView.setDeletedNodeView(nodeView);
		}
	    } else if ((e.getKeyCode() == KeyEvent.VK_V)
		    && (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK)) {
		PLAYNodeView deletedNodeView = this.higraphView
			.getDeletedNodeView();
		if (deletedNodeView != null) {
		    this.higraphView
			    .getHigraph()
			    .getWholeGraph()
			    .makeRootNode(
				    deletedNodeView.getNode().getPayload());
		}
	    } else if ((((e.getKeyCode() == KeyEvent.VK_DOWN) || (e
		    .getKeyCode() == KeyEvent.VK_RIGHT)))
		    && (e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK)) {
		this.controller.getNextNodeView();
		nodeView = this.controller.getCurrentNodeView();
		this.controller.getCurrentNodeView().setFillColor(
			Color.LIGHT_GRAY);
	    } else if ((((e.getKeyCode() == KeyEvent.VK_UP) || (e.getKeyCode() == KeyEvent.VK_LEFT)))
		    && (e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK)) {
		this.controller.getPreviousNodeView();
		nodeView = this.controller.getCurrentNodeView();
		this.controller.getCurrentNodeView().setFillColor(
			Color.LIGHT_GRAY);
	    } else if ((e.getKeyCode() == KeyEvent.VK_DOWN)
		    || (e.getKeyCode() == KeyEvent.VK_RIGHT)) {
		this.controller.getCurrentNodeView().setFillColor(null);
		this.controller.getNextNodeView();
		nodeView = this.controller.getCurrentNodeView();
		this.controller.getCurrentNodeView().setFillColor(
			Color.LIGHT_GRAY);
	    } else if ((e.getKeyCode() == KeyEvent.VK_UP)
		    || (e.getKeyCode() == KeyEvent.VK_LEFT)) {
		this.controller.getCurrentNodeView().setFillColor(null);
		this.controller.getPreviousNodeView();
		nodeView = this.controller.getCurrentNodeView();
		this.controller.getCurrentNodeView().setFillColor(
			Color.LIGHT_GRAY);
	    }
	    this.controller.refresh();
	}
    }

    /**
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }

}
