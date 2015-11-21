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

import tm.backtrack.BTTimeManager;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.TyAbstractClassDeclared;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractObjectDatum;
import tm.cpp.analysis.Cpp_Specifiers;
import tm.cpp.datum.ObjectDatum;
import tm.interfaces.TypeInterface;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;

/** A class for representing C++ class and struct types
 */
public class TyClass extends TyAbstractClassDeclared implements TyCpp {

//    private Declaration classDecl ;

    public TyClass(String nm, ScopedName fqn, Declaration decl) {
        super(nm, fqn, decl) ;
    }
    
    protected AbstractObjectDatum constructDatum(int add, AbstractDatum p, Memory m, String n, BTTimeManager timeMan) {
        return new ObjectDatum( add, p, m, n, this, timeMan ) ; }
    
    public String typeId() { return typeId( "", false ) ; }
    
    public String typeId( String seed, boolean lastWasLeft ) {
        seed = getFullyQualifiedName().getName() + seed ;
        if( (getAttributes() & Cpp_Specifiers.CVQ_CONST) != 0 )
            seed = "const "+seed ;
        return seed ;
    }
}
