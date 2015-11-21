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

// Copied over from Cpp on June 9, 2003 by mpbl

import tm.clc.analysis.*;
import tm.clc.ast.*;
import tm.javaLang.ast.ExpEnsureClassInit;
import tm.javaLang.ast.TyClass;
import tm.javaLang.ast.TyJavaArray;
import tm.javaLang.ast.TyInt;
import tm.javaLang.ast.TyRef;
import tm.utilities.Assert;
import tm.utilities.Debug;

/**
 * Generates the AST representation of an id expression.
 */
public class CGRIdExp extends CodeGenRule {

    Java_CTSymbolTable symbolTable;
    private boolean isInitialization;
    /**
     * Creates a new instance of the IdExpression rule
     * @param st the compile-time symbol table for id resolution
     * @param initial true if this is for initialization
     */
    public CGRIdExp (CTSymbolTable st, boolean initial) {
        symbolTable = (Java_CTSymbolTable)st;
        isInitialization = initial;
    }

    /**
     * Creates a new instance of the IdExpression rule (not for initialization)
     * @param st the compile-time symbol table for id resolution
     */

    public CGRIdExp (CTSymbolTable st) {
        this(st, false);
    }

    /**
     * Creates a new node (or nodes) for an ExpressionName or FieldAccess. In the case
     * of a qualifiedName will sometimes operate recursively to generate multiple nodes.
     * Nodes may in fact be ExpId. Alternatively, they could be ExpThisMember (for
     * accessing fields of this object), or OpMember (for the case where the name is of 
     * the form Q.id and Q itself represents an expressionName, thus effectively being 
     * a . operator). This last is the case that operates recursively.
     *
     * The expressionPtr is mapped as follows:
     * @id is a Java_ScopedName representing the expressionName or field name 
     * @operand[0] is optional. If present contains an expressionNode representing the
     *     object whose field is to be accessed.
     *
     * @param exp the standard expressionPointer
     *
     * REVISIT FOR ARRAYS!!!!!!!
     */
    public void apply (ExpressionPtr exp) {
        // name lookup
        ExpressionNode en = null;
        Declaration match = null;
        DeclarationSet candidates = null;
        TyClass target = null;

        if (!exp.id().is_qualified()){
    // Handles both Simple Expression Names (JLS 6.5.6.1) and fieldAccess 
            if (exp.operandCount() > 0) { // Primary . Identifier field access
                en = exp.get(0);
                if( en.get_type() instanceof TyRef )
                      en = new ExpFetch( ((TyRef)en.get_type()).getPointeeType(), en );
                Assert.error(en.get_type()instanceof TyAbstractPointer, "Not an address");
                TyAbstractClass theTarget = (TyAbstractClass)((TyAbstractPointer)en.get_type()).getPointeeType();

//****** Early return: array length special case cuts out a lot of processing                  
                if (theTarget instanceof TyJavaArray && exp.id().getTerminalId().equals("length")){
                	en = new OpMember(new TyRef(TyInt.get()),
                			".",exp.id().getTerminalId(),
    						new int[0],exp.id(),en);
                	exp.set(en);
                	return;
                }
//*********************************
                
                
                target = (TyClass)theTarget;
                candidates = symbolTable.lookupMemberField(target, exp.id ());
            } else   // Simple Expression Name
                candidates = symbolTable.lookup (exp.id (),Java_LFlags.EXPRESSION_LF);
            // if lookup returns nothing, error 
            if (candidates == null || candidates.isEmpty ()) 
                Assert.error ("No entity found matching id " +  exp.id().getName ());
            // look for the match among the candidates
            // a single, accessible candidate is the match
            match = candidates.getSingleMember();
            Assert.error(match!=null, "Multiple entities found matching id " + exp.id().getName ());
            d.msg(Debug.COMPILE, "match is " + match.toString());
            Assert.error(symbolTable.isAccessible(match), match.getName().getName() + " is inaccessible from " +
                symbolTable.getCurrentScope().toString());
            if (target != null) { // Primary . Identifier fieldAccess
                SHType typeSH = (SHType) target.getDeclaration().getDefinition();
                if (!match.hasSpecifier (Java_Specifiers.SP_STATIC)) // non-static field access
                    en = new OpMember(
                        new TyRef(match.getType ()),
                        ".", exp.id().getTerminalId(), 
                         symbolTable.getRelativePath (match, typeSH),
                         match.getName(), en);
                else  { // Expression.static
                	en = makeStaticNode(match, exp.opid.getTerminalId() );
                	en = new ExpEnsureClassInit(target, en);
                }
                    
            } else if (match.getCategory().contains (Java_LFlags.FIELD_VARIABLE)) {
                    // For our example path should be empty
            	if (match.hasSpecifier (Java_Specifiers.SP_STATIC)) { // static member variable
            		en = makeStaticNode(match, exp.opid.getTerminalId() );           		
            		en = new ExpEnsureClassInit(
            				(TyClass)match.getEnclosingBlock().getClassDeclaration().getType(),
							en);
            	}
             	else  { // non-static member variable
             		Assert.error( !( (SHCommon)symbolTable.getCurrentScope() ).isStaticContext(),
             				"Can't refer to an instance variable in a static context");
                    en = new ExpThisMember
                        (new TyRef (match.getType ()),
                         exp.id().getName (),
                         symbolTable.getRelativePath (match),
                         match.getName ());
             	}
            } else { // local variable 
                // other var, use ExpId
                d.msg (Debug.COMPILE, "id expression created with runtime id " + 
                       ((ScopedName) match.getRuntimeId ()).getName ());
                en = new ExpId 
                    (new TyRef (match.getType ()), exp.id().getName (), 
                     (ScopedName) match.getRuntimeId ());
            }
              
        } else { // JLS 6.5.6.2 Qualified Expression Names
            /** "If an expression name is of the form Q.Id, then Q has already been
            * classified as a package name, a type name, or an expression name"
            * We do that classification here as covered by JLS 6.5.2 Reclassification
            * of Contextually Ambiguous Names
            **/
            String id = exp.id().getTerminalId();
            exp.id().removeTerminalId();
            Declaration qualifier = symbolTable.lookup(exp.id(), new LFlags(Java_LFlags.AMBIGUOUS_LF)).getSingleMember();
            Assert.error(qualifier!= null, "Unable to disambiguate " + exp.id().getName());

    // Now we revert to 6.5.6.2 - If Q is a package name, then a compile-time error occurs
            Assert.error(!qualifier.getCategory().intersects(Java_LFlags.PACKAGE_LF),
                "Can't have variable " + id + " in package " + qualifier.getName().getName());
            
    // Lookup for the next two sections is the same
            if (qualifier.getCategory().intersects(Java_LFlags.TYPE_LF)) {
                match = symbolTable.lookupMemberField((TyAbstractClassDeclared)qualifier.getType(),
                    new Java_ScopedName(id));
                Assert.error(match.getSpecifiers().contains(Java_Specifiers.SP_STATIC),
                        id + " must be a class variable (it must be declared static)");
                en = makeStaticNode(match, id );
                en = new ExpEnsureClassInit(
                		(TyClass)qualifier.getType().getBaseType(),
						en);
            }
    // In Theo's words, "This is the interesting one!"
            else if (qualifier.getCategory().intersects(Java_LFlags.EXPRESSION_LF)) {
                TypeNode t = qualifier.getType().getBaseType();
                Assert.error(  t instanceof TyAbstractClass,"Qualifier " + qualifier.getName().getName() +
                    " is not a reference type");
                
//****** Early return: array length special case cuts out a lot of processing                  
                if (t instanceof TyJavaArray && id.equals("length")){
                    apply(exp);
                    en = exp.get();
                    if( en.get_type() instanceof TyRef )
                          en = new ExpFetch( ((TyRef)en.get_type()).getPointeeType(), en );
                    en = new OpMember(
                            new TyRef(TyInt.get()),
                            ".", id,
                             new int[0],
                             exp.id(), en);
                    exp.id().append(id);
                    exp.set (en);
                    return;                   
                }
//*********************************
                
                TyAbstractClassDeclared type = (TyAbstractClassDeclared)t;
                match = symbolTable.lookupMemberField(type, new Java_ScopedName(id));
                if (!match.hasSpecifier (Java_Specifiers.SP_STATIC)) { // non-static member variable
                    d.msg (Debug.COMPILE, "data member access expression created");
                    apply(exp);
                    en = exp.get();
                    if( en.get_type() instanceof TyRef )
                          en = new ExpFetch( ((TyRef)en.get_type()).getPointeeType(), en );

                    en = new OpMember(
                        new TyRef(match.getType ()),
                        ".", id,
                         symbolTable.getRelativePath (match, (SHCommon)qualifier.getClassScope()),
                         match.getName(), en);
                    
               } else { // static member variable
                    d.msg (Debug.COMPILE, "id expression created with runtime id " + 
                           ((ScopedName) match.getRuntimeId ()).getName ());
                    en = makeStaticNode(match, id);
               }
             }
            exp.id().append(id);
        }
        
        if( match.hasIntegralConstantValue() ) {
            en.set_integral_constant_value( match.getIntegralConstantValue()); }
        if( match.hasFloatingConstantValue() ) {
                en.set_floating_constant_value( match.getFloatingConstantValue()); }
    // Variables declared as finally are converted to rvalues.
        Assert.check( en.get_type() instanceof TyRef ) ;
        if( match.hasSpecifier( Java_Specifiers.SP_FINAL) && !isInitialization) {
            en = new ExpFetch( ((TyRef)en.get_type()).getPointeeType(), en ); }
        exp.set (en);
    }
    
    
    private ExpressionNode makeStaticNode(Declaration match, String id){
    	Declaration matchClass = match.getClassScope().getOwnDeclaration();
        TyClass tyClass = (TyClass) matchClass.getType() ;
        ExpressionNode classNode = new ExpId(
        		new TyRef (tyClass.getMetaClass()), tyClass.typeId(), 
             (ScopedName) matchClass.getRuntimeId ());
        return new OpMember( new TyRef(match.getType()), ".",
        		id,
				symbolTable.getRelativePath (match, (SHCommon)matchClass.getScopeHolder()),
				match.getName(), classNode);
    	
    }
}
