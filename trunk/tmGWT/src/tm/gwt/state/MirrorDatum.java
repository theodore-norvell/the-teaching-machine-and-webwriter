package tm.gwt.state;

import java.util.Vector ;

import com.google.gwt.user.client.rpc.IsSerializable ;

import tm.interfaces.Datum ;
import tm.interfaces.PointerInterface ;
import tm.interfaces.RegionInterface ;
import tm.interfaces.ScalarInterface ;

public class MirrorDatum implements Datum, IsSerializable {

    private final Vector<String> names = new Vector<String>() ;
    private final Vector<Object> properties = new Vector<Object>() ;

    protected int address;
    final protected Datum parent;
    protected String name ;
    protected String typeString ;
    protected String valueString ;
    protected byte[] bytes ;
    protected int highlight ;
    protected int birthOrder ;
    protected int serialNumber ;
    // Invariant. Either children==null and childLabel==null
    //            or     children==null and childLabel==null and children.length==childLabel.length
    protected MirrorDatum[] children ;
    protected String[] childLabel ;
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
        ms.put( this ) ;
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
        ms.put( this ) ;
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
        ms.put( this ) ;
    }
    
    public void update( Datum d, MirrorStore mirrorStore ) {
        if( d.getSerialNumber() != this.serialNumber ) 
            throw new AssertionError() ;
        this.address = d.getAddress() ;
        this.name = d.getName() ;
        this.typeString = d.getTypeString() ;
        this.valueString = d.getValueString() ;
        int len = d.getNumBytes() ;
        this.bytes = new byte[len] ;
        for( int i=0 ; i < len ; ++i ) this.bytes[i] = (byte)d.getByte( i ) ;
        this.highlight = d.getHighlight() ;
        // birthOrder and parent must be the same
        // The number of children might change, at least for regions.
        if ( d.getNumChildren() == 0 ) {
            this.children = null ; }
        else {
            int childCount = d.getNumChildren() ;
            MirrorDatum[] oldChildren = this.children ;
            int oldCildrenLength = oldChildren==null ? 0 : oldChildren.length ;
            if( childCount != this.children.length ) {
                this.children = new MirrorDatum[ childCount ] ;
                this.childLabel = new String[ childCount ] ; }
            for( int i = 0 ; i < childCount ; ++i ) {
                Datum child = d.getChildAt( i ) ;
                // We reuse the same MirrorDatum object
                // if it has the same serial number as the one we are copying.
                if( i < oldCildrenLength
                && oldChildren[i].serialNumber == child.getSerialNumber() ) {
                    this.children[ i ] = oldChildren[ i ] ;
                    this.children[ i ].update( child, mirrorStore ) ; }
                else {
                    this.children[ i ] = makeMirrorDatum( d, this, mirrorStore ) ; }
                this.childLabel[ i ] = d.getChildLabelAt( i ) ; } }
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
    public void setProperty(String name, Object info) 
    {
        int i ;
        int sz = names.size() ;
        for( i=0 ; i < sz ; ++i ) {
            String nm = names.elementAt(i) ;
            if( nm.equals( name ) ) break ; }
        if( i == sz ) {
            names.addElement( name ) ;
            properties.addElement( info ) ; }
        else {
            properties.setElementAt( info, i ) ; }
    }
    
    @Override
    public Object getProperty(String name)
    {
        int i ;
        int sz = names.size() ;
        for( i=0 ; i < sz ; ++i ) {
            String nm = (String) names.elementAt(i) ;
            if( nm.equals( name ) ) break ; }
        if( i == sz ) {
            return null ; }
        else {
            return properties.elementAt( i ) ; }
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
        
        @Override public void update( Datum d, MirrorStore mirrorStore ) {
            super.update( d, mirrorStore );
            if( !(d instanceof RegionInterface ) ) throw new AssertionError() ;
            this.frameBoundary = ((RegionInterface)d).getFrameBoundary() ;
        }
        
    }
    
    protected static class MirrorScalarDatum extends MirrorDatum implements ScalarInterface {
         protected MirrorScalarDatum( ScalarInterface datum, MirrorDatum parent, MirrorStore ms ) {
             super( datum, parent, ms ) ;
         }
    }
    
    protected static class MirrorPointerDatum extends MirrorDatum implements PointerInterface {
        
        private boolean isNull ;
        private int targetSerialNumber ;
        
        protected MirrorPointerDatum( PointerInterface datum, MirrorDatum parent, MirrorStore ms ) {
            super( datum, parent, ms ) ;
            this.isNull = datum.isNull() ;
            if( isNull ) targetSerialNumber = Integer.MIN_VALUE ;
            else {
                Datum deref = datum.deref() ;
                if( deref == null ) targetSerialNumber = Integer.MIN_VALUE ;
                else targetSerialNumber = deref.getSerialNumber() ;
            }
        }

        @Override public void update( Datum d, MirrorStore mirrorStore ) {
            super.update( d, mirrorStore );
            if( !(d instanceof PointerInterface ) ) throw new AssertionError() ;
            this.isNull = ((PointerInterface)d).isNull() ;
            if( isNull ) this.targetSerialNumber = Integer.MIN_VALUE ;
            else {
                PointerInterface datum = (PointerInterface)d ;
                Datum deref = datum.deref() ;
                if( deref == null ) targetSerialNumber = Integer.MIN_VALUE ;
                else targetSerialNumber = deref.getSerialNumber() ;
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
