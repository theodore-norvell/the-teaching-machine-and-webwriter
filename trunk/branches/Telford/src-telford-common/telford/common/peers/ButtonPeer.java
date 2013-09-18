package telford.common.peers;

import telford.common.*;

public abstract class ButtonPeer extends ComponentPeer {

		final protected Button button ;
		
		public ButtonPeer( Button button) { this.button = button ; }

		abstract public void setEnabled (boolean paused) ;

		abstract public void requestFocusInWindwow() ;
}
