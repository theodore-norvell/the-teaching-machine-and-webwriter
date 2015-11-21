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

import java.awt.Color;
import java.awt.Graphics2D;

import tm.interfaces.CommandInterface;
import tm.interfaces.DisplayContextInterface;
import tm.plugins.Requirement;
import tm.subWindowPkg.SubWindow;
import tm.subWindowPkg.ToolBar;
import tm.subWindowPkg.WorkArea;

/**
 * 
 * A description of the Type and its responsibilities
 *
 * @author mpbl
 */
public class PCDisplayPIFactory implements DisplayPIFactoryIntf{
	
	private String configId;
	
	private PCDisplayPIFactory(String parameter){
		configId = parameter;
	}

	
    static public PCDisplayPIFactory createInstance( String parameter ) {
    	return new PCDisplayPIFactory(parameter) ;
    }
	
	public DisplayInterface createPlugin(DisplayManager dm) {		
		return new PCDisplay(dm, configId);
	}

	public Requirement[] getRequirements() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getParameter() {
		return configId;
	}


	public String toString(){return "PCDisplayPiFactory("+configId+")";}
		
}
