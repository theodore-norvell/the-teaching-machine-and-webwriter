//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

package tm.displayEngine;

/* Major Changes, May 1998. Theo has experimented with running TM as an Applet. See
    rearranging.txt in the doc folder. Under this scheme, the displayManager relinquishes
    all responsibility for handling the open file menus and simply does the sub-windowing
    and displays. Thus it becomes an extension of a panel rather than a frame.
        As part of the redesign, Theo has unlinked the display system from the model system
    even farther. Calls between the two are now routed through a top level mediator rather
    than going direct. In keeping with this philosophy, the displayManager now implements
    a status interface.
    
    Question of the moment. Why do we need both a display manager and a display space.
    
   December 1998. Trying to find out why the iconbar is not posting properly. Since this
   is no longer really the top window (rather, it is added to BigApplet which is added
   to TMMainFrame) I took out the resize commands from this class and added in preferredSize()
   minimumSize(). However, removing resize from createAllDisplays kills all windows completely.
*/

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import telford.common.Kit ;
import tm.backtrack.BTTimeManager;
import tm.configuration.Configuration;
import tm.configuration.ConfigurationServer;
import tm.displayEngine.tmHigraph.HigraphManager;
import tm.interfaces.DisplayContextInterface;
import tm.interfaces.Datum;
import tm.interfaces.DisplayManagerInterface;
import tm.interfaces.CommandInterface;
import tm.interfaces.ImageSourceInterface;
import tm.interfaces.PortableDisplayContext ;
import tm.plugins.PlugInManager;
import tm.plugins.PlugInNotFound;
import tm.scripting.ScriptManager;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.utilities.TMException;

/**
 * This Display Manager is an update of the original TM DISPLAY MANAGER which knew
 * how many displays there were and what kind. Configuration files could suppress
 * a predefined display or move the display around within the main display space.
 * There was no capability to manage new displays as the plug-in system is
 * supposed to be able to do.
 * 
 * @author mpbl
 */
public class DisplayManager extends JPanel implements DisplayManagerInterface, DisplayContextInterface, Observer, InternalFrameListener{
    
	private static final long serialVersionUID = -7376951577050649901L;

	private static String CONFIG_VERSION = "2.0";
	private DisplayInterface myDisplays[] = new DisplayInterface[0];
    
	private CommandInterface commandProcessor;
	private ImageSourceInterface imageSource;
	String myTitle;
	private final JMenu viewMenu ;


// Configurable titles available to all displays	
	private Color highlightColor = Color.yellow;
	private Font displayFont = new Font("Dialog",Font.PLAIN,12); // Default for most displays
	private Font codeFont = new Font("Monospaced",Font.PLAIN,12);    // Default for code displays
	

	// Invariant: The number of component children of the windowArea equals the
	// length of the myDisplays array and the component children of the windowArea are
	// subwindows whose display objects are listed in myDisplays.
	private JDesktopPane windowArea;
	
	private HigraphManager higraphManager;

//===================================================================================
// Constructor
//===================================================================================
	public DisplayManager(String langName, ImageSourceInterface is, CommandInterface cp, JMenu vm){
		setLayout(new BorderLayout());
 		windowArea = new JDesktopPane();
 		windowArea.setBackground(Color.LIGHT_GRAY);
 		windowArea.setDoubleBuffered(true);
 		windowArea.setOpaque(true);
 		add("Center",windowArea);
		
		commandProcessor = cp;
		imageSource = is;
		viewMenu = vm ;
		
		myTitle = "The Teaching Machine -- " + langName + " edition";
		//ViewStateManager.createViewStateManager(vm);
		
 // Register myself with configuration server which should be up by now
		ConfigurationServer server = ConfigurationServer.getConfigurationServer();
		server.register(this,toString());
//		server.dump();

//		System.out.println("Display Manager adding itself to PlugInManager as an observer");
        PlugInManager pim = PlugInManager.getSingleton();
        pim.addObserver(this);
        
        higraphManager = new HigraphManager((DisplayContextInterface)this);
        
		setVisible(true);
		validate();
	}
	
	/** Implementation of the DisplayManagerInterface, the interface between
	 * the model and view
	 */
	
	public Component getComponent(){ return this;}
	
	public String getConfigId(){return "Display Manager";}
	
	public Vector<Datum> getSelection(DisplayInterface target){
		Vector<Datum> selection = new Vector<Datum>();
		for (int d = 0; d < myDisplays.length; d++) {
			DisplayInterface display = myDisplays[d];
			if ( display != null && display != target){
				Vector<Datum> hold = display.getSelected();
				if ( hold != null)
					selection.addAll(hold);				
			}
		}
		return selection;
	}
	
    public void createAllDisplays() {
    	Debug.getInstance().msg(Debug.DISPLAY, "In createAllDisplays()");
                
		
        PlugInManager pim = PlugInManager.getSingleton();
        
        // Remove all displays.
            for (int d = 0; d < myDisplays.length; d++){
                myDisplays[d].dispose() ;
            }
            windowArea.removeAll();      
        // Now retrieve the factories
        try {
            List <DisplayPIFactoryIntf> factoryList = 
            	pim.getFactoryList(DisplayManagerPIFactory.jackNameDisplay,
            			DisplayPIFactoryIntf.class, true);
        
	        Iterator <DisplayPIFactoryIntf> iterator = factoryList.iterator();
	        DisplayPIFactoryIntf pieFactory;
	        myDisplays = new DisplayInterface[factoryList.size()];
	        int d = 0;
	        
	        while(iterator.hasNext()){
         		pieFactory = iterator.next();
         		String id = pieFactory.getParameter();
         		Assert.check(id != null, "No id paramater for display factory " + pieFactory.toString());
         		Debug.getInstance().msg(Debug.DISPLAY, "Creating display plugin using " + pieFactory.toString());
	         	myDisplays[d] = pieFactory.createPlugin(this);
	         	//System.out.println("createAllDisplays: creates "+ myDisplays[d] +"::"+myDisplays[d].hashCode()) ;
	         	myDisplays[d].loadInitConfig();
	         	windowArea.add(myDisplays[d].getWindowComponent());
	    //     	ScriptManager.getManager().register(myDisplays[d]);
//	         	if (id.contains("Higraph."))
//	         		higraphManager.subgraphDisplayAdded((SubgraphVisualizer)myDisplays[d]);
			    d++;
	        }     
	        
        }
        catch( PlugInNotFound ex ) {
            ex.printStackTrace( System.err ) ;
            /*attention( "The Teaching Machine could not execute your\n"
                      +"request because of " + explanation + ".\n"
                      +"See the Java console for more detailed information." )*/ ; 
            throw new TMException(ex.getMessage()) ;
        }
		
	 	Graphics screen = getGraphics();
	 	if (screen != null)	screen.setFont(displayFont);
		
		setVisible(true);
		validate();
        //System.out.println("createAllDisplays: exits") ;
    }
    
    public DisplayInterface getDisplay(String configId){
    	for (int i = 0; i < myDisplays.length; i++ )
    		if (myDisplays[i].getId().equals(configId))
    			return myDisplays[i];
    	return null;
    }
    
    public DisplayInterface[] getDisplays(){
    	return myDisplays;
    }

	/* refresh
       =======   Refresh all displays.
    */
    public void refresh() {
    	for (int d = 0; d < myDisplays.length; d++) {
    		//System.out.println("Refreshing "+ myDisplays[d].getId() ) ;
    		if (myDisplays[d] != null) myDisplays[d].refresh() ; }
    }
   
  
/**************************************************************
 * DisplayContextInterface Implementation - the wa
 * 
 ***************************************************************/
    public Color getHighlightColor() { return highlightColor;}
    
    public Font getDisplayFont()  { return displayFont;}
    
    public Font getCodeFont() { return codeFont;}
	  
	public CommandInterface getCommandProcessor(){return commandProcessor;}
      	
	public ImageSourceInterface getImageSource(){return imageSource;}

	public String toString(){
	    return "Display Manager";
	}
	
    public void notifyOfSave(Configuration config){
	    config.setValue("highlightColor", Integer.toString(0x00ffffff & highlightColor.getRGB()));
	    config.setValue("displayFontName", displayFont.getName());
	    config.setValue("displayFontStyle", Integer.toString(displayFont.getStyle()));
	    config.setValue("displayFontSize", Integer.toString(displayFont.getSize()));
	    config.setValue("codeFontName", displayFont.getName());
	    config.setValue("codeFontStyle", Integer.toString(displayFont.getStyle()));
	    config.setValue("codeFontSize", Integer.toString(displayFont.getSize()));
	    config.setComment("highlightColor",
	    "Parameters common to all displays");
    }
    
    public void notifyOfLoad(Configuration config){
	    String temp = config.getValue("highlightColor");
	    if (temp != null) highlightColor = Color.decode(temp);
	    int size, style;
	    temp = config.getValue("displayFontStyle");
	    if (temp != null)
	        style = new Integer(temp).intValue();
	    else
	        style = displayFont.getStyle();
	    temp = config.getValue("displayFontSize");
	    if (temp != null)
	        size = new Integer(temp).intValue();
	    else
	        size = displayFont.getSize();
	    temp = config.getValue("displayFontName");
	    if (temp != null) {
	        displayFont = new Font(temp, style, size);
	    }
	    temp = config.getValue("codeFontStyle");
	    if (temp != null)
	        style = new Integer(temp).intValue();
	    else
	        style = codeFont.getStyle();
	    temp = config.getValue("codeFontSize");
	    if (temp != null)
	        size = new Integer(temp).intValue();
	    else
	        size = codeFont.getSize();
	    temp = config.getValue("codeFontName");
	    if (temp != null) {
	        codeFont = new Font(temp, style, size);
		}
		refresh();
    }
    
    public void dispose(){
        // Stop observing the plug-in-manager
        PlugInManager pim = PlugInManager.getSingleton(); ;
        pim.deleteObserver( this ) ;
        
        
        // Deregister all displays from the ScriptManager, dispose them,
        // and remove them from this AWT container.
    	ScriptManager scriptManager = ScriptManager.getManager();
    	for (int d = 0; d < myDisplays.length; d++){
    	    scriptManager.deRegister(myDisplays[d]);
    	    myDisplays[d].dispose() ;
    	}
    	windowArea.removeAll();   	
    	
    	// Dispose the HigraphManager
    	if(higraphManager != null) higraphManager.dispose();
    	
    	// Deregister with the ConfigurationServer
    	ConfigurationServer server = ConfigurationServer.getConfigurationServer();
		server.deregister(this);
    }

	public String getId() {
		return "Display Manager";
	}

	public void update(Observable observable, Object arg1) {
		Debug.getInstance().msg(Debug.DISPLAY, "updating " + getId() + " observer");
        if( observable instanceof PlugInManager ) {
            Set<String> jackNameSet = (Set<String>) arg1 ;
            if( jackNameSet.contains(DisplayManagerPIFactory.jackNameDisplay)) 
                createAllDisplays();
        }
		
	}

    @Override
    public BTTimeManager getTimeManager() {
        return commandProcessor.getTimeManager();
    }

	@Override
	public HigraphManager getHigraphManager() {
		return higraphManager;
	}

    @Override
    public JMenu getViewMenu() {
        return viewMenu ;
    }

	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {
        // TODO Auto-generated method stub
	}

	@Override
	public void internalFrameIconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	class PortableContext implements PortableDisplayContext {

        @Override
        public telford.common.Font getCodeFont() {
            // TODO The font sould be based on preferences from the configuration.
            return Kit.getKit().getFont() ;
        }

        @Override
        public telford.common.Font getDisplayFont() {
            // TODO The font sould be based on preferences from the configuration.
            return Kit.getKit().getFont() ;
        }
	}
	
	private PortableDisplayContext portableContext = new PortableContext() ;

    public PortableDisplayContext getPortableContext() {
        return portableContext ;
    }
}
