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

/*
2007.01.30 Complete rewrite to utilize XML

2001.01.23 When loading configuration file, any configuration for which no
    owner has been registered throws an IOException. At the same time,
    deregister had been changed to changeOwner

99.12.09 Some subWindows  killed off during a read were still hanging around.
    Fixed readConfiguration to notifyOfNoLoad in reverse order (see code).
*/

import java.util.*;
import java.io.*;

import tm.AttentionFrame;
import tm.interfaces.Configurable;
import tm.interfaces.PlatformServicesInterface;
import tm.utilities.Debug;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;



/**
 * All Teaching Machine configuration information information is stored in Configuration
 * files. The configuration server is a singleton class which handles reading and writing
 * of he files and notification of clients of the server. Clients implement
 * the Configurable interface and register with the server for updates.
 * 
 * Configuration files are basically xml files with the following structure:
 * document
 * 		<xml>  tag
 * 			<configFile> tag which identifies the document as a configuration file and
 * 							also handles the version
 * 				<config> tags which wrap configuration information for clients
 * 					<param> tags which store individual config name-value pairs
 *
 * The server uses SAX parsing to read and parse configuaration files
 * 
 * To be done: strict XML & proper parser error handling
 * 
 * @author mpbl
 */
public class ConfigurationServer extends DefaultHandler{

    private static final String CONFIG_VERSION = "2.0";
    private static Vector<Configuration> myVector = null;
    private static ConfigurationServer myself = null;
    //private static Vector<String> commentBlock = null;
    private static XMLReader xr;
    //private static XMLDocument document;
    private static final String XMLDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
    private static final String configFileTag = "<configFile version=\"2.0\">";
    static PlatformServicesInterface platform ;
    /* Note: This is an error. The lead <xml> is not part of the document
    and thus not a tag.
    */
//    private static String closeXMLTag = "</xml>";
    private static String closeConfigFileTag = "</configFile>";
    private static XMLElement current = null;  // Current open element

    private ConfigurationServer() {
    	super();
        if (myVector == null) myVector = new Vector<Configuration>();
        //if (commentBlock == null) commentBlock = new Vector<String>();
        try {
	    	xr = XMLReaderFactory.createXMLReader();
	    	xr.setContentHandler(this);
	    	xr.setErrorHandler(this);
        }catch (SAXException e) {
        	platform.showMessage("Configuaration Error", "Error parsing configuation file: "+e.getMessage(), e);
        }
    }
    
    public static void setPlatform( PlatformServicesInterface p ) {
    	platform = p ;
    }
    
/**
 *  Singleton Pattern. With only one server, there is no need to pass its
 *  reference around. Instead ask the server for a reference to
 *  itself, implemented via a static method. As a side-effect, if no server
 *  has yet been created, it instantiates it automatically by construction.
 *  
 *  The constructor is private so only the static member can access it
 *  enforcing the singleness of the object. So, effectively, this method
 *  implements the factory method pattern as well.
 * @return the configuration server
 */
    public static ConfigurationServer getConfigurationServer(){
        if (myself == null) myself = new ConfigurationServer();
        return myself;
    }
    
   /**
    * Load configuration information in from an input stream. Uses SAX xml
    * parsing
    * @param reader
    * @throws Exception
    */
   public void readConfiguration(Reader reader)throws Exception {
        Configuration configuration;
        Configurable owner;
 
 	   xr.parse(new InputSource(reader));
       for(int i = 0; i < myVector.size(); i++) {
           configuration = myVector.elementAt(i);
           owner = configuration.getOwner();
           if (owner != null) {
        	   //System.out.println("Notifying " + owner + " of load");
               owner.notifyOfLoad(configuration);
           }
       }
//       dump();
   }
   

    
   ////////////////////////////////////////////////////////////////////
   // Event handlers for SAX reading.
   ////////////////////////////////////////////////////////////////////

/**
 * document start callback
 */
   public void startDocument ()throws SAXException{
	   current = new XMLDocument();
   }

/**
* document end callback
*/
  public void endDocument ()throws SAXException{
	   current = null;
   }

/**
 * start xml element callback
 */
   public void startElement (String uri, String name, String qName, Attributes atts) throws SAXException{
	   String attName = null;
	   String attValue = null;
	   if (current != null){
			int a = atts.getLength();
			if (a > 1) throw new SAXException("Configuration XML tags can have no more than 1 attribute");
			else if (a == 1) {
				attName = atts.getLocalName(0);
				attValue = atts.getValue(0);
			}
			current.startElement(extractTargetName(uri, name, qName),
				attName, attValue);
		} else throw new SAXException("XML element tag occured before document opened");
   }

/**
 * end xml element callback
 */
   public void endElement (String uri, String name, String qName) throws SAXException {
		if (current != null){
			current.endElement(extractTargetName(uri, name, qName));
		} else throw new SAXException("XML element tag closure occurred before document opened");
   }
   
   private String extractTargetName(String uri, String name, String qName){
		if ("".equals (uri))
			return qName;
		return uri + name;
   }
   
 /* (non-Javadoc)
 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
 */
   public void characters (char ch[], int start, int length) throws SAXException {
	   String text = new String(ch, start, length);
	   if (current != null){
		   current.addText(text);
	   } else throw new SAXException("Can't have text before document opened");
   }


   ////////////////////////////////////////////////////////////////////
   // State changing callbacks for XMLElements.
   ////////////////////////////////////////////////////////////////////
   XMLElement getCurrent(){
       return current;
   }

   void setCurrent(XMLElement next){
       current = next;
       //	 System.out.println("Switched to " + current.toString());
   }

   Configuration openConfig(XMLConfigTag configTag){
       Configuration config = findConfig(configTag.getAttValue());
       if (config == null) {
           config = new Configuration(configTag.getAttValue());    // Nope, build a new one
           myVector.addElement(config);
       }
       return config;	   
   }




/** Write configuration information out to an output stream. All registered configurable
 * objects are called upon to update their information. Then all information for configurable
 * objects is written out. To keep obsolete data from hanging around indefinitely any information
 * not updated by an object is NOT SAVED to the output stream.
*/

   public void writeConfiguration(OutputStream os) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        bw.write(XMLDeclaration);
        bw.newLine();
        bw.write(configFileTag);
        bw.newLine();
        for (int i = 0; i < myVector.size(); i++) {
            Configuration current = myVector.elementAt(i);
            current.makeAllInactive();
            Configurable owner = current.getOwner();
            if (owner != null) {
                owner.notifyOfSave(current);
/*                bw.write(current.getName());
                bw.write("\u0009// Unique object identifier");*/
                current.saveConfiguration(bw);
            }
        }
        bw.write(closeConfigFileTag);
        bw.newLine();
//        bw.write(closeXMLTag);
//        bw.newLine();        
        bw.flush();
   }


/**  owners of configurable data must register for it. Any data loaded from the input stream
 * that is not claimed will not be repeated to an output stream. The identifier provides
 * a unique, non-volatile name for the owner. That name will be used in the configuration file.
*/
    public void register (Configurable owner, String identifier) {
        Configuration theConfiguration = findConfig(identifier);
        if (theConfiguration == null) {
        	platform.showMessage("Configuration error", "Can't find configuration for " + identifier);
        } else if( theConfiguration.getOwner() == null ){
            theConfiguration.setOwner(owner);
        }
    }
    
    public void deregister(Configurable owner){
        Configuration theConfiguration = findConfig(owner);
        if (theConfiguration != null) 
        	theConfiguration.setOwner(null);
    	
    }

/**
 *  Asynchronous request for configuration. Typically called by constructors
 */
    public Configuration getConfiguration(Configurable owner){
        return findConfig(owner);
   }

    /* If the name string is not known to the server it will be added.
    */
    public void setValue(Configurable owner, String name, String value){
        Configuration theConfiguration = findConfig(owner);

        if (theConfiguration != null)
            theConfiguration.setValue(name, value);
    }

// Precedes the (name, value) pair line with a comment
    public void insertComment(Configurable owner, String name, String comment) {
        Configuration theConfiguration = findConfig(owner);
        if (theConfiguration != null)
            theConfiguration.setComment(name,comment);
    }


    private Configuration findConfig(Configurable owner) {
        if (myVector != null){
            int i = 0;
            Configuration possible;
            while(i < myVector.size()){
                possible = myVector.elementAt(i);
                if (possible.getOwner()== owner){
                    return possible;
                }
                i++;
            }
        }
        return null;
    }

    Configuration findConfig(String identifier) {
        if (myVector != null){
            Configuration possible;
            int i = 0;
            while (i < myVector.size()){
                possible = myVector.elementAt(i);
                if (identifier.compareTo(possible.getName())== 0){
                    /*DBG System.out.println("Found configuration for " + identifier);/*DBG*/
                    return possible;
                }
                i++;
            }
        }
        return null;
    }
    
    
    public void dump(){
        Debug d = Debug.getInstance() ;
    	d.msg(Debug.ALWAYS, XMLDeclaration);
    	d.msg(Debug.ALWAYS, configFileTag);
        for (int i = 0; i < myVector.size(); i++) {
            myVector.elementAt(i).dump();
        }
    	d.msg(Debug.ALWAYS, closeConfigFileTag);
//    	d.msg(Debug.ALWAYS, closeXMLTag);
    }
}


