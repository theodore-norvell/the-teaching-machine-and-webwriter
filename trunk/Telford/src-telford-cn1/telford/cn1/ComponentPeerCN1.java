package telford.cn1;

import telford.common.MouseEvent;

import com.codename1.ui.Component;
import com.codename1.ui.Graphics;

public class ComponentPeerCN1 extends telford.common.peers.ComponentPeer{

	MyComponent myComponent;
	
	public ComponentPeerCN1(telford.common.Component component) {
		super(component);
		myComponent = new MyComponent();
	}

	@Override
	public Object getRepresentative() {
		return myComponent;
	}

	@Override
	public void addMouseListener(int count) {
		//default do nothing.
	}

	@Override
	public int getWidth() {
		return myComponent.getWidth();
	}

	@Override
	public int getHeight() {
		return myComponent.getHeight();
	}

	@Override
	public void repaint() {
		myComponent.repaint();
		System.out.println("repaint component");
	}
	

	class MyComponent extends Component{
		@Override public void paint(Graphics g) {
			telford.common.Graphics tg = new GraphicsCN1( (Graphics) g) ;
			component.paintComponent(tg) ;
			System.out.println("paint component");
			System.out.println(System.currentTimeMillis());			
		}
		
		@Override protected void paintBackground(Graphics g) {
			super.paintBackground(g);
			System.out.println("painting background");
			System.out.println(System.currentTimeMillis());			
		}

		@Override public void setY(int y) {
			super.setY(y);
			System.out.println("y is: " + y);
		};
		
		@Override
		public void pointerDragged(int x, int y) {
		    super.pointerDragged(x, y);
			component.fireMouseMoved(new MouseEvent(x,y));
		    System.out.println("x: " + x + " y:" + y);
		}
	}

}
