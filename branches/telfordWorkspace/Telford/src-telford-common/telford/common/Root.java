package telford.common;

public class Root extends Container{
	
	public Root( String title ) {
		peer = Kit.getKit().makeRootPeer( title, this ) ; //override peer created in Container
		setLayoutManager(Kit.getKit().getBorderLayoutManager());
	}

	public void add(Container container, Object constraint) {
		peer.add(container, constraint);
	}

	
}
