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

package tm.backtrack;

/** A record of a past or present value of a backtrackable object.
 */
class BTRecord<T> {
    T value ;  // The value of the object.
    BTTime end ;    // The last time at which this value is valid.
    BTTime start ;  // The first time at which this value is valid.
    BTRecord<T> prev ; // Link to previous records for the object.

    BTRecord( T v, BTTime e, BTTime s, BTRecord<T> p ) {
        value = v ;
        end = e ;
        start = s ;
        prev = p ;
    }
}