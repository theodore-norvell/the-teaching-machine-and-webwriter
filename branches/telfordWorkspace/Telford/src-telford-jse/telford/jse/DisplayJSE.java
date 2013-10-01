package telford.jse;

import javax.swing.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.WindowConstants;

import telford.common.*;

public class DisplayJSE extends JFrame implements Display{
	protected Root root;

	DisplayJSE () {
		addMouseListener( new MyMouseListener() ) ;
		addMouseMotionListener( new MyMouseListener() ) ;
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(500, 500);
		setVisible(true);
	}
	@Override
	public void setRooot(Root root) {
		this.root = root;
	}

	@Override
	public Root getRoot() {
		return root;
	}

	@Override
	public void setPreferredSize(int width, int height) {
		setSize(width, height);
	}
	
	@Override
	public void add(Container toolBar, Object north) {
		add(toolBar, north);
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

}
