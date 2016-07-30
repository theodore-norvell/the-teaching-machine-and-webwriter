package tm.interfaces;

import tm.virtualMachine.MemRegion ;

public interface StoreInterface {

    public RegionInterface getStack() ;

    public RegionInterface getHeap() ;

    public RegionInterface getStatic() ;
    
    public RegionInterface getScratch() ;

}
