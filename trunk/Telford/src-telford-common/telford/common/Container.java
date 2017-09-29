package telford.common;

import telford.common.peers.ContainerPeer;
import telford.common.Component;

public class Container extends Component {

	public Container() {
		this(true);
	}

	public Container(boolean makePeer) {
		super();
		if (makePeer) {
			peer = Kit.getKit().makeContainerPeer(this);
			setLayoutManager(Kit.getKit().getFlowLayoutManager());
		}
	}

//	public Container(int type) {
//		super(false);
//		peer = Kit.getKit().makeContainerPeer(this, type);
//		setLayoutManager(Kit.getKit().getFlowLayoutManager());
//	}

	public void add(Component component) {
		// TODO: ask layout manager whether a null constraint is allowed, and
		// throw exception if not
		getPeer().add(component);
	}

	public void add(Component component, Object constraint) {
		getPeer().add(component, constraint);
	}

	public void remove(Component component) {
		getPeer().remove(component);
	}

	@Override
	public ContainerPeer getPeer() {
		return (ContainerPeer) peer;
	}

	public void setLayoutManager(LayoutManager lm) {
		getPeer().setLayoutManager(lm);
	}

	@Override
	public final void paintComponent(Graphics g) {
	}

}
