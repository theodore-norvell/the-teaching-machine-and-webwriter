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

import java.util.*;

import tm.clc.analysis.Declaration;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.FunctionDeclaration;
import tm.clc.analysis.LFlags;
import tm.clc.analysis.ScopeHolder;
import tm.clc.analysis.ScopedName;
import tm.clc.analysis.SpecifierSet;
import tm.clc.ast.TypeNode;
import tm.interfaces.SourceCoords;
import tm.javaLang.analysis.Java_CTSymbolTable;
import tm.javaLang.analysis.Java_LFlags;
import tm.javaLang.analysis.Java_ScopedName;
import tm.javaLang.analysis.Java_SpecifierSet;
import tm.javaLang.analysis.Java_Specifiers;
import tm.javaLang.analysis.SHCommon;
import tm.javaLang.analysis.SHPackage;
import tm.javaLang.analysis.SHProgram;
import tm.javaLang.analysis.SHType;
import tm.javaLang.ast.TyClass;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.utilities.TMException;

/**
 * Overview:
 * This class is the second pass in the compiling process. It walks over the parse
 * tree and populates the {@link tm.javaLang.analysis.Java_CTSymbolTable
 * compile-time symbol table} with {@link tm.javaLang.analysis.SHCommon scope holders}
 * It creates {@ link tm.clc.analysis.Declaration Declaration} objects and linkes them
 * into the parse tree.
 */

public class SecondPass implements JavaParserTreeConstants{

    private static int anonymousClassCount;
    private static int blockCount;
    private static Debug d = Debug.getInstance();
    private static Hashtable crossRef;

    /** Reset the state of the second pass. Since second pass is static it
     * may need to be reset, for instance when a file is reloaded
     */
    static public void reset(){
    	blockCount = 0;
    	anonymousClassCount = 0 ;
    	crossRef = new Hashtable(100);
    	// empty out the program scopeholder
        SHProgram.reset();
    }

    /** Creates declarations for (most of) the nodes in the subTree and uses them to 
     * populate the {@link tm.javaLang.analysis.Java_CTSymbolTable compile-time symbol table}
     *   
     * @param n_prime the root node of the subTree over which the secondPass is to be run
     * @param symtab the Compile Time symbol table
     */
    static public void doSecondPass(Node n_prime, Java_CTSymbolTable symtab) {
        SimpleNode n = (SimpleNode) n_prime;
        Declaration decl = null;
        TypeNode type;
        {
            d.msg(Debug.COMPILE, "Line #" + n.getCoords().getLineNumber()+ ": 2nd pass in  " +            	
				symtab.getCurrentScope().toString() + ": n.id = " + jjtNodeName[n.id] +
                "    n.name = " + ((n.getDecl()==null) ? "" :
                	n.getDecl().getName().toString()));
        }
        


        try {
            switch(n.id) {
            /*
              1. Deals with package info
              2. Deals with import declarations
              3. Deals with type declarations
            */
            case JJTCOMPILATIONUNIT:
                doSecondPass(n.jjtGetChild(0), symtab); // optional package declaration
                Java_ScopedName sn = new Java_ScopedName( new String[] {n.getString()} ) ;
                decl = new Declaration(Java_LFlags.COMPILATION_UNIT_LF, sn, null, n.getSpecSet(),null );
                symtab.createCompilationUnitScope(decl);
                doSecondPass(n.jjtGetChild(1), symtab); // optional imports (moved by mpbl)
                for(int i = 2; i < (n.jjtGetNumChildren()); i++) {
                    doSecondPass(n.jjtGetChild(i), symtab);
                }
                symtab.exitAllScopes();
            break;

            /*
              creates the package scope (leaves the exiting
              for the compilation unit to do). Declaration not created here
              as it may already exist if package already in symtab.
             */
            case JJTOPTPACKAGEDECLARATION:
                if (n.getBoolean())
                    symtab.createPackageScope(n.getName(),n.getSpecSet(), n.getCoords());
                else  //default package.
                    symtab.createPackageScope(SHProgram.getDefaultPackageName(), null, n.getCoords());

                // retrieve the declaration for n
                decl = symtab.getCurrentScope().getOwnDeclaration();
            break;

            /*
              Adds one import declaration to the current compilation unit scope
            */
            case JJTIMPORTDECLARATION:
               decl = new Declaration(Java_LFlags.IMPORT_LF, n.getName(),null, n.getSpecSet(),null);
               symtab.addImportDeclaration(decl, n.getBoolean());
            break;

            /*
              1. Creates declaration
              2. Tells node where declaration is
              3. Recurses on class body within a scope
             mpbl IGNORES Extends & Implements because we don't have scopeholders for superclasses
            */
            case JJTUNMODIFIEDCLASSDECLARATION: {

                LFlags flags = getAccessFlags(n.getSpecSet());
                flags.set(Java_LFlags.CLASS_LF);
                decl = new Declaration(flags, symtab.createFQName(n.getName()), null, n.getSpecSet(), null );
                decl.setType(createClassType(n, symtab, decl));
                symtab.createTypeScope(decl);
                SimpleNode classBody = (SimpleNode) n.jjtGetChild(2) ;
                doSecondPass(classBody, symtab);
                if( ! classBody.getBoolean() ) {
                    SimpleNode constructor = 
                        declareDefaultConstructor(decl, n.getSpecSet(), symtab, n.getName(), n.getCoords()) ;
                    classBody.jjtAddChild(constructor, classBody.jjtGetNumChildren() ) ;
                    constructor.jjtSetParent( classBody ) ;
                }
                symtab.exitScope();
            }
            break;

            /*
              1. Creates declaration
              2. Tells node where declaration is
              3. Recurses on interface body within a scope
            */
            case JJTUNMODIFIEDINTERFACEDECLARATION: {
                LFlags flags = getAccessFlags(n.getSpecSet());
                flags.set(Java_LFlags.INTERFACE_LF);
                decl = new Declaration(flags, symtab.createFQName( n.getName()),null, n.getSpecSet(),null);
                decl.setType(createClassType(n, symtab, decl));
                symtab.createTypeScope(decl);
                ((SHCommon)symtab.getCurrentScope()).setStaticContext();    // interfaces are implicitly static
                doSecondPass(n.jjtGetChild(1), symtab);
                symtab.exitScope();
            }
            break;

            /*
              1. Creates declaration
              2. Tells node where declaration is
              3. Tells scopeholder about the declaration
              4. Recurses on constructor block within a scope
            */
            case JJTCONSTRUCTORDECLARATION:
            case JJTMETHODDECLARATION: {
                // Start on construction of declaration
                SimpleNode header = (SimpleNode) n.jjtGetChild(0);
                LFlags flags = getAccessFlags(header.getSpecSet());
                flags.set(n.id == JJTCONSTRUCTORDECLARATION ? Java_LFlags.CONSTRUCTOR_LF : Java_LFlags.METHOD_LF);
                decl = new FunctionDeclaration(
                    flags, symtab.createFQName( header.getName()),
					null, header.getSpecSet(),null);
                symtab.createFunctionScope(decl);
                processChildren(n, symtab);
                symtab.exitScope();
            }
            break;


            /*
              1. Creates a declaration
              2. Tells the node where declaration is
              3. Adds declaration
            */
            case JJTFORMALPARAMETER:
                decl = new Declaration(Java_LFlags.LOCAL_VARIABLE_LF, n.getName(),null, n.getSpecSet(),null);
                symtab.addDeclaration(decl);
            break;


            case JJTVARIABLEDECLARATOR: {
                if (symtab.getCurrentScope() instanceof SHType){
                    LFlags flags = getAccessFlags(n.getSpecSet());
                    flags.set(Java_LFlags.FIELD_VARIABLE);
                    decl = new Declaration(flags, symtab.createFQName( n.getName()),
                    		null, n.getSpecSet(),null);
                    symtab.addDeclaration(decl);    // Fields appear directly in classes
                }
                else {
              /*JLS 14.4.2 Scope of Local Variable Declarations
               * The scope of a local variable declaration in a block (§14.4.2) is the
               * rest of the block in which the declaration appears, starting with its
               * own initializer (§14.4) and including any further declarators to the
               * right in the local variable declaration statement.
               * 
               * The name of a local variable v may not be redeclared as a local variable
               * of the directly enclosing method, constructor or initializer block
               * within the scope of v, or a compile-time error occurs. The name of a
               * local variable v may not be redeclared as an exception parameter of a
               * catch clause in a try statement of the directly enclosing method,
               * constructor or initializer block within the scope of v, or a compile-
               * time error occurs.
               * However, a local variable of a method or initializer block may be
               * shadowed (§6.3.1) anywhere inside a class declaration nested within the
               * scope of the local variable. 
               * 
               */
                	DeclarationSet possibles = symtab.lookup(n.getName(),Java_LFlags.LOCAL_VARIABLE_LF);
                    Enumeration iterator = possibles.elements();
                    ScopeHolder thisClass = symtab.getCurrentScope().getClassDeclaration().getScopeHolder();
                    while (iterator.hasMoreElements()) {
                    	Declaration possible = (Declaration)iterator.nextElement();
                    	if (possible.getCategory().equals(Java_LFlags.LOCAL_VARIABLE_LF) &&
                    			possible.getClassScope()== thisClass)
                        	Assert.error(
                        			"Duplicate local variable " + n.getName());
                    }
                // The decl is actually for the local scope surrounding the variable
                   decl = new Declaration(Java_LFlags.EMPTY_LF, n.getName(),null, n.getSpecSet(),null);
                    //new Declaration(Java_LFlags.LOCAL_VARIABLE_LF, n.getName(),null, n.getSpecSet(),null);
                    symtab.createLocalScope(decl);   // local variable creates its own scope
                }
                processChildren(n, symtab);
            }
            break;
            
            case JJTVARIABLEINITIALIZER:
            case JJTARRAYINITIALIZER: { // Create a block scope for field initialization
            	SimpleNode parent = (SimpleNode)n.jjtGetParent();
                SimpleNode grandparent = (SimpleNode)parent.jjtGetParent() ;
            	if (grandparent.id == JJTCLASSBODY || grandparent.id == JJTINTERFACEBODY ) {
	            	decl = new Declaration(Java_LFlags.EMPTY_LF,
	            			new Java_ScopedName("_anonymousBlock_" + blockCount++),
	            			null, parent.getSpecSet(),null);
	            	symtab.createBlockScope(decl);
	            	processChildren(n, symtab);
	            	symtab.exitScope();
            	}
            }
            break;
            
            case JJTINITIALIZERBLOCK:
            case JJTCONSTRUCTORDECLARATOR:
            case JJTOPTTHROWSCLAUSE:
            case JJTMETHODDECLARATOR:
            case JJTIMPORTDECLARATIONS:
            case JJTCLASSBODY:
            case JJTINTERFACEBODY:
            case JJTCONSTRUCTORBODY:
            case JJTMETHODBODY:
            case JJTARRAYDIMSANDINITS:
            case JJTFORMALPARAMETERS:
            case JJTTHISEXPLICITCONSTRUCTORINVOCATION:
            case JJTSUPEREXPLICITCONSTRUCTORINVOCATION:
            case JJTARGUMENTS:
                processChildren(n, symtab);
            break;

            case JJTOPTINTERFACEEXTENDSCLAUSE:
            case JJTOPTIMPLEMENTSCLAUSE:
            case JJTOPTCLASSEXTENDSCLAUSE:
                /* if(n.getBoolean()) {
                    ((ClassSH)symtab.getCurrentScope()).addSuperclass(sc)//Shit we don't know enclosing scope for superclass
                 * actually, we should add superclasses to class type node but we don't know if they've been created yet.
                }*/
                Assert.check("This node should not have been processed");
            break;

            case JJTQUALIFIEDALLOCATIONEXPRESSION: {
                // First child is the enclosing object
                doSecondPass(n.jjtGetChild(0), symtab);
                // Second child is the arguments
                doSecondPass(n.jjtGetChild(1), symtab);
                //Third child is the class body
                //(0) Make up a name
                n.setName( new Java_ScopedName("Class$"+anonymousClassCount) );
                //(1) Make a TypeNd and a declaration
                decl = new Declaration(Java_LFlags.CLASS_LF, n.getName(), null, new Java_SpecifierSet(), null );
                decl.setType(createClassType(n, symtab, decl));
                //(2) Add the declaration to the symbol table and traverse the classbody
                symtab.createTypeScope(decl);
                doSecondPass(n.jjtGetChild(2), symtab);
                symtab.exitScope();
            }
            break ;

            case JJTALLOCATIONEXPRESSION: {
                // First child could be ArrayDimsAndInits
                // or it could be the constructor Arguments
                // In either case we visit the first child.
                doSecondPass(n.jjtGetChild(0), symtab);
                if( n.jjtGetNumChildren() > 1 ) {
                    Assert.check( n.getBoolean() );
                    // Anonymous class. Child 1 is body.
                    // Use a number for the name (TBD check sanity of this)
                    n.setName(new Java_ScopedName("Class$"+anonymousClassCount));

                    decl = new Declaration(Java_LFlags.CLASS_LF,n.getName(),null, n.getSpecSet(), null );
                    decl.setType(createClassType(n, symtab, decl));
                    symtab.createTypeScope(decl);
                    doSecondPass(n.jjtGetChild(1), symtab);
                    symtab.exitScope();
                }
            }
            break ;

            case JJTLABELEDSTATEMENT:
                processChildren(n, symtab);
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
            case JJTFORSTATEMENT: {
            	SimpleNode parent = (SimpleNode)n.jjtGetParent();
            	Java_SpecifierSet specSet = null;
            	if (parent.id == JJTINITIALIZERBLOCK && parent.getBoolean()){
            		specSet = new Java_SpecifierSet();
            		specSet.add(Java_Specifiers.SP_STATIC);
            	}
                decl = new Declaration(Java_LFlags.EMPTY_LF,
                		new Java_ScopedName("_anonymousBlock_" + blockCount++),
						null, specSet, null );
                symtab.createBlockScope(decl);
                processChildren(n, symtab);
                symtab.exitScope();
            }
            break;

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
            case JJTCASTEXPRESSION:
            case JJTPARENS:
            case JJTSUBSCRIPT:
            case JJTSUPERMETHODCALL:
            case JJTMETHODNAMECALL:
            case JJTMETHODCALL:
            case JJTFIELDACCESS:

                processChildren(n, symtab);
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
  // For all nodes, if a declaration was created, add a crossref so a node can
  // be recovered, given its declaration
        if (decl != null) {
            n.setDecl(decl);
            crossRef.put(decl,n);
        }
    }


    /** Create a simple node representing a default constructor
     * Also creates the FunctionDeclaration (attached to the node) and adds
     * a function scope to the symbol table.
     * 
     * @author Theo
     *
     * @param classDecl The declaration of the class containing the constructor
     * @param classSpecSet The specifier set associated with that class
     * @param symtab The compile time symbol table
     * @param name The unqualified name of the class.
     */
     static SimpleNode declareDefaultConstructor(
            Declaration classDecl,
            Java_SpecifierSet classSpecSet,
            Java_CTSymbolTable symtab,
            ScopedName name,
            SourceCoords coords ) {
        // Create a specifier set
        Java_SpecifierSet specSet = new Java_SpecifierSet() ;
        if( classSpecSet.contains( Java_Specifiers.SP_PUBLIC )) {
            specSet.add(Java_Specifiers.SP_PUBLIC  ) ; }
        else if( classSpecSet.contains( Java_Specifiers.SP_PROTECTED )) {
            specSet.add(Java_Specifiers.SP_PROTECTED  ) ; }
        else if( classSpecSet.contains( Java_Specifiers.SP_PRIVATE )) {
            specSet.add(Java_Specifiers.SP_PRIVATE  ) ; }  
        
        // Create the flags
        LFlags flags = getAccessFlags( specSet ) ;
        flags.set(Java_LFlags.CONSTRUCTOR_LF );
        
        // Create the declaration
        Declaration decl = new FunctionDeclaration(
            flags, symtab.createFQName( name ),
            null, specSet,null);
        symtab.createFunctionScope(decl);
        
        symtab.exitScope();
        
        SimpleNode constructor = new SimpleNode( JJTCONSTRUCTORDECLARATION) ;
        constructor.setBoolean( true ) ;
        constructor.setDecl( decl ) ;
        constructor.setCoords( coords ) ;
        
        return constructor ;
    }

    private static void processChildren(SimpleNode n, Java_CTSymbolTable symtab) {
        for(int i = 0; i < n.jjtGetNumChildren(); i++) {
            doSecondPass(n.jjtGetChild(i), symtab);
        }
    }
    
    public static Hashtable getCrossRef(){
    	return crossRef;
    }
    

    private static LFlags getAccessFlags(SpecifierSet specSet){
//        LFlags flags = new LFlags(Java_LFlags.FIELD_VARIABLE);
        LFlags flags = new LFlags();
        if(specSet.contains(Java_Specifiers.SP_PRIVATE))
            flags.set(Java_LFlags.PRIVATE_ACCESS);
        else if (specSet.contains(Java_Specifiers.SP_PROTECTED))
            flags.set(Java_LFlags.PROTECTED_ACCESS);
        else if (specSet.contains(Java_Specifiers.SP_PUBLIC))
            flags.set(Java_LFlags.PUBLIC_ACCESS);
        else flags.set(Java_LFlags.PACKAGE_ACCESS);
// Static is an independent access issue
        if(specSet.contains(Java_Specifiers.SP_STATIC))
            flags.set(Java_LFlags.STATIC);
        else
            flags.set(Java_LFlags.NOT_STATIC);
        return flags;
    }


/* Creates a type node for classes
 **/
    private static TyClass createClassType(SimpleNode n, Java_CTSymbolTable symtab, Declaration decl){
        ScopeHolder scope = symtab.getCurrentScope();
        ScopeHolder encl = scope.getEnclosingScope();
        ScopedName fullQN = new Java_ScopedName(n.getName());
        while (scope != null) { // Build fully qualified name for class
            if (scope instanceof SHType || scope instanceof SHPackage)
                fullQN.insert(scope.getOwnDeclaration().getName());
            scope = scope.getEnclosingScope();
        }
        Debug.getInstance().msg(Debug.COMPILE, "Created TyClass node for " + fullQN.getName());
        TyClass tyClass = new TyClass(n.getName(),fullQN, decl);
        // TODO 
 /*       while (encl != null){ // add modifier chain for inner classes
            if (encl instanceof SHType)
                tyClass.addToEnd(encl.getOwnDeclaration().getType());
            encl = encl.getEnclosingScope();
        } */
        return tyClass;
    }
    

}