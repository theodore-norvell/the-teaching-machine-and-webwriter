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

package tm.javaLang.analysis;

import tm.clc.analysis.CTSymbolTable;
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.ast.ExpFetch;
import tm.clc.ast.ExpThisMember;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TyAbstractClassDeclared;
import tm.javaLang.analysis.Java_CTSymbolTable;
import tm.javaLang.ast.TyRef;
import tm.utilities.Assert;



/**
 * Generates the AST representation of an id expression.
 */
public class CGRSuperExp extends CodeGenRule {

    Java_CTSymbolTable symbolTable;
    /**
     * Creates a new rule instance
     * @param st the compile-time symbol table for id resolution
     */
    public CGRSuperExp(CTSymbolTable st) {
        symbolTable = (Java_CTSymbolTable)st;
    }

    /**
     * Creates a new node (or nodes) for super FieldAccess, either in the form
     * "super.Identifier" or "ClassName.super.Identifier"
     * Both produce an ExpThisMember node.
     *
     * The expressionPtr is mapped as follows:
     * @opid is a Java_ScopedName representing the identifier 
     * @opcat is a Java_ScopedName representing the ClassName. null if none
     *
     * @param exp the standard expressionPointer
     *
     * REVISIT FOR ARRAYS???????
     */
    public void apply (ExpressionPtr exp) {
        SHType targetType = null;
        if(exp.opcat != null) {
            Declaration target = symbolTable.lookup(exp.opcat,Java_LFlags.CLASS_LF).getSingleMember();
            Assert.error(target!=null, "Cannot find class " + exp.opcat.getName());
            Assert.error(symbolTable.isAccessible(target), "Cannot access class "  + exp.opcat.getName());
            targetType = (SHType)target.getDefinition();
        }
        else targetType = (SHType)symbolTable.getCurrentScope().getClassDeclaration().getDefinition();

        targetType = targetType.getTheSuperType();
        Declaration match = symbolTable.lookupMemberField(
            (TyAbstractClassDeclared)targetType.getClassDeclaration().getType(), exp.opid).getSingleMember();
        Assert.error(match!=null, "Cannot find id " + exp.opid.getName());
        Assert.error(symbolTable.isAccessible(match), "Cannot access id "  + exp.opid.getName());
        
        ExpressionNode en = new ExpThisMember
            (new TyRef (match.getType ()),
             exp.id().getName (),
             symbolTable.getRelativePath (match),
             match.getName ());
        if( match.hasIntegralConstantValue() ) {
            en.set_integral_constant_value( match.getIntegralConstantValue()); }
        if( match.hasFloatingConstantValue() ) {
                en.set_floating_constant_value( match.getFloatingConstantValue()); }
    // Variables declared as finally are converted to rvalues.
        Assert.check( en.get_type() instanceof TyRef ) ;
        if( match.hasSpecifier( Java_Specifiers.SP_FINAL ) ) {
            en = new ExpFetch( ((TyRef)en.get_type()).getPointeeType(), en ); }
        exp.set (en);
    }
}

