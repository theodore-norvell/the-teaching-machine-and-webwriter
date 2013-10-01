package telford.cn1;

import com.codename1.ui.*;

public class RootPeerCN1 extends telford.common.peers.RootPeer {
	Form form ;

	RootPeerCN1( String title, telford.common.Root root ){
		super(root) ;
		form = new MyForm( title ) ;
	}

	@Override
	public int getWidth() { return form.getWidth() ; }

	@Override
	public int getHeight() { return form.getHeight() ; }
	
	@Override
	public void show() { form.show(); }
	
	@Override
	public void repaint() { form.repaint(); }



	class MyForm extends Form {

		MyForm( String title ) { super(title) ; }

		@Override public void paint( Graphics g ) {
			GraphicsCN1 wrapper = new GraphicsCN1(g) ;
			root.paint( wrapper ) ;
		}
		
		@Override public void pointerPressed( int x, int y ) {
			System.out.println("pointerPressed0") ;
			super.pointerPressed( x, y ) ;
			System.out.println("pointerPressed1") ;
			root.pointerPressed( x, y ) ;
			System.out.println("pointerPressed2") ;
		}
		
		@Override public void pointerReleased( int x, int y ) {
			System.out.println("pointerReleased0") ;
			super.pointerReleased( x, y ) ;
			System.out.println("pointerReleased1") ;
			root.pointerReleased( x, y ) ;
			System.out.println("pointerReleased2") ;
		}
		
		@Override public void pointerDragged( int x, int y ) {
			System.out.println("pointerDragged0") ;
			super.pointerDragged( x, y ) ;
			System.out.println("pointerDragged1") ;
			root.pointerDragged( x, y ) ;
			System.out.println("pointerDragged2") ;
		}
		
		@Override public void pointerDragged( int[] x, int[] y ) {
			System.out.println("pointerDragged0[]") ;
			super.pointerDragged( x, y ) ;
			System.out.println("pointerDragged1[]") ;
			if( x.length > 0 && y.length > 0) root.pointerDragged( x[0], y[0] ) ;
			System.out.println("pointerDragged2[]") ;
		}
	}



	@Override
	public void setSize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

}