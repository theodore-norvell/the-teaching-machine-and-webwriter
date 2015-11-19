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


public interface PointerInterface extends ScalarInterface {

    
    /**  Return the datum the pointer points at. Return null
    if the pointer is null or otherwise invalid.
    <p>
    NOTE: In any language (e.g. C++) in which a pointer can be considered
    to have a value of its own (i.e. the address at which it is pointing)
    the getValueString() and getByte(i) methods should work appropriately.
    The getTypeString() method should return a terse string consistent with
    the syntax of the language, e.g. "int *" in C where the pointer derefs
    an integer.
     */
	public Datum deref();


    public boolean isNull() ;

}

