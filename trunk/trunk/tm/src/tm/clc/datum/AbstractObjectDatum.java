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

package tm.clc.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.AbstractFunctionDefn;
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TypeNode ;
import tm.clc.ast.VirtualFunctionTable;
import tm.interfaces.Datum;
import tm.utilities.Assert;
import tm.utilities.Visitor;
import tm.virtualMachine.Memory;


/*=========================================================================
Class: AbstractObjectDatum

Overview:
This class represents an structured data item stored in memory.
===========================================================================
*/ 
 

 public abstract class AbstractObjectDatum extends AbstractDatum  {
    private static final int OUTER_OBJECT = -1;
    
     /** Subobjects are representatives of the base classes of an object */
     private   AbstractObjectDatum [] subObject ;
     private   AbstractObjectDatum superObject ;
     /** Outer objects are used for Java's inner classes. */
     private   AbstractObjectDatum outerObject ;
     private   AbstractDatum [] field ;
     protected TyAbstractClass type ;
    
    /** Creates a struct datum. Immediately after construction all
      * subobjects and fields should be added, subobjects first and then
      * fields.
      */
    public AbstractObjectDatum(int add, Datum p, Memory m, String n, TyAbstractClass tp, BTTimeManager timeMan) {
        this(add, p, m, n, tp, 0, timeMan);
    }
    
    /** Creates a struct datum. Immediately after construction all
     * subobjects and fields should be added, subobjects first and then
     * fields.
     */
   public AbstractObjectDatum(int add, Datum p, Memory m, String n, TyAbstractClass tp, int numberOfElements, BTTimeManager timeMan) {
        super(add,0,p, m, n, tp, timeMan);
        type = tp ;
        subObject = new AbstractObjectDatum[ tp.superClassCount() ] ;
        field = new AbstractDatum[ tp.fieldCount() + numberOfElements ] ; }
   
   protected AbstractObjectDatum(AbstractObjectDatum original){
	   super(original);
	   // watch out for superObject
	   Assert.check(original.outerObject == null, "Sorry, can't clone inner objects");
	   type = original.type;
	   superObject = null;
	   subObject = new AbstractObjectDatum[original.subObject.length];
	   for (int i = 0; i < subObject.length; i++){
		   subObject[i] = (AbstractObjectDatum)original.subObject[i].copy();
		   if (original.superObject==original.subObject[i]) superObject = subObject[i];
	   }
	   field = new AbstractDatum[original.field.length];
	   for (int j = 0; j < field.length; j++)
		   field[j] = (AbstractDatum)original.field[j].copy();
   }
    
    /** This method should be called immediately after construction.
      * Subobjects must be added before fields.
      * Subobjects must be added in ascending order of
      * address.
      */
    public void addSubObject(int i, int birth_order, AbstractObjectDatum s) {
        Assert.check( subObject[i] == null ) ;
        Assert.check( s.getParent() == this ) ;
        subObject[i] = s ;
        s.setBirthOrder(birth_order);
        s.superObject = this ;
        size += s.getNumBytes() ;
    }
    
    /** Set the outer object. Should be called for objects of inner classes in Java.
     * 
     * @param oo
     */
    public void setOuterObject(AbstractObjectDatum oo) {
        outerObject = oo ;
    }
    
    /** getOuterObject
     * 
     * @return The outer object if any. Otherwise returns null.
     */
    public AbstractObjectDatum getOuterObject() {
        return outerObject ;
    }

    /** This method should be called immediately after the subobjects
      * have all been added.
      * Fields must be added in ascending order of
      * address.
      */
    public void addField(int i, int birth_order, AbstractDatum f) {
        /*DBG System.out.println("Adding field at "+f.address) ;*/
        Assert.check( field[i] == null ) ;
        Assert.check( f.getParent() == this ) ;
        field[i] = f ;
        f.setBirthOrder(birth_order);
        size += f.getNumBytes() ;
        /*DBG System.out.println("Size is now"+size) ; */
    }
    
    public void defaultInitialize(){
    	for(int i = 0; i < subObject.length; i++)
    		subObject[i].defaultInitialize();
    	for(int i = 0; i < field.length; i++)
    		field[i].defaultInitialize();
    }
    
    public AbstractObjectDatum getSubObject( int[] path ) {
        return getSubObject( path, 0 ) ; }
        
    protected AbstractObjectDatum getSubObject( int[] path, int i ) {
        if( i==path.length ) {
            return this ; }
        else if( path[i] == OUTER_OBJECT ) {
            return outerObject.getSubObject( path, i+1 ); }
        else {
            Assert.check( 0 <= path[i] && path[i] < subObject.length ) ;
            return subObject[path[i]].getSubObject( path, i+1 ) ; } }
    
    public AbstractDatum getFieldByName(int[] path, ScopedName fieldName) {
        return getSubObject( path ).getFieldByName( fieldName ) ; }
        
    public int numberOfFields() {
        return field.length ; }
        
    public String getDisplayNameForField( int i ) {
        return type.getField( i ).getName().getUnqualifiedName() ; }
        
    public AbstractDatum getField( int i ) {
        return field[i] ; }
        
    public AbstractDatum getFieldByName(ScopedName fieldName) {
        for( int i=0, sz=type.fieldCount() ; i<sz ; ++i) {
            if( fieldName.equals( type.getField(i).getName() ) ) {
                return field[i] ; } }
        Assert.apology( "Field "+fieldName+" not found in object " + toString() ) ;
        return null ; }

    /* The StructureInterface is implemented below */
    
    /** How many sub objects and fields are there  */
    public int getNumChildren() {return subObject.length + field.length ; }
    
    /** What is the name of sub object or field i (counted from 0) */
    public String getChildLabelAt(int i) {
        if( 0 <= i && i < subObject.length ) {
            return type.getSuperClass(i).getTypeString() ; }
        else if( i < subObject.length + field.length ) {
            return getDisplayNameForField( i - subObject.length ) ; }
        else {
            Assert.check(false) ; 
            return null; } }
    
    // What is field or subobject i (counted from 0)
    public Datum getChildAt( int i ) {
        if( 0 <= i && i < subObject.length ) {
            return subObject[ i ] ; }
        else if( i < subObject.length + field.length ) {
            return field[ i - subObject.length ] ; }
        else {
            Assert.check(false) ; 
            return null; } }
     
     public void walkSubObjectTree( Visitor<AbstractObjectDatum> visitor ) {
        for( int i=0 ; i < subObject.length ; ++i ) {
            subObject[i].walkSubObjectTree( visitor ) ; }
        visitor.visit( this ) ; }
     
     /** Return the root of this subobject hierarchy */
     public AbstractObjectDatum rootObject() {
         if( superObject == null ) return this ;
         else return superObject.rootObject() ;
     }
     
     /** Lookup a virtual function in the virtual function table.
      * The algorithm is a dfs search through the sub-object hierarchy.
      * @return null if no definition is found in the hierarchy.
      *         Otherwise a pair consisting of the definition together with subobject. */
     public VirtualFcnDescription findVirtualFunctionDefn( Object key ) {
         VirtualFunctionTable vfn = type.getVirtualFunctionTable() ;
         Assert.check( vfn != null ) ;
         AbstractFunctionDefn defn = vfn.get( key  ) ;
         if( defn != null ) return new  VirtualFcnDescription( defn, this);
         for( int i=0 ; i < subObject.length ; ++i ) {
             VirtualFcnDescription result = subObject[i].findVirtualFunctionDefn( key  ) ;
             if(result != null ) return result ; }
         return null ;
     }
     
     static public class VirtualFcnDescription {
         VirtualFcnDescription(AbstractFunctionDefn defn, AbstractObjectDatum subObj ) {
             this.defn = defn ;
             this.subObj = subObj ;
         }
         public AbstractFunctionDefn defn ;
         public AbstractObjectDatum subObj ;
     }
}