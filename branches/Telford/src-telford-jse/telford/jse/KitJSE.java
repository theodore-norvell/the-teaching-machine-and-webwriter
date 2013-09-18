package telford.jse;

import java.awt.*;

import telford.common.Container;
import telford.common.Display;
import telford.common.peers.ContainerPeer;

public class KitJSE extends telford.common.Kit {

	private Display display;

	@Override
	public telford.common.Font getFont() {
		Font f = new java.awt.Font(Font.SERIF, Font.PLAIN, 18);
		return new FontJSE(f) ;
	}
	
	@Override
	public Object getNorth() {
		new java.awt.BorderLayout();
		String north = BorderLayout.NORTH;
		return north;
	}
	
	@Override
	public telford.common.BorderLayout getBorderLayout() {
		java.awt.BorderLayout b = new java.awt.BorderLayout();
		return new BorderLayoutJSE(b);
	}

	@Override
	public telford.common.peers.RootPeer makeRootPeer (String title, telford.common.Root root) {
		return new RootPeerJSE( title, root ) ;
	}
	
	@Override
	public Display getDisplay() {	
		if( display == null ) 
			display = new DisplayJSE();
		return display ;
	}

	@Override
	public telford.common.peers.ButtonPeer makeButtonPeer(String title, telford.common.Button button) {
		return new ButtonPeerJSE ( title, button);
	}

	@Override
	public ContainerPeer makeContainerPeer(Container container) {
		return new ContainerPeerJSE (container);
	}

}
