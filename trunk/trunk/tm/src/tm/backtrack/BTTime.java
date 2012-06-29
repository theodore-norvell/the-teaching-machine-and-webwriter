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
class BTTime {
    int epoch ;     // The number of preceding times on this line
    BTTime prev ;   // The previous time
    String description ;

    BTTime( BTTime p, String description ) {
        if( p==null) epoch = 0 ;
        else epoch = p.epoch + 1 ;
        prev = p ;
        this.description = description ; }

    static BTTime leastCommonTime( BTTime a, BTTime b ) {
        while( a.epoch > b.epoch ) a = a.prev ;
        while( b.epoch > a.epoch ) b = b.prev ;
        while( a != b ) {a = a.prev; b = b.prev ; }
        return a ; }
}