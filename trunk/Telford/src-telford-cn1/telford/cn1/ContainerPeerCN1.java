package telford.cn1;

import telford.common.MouseEvent;

import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.layouts.Layout;


public class ContainerPeerCN1 extends telford.common.peers.ContainerPeer {
	Container myContainer ;
	
	ContainerPeerCN1(telford.common.Container container) {
		super ( container );
		myContainer = new MyContainer();
	}

	@Override
	public void add(telford.common.Component component) {
		myContainer.addComponent((Component) component.getPeer().getRepresentative());
	}

	@Override
	public void remove(telford.common.Component component) {
		myContainer.removeComponent((Component) component.getPeer().getRepresentative());
	}

	@Override
	public void add(telford.common.Component component, Object constraint) {
		myContainer.addComponent(constraint, (Component) component.getPeer().getRepresentative());
	}

	@Override
	public void repaint() {
		myContainer.repaint();
	}

	@Override
	public int getWidth() {
		return myContainer.getWidth();
	}

	@Override
	public int getHeight() {
		return myContainer.getHeight();
	}

	@Override
	public Object getRepresentative() {
		return myContainer;
	}

	@Override
	public void setLayoutManager(telford.common.LayoutManager lm) {
		myContainer.setLayout((Layout) lm.getRepresentative());
	}

	@Override
	public void addMouseListener(int count) {
		//default do nothing.
	}

	class MyContainer extends Container {
		
		@Override
		public void pointerDragged(int x, int y) {
		    super.pointerDragged(x, y);
			component.fireMouseMoved(new MouseEvent(x,y));
		    System.out.println("x: " + x + " y:" + y);
		}
	}

}
