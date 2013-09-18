package telford.cn1;


import telford.common.Root;
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
	
	

}
