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

import tm.clc.analysis.Declaration;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.DeclarationSetMulti;
import tm.clc.analysis.LFlags;
import tm.clc.analysis.ScopeHolder;
import tm.utilities.Assert;
import tm.utilities.Debug;


/**
* This class represents the compilation unit scopeHolder. It holds only class scopeHolders
*/

public class SHCompilationUnit extends SHCommon {
// These should probably be vectors
/*    private SHCommon singleImports = new SHCommon(this);      // the singlr-Type imports for this compilation unit
    private SHCommon demandImports = new SHCommon(this);*/
    
    private Vector singleImports = new Vector();
    private Vector demandImports = new Vector();

    /** Create a compilation Unit scopeHolder
     * @param encl the enclosing package scope
     */
    public SHCompilationUnit(ScopeHolder encl) {
        super(encl);
        Java_ScopedName javaLang = new Java_ScopedName("java");
        javaLang.append("lang");
        Declaration decl = new Declaration(Java_LFlags.IMPORT_LF, javaLang,null, null, null);
        addImportDeclaration(decl, true);
    }

    /** Creates a new typeScope
     * adds it to the declaration as its definition and adds
     * the declaration to the scopes of this CompilationUnit
     * @param decl the declaration of the class being entered
     * @return the new class scopeHolder
     */
    public SHCommon createTypeScope(Declaration decl) {
       return createBlock(decl, new SHType(this));
    }

   // We'll deal with import-on-demand stuff later...
    /** add an imported declaration to the scopeHolder
     * @param decl import declaration to be added
     * @param onDemand true for onDemand imports
     */
    public void addImportDeclaration(Declaration decl, boolean onDemand) {
        if(onDemand)
            demandImports.addElement(decl);
        else
            singleImports.addElement(decl);
    }
    



/* Functions & Expressions can't be looked up in a Compilation Unit so this bottoms out the lookupFunction recursion
 */

    protected DeclarationSet lookupFunction(String name, LFlags flags) {
        return new DeclarationSetMulti();      
    }

    protected DeclarationSet lookupExpression(String name, LFlags flags) {
        return new DeclarationSetMulti();      
    }

    protected DeclarationSet lookupType(String name, LFlags flags){
       d.msg(Debug.COMPILE, "simple lookupType("+name +", "+flags.toString()+") in " + toString());
        DeclarationSet possible = localLookup(name, flags);
        if (!possible.isEmpty()) return possible;
        SHProgram theProgram = SHProgram.getProgramSH();
        
        // Check single imports
        for(int i = 0; i < singleImports.size(); i++) {
        	Declaration importDec = (Declaration)singleImports.elementAt(i);
        	if (name.compareTo(importDec.getUnqualifiedName())==0){ // Type is imported
        		d.msg(Debug.COMPILE, importDec.getName().getName() + " is imported in " + toString());
        		possible = theProgram.lookupType(importDec.getName(), flags);
        		Assert.error(!possible.isEmpty(), "Imported type " + importDec.getName() + " does not exist.");
        		return possible;
        	}
        }
        
        /*Check on-demand imports. There are a number of possibilities
         * Both package and type already exist in the symtab
         * Package exists in symtab but not the type
         * Neither is in the symtab
         * Get the demand declaration, create a new name by appending the
         * type to the demand dec, then search for that type
         */
        
        Java_ScopedName wanted; 
        for(int i = 0; i < demandImports.size(); i++) { // for each demand import
        	// Make up a possible import replacing '*' with desired class
        	Declaration demandDec = (Declaration)demandImports.elementAt(i);
        	wanted = new Java_ScopedName(demandDec.getName());
        	d.msg(Debug.COMPILE, "Checking demand import " + demandDec.getName() + ".* for inclusion of " + name);
        	wanted.append(name);
        	possible = theProgram.lookupType(wanted,flags);
        	/*Declaration packageDec = theProgram.lookupPackage(demandDec.getName(), new LFlags(Java_LFlags.PACKAGE_LF)).getSingleMember();
        	Assert.error(packageDec != null, "demand import declaration " + demandDec.getName().getName() + " not found.");
        	possible = ((SHPackage)(packageDec.getScopeHolder())).lookupType(name, flags);*/
        	if (!possible.isEmpty()) return possible;
        }
        
        
        // Now look in this package BEFORE DEMAND IMPORTS??
        return ((SHPackage)getEnclosingScope()).lookupType(name, flags);
    }


 /** Standard descriptor
     * @return "compilation unit scope"
     */
    public String toString(){return "scope of compilation unit " + getOwnDeclaration().getName();}

}
