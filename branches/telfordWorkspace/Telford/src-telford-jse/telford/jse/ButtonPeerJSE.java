package telford.jse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

class ButtonPeerJSE extends telford.common.peers.ButtonPeer {
	MyButton myButton ;
	telford.common.ActionListener myActionListener;
	
	ButtonPeerJSE( String title, telford.common.Button button ) {
		super( button ) ;
		myButton = new MyButton( title ) ;
		myButton.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO create the right kind of event.
				telford.common.ActionEvent ev = new telford.common.ActionEvent() {} ;
				ButtonPeerJSE.this.button.fireAction(ev);
			}
		});
	}
	
	@Override
	public void setEnabled(boolean paused) {
		myButton.setEnabled(paused);
	}

	@Override
	public void requestFocusInWindwow() {
		myButton.requestFocusInWindow();
	}
	
	@Override
	public MyButton getRepresentative() {
		return myButton;
	}

	class MyButton extends JButton {
		
		MyButton(String title) {
			super(title);
		}
		
	}
}

