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

package tm.configuration;

import java.util.*;

import tm.utilities.Assert;

/**
 * Couplets are basically just (name, pair) strings for saving configuration
    information in text readable/editable format. 
 *
 * @author mpbl
 */

public class Couplet {
    public String name;
    public String value;
    public String leadingText = null;  // Comment lines before the pair
    public boolean isActive = false;
    
    Couplet (XMLParamTag param){ 
    	if (param != null)
    	name = param.getAttName();
    	value = param.getText();
        isActive = true;
    }
    
    Couplet(String name){
    	this.name = name;
    }
    
    public String toString(){
    	String rValue = "";
    	if (leadingText!=null)
    		rValue = leadingText + "\n" ;
    	rValue += name + ": " + value;
    	return rValue;
    }
      
}