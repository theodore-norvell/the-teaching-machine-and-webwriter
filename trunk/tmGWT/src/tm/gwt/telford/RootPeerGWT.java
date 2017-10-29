package tm.gwt.telford;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import telford.common.Root;

public class RootPeerGWT extends telford.common.peers.RootPeer {
	 RootPanel panelHolder ;

	RootPeerGWT(String title, Root root) {
		super(root);
		 panelHolder = RootPanel.get(title);
	}

	@Override
	public int getWidth() {
		String width = panelHolder.getElement().getStyle().getProperty("width");
		return Integer.parseInt(width);
	}

	@Override
	public int getHeight() {
		String height = panelHolder.getElement().getStyle().getProperty("height");
		return Integer.parseInt(height);
	}

	@Override
	public void repaint() {
		panelHolder.clear();

	}

	@Override
	public void setStyleName(String styleName){
		
	}
	
	@Override
	public void add(telford.common.Component component) {
			panelHolder.add((Widget) component.getPeer().getRepresentative());
	}

	@Override
	public void remove(telford.common.Component component) {
		panelHolder.remove((Widget) component.getPeer().getRepresentative());
	}

	@Override
	public void add(telford.common.Component component, Object constraint) {
	}

	@Override
	public Object getRepresentative() {
		return panelHolder;
	}

	@Override
	public void setLayoutManager(telford.common.LayoutManager lm) {
	}

	@Override
	public void addMouseListener() { 
        // TODO
        throw new AssertionError("Mouse listeners for RootPeers, not supported.") ;
	}
}
