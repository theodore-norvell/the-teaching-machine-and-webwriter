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
 * Created on Jul 5, 2005
 */
package tm.clc.ast;

import java.util.Hashtable;

/** Collect definitions of virtual functions.
 * @author theo
 *
 */
public class VirtualFunctionTable {
    Hashtable hashtable = new Hashtable() ;
    
    /** Add a definition. The key of the definition should be the key of the overridden virtual
     *  function. */
    
    public void add( AbstractFunctionDefn defn) {
        hashtable.put( defn.getKey(), defn ) ;
    }
    
    /** @return null if the key does not map to anything. Otherwise the definition provded earlier. */
    public AbstractFunctionDefn get( Object key ) {
        return (AbstractFunctionDefn) hashtable.get( key ) ;
    }
}
