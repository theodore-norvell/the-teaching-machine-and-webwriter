package tm.gwt.telford;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;

import telford.common.Component;

public class ButtonPeerGWT extends telford.common.peers.ButtonPeer {
	Button myButton;
	HandlerRegistration clickHandlerRegistration = null ;

	ButtonPeerGWT(String title, telford.common.Button button) {
		super(button);
		myButton = new Button(title);	
		myButton.addClickHandler(new MouseListenerGWT(component) {
			@Override
			public void onClick(ClickEvent e) {
				telford.common.ActionEvent ev = new telford.common.ActionEvent() {} ;
				((telford.common.Button)component).fireAction(ev);
			}
		});
	
		//myButton.addClickHandler(new MyHandler());
	}

	@Override
	public void setEnabled(boolean paused) {
		myButton.setEnabled(paused);
	}

	@Override
	public void requestFocusInWindwow() {
	}

//	@Override
	public Button getRepresentative() {
		return myButton;
	}

	@Override
	public void addMouseListener( ) {       
        //throw new AssertionError("Mouse listeners for Buttons, not supported.") ;
        int telfordCount = component.mouseListenerCount() ;
        int platformCount = this.clickHandlerRegistration == null ? 0 : 1 ;
        if( platformCount == 0 && telfordCount > 0 ) {
            MouseListenerGWT mouseListener =  new MouseListenerGWT(component) ;
            this.clickHandlerRegistration = myButton.addClickHandler( mouseListener ) ;
        }
        else if( platformCount > 0 && telfordCount == 0 ) {
            this.clickHandlerRegistration.removeHandler(); 
            this.clickHandlerRegistration = null ;
        }
		
	}

	@Override
	public int getWidth() {
		// TODO
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO
		return 0;
	}

	@Override
	public void repaint() {
	}

	@Override
	public void setStyleName(String styleName){
		
	}
	
	class MyHandler implements ClickHandler {
		/**
		 * Fired when the user clicks on the sendButton.
		 */
		public void onClick(ClickEvent event) {
			telford.common.ActionEvent ev = new telford.common.ActionEvent() {} ;
			((telford.common.Button)component).fireAction(ev);
		}
	}
}
