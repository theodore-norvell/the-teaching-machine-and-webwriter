package telford.common;

public class Root extends Container{
	
	public Root( String title ) {
		super(false) ;
		peer = Kit.getKit().makeRootPeer( title, this ) ; //override peer created in Container
		setLayoutManager(Kit.getKit().getBorderLayoutManager());
	}

	public void add(Container container, Object constraint) {
		getPeer().add(container, constraint);
	}
	
}
