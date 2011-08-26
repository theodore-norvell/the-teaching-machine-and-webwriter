package tm.clc.datum;

/**
Class: AbstractDatum

Overview:
This class makes concrete the part of the Datum Interface that is common
to all Datums, irrespective of Type.
*/ 

import tm.backtrack.BTVar;
import tm.backtrack.BTTimeManager;
import tm.clc.ast.TypeNode;
import tm.interfaces.Datum;
import tm.interfaces.TypeInterface;
import tm.utilities.Assert;
import tm.virtualMachine.Console;
import tm.virtualMachine.Memory;
import tm.virtualMachine.PropertyList;
import tm.virtualMachine.VMState;

public abstract class AbstractDatum extends PropertyList implements Datum {
	protected int address;
	protected Datum parent;
	protected int size;
	protected Memory mem ;
	protected TypeNode type ;
	protected String name ;
	protected BTVar highlight ;
	protected int birthOrder = 0;

	protected AbstractDatum(int add, int s, Datum p, Memory m, String n,
	                       TypeNode tp, BTTimeManager timeMan) {
		address = add;
		size = s;
		parent = p;
		mem = m ;
		type = tp ;
		name = n ;
        highlight = new BTVar(timeMan) ;
        highlight.set( new Integer( Datum.PLAIN ) ) ;
	}

// These methods are common to all Datums

	public int getNumBytes() { return size;}

	public Datum getParent(){return parent;}

    /* This must be fixed. The highlight must be backtrackable */
    public void putHighlight( int h ) {
        highlight.set( new Integer( h ) ) ; }
    
    public int getHighlight() {
        return ((Integer)highlight.get()).intValue() ; }
    
	public int getAddress() {return address;}

	public int getByte(int i){
		Assert.check( 0 <= i && i < size ) ;
		return mem.getByte(address+i) ;}
    
    public void putByte(int i, int val){
		Assert.check( 0 <= i && i < size ) ;
		mem.putByte( address+i, (byte)val ) ;}
		
	public TypeInterface getType() { return type ; }
        
	public void output( VMState vms ) {
		String str = getValueString() ;
		Console console = vms.getConsole() ;
		for(int i = 0, sz = str.length(); i < sz ; ++i ) {
			console.putchar( str.charAt( i ) ) ; } }

    /* Input from cin as performed by the >> operator.
       Returns true if the operation completed and false if
       more input characters are required from the user.
    */
    public boolean input( VMState vms ) {
        Assert.apology("Input is not defined for this type" ) ;
        return true ; }
        
    public String toString() {
        return "{"+ getName() + " at address " + address
             + " has type " + getTypeString()
             + " has value " + getValueString() +"}" ; }

    /** getName -- Return the name of the datum */
    public String getName() { return name ; }

	/** getTypeString --- Return a string suitable for display representing the type */
	public String  getTypeString() {return type.getTypeString() ; }


	/** What number child of the parent is this? Starting from 0. */
	public int getBirthOrder(){
	    Assert.check(parent != null);
	    return birthOrder;
	}
	
	/** Set the birth-order. */
	public void setBirthOrder(int i){
	    Assert.check(parent != null);
	    birthOrder = i;
	}
		
	/** How many sub datums? */
	public int getNumChildren() { return 0 ; }
	
	/** Get a subdatum */
	public Datum getChildAt(int i) { Assert.check(false); return null ; }
	
	/** Get the label of a subdatum. E.g. field name, or subscript */
	public String getChildLabelAt(int i) { Assert.check(false);  return null ; }

// These methods are particular to the kind of Datum

	/** getValueString --- Return a string suitable for display in the expression
	                       window or in a memory window. */
	public abstract String getValueString();
    
    /** perform the default initialization on the datum
     * 
     */
    public abstract void defaultInitialize();
    
    /** get the Class associated with the data represented by the datum
     * 
     */
    public Class getNativeClass(){
    	Assert.unsupported("Native objects");
    	return null;
    }

    /** get the value associated with the data represented by the datum
     * as it would be input to a <code>invoke</code> method of the <code>Method</code> class
     * in <code>java.lang.reflection</code>
     * 
     */

	public Object getNativeValue(){
    	Assert.unsupported("Native objects");
    	return null;

	}
	
	/**
	 * Use the object, as returned by the <code>invoke</code> method of the <code>Method</code> class
     * in <code>java.lang.reflection</code>, to set the value of the datum
	 * @param obj
	 */
	
	public void putNativeValue(Object obj){
		putNativeValue(obj, null);
	}
	
	/**
	 * Use the object, as returned by the <code>invoke</code> method of the <code>Method</code> class
     * in <code>java.lang.reflection</code>, to set the value of the datum
	 * @param obj
	 */
	
	public void putNativeValue(Object obj, VMState vms){
    	Assert.unsupported("Native objects");
	}
}