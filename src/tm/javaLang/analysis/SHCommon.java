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
import tm.clc.analysis.Definition;
import tm.clc.analysis.LFlags;
import tm.clc.analysis.ScopeHolder;
import tm.clc.analysis.ScopedName;
import tm.clc.analysis.SpecifierSet;
import tm.clc.analysis.UndefinedSymbolException;
import tm.clc.ast.TypeNode;
import tm.interfaces.SourceCoords;
import tm.javaLang.parser.JavaParserTreeConstants;
import tm.utilities.Assert;
import tm.utilities.Debug;


/**
 * This class is the base ScopeHolder for all Java ScopeHolders. It specializes
 * the {@link tm.clc.analysis.ScopeHolder common language ScopeHolder} for Java.
 * It implements the state pattern by providing transition methods to all possible
 * scopes then providing default methods blocking all state transition.
 * Each actual state derived from the base class over-rides the legal transitions to
 * other states.
 * It also handles basic lookUp services providing the localLookup method for all
 * Java ScopeHolders as well as default methods for normal lookups.
 * @author mpbl
 * */

public class SHCommon extends ScopeHolder implements Definition, Java_LFlags,
                                                   JavaParserTreeConstants {
/** From section 8.1.2
 * A statement or expression occurs in a static context if and only if the
 * innermost method, constructor, instance initializer, static initializer,
 * field initializer, or explicit constructor statement enclosing the
 * statement or expression is a static method, a static initializer, the
 * variable initializer of a static variable, or an explicit constructor
 *  invocation statement.
 * 
 * Since we are associating static context with Java ScopeHolders we are deFacto
 * making field initialization into a scope
 * 
 * Thus we will convert
 * 
 * class C {
 *    int x = <expression>;
 * }
 * 
 * to
 * class C {
 *    int x;
 *    {   this.x = <expression>;}
 * }
 * 
 * effectively creating a new initializer block, which can then have a static
 * context attached to it or not.
 * 
 */
                                                   
    private boolean staticContext = false;     // Is this scope inside a static context?
    
    public static String multipleDecs = "Found multiple declarations ";


        /** Constructor
        * @param encl the {@link tm.clc.analysis.ScopeHolder scopeHolder} for the enclosing scope
        */                                                       
    public SHCommon (ScopeHolder encl) { 
        super(encl);
    }
    
    /** Get the effective enclosing fundamental scope: SHFunction, SHType, SHPackage and
     *  SHProgram are all baseScopes 
     * @return the baseScope
     */    
    public SHCommon getBaseScope(){ // over-ride in baseScopes
        return ((SHCommon)enclosingScope).getBaseScope();
    }
    
    
    
    
     /** If the package has not been encountered before, creates both a
     * new Declaration and a new packageScope and adds it to the list of 
     * enclosed packageScopes. Must be in program
     * scope
     * @param name the name of the package being entered
     * @param specSet the SpecifierSet
     * @return The scopeHolder for the program
     */    
    public SHCommon createPackageScope(ScopedName name, SpecifierSet specSet, SourceCoords coords) {
        return oops("package scope");
    }
    
    
     /** <p>Creates a new compilationUnitScope
      *     and adds it to the list of enclosed compilationUnitScopes.</p>
      * <p>Assertion: scope is not already in the list of compilationUnitScopes.</p>
      * @return new compilationUnit scope
      * @param decl The declaration that spawned the scope
      */    
    public SHCommon createCompilationUnitScope(Declaration decl) {
        return oops("compilation unit scope");
    }
    
    
    /** Creates a new typeScope
     * adds it to the declaration as its definition and adds
     * the declaration to the scopes of this CompilationUnit
     * @param decl the declaration of the class being entered
     * @return the new class scopeHolder
     */    
    public SHCommon createTypeScope(Declaration decl) {
        return oops("type scope");
    }
    
    
    /** Creates a new functionScope
     * adds it to the declaration as its definition and adds
     * the declaration to the scopes of this CompilationUnit
     * @param decl the declaration of the function being entered
     * @return the new class scopeHolder
     */    
    public SHCommon createFunctionScope(Declaration decl) {
        return oops("function scope");
    }
    
    
     /** Creates a new blockScope
      * and adds it to the list of blockScopes in this class
      * @return the new scopeHolder for the block
      * @param decl the declaration that created the block
      */    
    public SHCommon createBlockScope(Declaration decl) {
        return oops("block scope");
    }

   
     /** Creates a new localScope
      * @return the new scopeHolder for the block
      * @param decl the declaration that created the block
      */    
    public SHCommon createLocalScope(Declaration decl) {
        return oops("local scope");
    }

   
    
    private SHCommon oops(String scope){
        Assert.apology("Sorry. Can't create " + scope + " from " + toString());
        return null;
    }
    
    /** Add in the declaration that created the scopeHolder
     * @param decl the declaration that created the block
     */    
    public void addOwnDeclaration(Declaration decl){
        ownDeclaration = decl;
    }
    
    
    public void addDeclaration(Declaration decl){
//        Assert.error(localLookup(decl.getName().getTerminalId(), decl.getCategory()) == null, "Duplicate declaration in " + toString());
        super.addDeclaration(decl);
        decl.setEnclosingBlock(this);
    }
    
   /************************* SERVICE ROUTINES FOR CREATING SCOPES ***********************
    * This common createBlock routine is used for any type of block scope
    * including function blocks,  class blocks & anonymous blocks 
    * @param decl the declaration of the scope being entered
    * @param sh the (new) specialized scopeHolder for the scope being entered
     * @return the scopeHolder
     */    
    protected SHCommon createBlock(Declaration decl, SHCommon sh) {
       decl.setDefinition(sh);    // The definition of a Block IS its scopeholder
       sh.addOwnDeclaration(decl); // Let the ScopeHolder know about its declaration
       addDeclaration(decl);   // Oh, yeah. And add the Block declaration to this Scope
       if (sh instanceof SHType)
           sh.classDeclaration = decl;
       else sh.classDeclaration = (sh.enclosingScope == null) ? null : sh.enclosingScope.getClassDeclaration();
       // If necessary, set the context to static (This may be too simple minded)
       SpecifierSet specSet = decl.getSpecifiers();
       sh.staticContext = specSet != null ? specSet.contains(Java_Specifiers.SP_STATIC) :
				       	this.staticContext ; 
       return sh;
    }
    
     /** Creates a new localScope, a ScopeHolder that defines the scope
      * of a local variable. 
      * Local variables in Java are anomalous in tnat they are the only
      * names whose scope starts just at the point of declaration. All
      * other Java names have a scope that ranges throughout the entire
      * enclosing scope. Thus, while the normal procedure is to
      * 1. add the new declaration directly to its enclosing scope
      * 2. create a new scopeHolder which is the <i>definition</i> of the
      *     new declaration
      * 
      * here we have to proceed differently.
      * 1. The declaration is for the local scopeholder not the variable
      *    The local scopeholder is connected into the scope tree rendering
      *    the local variable itself invisible to searches carried out at
      *    the level of the enclosing scopeholder
      * 2. A separate declaration is created for the variable and added
      *    to the local scopeholder. This declaration has no definition and
      *    is itself only a leaf of the scope tree. Scopes that occur inside
      *    its scope are added to its scopeholder.
     * While it is tempting to make the localScope the definition for actual
     * variable, scopeholders are implemented recursively so this would create
     * a recursion loop. The lack of a definition for local variable declarations
     * seems to make them anomolous. However, the local scopeholder declaration
     * may be viewed as the stand-in for the actual variable declaration.
     * 
     * Much of the information in the local variable declaration is doubled in its
     * local scopeholder declaration. While this was done to keep argument
     * passing for all scope creation consistent, it does mean that in many places
     * the scope declaration can genuinely function as a stand-in for the variable
     * declaration. If the actual variable declaration is needed, the local
     * scopeholder has a getTheVariable method which can provide it directly.
     * 
     * @return the new local scopeHolder 
     * @param wrap The declaration of the local variable scope which
     * 				will contain the actual local variable declaration

     */    
    protected SHCommon createLocal(Declaration wrap){
        addDeclaration(wrap);
        
    	// Create the actual variable declaration
        Declaration theDecl =
    		new Declaration(Java_LFlags.LOCAL_VARIABLE_LF, wrap.getName(),null, wrap.getSpecifiers(),null);
        SHLocal local = new SHLocal(this);       
        local.classDeclaration = getClassDeclaration();
        local.addOwnDeclaration(wrap);
		wrap.setDefinition(local);
        local.addDeclaration(theDecl); // of variable to new local scopeHolder
        return local;
    }
    
/* This is tricky. We're creating a Type in a place where it effectively has its own localScope.
 * In effect we are creating two ScopeHolders, the sHLocal which actually contains the type declaration
 * decl and the definition of the Type which is a SHType.
 **/    
    protected SHCommon createLocalType(Declaration decl){
        SHLocal local = new SHLocal(this);
        Declaration anon = new Declaration( Java_LFlags.EMPTY_LF, new Java_ScopedName ("__LocalScope"),local, null, null);
        SHType definition = new SHType(local);
        decl.setDefinition(definition);
        definition.addOwnDeclaration(decl);
        addDeclaration(anon);
        local.addDeclaration(decl);
        definition.classDeclaration = decl;
        return definition;
    }
    
       
   
    /** One time set of the context to <i>static</i>
     */    
    public void setStaticContext(){
        staticContext = true;
    }
    
    /** is the context static?
     * @return <b>true</b> if context is <i>static</i>.
     */    
    public boolean isStaticContext(){
        return staticContext;
    }
    
    /** Get the package to which this scope belongs. 
     * @return the package to which it belongs or null if its a program
     */    
    public SHPackage getPackage(){ // Is this being used?
        ScopeHolder check = this;
        while (check != null) {
            if (check instanceof SHPackage) return (SHPackage)check;
            check = check.getEnclosingScope();
        }
        return null;
    }
    
    protected boolean buildRelativePath (Declaration decl, Vector path) {
        SHType myClass = (SHType)getClassDeclaration().getDefinition();
        return (myClass == null) ? false : myClass.buildRelativePath (decl, path);
    }
    
    
/**************************** LOOKUP ROUTINES *********************************************/    
    /**
     * Basic lookup qualified names in this scope.
     *
     * @param name a <code>ScopedName</code> value representing the id.
     * @param flags a <code>LFlags</code> value constraining the search.
     * @return a <code>DeclarationSet</code> value containing matching 
     * <code>Declarations</code>. May be empty but never null
     */
    public DeclarationSet lookup(ScopedName name, LFlags flags){
  /* All this guarding is to satisfy the compiler. We are no longer using
   *  UndefinedSymbolException. Instead we are detecting a whole host of
   *  different compile errors and calling Assert.error(). By cutting off
   *  the exception at this base class pass we prevent it from having to
   *  be dealt with in the derived scopeHolders. Better to get rid of it
   *  completely!!!!
   **/
       try { // see comment in unqualified version below
           if (false) throw new UndefinedSymbolException(name); // never thrown
           return (name.is_qualified()) ? qualifiedLookup(name, flags) : unqualifiedLookup(name,flags);
       } catch (UndefinedSymbolException e) { // This satisfies the compiler as we over-ride lookups
           return null;
       }
    }
    
   /**
     * Default Java id resolution given an unqualified id.
     * only searches this scope
     * @param name the id to resolve
     * @param flags an <code>LFlags</code> value constraining the search
     * @return a <code>DeclarationSet</code> value with matching 
     * <code>Declarations</code>.
     */
    protected DeclarationSet unqualifiedLookup(ScopedName name, LFlags flags){
        Assert.check(!name.is_qualified(), "Call made to unqualifiedLookup with qualified name in " + this.toString());
       try { // see comment in overloaded version below
           if (false) throw new UndefinedSymbolException(name);
           return lookup(name.getTerminalId(), flags);
       } catch (UndefinedSymbolException e) { // This satisfies the compiler as we over-ride lookups
           return null;
       }
    }

    
   /**
     * Default Java id resolution given an unqualified id.
     * @param name A string vesrion of the id to resolve
     * @param flags an <code>LFlags</code> value constraining the search
     * @return a <code>DeclarationSet</code> value with matching 
     * <code>Declarations</code>.
     * @exception UndefinedSymbolException not thrown in the default lookup as not all lookups are expected to be successful
     */
    protected DeclarationSet lookup(String name, LFlags flags){
 /* Design Note: delegates lookup to specialized routines for each type of name. In this default version
  * the specialized routines mostly delegate it right back to a generic routine which tries this scope, and
  * if it can't find it there it recursively checks the enclosing scope until it is found. This design
  * permits specialized scopeHolders to over-ride selected specialised lookups without having to redelegate
  **/
       try {
           if (false) throw new UndefinedSymbolException(name);
           if (flags.intersects(Java_LFlags.PACKAGE)) return lookupPackage(name, flags);
           if (flags.intersects(Java_LFlags.TYPE)) return lookupType(name, flags);
           if (flags.intersects(Java_LFlags.FUNCTION)) return lookupFunction(name, flags);
           if (flags.intersects(Java_LFlags.EXPRESSION)) return lookupExpression(name, flags);
           if (flags.intersects(Java_LFlags.AMBIGUOUS)) return lookupAmbiguous(name, flags);
          // Although not required for name resolution, we do need to to be able to check if a CU exists
           if (flags.intersects(Java_LFlags.COMPILATION_UNIT)) return lookupCU(name, flags);
           Assert.check("Got to lookup something");
           return null;
       } catch (UndefinedSymbolException e) { // This satisfies the compiler as we over-ride lookups
           return null;
       }
    }
    
   protected DeclarationSet lookupPackage(String name, LFlags flags) {
       d.msg(Debug.COMPILE, "simple lookupPackage("+name+", "+flags.toString()+") in " + toString());
        return SHProgram.getProgramSH().lookupPackage(name, flags);   // There's no point in looking in low-level SH's
    }

    protected DeclarationSet lookupType(String name, LFlags flags) {
       d.msg(Debug.COMPILE, "simple lookupType("+name+", "+flags.toString()+") in " + toString());
        DeclarationSet possible = localLookup(name, flags);
        return (!possible.isEmpty() ) ?  possible :  ( (SHCommon)getEnclosingScope()).lookupType(name, flags);      
    }
    
    protected DeclarationSet lookupFunction(String name, LFlags flags) {
       d.msg(Debug.COMPILE, "simple lookupFunction("+name+", "+flags.toString()+") in " + toString());
 /*	commented out on 2005.04.07 - remove once testing complete
  *        DeclarationSet possible = localLookup(name, flags); 
        return (!possible.isEmpty()) ?  possible : 
        	( (SHCommon)getEnclosingScope()).lookupFunction(name, flags);  */
 // This should be more efficient since funtions can only be in type scope
       return ((SHType)getOwnDeclaration().getClassScope()).lookupFunction(name, flags);
    }
    
    protected DeclarationSet lookupExpression(String name, LFlags flags) {
       d.msg(Debug.COMPILE, "simple lookupExpression("+name+", "+flags.toString()+") in " + toString());
        DeclarationSet possible = localLookup(name, flags);
        return (!possible.isEmpty()) ?  possible :  ( (SHCommon)getEnclosingScope()).lookupExpression(name, flags);      
    }
    
    /**
     * Default Java id resolution for an ambiguous identifier. Since looking up
     * ambiguous names involves classifying them, LFlags is actually an output
     * @param name A string version of the id to resolve
     * @param flags an <code>LFlags</code> value to be defined by the search
     * @return a <code>DeclarationSet</code> value with matching 
     * <code>Declarations</code>.
     * @exception UndefinedSymbolException not thrown in the default lookup as not all lookups are expected to be successful
     */
    protected DeclarationSet lookupAmbiguous(String name, LFlags flags) {
       d.msg(Debug.COMPILE, "simple lookupAmbiguous("+name+", "+flags.toString()+") in " + toString());
       flags.classify(Java_LFlags.VARIABLE); // must over-ride flags to categorize
        DeclarationSet possibles = lookupExpression(name, flags);
        if (possibles.isEmpty()){
        	flags.classify(Java_LFlags.TYPE);  // no clear, set adds a flag
        	possibles = lookupType(name, flags);
        	if (possibles.isEmpty()) {	// must be a package
        		flags.classify(Java_LFlags.PACKAGE);  // setRawValue over-rides
        		d.msg(Debug.COMPILE, "Simple lookupAmbiguous: " + name + " categorized as package.");
        		possibles = lookupPackage(name,flags);
        	}
        }
        return possibles;
    }

    
    /**
     * Although not required for name resolution, we do need to to be able to check if a CU exists
     * t
     * @param name The <code>JavaScopedName</code> to be resolved
     * @param flags an <code>LFlags</code> value which should be Java_LFlags.COMPILATION_UNIT
     * @return a <code>DeclarationSet</code> value with matching 
     * <code>Declarations</code>.
     */
        protected DeclarationSet lookupCU(String name, LFlags flags) {
        	return getPackage().lookupCU(name,flags);
        }


    
    /**
    * Looks up a simple name in this scope only and filters it for category. The
    * base routine upon which all other lookup is built.
    * @param name the identifier being sought
    * @param category the category of declaration desired in the filtered output
     * @return a single Declaration, or a populated or empty Declaration Set
     */    
     
    DeclarationSet localLookup(String name, LFlags category){
       d.msg(Debug.COMPILE, "localLookup("+name+", "+category.toString()+") in " + toString());
        DeclarationSet possibles = scope.get(name);
        return winnowOnFlags(possibles, category);
    }

    
/*************************** QUALIFIED NAME LOOKUP ROUTINES *********************************/
    /**
     * Default Java id resolution given a qualified id.
     * @param name the qualified (perhaps partly) id to resolve
     * @param flags an <code>LFlags</code> value constraining the search
     * @return a <code>DeclarationSet</code> value with matching 
     * <code>Declarations</code>.
     */
    protected DeclarationSet qualifiedLookup (ScopedName name, LFlags flags){
       try { // see comment in unqualified version above
           if (false) throw new UndefinedSymbolException(name);
           if (flags.intersects(Java_LFlags.TYPE)) return lookupType(name, flags);
           if (flags.intersects(Java_LFlags.FUNCTION)) return lookupFunction(name, flags);
           if (flags.intersects(Java_LFlags.EXPRESSION)) return lookupExpression(name, flags);
           if (flags.intersects(Java_LFlags.AMBIGUOUS)) return lookupAmbiguous(name, flags);
           if (flags.intersects(Java_LFlags.PACKAGE) ) return lookupPackage(name, flags);
           // Although not required for name resolution, we do need to to be able to check if a CU exists
           if (flags.intersects(Java_LFlags.COMPILATION_UNIT)) return lookupCU(name, flags);
           Assert.check("Got to lookup something");
           return null;
       } catch (UndefinedSymbolException e) { // This satisfies the compiler as we over-ride lookups
           return null;
       }
    }
    
   protected DeclarationSet lookupPackage(ScopedName name, LFlags flags) {
       d.msg(Debug.COMPILE, "qualified lookupPackage("+name.getName()+", "+flags.toString()+") in " + toString());
        return SHProgram.getProgramSH().lookupPackage(name, flags);   // There's no point in looking in low-level SH's
    }

   protected DeclarationSet lookupType(ScopedName name, LFlags flags) {
       d.msg(Debug.COMPILE, "qualified lookupType("+name.getName()+", "+flags.toString()+") in " + toString());
       if (!name.is_qualified()) return lookupType(name.getName(), flags);
       Java_ScopedName packageOrType = (Java_ScopedName)name.clone();
       String type = packageOrType.getTerminalId();
       packageOrType.removeTerminalId();
       DeclarationSet possibles = lookupType(packageOrType, flags);  // recurse
       if (possibles.isEmpty())  // packageOrType must be a package
	       possibles = SHProgram.getProgramSH().lookup(name, flags);
       else { // We've found a type
	       Declaration possible = possibles.getSingleMember();
	       Assert.error(possible != null, multipleDecs + "for type " + name.getName());
	       SHCommon target = (SHCommon)possible.getDefinition();
	       possibles = target.lookupType(type,flags);
       }
       return possibles;
    }

   /**
    * Java name resolution given a qualified function name.
    * @param name the qualified name in the form ambiguousName.id
    * @param flags an <code>LFlags</code> value constraining the search
    * @return a <code>DeclarationSet</code> value with matching 
    * <code>Declarations</code>.
    */
   protected DeclarationSet lookupFunction(ScopedName name, LFlags flags) {
       d.msg(Debug.COMPILE, "qualified lookupFunction("+name.getName()+", "+flags.toString()+") in " + toString());
 /* 2005.04.07: I think this is just plain wrong
	   String id = name.getTerminalId();
// was lookupExpression - must be error - only worked because lookupExpression & lookupFunction currently identical!!! 
        if (!name.is_qualified()) return lookupFunction(id, flags);
        Assert.check("Lookup shouldn't be called on qualified function names.");
        return null;
 */
       String id = name.getTerminalId();
       String descriptor = "method " + id;
       ScopedName clone = (ScopedName)name.clone();
       clone.removeTerminalId();
       LFlags category = new LFlags(Java_LFlags.EMPTY);
       DeclarationSet possibles = lookupAmbiguous(clone, category);
       switch (flags.getRawVal()){ // Section 6.5.7.2 of Java spec
	       case Java_LFlags.PACKAGE:{
		       Assert.error(descriptor + " can't be qualified by a package name (" +
					clone.getName() + ")");  
		       break;
	       }
	       case Java_LFlags.TYPE: {  // static method of type name
		       descriptor += " in type " + clone.getName();
		       Declaration declaration = possibles.getSingleMember();
		       Assert.check(declaration != null,"No single declaration for type" + clone.getName());
		       possibles = ((SHCommon)declaration.getScopeHolder()).lookupFunction(id, category);
		       Assert.error(possibles != null, "No matches for " + descriptor);
		       possibles = winnowOnSpecifier(possibles,Java_Specifiers.SP_STATIC);
		       Assert.error(possibles != null, "No static matches for " + descriptor);
		       break;
	       	
	       }
	       /*  let T be the type of the expression Q; Id must name at least one
	        * method of the type T. */
	       case Java_LFlags.EXPRESSION:{
		       descriptor += " in expression " + clone.getName();
		       Declaration declaration = possibles.getSingleMember();
		       TypeNode expType = declaration.getType();
		       // Now how do I get the scopeHolder - I'm sure I just solved this!!!
		       //expType.
		       break;
	       }
       }
       
       
       return possibles;
	   //((SHType)getOwnDeclaration().getClassScope()).lookupFunction(name, flags);
    }
    
    protected DeclarationSet lookupExpression(ScopedName name, LFlags flags) {
       d.msg(Debug.COMPILE, "qualified lookupExpression("+name.getName()+", "+flags.toString()+") in " + toString());
        String id = name.getTerminalId();
        if (!name.is_qualified()) return lookupExpression(id, flags);
        Assert.check("Lookup shouldn't be called on qualified expression names.");
        return null;
//        name.removeTerminalId();
//        DeclarationSet possible = lookupAmbiguous(name, flags);
//        Declaration qualifier = possible.getSingleMember();
//        Assert.error(qualifier!= null, "Unable to disambiguate " + name.getName());
/* At this point we are governed by JLS 6.5.6.2. This code implements part of it. In keeping
 * with other lookups it does not apply accessability rules (or any rules that follow them).
 * Should we change this? 
 */
//        Assert.error(!qualifier.getCategory().intersects(Java_LFlags.PACKAGE_LF),
//            "Can't have variable " + id + " in package " + qualifier.getName().getName());
//        DeclarationSet target = null;
//        if (qualifier.getCategory().intersects(Java_LFlags.TYPE_LF))
//            target = ((SHCommon)qualifier.getDefinition()).lookupExpression(id, flags);
//        else if (qualifier.getCategory().intersects(Java_LFlags.EXPRESSION_LF)) {
//            TypeNode type = qualifier.getType();
//            Assert.error( type instanceof TyRef,"Qualifier " + qualifier.getName().getName() +
//                " is not a reference type");
//            type.g
//        }
//        d.msg("Found " + qualifier.toString() + ", now look for " + id +
//            " in " + (SHCommon)qualifier.getDefinition());
//        name.append(id);    // restore name
//        return ((SHCommon)decl.getDefinition()).lookupExpression(id, flags);
    }
    
/**
 * Default Java id resolution for an ambiguous identifier. Recursive. Since looking up
 * ambiguous names involves classifying them, LFlags is actually an output
 * @param name The <code>JavaScopedName</code> to be resolved
 * @param flags an <code>LFlags</code> value to be defined by the search
 * @return a <code>DeclarationSet</code> value with matching 
 * <code>Declarations</code>.
 * @exception UndefinedSymbolException not thrown in the default lookup as not all lookups are expected to be successful
 */
    protected DeclarationSet lookupAmbiguous(ScopedName name, LFlags flags) {
        d.msg(Debug.COMPILE, "qualified lookupAmbiguous("+name.getName()+", "+ flags.toString() +
            ") in " + toString());
        DeclarationSet ambiguous = null;
        Declaration decl = null;
        if (!name.is_qualified()) {
            ambiguous = lookupAmbiguous(name.getName(),flags);
            if (!ambiguous.isEmpty())
            	Assert.error(ambiguous.getSingleMember()!=null,
            		multipleDecs + "in ambiguous lookup");
        } else {
            DeclarationSet possibles = null;
            SHCommon scope = null;
            String id = name.getTerminalId();
            ScopedName clone = (ScopedName)name.clone();
            clone.removeTerminalId();
            ambiguous = lookupAmbiguous(clone, flags);
            switch (flags.getRawVal()){
                case Java_LFlags.PACKAGE:{
 /* This piece of code is particularly tricky. Normally the lookup would actually
  * do a lookup. In this one case however, only the classification has been done
  * so ambiguous is an empty set. In the first instance this was done because the
  * standard suggested it in section 6.5.2 in the part about the reclassification
  * of simple ambiguous names, vis. -
  * 
  * "If the name to the left of the "." is reclassified as a PackageName, then if
  * there is a package whose name is the name to the left of the "." and that
  * package contains a declaration of a type whose name is the same as the Identifier,
  * then this AmbiguousName is reclassified as a TypeName. Otherwise, this AmbiguousName
  * is reclassified as a PackageName. A later step determines whether or not a package
  * of that name actually exists."
  * 
  *  It has been kept because if name has been classified
  * as a packageName, id could be either a type or a package. Thus we first lookup
  * Type id in package name. If we can't find it, then id is a packageName, but
  * we don't look it up because we want to lookup name name.id and id is only added
  * back into name at the end of this routine. Thus we classify, but don't lookup
  * deferring that to the next step in the recursion (which is consistent with what
  * is done in classifying a simple ambiguous name as a package). There. Clear as mud!
  * 
  * As a result, job 1 is looking up package name
  */
                	SHProgram programSH = SHProgram.getProgramSH();
                	ambiguous = programSH.lookupPackage(clone, flags);   // There's no point in looking in low-level SH's                	
            		if (ambiguous.isEmpty()){ // Package not yet encountered
            			programSH.createPackageScope(clone, null, null);
            			ambiguous = programSH.lookupPackage(clone, flags);
            		}
                    decl = ambiguous.getSingleMember();
                    Assert.error(decl!=null,multipleDecs + "for package " + name.getName());
                    flags.classify(Java_LFlags.TYPE);
                    possibles = ((SHPackage)decl.getDefinition()).lookupType(id, flags);
                    if(possibles.isEmpty()){
                        flags.classify(Java_LFlags.PACKAGE);
                    } else ambiguous = possibles;
               }
                break;
                case Java_LFlags.EXPRESSION:
                case Java_LFlags.TYPE:{
                	/** If the name to the left of the "." is reclassified as a TypeName,
                	 * then if the Identifier is the name of a method or field of the
                	 * class or interface denoted by TypeName, this AmbiguousName is
                	 * reclassified as an ExpressionName. Otherwise, if the Identifier
                	 * is the name of a member type of the class or interface denoted by
                	 * TypeName, this AmbiguousName is reclassified as a TypeName.
                	 * Otherwise, a compile-time error results.
                	 * 
                	 * If the name to the left of the "." is reclassified as an ExpressionName,
                	 * then let T be the type of the expression denoted by ExpressionName. If
                	 * the Identifier is the name of a method or field of the class or interface
                	 * denoted by T, this AmbiguousName is reclassified as an ExpressionName.
                	 * Otherwise, if the Identifier is the name of a member type (§8.5, §9.5)
                	 * of the class or interface denoted by T, then this AmbiguousName is
                	 * reclassified as a TypeName. Otherwise, a compile-time error results.
                	 */
                    decl = ambiguous.getSingleMember(); // Should point to the type in the symTab
                    Assert.check(decl!= null, multipleDecs + "in disambiguating " + name.getName());
                    scope = (SHCommon)decl.getClassScope();
                    possibles = scope.lookupFunction(id, new LFlags(Java_LFlags.METHOD));
                    if (possibles.isEmpty())
                        possibles = scope.lookupExpression(id, new LFlags(Java_LFlags.FIELD_VARIABLE));
                    if (!possibles.isEmpty()){
                        flags.classify(Java_LFlags.EXPRESSION);
                        ambiguous = possibles;
                    }
                    else {
                        possibles = scope.lookupType(id,new LFlags(Java_LFlags.TYPE));
                        if (possibles.isEmpty())
                            Assert.error("Can't disambiguate " + clone.getName());
                        ambiguous = possibles;
                    }
                }
                break;
                
                 default:
                    Assert.check("Erroneous classification flag in lookupAmbiguous");
            }
        }
        return ambiguous;
    }
 
    
    /**
     * Although not required for name resolution, we do need to to be able to check if a CU exists
     * t
     * @param name The <code>JavaScopedName</code> to be resolved
     * @param flags an <code>LFlags</code> value which should be Java_LFlags.COMPILATION_UNIT
     * @return a <code>DeclarationSet</code> value with matching 
     * <code>Declarations</code>.
     */
        protected DeclarationSet lookupCU(ScopedName name, LFlags flags) {
        	return getPackage().lookupCU(name.getName(),flags);
        }
 
    
/* Winnows declarations that do not contain the required specifier
 * out of a declaration set
 */    
    private DeclarationSet winnowOnSpecifier(DeclarationSet possibles, int specifier){
    	return winnow(possibles, specifier, null);
    }
    
/* Winnows declarations that do not contain the required flag(s)
 * out of a declaration set
 */    
    private DeclarationSet winnowOnFlags(DeclarationSet possibles, LFlags flags){
    	return winnow(possibles, -1, flags);
    }

    private DeclarationSet winnow(DeclarationSet possibles, int specifier, LFlags flags){
    	DeclarationSet matches = new DeclarationSetMulti();
    	if (possibles == null) return matches;   // may return null from inside localLookup
        Enumeration iterator = possibles.elements();
        while (iterator.hasMoreElements()) {
            Declaration possible = (Declaration) iterator.nextElement();
            boolean keeper = flags == null ? possible.hasSpecifier(specifier) :
            	possible.getCategory().intersects(flags);
            if (keeper)
        		matches.merge(possible);
        }
        return matches;
    }
    
    
    public boolean isAccessible(Declaration decl){
        return isAccessible(this, decl);
       }

    // Note. Although this was set up for a more general case,
    // so far it has never been used
    public boolean isAccessible(SHCommon target, Declaration decl){
        return isAccessible(target, (SHCommon)decl.getEnclosingBlock(),
            new Access(decl.getSpecifiers()));
    }
    

    /* are elements from the target scope which are ranked access, accessible from the from scope?*/   
    private boolean isAccessible(SHCommon from, SHCommon target, Access access){
        // 
        d.msg(Debug.COMPILE, "Checking accessibility of " + target.toString() + " from " + from.toString() +
                        " for access " + access.toString());
        boolean accessible = access.isAccessible();
        if (accessible){    // Anything but private
            SHCommon base = target.getBaseScope();  // Get the next base scope up
            if (base == target) base = ( (SHCommon)target.getEnclosingScope()).getBaseScope();
            if (base instanceof SHPackage)
                accessible = (access.getAccess() == Access.ISpUBLIC) ||  // public is always accessible
                    (from.getPackage() == base);   // at package level package and protected have to be in same package
            else if (base instanceof SHType) {
                d.msg(Debug.COMPILE, "Base scopeholder: " + base.toString() + "  from: " + from.toString());
                SHType fromClass = (SHType)from.getClassDeclaration().getDefinition();
                if (base == fromClass) accessible = true;
                else if(((SHType)base).inheritsFrom(fromClass))
                    accessible = access.getAccess()<= Access.ISpROTECTED;
                else {
                    access.modify((Java_SpecifierSet)base.getOwnDeclaration().getSpecifiers());
                    accessible = isAccessible(from, base, access);
                }
            }
        }
        else {
            accessible = (from.getClassDeclaration() == target.getClassDeclaration());
        }
        return accessible;
    }
    

    
 /* Service routine to create a new ScopedName that is a subset of the given name.
  * Assertion: name is completed and its index is set to the point at which the subset is to start
  * @parameter name the base name
  * @return a new completed ScopedName that contains all components in the base name from the place where index points to the terminalID
  */
    
/*    ScopedName innerName(ScopedName name){
        ScopedName inner = new Java_ScopedName(name.selectedPart());
        while (!name.index.terminal()){
            name.index.advance();
            inner.append(name.selectedPart());
        }
        inner.completed();
        return inner;
    }*/

    
    /** Standard method to provide a description
     * @return a string representing the type of scope
     */    
    public String toString(){return ownDeclaration != null ? ownDeclaration.getName().getName() : "";}
     
}


/* The accessability of something depends not only upon its declaration but
 * also upon from where it is trying to be accessed. For example, a public method
 * of a package level class effectively has package access when viewed from
 * outside the class. This class represents modifiable access objects whose
 * privacy can always be increased.
 **/
class Access {
    // Access hierarchy. Higher numbers => greater privacy, less access
    static final int ISpUBLIC = 0;
    static final int ISpROTECTED = 1;
    static final int ISpACKAGE = 2;
    static final int ISpRIVATE = 3;
    private Java_SpecifierSet access;
    
    
    Access(SpecifierSet s) { // Start access object as declared
        access = (Java_SpecifierSet)s;
    }
    
    boolean isAccessible() { return (getAccess() != ISpRIVATE);}
    
    int getAccess(){
        return getAccess(access);
    }

    // return the inherent accessability of specifierSet s
    int getAccess(Java_SpecifierSet s){
        int a = ISpACKAGE;  // default for no specification
        if (s.contains(Java_Specifiers.SP_PRIVATE))
            a = ISpRIVATE;
        else if (s.contains(Java_Specifiers.SP_PROTECTED))
            a = ISpROTECTED;
        else if (s.contains(Java_Specifiers.SP_PUBLIC))
            a = ISpUBLIC;
        return a;
    }

    // modify access by specifierSet mod. Can only increase privacy
    void modify( Java_SpecifierSet mod){
        if (getAccess(mod) > getAccess())
            access = mod;
    }
    
    public String toString(){
        String description = "";
        switch (getAccess()){
            case ISpUBLIC:
                description = "public access";
                break;
            case ISpROTECTED:
                description = "protected access";
                break;
            case ISpACKAGE:
                description = "package access";
                break;
            case ISpRIVATE:
                description = "private access";
                break;
        }
        return description;
    }
}


