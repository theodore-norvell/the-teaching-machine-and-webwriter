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

import java.util.Vector;

import tm.clc.analysis.*;
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TyAbstractClassDeclared;
import tm.clc.ast.TypeNode;
import tm.interfaces.SourceCoords;
import tm.javaLang.ast.TyClass;
import tm.javaLang.parser.JavaParserConstants;
import tm.utilities.Assert;
import tm.utilities.Debug;

/*******************************************************************************
Class: Java_CTSymbolTable

Overview:
This class represents the compile time symbol table for Java.
*******************************************************************************/


public class Java_CTSymbolTable extends CTSymbolTable
                                implements JavaParserConstants {

    private SHProgram programScope;
    private LFlags lookupContext = new LFlags();
    
    /** Create a compile time symbol table for the current program */    
    public Java_CTSymbolTable() { 
        programScope = SHProgram.getProgramSH();
        currentScope = programScope;
    }
    
    /** In some languages signifies the entering of a scope without a declaration (e.g.
     * a block scope). All scopes in our implementation of Java have a declaration so
     * it is an error to call this method.
     */    
    public void enterScope() {
        Assert.check("enterScope() should not be called without a declaration argument");
    }
    
    /** Revert to an pre-existing scope. Subsidiary files have to be compiled
     * at the top scope level (program scope), but their compilation may be
     * invoked in the middle of compiling another file, requiring a reversion
     * to the original scope after the secondary compilation is complete.
     */    
    public void enterScope(ScopeHolder entry) {
        currentScope = entry;
    }
    
    public void createBlockScope(Declaration decl) {
       currentScope = ((SHCommon)currentScope).createBlockScope(decl);
       debugReport();
    }
    
    public void createFunctionScope(Declaration decl){
       currentScope = ((SHCommon)currentScope).createFunctionScope(decl);
       debugReport();
    }

    
    public void createPackageScope(ScopedName name, SpecifierSet specSet, SourceCoords coords) {
    	// Package scope may already exist - cut to chase and jump straight to program
    	Declaration existingPackage =
    		programScope.lookupPackage(name, new LFlags(Java_LFlags.PACKAGE)).getSingleMember();
    	currentScope = (existingPackage != null) ?
    			existingPackage.getScopeHolder() :
    			((SHCommon)currentScope).createPackageScope(name, specSet, coords);
    				
    		debugReport();
    }
    
    public void addImportDeclaration(Declaration decl, boolean onDemand) {
        ((SHCompilationUnit) currentScope).addImportDeclaration(decl, onDemand);
    }
    
    public void createCompilationUnitScope(Declaration decl) {
    	// compilationUnit scope may already exist
    	Declaration existingCU =
    		currentScope.lookUp(decl.getName(), new LFlags(Java_LFlags.COMPILATION_UNIT)).getSingleMember();
        currentScope = (existingCU != null) ?
        		existingCU.getScopeHolder() :
        		((SHCommon)currentScope).createCompilationUnitScope(decl);
        debugReport();
    }
    
    public void createTypeScope(Declaration decl) {
        currentScope = ((SHCommon)currentScope).createTypeScope(decl);
//        currentScope.addOwnDeclaration(decl);
        debugReport();
    }
    
    public void createLocalScope(Declaration decl){
        currentScope = ((SHCommon)currentScope).createLocalScope(decl);
        debugReport();
    }
    
    public void exitScope() {
        while (currentScope instanceof SHLocal)  // Peel off any sHLocal's
            currentScope = (SHCommon)currentScope.getEnclosingScope();
        currentScope = (SHCommon)currentScope.getEnclosingScope();
    }
    
    public void exitAllScopes() { currentScope = programScope; }
    
/*  Create a new fully qualified scopedName by appending the given
 *  simpleName to the fully qualified scope name of the base scope
 *  of the current scope.
 * 
 */    
    public Java_ScopedName createFQName(ScopedName simpleName) {
 	   SHCommon baseScope = ((SHCommon)getCurrentScope()).getBaseScope();
 	   Java_ScopedName name = (Java_ScopedName)
 		   (baseScope.getOwnDeclaration().getName()).clone();
 	   name.append(simpleName);
        return name;
     }


    /** Build the <em>relative path</em> for this <code>Declaration</code>.
	 * from the indicated scope. The path is an array of integers whose
	 * length indicates how many classes away the declaration is.
	 * <br><strong>note:</strong> The integers represent through which related
         * class the path runs, as follows:
         * (1) a non-negative integer gives the number of the superclass in the list
         *      of the class's superclasses
         * (2) a -1 indicates the next outer class
         * Thus a path of [-1,0,2] would mean the declaration occurred in the
         * 2nd supertype of the 0'th supertype of the immediate outer type
         * of the type in which the starting scope occurs
	 * @param match a declaration known to be in the class hierarchy 
	 * @param scope the starting scope from which the path is to begin. If omitted,
         *           the default is the currentScope
         * @return the path or a null if the declaration's scope is not in the type hierarchy
	 */
    protected int[] getRelativePath(Declaration match){
        return getRelativePath(match, (SHCommon)currentScope);
    }
    
    protected int[] getRelativePath(Declaration match, SHCommon scope){
        Vector pathV = new Vector();
        int[] path = null;
        scope.buildRelativePath (match, pathV);
        path = new int[pathV.size()];
        for (int i = 0; i < pathV.size(); i++)
                path[i] = ((Integer)pathV.elementAt(i)).intValue();
        return path;
    }

    public DeclarationSet lookup (ScopedName name, LFlags flags) {
        DeclarationSet matches = null;

        try {
            matches = currentScope.lookup(name, flags);
        } catch (LookupException e) {
            System.err.println("Nested exception trace is") ;
            e.printStackTrace( System.err ) ;
            Assert.check ("Internal error: LookupException in Java" );
        }
        return matches;
    }
    
/* Special lookup to find a member field, given a TyAbstractClass TypeNode.
 * Assertion. The TyAbstractClass TypeNode has already gone through valid lookups
 **/
    
    public Declaration lookupMemberField(TyAbstractClass type, ScopedName name){
        SHType scope = (SHType)((TyAbstractClassDeclared)type).getDeclaration().getDefinition();
        Declaration found = scope.lookUp(name, Java_LFlags.FIELD_VARIABLE_LF).getSingleMember();
        Assert.error(found!=null, "Can't find " + name.getName());
        Assert.error(isAccessible(found),  "Can't access " + name.getName());
        return found;
    }
    
/* Special lookup to find a member field, given a TyAbstractClass TypeNode.
 * Assertion. The TyAbstractClass TypeNode has already gone through valid lookups
 * multiple methods may be found so no single or accessability checks are done
 **/
    
    public DeclarationSet lookupMemberMethods(TyAbstractClassDeclared type, ScopedName name){
        return lookupMemberMethods((SHType)type.getDeclaration().getDefinition(),name);
    }

    public DeclarationSet lookupMemberMethods(SHType typeSH, ScopedName name){
        return typeSH.lookUp(name, Java_LFlags.METHOD_LF);
    }

    public boolean isAccessible(Declaration decl){
        return ((SHCommon)currentScope).isAccessible(decl);
       }
    
    public String toString(){return "Java CT Symbol Table";
    }
    
    /** Look in symbol table for a class
     * @param className the name of the class to look for
     * @return The TyClass for the class.
     */
    public TyClass getTypeNodeForClass(
            ScopedName className) {
            
        // Get TyClass object for the String and Object classes.
            LFlags classFlags = new LFlags( LFlags.CLASS ) ;
            DeclarationSet declSet = lookup( className, classFlags ) ;
            Declaration decl = declSet.getSingleMember() ;
            Assert.check( decl != null, "Could not find "+className.getName() ) ;
            TypeNode type = decl.getType() ;
            Assert.check( type instanceof TyClass ) ;
            return (TyClass) type ;
    }

    
    private void debugReport() { 
        Debug.getInstance().msg(Debug.COMPILE, "Created " + currentScope.toString() + " in scope of "
        + (currentScope.getClassDeclaration() == null ? "no " :
            currentScope.getClassDeclaration().toString()) + " class");
    }

    public void dumpContents(String context, Debug d){
        d.msg(Debug.COMPILE, "****" + toString() + ' ' + context + " ***********************");
        d.msg(Debug.COMPILE, toString());
        programScope.dumpContents("", d);
        d.msg(Debug.COMPILE, "----" + toString() + ' ' + context + " -----------------------");
    }
   
    public void dumpContents(Debug d){
    	dumpContents(" ", d);
    }
    
}

