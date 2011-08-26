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
import java.io.*;
import tm.interfaces.Configurable;
import tm.utilities.Debug;

/*  A configuration object represents the configurable data for a single
    Configurable object. The file representation of a configurable object
    is started by a <config> tag and ended by </config>. It may contain
    arbitrary text as well as 0 or more parameters which are contained
    by <param> </param> tags. All text between parameter tags is part of
    the parameter.
    Internally, parameters are used to define couplets - name-value pairs
    with an optional leading comment.
    To conform to the original configuration structure, any text which precedes
    a parameter is taken to be a comment on that parameter and is stored as the
    leading text for the couplet which represents the parameter.
    Thus the only text not associated with a couplet would be text which
    trails the final parameter.
    It may contain
*/
public class Configuration {
//    public boolean newDataLoaded;
    private Vector <Couplet> myData;
    private Configurable myOwner;
    private String myName;
    private String trailingText = null;
    
    public Configuration(Configurable owner, String identifier){
        myOwner = owner;
        myData = new Vector <Couplet>();
        myName = identifier;
//        newDataLoaded = false;
    }
    
   /*   This form allows for configurations to be read in directly from
   files before the owner objects have been created. Objects claim their
   configurations by registering and providing the identifier.
   */
    public Configuration(String identifier){
        this(null,identifier);
    }
    
    public void saveConfiguration(BufferedWriter bw)throws IOException{
  // First, strip out any inactive data      
        Couplet pair;
        for (int i = myData.size() - 1; i >= 0; i--) {
            pair = myData.elementAt(i);
            if (!pair.isActive) {
                myData.removeElementAt(i);
            }
        }
            
        bw.write("<config id=\"" + myName + "\">");
        bw.newLine();
 
        for (int i = 0; i < myData.size(); i++){
            pair = myData.elementAt(i);
            //if (pair.leadingText != null) bw.write(pair.leadingText);
            bw.write("<param name=\"" + pair.name + "\">" + pair.value + "</param>");
            bw.newLine();
        }
        if (trailingText != null) bw.write(trailingText);
        bw.write("</config>");
        bw.newLine();
        bw.newLine();
}
    
 
 /* This routine assumes the reader is pointing at a legitimate 
 configuration set as defined by saveConfiguration, above
 
    public void loadConfiguration(XMLConfigTag config){
		int n = config.getNumberEmbedded();
		trailingText = config.getText(); // All leading text associated with first pair
		for (int p = 0; p < n; p++) {
			EmbeddedElement embedded = config.getEmbedded(p);
			XMLParamTag paramTag =(XMLParamTag)embedded.getElement();
			Couplet pair = getPair(paramTag.getAttValue());
			pair.leadingText = trailingText; // actually leading text for this pair
			pair.value = paramTag.getText();
			pair.isActive = true;
			System.out.println(pair.toString());
			trailingText = embedded.getText(); 
		}
//        newDataLoaded = true;
    }*/
    
    public void loadParameter(XMLParamTag paramTag){
		Couplet pair = getPair(paramTag.getAttValue());
		pair.value = paramTag.getText();
    }
    
    
    public void makeAllInactive(){
        for(int i =0; i < myData.size(); i++) {
            Couplet pair = myData.elementAt(i);
            pair.isActive = false;
        }
    }
    
    public Configurable getOwner(){
        return myOwner;
    }
    
    public String getName(){
        return myName;
    }
    
    public void setOwner(Configurable owner){
        myOwner = owner;
    }
    
    public String getValue(String name){
        Couplet find = findPair(name);
        if (find == null) 
            return null;
        else
            return find.value;
    }
    
    public void setValue(String name, String value){
		Couplet pair = getPair(name);  
		pair.value = value;
		pair.isActive = true;
    }
    
    public void setComment(String name, String comment){
        Couplet pair = getPair(name);
        pair.leadingText = comment;
        pair.isActive = true;
     }
    
    private Couplet getPair(String name) {
    	Couplet pair = findPair(name);
    	if (pair == null) {
	        pair = new Couplet(name);
	        myData.addElement(pair);
    	}
        return pair;
    }
    
    private Couplet findPair(String name) {
        int i = 0;
        Couplet pair;
        while(i < myData.size()){
            pair = myData.elementAt(i);
            if (name.compareTo(pair.name)== 0){
                return pair;
            }
            i++;
        }
        return null;
    }
    
    public void dump(){
    	// Parallels saveConfiguration write loop
        Debug d = Debug.getInstance() ;
        d.msg(Debug.ALWAYS, "<config id=\"" + myName + "\">");
        for (int i = 0; i < myData.size(); i++){
            Couplet pair = myData.elementAt(i);
            //if (pair.leadingText != null) bw.write(pair.leadingText);
            d.msg(Debug.ALWAYS, "<param name=\"" + pair.name + "\">" + pair.value + "</param>");
        }
        if (trailingText != null) d.msg(Debug.ALWAYS, trailingText);
        d.msg(Debug.ALWAYS, "</config>");
    }
}
