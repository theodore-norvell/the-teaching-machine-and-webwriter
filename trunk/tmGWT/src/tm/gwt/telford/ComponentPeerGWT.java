package tm.gwt.telford;
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
		return myComponent.getOffsetWidth();
	}

	@Override
	public int getHeight() {
		return myComponent.getOffsetHeight();
	}

	@Override
	public void repaint() {
	}
	
	@Override
	public void setStyleName(String styleName){
		myComponent.setStyleName(styleName);
	}
	class MyComponent extends Widget{
		public void paintComponent() {
		}
	}
}