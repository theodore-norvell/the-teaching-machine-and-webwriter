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

/** This exception is thrown when a method has
 * trouble creating an instance of a plug-in factory.
 * @author theo
 */
public class PlugInNotFound extends Exception {

    public PlugInNotFound() {
        super();
    }

    public PlugInNotFound(String message) {
        super(message);
    }

    public PlugInNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public PlugInNotFound(Throwable cause) {
        super(cause);
    }
}
