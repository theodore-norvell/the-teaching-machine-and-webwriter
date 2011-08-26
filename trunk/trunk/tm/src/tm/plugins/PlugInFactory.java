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
 * Created on 6-Jul-2006 by Theodore S. Norvell. 
 */
package tm.plugins;

/**
 * <p>In addition to the methods listed below, each instance of this class
 * should have a static method called createInstance which takes a single
 * parameter of type String and which returns an object that implements
 * PlugInFactory. The String parameter is supplied by the PlugInManager to
 * differentiate between variations on the same plug-in.  E.g. store views
 * that view different regions.</p>
 * 
 * <p>By convention, the object returned from getInstance
 * will implement a subinterface of this interface, which will
 * have a public method called "createPlugIn". The parameters 
 * of "createPlugIn" will vary, and so the method cannot
 * be declared here and cannot be put under compiler control.</p>
 *<p>
 * Although not currently strictly necessary, createPlugIn should return
 * an implementation of the PlugInInterface. It's not currently strictly
 * necessary because the PlugInInterface is a tag interface but that
 * might change in the future.
 * 
 * @author tsn
 * @see tm.interfaces.PlugIn
 */
public interface PlugInFactory {
    
    // static PlugInFactory createInstance(String) ;
    
    /** Specifies all the requirements of a plug-in.
     * <p> 
     *  If the plug-ins created by this factory use other plug-ins,
     *  their requirements should be represented by the array returned
     *  from this function.
     *  @return an array. If the plug-in has no requirements, then an empty array or null can be returned. 
     */
    Requirement[] getRequirements() ;

}
