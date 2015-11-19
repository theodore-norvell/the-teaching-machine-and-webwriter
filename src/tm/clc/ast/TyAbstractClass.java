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

package tm.clc.ast;


import java.util.Vector ;

import tm.backtrack.BTTimeManager;
import tm.clc.analysis.Declaration;
import tm.clc.analysis.ScopedName;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractObjectDatum;
import tm.interfaces.TypeInterface;
import tm.utilities.Assert;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;

/** A common class for representing class and struct types also used for Java array types
 */
public abstract class TyAbstractClass extends TypeNode {
    
    // defined_flag -- True if a definition is known for the class.
    private boolean defined_flag ;
    
    // super_classes -- A Vector of TyClasses
    private Vector super_classes ;
    
    // fields -- A Vector of VarNodes
    private Vector fields ;
    
    // size -- number of bytes required.
    private int size = -1;
    
    // name for display purposes only
    private String typeString ;
    
    private int numberOfFieldsIncludingSubObjects = -1;
    
    private VirtualFunctionTable vfn = new VirtualFunctionTable() ;
    
    /** Constructor
     * @param nm the simple name of the type
     */
    public TyAbstractClass(String nm) {
        super() ;
        typeString = nm ;
        defined_flag = false ;
        super_classes = new Vector() ;
        fields = new Vector() ;
    }
    
    /** @see TypeNode
     */
    public String getTypeString() { return typeString ; }
    
    /** Has the class been fully defined. I.e. all fields and superclasses have been added.
     * @return true if the class has been fully defined.
     */
    public boolean isDefined () {
        return defined_flag ;
    }
    
    /** Affirm that no more super classes or fields will be added. */
    public void setDefined() {
        defined_flag = true ; }
    
    
    /** Add a super class. The superclass need not be
     *  defined when added.
     * @param superClass the super class to be added
     */
    public void addSuperClass( TyAbstractClass superClass ) {
        Assert.check( ! defined_flag ) ;
        super_classes.addElement( superClass ) ;
    }
    
    /** Add a field to the class
     * @param field The field to be added
     */
    public void addField( VarNode field ) {
        Assert.check( ! defined_flag ) ;
        fields.addElement( field ) ; }
    
    /** How many super classes are there for this class
     * @return the number of super classes
     */
    public int superClassCount() {
        return super_classes.size() ; }
    
    /** How many fields are there in this class
     * @return the field count
     */
    public int fieldCount() {
        return fields.size() ; }
    
    /** fetch a reference to a particular superclass
     * @param i the number of the superclass
     * @return a reference to the desired superclass
     */
    public TyAbstractClass getSuperClass(int i) {
        return (TyAbstractClass) super_classes.elementAt(i) ; }
    
    /** fetch a reference to a specific field of this class
     * @param i the number of the field
     * @return a reference to the field
     */
    public VarNode getField(int i) {
        return (VarNode) fields.elementAt(i) ; }
    
    /** Fetches the total number of bytes of memory required for objects of this class
     * Assertion: All super classes must be defined before this routine is
     * called the first time.
     * @return the size of objects, in bytes
     */
    public int getNumBytes() {
        Assert.check( defined_flag ) ;
        // Size is calculated lazily.  This allows
        // super classes to be added that are not
        // defined. However the super classes must 
        // all be defined when this routine is
        // called the first time.
        if( size == -1 ) {
            size = 0 ;
            for( int i=0, sz=superClassCount() ; i < sz ; ++i ) {
                size += getSuperClass(i).getNumBytes() ; }
            for( int i=0, sz=fieldCount() ; i < sz ; ++i ) {
                size += getField(i).get_type().getNumBytes() ; }
        }
        
        return size ; }
    
    /** Get the total number of fields in objects of this class (including fields of the
     * superclasses).
     * Assertion. All superclasses must be fully defined before this method is called
     * the first time
     * @return the number of fields
     */
    public int getNumberOfFieldsIncludingSubObjects() {
        Assert.check( defined_flag ) ;
        // The number of fields is calculated lazily.  This allows
        // super classes to be added that are not
        // defined. However the super classes must be
        // defined all be defined when this routine is
        // called the first time.
        if( numberOfFieldsIncludingSubObjects == -1 ) {
            numberOfFieldsIncludingSubObjects = 0 ;
            for( int i=0, sz=superClassCount() ; i < sz ; ++i ) {
                numberOfFieldsIncludingSubObjects
                += getSuperClass(i).getNumberOfFieldsIncludingSubObjects() ; }
            numberOfFieldsIncludingSubObjects+=fieldCount() ;
        }
        return numberOfFieldsIncludingSubObjects ; }
    
    /** Given a path through the subobject tree an fully qualified name for a field, compute its position
     * @return the position, or -1, in case the position couldn't be found.
     * @param path the path through the subobject tree
     * @param sc_name fully qualified field name
     */
    public int convertPathAndScopedNameToPosition( int[] path, ScopedName sc_name ) {
        return convertPathAndScopedNameToPosition( 0, path, sc_name ) ; }
    
    private int convertPathAndScopedNameToPosition( int k, int[] path, ScopedName sc_name ) {
        Assert.check( defined_flag ) ;
        if( k == path.length ) {
            int c = getNumberOfFieldsIncludingSubObjects() ;
            int i = fields.size() ;
            while( true ) {
                c -= 1 ;
                i -= 1 ;
                if( i < 0 ) break ;
                VarNode vn = (VarNode) fields.elementAt(i) ;
                if( vn.getName().equals( sc_name ) ) break ; }
            if( i < 0 ) return -1 ;
            return c ; }
        else {
            // Get the position within the subobject.
            Assert.check( 0 <= path[k] && path[k] < super_classes.size() ) ;
            TyAbstractClass super_class = (TyAbstractClass) super_classes.elementAt(path[k]) ;
            int c = super_class.convertPathAndScopedNameToPosition( k+1, path, sc_name ) ;
            if( c == -1 ) return -1 ;
            // Add to that the number of fields in earlier subobjects
            for( int i=0 ; i < path[k] ; ++i ) {
                TyAbstractClass earlier_super_class = (TyAbstractClass) super_classes.elementAt(i) ;
                c += earlier_super_class.getNumberOfFieldsIncludingSubObjects() ; }
            Assert.check( 0 <= c && c < getNumberOfFieldsIncludingSubObjects() ) ;
            return c ; } }
    
    
    /** Create a datum. This is a template method since the
     * type of the datum depends on the concrete class, we defer
     * this part of making a datum to the concrete class.
     * @param add address
     * @param p parent datum
     * @param m the memory
     * @param n the name of the datum
     * @return
     */
    protected abstract AbstractObjectDatum constructDatum(int add, AbstractDatum p, Memory m, String n, BTTimeManager timeMan) ;
    
    /** @see TypeNode
     */
    public AbstractDatum makeMemberDatum( VMState vms,
            int address,
            AbstractDatum parent,
            String name ) {
        /*DBG System.out.println("TyAbstractClass.makeMemberDatum at "+address) ;*/
        Assert.check( defined_flag ) ;
        // Create the object
        AbstractObjectDatum d = constructDatum( address, parent,
                vms.getMemory(), name, vms.getTimeManager() ) ;
        
        int birth_order = 0 ;
        int	currentAddress = address ;
        
        // Create and add direct subobjects generated by the direct superclasses
        for( int i=0, sz= super_classes.size() ; i<sz ; ++i ) {
            TyAbstractClass subobject_type = getSuperClass( i ) ;
            String subobject_name = subobject_type.getTypeString() ;
            // @todo following line is C++ specific.
            AbstractDatum subobject = subobject_type.makeMemberDatum
                ( vms, currentAddress, d,
                    name+"::"+subobject_name ) ;
            d.addSubObject( i, birth_order, (AbstractObjectDatum) subobject ) ;
            currentAddress += subobject_type.getNumBytes() ;
            birth_order += 1 ; }
        
        // Create and add the direct fields
        for( int i=0, sz=fieldCount() ; i<sz ; ++i ) {
            /*DBG System.out.println("Adding a field at "+fieldAddress) ; */
            TypeNode fieldType = getField(i).get_type()  ;
            String fieldName = getField(i).getName().getUnqualifiedName() ;
            // @todo following line is C++ specific.
            AbstractDatum f = fieldType.makeMemberDatum(vms, currentAddress, d,
                    name+"."+fieldName) ;
            d.addField( i, birth_order, f ) ;
            currentAddress += fieldType.getNumBytes() ;
            birth_order += 1 ; }
        
        // Return the datum
        return d ; }
    
    /** @see TypeNode
     */
    public AbstractDatum makeDatum(VMState vms, MemRegion mr, String name ) {
        int address = mr.findSpace( getNumBytes() ) ;
        AbstractDatum d = makeMemberDatum( vms, address, null, name ) ;
        vms.getStore().addDatum(d) ;
        return d ; }
    
    public VirtualFunctionTable getVirtualFunctionTable() {
        return vfn ;
    }
}
