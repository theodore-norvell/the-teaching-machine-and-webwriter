package telford.common;

import telford.common.peers.ComponentPeer;

abstract public class Component {
	
	public Component () {
	}
	
	abstract public ComponentPeer getPeer();

	public void addMouseListener(MouseListener mouseListener){
		getPeer().addMouseListener(mouseListener);
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
	
}
