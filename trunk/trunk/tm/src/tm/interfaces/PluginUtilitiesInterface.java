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
 * Created on 2009-06-24 by Theodore S. Norvell. 
 */
package tm.interfaces;

import tm.utilities.TMFile;

/** This interface lists routines that plugins (especially display plugins)
 * might want access to, but which are not implemented by the EvaluatorInterface
 * as they are not implemented by the Evaluator.
 * 
 * @author theo
 */
public interface PluginUtilitiesInterface {

    
    /** Get a file of the users choosing.
     * This might invoke a file chooser dialog.  However in an applet,
     * security considerations typically prevent the use of file choosers,
     * so in this case the data files might come from back on the server.  
     * @return a data file of the users choosing.
     */
    public TMFile getDataFile() throws Throwable ;
}
