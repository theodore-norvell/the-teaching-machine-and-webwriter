package tm.clc.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.ast.TypeNode;
import tm.interfaces.Datum;
import tm.interfaces.ScalarInterface;
import tm.utilities.Assert;
import tm.virtualMachine.Memory;

abstract public class AbstractScalarDatum extends tm.clc.datum.AbstractDatum
									  implements ScalarInterface {

// These methods are common to all scalar Datums
	public AbstractScalarDatum(int add, int s, Datum p, Memory m, String name,
	                      TypeNode tp, BTTimeManager timeMan) {
		super(add,s,p, m, name, tp, timeMan);
	}

}
