package telford.jse;

import javax.swing.*;

import java.awt.*; 

import telford.common.Root;

class RootPeerJSE extends telford.common.peers.RootPeer{
	MyPanel myPanel ;

	RootPeerJSE( String title, Root root ) {
		super( root ) ;
		myPanel = new MyPanel( ) ;

	}

	@Override
	public int getWidth() {
		return myPanel.getWidth() ;
	}

	@Override
	public int getHeight() {
		return myPanel.getHeight() ;
	}

	@Override
	public void repaint() {
		myPanel.repaint() ;
	}
	
	@Override
	public void setStyleName(String styleName){
		
	}
	
	@Override
	public void add(telford.common.Component component) {
		myPanel.add((Component) component.getPeer().getRepresentative());
	}

	@Override
	public void remove(telford.common.Component component) {
		myPanel.remove((Component) component.getPeer().getRepresentative());
	}
	
	@Override
	public void add(telford.common.Component component, Object constraint) {
		myPanel.add( (Component) component.getPeer().getRepresentative(), constraint);
	}
	
	@Override
	public Object getRepresentative() {
		return myPanel;
	}

	@Override
	public void setLayoutManager(telford.common.LayoutManager lm) {
		myPanel.setLayout((LayoutManager) lm.getRepresentative());
	}
	
	@Override
	public void addMouseListener(int count) {
		UtilJSE.addMouseListener(myPanel, count, component);

	}

	
	class MyPanel extends JPanel {
				
		@Override public void paintComponent( Graphics g) {
			telford.common.Graphics tg = new GraphicsJSE( (Graphics2D) g) ;
			component.paintComponent(tg) ;
		}
	}


}
