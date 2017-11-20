package tm.gwt.state;

import java.util.HashMap ;

import com.google.gwt.user.client.rpc.IsSerializable ;

import tm.interfaces.RegionInterface ;
import tm.interfaces.StoreInterface ;
import tm.virtualMachine.MemRegion ;

public class MirrorStore implements StoreInterface, IsSerializable {
    private static final long serialVersionUID = -833023456696499362L ;
    private HashMap<Integer,MirrorDatum> map = new HashMap<Integer,MirrorDatum>() ;
    MirrorDatum.MirrorRegion heapRegion ;
    MirrorDatum.MirrorRegion scratchRegion ;
    MirrorDatum.MirrorRegion stackRegion ;
    MirrorDatum.MirrorRegion staticRegion ;
    
    public MirrorStore( ) {
        this.heapRegion = new MirrorDatum.MirrorRegion(this) ;
        this.scratchRegion = new MirrorDatum.MirrorRegion(this) ;
        this.stackRegion = new MirrorDatum.MirrorRegion(this) ;
        this.staticRegion = new MirrorDatum.MirrorRegion(this) ;
    }
    
    public void update( StoreInterface store ){
        com.google.gwt.core.client.GWT.log(">>> MirrorStore.update") ;
        
        RegionInterface heapRegion = store.getHeap() ;
        RegionInterface scratchRegion = store.getScratch() ;
        RegionInterface stackRegion = store.getStack() ;
        RegionInterface staticRegion = store.getStatic() ;
        
        // Since the regions never change, they should have the same serial numbers,
        // even if they don't.
        this.heapRegion.serialNumber = heapRegion.getSerialNumber() ;
        this.scratchRegion.serialNumber = scratchRegion.getSerialNumber() ;
        this.stackRegion.serialNumber = stackRegion.getSerialNumber() ;
        this.staticRegion.serialNumber = staticRegion.getSerialNumber() ;
        this.heapRegion.update( heapRegion, this ) ;
        this.scratchRegion.update( scratchRegion, this ) ;
        this.stackRegion.update( stackRegion, this ) ;
        this.staticRegion.update( staticRegion, this ) ;
        com.google.gwt.core.client.GWT.log("<<< MirrorStore.update") ;
    }
    
    MirrorDatum get( int serialNumber ) { return map.get( serialNumber ) ; }
    
    void put( MirrorDatum d ) {
        if( d.store != this ) { throw new AssertionError() ; }
        map.put( d.getSerialNumber(), d ) ; }

    @Override
    public MirrorDatum.MirrorRegion getStack() {
        return this.stackRegion ;
    }

    @Override
    public MirrorDatum.MirrorRegion getHeap() {
        return this.heapRegion ;
    }

    @Override
    public MirrorDatum.MirrorRegion getStatic() {
        return this.staticRegion  ;
    }

    @Override
    public MirrorDatum.MirrorRegion getScratch() {
        return this.scratchRegion ;
    }
}
