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

package tm.interfaces;

public interface ViewableST {
    /** Returns the i'th entry in the symbol table. Assumes the entries
        run from 0 to numEntries - 1 BUT no assumptions are made as to order. */
    STEntry getEntry(int i);

    /** The entry at address a or null if no named variable there  */
    STEntry getEntryAt(int a);

    /** The number of entries. */
    int size() ;

    /** The number of entries in the current stack frame */
    int varsInCurrentFrame() ;

    /** The number of entries in the global frame. */
    int varsInGlobalFrame() ;
}



