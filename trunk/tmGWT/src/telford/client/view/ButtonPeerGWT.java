package telford.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

public class ButtonPeerGWT extends telford.common.peers.ButtonPeer {
	Button myButton;

	ButtonPeerGWT(String title, telford.common.Button button) {
		super(button);
		myButton = new Button(title);
		myButton.addClickHandler(new MyHandler());
	}

	@Override
	public void setEnabled(boolean paused) {
		myButton.setEnabled(paused);
	}

	@Override
	public void requestFocusInWindwow() {
	}

	@Override
	public Button getRepresentative() {
		return myButton;
	}

	@Override
	public void addMouseListener(int count) {
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

	class MyHandler implements ClickHandler {
		/**
		 * Fired when the user clicks on the sendButton.
		 */
		public void onClick(ClickEvent event) {
			String html = ((Button) event.getSource()).getHTML();
			if ("Draw another circle".equals(html)) {
			} else if ("Clear previous circle".equals(html)) {
			}
		}
	}
}
