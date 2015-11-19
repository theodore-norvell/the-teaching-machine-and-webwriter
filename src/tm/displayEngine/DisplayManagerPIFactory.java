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

/*
 * Created on 31-Jul-2006 by Theodore S. Norvell. 
 */
package tm.displayEngine;

import javax.swing.JMenu;

import tm.interfaces.CommandInterface;
import tm.interfaces.DisplayManagerInterface;
import tm.interfaces.DisplayManagerPIFactoryIntf;
import tm.interfaces.ImageSourceInterface;
import tm.plugins.Requirement;

/**
 * @author theo
 */
public class DisplayManagerPIFactory implements DisplayManagerPIFactoryIntf {

    public static String jackNameDisplay = "tm.displayEngine.DisplayManager:Display" ;
    
    public static DisplayManagerPIFactory createInstance( String parameter ) {
        return new DisplayManagerPIFactory() ;
    }
    
    private DisplayManagerPIFactory() {}

    /**
     * @see tm.interfaces.DisplayManagerPIFactoryIntf#createDisplayManager(java.lang.String, tm.interfaces.ImageSourceInterface, tm.interfaces.CommandInterface, java.awt.Menu)
     */
    public DisplayManagerInterface createPlugin(String langName,
            ImageSourceInterface is, CommandInterface cp, JMenu vm) {
        return new DisplayManager( langName, is, cp, vm ) ;
    }

    /* (non-Javadoc)
     * @see tm.plugins.PlugInFactory#getRequirements()
     */
    public Requirement[] getRequirements() {
        return new Requirement[] { new Requirement(jackNameDisplay, DisplayPIFactoryIntf.class, true, true ) } ;
    }
	public String toString(){return "DisplayManagerPiFactory";}

}