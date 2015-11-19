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
 * A base class representing an XML element, providing
 * defaults for basic Sax parser callbacks. Designed to be
 * extended by element classes for each kind of XML tag. 
 *
 * @author mpbl
 */
class XMLElement{
	protected XMLElement parent;
	protected ConfigurationServer server;
// leading text - that is all text up to next embedded element
	protected String myText = "";
	protected String tagName;	// name of the XML tag
	// Each tag may have 0 or 1 attributes
	protected String attName = null;
	protected String attValue = null;
	
	
	/**
	 * Base constructor. Provides basic services but
	 * individual elements will likely have to extend.
	 * @param name: the xml tag name
	 * @param attName: the name of the attribute, if any
	 * @param attValue: the value of the attribute, if any
	 * @param parent: the XMLElement in which this one is embedded
	 */
	
	public XMLElement(String name, String attName, String attValue, XMLElement parent){
		server = ConfigurationServer.getConfigurationServer();		
		tagName = name;		// parent startElement routine checks for correct name
		this.parent = parent;
		if (attName != null){
			this.attName = attName;
			this.attValue = attValue;
		}
//		System.out.println(toString());
	}

/**
 * Sax callback. Start a new element inside this one. The kind
 * of elements permitted varies with the type of element. This
 * function is the major one to over-ride in inheriting classes.
 * @param name: the new xml tag name
 * @param attName: the name of the new tag's attribute, if any
 * @param attValue: the value of the new tag's attribute, if any
 */
	
	public void startElement(String name, String attName, String attValue) throws SAXException{
	}
	
	
/**
 * Sax callback. Ends this element. Designed to provide basic services but
 * individual elements may have to over-ride.
 * @param name: the xml tag name
 */

	public void endElement(String name) throws SAXException {
		if (tagName.equals(name)){
//			System.out.println("end element " + toString() + " Switching to " + parent.toString());
			parent.clearText();   	// clear out parent's leading text
			server.setCurrent(parent);	// change state
		}
		else throw new SAXException(
		"endElement tag mismatch. Tried to end " + name + " inside " + toString());
	}
	
/**
 * Sax callback for this element. Designed to provide basic services but
 * individual elements may have to over-ride.
 * @param moreText: text to be added to existing text
 */
	public void addText(String moreText) {
		myText += moreText;
	}
	
	public void clearText(){
		myText="";	//Clear out text before element
	}
	
	
	public String getText(){ return myText;}
	public String getTagName(){ return tagName;}
	public String getAttName(){return attName;}
	public String getAttValue(){return attValue;}
	
	public String toString(){return "<" + tagName + " " + attName + " = " + attValue + ">" + myText;}

}
