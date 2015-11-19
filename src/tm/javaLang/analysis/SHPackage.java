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
import tm.clc.analysis.ScopeHolder;
import tm.javaLang.JavaFileManager;
import tm.javaLang.parser.SimpleNode;
import tm.utilities.Assert;
import tm.utilities.Debug;
import tm.utilities.TMFile;


/**
* This class represents a package scopeholder.
*/

/**
 * @author Michael Bruce-Lockhart
 * Aug 11, 2004
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SHPackage extends SHCommon {
 
   
    /** Create a new package scopeHolder
     * @param encl The scopeHolder for the enclosing program
     */    
    public SHPackage(ScopeHolder encl) {
        super(encl);
        classDeclaration = null;
    }

    public SHCommon getBaseScope(){ // over-ride in baseScopes
        return this;
    }
    

    
    /** <p>Creates a new compilationUnitScope
     *     and adds it to the list of enclosed compilationUnitScopes.</p>
     * <p>Assertion: scope is not already in the list of compilationUnitScopes.</p>
     * @return new compilationUnit scope
     * @param decl the declaration that created the Compilation unit
     */    
    public SHCommon createCompilationUnitScope(Declaration decl) {
        return createBlock(decl, new SHCompilationUnit(this));
    }
    
    /**
     * Performs id resolution given a name.
     * assertion: the id represents the name of a compilationUnit
     * @param name the name to resolve
     * @return a <code>DeclarationSet</code> value with matching 
     * <code>Declarations</code>.
     */
    
    public DeclarationSet lookupCU(String name, LFlags flags){
    	return localLookup(name,flags);
    }
 
    /**
     * Performs id resolution given a name.
     * assertion: the id represents the simple name of a type
     * @param name the simple name to resolve
     * @param flags an <code>LFlags</code> value constraining the search to types
     * @return a <code>DeclarationSet</code> value with matching 
     * <code>Declarations</code>.
     */
 
    /* relays lookup request to all its compilation units */
    public DeclarationSet lookupType (String name, LFlags flags) {
       d.msg(Debug.COMPILE, "simple lookupType("+ name +", "+flags.toString()+") in " + toString());
       // Get any units already compiled
        Enumeration compiledUnits = scope.elements();
        DeclarationSet possibles = new DeclarationSetMulti();
        d.msg(Debug.COMPILE, "Look for " + name + " in already compiled parts of the package.");
        while (compiledUnits.hasMoreElements()) {
        	// Get the declaration of the next compilation unit
        	Declaration cUDecl = ((DeclarationSet)(compiledUnits.nextElement())).getSingleMember();
        	Assert.error(cUDecl!=null, "No single declaration for a compilationUnit in package.");
        	// Get the compilationUnit scopeHolder
        	SHCompilationUnit compilationUnit = (SHCompilationUnit)cUDecl.getScopeHolder();
        	possibles = compilationUnit.localLookup(name, flags);
        	if (!possibles.isEmpty()) break;
        }
        if (possibles.isEmpty()) {
            d.msg(Debug.COMPILE, "Look for "+ name + ".java as file name in " + getOwnDeclaration().getName());
            JavaFileManager fileManager = JavaFileManager.getFileManager();
            TMFile tmFile = fileManager.getFileFromPackage(name, getOwnDeclaration().getName());
            if (tmFile != null) { // A file of name exists in directory of this package
                d.msg(Debug.COMPILE, "Found file ");
                SimpleNode cuNode = fileManager.doFirstTwoPasses(tmFile);
                Declaration newDecl = cuNode.getDecl();
//                addDeclaration(newDecl);   // add new compilationUnit declaration to package
                // Now get the actual class declaration from the compilation unit
                possibles = ( (SHCompilationUnit)(newDecl.getScopeHolder())).localLookup(name, flags);
            }
        }
        return possibles;
    }
    
    
   
    
    /**********************************************************************
     * Recursion cutouts. Many kinds of lookups can't be done at the package level or above
     * so just return an empty set
     **/
    
    protected DeclarationSet lookupExpression(String name, LFlags flags) {
        return new DeclarationSetMulti();      
    }
    
    /**********************************************************************
     * suppresses contentDumping for classes that are part of the built-in java package
     * This is a debugging technique and built-in class information usually just clutters
     * the output.
     **/
    
    public void dumpContents(String indent, Debug d){   // used for debugging
    	 // To restore dumping of "built-in" java packages. comment out the if statement
    	if (!this.getOwnDeclaration().getName().contains(new Java_ScopedName("java")))
	        super.dumpContents(indent, d);
   }

 

    
    /** Standard descriptor
     * @return "package scope"
     */    
    public String toString(){return "scope of package " + super.toString();}
    

}