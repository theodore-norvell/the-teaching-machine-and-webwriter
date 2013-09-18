package telford.common.peers;

import ratRace.view.View;
import telford.common.*;

public abstract class RootPeer extends ContainerPeer {
	
	protected static Root root ;
	
	public RootPeer(Container container) {
		super(root);
	}
	
	abstract public int getWidth() ;
	
	abstract public int getHeight() ;
	
	abstract public void repaint() ;

	abstract public void remove(View view) ;

	abstract public void add(View view) ; 

}
