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

package tm.clc.analysis;

import java.util.*;
import tm.clc.ast.TypeNode;
import tm.utilities.Assert;
import tm.utilities.Debug;

/**
 * Represents a declaration from the perspective of name resolution. 
 * Can represent any type of declaration (namespace, function, type, 
 * variable, label). 
 *
 * <br>Notes: 
 * <ul><li>definitions in code are represented by a combination of 
 *   Declaration and Definition object
 * <li>label definitions are considered declarations from the 
 *   perspective of the symbol table. They have no corresponding 
 *   Definition object.</ul>
 *
 * @author Derek Reilly
 * @version 1.0
 * @created Aug 13 2001
 */
public class Declaration implements Definition, DeclarationSet {

    private final static String CANNOT_BUILD_RELATIVE_PATH = 
        "A relative path for this Declaration cannot be built - the " + 
        "runtimeId needs to be a ScopedName, but we have {0}";

    private ScopedName name;
    private Definition defn; 
    private TypeNode type;

    //    private boolean defining;
    private LFlags category;

    private SpecifierSet specSet;

    private Object runtimeId = null;
    
    private boolean hasIntegralConstantValue = false ;
    private long integralConstantValue ;
    private boolean hasFloatingConstantValue = false ;
    private double floatingConstantValue ;

    // the block in which the declaration appears.
    // ?? is this reference necessary? 
    // ScopeHolder already has an 'enclosing scope' reference, so 
    // type, namespace, and function declarations will have a duplicate
    // reference. The ScopeHolder reference needs to be there, to accommodate
    // block scopes, which have no related Declaration.
    // Variable declarations don't have ScopeHolders as definitions, so
    // this reference needs to be here if it is needed at all.
    private ScopeHolder enclosingBlock = null;

    private int scopePos = -1;

    /**
     * Creates a new declaration, with a more or less complete set of 
     * initialization detail. 
     * @param cat the category of declaration  
     * @param name the ScopedName associated with this declaration
     * @param defn the Definition associated with this declaration
     * @param specifiers the set of modifiers qualifying the declaration
     * @param type the AST representation of the type 
     */

    public Declaration (LFlags cat, ScopedName name, Definition defn, 
                        SpecifierSet specifiers, TypeNode type) {
        category = cat.writable ();
        this.name = name;
        if (defn == null && category.contains (LFlags.VARIABLE)) {
            // default to self-definition (scalar variable)
            this.defn = this;
        } else {
            this.defn = defn;
        }
        specSet = specifiers;
        this.type = type;
    }

    /**
     * Creates a new declaration, with much left undetermined. 
     * @param category the category of declaration  
     * @param name the ScopedName associated with this declaration
     */
    public Declaration (LFlags category, ScopedName name) {
        this (category, name, null, null, null);
    }
   
    /**
     * Creates a new declaration, with much left undetermined. 
      * @param name the ScopedName associated with this declaration
     * @param specSet the set of modifiers qualifying the declaration
     */
    // Added by Mike and Jon
    //public Declaration(ScopedName name, SpecifierSet specSet) {
    //    this(new LFlags(), name, null, specSet, null);
    //}
    
     /**
     * Creates a new declaration, with much left undetermined. 
     * @param name the ScopedName associated with this declaration
     */
   // Added by Mike and Jon
   // public Declaration(SourceCoords coords, ScopedName name) {
   //     this(new LFlags(), name, null, null, null);
   // }

    /**
     * Returns the category of this <code>Declaration</code>.
     * @return an <code>LFlags</code> category
     */

    public LFlags getCategory () { return category; }



    /**

     * Returns the list of specifiers that qualified this 

     * <code>Declaration</code>. 

     * @return the specifiers

     */

    public SpecifierSet getSpecifiers () { return specSet; }



    /**
     *
     * Indicates whether this <code>Declaration</code> has a particular
     *
     * specifier / modifier.
     * @param specifier the (language-specific) specifier to test for.
     * @return true if it has the specifier
     */

    public boolean hasSpecifier (int specifier) { 

        return  (specSet != null && specSet.contains (specifier));

    }



    /**
     * Provides access to the <code>Definition</code> associated with this
     * <code>Declaration</code>.
     * @return the associated <code>Definition</code>, or <code>null</code> if 
     * none exists
     */

    public Definition getDefinition () { return defn; }



    /**
     * Allows the <code>Definition</code> to be set or changed. 
     * @param d a <code>Definition</code> value
     */

    public void setDefinition (Definition d) { defn = d; }



    /**
     * Provides the fully-qualified id associated with this declaration
     *
     * @return a <code>ScopedName</code> representation of the fqn
     */

    public ScopedName getName () { return name; }



    /**
     * Sets the fully qualified id associated with this declaration.
     *
     * @param name a <code>ScopedName</code> representation of the fqn
     */

    public void setName (ScopedName name) { this.name = name; }


    /**
     * Returns the id associated with this declaration in unqualified form.
     *
     * @return the unqualified id as a <code>String</code> 
     */

    public String getUnqualifiedName () {
        if (name != null) return name.getUnqualifiedName ();
        else return null;
    }



    /**
     * Sets the runtime name of this <code>Declaration</code>. The <em>runtime name</em>
     * is a name suitable for use as an identifier in a runtime symbol table. If unset, 
     * the <code>ScopedName</code> will be
     * used. The logic determining when to use the <code>ScopedName</code> or assign
     * a different id is language-dependent.
     * @param name the id to use as a runtime name
     */

    public void setRuntimeId (Object name) { runtimeId = name; }



    /** 
     * Returns a key suitable for use as a runtime 
     * symbol table id for this <code>Declaration</code>. 
     * @return the runtime representation of this <code>Declaration</code>
     */

    public Object getRuntimeId () { 
        return (runtimeId == null) ? name : runtimeId;
    }



    /**
     * Returns a relative path from the current scope (inside a class) or class scope
     * of object (for member access). This is equivalent to the runtime id, presented
     * as an array of <code>int</code>.
     * @return the path to this declaration, or <code>null</code> if none has been
     * built.
     */
    public int [] getRelativePath () {
        if (!(runtimeId instanceof ScopedName)) 
            Assert.check (CANNOT_BUILD_RELATIVE_PATH, 
                            runtimeId.getClass().getName ());
        return getRelativePath ((ScopedName) runtimeId);
    }



    /**
     * Returns a relative path from the current scope (inside a class) or class scope
     * of object (for member access). This is equivalent to the runtime id, presented
     * as an array of <code>int</code>.
     * @return the path to this declaration, or <code>null</code> if none has been
     * built.
     * @param sn_path ?the name used as an identifier in the runtime symbol table?(mpbl)
     */
    public static int [] getRelativePath (ScopedName sn_path) {
        sn_path.completed ();
        int [] path = new int [sn_path.length ()];
        sn_path.index.reset ();
        if (sn_path.length () > 0) {
            do {
                try {
                    path [sn_path.index.value ()] = 
                        Integer.parseInt (sn_path.selectedPart ());
                } catch (NumberFormatException nfe) {
                    sn_path.index.reset ();
                    Assert.apology (CANNOT_BUILD_RELATIVE_PATH, 
                                    "non-numeric ScopedName");
                }
            } while (sn_path.index.advance ());
            sn_path.index.reset ();
        }
        return path;
    }



    /**
     * gets the related <code>TypeNode</code>, or null if not applicable
     * @return the type of entity declared
     */
    public TypeNode getType () { return type; }

    /**
     * sets the related <code>TypeNode</code>
     * @param type the type of entity declared
     */
    public void setType (TypeNode type) { 
        // ** this should be in the definition.
        this.type = type; 
    }



    /**
     * Does the definition matching this declaration have scope?
     * @return <code>true</code> if definition does have scope
     */
    public boolean isScopeHolder () { return (defn instanceof ScopeHolder); }



    /**
     * Returns the position in the enclosing scope that this declaration was
     * made.
     *
     * @return the position
     */
    public int getPosition () { return scopePos; }

    /**
     * Returns the block in which this declaration appears.
     *
     * @return a <code>ScopeHolder</code> value representing the block in 
     * which this declaration appears.
     */
    public ScopeHolder getEnclosingBlock () { return enclosingBlock; }

    /**
     * Sets the position in the enclosing scope that this declaration appears
     *
     * @param pos the position
     */
    protected void setPosition (int pos) { scopePos = pos; }

    /**
     * Sets the block in which this declaration appears
     * changed to public 2003.10.09 by mpbl
     *
     * @param s a <code>ScopeHolder</code> representing the block in which 
     * this declaration appears.
     */
    public void setEnclosingBlock (ScopeHolder s) {
        enclosingBlock = s;
    }

    /** set the integralConstant which appears to be an arbitrary integer tag for the
     * declaration
     * @param val the integralConstant
     */    
    public void setIntegralConstantValue( long val ) {
        hasIntegralConstantValue = true ;
        integralConstantValue = val ; }
    
    /** an integralConstant has been set
     * @return true if an integralConstant has been set
     */    
    public boolean hasIntegralConstantValue() {
        return hasIntegralConstantValue ; }
    
    /** retrieve the attached integralConstant
     * @return the integerConstant attached by the last setIntegralConstant call
     */    
    public long getIntegralConstantValue() {
        return integralConstantValue ; }

    /** set the floatingConstant which appears to be an arbitrary integer tag for the
     * declaration
     * @param val the integralConstant
     */    
    public void setFloatingConstantValue( double val ) {
        hasFloatingConstantValue = true ;
        floatingConstantValue = val ; }
    
    /** a floatingConstant has been set
     * @return true if a floatingConstant has been set
     */    
    public boolean hasFloatingConstantValue() {
        return hasFloatingConstantValue ; }
    
    /** retrieve the attached floatingConstant
     * @return the floatingConstant attached by the last setFloatingConstant call
     */    
    public double getFloatingConstantValue() {
        return floatingConstantValue ; }

    // ** DeclarationSet implementation - javadoc taken from interface
    //    unless overridden
    public boolean isEmpty () { return false; }

    

    /** Return the scopeHolder associated with the definition (which appears to be either the
     * scopeHolder for the declaration, or another declaration)
     * @return the scopeHolder associated with the declaration's definition
     */    
    public ScopeHolder getScopeHolder () { 
        if (isScopeHolder ()) return ((ScopeHolder) defn);
        else if (defn != this) return ((Declaration)defn).getScopeHolder ();
        else Assert.apology ("No scope associated with this declaration");
        return null;
    }
    
    /** Added 2003.10.05 by mpbl to support Java.
     * Return the scopeHolder of the associated class which will normally
     * be the associated class Declaration's Definition for scopes which have a Definition
     * otherwise is the class Declaration's Definition of the enclosingBlock.
     * Will return null for scopes which do not have an associated class (such as package).
     * 
     * @return the scopeHolder associated with the declaration's definition
     */    
    public ScopeHolder getClassScope() {
        ScopeHolder scope = isScopeHolder() ? (ScopeHolder) defn :
            enclosingBlock;
        return enclosingBlock == null ? null : (ScopeHolder) scope.getClassDeclaration().getDefinition();
    }



    /** Returns itself. By definition a declaration is a declarationSet with only a
     * single declaration
     * @return itself
     */    
    public Declaration getSingleMember () { return this; }

   /** Returns itself. By definition a declaration is a declarationSet with only a
     * single declaration, which is the first declaration.
     * @return itself
     */    

    public Declaration getFirstMember () { return this; }


    /** Satisfies declarationSet interface. Can't append to a single declaration.
     * @param ds will assert an apology
     */    
    public void append (DeclarationSet ds) {
        Assert.apology ("Cannot append to single Declaration");
    }



    /**

     * Merges (union) the elements of the DeclarationSet passed in with the 
     * current elements. 
     * <br><em>Invalid operation on Declarations - an error message
     * will be displayed.</em>
     * @param ds the DeclarationSet whose elements will be merged
     */

    public void merge (DeclarationSet ds) { 
        Assert.apology ("Cannot merge against single Declaration");
    }



    /** Returns an enumeration over the elements of the set
     * <br><em>note:</em>Here for interface compliance - it is better to use
     * <code>getSingleMember</code> instead where possible
     * @return the enumeration containing only this declaration
     */
    public Enumeration elements () { 
        Vector v = new Vector ();
        v.addElement (this);
        return v.elements (); 
    }



    /** Satisfies declarationSet interface.
     * @param d the declaration to be compared
     * @return <CODE>true</CODE> if this declaration
     */    
    public boolean contains (Declaration d) { 
        return this == d;
    }



    /** The size of the declarationSet
     * @return 1
     */    
    public int size () { return 1; }
    public Declaration declaration (int i) { 
        if (i == 0) return this;
        throw new NoSuchElementException ("" + i);
    }
    
    public String toString(){return "Declaration of " + name.toString();}
   
    /** Used for debugging. Dumps the contents of the declaration to
     * System.out
     * 
     */    
    public void dumpContents(String indent, Debug d){
        if( ! d.isOn(Debug.COMPILE) ) return ;
        d.msg(Debug.COMPILE, indent);
        if (name != null) d.msg(Debug.COMPILE, name.toString() + " ");
        if (category != null && category.getRawVal()!= 0){
            int mask = 0x1;
            boolean flagOut = false;
            for (int i = 0; i < 16; i++) {
                if ((category.getRawVal() & mask) != 0) {
                    if (flagOut) d.msg(Debug.COMPILE, "+ ");
                    d.msg(Debug.COMPILE, category.toString(mask) + " ");
                    flagOut = true;
                }
                mask *= 2;
            }
        }
        d.msgNoNL(Debug.COMPILE, toString() + ":");
       
        if (specSet != null) d.msgNoNL(Debug.COMPILE, specSet.toString() + " ");
        if (type != null) d.msgNoNL(Debug.COMPILE, type.toString() + " ");
        if (defn != null) {
            if (isScopeHolder()){
                ((ScopeHolder)defn).dumpContents(indent + "   ", d);
                d.msg(Debug.COMPILE, indent + "}");
            }
            else d.msg(Debug.COMPILE, " {Empty def}" /*defn.toString()*/);
        }
        else d.msg(Debug.COMPILE, "") ;
    }
    // ** End DeclarationSet implementation
}
