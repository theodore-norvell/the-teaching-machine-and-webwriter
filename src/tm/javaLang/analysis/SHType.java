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
import java.util.Vector;

import tm.clc.analysis.Declaration;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.DeclarationSetMulti;
import tm.clc.analysis.LFlags;
import tm.clc.analysis.ScopeHolder;
import tm.utilities.Assert;
import tm.utilities.Debug;


/**
* This class is a ScopeHolder for classes & interfaces.
*/

public class SHType extends SHCommon {

    private Vector supertypes = new Vector ();  // The types it extends or implements

    /**
     * Creates a new instance
     * @param encl the physically enclosing scope (distinct from class
     * hierarchy
     */
    public SHType(ScopeHolder encl) {
        super(encl);
    }

    public SHCommon getBaseScope(){ // over-ride in baseScopes
        return this;
    }


    /** Creates a new typeScope (inner class)
     * adds it to the declaration as its definition and adds
     * the declaration to the scopes of this Type
     * @param decl the declaration of the class being entered
     * @return the new class scopeHolder
     */
    public SHCommon createTypeScope(Declaration decl) {
       return createBlock(decl, new SHType(this));
    }


    /** Creates a new function scope
     * adds it to the declaration as its definition and adds
     * the declaration to the scopes of this Type
     * @param decl the declaration of the function being entered
     * @return the new function scopeHolder
     */
    public SHCommon createFunctionScope(Declaration decl) {
       return createBlock(decl, new SHFunction(this));
    }

    /** Creates a new blockScope
     * and adds it to the list of blockScopes in this type
     * @return the new scopeHolder for the block
     * @param decl the declaration that created the block
     */
    public SHCommon createBlockScope(Declaration decl) { //MAKE SURE THIS IS LEGAL
       return createBlock(decl, new SHBlock(this));
    }


   /** Add SHCommon for a superClass of the class
     * @param sc The SHCommon for the superclass being added
     * Assertion: the first element added will be "the" superclass,
     * the one defined by an extends clause
     */    
    
    public void addSuperclass (ScopeHolder sc) {
        SHType moi = (SHType)sc;
        if (moi.inheritsFrom(this))
            Assert.error(toString() + " cannot inherit from a Type that inherits from it");
        supertypes.addElement (moi);
    }

    protected boolean inheritsFrom(SHType child) {
        if (child == this) return true;
        Enumeration parents = supertypes.elements();
        while (parents.hasMoreElements())
            if (((SHType)parents.nextElement()).inheritsFrom(child)) return true;
        return false;
    }
    
    /** Get the supertypes of this class
     * @return A vector of the supertypes' CommonSHs
     */
    public Enumeration getSupertypes () { return supertypes.elements(); }

/*** NOTE. We had a conflict here between Theo & me & I'm not sure why
 */
    public SHType getTheSuperType(){
        return (SHType) supertypes.elementAt(0);
    }
    

    protected boolean buildRelativePath (Declaration decl, Vector path) {
        // Fetch the scope of the immediate type holding the declaration
        Debug.getInstance().msg(Debug.COMPILE, "in " + toString() + " building RelativePath from "
            + decl.getName().getName());
/*  2003.10.07 (mpbl): The first of these lines was commented out & the second line substituted
 * obviously by me, when or why I don't know. Was causing a bug when looking up fields
 * The first seems correct. Leaving this in in case I spawn another bug
 */
        
        ScopeHolder scope = decl.isScopeHolder() ? 
            (ScopeHolder) decl.getDefinition() : decl.getEnclosingBlock();
        SHType type = (SHType)scope.getClassDeclaration().getDefinition(); 
        boolean found = (type == this);
        if (!found)
            // search superclasses
            for (int i = 0; i < supertypes.size () && !found; i++) {
                SHType sc = (SHType) supertypes.elementAt (i);
                path.addElement(new Integer(i));
                if (sc.buildRelativePath (decl, path)) found = true;
                else path.removeElementAt( path.size() - 1 ) ;
            }
        if(!found) {
            Declaration outer = getEnclosingScope().getClassDeclaration();

            if (outer != null ) { // I am an inner class
                path.addElement(new Integer(-1));
                found = ((SHType)outer.getDefinition()).buildRelativePath(decl, path);
            }
        }

        return found;
    }



    protected DeclarationSet lookupType(String name, LFlags flags) {
        d.msg(Debug.COMPILE, "simple lookupType("+name+", "+flags.toString()+") in " + toString());
        DeclarationSet possibles = localLookup(name, flags);
        if (!possibles.isEmpty()) return possibles;
        d.msg(Debug.COMPILE, "checking in supertypes as well");
        Enumeration iterator = getSupertypes();
        while (iterator.hasMoreElements())
            possibles.merge(((SHType)iterator.nextElement()).lookupType(name,flags));
        return !possibles.isEmpty() ? possibles : ((SHCommon)getEnclosingScope()).lookupType(name, flags); 
    }

    protected DeclarationSet lookupFunction(String name, LFlags flags) {
        d.msg(Debug.COMPILE, "simple lookupFunction("+name+", "+flags.toString()+") in " + toString());
        DeclarationSet possibles = new DeclarationSetMulti();
        possibles.merge(localLookup(name, flags));
        if (!flags.contains(Java_LFlags.CONSTRUCTOR)) {
	        d.msg(Debug.COMPILE, "checking in supertypes as well");
	        Enumeration iterator = getSupertypes();
	        while (iterator.hasMoreElements()){
	            possibles.merge( ((SHType)iterator.nextElement()).lookupFunction(name,flags));
	        }
        }
        return possibles.isEmpty() ?  ( (SHCommon)getEnclosingScope()).lookupFunction(name, flags) : possibles;
    }

    /**
     * At the type level we are looking up the name of a member variable
     * The routine is recursive.
     * 1. It looks locally first and quits if it finds a match since under
     *    JLS 8.3 such a match hides all occurrences of the same field name
     *    in supertypes of the class and shadows all occurrences in enclosing
     *    scopes.
     * 2. If no match is found it looks recursively up the supertype chains.
     *    Because of 1 it will quit as soon as it finds the name in a
     *    particular chain (hiding) but it will find all distinct instances
     *    across parallel chains. It will then throw out any which are not
     *    accessible (should only occur in <i>the</> supertype since fields
     *    of interfaces are intrinsically accessible (JLS 6.6.1)).
     * 3. If there is still no match, it looks up the enclosing scope holder
     *    hierarchy.
     * @param encl the physically enclosing scope (distinct from class
     * hierarchy
     */

    
    protected DeclarationSet lookupExpression(String name, LFlags flags) {
        d.msg(Debug.COMPILE, "simple lookupExpression("+name+", "+flags.toString()+") in " + toString());
        DeclarationSet possibles = localLookup(name, flags);
        if (!possibles.isEmpty()) return possibles;
        
        d.msg(Debug.COMPILE, "checking in supertypes as well");
        Enumeration iterator = getSupertypes();
        boolean theSupertype = true;
        boolean blocked = false;
        String blockedName = "";
        while (iterator.hasMoreElements()){ // get every distinct hit in parallel supertypes
            possibles.merge(((SHType)iterator.nextElement()).lookupExpression(name,flags));
            if (theSupertype) { // Accessibility is only an issue in the supertype
                if (!possibles.isEmpty()){ // at least one field among supertypes
                	Declaration possible = possibles.getSingleMember();
                	Assert.error(possible != null, multipleDecs + "for field " + name);
                	// Save the name of the single superType declaration in case it is inaccessible
        	        blockedName = possible.getName().getName();
        	        // Back to declarationSet since we are building it in the loop
        	        possibles = winnowOnAccessibility(possibles);
        	        blocked = possibles.isEmpty(); // empty => THE supertype field is inaccessible
                }
            	theSupertype = false;                
            }
        }
        if (!possibles.isEmpty()) return possibles;
        possibles = ( (SHCommon)getEnclosingScope()).lookupExpression(name, flags);
        Assert.error(!(possibles.isEmpty() && blocked),
        		blockedName + " is inacessible from " + toString());
        return possibles;
    }

    protected DeclarationSet lookupAmbiguous(String name, LFlags flags) {
        return new DeclarationSetMulti();
    }


    /** looking for qualified members of a Type, implies first finding an inner class that
     * matches element[0] of the ScopedName. I believe that must be a member type
     * (although other kinds of inner types can exist I don't think they can be referenced like this)
     * @param the id of the declaration being sought
     * @return a DeclarationSet containing only 1 Declaration
     */
// I Don't think we should ever do a qualified lookup on a type.
//    protected DeclarationSet qualifiedLookup(ScopedName name, LFlags flags){
//        Assert.check(flags.intersects(Java_LFlags.MEMBER_LF), "Can only look for members in a type.");
//        name.index.reset();
//        DeclarationSet innerClasses = unqualifiedLookup(name.selectedPart(),Java_LFlags.TYPE_LF);
//        Assert.error((innerClasses.getSingleMember() == null), "can't find " + name.selectedPart() + " in " + toString() );
//        SHType innerType = (SHType)innerClasses.getSingleMember().getScopeHolder();
//        name.index.advance();
//        if (name.index.terminal())
//            return innerType.unqualifiedLookup(name.getTerminalId(), flags);
//        else
//            return innerType.qualifiedLookup(innerName(name),flags);
//    }
    
    
    private DeclarationSet winnowOnAccessibility(DeclarationSet possibles){
    	DeclarationSet matches = new DeclarationSetMulti();
        Enumeration iterator = possibles.elements();
        while (iterator.hasMoreElements()) {
            Declaration possible = (Declaration) iterator.nextElement();
            if (isAccessible(possible))
        		matches.merge(possible);
        }
        return matches;
    }


    /** Standard descriptor
     * @return "class scope"
     */

    public String toString(){
    	return (isStaticContext() ?	"static " : "" ) + 	"scope of type " + super.toString();
    }

   public void dumpContents(String indent, Debug d){   // used for debugging
       Enumeration iterator = supertypes.elements();
       if (iterator.hasMoreElements()) {
           d.msg(Debug.COMPILE, "Supertypes {");
           while (iterator.hasMoreElements()){
               SHType supertype = (SHType)iterator.nextElement();
               d.msg(Debug.COMPILE, " " + supertype.getOwnDeclaration().getName());
           }
           d.msg(Debug.COMPILE, "} ");
       }
       super.dumpContents(indent, d);
   }


}