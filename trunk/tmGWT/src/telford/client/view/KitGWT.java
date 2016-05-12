package telford.client.view;


import telford.common.Component;
import telford.common.Container;
import telford.common.Display;
import telford.common.LayoutManager;
import telford.common.Random;
import telford.common.Timer;
import telford.common.peers.ComponentPeer;
import telford.common.peers.ContainerPeer;

public class KitGWT extends telford.common.Kit {

	private Display display;

	@Override
	public telford.common.Font getFont() {
		String f = "normal 16px serif";
		return new FontGWT(f) ;
	}
	
	@Override
	public telford.common.peers.RootPeer makeRootPeer (String title, telford.common.Root root) {
		return new RootPeerGWT( title, root ) ;
	}
	
	@Override
	public Display getDisplay() {	
		if( display == null ) 
			display = new DisplayGWT();
		return display ;
	}

	@Override
	public telford.common.peers.ButtonPeer makeButtonPeer(String title, telford.common.Button button) {
		return new ButtonPeerGWT ( title, button);
	}

	@Override
	public ContainerPeer makeContainerPeer(Container container) {
		//TODO
		return null;
	}
	
	@Override
	public ComponentPeer makeComponentPeer(Component component) {
		return new ComponentPeerGWT(component);
	}

	@Override
	public telford.common.BorderLayout getBorderLayoutManager() {
		//TODO 
		return null;
	}

	@Override
	public Timer getTimer(int delay,boolean repeats, telford.common.Root root, telford.common.ActionListener actionListener) {
		//TODO 
		return null;
	}

	@Override
	public LayoutManager getFlowLayoutManager() {
		//TODO 
		return null;
	}

	@Override
	public Random getRandom() {
		//TODO 
		return null;
	}
}
	
