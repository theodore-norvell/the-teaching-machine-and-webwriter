package tm.gwt.telford;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import telford.common.Root;

public class RootPeerGWT extends telford.common.peers.RootPeer {
	 RootPanel canvasHolder ;

	RootPeerGWT(String title, Root root) {
		super(root);
		 canvasHolder = RootPanel.get(title);
	}

	@Override
	public int getWidth() {
		String width = canvasHolder.getElement().getStyle().getProperty("width");
		return Integer.parseInt(width);
	}

	@Override
	public int getHeight() {
		String height = canvasHolder.getElement().getStyle().getProperty("height");
		return Integer.parseInt(height);
	}

	@Override
	public void repaint() {
		canvasHolder.clear();

	}

	@Override
	public void setStyleName(String styleName){
		
	}
	
	@Override
	public void add(telford.common.Component component) {
			canvasHolder.add((Widget) component.getPeer().getRepresentative());
	}

	@Override
	public void remove(telford.common.Component component) {
		canvasHolder.remove((Widget) component.getPeer().getRepresentative());
	}

	@Override
	public void add(telford.common.Component component, Object constraint) {
	}

	@Override
	public Object getRepresentative() {
		return canvasHolder;
	}

	@Override
	public void setLayoutManager(telford.common.LayoutManager lm) {
	}

	@Override
	public void addMouseListener(int count) {
		// TODO
	}
}
