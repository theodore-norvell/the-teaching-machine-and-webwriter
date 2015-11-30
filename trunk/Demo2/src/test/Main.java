package test;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class Main {
	private JTextField txt = null;
	
	public Main(){
		JFrame frame = new JFrame();
		frame.setBounds(400, 100, 400, 600);
		frame.setLayout(null);
		frame.setVisible(true);
		JButton but1 = new JButton("test");
		but1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("test triggered");
				System.out.println(e);
			}});
		but1.setBounds(10, 10, 100, 20);
		frame.add(but1);
		
		JButton but2 = new JButton("go");
		but2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int id = MouseEvent.MOUSE_PRESSED;
				long now = System.currentTimeMillis() ;
				int modifiers = InputEvent.BUTTON1_MASK ;
				injectMouseEvent(frame, id, now, modifiers, 50, 50,50,50,1,false, MouseEvent.BUTTON1);
				
				id = MouseEvent.MOUSE_RELEASED;
				now = System.currentTimeMillis() ;
				modifiers = InputEvent.BUTTON1_MASK ;
				injectMouseEvent(frame, id, now, modifiers, 50, 50, 50,50,1,false, MouseEvent.BUTTON1);
			}

			
		});
		but2.setBounds(10, 50, 100, 20);
		frame.add(but2);
		
		JButton but3 = new JButton("input");
		but3.setBounds(10, 100, 100, 20);
		but3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
//				if(!txt.isFocusOwner()){
//					txt.requestFocus();
//				}
				int id = KeyEvent.KEY_PRESSED;
				long now = System.currentTimeMillis();
				int modifiers = 0x00;
				int keyCode = 69;
				char keyChar = 'e';
				injectKeyEvent(frame, id, now, modifiers,
						keyCode, keyChar, KeyEvent.KEY_LOCATION_STANDARD, 0x45);
				
				id = KeyEvent.KEY_TYPED;
				now = System.currentTimeMillis();
				modifiers = 0x00;
				keyCode = 0;
				keyChar = 'e';
				injectKeyEvent(frame, id, now, modifiers,
						keyCode, keyChar, KeyEvent.KEY_LOCATION_UNKNOWN, 0x45);
				
				id = KeyEvent.KEY_RELEASED;
				now = System.currentTimeMillis();
				modifiers = 0x00;
				keyCode = 69;
				keyChar = 'e';
				injectKeyEvent(frame, id, now, modifiers,
						keyCode, keyChar, KeyEvent.KEY_LOCATION_STANDARD, 0x45);
			}});
		frame.add(but3);
		
		txt = new JTextField();
		txt.setBounds(10, 150, 100, 20);
		frame.add(txt);
	}

	private void injectMouseEvent(Component root, int id, long now, int modifiers,
			int x, int y, int xAbs, int yAbs, int clickCount,
			boolean popupTrigger, int button) {
		System.out.println( "Searching for point (" +x+ "," +y+ ")" ) ;
		Triple<Component,Integer,Integer> result = searchForMouseEventSource( root, x, y) ;
		if( result != null ) {
			Component source = result.first;
			int localX = result.second;
			int localY = result.third;
			MouseEvent ev = new MouseEvent( source, id, now, modifiers, localX, localY,
					xAbs, yAbs, clickCount, popupTrigger, button) ;
			System.out.println( "Found source. New event is " + ev ) ;
			fire(ev);
		} else {
			System.out.println( "No source found" ) ;
		}
	}
	
	private void injectKeyEvent(Component root, int id, long now, int modifiers,
			int keyCode, char keyChar, int keyLoc, long extendedKeyCode){
		System.out.println("injecting key event");
//		if(!txt.isFocusOwner()){
			txt.requestFocus();
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
	
	private void injectActionEvent(Component root, int id, long now, int modifiers,
			int x, int y, String command) {
		System.out.println( "Searching for point (" +x+ "," +y+ ")" ) ;
		Triple<Component,Integer,Integer> result = searchForMouseEventSource( root, x, y) ;
		if( result != null ) {
			Component source = result.first;
			ActionEvent ev = new ActionEvent(source, id, command, now, modifiers) ;
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

	private static class MyEQ extends EventQueue{
		private long count = 0;
		
		@Override
		public void dispatchEvent(AWTEvent e){
			if(e instanceof KeyEvent){
				System.out.println(String.format("%d:%s", count, e));
				count++;
			}
			super.dispatchEvent(e);
		}
	}
	
	public static void main(String[] args) {
		EventQueue eq = Toolkit.getDefaultToolkit().getSystemEventQueue();
		eq.push(new MyEQ());
		SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                new Main();
            }
        });
	}

}
