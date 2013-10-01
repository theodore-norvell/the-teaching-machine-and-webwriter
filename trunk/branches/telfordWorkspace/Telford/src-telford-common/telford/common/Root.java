package telford.common;

import ratRace.view.View;
import telford.common.peers.*;

public class Root extends Container{
	RootPeer peer ;
	
	public Root( String title ) {
		peer = Kit.getKit().makeRootPeer( title, this ) ;
	}
	
	public void paint( Graphics g ) {
		// default do nothing.
	}
	

	public void pointerPressed( int x, int y ) {
		// default do nothing.
	}

	public void pointerReleased( int x, int y ) {
		// default do nothing.
	}
	

	public void pointerDragged( int x, int y ) {
		// default do nothing.
	}
	
	public void repaint() { peer.repaint() ; }
	
	public int getWidth() { return peer.getWidth() ; }
	
	public int getHeight() { return peer.getHeight() ; }
	
	public void remove(View view) {
		peer.remove(view); //put it under root
	} 

	public void add(View view){
		peer.add(view);
	}

}
