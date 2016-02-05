package telford.jse;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class ComponentPeerJSE extends telford.common.peers.ComponentPeer{

	MyComponent myComponent;
	
	public ComponentPeerJSE(telford.common.Component component) {
		super(component);
		myComponent = new MyComponent();
	}

	@Override
	public Object getRepresentative() {
		return myComponent;
	}

	@Override
	public void addMouseListener(int count) {
		UtilJSE.addMouseListener(myComponent, count, component);
	}

	@Override
	public int getWidth() {
		return UtilJSE.getWidth(myComponent);
	}

	@Override
	public int getHeight() {
		return UtilJSE.getHeight(myComponent);
	}

	@Override
	public void repaint() {
		UtilJSE.repaint(myComponent);
	}
	

	class MyComponent extends JPanel{
		@Override public void paintComponent( Graphics g) {
			super.paintComponent(g);
			telford.common.Graphics tg = new GraphicsJSE( (Graphics2D) g) ;
			component.paintComponent(tg) ;
		}
	}
	
}