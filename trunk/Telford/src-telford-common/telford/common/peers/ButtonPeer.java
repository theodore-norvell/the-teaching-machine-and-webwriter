package telford.common.peers;

import telford.common.*;

public abstract class ButtonPeer extends ComponentPeer {
		
		public ButtonPeer( Button button) { super(button); }

		abstract public void setEnabled (boolean paused) ;

		abstract public void requestFocusInWindwow() ;
}
