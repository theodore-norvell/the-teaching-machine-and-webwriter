package tm.clc.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.ast.TypeNode;
import tm.interfaces.ScalarInterface;
import tm.utilities.Assert;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;

public class VoidDatum extends AbstractDatum
									  implements ScalarInterface {

// These methods are common to all scalar Datums
	public VoidDatum(Memory m, TypeNode tp, BTTimeManager timeMan) {
		super(0, 1, null, m, "", tp, timeMan) ;
	}
	
	public String getValueString(){
	    return "no value" ;}

	/* (non-Javadoc)
	 * @see tm.interfaces.Datum#defaultInitialize()
	 */
	public void defaultInitialize() {
		// I don't think this should ever happen.
		Assert.apology("defaultInitialize() executed on void datum") ;
	}
	/**
	 * Use the object, as returned by the <code>invoke</code> method of the <code>Method</code> class
     * in <code>java.lang.reflection</code>, to set the value of the datum
	 * @param obj
	 */
	
	public void putNativeValue(Object obj, VMState vms){
//    	Deliberate nop to override Assert.unsupported("Native objects");
	}

}