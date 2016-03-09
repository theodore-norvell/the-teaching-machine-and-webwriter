package ca.mun.engr;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JButton;

/**
 * Servlet implementation class ButtonClick
 */
@WebServlet("/ButtonClick")
public class MouseClick extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public MouseClick() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int x = Integer.parseInt(request.getParameter("x"));
		int y = Integer.parseInt(request.getParameter("y"));
//		
		
		int id = MouseEvent.MOUSE_PRESSED ;
		long now = System.currentTimeMillis() ;
		int modifiers = InputEvent.BUTTON1_MASK ;
		int xAbs = x ;		// ?
		int yAbs = y ;		// ?
		int clickCount = 1 ;
		boolean popupTrigger = false ;
		int button = 1 ;
		
		injectMouseEvent(ExternalRunner.getComponentRoot(), id, now, modifiers, x, y, xAbs, yAbs,
				clickCount, popupTrigger, button);
		
//		Triple<Component,Integer,Integer> focus = searchForMouseEventSource( ExternalRunner.getComponentRoot(), x, y) ;
//		Component focusTarget = focus.first;
//		focusTarget.requestFocus();
		
		id = MouseEvent.MOUSE_RELEASED ;
		now = System.currentTimeMillis() ;
		modifiers = InputEvent.BUTTON1_MASK ;
		xAbs = x ;		// ?
		yAbs = y ;		// ?
		clickCount = 1 ;
		popupTrigger = false ;
		button = 1 ;
		
		injectMouseEvent(ExternalRunner.getComponentRoot(), id, now, modifiers, x, y, xAbs, yAbs,
				clickCount, popupTrigger, button);
		
		id = MouseEvent.MOUSE_CLICKED ;
		now = System.currentTimeMillis() ;
		modifiers = InputEvent.BUTTON1_MASK ;
		xAbs = x ;		// ?
		yAbs = y ;		// ?
		clickCount = 1 ;
		popupTrigger = false ;
		button = 1 ;
		
		injectMouseEvent(ExternalRunner.getComponentRoot(), id, now, modifiers, x, y, xAbs, yAbs,
				clickCount, popupTrigger, button);
//		int id = ActionEvent.ACTION_PERFORMED;
//		long now = System.currentTimeMillis() ;
//		int modifiers = InputEvent.BUTTON1_MASK ;
//		
//		injectActionEvent(ExternalRunner.getComponentRoot(), id, now, modifiers, x, y, command);
		
//		response.setContentType("application/json");
//		PrintWriter out = response.getWriter();
//		out.append("{'status': 'ok'}");
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	private void injectMouseEvent(Component root, int id, long now, int modifiers,
			int x, int y, int xAbs, int yAbs, int clickCount,
			boolean popupTrigger, int button) {
		System.out.println( "Searching for point (" +x+ "," +y+ ")" ) ;
		Triple<Component,Integer,Integer> result = searchForMouseEventSource( root, x, y) ;
		if( result != null ) {
			Component source = result.first;
			//set Focus
			source.requestFocus();
			int localX = result.second;
			int localY = result.third;
			MouseEvent ev = new MouseEvent( source, id, now, modifiers, localX, localY,
					xAbs, yAbs, clickCount, popupTrigger, button) ;
//			KeyEvent ke = new KeyEvent(source, localY, now, localY, localY, 0, localY)
			System.out.println( "Found source. New event is " + ev ) ;
			fire(ev);
		} else {
			System.out.println( "No source found" ) ;
		}
	}
	
	
	private void fire(final AWTEvent ev) {
		EventQueue q = Toolkit.getDefaultToolkit().getSystemEventQueue() ;
		q.postEvent(ev);
		System.out.println( "Event posted" ) ;
	}


	private Triple<Component,Integer,Integer> searchForMouseEventSource( Component p, int x, int y ) {
		synchronized( p.getTreeLock() ) {
			return searchForMouseEventSourceImpl( p, x, y ) ;
		}
	}
	
	private Triple<Component,Integer,Integer> searchForMouseEventSourceImpl( Component comp, int x, int y ) {
		// Based on getMouseEventTarget in java.awt.container
		// Search for a descendant component that can take the event.
		if( comp instanceof Container ) {
			Container cont = (Container) comp ;
			for (int i = 0; i < cont.getComponentCount(); i++) {
				Component child = cont.getComponent(i) ;
				int childX = child.getX() ;
				int childY = child.getY();
				if( child != null && child.isVisible() && child.contains(x - childX, y - childY) ) {
					Triple<Component,Integer,Integer> result
					    = searchForMouseEventSourceImpl( child, x-childX, y-childY );
					if( result != null ) return result ;
				}
			}
		}
		// If we get here there, is no descendant that can take the event.
		if( comp.contains(x,y) && takesMouseEvents( comp ) ) {
			return new Triple<Component,Integer,Integer>( comp, x, y) ; }
		else {
			return null ; }
	}

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
