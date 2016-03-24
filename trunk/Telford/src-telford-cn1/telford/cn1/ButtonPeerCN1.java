package telford.cn1;


import com.codename1.ui.Button;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;

public class ButtonPeerCN1 extends telford.common.peers.ButtonPeer {
	MyButton myButton ;
	
	ButtonPeerCN1( String title, telford.common.Button button ) {
		super( button ) ;
		myButton = new MyButton( title ) ;
		myButton.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent evt) {
				telford.common.ActionEvent ev = new telford.common.ActionEvent() {} ;
				((telford.common.Button)component).fireAction(ev);
			}
		} );
	}
	
	@Override
	public void setEnabled(boolean paused) {
		myButton.setEnabled(paused);
	}

	@Override
	public void requestFocusInWindwow() {
		myButton.requestFocus();
	}
	
	@Override
	public MyButton getRepresentative() {
		return myButton;
	}

	@Override
	public void addMouseListener(int count) {
		
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
	
	class MyButton extends Button {
		
		MyButton(String title) {
			super(title);
		}
//		@Override
//		public void pointerPressed(int x, int y) {
//			super.pointerPressed(x, y);
//			component.repaint();
//		}
		
	}

}
