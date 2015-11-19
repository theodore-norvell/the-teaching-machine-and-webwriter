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


import java.util.Enumeration;

import tm.clc.analysis.Declaration;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.DeclarationSetMulti;
import tm.clc.analysis.LFlags;
import tm.clc.analysis.ScopedName;
import tm.clc.analysis.SpecifierSet;
import tm.interfaces.SourceCoords;
import tm.javaLang.JavaFileManager;
import tm.utilities.Debug;
import tm.utilities.TMFile;


/**
* This class represents the program scopeHolder. It holds only package scopeHolders
*/

/**
 * @author Michael Bruce-Lockhart
 * Aug 17, 2004
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SHProgram extends SHCommon {
    
    private static SHProgram programScope = null;
    final private static ScopedName defaultPackageName = new Java_ScopedName("default");
    
    /** Create a program scopeHolder with no enclosing Scope */    
    private SHProgram() { 
        super(null);
        classDeclaration = null;    // Can't be inside a class
    }

    public SHCommon getBaseScope(){ // over-ride in baseScopes
        return this;
    }
    
   
    /** gets the singleton instance of the Program Scope Holder
     * @return the SHProgram
     */    
    public static SHProgram getProgramSH() {
        if (programScope == null) {
            programScope = new SHProgram();
        }
        
        return programScope;
    }
    
    /** reset the singleton instance of the Program Scope Holder
      */    
    public static void reset() {
        programScope = null;
    }
    
    public static ScopedName getDefaultPackageName(){
    	return defaultPackageName;
    }




    
    /** creates a new package scope for the named package if one doesn't already exist.
     * @param name The name of the package
     * @param specSet the specification set for the package
     * @return the scope holder for the package
     */    
    public SHCommon createPackageScope(ScopedName name, SpecifierSet specSet, SourceCoords coords) {
        d.msg(Debug.COMPILE, "createPackageScope on "+name + " in " + toString() + " requested.");
        if (name.is_qualified()){ // Make sure parent packages have been added
            Java_ScopedName interimName = (Java_ScopedName)name.clone();
            interimName.removeTerminalId();
            createPackageScope(interimName,specSet, coords);
        }
        Enumeration packages = elements();
        while(packages.hasMoreElements()) { 
        	DeclarationSet decSet = (DeclarationSet)(packages.nextElement());
        	for (int d = 0; d < decSet.size(); d++){
	            Declaration decl = decSet.declaration(d);
	            ScopedName nametemp = decl.getName();
	  //          String stringtemp = nametemp.getName();
	            if (nametemp.equals(name)) // Already in the list
	                return (SHCommon)decl.getScopeHolder();
            }
        }
/*
 * The error so far. When I add the package StaticVariables.EmptyPackage.SubSubPackage
 * it finds StaticVariables has already been added, but StaticVariables.EmptyPackage has
 * not. So it creates a new package scope and a declaration for it and adds it to the
 * Program scope. Only trouble is, next time around it can't find it so it adds it again.
 * Also, hashTable for Program scope only has two entries. One is StaticVariables. The
 * other has the key EmptyPackage and a growing declarationSet every time we add
 * EmptyPackage again. After 3 tries we have the first two declarations with the name
 * StaticVariables.EmptyPackage.SubSubPackage and the last with just StaticVariables.EmptyPackage
 * ???????????
 */
        // Not in the list. Create new scopeHolder and add it to the list
        d.msg(Debug.COMPILE, "Package " + name + " not in the list. Creating.");
        SHPackage packageTemp = new SHPackage(this);    // The scopeHolder for this package
        Declaration decl = new Declaration(new LFlags(Java_LFlags.PACKAGE_LF),name, packageTemp,specSet,null);
        packageTemp.addOwnDeclaration(decl);
        addDeclaration(decl);
        return packageTemp;
    }
    
    /** Performs id resolution for type names - looks for the type in the default package
     * @param name the id to resolve
     * @param flags an <code>LFlags</code> value constraining 
     * @return a <code>DeclarationSet</code> value with matching 
     * <code>Declarations</code>.
     */
    protected DeclarationSet lookupType(String name, LFlags flags) {
        d.msg(Debug.COMPILE, "simple lookupType("+name+", "+flags.toString()+") in " + toString());
         // lookup in the default scopehilderSDeclarationSet possible = localLookup(name, flags);
         return lookupTypeInPackage(defaultPackageName, name);      
     }
     
    /** Performs id resolution for type names - for name Q.Id looks for
     * type Q in package Id
     * @param name the id to resolve
     * @param flags an <code>LFlags</code> value constraining 
     * @return a <code>DeclarationSet</code> value with matching 
     * <code>Declarations</code>.
     */
    protected DeclarationSet lookupType(ScopedName name, LFlags flags) {
        d.msg(Debug.COMPILE, "qualified lookupType("+name+", "+flags.toString()+") in " + toString());
        Java_ScopedName pkg = (Java_ScopedName)name.clone();
        String type = pkg.getTerminalId();
        pkg.removeTerminalId();
        return lookupTypeInPackage(pkg, type);      
     }
     
  /** Performs id resolution for package names
     * @param name the id to resolve
     * @param flags an <code>LFlags</code> value constraining 
     * @return a <code>DeclarationSet</code> value with matching 
     * <code>Declarations</code>.
     */
    
     public DeclarationSet lookupPackage(String name, LFlags flags) {
        return lookupPackage(new Java_ScopedName(name), flags);  
    }

     public DeclarationSet lookupPackage(ScopedName name, LFlags flags) {
        d.msg(Debug.COMPILE, "qualified lookupPackage("+name +", "+flags.toString()+") in " + toString());
        DeclarationSet possibles = localLookup(name.getTerminalId(), flags);
        if (!possibles.isEmpty()) {// Package declarations with same terminal id
        	Enumeration iterator = possibles.elements();
        	while (iterator.hasMoreElements()) {
        		Declaration possible = (Declaration) iterator.nextElement();
        		if (possible.getName().equals(name)) return possible;
        	}
        }
        // No luck. Maybe package has not been compiled yet
        return new DeclarationSetMulti();   // Empty set
    }
     
     /**
      * @param name the canonical name of a type in a package as referred
      * 		 to by an import statement in a compilation unit
      * 
      * @param type The id of the type being looked for in the package 
      */
     // Note. This could be refactored back into lookupType for SHProgram
     
     private DeclarationSet lookupTypeInPackage(ScopedName pkg, String type) {
        d.msg(Debug.COMPILE, "looking up " + type + " in package "+pkg.getName());
     	LFlags flags = new LFlags(Java_LFlags.TYPE);
     	Declaration packageDec = lookupPackage (pkg, new LFlags(Java_LFlags.PACKAGE)).getSingleMember();
     	if (packageDec != null) { 
     		return ((SHPackage)packageDec.getScopeHolder()).lookupType(type, flags );
	    }
     	// Package may exist but may not have been compiled yet
     	JavaFileManager theManager = JavaFileManager.getFileManager();
     	TMFile tmFile = theManager.getFileFromPackage(type, pkg);
     	if (tmFile != null) { // Found the file
     		// Do the first two passes, linking declarations into symbol table
     		// This should pick up package declaration as well
     		Declaration newDecl = theManager.doFirstTwoPasses(tmFile).getDecl();
     		// return the type declaration from the compilationUnit
     		return ( (SHCompilationUnit)(newDecl.getScopeHolder())).localLookup(type, flags);
     	}     	
     	return new DeclarationSetMulti();    	    	
     }

    
    
    /** Standard descriptor
     * @return "program scope"
     */    
    public String toString(){return "scope of program " + super.toString();}
    
}