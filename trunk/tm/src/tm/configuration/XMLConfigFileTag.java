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

import org.xml.sax.SAXException;


/**
 * A class representing a configFile tag, providing
 * basic Sax parser callbacks. A configFile consists
 * of a number of config elements
 *
 * @author mpbl
 */
public class XMLConfigFileTag extends XMLElement {
	
	public XMLConfigFileTag(String name, String attName, String attValue, XMLElement parent)throws SAXException{
		super(name, attName, attValue, parent);
		if (attName == null)
			throw new SAXException("<configFile> tag requires a version attribute.");
	}
	
	/**
	 * Sax callback. Start a config element inside this one. Any
	 * other element is an error.
	 * @param name: the new xml tag name
	 * @param attName: the name of the new tag's attribute, if any
	 * @param attValue: the value of the new tag's attribute, if any
	 */
	public void startElement(String name, String attName, String attValue)throws SAXException {
		if (name.equals("config")){
			XMLConfigTag configTag = new XMLConfigTag(name, attName, attValue, this);
			server.setCurrent(configTag);
			configTag.setConfiguration(server.openConfig(configTag));
		}
		else throw new SAXException(
			"The only xml element allowed at <configFile> tag level is <config>");
	}
	
}
