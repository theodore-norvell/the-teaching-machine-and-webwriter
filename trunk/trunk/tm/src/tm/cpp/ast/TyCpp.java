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

package tm.cpp.ast;

/**
 * Interface for additional capabilities of C++ types.
 */
public interface TyCpp {
    
    /** Create a C-style type representation for the type.
        For example "const char ", "char *", "char (*)[10]" etc.
      @param seed What's been built so far.
      @param lastWasLeft Whether the last addition to the seed was on the left or not.
	         This is used to determine whether to add paretheses to the seed.
    */
    String typeId( String seed, boolean lastWasLeft ) ;

}