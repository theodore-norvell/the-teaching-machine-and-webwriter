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

/*
 * Created on 7-Jul-2006 by Theodore S. Norvell. 
 */
package tm.plugins;

/** 
     * <p>Each Requirement represents a requirement that some plug-in has for other plug-ins.
     * </p>
     * <p>Typically <code>Requirment</code>s are returned from a <code>PlugInFactory</code>
     * as an array specifying all the requirements that a plug in has in terms of
     * other plug-ins.
     * </p>
     * <p>For example the object created by
     *    <pre>   new Requirement("fred", FredFactoryInterface.class, true, false )</pre>
     *    represents that there must be exactly 1 factory plugged in to the jack named
     *    <code>"fred"</code> and that that factory must implement the interface
     *    (or class) called <code>FredFactoryInterface</code>.
     * 
*/
public class Requirement {

    private String jackName;
    private Class<? extends PlugInFactory> interfaceRequired;
    private boolean mandatory;
    private boolean multiple;

    /** Create a Requirement.
     * 
     * @param jackName The name of the jack.
     * @param interfaceRequired The interface that should be implemented by each plug-in factory that
     * pluged in to the given jack name.
     * @param mandatory If this is true, then at least 1 factory must be plugged in to the given jack name.
     * @param multiple If this is true, more than 1 factory may be plugged in to the given jack name.
     */
    public Requirement(String jackName, Class<? extends PlugInFactory> interfaceRequired, boolean mandatory, boolean multiple ) {
        this.jackName = jackName ;
        this.interfaceRequired = interfaceRequired ;
        this.mandatory = mandatory ;
        this.multiple = multiple ;
    }

    /**
     * @return Returns the interface required.
     */
    public Class<? extends PlugInFactory> getInterfaceRequired() {
        return interfaceRequired;
    }

    /**
     * @return Returns the jack name.
     */
    public String getJackName() {
        return jackName;
    }

    /**
     * @return Returns true if the jack must be plugged into.
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * @return Returns true if multiple plug-ins may be plugged into the jack.
     */
    public boolean isMultiple() {
        return multiple;
    }
}
