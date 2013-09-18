package telford.jse;

import javax.swing.*;

import java.awt.*; 
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Component;

import ratRace.view.View;
import telford.common.Root;

class RootPeerJSE extends telford.common.peers.RootPeer{
	MyPanel myPanel ;

	RootPeerJSE( String title, Root root ) {
		super( root ) ;
		myPanel = new MyPanel( ) ;
		myPanel.addMouseListener( new MyMouseListener() ) ;
		myPanel.addMouseMotionListener( new MyMouseListener() ) ;
		
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
	public void remove(View view) {
		myPanel.remove(view);
	}

	@Override
	public void add(View view) {
		myPanel.add(view);
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
	public Object getRepresentative() {
		return myPanel;
	}
		
	class MyMouseListener implements MouseListener, MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			root.pointerDragged( e.getX(), e.getY() ) ;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			root.pointerPressed( e.getX(), e.getY() ) ;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			root.pointerReleased( e.getX(), e.getY() ) ;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
		
	}
	
	class MyPanel extends JPanel {
				
		@Override public void paintComponent( Graphics g) {
			telford.common.Graphics tg = new GraphicsJSE( (Graphics2D) g) ;
			root.paint(tg) ;
		}
	}
	
}
