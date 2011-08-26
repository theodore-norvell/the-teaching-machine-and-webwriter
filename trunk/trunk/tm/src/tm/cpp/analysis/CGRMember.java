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

import tm.clc.analysis.CTSymbolTable;
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.Definition;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.LFConst;
import tm.clc.analysis.LFlags;
import tm.clc.analysis.ScopedName;
import tm.clc.analysis.UndefinedSymbolException;
import tm.clc.ast.ExpId;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.OpMember;
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.ExpFunctionName;
import tm.cpp.ast.ExpMemberName;
import tm.cpp.ast.TyRef;
import tm.cpp.parser.ParserConstants;
import tm.utilities.Assert;
import tm.utilities.Debug;

/**
 * Generates the AST representation of member access in both forms
 * (., ->).
 */
public class CGRMember extends CodeGenRule implements ParserConstants {

    static final String NO_SUCH_MEMBER = "Member named {0} not found." ;
    
    static final String dot = OpTable.get (DOT);
    static final String arrow = OpTable.get (ARROW);

    boolean dotNotation;

    CTSymbolTable symbolTable;

    /** 
     * Creates a new rule instance
     * @param symbolTable the compile-time symbol table for id resolution
     */
    public CGRMember (CTSymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        dotNotation = true;
    }

    /** 
     * Creates a new rule instance
     * @param symbolTable the compile-time symbol table for id resolution
     * @param dotNotation dot or arrow notation indicator
     */
    public CGRMember (CTSymbolTable symbolTable, boolean dotNotation) {
        this.symbolTable = symbolTable;
        this.dotNotation = dotNotation;
    }


    public void apply (ExpressionPtr exp) {
        d.msg (Debug.COMPILE, ">> generating member access expression ");

        Assert.check (exp.get (1) instanceof ExpMemberName);
        ExpMemberName member = (ExpMemberName) exp.get (1); 
        ScopedName member_sn = member.getName(); 
        String member_name = member_sn.getTerminalId (); 

        // find the class/object declaration
        TypeNode ot = exp.get_base_type (0);
        if (!dotNotation) { // need the pointee type to find the class object
            // but we can't dereference the pointer because OpMember doesn't
            // want us to do that
            Assert.check (ot instanceof TyAbstractPointer);
            ot = ((TyAbstractPointer) ot).getPointeeType ();
        }
        Assert.check (ot instanceof TyAbstractClass);
        Declaration cdecl = 
            (symbolTable.getClassDeclaration ((TyAbstractClass) ot));

        // get the class scope from this
        Assert.check (cdecl != null);
        Definition defn = cdecl.getDefinition ();
        d.msg (Debug.COMPILE, ">> class scope is " + defn.getClass().getName ());
        Assert.check (defn instanceof ClassSH);
        ClassSH c_scope = (ClassSH) defn;
        
        // !! what about qualified member names.. we have code for this 
        //    somewhere
        Declaration match = null;
        
        DeclarationSet declSet ;
        try {
            declSet = c_scope.lookup(member_sn,
                                     new LFlags (Cpp_LFlags.VARIABLE + Cpp_LFlags.FUNCTION)) ; }
        catch( UndefinedSymbolException e ) {
            Assert.apology( NO_SUCH_MEMBER, member_sn.getName() ) ;
            declSet=null; }
        
        // For reasons I don't understand, the above try-catch doesn't do the
        // trick. So I am adding the following test.  Probably the correct
        // thing to do is to fix lookup to throw the exception when there
        // is no match. Syste
        if( declSet.isEmpty() ) {
            Assert.apology( NO_SUCH_MEMBER, member_sn.getName() ) ; }
            
        match = declSet.getFirstMember (); // may be overloaded function id

        Assert.check (match != null);

        // build the member expression
        ExpressionNode member_exp;
        String operator = exp.op().getName ();

        if (match.getCategory().intersects (Cpp_LFlags.FUNCTION)) {
            d.msg (Debug.COMPILE, ">> class method is being accessed, returning ExpFunctionName for " + member_sn.getName ());
            member_exp = new ExpFunctionName (member_sn);
            ((ExpFunctionName) member_exp).setClassObject (exp.get (0));
            ((ExpFunctionName) member_exp).setClassScope (c_scope);
            ((ExpFunctionName) member_exp).setMemberAccessOperator (operator);
        } else { // data member - create member access expression

            d.msg (Debug.COMPILE, ">> details are : ");
            d.msg (Debug.COMPILE, ">> type : ref to " + match.getType().getTypeString ());
            d.msg (Debug.COMPILE, ">> operator : " + operator);
            d.msg (Debug.COMPILE, ">> member name : " + member_sn.getTerminalId ());
            StringBuffer path = new StringBuffer ();
            int [] relative_path = match.getRelativePath ();
            for (int i = 0; i < relative_path.length; i++) {
                path.append ("" + relative_path [i]);
                if (i != relative_path.length - 1) 
                    path.append (", ");
            }
            d.msg (Debug.COMPILE, ">> path : " + ((relative_path.length == 0) ? "in class" : path.toString ()));
            d.msg (Debug.COMPILE, ">> member id : " + member_sn.getName ());
            member_exp = 
                new OpMember (new TyRef (match.getType ()), operator, 
                               member_sn.getTerminalId (),
                               match.getRelativePath (), match.getName (), 
                               exp.get (0));
            if( match.hasIntegralConstantValue() ) {
                member_exp.set_integral_constant_value( match.getIntegralConstantValue()); }
                
        }
        exp.set (member_exp);
    }
}
