package tm.gwt.state;

import java.util.HashMap ;

import tm.interfaces.RegionInterface ;

public class MirrorStore extends HashMap<Integer, MirrorDatum> {
    private static final long serialVersionUID = -833023456696499362L ;
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
    
}
