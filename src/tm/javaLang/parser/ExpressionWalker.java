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

package tm.javaLang.parser;

import java.util.Vector;
import tm.clc.analysis.CGRFetch;
import tm.clc.analysis.CGRParentheses;
import tm.clc.analysis.CodeGenRule;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.LFlags;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.ExpArgument;
import tm.clc.ast.ExpResult;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.NoExpNode;
import tm.clc.ast.NodeList;
import tm.clc.ast.OpAssign;
import tm.clc.ast.TypeNode;
import tm.interfaces.SourceCoords;
import tm.javaLang.analysis.*;
import tm.javaLang.ast.TyBoolean;
import tm.javaLang.ast.TyByte;
import tm.javaLang.ast.TyChar;
import tm.javaLang.ast.TyClass;
import tm.javaLang.ast.TyInt;
import tm.javaLang.ast.TyPointer;
import tm.javaLang.ast.TyPrimitive;
import tm.javaLang.ast.TyRef;
import tm.javaLang.ast.TyShort;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.utilities.TMException;
import tm.virtualMachine.VMState;


/**
 * Part of the fourth pass. ExpressionWalkers walk the parse tree for expressions,
 * generating the Abstract Syntax Tree (which should really be called the Abstract
 * Code Graph) for the expression.
 * 
 * @author  mpbl
 */

public class ExpressionWalker implements JavaParserTreeConstants {
    private Java_CTSymbolTable symtab;
    private JavaExpressionBuilder eBuilder;

    /** Creates a new instance of an ExpressionWalker */
    public ExpressionWalker(Java_CTSymbolTable st) {
        symtab = st;
        eBuilder = JavaExpressionBuilder.getBuilder(st);
    }

    /** Recursive processing of ExpressionNodes
     * @param nStart the parserTree node which acts as the root for the current
     * 			piece of expression
     * @param vms the Virtual Machine State
     * @return the expressionNode which is the root of the code graph
     * 			corresponding to start
     */
    ExpressionNode process(Node nStart, VMState vms) {
        SimpleNode n = (SimpleNode) nStart;
        ExpressionPtr expPtr = null;
        CodeGenRule rule;
        boolean staticContext = ((SHCommon)symtab.getCurrentScope()).isStaticContext();
        {
            Debug.getInstance().msg(Debug.COMPILE, "Line #" + n.getCoords().getLineNumber()+
                ": Expression Walking in n.id = " + jjtNodeName[n.id] +
                "    n.name = " + n.getName());//((n.getDecl()==null) ? "" : n.getDecl().getName().toString()));
        }


        try{
            switch(n.id){
                case JJTTHISEXPLICITCONSTRUCTORINVOCATION: {
                    NodeList args = processArguments( (SimpleNode)n.jjtGetChild(0), vms) ;
                    
                    Declaration constructorDecl = ((SimpleNode) n.jjtGetParent().jjtGetParent()).getDecl() ;
                    Java_ScopedName classFQN = (Java_ScopedName)
                        constructorDecl.getClassScope().getClassDeclaration().getName();
                    return thisConstructorCall( classFQN, args) ;
                }
                
                case JJTSUPEREXPLICITCONSTRUCTORINVOCATION : {
                    ExpressionNode outer = null ;
                    int argsIndex = 0 ;
                    if( n.getBoolean() ) {
                        outer = process( n.jjtGetChild(0), vms ) ;
                        argsIndex = 1 ; }
                        
                    NodeList args= processArguments( (SimpleNode)n.jjtGetChild(argsIndex), vms) ;
                    
                    Declaration constructorDecl = ((SimpleNode) n.jjtGetParent().jjtGetParent()).getDecl() ;
                    Java_ScopedName classFQN = (Java_ScopedName)
                        constructorDecl.getClassScope().getClassDeclaration().getName();
                    return superConstructorCall( classFQN, outer, args) ;
                }
                
                case JJTBINOP: { /* Represents the application of a binary operator
                                    int attribute: The token kind of the operator.
                                    String attribute: The image of the operator.
                                    Child(0): The left operand. An expression node.
                                    Child(1): The right operand. An expression node.
                                    generate new OpInt: OpLogical or OpFloat **/
                    expPtr  = new ExpressionPtr (new Java_ScopedName(n.getString()),
                    		new Object [] {});
                    expPtr.addOperand(process(n.jjtGetChild(0), vms));
                    expPtr.addOperand(process(n.jjtGetChild(1), vms));
                    eBuilder.apply(expPtr);
                }
                break;

                case JJTLITERAL: {
                    return Literals.makeConstant(n.getInt(), n.getString(), symtab, vms);
                }
                case JJTCONDITIONAL: {/*Represents a conditional expression: E0 ? E1 : E2
                                        Child(0): The first operand. An expression node.
                                        Child(1): The second operand. An expression node.
                                        Child(2): The third operand. An expression node.
                                        */
                    punt();
                }
                break;

                case JJTINSTANCEOF: {/*Represents an instanceof expression.
                                    Java_SpecifierSet attribute: represents the right operand
                                    Child(0): The left operand. An expression node.
                                    */
                    expPtr  = new ExpressionPtr (
                       new Java_ScopedName(n.getSpecSet().get_type_name()), new Object[] {});
                    expPtr.addOperand(process(n.jjtGetChild(0), vms));
                    rule = new CGRInstanceof(symtab);
                    rule.apply(expPtr);
                }
                break;

                case JJTUNARY_PREFIX:{/*Represents a prefix operation.
                                        int attribute: The token kind of the operator.
                                        String attribute: The image of the operator.
                                        Child(0): The operand. An expression node.
                                       **/
                    expPtr  = new ExpressionPtr (
                       new Java_ScopedName(n.getString()), new Object[] {});
                    expPtr.addOperand(process(n.jjtGetChild(0), vms));
                    eBuilder.apply(expPtr);
                }
                break;

                case JJTUNARY_POSTFIX:{ /* Represents a postfix operation.
                                            int attribute: The token kind of the operator.
                                            String attribute: The image of the operator.
                                            Child(0): The operand. An expression node.
                                            */
                    String image = n.getString();
                    expPtr  = new ExpressionPtr (
                       new Java_ScopedName(image), new Object[] {});
                    expPtr.addOperand(process(n.jjtGetChild(0), vms));
                    rule = eBuilder.rb.get(image.equalsIgnoreCase("++") ?
                        JavaExpressionBuilder.R_POSTFIX_INCREMENT :
                        JavaExpressionBuilder.R_POSTFIX_DECREMENT );
                    rule.apply(expPtr);
                 }
                break;

                case JJTCASTEXPRESSION:{ /* Represents a cast expression.
                                            Java_SpecifierSet attribute: represents the type (the left operand)
                                            Child(0): The right operand. An expression node.
                                            */
                    expPtr  = new ExpressionPtr (
                       n.getName(), new Object[] {});
                    expPtr.opcat = new Java_ScopedName("cast");
                    expPtr.addOperand( new NoExpNode( n.getDecl().getType() ) ) ;
                    expPtr.addOperand(process(n.jjtGetChild(0), vms));
                    rule = eBuilder.rb.get(JavaExpressionBuilder.R_CASTING_CONVERT);
                    rule.apply(expPtr);
                }
                break;

                case JJTFIELDACCESS:{ /* Represents a field access expression of the
                                         form "Primary . Identifier". Note that ExpressionNames
                                         are not Primary expressions, so for example "a.b" does not
                                         used JJTFEILDACCESS, but rather JJTEXPRESSSIONNAME.
                                          String attribute: The field identifier.
                                          Child(0): The (left) operand. An expression node.
                                        */
                    expPtr  = new ExpressionPtr (new Java_ScopedName(n.getString()),
                        new Object [] {});
                    expPtr.addOperand(process(n.jjtGetChild(0), vms));
                    rule = new CGRIdExp (symtab);
                    rule.apply (expPtr);
                }
                break;
                case JJTEXPRESSIONNAME:{ /* Represents an expression of the form "ExpressionName" (See JLS chapter 6).
                                            ScopedName attribute. The name. (not absolute, length>=1)
                                            */
                    expPtr  = new ExpressionPtr (n.getName(), new Object [] {});
                    rule = new CGRIdExp (symtab);
                    rule.apply (expPtr);
                }
                break;

                case JJTPARENS:{ /* Represents a parenthesized expression.
                                    Child(0) The operand, an expression node.
                                    */
                    expPtr  = new ExpressionPtr (process(n.jjtGetChild(0), vms));
                    rule = new CGRParentheses("(",  ")");
                    rule.apply(expPtr);
                }
                break;

                case JJTTHISEXP: { /* Represents expressions of the form "this" or "ClassName.this".
                                    ScopedName attribute: The ClassName or null. (not absolute, length >= 1)
                                    ExpThis*/
                	Assert.error(!staticContext,  
                			"Can't use \"this\" in a static context.");
                    expPtr  = new ExpressionPtr (n.getName(), new Object[] {});
                    rule = new CGRThisExp(symtab);
                    rule.apply(expPtr);
               }
                break;
                case JJTSUPEREXP:{ /* Represents expressions of the form "super.Identifier" or "ClassName.super.Identifier".
                                    ScopedName attribute: The ClassName or null. (not absolute, length >= 1)
                                    String attribute: The identifier
                                    both forms ExpThisMember trick is getting right path */
                	Assert.error(!staticContext,  
        			"Can't use \"super\" in a static context.");
                    expPtr  = new ExpressionPtr (
                       new Java_ScopedName(n.getString()), new Object[] {});
                    expPtr.opcat = n.getName(); 
                    rule = new CGRSuperExp(symtab);
                    rule.apply(expPtr);
               }
                break;

                case JJTCLASSEXP:{ /* Represents an expression of the form "ResultType.class".
                                    Java_SpecifierSet attribute: represents the type.
                                    * this is for reflection so phase 2
                                    */
                    punt();
                }
                break;

                case JJTSUPERMETHODCALL:{ /* Represents a function call expression of the form: "super.Id(...)" or "ClassName.super.Id(...)".
                                            ScopedName attribute: The ClassName or null. (not absolute, length >= 1)
                                            String attribute: The identifier
                                            Child(0): The arguments. A JJTARGUMENTS node.
                                            15.2.1 recipient is still this
                                            */
                	Assert.error(!staticContext,  
        			"Can't use \"super\" in a static context.");
                    expPtr  = new ExpressionPtr (new Java_ScopedName(n.getString()),
                                            new Object [] {});
                    // Build the argument list
                    expPtr.addOperand(processArguments((SimpleNode)n.jjtGetChild(0), vms));
                    expPtr.opcat = n.getName();  // may be null
                    rule = new CGRMethodCall (symtab, true);
                    rule.apply (expPtr);
                }
                break;

                case JJTMETHODNAMECALL:{ /* Represents a method call expression of the form: MethodName(...).
                                            ScopedName attribute: The MethodName. (not absolute, length >= 1)
                                            Child(0): The arguments. A JJTARGUMENTS node.
                                            */
                    expPtr  = new ExpressionPtr (n.getName(), new Object [] {});
                    // Build the argument list
                    expPtr.addOperand(processArguments((SimpleNode)n.jjtGetChild(0), vms));
                    rule = new CGRMethodCall (symtab);
                    rule.apply (expPtr);
                }
                break;

                case JJTMETHODCALL:{ /* Represents a method call expression of the form "Primary . Identifier ( ... )"
						                (But not explicit constructor invocations.)
                                        String attribute: The Identifier.
                                        Child(0): The left operand. An expression node.
                                        Child(1): The arguments. A JJTARGUMENTS node.
                                        */
                    expPtr  = new ExpressionPtr (new Java_ScopedName(n.getString()), new Object [] {});
                    // Build the argument list
                    expPtr.addOperand(processArguments((SimpleNode)n.jjtGetChild(1), vms));
                    // Process the recipient
                    expPtr.addOperand(process(n.jjtGetChild(0), vms));
                    // Fetch the recipient if needed
                    rule = new CGRFetch (1);
                    rule.apply (expPtr);
                    // Build the method call
                    rule = new CGRMethodCall (symtab);
                    rule.apply (expPtr);
                }
                break;


                case JJTSUBSCRIPT:{ /* Represents a subscript expression a[b].
                                        Child(0): The left operand. An expression node.
                                        Child(1): The right operand. An expression node.
                                        */
                    expPtr  = new ExpressionPtr (new Java_ScopedName(n.getString()),
                    		new Object [] {});
                    expPtr.addOperand(process(n.jjtGetChild(0), vms));
                    expPtr.addOperand(process(n.jjtGetChild(1), vms));
                    rule = new CGRSubscript();
                    rule.apply(expPtr);
                }
                break;

                case JJTALLOCATIONEXPRESSION:{ /* Represents a "new" expression. These come in four forms
                                                    new PrimitiveType ArrayDimsAndInit
                                                    new Name ArrayDimsAndInits
                                                    new Name ( ... )
                                                    new Name ( ... ) ClassBody

                                                    Java_SpecifierSet: The base type (including the Name for nonprimitive types)
                                                    bool attribute: indicates that arguments are present, i.e the third or fourth form.
                                                    If the bool attribute is true:
                                                    Child(0): The arguments. A JJTARGUMENTS node.
                                                    Child(1) (optional) : A JJTCLASSBODY representing the body of an anonymous class.
                                                    Otherwise:
                                                    Child(0): The array dimensions and initializer. A JJTARRAYDIMSANDINITS node.
                                                **/
                   	SimpleNode child = (SimpleNode)n.jjtGetChild(0);
                   	rule = null;
	                if (n.getBoolean()) { // constructor
	                    expPtr  = new ExpressionPtr (n.getSpecSet().get_type_name(), new Object [] {});
                        // Build the argument list
                        expPtr.addOperand(processArguments(child, vms),0);
                        if( n.jjtGetNumChildren() > 1 )
                        	punt() ;
                        else
                        	rule = new CGRNew( symtab ) ;
 	                } else { // array
 	                	TypeNode type = null;
	                    Java_SpecifierSet specSet = (Java_SpecifierSet)n.getSpecSet();
	                    ScopedName namedType = specSet.get_type_name();
	                    if (namedType == null) { // must be primitive type
	                        String id = specSet.getPrimitiveName();
	                        type = specSet.getPrimitiveType();
	                        Assert.check((id != null), "No primitive equivalent to un-named variable type!");
	                        expPtr = new ExpressionPtr (new Java_ScopedName(id), new Object [] {});
	                    } else {
	                        DeclarationSet targets = symtab.getCurrentScope().lookUp(namedType, new LFlags(Java_LFlags.TYPE));
	                        Assert.error(targets != null,
	                                     "Can't find declaration for " + namedType.getName() + " in CTSymbolTable!",
	                                     n.getCoords());
	                        Declaration target = targets.getSingleMember();
	                        Assert.error(target != null,
	                                "Ambiguous reference for " + namedType.getName() + " in " + symtab.getCurrentScope(),
	                                n.getCoords());
	                        type = new TyPointer();
	                        type.addToEnd(target.getType());  //expPtr NOT DEFINED!!!!
	                        expPtr = new ExpressionPtr(target.getName(), new Object [] {});
	                    }
	                    
                       /* The child must be a node of type JJTARRAYDIMSANDINITS 
	                 
	                      This represents the array dimensions and initialization
			              part of an allocation ("new") expression. These come
			              in two basic forms: Those without initializers may
			              have explicit dimensions. E.g. [i][j][k][][]

			              Those with initializers must not have explicit
			              dimensions. E.g.   [][] ArrayInitializer

			              int attribute: The number of "empty" bracket pairs.
			                             for the above examples it would be 2.
			              bool attribute: Indicates the presence of an array initializer.
			              If bool attribute then
			                 Child(0): An array initializer, a JJTARRAYINITIALIZER.
			              Else
			                 Children: The explicit dimensioning expressions.
			                      Expression nodes.
	                    */
	                    Vector initTree = null;
	                    int dimensions = 0;
                		if (child.getBoolean()) { // initializers
                			initTree = buildArrayInitTree((SimpleNode)child.jjtGetChild(0), vms, type);
                	    	Debug debug = Debug.getInstance();
                	    	if (debug.isOn()) debug.msg(Debug.COMPILE, "Initialization tree: " +
                	    			arrayInitTreeString(initTree));
                			dimensions = child.getInt();
                		}
	                	else { // No initializer. There may be explicit dimensions.
	                		dimensions = child.jjtGetNumChildren(); // explicit dimensions
	                		for (int i = 0; i < dimensions; i++)  // for each explicit dimension
	                			expPtr.addOperand(ensureRValue(child.jjtGetChild(i), vms));
	                		dimensions += child.getInt();
	                	}
                    	rule = new CGRNewArray(type,
                                               symtab.getTypeNodeForClass(Java_ScopedName.JAVA_LANG_OBJECT),
                                               initTree,
                                               dimensions,
                                               eBuilder);
	                }
                    rule.apply (expPtr);
                }
                break;
                
               
                case JJTVARIABLEINITIALIZER: {
/*                    expPtr  = new ExpressionPtr (((SimpleNode)n.jjtGetParent()).getName(), new Object [] {});
                    rule = new CGRIdExp (symtab, true);
                    rule.apply (expPtr);    // ExpId now in expression
                    ExpressionPtr assign = new ExpressionPtr (new Java_ScopedName("="), new Object [] {});
                    assign.set(expPtr.get(),0);
                    assign.set(process(n.jjtGetChild(0), vms),1);
                    eBuilder.apply(assign);*/
                }
                break;
                

                case JJTQUALIFIEDALLOCATIONEXPRESSION:{ /* Represents a "new" expression of the form Primary "." "new" Identifier (... ) ClassBody_opt
                                                            String attribute: The identifier
                                                            Child(0): The primary expression. An expression node.
                                                            Child(1): The argument. A JJTARGUMENTS node.
                                                            Child(2) (optional) : A JJTCLASSBODY representing the body of an anonymous class.
                                                            */
                    punt();
                }
                break;


            }
            return expPtr == null ? null : expPtr.get();
        } catch( TMException e ) {
            if( e.getSourceCoords() == SourceCoords.UNKNOWN ) {
                SourceCoords coords = n.getCoords() ;
                e.setSourceCoords( coords ) ; }
            throw e ; }
        catch( RuntimeException e ) {
             SourceCoords coords = n.getCoords( ) ;
             Debug.getInstance().msg(Debug.COMPILE, "Exception thrown while processing line "+coords);
             throw e ;
        }

    }
    
    /** Convenience method for the initialization of local variables
     * 
     * @param variable the parser node corresponding to the variable being initialized
     * @param expression the parser node corresponding to the initialization expression
     * @param vms the virtual machine state
     * @return the assignment expressionNode for the initialization
     */

    ExpressionNode initialize(SimpleNode variable, SimpleNode expression, VMState vms) {
        ExpressionPtr expPtr  = new ExpressionPtr (variable.getName(), new Object [] {});
        CodeGenRule rule = new CGRIdExp (symtab, true);
        rule.apply (expPtr);    // ExpId now in expression
        ExpressionPtr assign = new ExpressionPtr (new Java_ScopedName("="), new Object [] {});
        assign.set(expPtr.get(),0);
        assign.set(process(expression, vms),1);
        eBuilder.apply(assign);
        return assign.get();
    }

/** Convenience method for the initialization of local array variables
 * 
 * @param variable the parser node corresponding to the array variable being initialized
 * @param expression the parser node corresponding to the initialization expression
 * @param vms the virtual machine state
 * @param elementType the basic element type of the array being initialized
 * @return the assignment expressionNode for the initialization
 */
    ExpressionNode initArray(SimpleNode variable, SimpleNode expression, VMState vms, TypeNode elementType) {
    // Effectively, building an assignment. ExpId for the variable is the left side
        ExpressionPtr expPtr  = new ExpressionPtr (variable.getName(), new Object [] {});
        CodeGenRule rule = new CGRIdExp (symtab, true);
        rule.apply (expPtr);    // ExpId now in expression
        ExpressionPtr assign = new ExpressionPtr (new Java_ScopedName("="), new Object [] {});
        assign.set(expPtr.get(),0);
        
        // The right hand side is the initialization which is a CGRNewArray
		Vector initTree = buildArrayInitTree(expression, vms, elementType);
    	Debug debug = Debug.getInstance();
    	if (debug.isOn()) debug.msg(Debug.COMPILE, "Initialization tree: " +
    			arrayInitTreeString(initTree));
		int dimensions = variable.getInt() + variable.getSpecSet().getArrayDimCount();
    	rule = new CGRNewArray(elementType,
                               symtab.getTypeNodeForClass(Java_ScopedName.JAVA_LANG_OBJECT),
                               initTree,
                               dimensions,
                               eBuilder);
        ExpressionPtr initializer = new ExpressionPtr (new Java_ScopedName("fakingIt"), new Object [] {});  	
        rule.apply (initializer);
        assign.set(initializer.get(), 1);
        
        eBuilder.apply(assign);
        return assign.get();
    }
    
    
    private Vector buildArrayInitTree(SimpleNode n, VMState vms, TypeNode elementType){
    	Vector tree = new Vector();
    	for (int i = 0; i < n.jjtGetNumChildren(); i++){
    		SimpleNode child = (SimpleNode)n.jjtGetChild(i);
    		if (child.id == JJTARRAYINITIALIZER)
    			tree.addElement(buildArrayInitTree(child, vms, elementType));
    		else
    			tree.addElement(process(child, vms));
    	}
    	return tree;  	
    }
    
    private String arrayInitTreeString(Vector tree){
    	String string = "{";
    	for (int i = 0; i < tree.size(); i++){
    		Object node = tree.elementAt(i);
     		string += node instanceof Vector ?
    			 arrayInitTreeString((Vector) node) :
    			 	node.toString();
    		string += i == tree.size() - 1 ? "}" : ", ";
     	}
    	return string;
    }
    
    /**
     * Convenience method to create a return expression
     * @param expression the parser node corresponding to the return expression
     * @param returnType the return type for the method
     * @param vms the virtual machine state
     * @return the expression node for the assignment of the return expression to a
     * new <code>ExpResult</code> 
     */

    ExpressionNode returnExpression(Node expression, TypeNode returnType, VMState vms) {
        TyRef tyRef = new TyRef( returnType ) ;
        ExpressionNode loperand = new ExpResult( tyRef ) ;
        ExpressionNode roperand = process( expression, vms ) ;
        ExpressionPtr assign = new ExpressionPtr (new Java_ScopedName("="),
                                                  new Object [] {loperand, roperand});
        eBuilder.apply(assign);
        return assign.get();
    }

    /**
     * Convenience method to create the implicit initialization expression needed
     * to initialize a parameter to the calling argument
     * @param parameter the parser node corresponding to the method argument parameter
     * @param i the position of the parameter in the parameter list
     * @return the expression node for the assignment of the argument in the call
     * to the method parameter 
     */
    ExpressionNode processParameter(SimpleNode parameter, int i) {
        ExpressionPtr expPtr  = new ExpressionPtr (parameter.getName(), new Object [] {});
        CodeGenRule rule = new CGRIdExp (symtab);
        rule.apply (expPtr);    // ExpId now in expression
        ExpressionNode loperand = expPtr.get();
        ExpressionNode roperand = new ExpArgument(parameter.getDecl().getType(), i);
        ExpressionNode initialize = new OpAssign (loperand.get_type (),
                                        "=", loperand, roperand);
        initialize.setUninteresting(true);
        return initialize;
    }

    private NodeList processArguments(SimpleNode argList, VMState vms){
        NodeList arguments = new NodeList();
        for (int i = 0; i < argList.jjtGetNumChildren(); i++) {
            ExpressionPtr exp = new ExpressionPtr(process(argList.jjtGetChild(i),vms));
            CodeGenRule rule = eBuilder.rb.get(JavaExpressionBuilder.R_FETCH);
            rule.apply(exp);
            arguments.addLastChild(exp.get());
        }

        return arguments;
    }

    ExpressionNode ensureBoolean( Node n, VMState vms ) {
        ExpressionNode e = ensureRValue( n, vms ) ;
        Assert.error( e.get_type() == TyBoolean.get(),
                     "Expression must be boolean" ) ;
        return e ; }

    ExpressionNode ensureSwitchable( Node n, VMState vms ) {
        ExpressionNode e = ensureRValue( n, vms ) ;
        TypeNode type = e.get_type() ;
        Assert.error( type == TyChar.get()
                      || type == TyByte.get()
                      || type == TyShort.get()
                      || type == TyInt.get(),
                     "Expression must be of type char, byte, short, or int" ) ;
        return e ; }

    public ExpressionNode ensurePointerToThrowable(Node n, VMState vms) {
        ExpressionNode e = ensureRValue( n, vms ) ;
        TypeNode type = e.get_type() ;
        Assert.error( checkForThrowable( type ),
                      "expression in throw must be a reference type" ) ;
        return e ;
    }

    public boolean checkForThrowable(TypeNode type) {
        if( ! (type instanceof TyPointer) ) return false ;
        TypeNode pointeeType = ((TyPointer)type).getPointeeType() ;
        if( ! (pointeeType instanceof TyClass) ) return false ;
        /** @todo check that pointeeType is throwable */
        return true ;
    }

    private ExpressionNode ensureRValue(Node n, VMState vms ) {
        ExpressionNode e = process( n, vms ) ;
        ExpressionPtr expPtr = new ExpressionPtr( e ) ;
        CodeGenRule rule = new CGRFetch() ;
        rule.apply( expPtr );
        return expPtr.get() ; }

    public void checkAssignableCaseExpr(long value, TypeNode type) {
        boolean ok ;
        if( type == TyChar.get() )
            ok = 0 <= value && value < 0xFFFF ;
        else if( type == TyByte.get() )
            ok = -128 <= value && value < 128 ;
        else if( type == TyShort.get() )
            ok = -0x8000 <= value && value < 0x8000 ;
        else
            ok = true ;
        Assert.error( ok, "Value of case label is not assignable to the type of the switch expression" ) ;
    }

    private ExpressionNode punt(){
        Assert.apology("Expression not yet implemented");
        return null;
    }

    /**
     * @param theClass
     * @return
     */
    public ExpressionNode implicitSuperConstructorCall( ScopedName classFQN ) {
        return superConstructorCall( classFQN, null, new NodeList()) ;
    }
    
    /** Create a super constructor call
     * 
     * @param theClass The class that is being constructed
     * @param outerObject The outer object, if any, otherwise null.
     * @param arguments Any constructor arguments
     * @param vms The virtual machine state
     * @return
     */
    private ExpressionNode superConstructorCall(
            ScopedName classFQN,
            ExpressionNode outerObject,
            NodeList arguments ) { 
        
        if( outerObject != null ) punt() ;
        
        ExpressionPtr expPtr  = new ExpressionPtr (classFQN,
                new Object[] { arguments, outerObject });
        CodeGenRule rule = new CGRExplicitConstructorCall( true, symtab );
        rule.apply(expPtr);
        return expPtr.get() ;
    }   
    
    private ExpressionNode thisConstructorCall(
            ScopedName classFQN,
            NodeList arguments ) { 
        
        ExpressionPtr expPtr  = new ExpressionPtr (classFQN,
                new Object[] { arguments });
        CodeGenRule rule = new CGRExplicitConstructorCall( false, symtab );
        rule.apply(expPtr);
        return expPtr.get() ;
    }   
}
