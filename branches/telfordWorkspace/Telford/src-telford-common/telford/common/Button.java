package telford.common;

import java.util.ArrayList;

import telford.common.peers.ButtonPeer;

public class Button extends Component{
	final ButtonPeer peer ;
	ArrayList<ActionListener> listeners = new ArrayList<ActionListener>() ;
	
	public Button ( String title ) {
		peer = Kit.getKit().makeButtonPeer(title, this);
	}

	public void addActionListener(ActionListener e) {
		listeners.add(e) ;
	}
	
	public void fireAction( ActionEvent ev ) {
		for( ActionListener l : listeners ) l.actionPerformed(ev);
	}
	
	public void setEnabled(boolean paused) {
		peer.setEnabled(paused);
	}

	public void requestFocusInWindow() {
		peer.requestFocusInWindwow();
	}

	@Override
	public ButtonPeer getPeer() {
		return peer;
	}

}
