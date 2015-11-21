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

import tm.clc.analysis.Declaration;
import tm.clc.analysis.ScopeHolder;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.AbstractFunctionDefn;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.FunctionDefnBuiltIn;
import tm.clc.ast.FunctionDefnCompiled;
import tm.clc.ast.StatDo;
import tm.clc.ast.StatementNodeLink;
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TypeNode;
import tm.clc.ast.StatReturn;
import tm.clc.datum.AbstractDatum;
import tm.clc.rtSymTab.RT_Symbol_Table;
import tm.interfaces.SourceCoords;
import tm.javaLang.BuiltInStepperFactory;
import tm.javaLang.analysis.Java_CTSymbolTable;
import tm.javaLang.analysis.Java_SpecifierSet;
import tm.javaLang.analysis.Java_Specifiers;
import tm.javaLang.ast.TyFun;
import tm.javaLang.ast.TyJavaArray;
import tm.javaLang.ast.TyVoid;
import tm.javaLang.ast.TyClass;
import tm.javaLang.ast.TyMetaClass;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.utilities.TMException;
import tm.virtualMachine.VMState;


/**
 * This class is the fourth pass of the compiler. It walks over the parse
 * tree generating the AST (which should really be called the Abstract Code Graph) code
 * for expressions and statements.
 * Most of this work is actually delgated to 
 * {@link tm.javaLang.parser.StatementWalker StatementWalker}s and
 * {@link tm.javaLang.parser.ExpressionWalker ExpressionWalker}s
 * to walk parse tree statements and expressions, producing
 * the Abstract Syntax Tree (which should really be called the Abstract Code Graph).
 */

public class FourthPass implements JavaParserTreeConstants{
    private static Debug debug = Debug.getInstance();

    /** Reset the state of the fourth pass. Since fourth pass is static it
     * may need to be reset, for instance when a file is reloaded
     */
   static public void reset(){
    	
    }


    /** Recursively analyzes the parseTree, populating the
     * {@link tm.javaLang.analysis.Java_CTSymbolTable compile-time symbol table} and
     * managing the generation of the Abstract Code Graph (historically, the AST).
     * The actual code graph generation is largely delegated to StatementWalker
     * and CodeWalker objects.
     * @param n_prime the root node of the subTree over which the fourthPass is to be run
     * @param ctSymtab the Compile Time symbol table
     * @param vms the Virtual Machine State
     */
    static public void doFourthPass(Node n_prime, Java_CTSymbolTable ctSymtab, VMState vms) {
        SimpleNode n = (SimpleNode) n_prime;
        Declaration decl = n.getDecl();
        TypeNode type = null;
    	RT_Symbol_Table rtSymtab = (RT_Symbol_Table)vms.getSymbolTable();
        {
            Declaration own = ctSymtab.getCurrentScope().getOwnDeclaration();
            debug.msg(Debug.COMPILE,"Line #" + n.getCoords().getLineNumber()+ ": 4th pass in  " + (own == null ? "" : own.getName().toString() + " ") + ctSymtab.getCurrentScope().toString() +
                ": n.id = " + jjtNodeName[n.id] +
                "    n.name = " + ((n.getDecl()==null) ? "" : n.getDecl().getName().toString()));
        }

        try {
            switch(n.id) {
            /*
              1. Deals with package info
              2. Deals with import declarations
              3. Deals with type declarations
            */
            case JJTCOMPILATIONUNIT:
                doFourthPass(n.jjtGetChild(0), ctSymtab, vms); // optional package declaration
                ctSymtab.enterScope(decl);
                for(int i = 2; i < (n.jjtGetNumChildren()); i++) {
                    doFourthPass(n.jjtGetChild(i), ctSymtab, vms);
                }
                ctSymtab.exitAllScopes();
            break;

            /*
              enters the package scope (leaves the exiting
              for the compilation unit to do).
             */
            case JJTOPTPACKAGEDECLARATION:
                decl = n.getDecl();
                ctSymtab.enterScope(decl);
            break;

            /*
              1. Enters the type scope
              3. Recurses on class body within a scope
            */
            case JJTUNMODIFIEDINTERFACEDECLARATION:
            case JJTUNMODIFIEDCLASSDECLARATION: {
                decl = n.getDecl();
                ScopedName name = decl.getName();
                ctSymtab.enterScope(decl);
                String staticInitializationChainName = name.getName()+ "#Static" ;
                String dynamicInitializationChainName = name.getName() + "#Dynamic" ;
                rtSymtab.newInitializationChain(staticInitializationChainName) ;
                rtSymtab.newInitializationChain(dynamicInitializationChainName) ;
                TyClass theClass = (TyClass)decl.getType() ;
                theClass.setStaticInitializationChainName( staticInitializationChainName ) ;
                theClass.setDynamicInitializationChainName( dynamicInitializationChainName ) ;
                TyMetaClass classNode = theClass.getMetaClass();
                // The following statement (creating and storing the metaDatum)
                // used to be executed only for classes with static fields.
                // However, since the metadatum also contains the flag for whether
                // the class was initialized, it needs to be created (and stored) in all cases
                AbstractDatum metaDatum = classNode.makeDatum(
                        vms, vms.getStore().getStatic(), name.getName()) ;
                metaDatum.defaultInitialize() ;
                rtSymtab.addStaticGlobal(name, name.getName(), metaDatum );
                int body = (n.id == JJTUNMODIFIEDCLASSDECLARATION) ? 2 : 1;
                doFourthPass(n.jjtGetChild(body), ctSymtab, vms);  // Go straight to class body
                rtSymtab.addInitializationStatement(staticInitializationChainName,
                		new StatReturn(SourceCoords.UNKNOWN, 0), null);
                rtSymtab.addInitializationStatement(dynamicInitializationChainName,
                		new StatReturn(SourceCoords.UNKNOWN, 0), null);
                ctSymtab.exitScope();
            }
            break;

            case JJTCLASSBODY:
            case JJTINTERFACEBODY: {
            	processChildren(n, ctSymtab, vms);
            }
            break;


            case JJTVARIABLEDECLARATOR: {  // Member variable declaration only
                if (n.jjtGetNumChildren() > 0) {  // initializer
                    type = decl.getType();
                    SimpleNode variableInitializer = (SimpleNode) n.jjtGetChild(0) ;
                    decl = variableInitializer.getDecl();
                    ctSymtab.enterScope(decl);
                    ExpressionWalker eWalker = new ExpressionWalker(ctSymtab);
                    SimpleNode expression = (SimpleNode) variableInitializer.jjtGetChild(0);
                    ExpressionNode expr = expression.id == JJTARRAYINITIALIZER ?
                        	eWalker.initArray(n, expression, vms, ((TyJavaArray)type.getBaseType()).getBaseElementType() ) :
                        		eWalker.initialize(n, expression, vms) ;
                    StatDo statDo = new StatDo( n.getCoords(), 0, expr );
                    String name =
                        decl.getClassScope().getClassDeclaration().getName().getName();
                    rtSymtab.addInitializationStatement(name +
                            (n.getSpecSet().contains(Java_Specifiers.SP_STATIC) ?
                                    "#Static" : "#Dynamic"),
                                    statDo, statDo.next());
                    ctSymtab.exitScope(); }
            }
            break ;
            
            case JJTINITIALIZERBLOCK: {
            	StatementWalker sw = new StatementWalker(ctSymtab);
            	SimpleNode initBlock = (SimpleNode)n.jjtGetChild(0);
            	StatementNodeLink top = new StatementNodeLink();
            	StatementNodeLink bottom = sw.process(initBlock,top,vms);
                if( bottom == top ) {
                    // no code was generated, so we don't add it.
                }
                else {
                    String name =
                        initBlock.getDecl().getClassScope().getClassDeclaration().getName().getName();
                    rtSymtab.addInitializationStatement(name +
                            (n.getBoolean() ? "#Static" : "#Dynamic"),
                            top.get(), bottom);
                }
            }
            break;

            case JJTMETHODDECLARATION:
            case JJTCONSTRUCTORDECLARATION: {
                boolean isConstructor = n.id==JJTCONSTRUCTORDECLARATION ;
                boolean isCompilerGeneratedConstructor = isConstructor && n.getBoolean() ;
                Java_SpecifierSet specSet = (Java_SpecifierSet) decl.getSpecifiers();
                boolean isStatic = specSet.contains( Java_Specifiers.SP_STATIC ) ;
                boolean isAbstract = specSet.contains( Java_Specifiers.SP_ABSTRACT ) ;
                boolean isNative = specSet.contains( Java_Specifiers.SP_NATIVE ) ;
                
                // Abstract functions have no function definition object, so we bail out here.
                if( isAbstract ) {
                    break ; }
                
                // Create a function definition object
                AbstractFunctionDefn theFunction ;
                StatementNodeLink bodyLink ;
                {
                    Object runtimeId = decl.getRuntimeId() ;
                    if( isNative ) {
                        bodyLink = null ;
                        FunctionDefnBuiltIn theFunctionBI
                        = new FunctionDefnBuiltIn( runtimeId,
                                                   BuiltInStepperFactory.makeStepper( decl ) ) ;
                        theFunction = theFunctionBI; }
                    else {
                        FunctionDefnCompiled theFunctionUD
                        = new FunctionDefnCompiled( runtimeId );
                        bodyLink = theFunctionUD.getBodyLink() ;
                        theFunction = theFunctionUD ; } }
                
                ctSymtab.enterScope(decl);
                
                // Add the function definition object to either the runtime symbol table
                // or to the virtual function table for the class.
                if( isStatic || isConstructor ) {
                    rtSymtab.addFunctionDefinition( theFunction);
                    debug.msg(Debug.COMPILE, "Added function " + (String)theFunction.getKey()); }
                else {
                    Declaration classDecl = ctSymtab.getCurrentScope().getClassDeclaration() ;
                    TyAbstractClass tyClass = (TyAbstractClass) classDecl.getType() ;
                    tyClass.getVirtualFunctionTable().add( theFunction ) ;
                    debug.msg(Debug.COMPILE, "Added function " + (String)theFunction.getKey()
                            + " to the VFN for " + tyClass.toString()) ; }
                
                // Check for main function
                if (decl.getName().getTerminalId().equals("main")) {
                    // check for static, public, returns void, takes a single parameter
                    //  of type pointer to array of pointer to string)
                    boolean isMain =  specSet.contains(Java_Specifiers.SP_PUBLIC)
                                   && isStatic
                                   && TyVoid.get().equal_types(((TyFun)decl.getType()).returnType())
                           /* TODO && p.jjtGetNumChildren() == 1
                                   && param is of type pointer to array of pointer to string
                           */ ;
                    if( isMain ) {
                        TyClass theClass = (TyClass) ctSymtab.getCurrentScope().getClassDeclaration().getType() ;
                        rtSymtab.addMainFunction( theFunction, theClass) ; } }

                if( ! isNative ) {
                    // Pass it over to the StatementWalker
                    StatementWalker sw = new StatementWalker(ctSymtab, (TyFun)decl.getType());
                    sw.process(n, bodyLink, vms); }
                
                ctSymtab.exitScope();
            }
            break;

            default:
                Assert.check("Unexpected node "+jjtNodeName[n.id]);
            } }
        catch( TMException e ) {
            if( e.getSourceCoords() == SourceCoords.UNKNOWN ) {
                SourceCoords coords = n.getCoords() ;
                e.setSourceCoords( coords ) ; }
            throw e ; }
        catch( RuntimeException e ) {
             SourceCoords coords = n.getCoords( ) ;
             Debug.getInstance().msg(Debug.COMPILE, "Exception thrown while processing line "+coords);
             throw e ; }
    }

    private static void processChildren(SimpleNode n, Java_CTSymbolTable ctSymtab, VMState vms) {
        for(int i = 0; i < n.jjtGetNumChildren(); i++) {
            doFourthPass(n.jjtGetChild(i), ctSymtab, vms);
        }
    }


}


