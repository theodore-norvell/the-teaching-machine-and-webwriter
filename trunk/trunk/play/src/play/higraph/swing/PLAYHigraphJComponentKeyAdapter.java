/**
 * PLAYHigraphJComponentKeyAdapter.java - play.higraph.swing - PLAY
 * 
 * Created on 2012-03-21 by Kai Zhu
 */
package play.higraph.swing;

import higraph.swing.SubgraphTransferHandler;
import higraph.view.ComponentView;
import higraph.view.DropZone;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import play.controller.Controller;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYTag;
import play.higraph.view.PLAYDropZone;
import play.higraph.view.PLAYHigraphView;
import play.higraph.view.PLAYNodeView;
import play.higraph.view.PLAYSubgraphEventObserver;
import play.higraph.view.model.PLAYViewSelectionModel;

/**
 * @author Kai Zhu
 * 
 */
public class PLAYHigraphJComponentKeyAdapter implements KeyListener {

	private PLAYHigraphView playHigraphView;



	private Controller controller;

	private PLAYViewSelectionModel playViewSelectionModel;

	private PLAYSubgraphEventObserver eo;

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

		PLAYTag tag =null;
		PLAYNodeView nodeView = (PLAYNodeView) this.playViewSelectionModel
				.getSelectedViewList(playHigraphView).get(
						this.playViewSelectionModel.getSelectedViewList(playHigraphView)
						.size() - 1);
		if (nodeView != null){
			tag = nodeView.getNode().getTag();
		}

		if (nodeView != null && (isEditable(tag))) {
			System.out.println("inside label");
			StringBuffer expBuffer = new StringBuffer(nodeView.getLabel()
					.getTheLabel());
			//System.out.println("buffer = " + expBuffer);
			if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE
					|| e.getKeyChar() == KeyEvent.VK_DELETE) {
				System.out.println("inside backspace");
				if (expBuffer.length() > 1) {
					expBuffer.delete((expBuffer.length() - 2),
							expBuffer.length());
				} else {
					if (expBuffer.length() > 0) {
						expBuffer.deleteCharAt(0);
					}
				}
			} else if(e.getKeyChar() == KeyEvent.VK_ENTER){
				System.out.println("enter");
				expBuffer.deleteCharAt(expBuffer.length() - 1);
				nodeView.getLabel().setTheLabel(expBuffer.toString());
				nodeView.getNode().getPayload().setPayloadValue(nodeView.getLabel().getTheLabel());
				nodeView.setFillColor(null);
				playViewSelectionModel.clearSelectedNodeViewList();
			}
			else {
				if (expBuffer.length() > 0) {
					System.out.println("buffer>0");
					expBuffer.deleteCharAt(expBuffer.length() - 1).append(
							e.getKeyChar());
				} else {
					expBuffer.append(e.getKeyChar());
				}
				nodeView.getLabel().setTheLabel(expBuffer.toString()+'|');
			}
			
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
					.getSelectedViewList(playHigraphView)) {
				if (object instanceof PLAYNodeView) {
					PLAYNodeView nodeView = (PLAYNodeView) object;
					if (nodeView.getNode().canDelete()) {
						nodeView.getNode().delete();
						this.playHigraphView.setDeletedNodeView(nodeView);
					}
				}
			}
		}

		else if ((e.getKeyCode() == KeyEvent.VK_V)
				&& (e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK)) {
			PLAYNodeView deletedNodeView = this.playHigraphView
					.getDeletedNodeView();
			if (deletedNodeView != null) {
				this.playHigraphView.getHigraph().getWholeGraph()
				.makeRootNode(deletedNodeView.getNode().getPayload());
			}
		} 

		else if ((((e.getKeyCode() == KeyEvent.VK_DOWN) || (e.getKeyCode() == KeyEvent.VK_RIGHT)))
				&& (e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK)) {
			this.playViewSelectionModel.setFocus(this.playViewSelectionModel
					.getFocus() + 1);
			//this.playViewSelectionModel.setMultipleSelectedVideList(this.playHigraphView);
			for (Object object : this.playViewSelectionModel
					.getSelectedViewList(playHigraphView)) {
				((ComponentView<?, ?, ?, ?, ?, ?, ?>) object)
				.setFillColor(Color.LIGHT_GRAY);
			}
		} 

		else if ((((e.getKeyCode() == KeyEvent.VK_UP) || (e.getKeyCode() == KeyEvent.VK_LEFT)))
				&& (e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK)) {
			this.playViewSelectionModel.setFocus(this.playViewSelectionModel
					.getFocus() - 1);
			//this.playViewSelectionModel.setMultipleSelectedVideList(this.playHigraphView);
			for (Object object : this.playViewSelectionModel
					.getSelectedViewList(playHigraphView)) {
				((ComponentView<?, ?, ?, ?, ?, ?, ?>) object)
				.setFillColor(Color.LIGHT_GRAY);
			}
		} 

		else if ((e.getKeyCode() == KeyEvent.VK_DOWN)
				|| (e.getKeyCode() == KeyEvent.VK_RIGHT)) {
			for (Object object : this.playViewSelectionModel
					.getSelectedViewList(playHigraphView)) {
				((ComponentView<?, ?, ?, ?, ?, ?, ?>) object)
				.setFillColor(null);
			}
			this.playViewSelectionModel.setAnchor(this.playViewSelectionModel
					.getAnchor() + 1);
			this.playViewSelectionModel.setFocus(this.playViewSelectionModel
					.getFocus() + 1);

			//this.playViewSelectionModel.setSelectedViewList(this.playHigraphView);
			((ComponentView<?, ?, ?, ?, ?, ?, ?>) this.playViewSelectionModel
					.getSelectedViewList(playHigraphView).get(
							this.playViewSelectionModel.getSelectedViewList(playHigraphView)
							.size() - 1))
							.setFillColor(Color.LIGHT_GRAY);
		} 

		else if ((e.getKeyCode() == KeyEvent.VK_UP)
				|| (e.getKeyCode() == KeyEvent.VK_LEFT)) {

			for (Object object : this.playViewSelectionModel
					.getSelectedViewList(playHigraphView)) {
				((ComponentView<?, ?, ?, ?, ?, ?, ?>) object)
				.setFillColor(null);
			}
			this.playViewSelectionModel.setAnchor(this.playViewSelectionModel
					.getAnchor() - 1);
			this.playViewSelectionModel.setFocus(this.playViewSelectionModel
					.getFocus() - 1);

			//this.playViewSelectionModel
			//.setSelectedViewList(this.playHigraphView);
			((ComponentView<?, ?, ?, ?, ?, ?, ?>) this.playViewSelectionModel
					.getSelectedViewList(playHigraphView).get(
							this.playViewSelectionModel.getSelectedViewList(playHigraphView)
							.size() - 1))
							.setFillColor(Color.LIGHT_GRAY);
		}

		else if(this.playViewSelectionModel.getSelectedViewList(playHigraphView).size() ==1){

			PLAYTag tag = null;
			PLAYNodeView nodeView = (PLAYNodeView) this.playViewSelectionModel
					.getSelectedViewList(playHigraphView).get(
							this.playViewSelectionModel.getSelectedViewList(playHigraphView)
							.size() - 1);
			if (nodeView != null){
				tag = nodeView.getNode().getTag();
			}

			if (nodeView != null && !(isEditable(tag))) {
				if ((e.getKeyCode() == KeyEvent.VK_SLASH) &&(e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK)){
					PLAYTag currentTag = PLAYTag.IF;
					addNewTag(currentTag);
				}

				else if ((e.getKeyCode() == KeyEvent.VK_2)&&(e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK)){
					PLAYTag currentTag = PLAYTag.WHILE;
					addNewTag(currentTag);
				}

				else if ((e.getKeyCode() == KeyEvent.VK_V)){
					PLAYTag currentTag = PLAYTag.VARDECL;
					addNewTag(currentTag);
				}

				else if ((e.getKeyCode() == KeyEvent.VK_M)){
					PLAYTag currentTag = PLAYTag.METHOD;
					addNewTag(currentTag);
				}

				else if ((e.getKeyCode() == KeyEvent.VK_EQUALS)&&!(e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK)){
					PLAYTag currentTag = PLAYTag.ASSIGN;
					addNewTag(currentTag);
				}

				else if ((e.getKeyCode() == KeyEvent.VK_B)){
					PLAYTag currentTag = PLAYTag.BOOLEANTYPE;
					addNewTag(currentTag);
				}

				else if ((e.getKeyCode() == KeyEvent.VK_N)){
					PLAYTag currentTag = PLAYTag.NUMBERTYPE;
					addNewTag(currentTag);
				}

				else if ((e.getKeyCode() == KeyEvent.VK_S)){
					PLAYTag currentTag = PLAYTag.STRINGTYPE;
					addNewTag(currentTag);
				}
				
				else if ((e.getKeyCode() == KeyEvent.VK_EQUALS)&&(e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK)){
					PLAYTag currentTag = PLAYTag.PLUS;
					addNewTag(currentTag);
				}
				
				else if ((e.getKeyCode() == KeyEvent.VK_UNDERSCORE)&&(e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK)){
					PLAYTag currentTag = PLAYTag.MINUS;
					addNewTag(currentTag);
				}
				
				else if ((e.getKeyCode() == KeyEvent.VK_8)&&(e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK)){
					PLAYTag currentTag = PLAYTag.MULTIPLY;
					addNewTag(currentTag);
				}
				
				else if ((e.getKeyCode() == KeyEvent.VK_SLASH)){
					PLAYTag currentTag = PLAYTag.DIVIDE;
					addNewTag(currentTag);
				}
				
				else if ((e.getKeyCode() == KeyEvent.VK_COMMA)&&(e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK)){
					PLAYTag currentTag = PLAYTag.LESS;
					addNewTag(currentTag);
				}
				
				else if ((e.getKeyCode() == KeyEvent.VK_PERIOD)&&(e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK)){
					PLAYTag currentTag = PLAYTag.GREATER;
					addNewTag(currentTag);
				}
			}
		}

		this.controller.refresh(this.playHigraphView);
	}

	public void addNewTag(PLAYTag currentTag){
		System.out.println("add new tag");

		if(playViewSelectionModel.getFlag() == false){
			for (Object object : this.playViewSelectionModel
					.getSelectedViewList(playHigraphView))
			{
				System.out.println(object);

				PLAYNodeView currentNodeView = (PLAYNodeView) object;
				PLAYNode currentNode = currentNodeView.getNode();

				System.out.println("currentNode = "+ currentNode.getTag());
				System.out.println(playViewSelectionModel.getAnchor());
				
				if(currentNode.canReplace(currentTag)){
					currentNode.replace(currentTag);
				}
				else if(currentNode.canInsertChild(currentNode.getNumberOfChildren(),currentTag))
				{
					System.out.println("inside insert");
					currentNode.insertChild(currentNode.getNumberOfChildren(), currentTag);
				}
			}		
		}
		else
		{
			PLAYNodeView currentNodeView = playViewSelectionModel.getParentNodeView();
			PLAYNode currentNode = currentNodeView.getNode();

			if(currentNode.canInsertChild(playViewSelectionModel.getAnchor(),currentTag))
			{
				System.out.println("inside insert");
				currentNode.insertChild(playViewSelectionModel.getAnchor(), currentTag);
			}
		}
	}

	/**
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {

	}
	
	public boolean isEditable(PLAYTag pt){
		return pt.equals(PLAYTag.NUMBERLITERAL) || pt.equals(PLAYTag.STRINGLITERAL)
				|| pt.equals(PLAYTag.THISVAR) || pt.equals(PLAYTag.LOCALVAR)
				|| pt.equals(PLAYTag.WORLDVAR) || pt.equals(PLAYTag.NUMBERTYPE)
				|| pt.equals(PLAYTag.STRINGTYPE) || pt.equals(PLAYTag.BOOLEANTYPE);
	}
}
