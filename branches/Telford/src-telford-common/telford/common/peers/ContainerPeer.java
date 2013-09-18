package telford.common.peers;

import telford.common.Component;
import telford.common.Container;

public abstract class ContainerPeer extends ComponentPeer {

	protected Container container ;
	
	public ContainerPeer( Container container) {this.container = container ; }

	abstract public void add(Component component);

	abstract public void remove(Component component);

}
