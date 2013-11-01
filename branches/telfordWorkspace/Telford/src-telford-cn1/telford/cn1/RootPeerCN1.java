package telford.cn1;

import telford.common.Root;

import com.codename1.ui.*;
import com.codename1.ui.layouts.Layout;

public class RootPeerCN1 extends telford.common.peers.RootPeer {
	MyForm myForm ;

	RootPeerCN1( String title, Root root ) {
		super( root ) ;
		myForm = new MyForm( ) ;

	}

	@Override
	public int getWidth() {
		return myForm.getWidth() ;
	}

	@Override
	public int getHeight() {
		return myForm.getHeight() ;
	}

	@Override
	public void repaint() {
		myForm.repaint() ;
	}
	
	@Override
	public void add(telford.common.Component component) {
		myForm.addComponent((Component) component.getPeer().getRepresentative());
	}

	@Override
	public void remove(telford.common.Component component) {
		myForm.removeComponent((Component) component.getPeer().getRepresentative());
	}
	
	@Override
	public void add(telford.common.Component component, Object constraint) {
		myForm.addComponent( constraint, (Component) component.getPeer().getRepresentative() );
		
	}
	
	@Override
	public Object getRepresentative() {
		return myForm;
	}

	@Override
	public void setLayoutManager(telford.common.LayoutManager lm) {
		Layout layout = (Layout) lm.getRepresentative() ;
		myForm.setLayout(layout);
	}
	
	@Override
	public void addMouseListener(int count) {
		//myForm.addMouseListener( new MouseListenerCN1(mouseListener));
	}

	
	class MyForm extends Form {
				
		@Override public void paint( Graphics g) {
			//super.paint(g);
			telford.common.Graphics tg = new GraphicsCN1( g) ;
			component.paintComponent(tg) ;
		}
		
		@Override
		public void pointerPressed(int x, int y) {
			super.pointerPressed(x, y);
			component.repaint();
		}
	}
	
}