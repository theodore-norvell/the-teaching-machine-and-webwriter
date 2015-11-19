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
 * A class representing a config tag, providing
 * basic Sax parser callbacks. A config consists
 * of a number of param elements
 *
 * @author mpbl
 */
public class XMLConfigTag extends XMLElement {
	Configuration myConfig;	// The configuration being changed/created by this tag
	
	public XMLConfigTag(String name, String attName, String attValue, XMLElement parent)throws SAXException{
		super(name, attName, attValue, parent);
		if (attName == null || !attName.equals("id"))
			throw new SAXException("<config> tag requires an id attribute");
	}
	
/**
 * Sax callback. Start a param element inside this one. Any
 * other element is an error.
 * @param name: the new xml tag name
 * @param attName: the name of the new tag's attribute, if any
 * @param attValue: the value of the new tag's attribute, if any
 */
	public void startElement(String name, String attName, String attValue)throws SAXException {
		if (name.equals("param"))
			server.setCurrent(new XMLParamTag(name, attName, attValue, this, myConfig));
		else throw new SAXException(
			"The only xml element allowed at <config> tag level is <param>");
	}
	
	public void setConfiguration(Configuration config){myConfig = config;}
	
}
