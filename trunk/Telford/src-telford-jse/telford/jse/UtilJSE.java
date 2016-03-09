package telford.jse;

import java.awt.Component;

public class UtilJSE {
	
	public static void addMouseListener(Component component, int count, telford.common.Component component2){
		
			int oldCount = component.getMouseListeners().length ;
			if( oldCount == 0 && count > 0 ) {
				MouseListenerJSE ml =  new MouseListenerJSE(component2) ;
				component.addMouseListener( ml );
				component.addMouseMotionListener( ml ); }
			else if( oldCount > 0 && count == 0 ) {
				MouseListenerJSE old = (MouseListenerJSE) component.getMouseListeners()[0] ;
				component.removeMouseListener( old ); 
				component.removeMouseMotionListener(old); 
			}
	}
	
	public static int getWidth(Component component){
		return component.getWidth();
	}
	
	public static int getHeight(Component component){
		return component.getHeight();
	}
	
	public static void repaint(Component component){
		component.repaint();
	}
}
