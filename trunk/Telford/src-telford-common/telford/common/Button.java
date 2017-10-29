package telford.common;

import java.util.ArrayList;

import telford.common.peers.ButtonPeer;

public class Button extends Component{

	ArrayList<ActionListener> listeners = new ArrayList<ActionListener>() ;
	
	public Button ( String title ) {
		super() ;
		peer = Kit.getKit().makeButtonPeer(title, this);
	}

	public void addActionListener(ActionListener e) {
		listeners.add(e) ;
	}
	
	public void fireAction( ActionEvent ev ) {
		for( ActionListener l : listeners ) l.actionPerformed(ev);
	}
	
	public void setEnabled(boolean paused) {
		getPeer().setEnabled(paused);
	}

	public void requestFocusInWindow() {
		getPeer().requestFocusInWindwow();
	}

	@Override
	public ButtonPeer getPeer() {
		return (ButtonPeer) peer;
	}

    @Override
    public void paintComponent(Graphics g) {
    }

}
