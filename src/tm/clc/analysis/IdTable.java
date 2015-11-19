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

package tm.clc.analysis;

import java.util.Hashtable ;

/** The IdTable is used to throw away duplicate
    copies of a string
*/
public class IdTable {
    private Hashtable h = new Hashtable() ;
    
    /** Returns a string equal to id. If a string
        equal to id was previously intered, then that
        string is returned. Otherwise, id is intered and
        returned. */
    public String inter (String id) {
        String id1 = (String) h.get(id) ;
        if (id1 == null) {
            h.put (id, id);
            return id ; }
        else {
            return id1 ; }
    }
}