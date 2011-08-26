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

package tm.clc.ast;

import java.util.Hashtable ;

import tm.utilities.Debug;

/** A function definition for storage in a run-time structure.
 * At this level of abstraction we are neutral as to how the function
 * is implemented. I.e. whether it is interpreted, or built-in.
 * TODO consider whether the name and type really need to be stored in
 * these objects.
 * 
 * @author theo
 */
abstract public class AbstractFunctionDefn
{

    protected static Debug debug = Debug.getInstance ();
    private Object key ;
    
    protected AbstractFunctionDefn( Object key ) { this.key = key ; }
    
    /**
     * Return a unique identifier for this function definition, useful as a key 
     * in the runtime symbol table.
     * @return the unique id
     */
    public Object getKey () { return key ; }
    
    public String toString() {
        StringBuffer buf = new StringBuffer() ;
        buf.append( "Function Definition\n" ) ;
        buf.append( "  Key: "+getKey()+"\n" ) ;
        return buf.toString() ;
    }
}
