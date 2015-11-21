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

package tm.cpp.analysis;


import java.util.Hashtable;

import tm.utilities.Assert;
import tm.utilities.TMFile;

/** Map unique file numbers to file.
 * Used so that the preprocessor can communicate to the parser
 * which files lines are in.  The unique file number is put in the "#file" directive.
 */

public class FileMap {
    Hashtable ht = new Hashtable() ;

    public void put( TMFile file ) {
        ht.put( new Integer( file.getUniqueNumber() ), file) ; }

    public TMFile get( Integer i ) {
        Object obj = ht.get( i ) ;
        Assert.check( obj != null ) ;
        return (TMFile) obj ; }
}