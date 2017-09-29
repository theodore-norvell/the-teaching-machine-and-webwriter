package telford.jse;

import java.awt.Font;

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

public class KitJSE extends telford.common.Kit {

	private Display display;

	@Override
	public telford.common.Font getFont() {
		Font f = new java.awt.Font(Font.SERIF, Font.PLAIN, 18);
		return new FontJSE(f) ;
	}
	
	public telford.common.Font getFont(String name, int style, int size) {
		return new FontJSE(name, style, size);
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
	
	public ContainerPeer makeContainerPeer(Container container, int type){
		return new ContainerPeerJSE (container, type);
	}
	
	@Override
	public CanvasPeer makeCanvasPeer(Canvas canvas) {
		return new CanvasPeerJSE (canvas);
	}

	@Override
	public telford.common.BorderLayout getBorderLayoutManager() {
		return new BorderLayoutJSE();
	}

	@Override
	public Timer getTimer(int delay,boolean repeats, telford.common.Root root, telford.common.ActionListener actionListener) {
		return new TimerJSE(delay,repeats, root, actionListener);
	}

	@Override
	public LayoutManager getFlowLayoutManager() {
		return new FlowLayoutJSE();
	}

	@Override
	public Random getRandom() {
		return new RandomJSE();
	}
	
}
	
