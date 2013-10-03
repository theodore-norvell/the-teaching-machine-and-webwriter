package telford.common.peers;

import telford.common.Component;
import telford.common.Container;
import telford.common.LayoutManager;

public abstract class ContainerPeer extends ComponentPeer {
	
	public ContainerPeer( Container container) {
		super(container);
	}

	abstract public void add(Component component);

	abstract public void remove(Component component);

	public abstract int getWidth();

	public abstract int getHeight() ;

	public abstract void add(Component component, Object constraint) ;

	public abstract void setLayoutManager(LayoutManager lm);

	
}
