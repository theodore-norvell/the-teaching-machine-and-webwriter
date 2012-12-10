/**
 * PLAYHigraphJComponentKeyAdapter.java - play.higraph.swing - PLAY
 * 
 * Created on 2012-03-21 by Kai Zhu
 */
package play.higraph.swing;

import higraph.view.ComponentView;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import play.controller.Controller;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYNodeView;
import play.higraph.view.model.PLAYViewSelectionModel;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYHigraphJComponentKeyAdapter implements KeyListener {

    private PLAYHigraphView playHigraphView;

    private Controller controller;

    private PLAYViewSelectionModel playViewSelectionModel;

    public PLAYHigraphJComponentKeyAdapter(PLAYHigraphView playHigraphView) {
	this.playHigraphView = playHigraphView;
	this.controller = Controller.getInstance();
	this.playViewSelectionModel = this.playHigraphView.getHigraph()
		.getWholeGraph().getPLAYViewSelectionModel();
    }

    /**
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    @Override
    public void keyTyped(KeyEvent e) {
	System.out.println("key typed" + e.getKeyChar() + "-" + e.getKeyCode());
	PLAYNodeView nodeView = (PLAYNodeView) this.playViewSelectionModel
		.getSelectedViewList().get(
			this.playViewSelectionModel.getSelectedViewList()
				.size() - 1);
	if (nodeView != null) {
	    StringBuffer expBuffer = new StringBuffer(nodeView.getLabel()
		    .getTheLabel());
	    if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE
		    || e.getKeyChar() == KeyEvent.VK_DELETE) {
		if (expBuffer.length() > 1) {
		    expBuffer.delete((expBuffer.length() - 2),
			    expBuffer.length());
		} else {
		    if (expBuffer.length() > 0) {
			expBuffer.deleteCharAt(0);
		    }
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
	    this.controller.refresh(this.playHigraphView);
	}
    }

    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {
	System.out.println("key Pressed" + e.getKeyCode() + "-"
		+ e.getModifiersEx());

	this.controller.setCheckPoint("KeyPressed");
	if (((e.getKeyCode() == KeyEvent.VK_X) && (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK))
		|| (e.getKeyCode() == KeyEvent.VK_DELETE)
		|| (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)) {
	    for (Object object : this.playViewSelectionModel
		    .getSelectedViewList()) {
		if (object instanceof PLAYNodeView) {
		    PLAYNodeView nodeView = (PLAYNodeView) object;
		    if (nodeView.getNode().canDelete()) {
			nodeView.getNode().delete();
			this.playHigraphView.setDeletedNodeView(nodeView);
		    }
		}
	    }
	} else if ((e.getKeyCode() == KeyEvent.VK_V)
		&& (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK)) {
	    PLAYNodeView deletedNodeView = this.playHigraphView
		    .getDeletedNodeView();
	    if (deletedNodeView != null) {
		this.playHigraphView.getHigraph().getWholeGraph()
			.makeRootNode(deletedNodeView.getNode().getPayload());
	    }
	} else if ((((e.getKeyCode() == KeyEvent.VK_DOWN) || (e.getKeyCode() == KeyEvent.VK_RIGHT)))
		&& (e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK)) {
	    this.playViewSelectionModel.setFocus(this.playViewSelectionModel
		    .getFocus() + 1);
	    this.playViewSelectionModel
		    .setMultipleSelectedVideList(this.playHigraphView);
	    for (Object object : this.playViewSelectionModel
		    .getSelectedViewList()) {
		((ComponentView<?, ?, ?, ?, ?, ?, ?>) object)
			.setFillColor(Color.LIGHT_GRAY);
	    }
	} else if ((((e.getKeyCode() == KeyEvent.VK_UP) || (e.getKeyCode() == KeyEvent.VK_LEFT)))
		&& (e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK)) {
	    this.playViewSelectionModel.setFocus(this.playViewSelectionModel
		    .getFocus() - 1);
	    this.playViewSelectionModel
		    .setMultipleSelectedVideList(this.playHigraphView);
	    for (Object object : this.playViewSelectionModel
		    .getSelectedViewList()) {
		((ComponentView<?, ?, ?, ?, ?, ?, ?>) object)
			.setFillColor(Color.LIGHT_GRAY);
	    }
	} else if ((e.getKeyCode() == KeyEvent.VK_DOWN)
		|| (e.getKeyCode() == KeyEvent.VK_RIGHT)) {
	    this.playViewSelectionModel.setAnchor(this.playViewSelectionModel
		    .getAnchor() + 1);
	    this.playViewSelectionModel.setFocus(this.playViewSelectionModel
		    .getFocus() + 1);
	    for (Object object : this.playViewSelectionModel
		    .getSelectedViewList()) {
		((ComponentView<?, ?, ?, ?, ?, ?, ?>) object)
			.setFillColor(null);
	    }
	    this.playViewSelectionModel
		    .setSelectedViewList(this.playHigraphView);
	    ((ComponentView<?, ?, ?, ?, ?, ?, ?>) this.playViewSelectionModel
		    .getSelectedViewList().get(
			    this.playViewSelectionModel.getSelectedViewList()
				    .size() - 1))
		    .setFillColor(Color.LIGHT_GRAY);
	} else if ((e.getKeyCode() == KeyEvent.VK_UP)
		|| (e.getKeyCode() == KeyEvent.VK_LEFT)) {
	    this.playViewSelectionModel.setAnchor(this.playViewSelectionModel
		    .getAnchor() - 1);
	    this.playViewSelectionModel.setFocus(this.playViewSelectionModel
		    .getFocus() - 1);
	    for (Object object : this.playViewSelectionModel
		    .getSelectedViewList()) {
		((ComponentView<?, ?, ?, ?, ?, ?, ?>) object)
			.setFillColor(null);
	    }
	    this.playViewSelectionModel
		    .setSelectedViewList(this.playHigraphView);
	    ((ComponentView<?, ?, ?, ?, ?, ?, ?>) this.playViewSelectionModel
		    .getSelectedViewList().get(
			    this.playViewSelectionModel.getSelectedViewList()
				    .size() - 1))
		    .setFillColor(Color.LIGHT_GRAY);
	}
	this.controller.refresh(this.playHigraphView);
    }

    /**
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }

}
