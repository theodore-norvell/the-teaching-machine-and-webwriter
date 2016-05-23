package telford.gwt;

import com.google.gwt.user.client.ui.Widget;

public class ComponentPeerGWT extends telford.common.peers.ComponentPeer{

	MyComponent myComponent;
	
	public ComponentPeerGWT(telford.common.Component component) {
		super(component);
		myComponent = new MyComponent();
	}

	@Override
	public Object getRepresentative() {
		return myComponent;
	}

	@Override
	public void addMouseListener(int count) {
	}

	@Override
	public int getWidth() {
		return UtilGWT.getWidth(myComponent);
	}

	@Override
	public int getHeight() {
		return UtilGWT.getHeight(myComponent);
	}

	@Override
	public void repaint() {
	}
	
	class MyComponent extends Widget{
		public void paintComponent() {
		}
	}
}