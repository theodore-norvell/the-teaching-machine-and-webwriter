package telford.jse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;


class ButtonPeerJSE extends telford.common.peers.ButtonPeer {
	MyButton myButton ;
	
	ButtonPeerJSE( String title, telford.common.Button button ) {
		super( button ) ;
		myButton = new MyButton( title ) ;
		myButton.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO create the right kind of event.
				telford.common.ActionEvent ev = new telford.common.ActionEvent() {} ;
				((telford.common.Button)component).fireAction(ev);
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
	
	@Override
	public void addMouseListener(telford.common.MouseListener mouseListener) {
		myButton.addMouseListener(new MouseListenerJSE(mouseListener));
	}
	
	@Override
	public int getWidth() {
		return myButton.getWidth();
	}

	@Override
	public int getHeight() {
		return myButton.getHeight();
	}

	@Override
	public void repaint() {
		myButton.repaint();
	}
	
	class MyButton extends JButton {
		
		MyButton(String title) {
			super(title);
		}
		
	}


}

