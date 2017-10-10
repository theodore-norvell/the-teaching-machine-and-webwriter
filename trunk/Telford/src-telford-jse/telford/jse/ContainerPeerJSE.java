package telford.jse;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import telford.jse.UtilJSE;

public class ContainerPeerJSE extends telford.common.peers.ContainerPeer {

	Container myContainer ;

	ContainerPeerJSE(telford.common.Container container) {
		super ( container );
		myContainer = new MyContainer();
	}
	
	ContainerPeerJSE(telford.common.Container container, int type) {
		super ( container );
		myContainer = new MyContainer();
	}

	@Override
	public void add(telford.common.Component component) {
		myContainer.add((Component) component.getPeer().getRepresentative());
	}

	@Override
	public void remove(telford.common.Component component) {
		myContainer.remove((Component) component.getPeer().getRepresentative());
	}

	@Override
	public void add(telford.common.Component component, Object constraint) {
		myContainer.add( (Component) component.getPeer().getRepresentative(), constraint);
	}

	@Override
	public void repaint() {
		UtilJSE.repaint(myContainer);
	}

	@Override
	public int getWidth() {
		return UtilJSE.getWidth(myContainer);
	}

	@Override
	public int getHeight() {
		return UtilJSE.getHeight(myContainer);
	}

	@Override
	public Object getRepresentative() {
		return myContainer;
	}

	@Override
	public void setLayoutManager(telford.common.LayoutManager lm) {
		myContainer.setLayout((LayoutManager) lm.getRepresentative());
	}

	@Override
	public void addMouseListener() {
		UtilJSE.addMouseListener(myContainer, component);
	}
	
	@Override
	public void setStyleName(String styleName){
		
	}
	class MyContainer extends JPanel {
	}


}
