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

import tm.interfaces.DisplayContextInterface;
import tm.plugins.Requirement;

/**
 * 
 * A description of the Type and its responsibilities
 *
 * @author mpbl
 */
public class ConsoleDisplayPIFactory implements DisplayPIFactoryIntf{
	
	private String configId;
	
	private ConsoleDisplayPIFactory(String parameter){
		configId = parameter;
	}

	
    static public ConsoleDisplayPIFactory createInstance( String parameter ) {
    	return new ConsoleDisplayPIFactory(parameter) ;
    }
	
	public DisplayInterface createPlugin(DisplayManager dm) {		
		return new ConsoleDisplay(dm, configId);
	}

	public Requirement[] getRequirements() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getParameter() {
		return configId;
	}
	public String toString(){return "ConsoleDisplayPiFactory("+configId+")";}

}
