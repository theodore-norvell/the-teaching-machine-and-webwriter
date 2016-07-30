package tm.gwt.state;

import java.util.HashMap ;

import tm.interfaces.RegionInterface ;
import tm.interfaces.StoreInterface ;
import tm.virtualMachine.MemRegion ;

public class MirrorStore implements StoreInterface {
    private static final long serialVersionUID = -833023456696499362L ;
    private HashMap<Integer,MirrorDatum> map = new HashMap<Integer,MirrorDatum>() ;
    MirrorDatum.MirrorRegion heapRegion ;
    MirrorDatum.MirrorRegion scratchRegion ;
    MirrorDatum.MirrorRegion stackRegion ;
    MirrorDatum.MirrorRegion staticRegion ;
    
    MirrorStore( RegionInterface heapRegion,
            RegionInterface scratchRegion,
            RegionInterface stackRegion,
            RegionInterface staticRegion) {
        this.heapRegion = (MirrorDatum.MirrorRegion) MirrorDatum.makeMirrorDatum( heapRegion, null, this ) ;
        this.scratchRegion = (MirrorDatum.MirrorRegion) MirrorDatum.makeMirrorDatum( scratchRegion, null, this ) ;
        this.stackRegion = (MirrorDatum.MirrorRegion) MirrorDatum.makeMirrorDatum( stackRegion, null, this ) ;
        this.staticRegion = (MirrorDatum.MirrorRegion) MirrorDatum.makeMirrorDatum( staticRegion, null, this ) ;
    }
    
    MirrorStore( ) {
        this.heapRegion = new MirrorDatum.MirrorRegion(this) ;
        this.scratchRegion = new MirrorDatum.MirrorRegion(this) ;
        this.stackRegion = new MirrorDatum.MirrorRegion(this) ;
        this.staticRegion = new MirrorDatum.MirrorRegion(this) ;
    }
    
    void update( StoreInterface store) {
        this.heapRegion.update( store.getHeap(), this ) ;
        this.scratchRegion.update( store.getScratch(), this ) ;
        this.stackRegion.update( store.getStack(), this ) ;
        this.heapRegion.update( store.getHeap(), this ) ;
    }
    
    MirrorDatum get( int serialNumber ) { return map.get( serialNumber ) ; }
    
    void put( MirrorDatum d ) { map.put( d.getSerialNumber(), d ) ; }

    @Override
    public RegionInterface getStack() {
        return this.stackRegion ;
    }

    @Override
    public RegionInterface getHeap() {
        return this.heapRegion ;
    }

    @Override
    public RegionInterface getStatic() {
        return this.staticRegion  ;
    }

    @Override
    public RegionInterface getScratch() {
        return this.scratchRegion ;
    }
}
