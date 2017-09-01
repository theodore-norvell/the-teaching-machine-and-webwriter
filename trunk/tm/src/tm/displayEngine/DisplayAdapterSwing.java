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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.JComponent ;

import tm.configuration.Configuration;
import tm.configuration.ConfigurationServer;
import tm.interfaces.CommandInterface;
import tm.interfaces.Datum;
import tm.interfaces.DisplayContextInterface;
import tm.subWindowPkg.ToolBar;
import tm.subWindowPkg.WorkAreaSwing;

/** Display Adapter is a generic Display PlugIn which can be used as a starting
 * to develop a display plugIn. It is the default adapter for non datum displays.
 * As such it provides a configurable plugIn which has the property that it
 * can be managed by the display manager. If your purpose is to provide
 * specialized display of datums see the DataVisualizerAdaptor.
 * 
 * DisplayAdapters automatically register and deregister for configurations but
 * not for scripting commands because, while all displays need to be configured
 * not all are scripted.
 * 
 * @since Feb 4, 2009
 * @author mpbl
 */
public abstract class DisplayAdapterSwing extends WorkAreaSwing implements DisplayInterface{
    
    private static final long serialVersionUID = 633568484394506313L;
    protected String configId;  
    protected DisplayContextInterface context;
    protected CommandInterface commandProcessor = null;
    protected ToolBar toolBar;


/**
 * 
 * @param dm the display manager that will display this adapter.
 * @param configId the id used in the config file for this display
 */
    public DisplayAdapterSwing(JComponent component, DisplayManager dm, String configId) {
        super(component, dm.getImageSource());     // Automatic scrollbars
        this.configId = configId;
        context = (DisplayContextInterface)dm;
        commandProcessor = context.getCommandProcessor();       
        setPreferredSize(this.getViewportSize());
        mySubWindow.addWorkArea(this);
        mySubWindow.setVisible(true);
        mySubWindow.addInternalFrameListener(dm);
        
        //System.out.println("Display adapter "+this+  "::" +this.hashCode() +" registering.") ;
        ConfigurationServer.getConfigurationServer().register(this,configId);
    }
    
    public String getId(){return configId;}
    
    /*Loads initial configuration which cannot be done in the constructor as
     * all subconstructors must be called before the initial configuration
     * can be loaded.
     * */
    public void loadInitConfig(){
        //System.out.println("loadConfig starts on "+this+  "::" +this.hashCode() ) ;
        
        //setName(configId);
        Configuration config =
            ConfigurationServer.getConfigurationServer().getConfiguration(this);
        if (config != null) {
            //System.out.println("loadInitConfig notifies") ;
            notifyOfLoad(config);   // load initial configuration
            //config.dump();
        }
        else {
            //System.out.println("loadInitConfig loads default") ;
            loadDefaultConfig(configId); }
        //System.out.println("loadInitConfig done") ;
        
    }


    public ToolBar getToolBar() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * Default implementation of refresh 
    */
    public void refresh() {
        super.refresh(); 
    }
    
    
    /**
     * Overide to reduce the generator to only that part of
     * it which the particular display is designed to handle. May be left empty.
     */
    protected void winnow(){
    }



    /**
     * Get datums selected within this visualizer. The display manager (@link DisplayManagerInterface)
     * creates implicit datum generators by polling all displays via getSelected to see which ones
     * have datum sets selected within their workArea (for example by clicking on a datum with
     * a mouse) and concatenating the results. Normally only the StoreDisplays respond. Developers
     * may over-ride this method if they want their Visualizer to act as a datum generator as well
     * as a datum visualizer.
     * 
     * @return a vector holding all datums selected within this display or null if none or this
     * visualizer does not act as a generator
     */
    public Vector<Datum> getSelected() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    public void notifyOfSave(Configuration config){
        Rectangle r = mySubWindow.getBounds();
        config.setValue("Position.x", Integer.toString(r.x));
        config.setValue("Position.y", Integer.toString(r.y));
        config.setValue("Width", Integer.toString(r.width));
        config.setValue("Height", Integer.toString(r.height));
        config.setValue("WindowTitle",mySubWindow.getTitle());
        config.setValue("Visible",mySubWindow.isVisible() ? "true" : "false");
    }
    
    public void notifyOfLoad(Configuration config){
        Rectangle r = mySubWindow.getBounds();
        String temp = config.getValue("Position.x");
        if (temp != null) r.x = new Integer(temp).intValue();
        temp = config.getValue("Position.y");
        if (temp != null) r.y = new Integer(temp).intValue();
        temp = config.getValue("Width");
        if (temp != null) r.width = new Integer(temp).intValue();
        temp = config.getValue("Height");
        if (temp != null) r.height = new Integer(temp).intValue();
        temp = config.getValue("WindowTitle");
        if (temp != null) mySubWindow.setTitle(temp);
        mySubWindow.setBounds(r);
        temp = config.getValue("Visible");
        mySubWindow.setVisible( !(temp != null && temp.equalsIgnoreCase("false")) );
        mySubWindow.validate();
    }
    
    public void loadDefaultConfig(String configId){
        mySubWindow.setBounds(0,0,100,100);
        mySubWindow.setTitle(configId);
        mySubWindow.setVisible(true);   
    }
    
    public void dispose(){
        //System.out.println("Display adapter "+this+  "::" +this.hashCode() +" DEregistering.") ;  
        ConfigurationServer.getConfigurationServer().deregister(this);
    }
        
    public String toString(){ return "displayAdapter " + configId;}

}


