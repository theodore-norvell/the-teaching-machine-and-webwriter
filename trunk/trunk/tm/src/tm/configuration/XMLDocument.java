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
 * A class representing an XML document, providing
 * basic Sax parser callbacks. The document expects
 * to find an <xml> tag as the first element
 *
 * @author mpbl
 */
public class XMLDocument extends XMLElement {
	
	XMLDocument(){
		super("theDocument", null, null, null);
	}

	@Override
	/**
	 * Sax callback. Start an xml element inside this one. Any
	 * other element is an error.
	 * @param name: the new xml tag name
	 * @param attName: the name of the new tag's attribute, if any
	 * @param attValue: the value of the new tag's attribute, if any
	 */
	public void startElement(String name, String attName, String attValue)throws SAXException{
		if (name.equals("xml")) return;
		if (name.equals("configFile"))
			server.setCurrent(new XMLConfigFileTag(name, attName, attValue, this));
		else throw new SAXException(
		"The only element allowed in a <document> is a <configFile> tag");
	}
	
	@Override
	/**
	 * Sax callback. For backward compatability with initial release 2.0
	 * which treated xml as an element (as in html) which had to be closed.
	 *  </xml> is ignored
	 * @param name: the new xml tag name
	 * @param attName: the name of the new tag's attribute, if any
	 * @param attValue: the value of the new tag's attribute, if any
	 */
	public void endElement(String name)throws SAXException{
		if (name.equals("xml")) return;
		super.endElement(name);
	}
}
