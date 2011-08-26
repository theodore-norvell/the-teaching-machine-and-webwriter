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
 * Created on May 26, 2005
 *
 */
package tm.clc.ast;

import java.util.Vector;

import tm.clc.analysis.Declaration;
import tm.clc.analysis.ScopedName;
import tm.interfaces.TypeInterface;

/**
 * @author theo
 *
 */
public abstract class TyAbstractClassDeclared extends TyAbstractClass {

    // fully_qualified_name -- An unambiguous name.
    private ScopedName fully_qualified_name ;
    
    private Declaration classDecl;

    public TyAbstractClassDeclared(String nm, ScopedName fqn, Declaration cDecl) {
        super(nm) ;
        fully_qualified_name = fqn ;
        classDecl = cDecl;
    }
    
    /** fetch the compile time declaration
     *  * @return the declaration
     */
    public Declaration getDeclaration() {
        return classDecl ; }

    /** retrieve the fully qualified name of this class
     * @return the fully qualified name
     */
    public ScopedName getFullyQualifiedName () {
        return fully_qualified_name; }
    
    /** Are the types "equal"?
     * @param t the type to be compared to this type
     * @return true if the names are equivalent
     */
    public boolean equal_types( TypeInterface t ) {
            // We implement `name' equivalance rather than
            // `structural' equivalence.
            if( t instanceof TyAbstractClassDeclared ) {
                TyAbstractClassDeclared tac = (TyAbstractClassDeclared) t ;
                    return tac.fully_qualified_name.equals( fully_qualified_name ) ; }
            else {
                return false ; } }

}
