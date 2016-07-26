package tm.gwt.state;


import com.google.gwt.user.client.rpc.IsSerializable ;

import tm.interfaces.Datum ;
import tm.interfaces.PointerInterface ;
import tm.interfaces.RegionInterface ;
import tm.interfaces.ScalarInterface ;

public class MirrorDatum implements Datum, IsSerializable {

    final protected int address;
    final protected Datum parent;
    final protected String name ;
    final protected String typeString ;
    final protected String valueString ;
    final protected byte[] bytes ;
    final protected int highlight ;
    final protected int birthOrder ;
    final protected int serialNumber ;
    final protected MirrorDatum[] children ;
    final protected String[] childLabel ;
    final protected MirrorStore store ;
    
    public static MirrorDatum makeMirrorDatum( Datum d, MirrorDatum parent, MirrorStore ms ) {
        if( d instanceof RegionInterface ) return new MirrorRegion((RegionInterface) d, ms ) ;
        if( d instanceof PointerInterface ) return new MirrorPointerDatum( (PointerInterface)d, parent, ms ) ;
        else if( d instanceof ScalarInterface ) return new MirrorScalarDatum( (ScalarInterface)d, parent, ms ) ;
        else return new MirrorDatum( d, parent, ms ) ;
    }

    private MirrorDatum( Datum d, MirrorDatum parent, MirrorStore ms ) {
        address = d.getAddress() ;
        this.parent = parent ;
        name = d.getName() ;
        typeString = d.getTypeString() ;
        valueString = d.getValueString() ;
        bytes = new byte[d.getNumBytes() ] ;
        for( int i=0 ; i < bytes.length ; ++i ) bytes[i] = (byte) d.getByte( i ) ;
        highlight = d.getHighlight() ;
        birthOrder = d.getBirthOrder() ;
        serialNumber = d.getSerialNumber() ;
        int numChildren = d.getNumChildren() ;
        if( numChildren == 0 ) {
            children = null ;
            childLabel = null ;
        }
        else {
            children = new MirrorDatum[ numChildren ] ;
            childLabel = new String[ numChildren ] ;
            for( int i = 0 ; i < numChildren ; ++i ) {
                MirrorDatum thisOrNull = (this instanceof MirrorRegion) ? null : this ;
                MirrorDatum child = makeMirrorDatum( d, thisOrNull, ms ) ;
                children[i] = child ;
                childLabel[i] = d.getChildLabelAt( i ) ;
            }
        }
        this.store = ms ;
        ms.put( serialNumber, this ) ;
    }
    
    public MirrorDatum( MirrorStore ms ) {
        address = 0 ;
        parent = null ;
        name = "" ;
        typeString = "" ;
        valueString = "" ;
        bytes = new byte[4] ;
        highlight = PLAIN ;
        birthOrder = 0 ;
        serialNumber = Integer.MAX_VALUE ;
        children = null ;
        childLabel = null ;
        this.store = ms ;
    }
    
    /** The following constructor is for test purposes only, at the moment. */
    public MirrorDatum(
            int address,
            Datum parent,
            String name ,
            String typeString ,
            String valueString ,
            byte[] bytes ,
            int highlight ,
            int birthOrder ,
            int serialNumber ,
            int childCount,
            MirrorStore ms
    ) {
        this.address = address ;
        this.parent = parent ;
        this.name = name ;
        this.typeString = typeString ;
        this.valueString = valueString ;
        this.bytes = bytes ;
        this.highlight = highlight ;
        this.birthOrder = birthOrder ;
        this.serialNumber = serialNumber ;
        if( childCount == 0 ) {
            this.children = null ;
            this.childLabel = null ; }
        else {
            this.children = new MirrorDatum[ childCount ] ;
            this.childLabel = new String[ childCount ] ; }
        this.store = ms ;
    }
    
    /** The following method is for test purposes only, at the moment. */
    public void addChild( String label, MirrorDatum child, int i) {
        if( child.parent != null && child.parent != this || child.parent == null && !(this instanceof RegionInterface) ) 
            throw new AssertionError( "child has wrong parent") ;
        if( child.birthOrder != i ) throw new AssertionError( "child has wrong position") ;
        childLabel[i] = label ;
        children[i] = child ;
    }

    @Override
    public int getHighlight() {
        return highlight ;
    }

    @Override
    public Datum getParent() {
        return parent ;
    }

    @Override
    public String getTypeString() {
        return typeString ;
    }

    @Override
    public String getName() {
        return name ;
    }

    @Override
    public String getValueString() {
        return valueString ;
    }

    @Override
    public int getNumBytes() {
        return bytes.length ;
    }

    @Override
    public int getAddress() {
        return address ;
    }

    @Override
    public int getByte(int i) {
        return bytes[i] ;
    }

    @Override
    public int getNumChildren() {
        if( children == null ) return 0 ;
        else return children.length ;
    }

    @Override
    public Datum getChildAt(int i) {
        return children[ i ] ;
    }

    @Override
    public int getBirthOrder() {
        return birthOrder ;
    }

    @Override
    public String getChildLabelAt(int i) {
        return childLabel[i] ;
    }

    @Override
    public void setProperty(String name, Object info) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object getProperty(String name) {
        // TODO Auto-generated method stub
        return null ;
    }

    @Override
    public int getSerialNumber() {
        return serialNumber ;
    }
    
    protected static class MirrorRegion extends MirrorDatum implements RegionInterface {

        private int frameBoundary ;
        
        protected MirrorRegion( RegionInterface region, MirrorStore ms ) {
            super( region, null, ms ) ;
            frameBoundary = region.getFrameBoundary() ;
        }
        
        public MirrorRegion(MirrorStore ms) {
            super( ms ) ;
        }
        
        @Override
        public int getFrameBoundary() {
            return frameBoundary ;
        }
        
    }
    
    protected static class MirrorScalarDatum extends MirrorDatum implements ScalarInterface {
         protected MirrorScalarDatum( ScalarInterface datum, MirrorDatum parent, MirrorStore ms ) {
             super( datum, parent, ms ) ;
         }
    }
    
    protected static class MirrorPointerDatum extends MirrorDatum implements PointerInterface {
        
        private final boolean isNull ;
        private final int targetSerialNumber ;
        
        protected MirrorPointerDatum( PointerInterface datum, MirrorDatum parent, MirrorStore ms ) {
            super( datum, parent, ms ) ;
            this.isNull = datum.isNull() ;
            if( isNull ) targetSerialNumber = Integer.MIN_VALUE ;
            else {
                Datum d = datum.deref() ;
                if( d == null ) targetSerialNumber = Integer.MIN_VALUE ;
                else targetSerialNumber = d.getSerialNumber() ;
            }
        }

        @Override
        public Datum deref() {
            if( targetSerialNumber == Integer.MIN_VALUE ) return null ;
            else { 
                Datum result = this.store.get( targetSerialNumber ) ;
                if( result == null ) throw new AssertionError( "MirrorPointerDatum.deref()") ;
                return result ;
            }
        }

        @Override
        public boolean isNull() {
            return isNull ;
        }
    }

}
