package tm.gwt.telford;

import telford.common.Canvas;
import telford.common.Component;
import telford.common.Container;
import telford.common.Display;
import telford.common.LayoutManager;
import telford.common.Random;
import telford.common.Timer;
import telford.common.peers.CanvasPeer;
import telford.common.peers.ComponentPeer;
import telford.common.peers.ContainerPeer;

public class KitGWT extends telford.common.Kit {

	@Override
	public telford.common.Font getFont() {
		return new FontGWT() ;
	}
	
	@Override
	public telford.common.Font getFont(String name, int style, int size) {
		return new FontGWT(name, style, size) ;
	}
	@Override
	public Display getDisplay() {	
		Display	display = new DisplayGWT();
		return display ;
	}
	
	@Override
	public telford.common.peers.RootPeer makeRootPeer (String title, telford.common.Root root) {
		return new RootPeerGWT( title, root ) ;
	}

	@Override
	public telford.common.peers.ButtonPeer makeButtonPeer(String title, telford.common.Button button) {
		return new ButtonPeerGWT ( title, button);
	}

	@Override
	public ContainerPeer makeContainerPeer(Container container) {
		return new ContainerPeerGWT(container);
	}
	
//	@Override
//	public ContainerPeer makeContainerPeer(Container container, int type) {
//		return new ContainerPeerGWT(container, type);
//	}
	
	@Override
	public CanvasPeer makeCanvasPeer(Canvas canvas) {
		return new CanvasPeerGWT(canvas);
	}

	@Override
	public telford.common.BorderLayout getBorderLayoutManager() {
		return new BorderLayoutGWT();
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
	
