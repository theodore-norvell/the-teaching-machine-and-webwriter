package telford.common;

import telford.common.peers.ContainerPeer;
import telford.common.Component;

public  class Container {
	ContainerPeer peer ;
	
	public Container ()  {
		peer = Kit.getKit().makeContainerPeer(this);
	}
	
	
	public void add(Component component) {
		peer.add(component);
	}
	
	public void remove(Component component) {
		peer.remove(component);
	}
}
