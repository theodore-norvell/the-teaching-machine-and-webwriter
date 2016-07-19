package tm.virtualMachine;

import tm.interfaces.Datum ;
import tm.interfaces.TypeInterface ;

public interface VMDatum extends Datum {

    /** @return the type of the data.
    */ 
    public TypeInterface getType();
    //-------------------------------------------------------------------------

}
