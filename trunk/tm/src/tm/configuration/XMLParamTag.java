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
 * A class representing a param tag, providing
 * basic Sax parser callbacks. Parameters contain
 * name (specified by the value of the name attribute)
 * value (specified by the text) pairs.
 *
 * @author mpbl
 */
public class XMLParamTag extends XMLElement {
	Configuration theConfig;
	
	public XMLParamTag(String name, String attName, String attValue,
			XMLElement parent, Configuration config)throws SAXException{
		super(name, attName, attValue, parent);
		theConfig = config;
		if (attName == null || !attName.equals("name"))
			throw new SAXException("<param> tag requires a name attribute.");
	}
	
	
	/**
	 * Sax callback. Starting any element inside a param is an error.
	 * @param name: the new xml tag name
	 * @param attName: the name of the new tag's attribute, if any
	 * @param attValue: the value of the new tag's attribute, if any
	 */
	public void startElement(String name, String attName, String attValue)throws SAXException {
		throw new SAXException(
			"No xml elements may be embedded inside a <param> tag ");
	}
	
	public void endElement(String name)throws SAXException{
		if (tagName.equals(name))
			theConfig.loadParameter(this);
		super.endElement(name);
	}
	
}
