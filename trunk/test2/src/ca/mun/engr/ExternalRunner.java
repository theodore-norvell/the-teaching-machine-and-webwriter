package ca.mun.engr;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import sun.java2d.SurfaceData;

public class ExternalRunner {
	private static ExternalRunner instance = null;
	private Map<Integer, Object> jps = new HashMap<Integer, Object>();
	private static int index = 0;
	private static int user_num = 0;
	private JMenuBar menuBar = new JMenuBar();

	public static ExternalRunner getInstance(){
		if(instance == null){
			instance = new ExternalRunner();
		}
		return instance;
	}
	
	public void create() throws ClassNotFoundException, MalformedURLException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, InterruptedException{
		File file = new File("/Users/leonardo/Documents/the-teaching-machine-and-webwriter/trunk/tm/bin");
//		File file = new File("/Users/leonardo/Documents/the-teaching-machine-and-webwriter/trunk/test2/build/classes");
		// Convert File to a URL
		URL url = file.toURL();          // file:/c:/myclasses/
		URL[] urls = new URL[]{url};

//		JMenu view = new JMenu("view");
//		menuBar.add(view);
		// Create a new class loader with the directory
		ClassLoader cl = new URLClassLoader(urls);
		
		// Load in the class; MyClass.class should be located in
		// the directory file:/c:/myclasses/com/mycompany
		Class<?> cls = cl.loadClass("tm.TMMainFrame");
//		Class<?> clsStub = cl.loadClass("tm.TMMainFrameAppletStub");
//		Class<?> clsArg = cl.loadClass("tm.ArgPackage");
//		cls.newInstance();
		
//		String[] Args = new String[0];
//		Method process = clsArg.getDeclaredMethod("processArgs", Args.getClass());
//		Object stub = process.invoke(clsArg, (Object)Args);
//		Field installDirectory = clsArg.getDeclaredField("installDirectory");
//		Constructor<?> arg_Cons = clsStub.getConstructor(URL.class,URL.class);
//		arg_Cons.getParameterTypes();
//		Object install = installDirectory.get(stub);
//		AppletStub frameAppletStub = (AppletStub) arg_Cons.newInstance((URL)install,(URL)install);
//		
//		
//// load applet to start
////		Applet applet = (Applet) cls.newInstance();
//		Constructor<?> ctr = cls.getDeclaredConstructor();
//		Applet applet = (Applet) ctr.newInstance();
////		JFrame frame = new JFrame();
////		frame.getContentPane().add(applet);
////		frame.setVisible(true);
//		applet.setSize(800,570);
//		applet.validate();
//		((JApplet) applet).getRootPane().validate();
//		boolean av = applet.isValid();
//		boolean avr = applet.isValidateRoot();
//		boolean rpv = ((JApplet) applet).getRootPane().isValid();
//		System.out.println("av: "+av+" avr: "+avr+" rpv: "+rpv);
//		
//		
//		applet.init();
//		applet.setStub(frameAppletStub);
//		applet.start();

//		Object ac = applet;
		//		jps.put(jps.size(), cls.newInstance());
		
// load Frame to start		
//		Class[] argTypes = new Class[] { String[].class };
		String[] mainArgs = new String[0];
		Method m = cls.getDeclaredMethod("main",String[].class);
		m.invoke(cls, (Object)mainArgs);
		
		
		Field result = cls.getDeclaredField("tmBigApplet");
//		result.setAccessible(true);
		
//		
		while(result.get(cls) == null){
			Thread.sleep(50);
		}
		Component resultComp = (Component) result.get(cls);
		while(resultComp.getWidth()<1){
			Thread.sleep(50);
		}

		index = user_num;
		jps.put(index, resultComp);
		user_num++;
	}
	
	public String getIndex(){
		return Integer.toString(index);
	}

	
	private Component getComp(Object o){
		Component com = (Component) o;
		return com;
	}
	
	public void appletDispose(int idx){
		Applet applet = (Applet) jps.get(idx);
		
	}
    public BufferedImage ShotRequest(int idx) throws IOException {
    	BufferedImage ShotResult = getScreenshot(getComp(jps.get(idx)));
		return ShotResult;
	}

	private BufferedImage getScreenshot(Component com) throws IOException {
		if(com == null)
			return null;
		BufferedImage bi = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);  
		Graphics g = bi.createGraphics();
//		com.update(g);
	    com.paint(g);  //this == JComponent	
        g.dispose();
        File f = new File("/Users/leonardo/Documents/the-teaching-machine-and-webwriter/trunk/result.jpg");
        ImageIO.write(bi, "jpg", f);
        return bi;
	}
	
	
	
	
	public boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
		boolean sameImg = true;
	    if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
	    	BufferedImage result1 = scale(img1);
	    	BufferedImage result2 = scale(img2);
	    	for (int x = 0; x < result1.getWidth(); x++) {
	            for (int y = 0; y < result1.getHeight(); y++) {
	                if (result1.getRGB(x, y) != result2.getRGB(x, y)){
	                    sameImg =  false;
	                }
	            }
	    	}
//	    	for (int x = 0; x < img1.getWidth(); x++) {
//	            for (int y = 0; y < img1.getHeight(); y++) {
//	                if (img1.getRGB(x, y) != img2.getRGB(x, y))
//	                    return false;
//	            }
//	        }
//	    } else {
	    }
		return sameImg;
	}
	
	public Component getComponentRoot(int idx){
		return getComp(jps.get(idx));
	}
	public BufferedImage scale(BufferedImage sbi) {
	    BufferedImage dbi = null;
	    if(sbi != null) {
	        dbi = new BufferedImage(sbi.getWidth()/10, sbi.getHeight()/10, sbi.getType());
	        Graphics2D g = dbi.createGraphics();
	        AffineTransform at = AffineTransform.getScaleInstance(0.1, 0.1);
	        g.drawRenderedImage(sbi, at);
	    }
	    return dbi;
	}
}
