
package telford.common;

import telford.common.peers.*;

abstract public class Kit {
	
	static private Kit kit ;
		
	public static  Kit getKit() { return kit ; }
	
	public static void setKit(Kit kit) { Kit.kit = kit ; }
		
	abstract public Font getFont() ;
		
	abstract public RootPeer makeRootPeer( String title, Root root ) ;
	
	abstract public Display getDisplay();
	
	abstract public ButtonPeer makeButtonPeer (String title, Button button);

	abstract public ContainerPeer makeContainerPeer(Container container);
	
	abstract public LayoutManager getBorderLayoutManager();

	public abstract Timer getTimer(int delay,boolean repeats, ActionListener actionListener) ;
}
