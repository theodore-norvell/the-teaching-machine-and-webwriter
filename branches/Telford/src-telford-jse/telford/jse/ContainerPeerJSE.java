package telford.jse;

import java.awt.Container;
import java.awt.Component;

public class ContainerPeerJSE extends telford.common.peers.ContainerPeer {
	
	MyContainer myContainer ;

	ContainerPeerJSE(telford.common.Container container) {
		super ( container );
		myContainer = new MyContainer();
	}
	
	@Override
	public void add(telford.common.Component component) {
		myContainer.add((Component) component.getPeer().getRepresentative());
	}

	@Override
	public void remove(telford.common.Component component) {
		myContainer.remove((Component) component.getPeer().getRepresentative());
	}

	@Override
	public Object getRepresentative() {
		return myContainer;
	}
	
	class MyContainer extends Container {
	}
}
