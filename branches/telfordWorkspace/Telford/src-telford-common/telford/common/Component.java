package telford.common;

import telford.common.peers.ComponentPeer;

abstract public class Component {
	
	public Component () {
	}
	
	abstract public ComponentPeer getPeer();
	
}
