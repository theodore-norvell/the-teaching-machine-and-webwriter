package ca.mun.engr.servlet;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JTextField;

import ca.mun.engr.ExternalRunner;

/**
 * Servlet implementation class KeyClick
 */
@WebServlet("/KeyClick")
public class KeyClick extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public KeyClick() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int keycode = Integer.parseInt(request.getParameter("Code"));
		char keychar = request.getParameter("Char").charAt(0);
		int index = Integer.parseInt(request.getParameter("index"));
//		Cookie[] cookies = request.getCookies();
//        int index = -1;
//        
//        for(int i = 0; i < cookies.length; i++) { 
//            Cookie cookie1 = cookies[i];
//            if (cookie1.getName().equals("index")) {
//            	index = Integer.parseInt(cookie1.getValue());
//            	}
//        }
		
		ExternalRunner erKey = ExternalRunner.getInstance();
		
		int id = KeyEvent.KEY_PRESSED;
		long now = System.currentTimeMillis();
		int modifiers = 0x00;
		int keyCode = keycode;
		char keyChar = keychar;
		injectKeyEvent(erKey.getComponentRoot(index), id, now, modifiers,
				keyCode, keyChar, KeyEvent.KEY_LOCATION_STANDARD, 0x45);
		
		id = KeyEvent.KEY_TYPED;
		now = System.currentTimeMillis();
		modifiers = 0x00;
		keyCode = 0;
		keyChar = keychar;
		injectKeyEvent(erKey.getComponentRoot(index), id, now, modifiers,
				keyCode, keyChar, KeyEvent.KEY_LOCATION_UNKNOWN, 0x45);
		
		id = KeyEvent.KEY_RELEASED;
		now = System.currentTimeMillis();
		modifiers = 0x00;
		keyCode = keycode;
		keyChar = keychar;
		injectKeyEvent(erKey.getComponentRoot(index), id, now, modifiers,
				keyCode, keyChar, KeyEvent.KEY_LOCATION_STANDARD, 0x45);
		
		response.getWriter().append("{}");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private void injectKeyEvent(Component root, int id, long now, int modifiers,
			int keyCode, char keyChar, int keyLoc, long extendedKeyCode){
		System.out.println("injecting key event");
//		if(!txt.isFocusOwner()){
//			txt.requestFocus();
//		}
		
		KeyEvent ev = new KeyEvent(root, id, now, modifiers,
                keyCode, keyChar, keyLoc);
		
		Field ekc = null;
		try {
			ekc = ev.getClass().getDeclaredField("extendedKeyCode");
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ekc.setAccessible(true);
		
		try {
			ekc.set(ev, extendedKeyCode);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fire(ev);
	}
	
	private void fire(final AWTEvent ev) {
		EventQueue q = Toolkit.getDefaultToolkit().getSystemEventQueue() ;
		q.postEvent(ev);
		System.out.println( "Event posted" ) ;
	}


//	private Triple<Component,Integer,Integer> searchForMouseEventSource( Component p, int x, int y ) {
//		synchronized( p.getTreeLock() ) {
//			return searchForMouseEventSourceImpl( p, x, y ) ;
//		}
//	}
	
//	private Triple<Component,Integer,Integer> searchForMouseEventSourceImpl( Component comp ) {
//		// Based on getMouseEventTarget in java.awt.container
//		// Search for a descendant component that can take the event.
//		if( comp instanceof JTextField ) {
//			Container cont = (JTextField) comp ;
//			for (int i = 0; i < cont.getComponentCount(); i++) {
//				Component child = cont.getComponent(i) ;
//				if( child.isFocusOwner() ) {
//					Triple<Component,Integer,Integer> result
//					    = searchForMouseEventSourceImpl( child, x-childX, y-childY );
//					if( result != null ) return result ;
//				}
//			}
//		}
//		// If we get here there, is no descendant that can take the event.
//		if( comp.contains(x,y) && takesMouseEvents( comp ) ) {
//			return new Triple<Component,Integer,Integer>( comp, x, y) ; }
//		else {
//			return null ; }
//	}

	private boolean takesMouseEvents( Component comp ) {
		// The following is what we want (from java.awt.Container)
		//return (comp.eventMask & AWTEvent.MOUSE_MOTION_EVENT_MASK) != 0
		//        || (comp.eventMask & AWTEvent.MOUSE_EVENT_MASK) != 0
		//        || (comp.eventMask & AWTEvent.MOUSE_WHEEL_EVENT_MASK) != 0
		//        || comp.mouseListener != null
		//        || comp.mouseMotionListener != null
		//        || comp.mouseWheelListener != null;
		// The next line covers the last 3 cases, I think.
		return comp.getMouseListeners().length != 0 
			|| comp.getMouseMotionListeners().length != 0
			|| comp.getMouseWheelListeners().length != 0 ;
	}
}
