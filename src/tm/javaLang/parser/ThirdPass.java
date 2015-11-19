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

import tm.clc.analysis.Declaration;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.FunctionDeclaration;
import tm.clc.analysis.LFlags;
import tm.clc.analysis.ScopeHolder;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TypeNode;
import tm.clc.ast.VarNode;
import tm.interfaces.SourceCoords;
import tm.javaLang.analysis.Java_CTSymbolTable;
import tm.javaLang.analysis.Java_LFlags;
import tm.javaLang.analysis.Java_ScopedName;
import tm.javaLang.analysis.Java_SpecifierSet;
import tm.javaLang.analysis.Java_Specifiers;
import tm.javaLang.analysis.SHLocal;
import tm.javaLang.analysis.SHType;
import tm.javaLang.ast.TyClass;
import tm.javaLang.ast.TyFun;
import tm.javaLang.ast.TyJavaArray;
import tm.javaLang.ast.TyPointer;
import tm.javaLang.ast.TyVoid;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.utilities.TMException;



/**
 * Overview:
 * This class is the third pass in the compiling process. It is a pass over the
 * {@link tm.javaLang.analysis.Java_CTSymbolTable compile-time symbol table}
 * that produces all the {@link tm.clc.ast.TypeNode type nodes} for the user
 * declared types (i.e. classes and interfaces)
 */

public class ThirdPass implements JavaParserTreeConstants{

    static private Debug d = Debug.getInstance();
    
    /** Reset the state of the third pass. Since third pass is static it
     * may need to be reset, for instance when a file is reloaded
     */
    static public void reset(){
    	
    }



    /** Produce all the type nodes for the user declared types.
     * @param n_prime the root node of the subTree over which the thirdPass is to be run
     * @param symtab the Compile Time symbol table
     */
    static public void doThirdPass(Node n_prime, Java_CTSymbolTable symtab) {
        SimpleNode n = (SimpleNode) n_prime;
        Declaration decl;
        TypeNode type = null;
        {
            Declaration own = symtab.getCurrentScope().getOwnDeclaration();
            d.msg(Debug.COMPILE,"Line #" + n.getCoords().getLineNumber()+ ": 3rd pass in  " + 
            	(own == null ? "" : own.getName().toString() + " ") +
				symtab.getCurrentScope().toString() + ": n.id = " + jjtNodeName[n.id] +
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
                doThirdPass(n.jjtGetChild(0), symtab); // optional package declaration
                decl = n.getDecl();
                symtab.enterScope(decl);
                doThirdPass(n.jjtGetChild(1), symtab); // optional imports
                for(int i = 2; i < (n.jjtGetNumChildren()); i++) {
                    doThirdPass(n.jjtGetChild(i), symtab);
                }
                symtab.exitAllScopes();
            break;

            /*
              enters the package scope (leaves the exiting
              for the compilation unit to do).
             */
            case JJTOPTPACKAGEDECLARATION:
                decl = n.getDecl();
                symtab.enterScope(decl);
            break;

            /*
              Nothing to do on third pass
            */
            case JJTIMPORTDECLARATION:
            break;

            /*
              1. Enters the type scope
              2. Calls child nodes 0 & 1 to resolve extends and implements
              3. Recurses on class body within a scope
            */
            case JJTUNMODIFIEDINTERFACEDECLARATION:
            case JJTUNMODIFIEDCLASSDECLARATION:
                if(!n.getBoolean()) {   // If this node hasn't been processed
                    n.setBoolean(true);     // Processing this class
                    decl = n.getDecl();
                    symtab.enterScope(decl);
                    processChildren(n, symtab); // Now we resolve extends and implements
                    TyClass tyClass = (TyClass)decl.getType();
                    tyClass.setDefined();
                    symtab.exitScope();
                }
            break;

            case JJTFORMALPARAMETERS: {
                processChildren(n, symtab);
                FunctionDeclaration myFunction = (FunctionDeclaration)
                    ((SimpleNode)((SimpleNode)n.jjtGetParent()).jjtGetParent()).getDecl();
                for (int i = 0; i < n.jjtGetNumChildren(); i++) {
                    Declaration parameter = ((SimpleNode)n.jjtGetChild(i)).getDecl();
                    myFunction.addParameter(parameter.getType(), false);
                }
            }
            break;


            /*
              1. retrieves declaration from symtab
              2. sets the declaration's type
              3. Processes children
            */
            case JJTFORMALPARAMETER:
            case JJTVARIABLEDECLARATOR: {
                decl = n.getDecl();
                Java_SpecifierSet specSet = (Java_SpecifierSet)n.getSpecSet();
                ScopedName namedType = specSet.get_type_name();
                ScopeHolder myScope = symtab.getCurrentScope();
                if (namedType == null) { // must be primitive type
                    type = specSet.getPrimitiveType();
                    Assert.check((type != null), "No primitive equivalent to un-named variable type!");
                } else {
                    DeclarationSet targets = myScope.lookUp(namedType, new LFlags(Java_LFlags.TYPE));
                    Assert.error(targets != null,
                                 "Can't find declaration for " + namedType.getName() + " in CTSymbolTable!",
                                 n.getCoords());
                    Declaration target = targets.getSingleMember();
                    Assert.error(target != null,
                            "Ambiguous reference for " + namedType.getName() + " in " + symtab.getCurrentScope(),
                            n.getCoords());
                    type = new TyPointer();
                    type.addToEnd(target.getType());
                }
                // Generate extra nodes for arrays
                type = genArrayNodes(symtab, type, specSet.getArrayDimCount() + n.getInt());
                	
                decl.setType(type);
                if (n.id == JJTVARIABLEDECLARATOR)
                    if (myScope instanceof SHType){    // Effectively at type level
                    	TyClass myClass =
                    		(TyClass)myScope.getOwnDeclaration().getType();
                    	VarNode myNode =
                    		new VarNode(symtab.createFQName(n.getName()),type);
                    	if (decl.hasSpecifier(Java_Specifiers.SP_STATIC))
							myClass.addStaticField(myNode);
						else
							myClass.addField(myNode);
                    }
                    else { // Enter the scope of the local variable. Don't need to exit. Done automatically when
                        // block exited
                        symtab.enterScope(decl);
                        Declaration varDecl = ((SHLocal)symtab.getCurrentScope()).getTheVariable();
                        varDecl.setType(type);
                    }
                processChildren(n, symtab);
            }
            break;
            
            case JJTARRAYINITIALIZER:
            case JJTVARIABLEINITIALIZER: {
            	decl = n.getDecl();
            	if (decl != null) {
                    symtab.enterScope(decl);
                    processChildren(n, symtab);
                    symtab.exitScope();
            	}
            	else processChildren(n, symtab);
            }
            break ;

            case JJTMETHODDECLARATION: {
                decl = n.getDecl();
                symtab.enterScope(decl);

                // type := the return type
                Java_SpecifierSet specSet = (Java_SpecifierSet)decl.getSpecifiers();
                Assert.check(specSet != null, "No specifier set for method " + decl.getName().toString() + " declaration.");
                ScopedName namedType = specSet.get_type_name();
                if (namedType == null) { // must be primitive type
                    type = specSet.getPrimitiveType();
                    Assert.check((type != null), "No primitive equivalent to un-named return type!");
                } else {
                    Declaration target = symtab.lookup(namedType, new LFlags(Java_LFlags.TYPE)).getSingleMember();
                    Assert.check((target != null), "Can't find declaration for " + namedType.getName() + " in CTSymbolTable!");
                    // Non primitive types are returned by pointer
                    type = target.getType();
                    Assert.check( type != null ) ;
                    type = new TyPointer( type ) ;
                }
                type = genArrayNodes(symtab, type, specSet.getArrayDimCount() + ((SimpleNode)n.jjtGetChild(0)).getInt());

                processChildren(n, symtab);
                Vector params = getParameters(n) ;
                TyFun tyFun = makeTyFun(params,type);
                decl.setType(tyFun);
                
                String runtimeID ;
                if( specSet.contains( Java_Specifiers.SP_STATIC ) ) {
                    runtimeID = decl.getName().getName() ; }
                else {
                    runtimeID = decl.getName().getTerminalId() ; }
                runtimeID += "#" + tyFun.typeId() ;
                decl.setRuntimeId( runtimeID );
                
                symtab.exitScope();
            }
            break;

            case JJTCONSTRUCTORDECLARATION:
                decl = n.getDecl();
                symtab.enterScope(decl);
                
                processChildren(n, symtab);
                
                Vector params ;
                if( n.getBoolean() ) {
                    // Compiler Generated Constructor
                    // TODO Deal with Anonymous constructors.
                    params = new Vector() ; }
                else {
                    params = getParameters(n); }
                TyFun tyFun = makeTyFun(params,TyVoid.get());
                decl.setType(tyFun);
                decl.setRuntimeId(decl.getName().getName() + "%" + tyFun.typeId());
                
                symtab.exitScope();
            break;


            case JJTCONSTRUCTORDECLARATOR:
            case JJTOPTTHROWSCLAUSE:
            case JJTMETHODDECLARATOR:
            case JJTIMPORTDECLARATIONS:
            case JJTCLASSBODY:
            case JJTINTERFACEBODY:
            case JJTCONSTRUCTORBODY:
            case JJTMETHODBODY:
            case JJTARRAYDIMSANDINITS:
            case JJTTHISEXPLICITCONSTRUCTORINVOCATION:
            case JJTSUPEREXPLICITCONSTRUCTORINVOCATION:
            case JJTARGUMENTS:
                processChildren(n, symtab);
            break;

            case JJTOPTINTERFACEEXTENDSCLAUSE:
            case JJTOPTIMPLEMENTSCLAUSE: {
                if(n.getBoolean()) { // declaration set by parent node
                    decl = ((SimpleNode)n.jjtGetParent()).getDecl();
                    SHType myScope = (SHType)symtab.getCurrentScope();
                    for (int i = 0; i <  n.jjtGetNumChildren(); i++) {
                        ScopedName targetName = ( (SimpleNode)n.jjtGetChild(i) ).getName();
                        Declaration target = myScope.getEnclosingScope().lookUp(
                                targetName, new LFlags(Java_LFlags.TYPE)).getSingleMember();
                        Assert.error(target != null, "Unable to find superType " + targetName);
                        if (target.getScopeHolder()==myScope)
                            Assert.error("Type " + decl.getName().getName() + " can't inherit from itself",
                                         n.getCoords());
                        TyClass tyClass = (TyClass)decl.getType();
                        tyClass.addSuperClass((TyAbstractClass)target.getType());
                        SimpleNode tNode = (SimpleNode)SecondPass.getCrossRef().get(target);
                        if (!tNode.getBoolean()){
                            doThirdPass(tNode,symtab);
                            symtab.enterScope(decl); // restore scope
                        }
                        myScope.addSuperclass(target.getScopeHolder());
                    }
                }
            }
            break;

            case JJTOPTCLASSEXTENDSCLAUSE: {
                if(n.getBoolean()) { // We do have an extends clause
                    decl = ((SimpleNode)n.jjtGetParent()).getDecl();
                    SHType myScope = (SHType)symtab.getCurrentScope();
                    DeclarationSet target = myScope.getEnclosingScope().lookUp(n.getName(), Java_LFlags.CLASS_LF);
                    Assert.error(target != null && target.getSingleMember()!= null,
                        "Can't find " + n.getName().getName() + " super class declaration for " + decl.getName(),
                        n.getCoords() );
                    Declaration theTarget = target.getSingleMember();
                    if (target.getScopeHolder()==myScope)
                        Assert.error("Type " + decl.getName().getName() + " can't inherit from itself",
                                     n.getCoords());
                    TyClass tyClass = (TyClass)decl.getType();
                    Assert.check(tyClass!=null, "TyClass node not defined for " + decl.toString());
                    tyClass.addSuperClass((TyAbstractClass)theTarget.getType());
                    tyClass.setDirectSuperClass( (TyClass)theTarget.getType()) ;
                    SimpleNode tNode = (SimpleNode)SecondPass.getCrossRef().get(theTarget);
                    if (!tNode.getBoolean()){
                        doThirdPass(tNode,symtab);
                        symtab.enterScope(decl); // restore scope
                    }
                    myScope.addSuperclass(theTarget.getScopeHolder());  // At this point all superclasses should be defined
                }
                else {
                    // TODO. Except when the class is java.lang.Object itself,
                    // java.lang.Object should be supplied as the default.
                    // Presumably the code to handle the defaulting belongs not
                    // in this else clause, but rather above the if
                }
            }
            break;

            case JJTQUALIFIEDALLOCATIONEXPRESSION: {
                // First child is the enclosing object
                doThirdPass(n.jjtGetChild(0), symtab);
                // Second child is the arguments
                doThirdPass(n.jjtGetChild(1), symtab);
                //Third child is the class body
                // [TBD Shouldn't the super classes of the
                // anonymous class be added?
                // Be sure to fix JJTALLOCATIONEXPRESSION too.]
                decl = n.getDecl();
                symtab.enterScope(decl);
                doThirdPass(n.jjtGetChild(2), symtab);
                symtab.exitScope();
            }
            break ;

            case JJTALLOCATIONEXPRESSION: {
                // First child could be ArrayDimsAndInits
                // or it could be the constructor Arguments
                // In either case we visit the first child.
                doThirdPass(n.jjtGetChild(0), symtab);
                if( n.getBoolean())
                    if (n.jjtGetNumChildren()>1){
                        // Anonymous class. Child 1 is body.
                        // [TBD Shouldn't the super classes of the
                        // anonymous class be added?
                        // Be sure to fix JJTQUALIFIEDALLOCATIONEXPRESSION too.]
                        decl = n.getDecl();
                        symtab.enterScope(decl);
                        doThirdPass(n.jjtGetChild(1), symtab);
                        symtab.exitScope();
                    }
            }
            break ;

            case JJTLABELEDSTATEMENT:
                processChildren(n, symtab);
            break;


            case JJTCASTEXPRESSION: {
                Java_SpecifierSet specSet = (Java_SpecifierSet)n.getSpecSet();
                ScopedName namedType = specSet.get_type_name();
                ScopeHolder myScope = symtab.getCurrentScope();
                if (namedType == null) { // must be primitive type
                    type = specSet.getPrimitiveType();
                    Assert.check((type != null), "No primitive equivalent to un-named cast!");
                    namedType = new Java_ScopedName(type.getTypeString());
                } else {
                    Declaration target = myScope.lookUp(namedType, new LFlags(Java_LFlags.TYPE)).getSingleMember();
                    Assert.error(target != null,
                                 "Can't find declaration for " + namedType.getName() + " in CTSymbolTable!",
                                 n.getCoords());
                    type = new TyPointer( target.getType() ) ;
                }
                // Create a declaration to hold the type
                decl = new Declaration(Java_LFlags.EMPTY_LF,new Java_ScopedName(""));
                decl.setType(type);
                n.setDecl(decl);
                n.setName(namedType);
                processChildren(n, symtab);
            }
            break;


            case JJTTHISEXP:
            case JJTSUPEREXP:
            case JJTEXPRESSIONNAME:
            case JJTLITERAL:
            case JJTNAMENODE:
            case JJTCLASSEXP:
            break;

             /*
              1. Processes children within a scope
            */
            case JJTCATCH:
            case JJTBLOCK:
            case JJTFORSTATEMENT:
                decl = n.getDecl();
                symtab.enterScope(decl);
                processChildren(n, symtab);
                symtab.exitScope();
            break;


            case JJTINITIALIZERBLOCK:
            case JJTSWITCHSTATEMENT:
            case JJTIFSTATEMENT:
            case JJTWHILESTATEMENT:
            case JJTDOSTATEMENT:
            case JJTOPTFORINIT:
            case JJTFORINIT:
            case JJTRETURNSTATEMENT:
            case JJTTHROWSTATEMENT:
            case JJTSYNCHRONIZEDSTATEMENT:
            case JJTTRYSTATEMENT:
            case JJTCATCHES:
            case JJTEXPRESSIONSTATEMENT:
            case JJTASSERTSTATEMENT:
            case JJTCASE:
            case JJTSWITCHLABEL:
            case JJTOPTEXPRESSION:
            case JJTOPTFORUPDATE:
            case JJTBINOP:
            case JJTCONDITIONAL:
            case JJTINSTANCEOF:
            case JJTUNARY_PREFIX:
            case JJTUNARY_POSTFIX:
            case JJTPARENS:
            case JJTSUBSCRIPT:
            case JJTSUPERMETHODCALL:
            case JJTMETHODCALL: 
            case JJTFIELDACCESS:

                processChildren(n, symtab);
            break;
            
            case JJTMETHODNAMECALL:{
            	/* Represents a method call expression of the form: MethodName(...).
            	 * ScopedName attribute: The MethodName. (not absolute, length >= 1)
            	 * Child(0): The arguments. A JJTARGUMENTS node. 
            	 */
            		symtab.getCurrentScope().lookUp(n.getName(), Java_LFlags.METHOD_LF);
            		processChildren(n, symtab);
            	}
                break;
           

            case JJTEMPTYSTATEMENT:
            case JJTBREAKSTATEMENT:
            case JJTCONTINUESTATEMENT:
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

    /** Add extra nodes to transform a type into an pointer to an array type.
	 * @param symtab
	 * @param type
	 * @param dims the number of dimensions for the array. dims can be 0.
	 * @return A type node constructed as follows:
     * <PRE>
     *      TyPointer ---> TyJavaArray ---> type
     *      \_____________________________/
     *           repeated dims times
     * </PRE>
	 */
	private static TypeNode genArrayNodes(Java_CTSymbolTable symtab, TypeNode type, int dims) {
		for ( int i = 0; i < dims; i++) { // Array
			TypeNode tyArray = new TyJavaArray("", symtab.getTypeNodeForClass( Java_ScopedName.JAVA_LANG_OBJECT )) ;
			tyArray.addToEnd(type);
			type = new TyPointer();
			type.addToEnd(tyArray);
		}
		return type;
	}

	private static void processChildren(SimpleNode n, Java_CTSymbolTable symtab) {
        for(int i = 0; i < n.jjtGetNumChildren(); i++) {
            doThirdPass(n.jjtGetChild(i), symtab);
        }
    }

    private static TyFun makeTyFun(Vector params, TypeNode returnType){
        TyFun tyFun = new TyFun(params);
        tyFun.addToEnd(returnType);
        return tyFun;
    }



    /**
     * @param n
     * @return
     */
    private static Vector getParameters(SimpleNode n) {
        SimpleNode pNode = (SimpleNode)((SimpleNode)n.jjtGetChild(0)).jjtGetChild(0);
        Vector params = new Vector();
        for (int i = 0; i < pNode.jjtGetNumChildren(); i++){
            Declaration param = ((SimpleNode)pNode.jjtGetChild(i)).getDecl();
            params.addElement(param.getType());
        }
        return params;
    }
}


