
package telford.common;

import telford.common.peers.*;

abstract public class Kit {
	
	static private Kit kit ;
		
	public static  Kit getKit() { return kit ; }
	
	public static void setKit(Kit kit) { Kit.kit = kit ; }
		
	abstract public Font getFont() ;
	
	abstract public Font getFont(String name, int style, int size) ;
		
	abstract public RootPeer makeRootPeer( String title, Root root ) ;
	
	abstract public Display getDisplay();
	
	abstract public ButtonPeer makeButtonPeer (String title, Button button);

	abstract public ContainerPeer makeContainerPeer(Container container);
	
//	abstract public ContainerPeer makeContainerPeer(Container container, int type);
	
	abstract public CanvasPeer makeCanvasPeer(Canvas canvas);
	
	public abstract ComponentPeer makeComponentPeer(Component component);
	
	abstract public BorderLayout getBorderLayoutManager();

	public abstract Timer getTimer(int delay,boolean repeats, Root root, ActionListener actionListener) ;

	public abstract LayoutManager getFlowLayoutManager() ;

	public abstract Random getRandom();


}
