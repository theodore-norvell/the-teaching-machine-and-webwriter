package telford.cn1;


import telford.common.ActionListener;
import telford.common.Button;
import telford.common.Container;
import telford.common.Display;
import telford.common.LayoutManager;
import telford.common.Root;
import telford.common.Timer;
import telford.common.peers.ButtonPeer;
import telford.common.peers.ContainerPeer;
import telford.common.peers.RootPeer;

import com.codename1.ui.Font;
import com.codename1.ui.Form;

public class KitCN1 extends telford.common.Kit {

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ButtonPeer makeButtonPeer(String title, Button button) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContainerPeer makeContainerPeer(Container container) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LayoutManager getBorderLayoutManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timer getTimer(int delay, ActionListener actionListener) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
