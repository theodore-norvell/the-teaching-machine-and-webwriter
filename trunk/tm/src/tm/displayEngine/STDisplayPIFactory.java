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
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

import tm.interfaces.CommandInterface;
import tm.interfaces.DisplayContextInterface;
import tm.interfaces.STEntry;
import tm.plugins.Requirement;
import tm.subWindowPkg.SubWindow;
import tm.subWindowPkg.ToolBar;
import tm.subWindowPkg.OldWorkArea;

/**
 * 
 * A description of the Type and its responsibilities
 *
 * @author mpbl
 */
public class STDisplayPIFactory implements DisplayPIFactoryIntf{
	
	private String configId;
	
	private STDisplayPIFactory(String parameter){
		configId = parameter;
	}

	
    static public STDisplayPIFactory createInstance( String parameter ) {
    	return new STDisplayPIFactory(parameter) ;
    }
	
	public DisplayInterface createPlugin(DisplayManager dm) {		
		return new STDisplay(dm, configId);
	}

	public Requirement[] getRequirements() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getParameter() {
		return configId;
	}
	
	public String toString(){return "STDisplayPiFactory("+configId+")";}

}
