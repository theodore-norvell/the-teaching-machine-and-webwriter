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

/*
 * SHBlock.java
 *
 * Created on April 2, 2003, 8:36 AM
 */

package tm.javaLang.analysis;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.ScopeHolder;

/** The {@link ScopeHolder} for Java blocks which includes function blocks as well as
 * anonymous blocks
 * @author mpbl
 */
public class SHBlock extends SHCommon {


    /** Creates a new instance of SHBlock
     * @param encl The enclosing ScopeHolder
     */
    public SHBlock(ScopeHolder encl) {
        super(encl);
    }



    /** Creates a new blockScope
     * @return the new scopeHolder for the block
     */
    public SHCommon createBlockScope(Declaration decl) {
       return createBlock(decl, new SHBlock(this));
    }

    /** Creates a new localScope, an anonymous ScopeHolder
     * that can holds only its own Declaration
     * @return the new scopeHolder for the Type
     * @param decl The declaration for the Type
     */
    public SHCommon createLocalScope(Declaration decl){
        return createLocal(decl);
    }

    /** Creates a new local Type, which has a sHLocal to
     *  localize its scope as well as a SHType to hold its definition.
     * @return the new scopeHolder for the Type
     * @param decl The declaration for the Type
     */

    public SHCommon createTypeScope(Declaration decl){
        return createLocalType(decl);
    }





    /** Standard descriptor
     * @return "block scope"
     */
     public String toString(){
     	return (isStaticContext() ?	"static " : "" ) + 	"block scope";
     	}

}
