package telford.common;

import java.util.ArrayList;

import telford.common.peers.ComponentPeer;

public class Component {
	ComponentPeer peer;
	ArrayList<MouseListener> mouseListeners = new ArrayList<MouseListener>() ;
	
	public Component() { this(true) ; }
	
	public Component (boolean makePeer) {
		if(makePeer) peer = Kit.getKit().makeComponentPeer(this);
	}
	
	public ComponentPeer getPeer(){
		return peer;
	}

	public void addMouseListener(MouseListener mouseListener){
		mouseListeners.add(mouseListener) ;
		getPeer().addMouseListener(mouseListeners.size());
	}
	
	public void fireMouseMoved( MouseEvent evt ) {
		for(MouseListener l : mouseListeners ) l.mouseMoved( evt ) ;
	}
	
	public void paintComponent(Graphics g) {
	}
	
	public void repaint(){
		getPeer().repaint();
	}

	public int getWidth() {
		return getPeer().getWidth();
	}

	public int getHeight() {
		return getPeer().getHeight();
	}
	
	public void setStyleName(String styleName){
		getPeer().setStyleName(styleName);
	}
	
}
