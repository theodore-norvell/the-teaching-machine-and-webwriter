package tm.gwt.state;

import tm.interfaces.RegionInterface ;

public class MirrorRegion extends MirrorDatum implements RegionInterface {

    private int frameBoundary ;
    
    public MirrorRegion( RegionInterface region ) {
        super( region, null ) ;
        frameBoundary = region.getFrameBoundary() ;
    }
    @Override
    public int getFrameBoundary() {
        return frameBoundary ;
    }
    
}