package telford.common;

import telford.common.peers.ContainerPeer;
import telford.common.Component;

public class Container extends Component {
	ContainerPeer peer ;
	
	public Container ()  {
		peer = Kit.getKit().makeContainerPeer(this);
		//TODO: Choose right layoutManager for Container;
		setLayoutManager(Kit.getKit().getFlowLayoutManager());
	}
		
	public void add(Component component) {
		peer.add(component);
	}

	public void add(Component component, Object constraint) {
		peer.add(component, constraint);
	}
	
	public void remove(Component component) {
		peer.remove(component);
	}
	
	@Override
	public ContainerPeer getPeer() {
		return peer;
	}

	public void setLayoutManager(LayoutManager lm) {
		peer.setLayoutManager(lm);
	}
	
}
