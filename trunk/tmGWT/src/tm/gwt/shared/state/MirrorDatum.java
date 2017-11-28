package tm.gwt.shared.state;

import java.util.ArrayList ;

import com.google.gwt.user.client.rpc.IsSerializable ;

import tm.interfaces.Datum ;
import tm.interfaces.PointerInterface ;
import tm.interfaces.RegionInterface ;
import tm.interfaces.ScalarInterface ;

public class MirrorDatum implements Datum, IsSerializable {

    private ArrayList<String> names = new ArrayList<String>() ;
    private ArrayList<Object> properties = new ArrayList<Object>() ;

    protected int address;
    protected MirrorDatum parent;
    protected String name ;
    protected String typeString ;
    protected String valueString ;
    protected byte[] bytes ;
    protected int highlight ;
    protected int birthOrder ;
    protected int serialNumber ;
    // Invariant chidren.size() == childLabel.size() 
    protected ArrayList<MirrorDatum> children = new ArrayList<MirrorDatum>() ;
    protected ArrayList<String> childLabel = new ArrayList<String>()  ;
    protected MirrorStore store ;
    
    public static MirrorDatum makeMirrorDatum( Datum d, MirrorDatum parent, MirrorStore ms ) {
        // For legacy reasons the children of a region must have null parents.
        if( parent instanceof RegionInterface ) parent = null ;
        
        // Now pick the appropriate constructor.
        if( d instanceof RegionInterface ) return new MirrorRegion((RegionInterface) d, ms ) ;
        else if( d instanceof PointerInterface ) return new MirrorPointerDatum( (PointerInterface)d, parent, ms ) ;
        else if( d instanceof ScalarInterface ) return new MirrorScalarDatum( (ScalarInterface)d, parent, ms ) ;
        else return new MirrorDatum( d, parent, ms ) ;
    }
    
    private MirrorDatum() {}

    private MirrorDatum( Datum d, MirrorDatum parent, MirrorStore ms ) {
        address = d.getAddress() ;
        this.parent = parent ;
        name = d.getName() ;
        typeString = d.getTypeString() ;
        valueString = d.getValueString() ;
        bytes = new byte[d.getNumBytes() ] ;
        for( int i=0 ; i < bytes.length ; ++i ) bytes[i] = (byte) d.getByte( i ) ;
        highlight = d.getHighlight() ;
        if( d.getParent() != null ) birthOrder = d.getBirthOrder() ; else birthOrder = 0 ;
        serialNumber = d.getSerialNumber() ;
        int numChildren = d.getNumChildren() ;
        for( int i = 0 ; i < numChildren ; ++i ) {
            MirrorDatum thisOrNull = (this instanceof MirrorRegion) ? null : this ;
            children.add( makeMirrorDatum( d.getChildAt( i ), thisOrNull, ms ) ) ;
            childLabel.add(  d.getChildLabelAt( i ) ) ;
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
        this.store = ms ;
        ms.put( this ) ;
    }
    
    /** The following constructor is for test purposes only, at the moment.
     * Make a datum with no children. Children can be added later.*/
    public MirrorDatum(
            int address,
            MirrorDatum parent,
            String name ,
            String typeString ,
            String valueString ,
            byte[] bytes ,
            int highlight ,
            int birthOrder ,
            int serialNumber ,
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
        if( this.bytes==null || this.bytes.length != len ) this.bytes = new byte[len] ;
        for( int i=0 ; i < len ; ++i ) this.bytes[i] = (byte)d.getByte( i ) ;
        this.highlight = d.getHighlight() ;
        // birthOrder and parent must be the same.
        if( !(this instanceof RegionInterface) && this.birthOrder != d.getBirthOrder() )
            throw new AssertionError() ;
        // Check that the parents match up.
        if( this.parent == null && d.getParent() != null ) { throw new AssertionError() ; }
        else if ( this.parent != null && d.getParent() == null ) { throw new AssertionError() ; }
        else if ( this.parent != null && d.getParent() != null && this.parent.getSerialNumber() != d.getParent().getSerialNumber() ){
            throw new AssertionError() ; }
        if( this.getNumChildren() == 0 &&  d.getNumChildren() == 0 ) {
            // Nothing to do.
        } else {
            // Recurse on the children.
            // TODO Optimize this to avoid allocating new ArrayLists in some cases.
            
            ArrayList<String> newChildLabels = new ArrayList<String>() ;
            ArrayList<MirrorDatum> oldChildren = this.children ;
            int oldChildCount = oldChildren.size() ;
            ArrayList<MirrorDatum> newChildren = new ArrayList<MirrorDatum>() ;
            int newChildCount = d.getNumChildren() ;
            for( int i = 0 ; i < newChildCount ; ++i ) {
                Datum child = d.getChildAt( i ) ;
                MirrorDatum oldChild =
                        i < oldChildCount ? oldChildren.get( i ) : null ;
                MirrorDatum newChild ;
                // If possible, reuse the same datum object. This ensures
                // that properties persist.
                if( i < oldChildCount
                        && oldChild.serialNumber == child.getSerialNumber() ) {
                    newChild = oldChild ;
                    newChild.update( child, mirrorStore ) ;
                }
                else {
                    newChild = makeMirrorDatum( child, this, mirrorStore ) ;
                }
                newChildren.add( newChild ) ;
                newChildLabels.add( d.getChildLabelAt( i ) ) ;
            }
            this.children = newChildren ;
            this.childLabel = newChildLabels ;
        }
    }
    
    /** The following method is for test purposes only, at the moment. */
    public void addChild( String label, MirrorDatum child, int i) {
        if(    child.parent != null && child.parent != this
            || child.parent == null && !(this instanceof RegionInterface) ) 
            throw new AssertionError( "child has wrong parent") ;
        if( child.birthOrder != i )
            throw new AssertionError( "child has wrong position") ;
        if( i != this.children.size() )
            throw new AssertionError( "child not added to end" ) ;
        
        childLabel.add(  label ) ;
        children.add( child ) ;
    }
    
    /** The following method is for test purposes only, at the moment. */
    public void removeChild( MirrorDatum child) {
        if(    child.parent != null && child.parent != this
            || child.parent == null && !(this instanceof RegionInterface) ) 
            throw new AssertionError( "child has wrong parent") ;
        int i = child.getBirthOrder() ;
        if( i != this.children.size()-1 )
            throw new AssertionError( "only last child can be removed" ) ;
        this.children.remove( i ) ;
        this.childLabel.remove( i ) ;
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
        return children.size() ;
    }

    @Override
    public Datum getChildAt(int i) {
        return children.get( i ) ;
    }

    @Override
    public int getBirthOrder() {
        return birthOrder ;
    }

    @Override
    public String getChildLabelAt(int i) {
        return childLabel.get( i ) ;
    }

    @Override
    public void setProperty(String name, Object info) 
    {
        int i ;
        int sz = names.size() ;
        for( i=0 ; i < sz ; ++i ) {
            String nm = names.get( i ) ;
            if( nm.equals( name ) ) break ; }
        if( i == sz ) {
            names.add( name ) ;
            properties.add( info ) ; }
        else {
            properties.set( i, info ) ; }
    }
    
    @Override
    public Object getProperty(String name)
    {
        int i ;
        int sz = names.size() ;
        for( i=0 ; i < sz ; ++i ) {
            String nm = (String) names.get(i) ;
            if( nm.equals( name ) ) break ; }
        if( i == sz ) {
            return null ; }
        else {
            return properties.get( i ) ; }
    }

    @Override
    public int getSerialNumber() {
        return serialNumber ;
    }
    
    public static class MirrorRegion extends MirrorDatum
    implements RegionInterface, IsSerializable {

        private int frameBoundary ;
        
        private MirrorRegion() { }
        
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
            if( !(d instanceof RegionInterface ) ) throw new AssertionError() ;
            super.update( d, mirrorStore ) ; 
            this.frameBoundary = ((RegionInterface)d).getFrameBoundary() ;
        }
        
    }
    
    protected static class MirrorScalarDatum extends MirrorDatum implements ScalarInterface {
        
        private MirrorScalarDatum() {}
        
        protected MirrorScalarDatum( ScalarInterface datum, MirrorDatum parent, MirrorStore ms ) {
             super( datum, parent, ms ) ;
        }
    }
    
    protected static class MirrorPointerDatum extends MirrorDatum implements PointerInterface {
        
        private boolean isNull ;
        private int targetSerialNumber ;
        
        private MirrorPointerDatum() {} 
        
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
