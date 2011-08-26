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


import tm.clc.analysis.ScopedName;

/** 
 * Represents an initialization found in a <em>ctor-initializer</em>. 
 * This is a preliminary representation sent by the parser to the analysis
 * package.
 */
public class ConstructorInitializer
{

    private ScopedName name ;
    private Initializer init ;
    
    public ConstructorInitializer( ScopedName name, Initializer init ) {
        this.name = name ;
        this.init = init ; }
    
    ScopedName get_name() { return name ; }
    
    Initializer get_initializer() { return init ; }

}
