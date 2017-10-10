package telford.common.peers;

import telford.common.Component;

public abstract class ComponentPeer {
	
	protected Component component;
	
	public ComponentPeer(Component component){
		this.component = component;
	}
	
	public abstract Object getRepresentative() ;

	public abstract void addMouseListener();

	public abstract int getWidth();

	public abstract int getHeight() ;
	
	public abstract void repaint();
	
	public abstract void setStyleName(String styleName);

}
