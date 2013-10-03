package telford.common.peers;

import telford.common.Component;
import telford.common.MouseListener;

public abstract class ComponentPeer {
	
	protected Component component;
	
	public ComponentPeer(Component component){
		this.component = component;
	}
	
	public abstract Object getRepresentative() ;

	public abstract void addMouseListener(MouseListener mouseListener);

	public abstract int getWidth();

	public abstract int getHeight() ;
	
	public abstract void repaint();

}
