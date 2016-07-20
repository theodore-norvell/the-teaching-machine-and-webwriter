package tm.gwt.state;

import java.util.ArrayList ;

import tm.interfaces.Datum ;

public class MirrorDatum implements Datum {

    final protected int address;
    protected Datum parent;
    final protected String name ;
    final protected String typeString ;
    final protected String valueString ;
    final protected byte[] bytes ;
    final protected int highlight ;
    final protected int birthOrder ;
    final protected int serialNumber ;
    final protected MirrorDatum[] children ;
    final protected String[] childLabel ;

    public MirrorDatum( Datum d, MirrorDatum parent ) {
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
                MirrorDatum child = new MirrorDatum( d, thisOrNull ) ;
                children[i] = child ;
                childLabel[i] = d.getChildLabelAt( i ) ;
            }
        }
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

}
