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

import tm.clc.datum.AbstractDatum;
import tm.interfaces.TypeInterface;
import tm.utilities.Assert;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

/** Abstract class representing run type nodes. There should be one type node for
 * each distinct type in the project
 */
public abstract class TypeNode implements TypeInterface, Cloneable {

    
    /** Add modifier to end of modifier chain for this node
     * @param l the modifier node to be added to the end
     */    
    public void addToEnd( TypeNode l ) {
        Assert.check(false) ; } // Only applicable to type modifiers
    
        /** Make a Datum of this type which is a member of the referenced parent Datum
         * @param vms The virtual machine state.
         * @param addresss the location in memory of this Datum
         * @param parent the Datum to which this Datum belongs.
         * @param name name of the Datum
         * @return a new Datum of this type
         */        
    public abstract AbstractDatum makeMemberDatum(VMState vms,
                                                  int addresss,
                                                  AbstractDatum parent,
                                                  String name ) ;

        /** Make a Datum of this type
         * @return a new Datum of this type
         * @param mr The memory region in which this Datum is to be created
         * @param vms  The virtual machine state.
         * @param name name of the Datum
         */        
    public abstract AbstractDatum makeDatum(VMState vms,
	                                        MemRegion mr,
	                                        String name) ;

    /** Return the number of bytes of memory occupied by Datums of this type
     * @return the size of Datums of this type, in bytes
     */    
    public abstract int getNumBytes() ;
	
	/** A user friendly string identifying the type.
         *  For user display, may leave out some information
         * @return the user-friendly identifier string
         */
	abstract public String getTypeString() ;
        
	/** The fundamental underlying type, generally this except for
         *  pointer & reference types (Added 2003.08.22 by mpbl)
         * @return the fundamental underlyng type
         */
        public TypeNode getBaseType(){
            return this;
        }
	
	/** typeId is the language defined way of writing the type.
         *      In C++, this follows the ISO syntax category type-id
         * @return the formal id string
         */
	abstract public String typeId() ;
	
	private int attributes = 0 ;
	
        /** attach an integer attribute to this type
         * @param attr the attribute to be attached
         */        
	public void setAttributes( int attr ) {
	    attributes = attr ; } ;
	
            /** retrieve the attribute
             * @return the attached attribute
             */            
	public int getAttributes() {
	    return attributes ; }

        /** <p>Make a clone of this type node if cloning is supported.</p>
         * <B>Theo. This is dumb! TypeNode implements clonable, which is simply a policy
         * statement. Make a policy statement, stick to it.</B>
         * @return the clone, if it is available
         */        
	public TypeNode getClone () { 
		TypeNode copy = null;
		try { 
			copy = (TypeNode) this.clone ();
		} catch (CloneNotSupportedException e) {
			Assert.apology (e.getMessage ());
		}
			
		return copy;
	}
        
        /**
         * @return a unique description of this type node
         */        
        public String toString(){return "type node " + typeId() + " (" + getTypeString() + ")";}
}
