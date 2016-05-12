package telford.client.view;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.RootPanel;

import telford.common.Root;

public class RootPeerGWT extends telford.common.peers.RootPeer{
	RootPanel canvasHolder ;

	RootPeerGWT( String title, Root root ) {
		super( root ) ;
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
	public void add(telford.common.Component component) {
		canvasHolder.add((Canvas) component.getPeer().getRepresentative());
	}

	@Override
	public void remove(telford.common.Component component) {
		canvasHolder.remove((Canvas) component.getPeer().getRepresentative());
	}
	
	@Override
	public void add(telford.common.Component component, Object constraint) {
		//TOOD
	}
	
	@Override
	public Object getRepresentative() {
		return canvasHolder;
	}

	@Override
	public void setLayoutManager(telford.common.LayoutManager lm) {
		//TODO
	}
	
	@Override
	public void addMouseListener(int count) {
		//TODO
	}
}
