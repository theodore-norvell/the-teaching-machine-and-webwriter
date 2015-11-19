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
 * CGRMethodCall.java
 *
 * Created on August 13, 2003, 8:13 AM
 */

package tm.javaLang.analysis;

/**
 *
 * @author  mpbl
 */
import tm.clc.analysis.*;
import tm.clc.ast.ExpThis;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.NodeList;
import tm.clc.ast.OpFuncCall;
import tm.clc.ast.OpMemberCall;
import tm.clc.ast.TyAbstractClassDeclared;
import tm.clc.ast.TypeNode;
import tm.javaLang.ast.TyPointer;
import tm.javaLang.ast.ExpEnsureClassInit;
import tm.javaLang.ast.TyClass;
import tm.javaLang.ast.TyFun;
import tm.javaLang.ast.TyRef;
import tm.utilities.Assert;
import tm.utilities.Debug;


public class CGRMethodCall extends CGRInvocation {
    boolean superCall = false;
    
    /**
     * Creates a new rule instance for method calls (not constructor calls)
     * @param st the compile-time symbol table for id resolution
     */
    public CGRMethodCall(Java_CTSymbolTable st) {
        super(st);
    }
    
    public CGRMethodCall(Java_CTSymbolTable st, boolean sCall) {
        super(st);
        superCall = sCall;
    }
    
    /**
     * <P><strong>Precondition:</strong>
     * <ul><li> For calls of the form Primary . name ( args )
     *          <ul><li> exp.id() is the name (simple)
     *              </li> 
     *              <li> exp.get_list(0) is the arguments
     *              </li>
     *              <li> exp.get(1) is the primary expression
     *              </li>
     *              <li> superCall is false
     *              </li>
     *          </ul>
     *     </li>
     *     <li> For calls of the form MethodName ( args ) 
     *          <ul><li> exp.id() is the MethodName part (possibly qualified)
     *              </li> 
     *              <li> exp.get_list(0) is the arguments
     *              </li>
     *              <li> superCall is false
     *              </li>
     *          </ul>
     *     </li>
     *     <li> For calls of the form super . name ( args ) 
     *          <ul><li> exp.id() is the name part (unqualified)
     *              </li> 
     *              <li> exp.get_list(0) is the arguments
     *              </li>
     *              <li> superCall is true
     *              </li>
     *          </ul>
     *     </li>
     * </ul>
     */
    public void apply (ExpressionPtr exp) {
        DeclarationSet  candidates = null;
        ScopedName name = exp.id();
        boolean mustBeStatic = false;
        boolean isInThisClass = false;
        boolean suppress = true;
        ExpressionNode recipient = null;    // The implicit object
        TyClass otherClass = null;		// The qualifier class, if there is one
        boolean scriptRelay = false;
        
// JLS Section 15.12.1 Compile-Time Step 1: Determine Class or Interface to Search
        if (exp.operandCount() > 1) { // form expressionNode . methodCall(...)
            recipient = exp.get(1);
            TypeNode recipientType = recipient.get_type() ;
            String badTypeError = "Method call to non object" ;
            Assert.error( recipientType instanceof TyPointer, badTypeError ) ;
            recipientType = ((TyPointer)recipientType).getPointeeType() ;
            Assert.error( recipientType instanceof TyAbstractClassDeclared, badTypeError ) ;
            candidates = symbolTable.lookupMemberMethods((TyAbstractClassDeclared)recipientType, name);
        }
        else if (name.is_qualified()) {
            String id = exp.id().getTerminalId();
            name.removeTerminalId();
            
            Declaration qualifier =
                symbolTable.lookup(name, new LFlags(Java_LFlags.AMBIGUOUS)).getSingleMember();
            Assert.error(qualifier != null, "Unable to disambiguate qualifier " +
                name.getName() + "in lookup of method " + id);
            if (qualifier.getCategory().intersects(Java_LFlags.PACKAGE_LF))
                Assert.error(name.getName() +
                " is a package and packages cannot directly contain method " +
                id);
            else if (qualifier.getCategory().intersects(Java_LFlags.INTERFACE_LF))
                Assert.error(name.getName() +
                " is an interface and cannot contain a static method " );
            else if (qualifier.getCategory().intersects(Java_LFlags.CLASS_LF)) {
                candidates = ((SHCommon)qualifier.getDefinition()).
                    lookup(id, new LFlags(Java_LFlags.METHOD_LF));
                mustBeStatic = true; //Class.method => static
                otherClass = (TyClass) qualifier.getType().getBaseType();
                
// Trap for script relays added April 12, 2007 by mpbl
                if((id.equals("relay") || id.equals("relayRtnInt") || id.equals("relayRtnDouble"))
                		&& name.getTerminalId().equals("ScriptManager"))
                	scriptRelay = true;
            }
            else if (qualifier.getCategory().intersects(Java_LFlags.VARIABLE_LF)) {
                TypeNode qualifierType = qualifier.getType().getBaseType() ;
                Assert.error( qualifierType instanceof TyAbstractClassDeclared,
                        "Left operand of \".\" is not of a class type." ) ;
                candidates = symbolTable.lookupMemberMethods(
                                 (TyAbstractClassDeclared)qualifierType,
                                 new Java_ScopedName(id)) ;
                ExpressionPtr ePntr = new ExpressionPtr(qualifier.getName(),new Object [] {});
                CodeGenRule rule = new CGRIdExp (symbolTable);
                rule.apply (ePntr);
                rule = new CGRFetch();
                rule.apply(ePntr);
                recipient = ePntr.get();
                suppress = false;
            }
            else Assert.check("qualifier " + name.getName() + "for method " + id +
            " invocation is not of a category recognized by JLS");
            name.append(id);
        }
        else {// This class or super
        	isInThisClass = true;
            if (superCall){
                SHCommon thisScope = (SHCommon)symbolTable.getCurrentScope();
                Declaration target = thisScope.getClassDeclaration();
                Assert.error(target.getCategory().contains(Java_LFlags.INTERFACE_LF),
                    "Immediate Type enclosing super call cannot be an interface");
                if (exp.opcat != null) { // form is ClassName.super . Identifier
                    target = symbolTable.lookup(exp.opcat, new LFlags(Java_LFlags.TYPE)).getSingleMember();
                    Assert.error(target!=null,"Can't find class " + exp.opcat.getName());
                    Assert.error(
                        thisScope.enclosedBy((SHCommon)target.getDefinition()),
                        target.getName().getName() + " doesn't lexically enclose " + 
                        thisScope.toString());
                }
                // Now we have the class whose superclass we want. Find "the" superclass
                candidates = symbolTable.lookupMemberMethods(
                        ((SHType)target.getDefinition()).getTheSuperType(), name);
            }
            else candidates = symbolTable.lookup (exp.id (), new LFlags(Java_LFlags.METHOD));
        }
        
        ExpressionNode en = null;
        NodeList arguments = exp.get_list(0);
        
        // if lookup returns nothing, error 
        if (candidates == null || candidates.isEmpty ()) 
            Assert.error ("No function found matching id " +  exp.id().getName ());

 // Trap for script relays - no argument matching is carried out
        // look for the match among the candidates
       DeclarationSet possibles = (scriptRelay) ? candidates :
    	   	findClosestMethod(candidates, arguments);
        Assert.error(!possibles.isEmpty(),"No such method is accessable or applicable");
        Declaration method = possibles.getSingleMember();
        Assert.error(method!=null,"Ambiguous match for methods.");
        
/*        if (method.getSpecifiers().contains(Java_Specifiers.SP_NATIVE))
        	NativeMethodManager.setCalled(method);*/
        
        if (method.getCategory().contains(Java_LFlags.STATIC_LF)) {
            en = new OpFuncCall (
                ((TyFun)method.getType()).returnType(),
                exp.opid.getTerminalId(),
                false,
                method.getRuntimeId(),
                arguments );
            if (otherClass != null) // Static method of another class, make sure it is initialized
	            en = new ExpEnsureClassInit(otherClass, en);
        }
        else {
            Assert.error(!mustBeStatic,
                "method " + method.getName().getName() + " is not static ");
            Assert.error(!( ((SHCommon)symbolTable.getCurrentScope ()).isStaticContext() && isInThisClass),
                "Can't access " + method.toString() + " in a static context");
            if (recipient == null) {    // must be this object
                TyClass tyClass = (TyClass)
                    ((SHCommon)method.getDefinition()).getClassDeclaration().getType();
                recipient = new ExpThis(new TyRef(tyClass), "this");
            }
        
            Debug.getInstance().msg(Debug.COMPILE, "recipient of call to method " + method.getName().getName() +
                " is " + recipient.toString());
            en = new OpMemberCall(
                ((TyFun)method.getType()).returnType(),
                exp.opid.getTerminalId(),
                ".", false, suppress,
                method.getRuntimeId(),
                true,
                recipient,
                new int[0],
                arguments
                );
        }
        exp.set(en);
        
    }
}
