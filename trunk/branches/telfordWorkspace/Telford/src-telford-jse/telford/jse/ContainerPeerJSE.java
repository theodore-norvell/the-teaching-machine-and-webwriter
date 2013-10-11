package telford.jse;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class ContainerPeerJSE extends telford.common.peers.ContainerPeer {

	Container myContainer ;

	ContainerPeerJSE(telford.common.Container container) {
		super ( container );
		myContainer = new MyContainer();
	}

	@Override
	public void add(telford.common.Component component) {
		myContainer.add((Component) component.getPeer().getRepresentative());
	}

	@Override
	public void remove(telford.common.Component component) {
		myContainer.remove((Component) component.getPeer().getRepresentative());
	}

	@Override
	public void add(telford.common.Component component, Object constraint) {
		myContainer.add( (Component) component.getPeer().getRepresentative(), constraint);
	}

	@Override
	public void repaint() {
		myContainer.repaint();
	}

	@Override
	public int getWidth() {
		return myContainer.getWidth();
	}

	@Override
	public int getHeight() {
		return myContainer.getHeight();
	}

	@Override
	public Object getRepresentative() {
		return myContainer;
	}

	@Override
	public void setLayoutManager(telford.common.LayoutManager lm) {
		myContainer.setLayout((LayoutManager) lm.getRepresentative());
	}

	@Override
	public void addMouseListener(telford.common.MouseListener mouseListener) {
		MouseListenerJSE ml =  new MouseListenerJSE(mouseListener) ;
		myContainer.addMouseListener( ml );
		myContainer.addMouseMotionListener( ml );
	}

	class MyContainer extends JPanel {
		@Override public void paintComponent( Graphics g) {
			super.paintComponent(g);
			telford.common.Graphics tg = new GraphicsJSE( (Graphics2D) g) ;
			component.paintComponent(tg) ;
		}


	}


}
