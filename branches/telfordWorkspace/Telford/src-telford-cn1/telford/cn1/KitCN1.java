package telford.cn1;

import telford.common.Button;
import telford.common.Component;
import telford.common.Container;
import telford.common.Display;
import telford.common.LayoutManager;
import telford.common.Random;
import telford.common.Root;
import telford.common.Timer;
import telford.common.peers.ButtonPeer;
import telford.common.peers.ComponentPeer;
import telford.common.peers.ContainerPeer;
import telford.common.peers.RootPeer;

import com.codename1.ui.Font;
import com.codename1.ui.Form;

public class KitCN1 extends telford.common.Kit {

	private Display display;
	
	@Override
	public telford.common.Font getFont() {
		return new FontCN1(Font.createSystemFont( Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE)) ;
	}

	@Override
	public RootPeer makeRootPeer(String title, Root root) {
		return new RootPeerCN1( title, root ) ;
	}

	@Override
	public Display getDisplay() {
		if( display == null ) 
			display = new DisplayCN1();
		return display ;
	}

	@Override
	public ButtonPeer makeButtonPeer(String title, Button button) {
		return new ButtonPeerCN1 ( title, button);
	}

	@Override
	public ContainerPeer makeContainerPeer(Container container) {
		return new ContainerPeerCN1 (container);
	}

	@Override
	public ComponentPeer makeComponentPeer(Component component) {
		return new ComponentPeerCN1 (component);
	}
	
	@Override
	public telford.common.BorderLayout getBorderLayoutManager() {
		return new BorderLayoutCN1();
	}

	@Override
	public Timer getTimer(int delay,boolean repeats,telford.common.Root root, telford.common.ActionListener actionListener) {
		return new TimerCN1(delay,repeats,root, actionListener);
	}

	@Override
	public LayoutManager getFlowLayoutManager() {
		return new FlowLayoutCN1();
	}

	@Override
	public Random getRandom() {
		return new RandomCN1();
	}	

}
