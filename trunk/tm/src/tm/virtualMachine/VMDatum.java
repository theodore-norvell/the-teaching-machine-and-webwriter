package tm.virtualMachine;

import tm.interfaces.Datum ;
import tm.interfaces.TypeInterface ;

public interface VMDatum extends Datum {

    /** @return the type of the data.
    */ 
    public TypeInterface getType();
    //-------------------------------------------------------------------------

    /** @returns true if Datums are equal - is not equivalent to Object.equals().
     * Datums for which value semantics make sense should implement value equality
    */ 
    public boolean isEqual(Datum another);
    //-------------------------------------------------------------------------

    /** Datum equivalent of clone. i.e. d.copy() != d and d.copy().isEqual(d) should
     * be true.
     * Datums should implement deep copy (or lazy copy)
    */ 
    public Datum copy();
    //-------------------------------------------------------------------------
}
