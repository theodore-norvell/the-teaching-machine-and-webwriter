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

package tm.javaLang.ast;

/*******************************************************************************
Interface: TyJava

Overview:
This interface is an interface for Java types.

Review:          2002 06 13     Jonathan Anderson and Michael Burton
*******************************************************************************/

public interface TyJava extends tm.interfaces.TypeInterface {
    public String elementId();
    public String typeId();
    
    /** Tests whether another type can be widened to this type.
     * <p>This test is fussy in that pointers can only widen
     * to pointers, classes can only widen to classes and so on.
     * The reason is that this test is generally combined with an
     * equalTypes test and that test is also fussy; the two tests
     * should have the same expectations about how their inputs are
     * prepared.</p> */
    public boolean isReachableByWideningFrom(TyJava fromType);
}

