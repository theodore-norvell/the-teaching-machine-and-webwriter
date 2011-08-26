package tm.clc.datum;

import tm.clc.ast.TypeNode;
import tm.interfaces.Datum;
import tm.virtualMachine.Memory;


public interface AbstractArrayDatum
{
    
    abstract public long getNumberOfElements() ;
            
    abstract public AbstractDatum getElement( int i ) ;
}
